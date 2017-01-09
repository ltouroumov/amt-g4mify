package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.BadgeType;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.Rule;
import ch.heig.amt.g4mify.model.view.badgeType.BadgeTypeSummary;
import ch.heig.amt.g4mify.model.view.rule.RuleDetail;
import ch.heig.amt.g4mify.model.view.rule.RuleOutputView;
import ch.heig.amt.g4mify.model.view.rule.RuleSummary;
import ch.heig.amt.g4mify.repository.BadgeTypesRepository;
import ch.heig.amt.g4mify.repository.RulesRepository;
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

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static ch.heig.amt.g4mify.model.view.ViewUtils.*;

/**
 * @author ldavid
 * @created 12/5/16
 */
@RestController
@RequestMapping("/api/rules")
@Api(value = "rules", description = "Handles CRUD operations on rules")
public class RulesApi extends AbstractDomainApi {

    @Autowired
    private RulesRepository rulesRepository;
    @Autowired
    private BadgeTypesRepository badgeTypesRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves all rules from the domain", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error retrieving rules from the domain")})
    public ResponseEntity<List<RuleDetail>> index(@RequestParam(required = false, defaultValue = "0") long page,
                                                  @RequestParam(required = false, defaultValue = "50") long pageSize) {

        Domain domain = getDomain();
        List<RuleDetail> rules = rulesRepository.findByDomain(domain)
                .skip(page * pageSize)
                .limit(pageSize)
                .map(outputView(RuleDetail.class)::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(rules);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new rules in the domain",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Ok"),
            @ApiResponse(code = 500, message = "Error creating the rule in the domain")})
    public ResponseEntity<?> create(@RequestBody RuleSummary body) {


        Domain domain = getDomain();
        Rule input = inputView(Rule.class)
                .map("grants", badgeTypeId -> badgeTypesRepository.findOne((long)badgeTypeId))
                .from(body);
        input.setDomain(domain);

        if(input.getGrants() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Rule rule = rulesRepository.save(input);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(rule.getId())
                .toUri();

        return ResponseEntity.created(uri).body(outputView(RuleOutputView.class)
                .map("grants", viewMap(BadgeTypeSummary.class))
                .from(rule));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves a particular rule from the domain", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error retrieving the rule from the domain")})
    public ResponseEntity<?> show(@PathVariable long id) {

        Rule rule = rulesRepository.findOne(id);

        if(rule == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (canAccess(rule)) {
            return ResponseEntity.ok(outputView(RuleOutputView.class)
                    .map("grants", viewMap(BadgeTypeSummary.class))
                    .from(rule));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Updates a particular rule in the domain",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error updating the rule in the domain")})
    public ResponseEntity<RuleOutputView> update(@PathVariable long id, @RequestBody RuleSummary body) {

        Rule rule = rulesRepository.findOne(id);

        if(rule == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (canAccess(rule)) {

            updateView(rule)
                    .map("grants", badgeTypeId -> badgeTypesRepository.findOne((long)badgeTypeId))
                    .with(body);

            if(rule.getGrants() == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            rulesRepository.save(rule);

            return ResponseEntity.ok(outputView(RuleOutputView.class)
                    .map("grants", viewMap(BadgeTypeSummary.class))
                    .from(rule));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Deletes a particular rule from the domain")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error deleting the rule from the domain")})
    public ResponseEntity<?> delete(@PathVariable long id) {

        Rule rule = rulesRepository.findOne(id);

        if(rule == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (canAccess(rule)) {
            rulesRepository.delete(rule);
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private boolean canAccess(Rule rule) {
        return rule.getDomain().getId() == getDomain().getId();
    }
}
