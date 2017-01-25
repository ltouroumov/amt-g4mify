package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.EventRule;
import ch.heig.amt.g4mify.model.view.eventRule.EventRuleDetail;
import ch.heig.amt.g4mify.model.view.eventRule.EventRuleOutputView;
import ch.heig.amt.g4mify.model.view.eventRule.EventRuleSummary;
import ch.heig.amt.g4mify.repository.EventRulesRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static ch.heig.amt.g4mify.model.view.ViewUtils.*;

/**
 * @author ldavid
 * @created 12/5/16
 */
@RestController
@RequestMapping("/api/event-rules")
@Api(value = "event-rules", description = "Handles CRUD operations on event-rules")
public class EventRulesApi extends AbstractDomainApi {

    @Autowired
    private EventRulesRepository eventRulesRepository;

    @Autowired
    private EntityManager em;

    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    @ApiOperation(value = "Retreives all event-rules from the domain",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error retrieving event-rules from the domain")})
    public ResponseEntity<List<EventRuleDetail>> index(@RequestParam(required = false, defaultValue = "0") long page,
                                                       @RequestParam(required = false, defaultValue = "50") long pageSize,
                                                       @RequestParam(required = false, defaultValue = "") String type) {

        Domain domain = getDomain();
        List<EventRuleDetail> rules;
        if (!type.isEmpty()) {
            TypedQuery<EventRule> query = em.createNamedQuery("EventRule.FindByTypesInDomain", EventRule.class);
            query.setParameter(1, type);
            query.setParameter(2, domain.getId());

            rules = query.getResultList()
                    .stream()
                    .map(outputView(EventRuleDetail.class)::from)
                    .collect(Collectors.toList());
        } else {
            rules = eventRulesRepository.findByDomain(domain)
                        .skip(page * pageSize)
                        .limit(pageSize)
                        .map(outputView(EventRuleDetail.class)::from)
                        .collect(Collectors.toList());
        }
        return ResponseEntity.ok(rules);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new event-rule in the domain",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error creating event-rule in the domain")})
    public ResponseEntity<EventRuleOutputView> create(@RequestBody EventRuleSummary body) {

        Domain domain = getDomain();
        EventRule input = inputView(EventRule.class).from(body);

        input.setDomain(domain);

        if(input.getTypes() == null || input.getTypes().size() == 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        eventRulesRepository.save(input);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(input.getId())
                .toUri();

        return ResponseEntity.created(uri).body(outputView(EventRuleOutputView.class).from(input));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves a particular event-rule from the domain", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error retrieving the event-rule from the domain")})
    public ResponseEntity<?> show(@PathVariable long id) {

        EventRule eventRule = eventRulesRepository.findOne(id);

        if(eventRule == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (canAccess(eventRule)) {
            return ResponseEntity.ok(outputView(EventRuleOutputView.class)
                    .from(eventRule));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Updates a particular event-rule from the domain",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error updating the event-rule from the domain")})
    public ResponseEntity<EventRuleOutputView> update(@PathVariable long id, @RequestBody EventRuleSummary body) {

        EventRule eventRule = eventRulesRepository.findOne(id);

        if(eventRule == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (canAccess(eventRule)) {
            updateView(eventRule).with(body);
            eventRulesRepository.save(eventRule);

            return ResponseEntity.ok(outputView(EventRuleOutputView.class).from(eventRule));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Deletes a particular event-rule from the domain")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error deleting the event-rule from the domain")})
    public ResponseEntity<?> delete(@PathVariable long id) {

        EventRule eventRule = eventRulesRepository.findOne(id);

        if(eventRule == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (canAccess(eventRule)) {
            eventRulesRepository.delete(eventRule);
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private boolean canAccess(EventRule eventRule) {
        return eventRule.getDomain().getId() == getDomain().getId();
    }
}
