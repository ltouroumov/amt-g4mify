package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.BadgeType;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.view.badgeType.BadgeTypeSummary;
import ch.heig.amt.g4mify.model.view.badgeType.BadgeTypeDetail;
import ch.heig.amt.g4mify.repository.BadgeTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static ch.heig.amt.g4mify.model.view.ViewUtils.inputView;
import static ch.heig.amt.g4mify.model.view.ViewUtils.outputView;
import static ch.heig.amt.g4mify.model.view.ViewUtils.updateView;

/**
 * @author ldavid
 * @created 12/5/16
 */
@RestController
@RequestMapping("/api/badge-types")
public class BadgeTypesApi extends AbstractDomainApi {

    @Autowired
    private BadgeTypesRepository badgeTypesRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BadgeTypeDetail>> index(@RequestParam(required = false, defaultValue = "0") long page,
                                                       @RequestParam(required = false, defaultValue = "50") long pageSize) {

        Domain domain = getDomain();
        List<BadgeTypeDetail> badgeTypes = badgeTypesRepository.findByDomain(domain)
                .skip(page * pageSize)
                .limit(pageSize)
                .map(outputView(BadgeTypeDetail.class)::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(badgeTypes);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody BadgeType body) {

        Domain domain = getDomain();
        BadgeType input = inputView(BadgeType.class).from(body);
        input.setDomain(domain);

        BadgeType badgeType = badgeTypesRepository.save(input);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(badgeType.getId())
                .toUri();

        return ResponseEntity.created(uri).body(outputView(BadgeTypeSummary.class).from(badgeType));
    }

    @RequestMapping("/{id}")
    public ResponseEntity<BadgeTypeDetail> show(@PathVariable long id) {
        return badgeTypesRepository.findById(id)
                .filter(this::canAccess)
                .map(badgeType -> outputView(BadgeTypeDetail.class).from(badgeType))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.status(HttpStatus.NOT_FOUND)::build);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<BadgeTypeSummary> update(@PathVariable long id,
                                                   @RequestBody BadgeTypeSummary body) {
        return badgeTypesRepository.findById(id)
                .filter(this::canAccess)
                .map(badgeType -> {
                    updateView(badgeType).with(body);
                    return badgeTypesRepository.save(badgeType);
                })
                .map(badgeType -> outputView(BadgeTypeSummary.class).from(badgeType))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.status(HttpStatus.NOT_FOUND)::build);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        return badgeTypesRepository.findById(id)
                .filter(this::canAccess)
                .map(badgeType -> {
                    badgeTypesRepository.delete(badgeType);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(ResponseEntity.status(HttpStatus.NOT_FOUND)::build);
    }

    private boolean canAccess(BadgeType badgeType) {
        return badgeType.getDomain().getId() == getDomain().getId();
    }
}
