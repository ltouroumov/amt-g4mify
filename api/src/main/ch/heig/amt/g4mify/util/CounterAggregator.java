package ch.heig.amt.g4mify.util;

import ch.heig.amt.g4mify.model.Counter;
import ch.heig.amt.g4mify.model.Metric;
import ch.heig.amt.g4mify.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.logging.Logger;

/**
 * @author ldavid
 * @created 1/9/17
 */
@Component
public class CounterAggregator {

    private static final Logger LOG = Logger.getLogger(CounterAggregator.class.getSimpleName());

    @Autowired
    private EntityManager em;

    @Transactional
    public CounterAggregate aggregate(Counter counter, User user) {
        CounterAggregate aggregate = new CounterAggregate(counter.getName());

        for (Metric metric : counter.getMetrics()) {
            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            long start = 0;
            if (metric.getDuration() > 0) {
                start = now.minus(Duration.ofMinutes(metric.getDuration())).toInstant().getEpochSecond();
            }

            Query query = em.createQuery("SELECT SUM(b.value) FROM Bucket b WHERE b.metric = ?1 AND b.user = ?2 AND b.time > ?3", Long.class);
            query.setParameter(1, metric);
            query.setParameter(2, user);
            query.setParameter(3, start);

            long value = (Long)query.getSingleResult();
            aggregate.setMetric(metric.getName(), value);
        }

        return aggregate;
    }

}
