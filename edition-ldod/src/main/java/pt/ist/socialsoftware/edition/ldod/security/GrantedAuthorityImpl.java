package pt.ist.socialsoftware.edition.ldod.security;

import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityImpl implements GrantedAuthority {
	private static final long serialVersionUID = 1029928088340565343L;

	private final String rolename;

	public GrantedAuthorityImpl(String rolename) {
		this.rolename = rolename;
	}

	@Override
	public String getAuthority() {
		return this.rolename;
	}

}