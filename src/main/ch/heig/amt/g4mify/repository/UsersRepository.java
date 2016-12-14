package ch.heig.amt.g4mify.repository;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Component
public interface UsersRepository extends JpaRepository<User, Long> {

    Stream<User> findByDomain(Domain domain);

    Optional<User> findByDomainAndUsername(Domain domain, String username);

}
