package pt.ist.socialsoftware.edition.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.WebRequest;

import pt.ist.socialsoftware.edition.domain.LdoDUser;

public class SigninUtils {
    private static Logger log = LoggerFactory.getLogger(SigninUtils.class);

    public static void signin(WebRequest request, LdoDUser user) {
        log.debug("signin user:{}", user == null ? "NULL" : user.getUsername());

        LdoDUserDetailsService service = new LdoDUserDetailsService();
        UserDetails userDetails = service
                .loadUserByUsername(user.getUsername());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities()));

        LdoDSession ldoDSession = null;
        if (request.getAttribute("ldoDSession",
                WebRequest.SCOPE_SESSION) == null) {
            ldoDSession = new LdoDSession();
            request.setAttribute("ldoDSession", ldoDSession,
                    WebRequest.SCOPE_SESSION);
        } else {
            ldoDSession = (LdoDSession) request.getAttribute("ldoDSession",
                    WebRequest.SCOPE_SESSION);
        }
        ldoDSession.updateSession(user);

        log.debug("signin authenticate autnhentication:{}, user:{}",
                SecurityContextHolder.getContext().getAuthentication(),
                LdoDUser.getAuthenticatedUser() == null ? "NULL"
                        : LdoDUser.getAuthenticatedUser().getUsername());
    }

}
