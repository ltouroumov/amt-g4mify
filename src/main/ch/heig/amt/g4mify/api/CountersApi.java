package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Counter;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.Metric;
import ch.heig.amt.g4mify.model.view.OutputView;
import ch.heig.amt.g4mify.model.view.counter.CounterSummary;
import ch.heig.amt.g4mify.model.view.counter.CounterUpdate;
import ch.heig.amt.g4mify.model.view.metric.MetricSummary;
import ch.heig.amt.g4mify.repository.CountersRepository;
import ch.heig.amt.g4mify.repository.MetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private MetricsRepository metricsRepository;

    @RequestMapping(method = RequestMethod.GET)
    @Transactional
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

        try {
            countersRepository.save(input);
            Metric total = new Metric();
            total.setName("total");
            total.setDuration(-1);
            total.setCounter(input);

            metricsRepository.save(total);
            input.getMetrics().add(total);

            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(input.getId())
                    .toUri();

            return ResponseEntity.created(uri).body(getView().from(input));
        } catch (DataIntegrityViolationException ex) {
            throw new ApiException("Failed to create counter, most probable cause: duplicate name");
        }
    }

    @RequestMapping("/{name}")
    public ResponseEntity<CounterSummary> show(@PathVariable String name) {
        return countersRepository.findByDomainAndName(getDomain(), name)
                .filter(this::canAccess)
                .map(this.getView()::from)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.status(HttpStatus.NOT_FOUND)::build);
    }

    @RequestMapping(path = "/{name}", method = RequestMethod.PUT)
    public ResponseEntity<CounterSummary> update(@PathVariable String name,
                                                 @RequestBody CounterUpdate body) {
        return countersRepository.findByDomainAndName(getDomain(), name)
                .filter(this::canAccess)
                .map(counter -> {
                    updateView(counter).with(body);
                    return countersRepository.save(counter);
                })
                .map(this.getView()::from)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.status(HttpStatus.NOT_FOUND)::build);
    }

    @RequestMapping(path = "/{name}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String name) {
        return countersRepository.findByDomainAndName(getDomain(), name)
                .filter(this::canAccess)
                .map(counter -> {
                    countersRepository.delete(counter);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(ResponseEntity.status(HttpStatus.NOT_FOUND)::build);
    }

    private OutputView<CounterSummary> getView() {
        return outputView(CounterSummary.class).map("metrics", mapList(viewMap(MetricSummary.class)));
    }

    private boolean canAccess(Counter user) {
        return user.getDomain().getId() == getDomain().getId();
    }

}
