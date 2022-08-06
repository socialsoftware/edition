package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;

import java.util.Optional;

public class UserDto {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;
    private String listOfRoles;
    private String lastLogin;
    private boolean active;
    private String externalId;

    public UserDto(LdoDUser user) {
        this.setUserName(user.getUsername());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setEmail(user.getEmail());
        this.setEnabled(user.getEnabled());
        this.setListOfRoles(user.getListOfRolesAsStrings());
        this.setLastLogin(Optional.ofNullable(user.getLastLogin()).isPresent() ? user.getLastLogin().toString() : "");
        this.setActive(user.getActive());
        this.setExternalId(user.getExternalId());
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getListOfRoles() {
        return listOfRoles;
    }

    public void setListOfRoles(String listOfRoles) {
        this.listOfRoles = listOfRoles;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
