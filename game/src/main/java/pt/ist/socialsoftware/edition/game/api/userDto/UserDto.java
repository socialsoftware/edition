package pt.ist.socialsoftware.edition.game.api.userDto;

import org.joda.time.LocalDate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class UserDto {
    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl("http://localhost:8082/api");
//    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl("http://docker-user:8082/api");


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

    public UserDto() {
    }

    public UserDto(String username) {
        setUsername(username);
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setSocialMedalId(String socialMedalId) {
        this.socialMedalId = socialMedalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
