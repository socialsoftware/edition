package pt.ist.socialsoftware.edition.domain;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import pt.ist.socialsoftware.edition.security.UserDetailsImpl;

public class LdoDUser extends LdoDUser_Base {

	static public LdoDUser getUser() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		if (authentication != null) {
			UserDetailsImpl userDetails = null;
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetails) {
				userDetails = (UserDetailsImpl) principal;
				return userDetails.getUser();
			}
		}
		return null;
	}

	public LdoDUser(LdoD ldoD, String username, String password) {
		super();
		setLdoD(ldoD);
		setUsername(username);
		setPassword(password);
	}

}
