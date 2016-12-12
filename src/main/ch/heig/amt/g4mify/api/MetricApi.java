package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Counter;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.Metric;
import ch.heig.amt.g4mify.model.view.metric.MetricSummary;
import ch.heig.amt.g4mify.model.view.metric.MetricUpdate;
import ch.heig.amt.g4mify.repository.CountersRepository;
import ch.heig.amt.g4mify.repository.MetricsRepository;
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
@RequestMapping("/api/counters/{counterId}/metrics")
public class MetricApi extends AbstractDomainApi {

    @Autowired
    private CountersRepository countersRepository;

    @Autowired
    private MetricsRepository metricsRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<MetricSummary>> index(@PathVariable long counterId,
                                              @RequestParam(required = false, defaultValue = "0") long page,
                                              @RequestParam(required = false, defaultValue = "50") long pageSize) {
        Domain domain = getDomain();
        Counter counter = countersRepository.findOne(counterId);

        if (counter.getDomain().getId() != domain.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<MetricSummary> users = counter.getMetrics()
                .stream()
                .skip(page * pageSize)
                .limit(pageSize)
                .map(outputView(MetricSummary.class)::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);

    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@PathVariable long counterId,
                                    @RequestBody MetricUpdate body) {
        Domain domain = getDomain();
        Counter counter = countersRepository.findOne(counterId);

        if (counter.getDomain().getId() != domain.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Metric input = inputView(Metric.class).from(body);
        input.setCounter(counter);

        metricsRepository.save(input);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(input.getId())
                .toUri();

        return ResponseEntity.created(uri).body(outputView(MetricSummary.class).from(input));
    }

    @RequestMapping("/{id}")
    public ResponseEntity<MetricSummary> show(@PathVariable long id) {
        Metric metric = metricsRepository.findOne(id);

        if (canAccess(metric)) {
            return ResponseEntity.ok(outputView(MetricSummary.class).from(metric));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<MetricSummary> update(@PathVariable long id,
                                          @RequestBody Counter body) {
        Metric metric = metricsRepository.findOne(id);

        if (canAccess(metric)) {
            updateView(metric).with(body);
            return ResponseEntity.ok(outputView(MetricSummary.class).from(metric));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        Metric metric = metricsRepository.findOne(id);

        if (canAccess(metric)) {
            metricsRepository.delete(metric);
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private boolean canAccess(Metric metric) {
        return metric.getCounter().getDomain().getId() != getDomain().getId();
    }
}
