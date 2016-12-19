package ch.heig.amt.g4mify.actors;

import akka.actor.UntypedActor;
import ch.heig.amt.g4mify.actors.messages.ReceivedEvent;
import ch.heig.amt.g4mify.api.ApiException;
import ch.heig.amt.g4mify.model.*;
import ch.heig.amt.g4mify.repository.BucketsRepository;
import ch.heig.amt.g4mify.repository.CountersRepository;
import ch.heig.amt.g4mify.repository.EventsRepository;
import ch.heig.amt.g4mify.repository.MetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.Optional;
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

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private CountersRepository countersRepository;

    @Autowired
    private MetricsRepository metricsRepository;

    @Autowired
    private BucketsRepository bucketsRepository;

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

        for (CounterUpdate update : event.getUpdates()) {
            updateBucket(user, update);
        }

        event.setProcessed(Timestamp.from(Instant.now()));
        eventsRepository.save(event);
    }

    @Transactional
    private void updateBucket(User user, CounterUpdate update) {
        Metric metric = findMetric(user.getDomain(), update.getCounter());

        Instant now = Instant.now();
        Instant last15 = now.minus(Duration.ofMinutes(15));
        Instant at15 = now.with(ChronoField.MINUTE_OF_HOUR, (now.get(ChronoField.MINUTE_OF_HOUR) / 15) * 15);

        boolean ok;
        do {
            Bucket lastBucket = bucketsRepository.findBucketForUpdate(user, metric, last15.getEpochSecond())
                    .orElseGet(() -> {
                        Bucket theBucket = new Bucket();
                        theBucket.setUser(user);
                        theBucket.setMetric(metric);
                        theBucket.setValue(0);
                        theBucket.setVersion(0);
                        theBucket.setTime(at15.getEpochSecond());

                        return theBucket;
                    });

            lastBucket.setValue(lastBucket.getValue() + update.getAmount());
            try {
                bucketsRepository.save(lastBucket);
                ok = true;
            } catch (OptimisticLockException ex) {
                LOG.warning("Concurrent Bucket Update, retrying");
                ok = false;
            }
        } while (!ok);
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
