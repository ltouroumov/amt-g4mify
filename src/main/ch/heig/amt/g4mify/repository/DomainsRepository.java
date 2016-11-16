package ch.heig.amt.g4mify.repository;

import ch.heig.amt.g4mify.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ldavid
 * @created 11/14/16
 */
public interface DomainsRepository extends JpaRepository<Domain, Integer> {

}
