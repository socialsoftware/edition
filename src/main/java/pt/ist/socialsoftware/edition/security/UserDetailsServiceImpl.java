package pt.ist.socialsoftware.edition.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.Role;

@Service("myUserDetailService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

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