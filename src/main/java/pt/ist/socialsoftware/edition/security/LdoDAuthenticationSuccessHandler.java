package pt.ist.socialsoftware.edition.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;

@Component
public class LdoDAuthenticationSuccessHandler extends
		SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		if (request.getSession().getAttribute("ldoDSession") == null) {
			LdoDSession ldoDSession = new LdoDSession();
			for (VirtualEdition virtualEdition : LdoDUser.getUser()
					.getSelectedVirtualEditionsSet()) {
				ldoDSession.addSelectedVE(virtualEdition);
			}
			request.getSession().setAttribute("ldoDSession", ldoDSession);
		} else {
			LdoDSession ldoDSession = (LdoDSession) request.getSession()
					.getAttribute("ldoDSession");

			for (VirtualEdition virtualEdition : LdoDUser.getUser()
					.getSelectedVirtualEditionsSet()) {
				ldoDSession.addSelectedVE(virtualEdition);
			}

			LdoDUser.getUser().getSelectedVirtualEditionsSet()
					.addAll(ldoDSession.getSelectedVEs());
		}

		super.onAuthenticationSuccess(request, response, authentication);
	}
}