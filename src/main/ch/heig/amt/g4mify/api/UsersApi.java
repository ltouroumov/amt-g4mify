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
@RequestMapping("/{domainId}/users")
public class UsersApi {

    @Autowired
    private DomainsRepository domainsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> index(@PathVariable int domainId,
                                            @RequestParam(required = false, defaultValue = "0") long page,
                                            @RequestParam(required = false, defaultValue = "50") long pageSize) {

        Domain domain = domainsRepository.findOne(domainId);
        List<User> users = usersRepository.findByDomain(domain)
                .stream()
                .skip(page * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@PathVariable int domainId, @RequestBody User input) {

        Domain domain = domainsRepository.findOne(domainId);

        input.setDomain(domain);
        User user = usersRepository.save(input);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getUsername())
                .toUri();

        return ResponseEntity.created(uri).body(user);
    }

}
