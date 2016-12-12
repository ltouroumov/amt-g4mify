package ch.heig.amt.g4mify.api;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import ch.heig.amt.g4mify.actors.messages.ReceivedEvent;
import ch.heig.amt.g4mify.extension.SpringExtension;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.Event;
import ch.heig.amt.g4mify.model.User;
import ch.heig.amt.g4mify.model.view.event.EventInfos;
import ch.heig.amt.g4mify.model.view.event.EventResult;
import ch.heig.amt.g4mify.model.view.event.EventSubmit;
import ch.heig.amt.g4mify.model.view.event.EventSummary;
import ch.heig.amt.g4mify.model.view.user.UserSummary;
import ch.heig.amt.g4mify.repository.EventsRepository;
import ch.heig.amt.g4mify.repository.UsersRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static ch.heig.amt.g4mify.model.view.ViewUtils.*;

/**
 * @author ldavid
 * @created 11/21/16
 */
@RestController
@RequestMapping("/api/events")
@Api(value = "events", description = "Received events for users")
public class EventsApi extends AbstractDomainApi {

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ActorSystem system;

    @Autowired
    private SpringExtension ext;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Obtain events for a user")
    public ResponseEntity<EventInfos> get(@RequestParam(name = "user") long userId,
                                 @RequestParam(required = false, defaultValue = "0") long page,
                                 @RequestParam(required = false, defaultValue = "50") long pageSize) {
        Domain domain = getDomain();
        User user = usersRepository.findOne(userId);

        if (user.getDomain().getId() != domain.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<EventSummary> events = eventsRepository.findByUser(user)
                .skip(page * pageSize)
                .limit(pageSize)
                .map(outputView(EventSummary.class)::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new EventInfos(
                outputView(UserSummary.class).from(user),
                events
        ));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Publish an event to a user")
    public ResponseEntity<EventResult> post(@RequestBody EventSubmit input) {
        Domain domain = getDomain();
        User user = usersRepository.findOne(input.user);

        if (user.getDomain().getId() != domain.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Event event = inputView(Event.class).ignore("user").from(input);
        event.setUser(user);
        event.setReceived(Timestamp.from(Instant.now()));

        eventsRepository.save(event);

        ActorRef actor = system.actorOf(ext.props("eventActor"));
        actor.tell(new ReceivedEvent(event.getId()), null);

        return ResponseEntity.ok(outputView(EventResult.class).map("user", viewMap(UserSummary.class)).from(event));
    }

}
