package pt.ist.socialsoftware.edition.ldod.bff.user.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.Edition_Base;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LdoDUserDto {

    private String externalId;
    private String oldUsername;
    private String newUsername;

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String newPassword;
    private String lastLogin;
    private boolean active;

    private String roles;

    private List<String> selectedVE;

    private boolean user;
    private boolean admin;
    private boolean enabled;

    public LdoDUserDto() {
    }

    public LdoDUserDto(LdoDUser user) {
        setExternalId(user.getExternalId());
        setUsername(user.getUsername());
        setEnabled(user.getEnabled());
        setActive(user.getActive());
        setLastLogin(Optional.ofNullable(user.getLastLogin()).isPresent() ? user.getLastLogin().toString() : "");
        setEmail(user.getEmail());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setRoles(user.getListOfRolesAsStrings());
        setSelectedVE(user.getSelectedVirtualEditionsSet()
                .stream()
                .map(Edition_Base::getAcronym)
                .collect(Collectors.toList()));

    }

    public String getExternalId() {
        return externalId;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getOldUsername() {
        return oldUsername;
    }

    public void setOldUsername(String oldUsername) {
        this.oldUsername = oldUsername;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public boolean isUser() {
        return user;
    }

    public void setUser(boolean user) {
        this.user = user;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isActive() {
        return active;
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
                "oldUsername='" + oldUsername + '\'' +
                ", newUsername='" + newUsername + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", user=" + user +
                ", admin=" + admin +
                ", enabled=" + enabled +
                '}';
    }

    public static final class LdoDUserDtoBuilder {
        private String oldUsername;
        private String newUsername;
        private String firstName;
        private String lastName;
        private String email;
        private String newPassword;
        private boolean user;
        private boolean admin;
        private boolean enabled;

        private LdoDUserDtoBuilder() {
        }

        public static LdoDUserDtoBuilder aLdoDUserDto() {
            return new LdoDUserDtoBuilder();
        }

        public LdoDUserDtoBuilder oldUsername(String oldUsername) {
            this.oldUsername = oldUsername;
            return this;
        }

        public LdoDUserDtoBuilder newUsername(String newUsername) {
            this.newUsername = newUsername;
            return this;
        }

        public LdoDUserDtoBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public LdoDUserDtoBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public LdoDUserDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public LdoDUserDtoBuilder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public LdoDUserDtoBuilder user(boolean user) {
            this.user = user;
            return this;
        }

        public LdoDUserDtoBuilder admin(boolean admin) {
            this.admin = admin;
            return this;
        }

        public LdoDUserDtoBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public LdoDUserDto build() {
            LdoDUserDto ldoDUserDto = new LdoDUserDto();
            ldoDUserDto.setOldUsername(oldUsername);
            ldoDUserDto.setNewUsername(newUsername);
            ldoDUserDto.setFirstName(firstName);
            ldoDUserDto.setLastName(lastName);
            ldoDUserDto.setEmail(email);
            ldoDUserDto.setNewPassword(newPassword);
            ldoDUserDto.setUser(user);
            ldoDUserDto.setAdmin(admin);
            ldoDUserDto.setEnabled(enabled);
            return ldoDUserDto;
        }
    }
}
