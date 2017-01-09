package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.dsl.BadgeRuleEvaluator;
import ch.heig.amt.g4mify.model.BadgeType;
import ch.heig.amt.g4mify.model.Counter;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.BadgeRule;
import ch.heig.amt.g4mify.model.view.OutputView;
import ch.heig.amt.g4mify.model.view.badgeType.BadgeTypeSummary;
import ch.heig.amt.g4mify.model.view.badgeRule.BadgeRuleDetail;
import ch.heig.amt.g4mify.model.view.badgeRule.BadgeRuleSummary;
import ch.heig.amt.g4mify.repository.BadgeTypesRepository;
import ch.heig.amt.g4mify.repository.BadgeRulesRepository;
import ch.heig.amt.g4mify.util.CounterSpecResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class BadgeRulesApi extends AbstractDomainApi {

    private static final Logger LOG = Logger.getLogger(BadgeRulesApi.class.getName());

    @Autowired
    private BadgeRulesRepository badgeRulesRepository;
    @Autowired
    private BadgeTypesRepository badgeTypesRepository;
    @Autowired
    private CounterSpecResolver counterSpecResolver;

    private BadgeRuleEvaluator badgeRuleEvaluator = new BadgeRuleEvaluator();

    @RequestMapping(method = RequestMethod.GET)
    @Transactional
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
    public ResponseEntity<BadgeRuleDetail> create(@RequestBody BadgeRuleSummary body) {


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

    @RequestMapping("/{id}")
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

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<BadgeRuleDetail> update(@PathVariable long id, @RequestBody BadgeRuleSummary body) {

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
}
