package ch.heig.amt.g4mify.repository;

import ch.heig.amt.g4mify.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Component
public interface DomainsRepository extends JpaRepository<Domain, Long> {

    Domain findByName(String name);

}
