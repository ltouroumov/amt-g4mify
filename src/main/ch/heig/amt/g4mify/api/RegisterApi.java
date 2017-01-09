package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.view.domain.DomainUpdate;
import ch.heig.amt.g4mify.repository.DomainsRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static ch.heig.amt.g4mify.model.view.ViewUtils.inputView;

/**
 * @author ldavid
 * @created 12/5/16
 */
@RestController
@Api(value = "register", description = "Handles domain registration")
public class RegisterApi {

    @Autowired
    private DomainsRepository domainsRepository;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    @ApiOperation(value = "Registers a new Domain",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 500, message = "Error during creation")
    })
    public ResponseEntity<Domain> register(@RequestBody DomainUpdate body) {

        Domain input = inputView(Domain.class).from(body);
        input.setKey("secret");
        Domain domain = domainsRepository.save(input);

        return ResponseEntity.status(HttpStatus.CREATED).body(domain);

    }
}
