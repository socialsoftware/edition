package pt.ist.socialsoftware.edition.virtual.api.dto;



import pt.ist.socialsoftware.edition.notification.dtos.user.UserDto;

import javax.validation.constraints.NotBlank;

public class LdoDUserViewDto {
    @NotBlank
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private boolean active;

    public LdoDUserViewDto() {
    }

    public LdoDUserViewDto(UserDto user) {
        this.username = user.getUsername();
        this.enabled = user.isEnabled();
        this.active = user.isActive();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    public LdoDUserViewDto(String user) {
        UserDto userDto = new UserDto(user);
        this.username = user;
        this.enabled = userDto.isEnabled();
        this.active = userDto.isActive();
        this.firstName = userDto.getFirstName();
        this.lastName = userDto.getLastName();
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
