package ch.heig.amt.g4mify.repository;

import ch.heig.amt.g4mify.model.Badge;
import ch.heig.amt.g4mify.model.BadgeType;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Frederic on 12.12.16.
 */
public interface BadgesRepository extends JpaRepository<Badge, Long> {

    Optional<Badge> findByUserAndType(User user, BadgeType badgeType);

}
