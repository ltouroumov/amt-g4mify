package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.EventRule;
import ch.heig.amt.g4mify.model.view.badgeRule.BadgeRuleDetail;
import ch.heig.amt.g4mify.model.view.badgeRule.BadgeRuleOutputView;
import ch.heig.amt.g4mify.model.view.eventRule.EventRuleDetail;
import ch.heig.amt.g4mify.model.view.eventRule.EventRuleOutputView;
import ch.heig.amt.g4mify.model.view.eventRule.EventRuleSummary;
import ch.heig.amt.g4mify.repository.EventRulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/event-rules")
public class EventRulesApi extends AbstractDomainApi {

    @Autowired
    private EventRulesRepository eventRulesRepository;

    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public ResponseEntity<List<EventRuleDetail>> index(@RequestParam(required = false, defaultValue = "0") long page,
                                                       @RequestParam(required = false, defaultValue = "50") long pageSize) {

        Domain domain = getDomain();
        List<EventRuleDetail> rules = eventRulesRepository.findByDomain(domain)
                .skip(page * pageSize)
                .limit(pageSize)
                .map(outputView(EventRuleDetail.class)::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(rules);
    }

    @RequestMapping(method = RequestMethod.POST)
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

    @RequestMapping("/{id}")
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
