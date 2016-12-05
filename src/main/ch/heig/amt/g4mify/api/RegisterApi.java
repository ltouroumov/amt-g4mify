package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.view.View;
import ch.heig.amt.g4mify.model.view.domain.DomainUpdate;
import ch.heig.amt.g4mify.repository.DomainsRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ldavid
 * @created 12/5/16
 */
@RestController
@RequestMapping(path = "/register")
@Api(value = "register", description = "Handles Domain Registration")
public class RegisterApi {

    @Autowired
    private DomainsRepository domainsRepository;

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Register a new Domain")
    public ResponseEntity<Domain> register(@RequestBody DomainUpdate body) {

        Domain input = View.to(Domain.class, body);
        input.setKey("secret");
        Domain domain = domainsRepository.save(input);

        return ResponseEntity.status(HttpStatus.CREATED).body(domain);

    }

}
