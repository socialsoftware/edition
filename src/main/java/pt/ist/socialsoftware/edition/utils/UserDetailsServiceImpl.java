package pt.ist.socialsoftware.edition.utils;

import java.util.HashSet;
import java.util.Set;

import jvstm.Transaction;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.Role;
import pt.ist.socialsoftware.edition.domain.User;

@Service("myUserDetailService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		UserDetails matchingUser = null;

		Transaction.begin();

		LdoD ldoD = FenixFramework.getRoot();

		for (User user : ldoD.getUsers()) {

			if (user.getUsername().equals(username)) {
				Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
				for (Role role : user.getRoles()) {
					authorities
							.add(new GrantedAuthorityImpl(role.getRolename()));
				}
				matchingUser = new UserDetailsImpl(user.getUsername(),
						user.getPassword(), authorities);

				Transaction.commit();

				return matchingUser;
			}
		}

		Transaction.commit();

		if (matchingUser == null) {
			throw new UsernameNotFoundException("Wrong username or password");
		}

		return matchingUser;
	}
}