package ch.heig.amt.g4mify.repository;

import ch.heig.amt.g4mify.model.BadgeType;
import ch.heig.amt.g4mify.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Frederic on 12.12.16.
 */
public interface BadgeTypesRepository extends JpaRepository<BadgeType, Long> {

    Stream<BadgeType> findByDomain(Domain domain);

    Optional<BadgeType> findById(long id);

    Optional<BadgeType> findByDomainAndKey(Domain domain, String key);

}
