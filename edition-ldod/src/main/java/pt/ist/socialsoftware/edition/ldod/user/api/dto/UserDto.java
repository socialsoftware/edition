package pt.ist.socialsoftware.edition.ldod.user.api.dto;

import org.springframework.security.crypto.password.PasswordEncoder;
import pt.ist.socialsoftware.edition.ldod.domain.User;
import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;

public class UserDto {
    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

    private String username;

    // cached attributes
    private String firstName;

    private String lastName;

    private boolean isEnabled;

    private boolean isActive;

    private String socialMedalId;

    public UserDto(String username) {
        setUsername(username);
    }

    public UserDto(User user) {
        setUsername(user.getUsername());
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.isEnabled = user.getEnabled();
        this.isActive = user.getActive();
        this.socialMedalId = user.getSocialMediaId();
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        //return this.userProvidesInterface.getFirstName(this.username);
        return this.firstName;
    }

    public String getLastName() {
        //return this.userProvidesInterface.getLastName(this.username);
        return this.lastName;
    }

    public boolean isEnabled() {
        //return this.userProvidesInterface.isEnabled(this.username);
        return this.isEnabled;
    }

    public boolean isActive() {
        //return this.userProvidesInterface.isActive(this.username);
        return this.isActive;
    }

    public String getSocialMediaId() {
       //return this.userProvidesInterface.getSocialMediaId(this.username);
       return this.socialMedalId;
    }

    public void updatePassword(PasswordEncoder passwordEncoder, String currentPassword, String newPassword) {
        this.userProvidesInterface.updatePassword(this.username, passwordEncoder, currentPassword, newPassword);
    }
}
