package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.repository.DomainsRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ldavid
 * @created 11/28/16
 */
public class AbstractDomainApi {

    @Autowired
    private DomainsRepository domainsRepository;

    protected Domain getDomain(long id) {
        return domainsRepository.findOne(id);
    }

}
