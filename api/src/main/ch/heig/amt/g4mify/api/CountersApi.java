package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Counter;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.Metric;
import ch.heig.amt.g4mify.model.view.OutputView;
import ch.heig.amt.g4mify.model.view.counter.CounterSummary;
import ch.heig.amt.g4mify.model.view.counter.CounterUpdateView;
import ch.heig.amt.g4mify.model.view.metric.MetricSummary;
import ch.heig.amt.g4mify.repository.CountersRepository;
import ch.heig.amt.g4mify.repository.MetricsRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@Api(value = "counters", description = "Handles CRUD operations on counters")
public class CountersApi extends AbstractDomainApi {

    @Autowired
    private CountersRepository countersRepository;

    @Autowired
    private MetricsRepository metricsRepository;

    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    @ApiOperation(value = "Retrieves all the counters from the domain", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error retrieving counters from the domain")})
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
    @ApiOperation(value = "Creates a new counter in the domain",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error creating counter in the domain")})
    public ResponseEntity<?> create(@RequestBody CounterUpdateView body) {

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

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves a particular counter from the domain", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error retrieving the counters from the domain")})
    public ResponseEntity<CounterSummary> show(@PathVariable String name) {
        return countersRepository.findByDomainAndName(getDomain(), name)
                .filter(this::canAccess)
                .map(this.getView()::from)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.status(HttpStatus.NOT_FOUND)::build);
    }

    @RequestMapping(path = "/{name}", method = RequestMethod.PUT)
    @ApiOperation(value = "Updates a particular counter from the domain",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error updating the counter from the domain")})
    public ResponseEntity<CounterSummary> update(@PathVariable String name,
                                                 @RequestBody CounterUpdateView body) {
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
    @ApiOperation(value = "Deletes a particular counter from the domain")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error deleting the counter from the domain")})
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
