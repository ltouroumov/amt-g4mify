package ch.heig.amt.g4mify.repository;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Component
public interface UsersRepository extends JpaRepository<User, Long> {

    List<User> findByDomain(Domain domain);

}
