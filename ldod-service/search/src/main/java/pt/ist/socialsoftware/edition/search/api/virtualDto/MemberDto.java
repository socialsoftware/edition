package pt.ist.socialsoftware.edition.search.api.virtualDto;


public class MemberDto {

    private String user;
    private String role;

    public MemberDto() {

    }

    public String getUser() {
        return user;
    }

    public boolean hasRole(String role) {
        return this.role.equals(role);
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
