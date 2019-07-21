package pt.ist.socialsoftware.edition.ldod.user.feature.security;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.User;
import pt.ist.socialsoftware.edition.ldod.frontend.session.FrontendSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
public class UserModuleAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final Logger log = LoggerFactory.getLogger(UserModuleAuthenticationSuccessHandler.class);

    @Override
    @Atomic(mode = TxMode.WRITE)
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.debug("onAuthenticationSuccess");

        User user = User.getAuthenticatedUser();
        user.setLastLogin(LocalDate.now());

        FrontendSession frontendSession = new FrontendSession();
        request.getSession().setAttribute("frontendSession", frontendSession);
        frontendSession.updateSession(user.getUsername());

        super.onAuthenticationSuccess(request, response, authentication);
        log.debug("onAuthenticationSuccess");
    }

}