package ch.heig.amt.g4mify.actors;

import akka.actor.UntypedActor;
import ch.heig.amt.g4mify.actors.messages.ReceivedEvent;
import ch.heig.amt.g4mify.api.ApiException;
import ch.heig.amt.g4mify.dsl.Award;
import ch.heig.amt.g4mify.dsl.Changeset;
import ch.heig.amt.g4mify.dsl.CounterUpdate;
import ch.heig.amt.g4mify.model.*;
import ch.heig.amt.g4mify.repository.*;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ldavid
 * @created 11/21/16
 */
@Component
@Scope("prototype")
public class EventActor extends UntypedActor {

    private static Logger LOG = Logger.getLogger(EventActor.class.getSimpleName());

    private final EventsRepository eventsRepository;

    private final CountersRepository countersRepository;

    private final MetricsRepository metricsRepository;

    private final BucketsRepository bucketsRepository;

    private final BadgeTypesRepository badgeTypesRepository;

    private final BadgesRepository badgesRepository;

    private final BadgeRulesRepository badgeRulesRepository;

    private final EntityManager em;

    @Autowired
    public EventActor(EventsRepository eventsRepository, CountersRepository countersRepository, MetricsRepository metricsRepository, BucketsRepository bucketsRepository, BadgeTypesRepository badgeTypesRepository, BadgesRepository badgesRepository, BadgeRulesRepository badgeRulesRepository, EntityManager em) {
        this.eventsRepository = eventsRepository;
        this.countersRepository = countersRepository;
        this.metricsRepository = metricsRepository;
        this.bucketsRepository = bucketsRepository;
        this.badgeTypesRepository = badgeTypesRepository;
        this.badgesRepository = badgesRepository;
        this.badgeRulesRepository = badgeRulesRepository;
        this.em = em;
    }

    @Override
    public void onReceive(Object message) throws Throwable {

        if (message instanceof ReceivedEvent) {
            processEvent((ReceivedEvent) message);
        } else {
            throw new Exception("EventActor received unkown message type");
        }

    }

    @Transactional
    private void processEvent(ReceivedEvent message) throws InterruptedException {
        Event event = eventsRepository.findOne(message.getEventId());
        User user = event.getUser();

        TypedQuery<EventRule> query = em.createNamedQuery("EventRule.FindByTypesInDomain", EventRule.class);
        query.setParameter(1, event.getType());
        query.setParameter(2, event.getUser().getDomain().getId());

        List<EventRule> eventRules = query.getResultList();

        Changeset totalChanges = new Changeset();
        for (EventRule rule : eventRules) {
            Changeset ruleChanges = processEventRule(rule, event);
            totalChanges.merge(ruleChanges);
        }

        Set<Counter> updatedCounters = applyChangeset(user, totalChanges);
        Set<BadgeRule> badgeRules = new HashSet<>();
        for (Counter counter : updatedCounters) {
            List<BadgeRule> rules = badgeRulesRepository.findByDomainAndCounter(user.getDomain(), counter);
            badgeRules.addAll(rules);
        }

        for (BadgeRule rule : badgeRules) {
            processBadgeRule(user, rule);
        }

        event.setProcessed(Timestamp.from(Instant.now()));
        eventsRepository.save(event);
    }

    private Changeset processEventRule(EventRule rule, Event event) {
        Binding binding = new Binding();
        binding.setVariable("event", event);

        CompilerConfiguration configuration = new CompilerConfiguration();
        configuration.setScriptBaseClass("ch.heig.amt.g4mify.dsl.EventRuleScript");

        //TODO: Implement basic security -_-
        //TODO: Setup a secure class loader
        GroovyShell shell = new GroovyShell(getClass().getClassLoader(), binding, configuration);
        return (Changeset)shell.evaluate(rule.getScript());
    }

