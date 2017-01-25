package ch.heig.amt.g4mify.config.security;

import ch.heig.amt.g4mify.model.Domain;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * @author ldavid
 * @created 12/5/16
 */
public class DomainAuthenticationToken extends AbstractAuthenticationToken {

    private final long id;
    private final String key;

    public DomainAuthenticationToken(long id, String key) {
        super(null);
        this.id = id;
        this.key = key;
    }

    @Override
    public Object getCredentials() {
        return String.format("%d:%s", id, key);
    }

    @Override
    public Object getPrincipal() {
        return id;
    }
}
