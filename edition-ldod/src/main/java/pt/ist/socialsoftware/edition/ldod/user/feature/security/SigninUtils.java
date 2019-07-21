package pt.ist.socialsoftware.edition.ldod.user.feature.security;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.WebRequest;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.User;
import pt.ist.socialsoftware.edition.ldod.frontend.session.FrontendSession;

public class SigninUtils {
    private static final Logger log = LoggerFactory.getLogger(SigninUtils.class);

    @Atomic(mode = TxMode.WRITE)
    public static void signin(WebRequest request, User user) {
        log.debug("signin user:{}", user.getUsername());

        user.setLastLogin(LocalDate.now());

        UserModuleUserDetailsService service = new UserModuleUserDetailsService();
        UserDetails userDetails = service.loadUserByUsername(user.getUsername());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        FrontendSession frontendSession = new FrontendSession();
        request.setAttribute("frontendSession", frontendSession, WebRequest.SCOPE_SESSION);
        frontendSession.updateSession(user.getUsername());

        log.debug("signin authentication:{}, user:{}", SecurityContextHolder.getContext().getAuthentication(),
                User.getAuthenticatedUser() == null ? "NULL" : User.getAuthenticatedUser().getUsername());
    }

}
