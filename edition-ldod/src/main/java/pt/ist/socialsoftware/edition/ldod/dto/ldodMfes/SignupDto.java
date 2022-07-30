package pt.ist.socialsoftware.edition.ldod.dto.ldodMfes;

import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;

public class SignupDto {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private boolean conduct ;
    private String socialMediaId;
    private String socialMediaService;

    public SignupDto() {}

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isConduct() {
        return conduct;
    }

    public void setConduct(boolean conduct) {
        this.conduct = conduct;
    }

    public String getSocialMediaId() {
        return socialMediaId;
    }

    public void setSocialMediaId(String socialMediaId) {
        this.socialMediaId = socialMediaId;
    }

    public LdoDUser.SocialMediaService getSocialMediaService() {
        return socialMediaService.equals("")
                ? null
                : LdoDUser.SocialMediaService.valueOf(socialMediaService.toUpperCase());
    }

    public void setSocialMediaService(String socialMediaService) {
        this.socialMediaService = socialMediaService;
    }

    @Override
    public String toString() {
        return "SignupDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", conduct=" + conduct +
                ", socialMediaId='" + socialMediaId + '\'' +
                ", socialMediaService='" + socialMediaService + '\'' +
                '}';
    }
}
