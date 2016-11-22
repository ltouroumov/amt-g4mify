package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.repository.DomainsRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ldavid
 * @created 11/21/16
 */
public abstract class BaseDomainApi {

    @Autowired
    protected DomainsRepository domainsRepository;

    protected Domain getDomain(long domainId) {
        return domainsRepository.findOne(domainId);
    }

}
