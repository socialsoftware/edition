package pt.ist.socialsoftware.edition.ldod.bff.user.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;

public class SignupDto {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private boolean conduct;
    private String socialId;
    private String socialMedia;

    public SignupDto() {
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

    public String getSocialId() {
        return socialId;
    }

    public String getProviderId() {
        return "google";
    }

    public Long getExpireTime() {
        return 0L;
    }

    public String getDisplayName() {
        return String.format("%s %s", this.firstName, this.lastName);
    }

    public int getRank() {
        return 1;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public LdoDUser.SocialMediaService getSocialMedia() {
        return socialMedia.equals("")
                ? null
                : LdoDUser.SocialMediaService.valueOf(socialMedia.toUpperCase());
    }

    public void setSocialMedia(String socialMedia) {
        this.socialMedia = socialMedia;
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
                ", socialMediaId='" + socialId + '\'' +
                ", socialMediaService='" + socialMedia + '\'' +
                '}';
    }
}
