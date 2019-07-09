package pt.ist.socialsoftware.edition.ldod.api.user.dto;

import pt.ist.socialsoftware.edition.ldod.api.user.UserInterface;
import pt.ist.socialsoftware.edition.ldod.domain.User;

public class UserDto {
    private final UserInterface userInterface = new UserInterface();

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
        return this.userInterface.getFirstName(this.username);
    }

    public String getLastName() {
        return this.userInterface.getLastName(this.username);
    }

    public boolean isEnabled() {
        return this.userInterface.isEnabled(this.username);
    }

    public boolean isActive() {
        return this.userInterface.isActive(this.username);
    }
}
