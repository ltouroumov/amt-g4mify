package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.view.View;
import ch.heig.amt.g4mify.model.view.domain.DomainUpdate;
import ch.heig.amt.g4mify.repository.DomainsRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * @author ldavid
 * @created 12/5/16
 */
@RestController
@Api(value = "Handles Domain Registration")
public class RegisterApi {

    @Autowired
    private DomainsRepository domainsRepository;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<Domain> register(@RequestBody DomainUpdate body) {

        Domain input = View.to(Domain.class, body);
        input.setKey("secret");
        Domain domain = domainsRepository.save(input);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/api/domain")
                .buildAndExpand()
                .toUri();

        return ResponseEntity.created(uri).body(domain);

    }

}
