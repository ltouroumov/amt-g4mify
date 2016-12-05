package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.BadgeType;
import ch.heig.amt.g4mify.model.Counter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ldavid
 * @created 12/5/16
 */
@RestController
@RequestMapping("/api/badge-types")
public class BadgeTypesApi extends AbstractDomainApi {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BadgeType>> index(@RequestParam("domain") long domainId,
                                                 @RequestParam(required = false, defaultValue = "0") long page,
                                                 @RequestParam(required = false, defaultValue = "50") long pageSize) {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestParam("domain") long domainId,
                                    @RequestBody BadgeType body) {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(0)
                .toUri();

        return ResponseEntity.created(uri).body(body);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<BadgeType> show(@PathVariable long id) {
        return ResponseEntity.ok(new BadgeType());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<BadgeType> update(@PathVariable long id,
                                          @RequestBody BadgeType body) {
        return ResponseEntity.ok(body);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        return ResponseEntity.ok(null);
    }

}
