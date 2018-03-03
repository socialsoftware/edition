package pt.ist.socialsoftware.edition.core.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class LdoDSocialUserDetailsService implements SocialUserDetailsService {

    private LdoDUserDetailsService ldoDUserDetailsService;

    public LdoDSocialUserDetailsService() {
    }

    public LdoDSocialUserDetailsService(
            LdoDUserDetailsService ldoDUserDetailsService) {
        this.ldoDUserDetailsService = ldoDUserDetailsService;
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId)
            throws UsernameNotFoundException, DataAccessException {
        LdoDUserDetails ldoDUserDetails = this.ldoDUserDetailsService
                .loadUserByUsername(userId);
        return (SocialUserDetails) ldoDUserDetails;
    }

}
