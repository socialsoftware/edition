package pt.ist.socialsoftware.edition.notification.dtos.virtual;


public class MemberDto {

    private String user;
    private String role;

    public MemberDto() {

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean hasRole(String role) {
        return this.role.equals(role);
    }

}
