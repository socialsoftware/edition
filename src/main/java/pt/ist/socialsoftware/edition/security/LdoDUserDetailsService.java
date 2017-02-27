package pt.ist.socialsoftware.edition.security;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.Role;
import pt.ist.socialsoftware.edition.domain.Role.RoleType;

@Service
public class LdoDUserDetailsService implements UserDetailsService {
	private static Logger log = LoggerFactory.getLogger(LdoDUserDetailsService.class);

	@Override
	public LdoDUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("loadUserByUsername username:{}", username);

		LdoDUserDetails matchingUser = null;

		LdoD ldoD = LdoD.getInstance();

		for (LdoDUser user : ldoD.getUsersSet()) {

			if (user.getEnabled() && user.getActive() && user.getUsername().equals(username)
					&& (!ldoD.getAdmin() || user.getRolesSet().contains(Role.getRole(RoleType.ROLE_ADMIN)))) {
				Set<GrantedAuthority> authorities = new HashSet<>();
				for (Role role : user.getRolesSet()) {
					authorities.add(new GrantedAuthorityImpl(role.getType().name()));
				}
				matchingUser = new LdoDUserDetails(user, user.getUsername(), user.getPassword(), authorities);

				return matchingUser;
			}
		}

		if (matchingUser == null) {
			throw new UsernameNotFoundException(username);
		}

		return matchingUser;
	}

}