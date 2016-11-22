package ch.heig.amt.g4mify.repository;

import ch.heig.amt.g4mify.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author ldavid
 * @created 11/21/16
 */
@Component
public interface EventsRepository extends JpaRepository<Event, Long> {
}
