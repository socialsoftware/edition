package pt.ist.socialsoftware.edition.ldod.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Edition_Base;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

public class LdoDUserDto {
    @NotBlank
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private boolean active;
    private String roles;
    private List<String> selectedVE;

    public LdoDUserDto() {
    }

    public LdoDUserDto(LdoDUser user) {
        this.username = user.getUsername();
        this.enabled = user.getEnabled();
        this.active = user.getActive();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        setRoles(user.getListOfRolesAsStrings());
        setSelectedVE(user.getSelectedVirtualEditionsSet()
                .stream()
                .map(Edition_Base::getAcronym)
                .collect(Collectors.toList()));

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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public List<String> getSelectedVE() {
        return selectedVE;
    }

    public void setSelectedVE(List<String> selectedVE) {
        this.selectedVE = selectedVE;
    }

    @Override
    public String toString() {
        return "LdoDUserDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", enabled=" + enabled +
                ", active=" + active +
                ", roles='" + roles + '\'' +
                ", selectedVE=" + selectedVE +
                '}';
    }
}
