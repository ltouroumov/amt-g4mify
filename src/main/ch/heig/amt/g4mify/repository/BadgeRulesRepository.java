package ch.heig.amt.g4mify.repository;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.BadgeRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

/**
 * Created by Frederic on 14.12.16.
 */
public interface BadgeRulesRepository extends JpaRepository<BadgeRule, Long> {

    Stream<BadgeRule> findByDomain(Domain domain);

}
