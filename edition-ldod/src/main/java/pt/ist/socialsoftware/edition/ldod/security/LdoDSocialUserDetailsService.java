package pt.ist.socialsoftware.edition.ldod.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

//@Service
public class LdoDSocialUserDetailsService implements SocialUserDetailsService {

	private LdoDUserDetailsService ldoDUserDetailsService;

	public LdoDSocialUserDetailsService() {
	}

	public LdoDSocialUserDetailsService(LdoDUserDetailsService ldoDUserDetailsService) {
		this.ldoDUserDetailsService = ldoDUserDetailsService;
	}

	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
		return this.ldoDUserDetailsService.loadUserByUsername(userId);
	}

}
