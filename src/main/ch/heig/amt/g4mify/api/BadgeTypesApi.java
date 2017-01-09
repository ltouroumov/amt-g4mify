package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.BadgeType;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.view.badgeType.BadgeTypeSummary;
import ch.heig.amt.g4mify.model.view.badgeType.BadgeTypeDetail;
import ch.heig.amt.g4mify.repository.BadgeTypesRepository;
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

import static ch.heig.amt.g4mify.model.view.ViewUtils.inputView;
import static ch.heig.amt.g4mify.model.view.ViewUtils.outputView;
import static ch.heig.amt.g4mify.model.view.ViewUtils.updateView;

/**
 * @author ldavid
 * @created 12/5/16
 */
@RestController
@RequestMapping("/api/badge-types")
@Api(value = "badge-types", description = "Handles CRUD operations on badges-types")
public class BadgeTypesApi extends AbstractDomainApi {

    @Autowired
    private BadgeTypesRepository badgeTypesRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves all badge-types from the domain", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error retrieving badge-types from the domain")})
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
    @ApiOperation(value = "Creates a new badge-type in the domain",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error creating the badge-type in the domain")})
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

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @ApiOperation(value = "Retrieves a particular badge-type from the domain", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error retrieving the badge-type from the domain")})
    public ResponseEntity<BadgeTypeDetail> show(@PathVariable long id) {

        BadgeType badgeType = badgeTypesRepository.findOne(id);

        if(badgeType == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (canAccess(badgeType)) {
            return ResponseEntity.ok(outputView(BadgeTypeDetail.class).from(badgeType));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    @ApiOperation(value = "Updates a particular badge-type from the domain",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error updating the badge-type from the domain")})
    public ResponseEntity<BadgeTypeSummary> update(@PathVariable long id,
                                                   @RequestBody BadgeTypeSummary body) {

        BadgeType badgeType = badgeTypesRepository.findOne(id);

        if(badgeType == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (canAccess(badgeType)) {
            updateView(badgeType).with(body);
            badgeTypesRepository.save(badgeType);
            return ResponseEntity.ok(outputView(BadgeTypeSummary.class).from(badgeType));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    @ApiOperation(value = "Deletes a particular badge-type from the domain")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Error deleting the badge-type from the domain")})
    public ResponseEntity<?> delete(@PathVariable long id) {

        BadgeType badgeType = badgeTypesRepository.findOne(id);

        if(badgeType == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (canAccess(badgeType)) {
            badgeTypesRepository.delete(badgeType);
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private boolean canAccess(BadgeType badgeType) {
        return badgeType.getDomain().getId() == getDomain().getId();
    }
}
