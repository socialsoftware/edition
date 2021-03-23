package pt.ist.socialsoftware.edition.user.domain;


public class Role extends Role_Base {

    public enum RoleType {
        ROLE_USER, ROLE_ADMIN
    }

    public static Role getRole(RoleType type) {
        return UserModule.getInstance().getRolesSet().stream().filter(r -> r.getType().equals(type)).findFirst()
                .orElseGet(() -> new Role(type));
    }

    private Role(RoleType type) {
        setUserModule(UserModule.getInstance());
        setType(type);
    }

    public void remove() {
        setUserModule(null);
        getUsersSet().forEach(u -> u.remove());

        deleteDomainObject();
    }

}
