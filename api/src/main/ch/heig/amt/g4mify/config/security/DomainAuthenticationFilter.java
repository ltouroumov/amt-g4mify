package ch.heig.amt.g4mify.config.security;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.repository.DomainsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class DomainAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOG = Logger.getLogger(DomainAuthenticationFilter.class.getSimpleName());

    private static final Pattern authPattern = Pattern.compile("^(\\d+):(\\w+)$");

    @Autowired
    private DomainsRepository domainsRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Identity"); // <domain-id>:<domain-key>

        if (authorization == null) {
            // LOG.info("Request Identity: Anonymous");
            SecurityContextHolder.getContext().setAuthentication(null);
            filterChain.doFilter(request, response);
            return;
        }

        Matcher matcher = authPattern.matcher(authorization);
        if (!matcher.matches()) {
            doError(response);
            return;
        }

        long domainId = Integer.parseInt(matcher.group(1));
        String domainKey = matcher.group(2);

        Domain domain = domainsRepository.findOne(domainId);
        if (domain == null || !domain.getKey().equalsIgnoreCase(domainKey)) {
            doError(response);
        } else {
            // LOG.info("Request Identity: " + domain.getName());
            // Create our Authentication and let Spring know about it
            Authentication auth = new DomainAuthenticationToken(domainId, domainKey);
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        }

    }

    private void doError(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

}