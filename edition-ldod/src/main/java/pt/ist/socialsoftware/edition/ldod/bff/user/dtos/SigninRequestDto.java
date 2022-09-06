package pt.ist.socialsoftware.edition.ldod.bff.user.dtos;

public class SigninRequestDto {
    private  String username;
    private  String password;

    public SigninRequestDto(){}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "SigninRequestDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
