package pt.ist.socialsoftware.edition.ldod.domain;

public class Role extends Role_Base {

	public enum RoleType {
		ROLE_USER, ROLE_ADMIN
	};

	public static Role getRole(RoleType type) {
		return Text.getInstance().getRolesSet().stream().filter(r -> r.getType().equals(type)).findFirst()
				.orElseGet(() -> new Role(type));
	}

	private Role(RoleType type) {
		setText(Text.getInstance());
		setType(type);
	}

	public void remove() {
		setText(null);
		getUsersSet().forEach(u -> u.remove());

		deleteDomainObject();
	}

}
