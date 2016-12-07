package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.view.View;
import ch.heig.amt.g4mify.model.view.domain.DomainSummary;
import ch.heig.amt.g4mify.model.view.domain.DomainUpdate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author ldavid
 * @created 11/14/16
 */
@RestController
@RequestMapping("/api/domain")
@Api(value = "Domains", produces = MediaType.APPLICATION_JSON_VALUE)
public class DomainsApi extends AbstractDomainApi {

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Gets info about the current domain", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DomainSummary> show() {

        DomainSummary domain = View.to(DomainSummary.class, getDomain());

        return ResponseEntity.ok(domain);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ApiOperation(value = "Updates the current domain", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Domain> update(@RequestBody DomainUpdate input) {
        Domain domain = getDomain();
        View.update(domain, input);

        return ResponseEntity.ok(domainsRepository.save(domain));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ApiOperation("Destroy the current domain")
    public ResponseEntity<?> delete(@PathVariable long id) {
        domainsRepository.delete(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
