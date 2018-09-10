package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.Set;

public class Role extends Role_Base {

	public enum RoleType {
		ROLE_USER, ROLE_ADMIN
	}

	public static Role getRole(RoleType type) {
		UserManager userManager = UserManager.getInstance();
		Set<Role> roles = userManager.getRolesSet();
		return roles.stream().filter(r -> r.getType().equals(type)).findFirst()
				.orElseGet(() -> new Role(type));
	}

	private Role(RoleType type) {
		setUserManager(UserManager.getInstance());
		setType(type);
	}

}
