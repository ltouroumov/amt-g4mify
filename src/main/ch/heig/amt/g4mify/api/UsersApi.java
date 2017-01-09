package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Badge;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.User;
import ch.heig.amt.g4mify.model.view.user.UserDetail;
import ch.heig.amt.g4mify.model.view.user.UserSummary;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ch.heig.amt.g4mify.model.view.ViewUtils.*;

/**
 * @author ldavid
 * @created 11/14/16
 */
@RestController
@RequestMapping("/api/users")
@Api(value = "users", description = "Handles CRUD operations on users")
public class UsersApi extends AbstractDomainApi {

    @Autowired
    private UsersRepository usersRepository;


    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves all users from the domain", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error retrieving users from the domain")})
    public ResponseEntity<List<UserSummary>> index(@RequestParam(required = false, defaultValue = "0") long page,
                                                   @RequestParam(required = false, defaultValue = "50") long pageSize) {

        Domain domain = getDomain();
        List<UserSummary> users = usersRepository.findByDomain(domain)
                .skip(page * pageSize)
                .limit(pageSize)
                .map(outputView(UserSummary.class)::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new user in the domain", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error creating user in the domain")})
    public ResponseEntity<?> create(@RequestBody UserSummary body) {

        Domain domain = getDomain();
        User input = inputView(User.class).from(body);
        input.setDomain(domain);

        Optional<User> userOpt = usersRepository.findByDomainAndProfileId(domain, body.profileId);

        if (userOpt.isPresent()) {
            throw new ApiException("ProfileID '" + body.profileId + "' is already in use'");
        } else {
            User user = usersRepository.save(input);
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(user.getId())
                    .toUri();

            return ResponseEntity.created(uri).body(outputView(UserSummary.class).from(user));
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{pid}")
    @ApiOperation(value = "Retrieves a particular user from the domain", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error retrieving the user from the domain")})
    public ResponseEntity<UserDetail> show(@PathVariable String pid) {
        User user = usersRepository.findByProfileId(pid);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (canAccess(user)) {
            return ResponseEntity.ok(outputView(UserDetail.class).from(user));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{pid}")
    @ApiOperation(value = "Updates a particular user in the domain",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error updating the user in the domain")})
    public ResponseEntity<UserDetail> update(@PathVariable String pid,
                                             @RequestBody UserDetail body) {
        User user = usersRepository.findByProfileId(pid);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (canAccess(user)) {
            updateView(user).with(body);
            usersRepository.save(user);
            return ResponseEntity.ok(outputView(UserDetail.class).from(user));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{pid}")
    @ApiOperation(value = "Deletes a particular user from the domain")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error deleting the user from the domain")})
    public ResponseEntity<?> delete(@PathVariable String pid) {
        User user = usersRepository.findByProfileId(pid);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (canAccess(user)) {
            usersRepository.delete(user);
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping( method = RequestMethod.GET, path = "/{pid}/badges")
    @ApiOperation(value = "Retrieves all badges from a particular user in the domain")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error retrieving badges from a particular user in the domain")})
    public ResponseEntity<List<Badge>> index(@PathVariable String pid,
                                             @RequestParam(required = false, defaultValue = "0") long page,
                                             @RequestParam(required = false, defaultValue = "50") long pageSize) {
        User user = usersRepository.findByProfileId(pid);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (canAccess(user)) {
            List<Badge> badges = user.getBadges()
                    .stream()
                    .skip(page * pageSize)
                    .limit(pageSize)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(badges);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private boolean canAccess(User user) {
        return user.getDomain().getId() == getDomain().getId();
    }
}
