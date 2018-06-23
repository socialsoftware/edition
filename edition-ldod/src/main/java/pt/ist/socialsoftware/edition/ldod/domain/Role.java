package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.domain.Role_Base;

public class Role extends Role_Base {

	public enum RoleType {
		ROLE_USER, ROLE_ADMIN
	};

	public static Role getRole(RoleType type) {
		return LdoD.getInstance().getRolesSet().stream().filter(r -> r.getType().equals(type)).findFirst()
				.orElseGet(() -> new Role(type));
	}

	private Role(RoleType type) {
		setLdoD(LdoD.getInstance());
		setType(type);
	}

}
