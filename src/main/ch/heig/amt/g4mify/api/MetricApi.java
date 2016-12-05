package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Counter;
import ch.heig.amt.g4mify.model.Metric;
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
@RequestMapping("/api/counters/{counterId}/metrics")
public class MetricApi extends AbstractDomainApi {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Metric>> index(@PathVariable long counterId,
                                              @RequestParam(required = false, defaultValue = "0") long page,
                                              @RequestParam(required = false, defaultValue = "50") long pageSize) {
        return ResponseEntity.ok(new ArrayList<>());
    }

    public ResponseEntity<?> create(@PathVariable long counterId,
                                    @RequestBody Counter body) {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(0)
                .toUri();

        return ResponseEntity.created(uri).body(body);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Metric> show(@PathVariable long id) {
        return ResponseEntity.ok(new Metric());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Metric> update(@PathVariable long id,
                                          @RequestBody Counter body) {
        return ResponseEntity.ok(new Metric());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        return ResponseEntity.ok(null);
    }

}
