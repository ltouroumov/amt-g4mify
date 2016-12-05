package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Counter;
import ch.heig.amt.g4mify.model.Rule;
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
@RequestMapping("/api/rules")
public class RulesApi {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Rule>> index(@RequestParam("domain") long domainId,
                                            @RequestParam(required = false, defaultValue = "0") long page,
                                            @RequestParam(required = false, defaultValue = "50") long pageSize) {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestParam("domain") long domainId,
                                    @RequestBody Rule body) {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(0)
                .toUri();

        return ResponseEntity.created(uri).body(body);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Rule> show(@PathVariable long id) {
        return ResponseEntity.ok(new Rule());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Rule> update(@PathVariable long id,
                                          @RequestBody Rule body) {
        return ResponseEntity.ok(body);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        return ResponseEntity.ok(null);
    }

}
