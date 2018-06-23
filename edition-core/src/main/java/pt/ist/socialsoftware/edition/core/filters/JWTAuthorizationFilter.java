package pt.ist.socialsoftware.edition.core.filters;

import static pt.ist.socialsoftware.edition.core.security.SecurityConstants.HEADER_STRING;
import static pt.ist.socialsoftware.edition.core.security.SecurityConstants.SECRET;
import static pt.ist.socialsoftware.edition.core.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import pt.ist.socialsoftware.edition.core.security.LdoDUserDetailsService;

@Component
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private static Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);
    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            String user = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody().getSubject();

            if (user != null) {
                LdoDUserDetailsService service = new LdoDUserDetailsService();
                return new UsernamePasswordAuthenticationToken(service.loadUserByUsername(user), null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }

}
