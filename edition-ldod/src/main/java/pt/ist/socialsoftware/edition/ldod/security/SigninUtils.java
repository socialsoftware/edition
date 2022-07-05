package pt.ist.socialsoftware.edition.ldod.security;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.WebRequest;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;

public class SigninUtils {
	private static Logger log = LoggerFactory.getLogger(SigninUtils.class);

	@Atomic(mode = TxMode.WRITE)
	public static void signin(WebRequest request, LdoDUser user) {
		log.debug("signin user:{}", user.getUsername());

		user.setLastLogin(LocalDate.now());

		LdoDUserDetailsService service = new LdoDUserDetailsService();
		UserDetails userDetails = service.loadUserByUsername(user.getUsername());

		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

		LdoDSession ldoDSession = new LdoDSession();
		request.setAttribute("ldoDSession", ldoDSession, WebRequest.SCOPE_SESSION);
		ldoDSession.updateSession(user);

		log.debug("signin authentication:{}, user:{}", SecurityContextHolder.getContext().getAuthentication(),
				LdoDUser.getAuthenticatedUser() == null ? "NULL" : LdoDUser.getAuthenticatedUser().getUsername());
	}

}
