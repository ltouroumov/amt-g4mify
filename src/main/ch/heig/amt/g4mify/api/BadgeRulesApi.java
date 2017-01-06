package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.BadgeRule;
import ch.heig.amt.g4mify.model.view.badgeType.BadgeTypeSummary;
import ch.heig.amt.g4mify.model.view.badgeRule.BadgeRuleDetail;
import ch.heig.amt.g4mify.model.view.badgeRule.BadgeRuleOutputView;
import ch.heig.amt.g4mify.model.view.badgeRule.BadgeRuleSummary;
import ch.heig.amt.g4mify.repository.BadgeTypesRepository;
import ch.heig.amt.g4mify.repository.BadgeRulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static ch.heig.amt.g4mify.model.view.ViewUtils.*;

/**
 * @author ldavid
 * @created 12/5/16
 */
@RestController
@RequestMapping("/api/badge-rules")
public class BadgeRulesApi extends AbstractDomainApi {

    @Autowired
    private BadgeRulesRepository badgeRulesRepository;
    @Autowired
    private BadgeTypesRepository badgeTypesRepository;

    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public ResponseEntity<List<BadgeRuleDetail>> index(@RequestParam(required = false, defaultValue = "0") long page,
                                                       @RequestParam(required = false, defaultValue = "50") long pageSize) {

        Domain domain = getDomain();
        List<BadgeRuleDetail> rules = badgeRulesRepository.findByDomain(domain)
                .skip(page * pageSize)
                .limit(pageSize)
                .map(outputView(BadgeRuleDetail.class)::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(rules);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody BadgeRuleSummary body) {


        Domain domain = getDomain();
        BadgeRule input = inputView(BadgeRule.class)
                .map("grants", badgeKey -> badgeTypesRepository.findByDomainAndKey(domain, (String) badgeKey).orElse(null))
                .from(body);
        input.setDomain(domain);

        if(input.getGrants() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        BadgeRule badgeRule = badgeRulesRepository.save(input);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(badgeRule.getId())
                .toUri();

        return ResponseEntity.created(uri).body(outputView(BadgeRuleOutputView.class)
                .map("grants", viewMap(BadgeTypeSummary.class))
                .from(badgeRule));
    }

    @RequestMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable long id) {

        BadgeRule badgeRule = badgeRulesRepository.findOne(id);

        if(badgeRule == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (canAccess(badgeRule)) {
            return ResponseEntity.ok(outputView(BadgeRuleOutputView.class)
                    .map("grants", viewMap(BadgeTypeSummary.class))
                    .from(badgeRule));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<BadgeRuleOutputView> update(@PathVariable long id, @RequestBody BadgeRuleSummary body) {

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

            return ResponseEntity.ok(outputView(BadgeRuleOutputView.class)
                    .map("grants", viewMap(BadgeTypeSummary.class))
                    .from(badgeRule));
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

    private boolean canAccess(BadgeRule badgeRule) {
        return badgeRule.getDomain().getId() == getDomain().getId();
    }
}
