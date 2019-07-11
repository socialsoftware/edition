package pt.ist.socialsoftware.edition.ldod.user.feature.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

//@Service
public class UserModuleSocialUserDetailsService implements SocialUserDetailsService {

    private UserModuleUserDetailsService userModuleUserDetailsService;

    public UserModuleSocialUserDetailsService() {
    }

    public UserModuleSocialUserDetailsService(UserModuleUserDetailsService userModuleUserDetailsService) {
        this.userModuleUserDetailsService = userModuleUserDetailsService;
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
        UserModuleUserDetails userModuleUserDetails = this.userModuleUserDetailsService.loadUserByUsername(userId);
        return userModuleUserDetails;
    }

}
