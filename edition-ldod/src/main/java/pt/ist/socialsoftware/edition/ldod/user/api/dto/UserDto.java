package pt.ist.socialsoftware.edition.ldod.user.api.dto;

import org.springframework.security.crypto.password.PasswordEncoder;
import pt.ist.socialsoftware.edition.ldod.domain.User;
import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;
import sun.security.util.Password;

public class UserDto {
    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

    private String username;

    public UserDto(String username) {
        setUsername(username);
    }

    public UserDto(User user) {
        setUsername(user.getUsername());
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getFirstName() {
        return this.userProvidesInterface.getFirstName(this.username);
    }

    public String getLastName() {
        return this.userProvidesInterface.getLastName(this.username);
    }

    public boolean isEnabled() {
        return this.userProvidesInterface.isEnabled(this.username);
    }

    public boolean isActive() {
        return this.userProvidesInterface.isActive(this.username);
    }

    public void updatePassword(PasswordEncoder passwordEncoder, String currentPassword, String newPassword){
        this.userProvidesInterface.updatePassword(this.username, passwordEncoder, currentPassword, newPassword);
    }
}
