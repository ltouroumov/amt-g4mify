package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Counter;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.view.OutputView;
import ch.heig.amt.g4mify.model.view.counter.CounterSummary;
import ch.heig.amt.g4mify.model.view.counter.CounterUpdate;
import ch.heig.amt.g4mify.model.view.metric.MetricSummary;
import ch.heig.amt.g4mify.repository.CountersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static ch.heig.amt.g4mify.model.view.ViewUtils.*;

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
                .map(getView()::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);

    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody CounterUpdate body) {

        Domain domain = getDomain();
        Counter input = inputView(Counter.class).from(body);
        input.setDomain(domain);

        Counter counter = countersRepository.save(input);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(counter.getId())
                .toUri();

        return ResponseEntity.created(uri).body(outputView(CounterSummary.class).from(counter));
    }

    @RequestMapping("/{id}")
    public ResponseEntity<CounterSummary> show(@PathVariable long id) {
        Counter counter = countersRepository.findOne(id);
        if (canAccess(counter)) {
            return ResponseEntity.ok(getView().from(counter));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CounterSummary> update(@PathVariable long id,
                                          @RequestBody CounterUpdate body) {
        Counter counter = countersRepository.findOne(id);
        if (canAccess(counter)) {
            updateView(counter).with(body);
            countersRepository.save(counter);
            return ResponseEntity.ok(getView().from(counter));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        Counter counter = countersRepository.findOne(id);
        if (canAccess(counter)) {
            countersRepository.delete(counter);
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private OutputView<CounterSummary> getView() {
        return outputView(CounterSummary.class).map("metrics", mapList(viewMap(MetricSummary.class)));
    }

    private boolean canAccess(Counter user) {
        return user.getDomain().getId() == getDomain().getId();
    }

}
