package pt.ist.socialsoftware.edition.ldod.security;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUserManager;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualManager;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.user.domain.Role;
import pt.ist.socialsoftware.edition.user.domain.User;
import pt.ist.socialsoftware.edition.user.domain.UserManager;
import pt.ist.socialsoftware.edition.user.security.LdoDUserDetails;

@Service
public class LdoDUserDetailsService implements UserDetailsService {
	private static Logger log = LoggerFactory.getLogger(LdoDUserDetailsService.class);

	@Override
	@Atomic(mode = TxMode.READ)
	public LdoDUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("loadUserByUsername username:{}", username);

		LdoDUserDetails matchingUser = null;

		UserManager userManager = LdoDUserManager.getInstance();

		for (User user : userManager.getUsersSet()) {

			if (user.getEnabled() && user.getActive() && user.getUsername().equals(username)
					&& (!userManager.getAdmin() || user.getRolesSet().contains(Role.getRole(Role.RoleType.ROLE_ADMIN)))) {
				Set<GrantedAuthority> authorities = new HashSet<>();
				for (Role role : user.getRolesSet()) {
					authorities.add(new GrantedAuthorityImpl(role.getType().name()));
				}
				matchingUser = new LdoDUserDetails((LdoDUser) user, user.getUsername(), user.getPassword(), authorities);

				return matchingUser;
			}
		}

		if (matchingUser == null) {
			throw new UsernameNotFoundException(username);
		}

		return matchingUser;
	}

}