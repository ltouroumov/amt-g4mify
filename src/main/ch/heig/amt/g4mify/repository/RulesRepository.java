package ch.heig.amt.g4mify.repository;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

/**
 * Created by Frederic on 14.12.16.
 */
public interface RulesRepository extends JpaRepository<Rule, Long> {

    Stream<Rule> findByDomain(Domain domain);

}
