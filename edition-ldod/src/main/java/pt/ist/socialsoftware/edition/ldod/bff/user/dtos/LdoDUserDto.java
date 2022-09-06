package pt.ist.socialsoftware.edition.ldod.bff.user.dtos;

public class LdoDUserDto {

    private String oldUsername;
    private String newUsername;
    private String firstName;
    private String lastName;
    private String email;
    private String newPassword;

    private boolean user;
    private boolean admin;
    private boolean enabled;

    public LdoDUserDto() {
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
