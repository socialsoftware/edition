package pt.ist.socialsoftware.edition.ldod.user.feature.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.Role;
import pt.ist.socialsoftware.edition.ldod.domain.Role.RoleType;
import pt.ist.socialsoftware.edition.ldod.domain.User;
import pt.ist.socialsoftware.edition.ldod.domain.UserModule;

import java.util.HashSet;
import java.util.Set;

//@Service
public class UserModuleUserDetailsService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserModuleUserDetailsService.class);

    @Override
    @Atomic(mode = TxMode.READ)
    public UserModuleUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("loadUserByUsername username:{}", username);

        UserModuleUserDetails matchingUser = null;

        UserModule userModule = UserModule.getInstance();

        for (User user : userModule.getUsersSet()) {

            if (user.getEnabled() && user.getActive() && user.getUsername().equals(username)
                    && (!userModule.getAdmin() || user.getRolesSet().contains(Role.getRole(RoleType.ROLE_ADMIN)))) {
                Set<GrantedAuthority> authorities = new HashSet<>();
                for (Role role : user.getRolesSet()) {
                    authorities.add(new GrantedAuthorityImpl(role.getType().name()));
                }
                matchingUser = new UserModuleUserDetails(user, user.getUsername(), user.getPassword(), authorities);

                return matchingUser;
            }
        }

        if (matchingUser == null) {
            throw new UsernameNotFoundException(username);
        }

        return matchingUser;
    }

}