package ch.heig.amt.g4mify.repository;

import ch.heig.amt.g4mify.model.Bucket;
import ch.heig.amt.g4mify.model.Metric;
import ch.heig.amt.g4mify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author ldavid
 * @created 12/14/16
 */
public interface BucketsRepository extends JpaRepository<Bucket, Long> {

    Stream<Bucket> findByUserAndMetricOrderByTimeDesc(User user, Metric metric);

    @Query("select b from Bucket b where b.user = ?1 and b.metric = ?2 and b.time >= ?3")
    Optional<Bucket> findBucketForUpdate(User user, Metric metric, long time);

}
