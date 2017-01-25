package ch.heig.amt.g4mify.repository;

import ch.heig.amt.g4mify.model.Counter;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.BadgeRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Frederic on 14.12.16.
 */
public interface BadgeRulesRepository extends JpaRepository<BadgeRule, Long> {

    Stream<BadgeRule> findByDomain(Domain domain);

    @Query("select r from #{#entityName} r join r.depends dep where r.domain = ?1 and dep = ?2")
    List<BadgeRule> findByDomainAndCounter(Domain domain, Counter counter);

}
