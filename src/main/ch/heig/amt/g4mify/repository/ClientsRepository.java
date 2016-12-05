package ch.heig.amt.g4mify.repository;

import ch.heig.amt.g4mify.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author ldavid
 * @created 12/5/16
 */
@Component
public interface ClientsRepository extends JpaRepository<Client, Long> {

}
