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

    private Boolean isEnabled;

    private Boolean isActive;

    private String socialMedalId;

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
        if (firstName == null)
            firstName = this.userProvidesInterface.getFirstName(this.username);
        return firstName;
    }

    public String getLastName() {
        if (lastName == null)
            lastName = this.userProvidesInterface.getLastName(this.username);
        return lastName;
    }

    public boolean isEnabled() {
        if (isEnabled == null)
            isEnabled = this.userProvidesInterface.isEnabled(this.username);
        return isEnabled;
    }

    public boolean isActive() {
        if (isActive == null)
            isActive = this.userProvidesInterface.isActive(this.username);
        return isActive;
    }

    public String getSocialMediaId() {
        if (socialMedalId == null)
            socialMedalId = this.userProvidesInterface.getSocialMediaId(this.username);
        return socialMedalId;
    }

    public void updatePassword(PasswordEncoder passwordEncoder, String currentPassword, String newPassword) {
        this.userProvidesInterface.updatePassword(this.username, passwordEncoder, currentPassword, newPassword);
    }
}
