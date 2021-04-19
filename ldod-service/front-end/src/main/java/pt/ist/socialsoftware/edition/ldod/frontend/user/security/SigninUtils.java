package pt.ist.socialsoftware.edition.ldod.frontend.user.security;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.WebRequest;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.session.FrontendSession;
import pt.ist.socialsoftware.edition.user.api.dto.UserDto;

public class SigninUtils {
    private static final Logger log = LoggerFactory.getLogger(SigninUtils.class);

    private final static FeUserRequiresInterface feUserRequiresInterface = new FeUserRequiresInterface();

    @Atomic(mode = TxMode.WRITE)
    public static void signin(WebRequest request, UserDto user) {
        log.debug("signin user:{}", user.getUsername());

        feUserRequiresInterface.setLastLogin(user.getUsername(), LocalDate.now());

        UserModuleUserDetailsService service = new UserModuleUserDetailsService();
        UserDetails userDetails = service.loadUserByUsername(user.getUsername());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        FrontendSession frontendSession = new FrontendSession();
        request.setAttribute("frontendSession", frontendSession, WebRequest.SCOPE_SESSION);
        frontendSession.updateSession(user.getUsername());

        log.debug("signin authentication:{}, user:{}", SecurityContextHolder.getContext().getAuthentication(),
                UserModuleUserDetails.getAuthenticatedUser() == null ? "NULL" : UserModuleUserDetails.getAuthenticatedUser().getUsername());
    }

}
