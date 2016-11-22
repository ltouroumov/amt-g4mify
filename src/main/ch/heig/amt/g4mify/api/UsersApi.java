package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.User;
import ch.heig.amt.g4mify.repository.DomainsRepository;
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
@RequestMapping("/users")
public class UsersApi extends BaseDomainApi {

    @Autowired
    private UsersRepository usersRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> index(@RequestParam long domain,
                                            @RequestParam(required = false, defaultValue = "0") long page,
                                            @RequestParam(required = false, defaultValue = "50") long pageSize) {

        Domain _domain = getDomain(domain);
        List<User> users = usersRepository.findByDomain(_domain)
                .stream()
                .skip(page * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody User input) {

        User user = usersRepository.save(input);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(uri).body(user);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<User> show(@PathVariable long id) {
        User user = usersRepository.findOne(id);
        return ResponseEntity.ok(user);
    }

}
