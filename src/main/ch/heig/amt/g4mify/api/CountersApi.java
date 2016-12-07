package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Counter;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.view.View;
import ch.heig.amt.g4mify.model.view.counter.CounterSummary;
import ch.heig.amt.g4mify.model.view.metric.MetricSummary;
import ch.heig.amt.g4mify.model.view.user.UserSummary;
import ch.heig.amt.g4mify.repository.CountersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ch.heig.amt.g4mify.model.view.ViewUtils.mapList;
import static ch.heig.amt.g4mify.model.view.ViewUtils.outputView;
import static ch.heig.amt.g4mify.model.view.ViewUtils.viewMap;

/**
 * @author ldavid
 * @created 12/5/16
 */
@RestController
@RequestMapping("/api/counters")
public class CountersApi extends AbstractDomainApi {

    @Autowired
    private CountersRepository countersRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CounterSummary>> index(@RequestParam(required = false, defaultValue = "0") long page,
                                                      @RequestParam(required = false, defaultValue = "50") long pageSize) {
        Domain domain = getDomain();

        List<CounterSummary> users = countersRepository.findByDomain(domain)
                .skip(page * pageSize)
                .limit(pageSize)
                .map(outputView(CounterSummary.class).map("metrics", mapList(viewMap(MetricSummary.class)))::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);

    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody Counter body) {

        Domain domain = getDomain();

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(0)
                .toUri();

        return ResponseEntity.created(uri).body(body);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Counter> show(@PathVariable long id) {
        return ResponseEntity.ok(new Counter());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Counter> update(@PathVariable long id,
                                          @RequestBody Counter body) {
        return ResponseEntity.ok(new Counter());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        return ResponseEntity.ok(null);
    }

}
