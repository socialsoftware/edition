package pt.ist.socialsoftware.edition.core.dto;

public class LdoDUserDTO {
    private long id;
    private String username;
    private String password;
    private boolean enabled;

    public LdoDUserDTO() {
    }

    public LdoDUserDTO(String username, String password) {
        this.username = username;
        this.password = password;
        this.enabled = true;
    }


    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
