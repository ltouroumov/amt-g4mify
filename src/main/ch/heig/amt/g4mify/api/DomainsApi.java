package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.view.View;
import ch.heig.amt.g4mify.model.view.domain.DomainSummary;
import ch.heig.amt.g4mify.model.view.domain.DomainUpdate;
import ch.heig.amt.g4mify.repository.DomainsRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author ldavid
 * @created 11/14/16
 */
@RestController
@RequestMapping("/api/domains")
@Api(value = "Domains", produces = MediaType.APPLICATION_JSON_VALUE)
public class DomainsApi {

    private static Logger LOG = Logger.getLogger(DomainsApi.class.getSimpleName());

    @Autowired
    private DomainsRepository domainsRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Lists all domains", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DomainSummary>> index(@RequestParam(required = false, defaultValue = "0") long page,
                                                     @RequestParam(required = false, defaultValue = "50") long pageSize) {

        List<DomainSummary> domains = domainsRepository.findAll().stream()
                .skip(page * pageSize)
                .limit(pageSize)
                .map(domain -> View.to(DomainSummary.class, domain))
                .collect(Collectors.toList());

        return ResponseEntity.ok(domains);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Create a domain", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody DomainUpdate body) {

        Domain input = View.to(Domain.class, body);
        input.setKey("secret");
        Domain domain = domainsRepository.save(input);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(domain.getId())
                .toUri();

        return ResponseEntity.created(uri).body(domain);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get domain details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DomainSummary> show(@PathVariable long id) {
        DomainSummary domain = View.to(DomainSummary.class, domainsRepository.findOne(id));

        return ResponseEntity.ok(domain);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update a domain", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Domain> update(@PathVariable long id,
                                         @RequestBody DomainUpdate input) {
        Domain domain = domainsRepository.findOne(id);
        View.update(domain, input);

        return ResponseEntity.ok(domainsRepository.save(domain));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation("Remove a domain")
    public ResponseEntity<?> delete(@PathVariable long id) {
        domainsRepository.delete(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
