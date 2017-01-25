package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.repository.DomainsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author ldavid
 * @created 11/28/16
 */
public class AbstractDomainApi {

    @Autowired
    protected DomainsRepository domainsRepository;

    protected Domain getDomain() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long id = (Long)auth.getPrincipal();
        return domainsRepository.findOne(id);
    }

}
