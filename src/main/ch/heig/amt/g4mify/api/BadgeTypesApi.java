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
@RequestMapping("/api/badge-types")
public class BadgeTypesApi extends AbstractDomainApi {

    @Autowired
    private BadgeTypesRepository badgeTypesRepository;

    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public ResponseEntity<List<BadgeTypeSummary>> index(@RequestParam(required = false, defaultValue = "0") long page,
                                                       @RequestParam(required = false, defaultValue = "50") long pageSize) {

        Domain domain = getDomain();
        List<BadgeTypeSummary> badgeTypes = badgeTypesRepository.findByDomain(domain)
                .skip(page * pageSize)
                .limit(pageSize)
                .map(outputView(BadgeTypeSummary.class)::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(badgeTypes);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody BadgeTypeDetail body) {

        Domain domain = getDomain();
        BadgeType input = inputView(BadgeType.class)
                .map("previous", orNull(prevKey -> badgeTypesRepository.findByDomainAndKey(domain, (String) prevKey).orElse(null)))
                .from(body);
        input.setDomain(domain);

        BadgeType badgeType = badgeTypesRepository.save(input);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{key}")
                .buildAndExpand(badgeType.getKey())
                .toUri();

        return ResponseEntity.created(uri).body(outputView(BadgeTypeDetail.class).map("previous", orNull(prev -> ((BadgeType)prev).getKey())).from(badgeType));
    }

    @RequestMapping("/{key}")
    public ResponseEntity<BadgeTypeDetail> show(@PathVariable String key) {
        return badgeTypesRepository.findByDomainAndKey(getDomain(), key)
                .filter(this::canAccess)
                .map(badgeType -> outputView(BadgeTypeDetail.class).map("previous", orNull(prev -> ((BadgeType)prev).getKey())).from(badgeType))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.status(HttpStatus.NOT_FOUND)::build);
    }

    @RequestMapping(path = "/{key}", method = RequestMethod.PUT)
    public ResponseEntity<BadgeTypeSummary> update(@PathVariable String key,
                                                   @RequestBody BadgeTypeDetail body) {
        return badgeTypesRepository.findByDomainAndKey(getDomain(), key)
                .filter(this::canAccess)
                .map(badgeType -> {
                    updateView(badgeType)
                            .map("previous", orNull(prevKey -> badgeTypesRepository.findByDomainAndKey(getDomain(), (String) prevKey).orElse(null)))
                            .with(body);
                    return badgeTypesRepository.save(badgeType);
                })
                .map(badgeType -> outputView(BadgeTypeSummary.class).map("previous", orNull(prev -> ((BadgeType)prev).getKey())).from(badgeType))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.status(HttpStatus.NOT_FOUND)::build);
    }

    @RequestMapping(path = "/{key}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String key) {
        return badgeTypesRepository.findByDomainAndKey(getDomain(), key)
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
