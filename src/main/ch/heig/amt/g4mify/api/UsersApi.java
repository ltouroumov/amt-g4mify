package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.User;
import ch.heig.amt.g4mify.model.view.View;
import ch.heig.amt.g4mify.model.view.user.UserDetail;
import ch.heig.amt.g4mify.model.view.user.UserSummary;
import ch.heig.amt.g4mify.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ldavid
 * @created 11/14/16
 */
@RestController
@RequestMapping("/api/users")
public class UsersApi extends AbstractDomainApi {

    @Autowired
    private UsersRepository usersRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserSummary>> index(@RequestParam("domain") long domainId,
                                                   @RequestParam(required = false, defaultValue = "0") long page,
                                                   @RequestParam(required = false, defaultValue = "50") long pageSize) {

        Domain domain = getDomain(domainId);
        List<UserSummary> users = usersRepository.findByDomain(domain)
                .stream()
                .skip(page * pageSize)
                .limit(pageSize)
                .map(user -> View.to(UserSummary.class, user))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestParam("domain") long domainId,
                                    @RequestBody UserSummary body) {

        Domain domain = getDomain(domainId);
        User input = View.to(User.class, body);
        input.setDomain(domain);

        User user = usersRepository.save(input);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(uri).body(input);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<UserDetail> show(@PathVariable long id) {
        UserDetail user = View.to(UserDetail.class, usersRepository.findOne(id));
        return ResponseEntity.ok(user);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserDetail> update(@PathVariable long id,
                                             @RequestBody UserDetail body) {
        return ResponseEntity.ok(body);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        return ResponseEntity.ok(null);
    }

}
