package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.repository.DomainsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author ldavid
 * @created 11/14/16
 */
@RestController
@RequestMapping("/domains")
public class DomainsApi {

    private static Logger LOG = Logger.getLogger(DomainsApi.class.getSimpleName());

    @Autowired
    private DomainsRepository domainsRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Domain>> index(@RequestParam(required = false, defaultValue = "0") long page,
                                              @RequestParam(required = false, defaultValue = "50") long pageSize) {

        List<Domain> domains = domainsRepository.findAll().stream()
                .skip(page * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        return ResponseEntity.ok(domains);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody Domain input) {

        Domain domain = domainsRepository.save(input);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(domain.getId())
                .toUri();

        return ResponseEntity.created(uri).body(domain);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Domain> show(@PathVariable int id) {
        Domain domain = domainsRepository.findOne(id);

        return ResponseEntity.ok(domain);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Domain> update(@PathVariable int id, @RequestBody Domain input) {
        Domain domain = domainsRepository.findOne(id);
        domain.setName(input.getName());

        return ResponseEntity.ok(domainsRepository.save(domain));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int id) {
        domainsRepository.delete(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
