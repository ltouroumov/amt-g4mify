package ch.heig.amt.g4mify.actors;

import ch.heig.amt.g4mify.actors.messages.ReceivedEvent;
import ch.heig.amt.g4mify.api.ApiException;
import ch.heig.amt.g4mify.dsl.BadgeRuleEvaluator;
import ch.heig.amt.g4mify.dsl.EventRuleEvaluator;
import ch.heig.amt.g4mify.dsl.api.*;
import ch.heig.amt.g4mify.model.*;
import ch.heig.amt.g4mify.repository.*;
import ch.heig.amt.g4mify.util.CounterAggregate;
import ch.heig.amt.g4mify.util.CounterAggregator;
import ch.heig.amt.g4mify.util.CounterSpecResolver;
import ch.heig.amt.g4mify.util.KeyedLock;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

/**
 * @author ldavid
 * @created 1/20/17
 */
@Component
@Transactional
public class EventProcessor {

    private static Logger LOG = Logger.getLogger(EventActor.class.getSimpleName());

    private final EventsRepository eventsRepository;

    private final BucketsRepository bucketsRepository;

    private final BadgeTypesRepository badgeTypesRepository;

    private final BadgesRepository badgesRepository;

    private final BadgeRulesRepository badgeRulesRepository;

    private final EntityManager em;

    private final CounterSpecResolver counterSpecResolver;

    private final EventRuleEvaluator eventRuleEvaluator;

    private final CounterAggregator counterAggregator;

    private final BadgeRuleEvaluator badgeRuleEvaluator;

    @Autowired
    public EventProcessor(EventsRepository eventsRepository,
                      BucketsRepository bucketsRepository,
                      BadgeTypesRepository badgeTypesRepository,
                      BadgesRepository badgesRepository,
                      BadgeRulesRepository badgeRulesRepository,
                      EntityManager em,
                      CounterSpecResolver counterSpecResolver,
                      CounterAggregator counterAggregator) {
        this.eventsRepository = eventsRepository;
        this.bucketsRepository = bucketsRepository;
        this.badgeTypesRepository = badgeTypesRepository;
        this.badgesRepository = badgesRepository;
        this.badgeRulesRepository = badgeRulesRepository;
        this.em = em;
        this.counterSpecResolver = counterSpecResolver;
        this.counterAggregator = counterAggregator;
        this.eventRuleEvaluator = new EventRuleEvaluator();
        this.badgeRuleEvaluator = new BadgeRuleEvaluator();
    }

    public void process(ReceivedEvent message) throws InterruptedException {
        Event event = eventsRepository.findOne(message.getEventId());
        User user = event.getUser();

        TypedQuery<EventRule> query = em.createNamedQuery("EventRule.FindByTypesInDomain", EventRule.class);
        query.setParameter(1, event.getType());
        query.setParameter(2, event.getUser().getDomain().getId());

        List<EventRule> eventRules = query.getResultList();

        Changeset totalChanges = new Changeset();
        for (EventRule rule : eventRules) {
            Changeset ruleChanges = eventRuleEvaluator.evaluate(rule, event);
            totalChanges.merge(ruleChanges);
        }

        Set<Counter> updatedCounters = applyChangeset(user, totalChanges);

        Set<BadgeRule> badgeRules = new HashSet<>();
        for (Counter counter : updatedCounters) {
            List<BadgeRule> rules = badgeRulesRepository.findByDomainAndCounter(user.getDomain(), counter);
            badgeRules.addAll(rules);
        }

        for (BadgeRule rule : badgeRules) {
            boolean result = evaluateBadgeRule(user, rule);

            if (result) {
                awardBadge(user, rule.getGrants());
            }
        }

        event.setProcessed(Instant.now().getEpochSecond());
        eventsRepository.save(event);
    }

    public boolean evaluateBadgeRule(User user, BadgeRule rule) {
        Expression expr = badgeRuleEvaluator.evaluate(rule);

        List<CounterAggregate> dependencies = new ArrayList<>();
        for (Counter dependency : rule.getDepends()) {
            CounterAggregate aggregate = counterAggregator.aggregate(dependency, user);
            dependencies.add(aggregate);
        }

        Context ctx = new Context(dependencies);
        return expr.eval(ctx);
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
        awardBadge(user, badgeType);
    }

    private void awardBadge(User user, BadgeType badgeType) {
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
                badge.setAwarded(Instant.now().getEpochSecond());
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

    private KeyedLock bucketUpdateLock = new KeyedLock();

    @Transactional
    private Counter updateBucket(User user, CounterUpdate update) {
        Metric metric = counterSpecResolver.findMetric(user.getDomain(), update.counter);

        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime last15 = now.minus(Duration.ofMinutes(15));
        ZonedDateTime at15 = now.with(ChronoField.MINUTE_OF_HOUR, (now.get(ChronoField.MINUTE_OF_HOUR) / 15) * 15);

        Lock metricLock = bucketUpdateLock.get(update.counter);

        try {
            metricLock.lock();

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

            bucketsRepository.save(lastBucket);
        } finally {
            metricLock.unlock();
        }

        return metric.getCounter();
    }

}
