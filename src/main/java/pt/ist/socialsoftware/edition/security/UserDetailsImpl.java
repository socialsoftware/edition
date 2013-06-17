package pt.ist.socialsoftware.edition.security;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.LdoDUser;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = -6509897037222767090L;

	private Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
	private final String password;
	private final String username;
	private final String userId;

	public UserDetailsImpl(LdoDUser user, String username, String password,
			Collection<GrantedAuthority> authorities) {
		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.userId = user.getExternalId();
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public LdoDUser getUser() {
		return (LdoDUser) FenixFramework.getDomainObject(userId);
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}