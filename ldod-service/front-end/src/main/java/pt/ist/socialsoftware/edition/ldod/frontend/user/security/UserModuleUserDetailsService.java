package pt.ist.socialsoftware.edition.ldod.frontend.user.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.UserDto;

import java.util.HashSet;
import java.util.Set;

//@Service
public class UserModuleUserDetailsService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserModuleUserDetailsService.class);

    private FeUserRequiresInterface userRequiresInterface = new FeUserRequiresInterface();

    @Override
    @Atomic(mode = TxMode.READ)
    public UserModuleUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("loadUserByUsername username:{}", username);

        UserModuleUserDetails matchingUser = null;

    //    UserModule userModule = UserModule.getInstance();

//        for (User user : userModule.getUsersSet()) {
//
//            if (user.getEnabled() && user.getActive() && user.getUsername().equals(username)
//                    && (!userModule.getAdmin() || user.getRolesSet().contains(Role.getRole(RoleType.ROLE_ADMIN)))) {
//                Set<GrantedAuthority> authorities = new HashSet<>();
//                for (Role role : user.getRolesSet()) {
//                    authorities.add(new GrantedAuthorityImpl(role.getType().name()));
//                }
//                matchingUser = new UserModuleUserDetails(user, user.getUsername(), user.getPassword(), authorities);
//
//                return matchingUser;
//            }
//        }

        for (UserDto user : this.userRequiresInterface.getUsersSet()) {

            if (user.isEnabled() && user.isActive() && user.getUsername().equals(username)
                    && (!this.userRequiresInterface.getAdmin() || user.hasRoleTypeAdmin())) {
                Set<GrantedAuthority> authorities = new HashSet<>();
                for (String role : user.getRolesSet()) {
                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA\n");
                    System.out.println(role);
                    authorities.add(new GrantedAuthorityImpl(role));
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