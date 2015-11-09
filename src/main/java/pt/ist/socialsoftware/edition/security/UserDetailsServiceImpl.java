package pt.ist.socialsoftware.edition.security;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.Role;

@Service
public class UserDetailsServiceImpl implements SocialUserDetailsService {
    private static Logger log = LoggerFactory
            .getLogger(UserDetailsServiceImpl.class);

    @Override
    public SocialUserDetails loadUserByUserId(String userId)
            throws UsernameNotFoundException, DataAccessException {
        UserDetails userDetails = loadUserByUsername(
                ((LdoDUser) FenixFramework.getDomainObject(userId))
                        .getUsername());
        return new SocialUser(userDetails.getUsername(),
                userDetails.getPassword(), userDetails.getAuthorities());
    }

    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        log.debug("loadUserByUsername username:{}", username);

        UserDetails matchingUser = null;

        LdoD ldoD = LdoD.getInstance();

        for (LdoDUser user : ldoD.getUsersSet()) {

            if (user.getUsername().equals(username)) {
                Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
                for (Role role : user.getRolesSet()) {
                    authorities
                            .add(new GrantedAuthorityImpl(role.getRolename()));
                }
                matchingUser = new UserDetailsImpl(user, user.getUsername(),
                        user.getPassword(), authorities);

                return matchingUser;
            }
        }

        if (matchingUser == null) {
            throw new UsernameNotFoundException("Wrong username or password");
        }

        return matchingUser;
    }

}