    private void processBadgeRule(User user, BadgeRule rule) {
        Binding binding = new Binding();

        CompilerConfiguration configuration = new CompilerConfiguration();
        configuration.setScriptBaseClass("ch.heig.amt.g4mify.dsl.BadgeRuleScript");

        //TODO: Implement basic security -_-
        //TODO: Setup a secure class loader
        GroovyShell shell = new GroovyShell(getClass().getClassLoader(), binding, configuration);
        return (Changeset)shell.evaluate(rule.getScript());
    }

    private Set<Counter> applyChangeset(User user, Changeset changes) {
        // Update buckets
        Set<Counter> updatedCounters = new HashSet<>();
        for (CounterUpdate update : changes.updates.values()) {
            Counter updated = updateBucket(user, update);
            updatedCounters.add(updated);
        }

        // Award badges
        for (Award award : changes.awards) {
            awardBadge(user, award);
        }

        return updatedCounters;
    }

    private void awardBadge(User user, Award award) {
        BadgeType badgeType = badgeTypesRepository.findByDomainAndKey(user.getDomain(), award.getId())
                .orElseThrow(() -> new ApiException("Could not find badge type " + award.getId()));

        boolean ok;
        do {
            Optional<Badge> instance = badgesRepository.findByUserAndType(user, badgeType);
            Badge badge = null;
            if (!instance.isPresent()) {
                // If the badge has never been awarded
                // then create and add badge

                badge = new Badge();
                badge.setUser(user);
                badge.setType(badgeType);
                badge.setLevel(1);
                badge.setAwarded(Timestamp.from(Instant.now()));
            } else if (instance.isPresent() && !badgeType.isSingleton()) {
                // If the badge has already been awarded AND the badge-type is not a singleton
                // then increase badge level

                badge = instance.get();
                badge.setLevel(badge.getLevel() + 1);
            }

            if (badge != null) {
                try {
                    badgesRepository.save(badge);
                    ok = true;
                } catch (OptimisticLockException ex) {
                    LOG.warning("Concurrent Badge Update, retrying");
                    ok = false;
                }
            } else {
                ok = true;
            }
        } while (!ok);
    }

    @Transactional
    private Counter updateBucket(User user, CounterUpdate update) {
        Metric metric = findMetric(user.getDomain(), update.counter);

        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime last15 = now.minus(Duration.ofMinutes(15));
        ZonedDateTime at15 = now.with(ChronoField.MINUTE_OF_HOUR, (now.get(ChronoField.MINUTE_OF_HOUR) / 15) * 15);

        boolean ok;
        do {
            Bucket lastBucket = bucketsRepository.findBucketForUpdate(user, metric, last15.toInstant().getEpochSecond())
                    .orElseGet(() -> {
                        Bucket theBucket = new Bucket();
                        theBucket.setUser(user);
                        theBucket.setMetric(metric);
                        theBucket.setValue(0);
                        theBucket.setVersion(0);
                        theBucket.setTime(at15.toInstant().getEpochSecond());

                        return theBucket;
                    });

            if (update.set) {
                lastBucket.setValue(update.amount);
            } else {
                lastBucket.setValue(lastBucket.getValue() + update.amount);
            }
            try {
                bucketsRepository.save(lastBucket);
                ok = true;
            } catch (OptimisticLockException ex) {
                LOG.warning("Concurrent Bucket Update, retrying");
                ok = false;
            }
        } while (!ok);

        return metric.getCounter();
    }

    private static final Pattern counterPattern = Pattern.compile("^(\\w+)(.(\\w+))?$");

    private Metric findMetric(Domain domain, String counterStr) {
        Matcher matcher = counterPattern.matcher(counterStr);
        if (matcher.matches()) {
            String counterName = matcher.group(1);
            String metricName = Optional.ofNullable(matcher.group(3)).orElse("total");

            return countersRepository.findByDomainAndName(domain, counterName)
                    .map(counter -> metricsRepository.findByCounterAndName(counter, metricName)
                            .orElseThrow(() -> new ApiException("Metric not found " + metricName + " for counter " + counterName)))
                    .orElseThrow(() -> new ApiException("Counter not found " + counterName));
        } else {
            throw new ApiException("Counter reference is badly formatted");
        }
    }

}
