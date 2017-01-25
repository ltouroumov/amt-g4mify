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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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
@Api(value = "events", description = "Handles events from users")
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
    @ApiOperation(value = "Obtain events for a user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error obtaining event from a user in the domain")})
    @Transactional
    public ResponseEntity<EventInfos> get(@RequestParam(name = "user") String userId,
                                 @RequestParam(required = false, defaultValue = "0") long page,
                                 @RequestParam(required = false, defaultValue = "50") long pageSize) {
        Domain domain = getDomain();
        User user = usersRepository.findByDomainAndProfileId(domain, userId).orElse(null);

        if (user == null || user.getDomain().getId() != domain.getId()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
    @ApiOperation(value = "Publishes an event to a user",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error publishing event in the domain")})
    public ResponseEntity<EventResult> post(@RequestBody EventSubmit input) {

        Domain domain = getDomain();
        Event event = inputView(Event.class)
                .map("user", profileId -> usersRepository.findByDomainAndProfileId(domain, (String) profileId)
                        .orElseGet(() -> {
                            User user = new User();
                            user.setDomain(domain);
                            user.setProfileId((String) profileId);
                            user.setProfileUrl("http://example.com");

                            return usersRepository.save(user);
                        }))
                .from(input);

        event.setReceived(Instant.now().getEpochSecond());

        if (event.getUser().getDomain().getId() != domain.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        eventsRepository.save(event);

        ActorRef actor = system.actorOf(ext.props("eventActor"));
        actor.tell(new ReceivedEvent(event.getId()), null);

        return ResponseEntity.ok(outputView(EventResult.class).map("user", viewMap(UserSummary.class)).from(event));
    }

}
