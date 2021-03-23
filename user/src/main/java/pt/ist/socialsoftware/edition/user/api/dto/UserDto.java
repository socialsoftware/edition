package pt.ist.socialsoftware.edition.user.api.dto;

import org.joda.time.LocalDate;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.ist.socialsoftware.edition.user.domain.RegistrationToken;
import pt.ist.socialsoftware.edition.user.domain.User;
import pt.ist.socialsoftware.edition.user.api.UserProvidesInterface;

public class UserDto {
    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

    private String username;

    // cached attributes
    private String firstName;

    private String lastName;

    private boolean isEnabled;

    private boolean isActive;

    private String socialMedalId;

    private String externalId;

    private String password;

    private String email;

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
        this.externalId = user.getExternalId();
        this.password = user.getPassword();
        this.email = user.getEmail();
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

    public String getPassword() {
        return this.password;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public String getEmail() {
        return email;
    }

    public void updatePassword(PasswordEncoder passwordEncoder, String currentPassword, String newPassword) {
        this.userProvidesInterface.updatePassword(this.username, passwordEncoder, currentPassword, newPassword);
    }

    public boolean hasRoleTypeAdmin() {
        return this.userProvidesInterface.hasRoleTypeAdmin(this.username);
    }

    public boolean hasRoleTypeUser() {
        return this.userProvidesInterface.hasRoleTypeUser(this.username);
    }

    public String[] getRolesSet() {
        return this.userProvidesInterface.getRolesFromUser(this.username);
    }


    public RegistrationTokenDto createRegistrationToken(String toString) {
        return this.userProvidesInterface.createRegistrationToken(this.username, toString);
    }

    public void enableUnconfirmedUser() {
        this.userProvidesInterface.enableUnconfirmedUser(this.username);
    }


    public void update(PasswordEncoder passwordEncoder, String oldUsername, String newUsername, String firstName, String lastName, String email, String newPassword, boolean user, boolean admin, boolean enabled) {
        this.userProvidesInterface.updateUserInfo(this.username, passwordEncoder, oldUsername, newUsername, firstName, lastName, email, newPassword, user, admin, enabled);
    }

    public void switchActive() {
        this.userProvidesInterface.switchUserActive(this.username);
    }

    public void removeUser() {
        this.userProvidesInterface.removeUser(this.username);
    }

    public String getListOfRolesAsStrings() {
        return this.userProvidesInterface.getUserListofRoleAsStrings(this.username);
    }

    public LocalDate getLastLogin() {
        return this.userProvidesInterface.getUserLastLogin(username);
    }
}
