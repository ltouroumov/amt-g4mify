package ch.heig.amt.g4mify.config.security;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.repository.DomainsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class DomainAuthenticationFilter extends OncePerRequestFilter {

    private static final Pattern authPattern = Pattern.compile("^(\\d+):(\\w+)$");

    @Autowired
    private DomainsRepository domainsRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            return;
        }

        // validate the value in authorization
        if(!authPattern.asPredicate().test(authorization)){
            throw new SecurityException();
        }

        Matcher matcher = authPattern.matcher(authorization);
        if (!matcher.matches()) {
            throw new SecurityException();
        }

        long domainId = Integer.parseInt(matcher.group(1));
        String domainKey = matcher.group(2);

        Domain domain = domainsRepository.findOne(domainId);
        if (domain == null || !domain.getKey().equalsIgnoreCase(domainKey)) {
            throw new SecurityException();
        } else {
            // Create our Authentication and let Spring know about it
            Authentication auth = new DomainAuthenticationToken(domainId, domainKey);
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        }

    }

}