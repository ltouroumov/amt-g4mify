package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.actors.EventProcessor;
import ch.heig.amt.g4mify.dsl.BadgeRuleEvaluator;
import ch.heig.amt.g4mify.model.*;
import ch.heig.amt.g4mify.model.view.OutputView;
import ch.heig.amt.g4mify.model.view.badgeRule.BadgeRuleUpdate;
import ch.heig.amt.g4mify.model.view.badgeType.BadgeTypeSummary;
import ch.heig.amt.g4mify.model.view.badgeRule.BadgeRuleDetail;
import ch.heig.amt.g4mify.model.view.badgeRule.BadgeRuleSummary;
import ch.heig.amt.g4mify.repository.BadgeTypesRepository;
import ch.heig.amt.g4mify.repository.BadgeRulesRepository;
import ch.heig.amt.g4mify.repository.UsersRepository;
import ch.heig.amt.g4mify.util.CounterSpecResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static ch.heig.amt.g4mify.model.view.ViewUtils.*;

/**
 * @author ldavid
 * @created 12/5/16
 */
@RestController
@RequestMapping("/api/badge-rules")
@Api(value = "badge-rules", description = "Handles CRUD operations on badge rules")
public class BadgeRulesApi extends AbstractDomainApi {

    private static final Logger LOG = Logger.getLogger(BadgeRulesApi.class.getName());

    @Autowired
    private BadgeRulesRepository badgeRulesRepository;
    @Autowired
    private BadgeTypesRepository badgeTypesRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CounterSpecResolver counterSpecResolver;
    @Autowired
    private EventProcessor eventProcessor;

    private BadgeRuleEvaluator badgeRuleEvaluator = new BadgeRuleEvaluator();

    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    @ApiOperation(value = "Retreives all badge-rules from the domain",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error retrieving badge-rules from the domain")})
    public ResponseEntity<List<BadgeRuleSummary>> index(@RequestParam(required = false, defaultValue = "0") long page,
                                                        @RequestParam(required = false, defaultValue = "50") long pageSize) {

        Domain domain = getDomain();
        List<BadgeRuleSummary> rules = badgeRulesRepository.findByDomain(domain)
                .skip(page * pageSize)
                .limit(pageSize)
                .map(outputView(BadgeRuleSummary.class)
                        .map("grants", grant -> ((BadgeType) grant).getKey())::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(rules);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new badge-rule in the domain",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error creating badge-rule in the domain")})
    public ResponseEntity<BadgeRuleDetail> create(@RequestBody BadgeRuleUpdate body) {


        Domain domain = getDomain();
        BadgeRule input = inputView(BadgeRule.class)
                .map("grants", badgeKey -> badgeTypesRepository.findByDomainAndKey(domain, (String) badgeKey).orElse(null))
                .from(body);
        input.setDomain(domain);

        if(input.getGrants() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Set<String> counters = badgeRuleEvaluator.findCounters(input);
        for (String counterSpec : counters) {
            Counter counter = counterSpecResolver.findCounter(domain, counterSpec);
            input.getDepends().add(counter);
        }


        BadgeRule badgeRule = badgeRulesRepository.save(input);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(badgeRule.getId())
                .toUri();

        return ResponseEntity.created(uri).body(getView().from(badgeRule));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves a particular badge-rule from the domain", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error retrieving the badge-rule from the domain")})
    public ResponseEntity<BadgeRuleDetail> show(@PathVariable long id) {

        BadgeRule badgeRule = badgeRulesRepository.findOne(id);

        if(badgeRule == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (canAccess(badgeRule)) {
            return ResponseEntity.ok(getView().from(badgeRule));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(path = "/{id}/evaluate", method = RequestMethod.GET)
    @ApiOperation(value = "Evaluates a badge-rules's script againts the specified user's counters (debug method, does not trigger grants)", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error evaluating the rule")})
    public ResponseEntity<EvaluationResult> eval(@PathVariable long id, @RequestParam(name = "user") String pid) {

        BadgeRule badgeRule = badgeRulesRepository.findOne(id);

        if(badgeRule == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = usersRepository.findByDomainAndProfileId(getDomain(), pid).orElse(null);

        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (canAccess(badgeRule)) {
            boolean result = eventProcessor.evaluateBadgeRule(user, badgeRule);

            return ResponseEntity.ok(new EvaluationResult(result));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Updates a particular badge-rule from the domain",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error updating the badge-rule from the domain")})
    public ResponseEntity<BadgeRuleDetail> update(@PathVariable long id, @RequestBody BadgeRuleUpdate body) {

        BadgeRule badgeRule = badgeRulesRepository.findOne(id);

        if(badgeRule == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (canAccess(badgeRule)) {

            updateView(badgeRule)
                    .map("grants", badgeKey -> badgeTypesRepository.findByDomainAndKey(getDomain(), (String) badgeKey).orElse(null))
                    .with(body);

            if(badgeRule.getGrants() == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            badgeRulesRepository.save(badgeRule);

            return ResponseEntity.ok(getView().from(badgeRule));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Deletes a particular badge-rule from the domain")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error deleting the badge-rule from the domain")})
    public ResponseEntity<?> delete(@PathVariable long id) {

        BadgeRule badgeRule = badgeRulesRepository.findOne(id);

        if(badgeRule == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (canAccess(badgeRule)) {
            badgeRulesRepository.delete(badgeRule);
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private OutputView<BadgeRuleDetail> getView() {
        return outputView(BadgeRuleDetail.class)
                .map("depends", mapList(dep -> ((Counter)dep).getName()))
                .map("grants", viewMap(BadgeTypeSummary.class));
    }

    private boolean canAccess(BadgeRule badgeRule) {
        return badgeRule.getDomain().getId() == getDomain().getId();
    }

    private class EvaluationResult {

        public final boolean result;

        public EvaluationResult(boolean result) {
            this.result = result;
        }
    }
}
