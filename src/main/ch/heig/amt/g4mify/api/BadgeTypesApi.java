package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.BadgeType;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.view.badgeTypes.BadgeTypesSummary;
import ch.heig.amt.g4mify.model.view.badgeTypes.BadgeTypesDetail;
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
    public ResponseEntity<List<BadgeTypesDetail>> index(@RequestParam(required = false, defaultValue = "0") long page,
                                                        @RequestParam(required = false, defaultValue = "50") long pageSize) {

        Domain domain = getDomain();
        List<BadgeTypesDetail> badgeTypes = badgeTypesRepository.findByDomain(domain)
                .skip(page * pageSize)
                .limit(pageSize)
                .map(outputView(BadgeTypesDetail.class)::from)
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

        return ResponseEntity.created(uri).body(outputView(BadgeTypesDetail.class).from(badgeType));
    }

    @RequestMapping("/{id}")
    public ResponseEntity<BadgeTypesDetail> show(@PathVariable long id) {

        BadgeType badgeType = badgeTypesRepository.findOne(id);
        if (canAccess(badgeType)) {
            return ResponseEntity.ok(outputView(BadgeTypesDetail.class).from(badgeType));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<BadgeTypesSummary> update(@PathVariable long id,
                                                    @RequestBody BadgeTypesSummary body) {

        BadgeType badgeType = badgeTypesRepository.findOne(id);
        if (canAccess(badgeType)) {
            updateView(badgeType).with(body);
            badgeTypesRepository.save(badgeType);
            return ResponseEntity.ok(outputView(BadgeTypesSummary.class).from(badgeType));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        BadgeType badgeType = badgeTypesRepository.findOne(id);
        if (canAccess(badgeType)) {
            badgeTypesRepository.delete(badgeType);
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private boolean canAccess(BadgeType badgeType) { return badgeType.getDomain().getId() == getDomain().getId(); }
}
