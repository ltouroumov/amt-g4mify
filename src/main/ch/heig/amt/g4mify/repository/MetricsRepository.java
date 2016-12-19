package ch.heig.amt.g4mify.repository;

import ch.heig.amt.g4mify.model.Counter;
import ch.heig.amt.g4mify.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author ldavid
 * @created 12/12/16
 */
public interface MetricsRepository extends JpaRepository<Metric, Long>{

    List<Metric> findByCounter(Counter counter);

    Optional<Metric> findByCounterAndName(Counter counter, String metricName);

}
