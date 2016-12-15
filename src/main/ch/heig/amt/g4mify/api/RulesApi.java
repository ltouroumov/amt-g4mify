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
@RequestMapping("/api/rules")
public class RulesApi extends AbstractDomainApi {

    @Autowired
    private RulesRepository rulesRepository;
    @Autowired
    private BadgeTypesRepository badgeTypesRepository;

    @RequestMapping(method = RequestMethod.GET)
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

    @RequestMapping("/{id}")
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
