package pt.ist.socialsoftware.edition.ldod.filters;

import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetailsService;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private static Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);
    private final static String TOKEN_PREFIX = PropertiesManager.getProperties()
            .getProperty("spring.security.jwt.token.prefix");
    private final static String HEADER_STRING = PropertiesManager.getProperties()
            .getProperty("spring.security.jwt.header.string");
    private final static String SECRET = PropertiesManager.getProperties().getProperty("spring.security.jwt.secret");

    public JWTAuthorizationFilter(@Qualifier("authenticationManagerBean") AuthenticationManager authManager) {
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

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req, res);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse res) throws IOException {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            String user;
            try {
                user = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                        .getBody().getSubject();
            } catch (Exception e){
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }

            if (user != null) {
                LdoDUserDetailsService service = new LdoDUserDetailsService();
                try {
                    return new UsernamePasswordAuthenticationToken(service.loadUserByUsername(user), null,
                            new ArrayList<>());
                } catch (UsernameNotFoundException e) {
                    return null;
                }

            }
            return null;
        }
        return null;
    }
}
