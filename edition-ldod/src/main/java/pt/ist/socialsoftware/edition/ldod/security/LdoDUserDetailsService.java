package pt.ist.socialsoftware.edition.ldod.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.Role;
import pt.ist.socialsoftware.edition.ldod.domain.Role.RoleType;

import java.util.stream.Collectors;

//@Service
public class LdoDUserDetailsService implements UserDetailsService {
    private static Logger log = LoggerFactory.getLogger(LdoDUserDetailsService.class);

    @Override
    @Atomic(mode = TxMode.READ)
    public LdoDUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("loadUserByUsername username:{}", username);

        LdoD ldoD = LdoD.getInstance();

        LdoDUser matchingUser = ldoD.getUsersSet()
                .stream()
                .filter(user ->
                        user.getEnabled() && user.getActive() && user.getUsername().equals(username) &&
                                (!ldoD.getAdmin() || user.getRolesSet().contains(Role.getRole(RoleType.ROLE_ADMIN))))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new LdoDUserDetails(
                matchingUser,
                username,
                matchingUser.getPassword(),
                matchingUser.getRolesSet().stream().map(role -> new GrantedAuthorityImpl(role.getType().name())).collect(Collectors.toSet()));
    }

}