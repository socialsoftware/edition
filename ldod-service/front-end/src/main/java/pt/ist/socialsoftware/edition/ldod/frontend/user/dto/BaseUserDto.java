package pt.ist.socialsoftware.edition.ldod.frontend.user.dto;

public class BaseUserDto {


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

        public BaseUserDto(String username) {
            setUsername(username);
        }

        public BaseUserDto() {}

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

}
