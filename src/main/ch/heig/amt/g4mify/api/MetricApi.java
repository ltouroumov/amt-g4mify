package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Counter;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.Metric;
import ch.heig.amt.g4mify.model.view.metric.MetricSummary;
import ch.heig.amt.g4mify.model.view.metric.MetricUpdate;
import ch.heig.amt.g4mify.repository.CountersRepository;
import ch.heig.amt.g4mify.repository.MetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ch.heig.amt.g4mify.model.view.ViewUtils.*;

/**
 * @author ldavid
 * @created 12/5/16
 */
@RestController
@RequestMapping("/api/counters/{counterName}/metrics")
public class MetricApi extends AbstractDomainApi {

    @Autowired
    private CountersRepository countersRepository;

    @Autowired
    private MetricsRepository metricsRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<MetricSummary>> index(@PathVariable String counterName,
                                                     @RequestParam(required = false, defaultValue = "0") long page,
                                                     @RequestParam(required = false, defaultValue = "50") long pageSize) {
        Domain domain = getDomain();
        Optional<Counter> counterOpt = countersRepository.findByDomainAndName(domain, counterName);

        return counterOpt
                .filter(counter -> counter.getDomain().getId() != domain.getId())
                .map(counter -> {
                    List<MetricSummary> users = counter.getMetrics()
                            .stream()
                            .skip(page * pageSize)
                            .limit(pageSize)
                            .map(outputView(MetricSummary.class)::from)
                            .collect(Collectors.toList());

                    return ResponseEntity.ok(users);
                })
                .orElseGet(ResponseEntity.status(HttpStatus.NOT_FOUND)::build);

    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@PathVariable String counterName,
                                    @RequestBody MetricUpdate body) {
        Domain domain = getDomain();
        Optional<Counter> counterOpt = countersRepository.findByDomainAndName(domain, counterName);

        return counterOpt
                .filter(counter -> counter.getDomain().getId() != domain.getId())
                .map(counter -> {
                    try {
                        Metric input = inputView(Metric.class).from(body);
                        input.setCounter(counter);

                        metricsRepository.save(input);

                        URI uri = ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(input.getId())
                                .toUri();

                        return ResponseEntity.created(uri).body(outputView(MetricSummary.class).from(input));
                    } catch (DataIntegrityViolationException ex) {
                        throw new ApiException("Failed to create metric, most probable cause: duplicate name");
                    }
                })
                .orElseGet(ResponseEntity.status(HttpStatus.NOT_FOUND)::build);

    }

    @RequestMapping("/{metricName}")
    public ResponseEntity<MetricSummary> show(@PathVariable String counterName,
                                              @PathVariable String metricName) {

        return findMetric(counterName, metricName)
                .map(metric -> ResponseEntity.ok(outputView(MetricSummary.class).from(metric)))
                .orElseGet(ResponseEntity.status(HttpStatus.NOT_FOUND)::build);

    }

    @RequestMapping(path = "/{metricName}", method = RequestMethod.PUT)
    public ResponseEntity<MetricSummary> update(@PathVariable String counterName,
                                                @PathVariable String metricName,
                                                @RequestBody MetricUpdate body) {

        return findMetric(counterName, metricName)
                .map(metric -> updateView(metric).with(body))
                .map(metric -> ResponseEntity.ok(outputView(MetricSummary.class).from(metric)))
                .orElseGet(ResponseEntity.status(HttpStatus.NOT_FOUND)::build);

    }

    @RequestMapping(path = "/{metricName}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String counterName,
                                    @PathVariable String metricName) {

        return findMetric(counterName, metricName)
                .map(metric -> {
                    metricsRepository.delete(metric);
                    return ResponseEntity.ok(null);
                })
                .orElseGet(ResponseEntity.status(HttpStatus.NOT_FOUND)::build);

    }

    private Optional<Metric> findMetric(String counterName, String metricName) {
        Domain domain = getDomain();
        return countersRepository.findByDomainAndName(domain, counterName)
                .flatMap(counter -> metricsRepository.findByCounterAndName(counter, metricName))
                .filter(metric -> metric.getCounter().getDomain().getId() != domain.getId());
    }

}
