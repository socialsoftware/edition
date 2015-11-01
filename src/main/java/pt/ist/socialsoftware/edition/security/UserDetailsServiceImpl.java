package pt.ist.socialsoftware.edition.security;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.Role;

@Repository
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static Logger log = LoggerFactory
            .getLogger(UserDetailsServiceImpl.class);

    @Override
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