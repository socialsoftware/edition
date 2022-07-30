package pt.ist.socialsoftware.edition.ldod.security;

import java.util.Collection;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.security.SocialUserDetails;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;

public class LdoDUserDetails implements SocialUserDetails {
	private static Logger log = LoggerFactory.getLogger(LdoDUserDetails.class);

	private static final long serialVersionUID = -6509897037222767090L;

	private Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
	private final String password;
	private final String username;
	private final String userId;

	public LdoDUserDetails(LdoDUser user, String username, String password, Collection<GrantedAuthority> authorities) {
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
		// log.debug("getUser userId:{}", userId);
		return FenixFramework.getDomainObject(userId);
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

	@Override
	public String getUserId() {
		return getUser().getSocialMediaId();
	}

	public boolean hasRole(String role) {
		return getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
	}

}