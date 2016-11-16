package ch.heig.amt.g4mify.repository;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ldavid
 * @created 11/14/16
 */
public interface UsersRepository extends JpaRepository<User, String> {

    List<User> findByDomain(Domain domain);

}
