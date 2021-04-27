package pt.ist.socialsoftware.edition.ldod.frontend.user.dto;

import org.joda.time.LocalDate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.enums.Role_Type;

import java.util.List;

public class UserDto {
    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl("http://localhost:8082/api");

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

    public void updatePassword(PasswordEncoder passwordEncoder, String currentPassword, String newPassword) {
        webClientUser.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/updatePassword")
                        .queryParam("username", username)
                    .queryParam("currentPassword", passwordEncoder.encode(currentPassword))
                    .queryParam("newPassword", passwordEncoder.encode(newPassword))
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.userProvidesInterface.updatePassword(this.username, passwordEncoder, currentPassword, newPassword);
    }

    public boolean hasRoleTypeAdmin() {
        return webClientUser.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/hasRoleAdmin")
                    .queryParam("username", this.username)
                    .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional()
                .orElse(false);
        //        return this.userProvidesInterface.hasRoleTypeAdmin(this.username);
    }

    public boolean hasRoleTypeUser() {
        return webClientUser.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/hasRoleUser")
                        .queryParam("username", this.username)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional()
                .orElse(false);
        //        return this.userProvidesInterface.hasRoleTypeUser(this.username);
    }

    public List<String> getRolesSet() {
        return webClientUser.build()
                .get()
                .uri("/user/" + username + "/roles")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
        //        return this.userProvidesInterface.getRolesFromUser(this.username);
    }


    public RegistrationTokenDto createRegistrationToken(String toString) {
        return webClientUser.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/createRegistrationToken")
                    .queryParam("username", username)
                    .queryParam("toString", toString)
                    .build())
                .retrieve()
                .bodyToMono(RegistrationTokenDto.class)
                .blockOptional()
                .orElse(null);
        //        return this.userProvidesInterface.createRegistrationToken(this.username, toString);
    }

    public void enableUnconfirmedUser() {
        webClientUser.build()
                .post()
                .uri("/user/" + this.username + "/enableUnconfirmed")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.userProvidesInterface.enableUnconfirmedUser(this.username);
    }


    public void update(PasswordEncoder passwordEncoder, String oldUsername, String newUsername, String firstName, String lastName, String email, String newPassword, boolean user, boolean admin, boolean enabled) {
        webClientUser.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/updateUserInfo")
                    .queryParam("username", this.username)
                    .queryParam("oldUsername", oldUsername)
                    .queryParam("newUsername", newUsername)
                    .queryParam("firstName", firstName)
                    .queryParam("lastName", lastName)
                    .queryParam("email", email)
                    .queryParam("newPassword", passwordEncoder.encode(newPassword))
                    .queryParam("user", user)
                    .queryParam("admin", admin)
                    .queryParam("enabled", enabled)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.userProvidesInterface.updateUserInfo(this.username, passwordEncoder, oldUsername, newUsername, firstName, lastName, email, newPassword, user, admin, enabled);
    }

    public void switchActive() {
        webClientUser.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/switchUserActive")
                    .queryParam("username", username)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.userProvidesInterface.switchUserActive(this.username);
    }

    public void removeUser() {
        webClientUser.build()
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/removeUser")
                        .queryParam("username", username)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.userProvidesInterface.removeUser(this.username);
    }

    public String getListOfRolesAsStrings() {
        return webClientUser.build()
                .get()
                .uri("/user/"+ this.username + "/listOfRoles")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional()
                .orElse("");
        //        return this.userProvidesInterface.getUserListofRoleAsStrings(this.username);
    }

    public LocalDate getLastLogin() {
        return webClientUser.build()
                .get()
                .uri("/user/"+ this.username + "/lastLogin")
                .retrieve()
                .bodyToMono(LocalDate.class)
                .blockOptional()
                .orElse(LocalDate.now());
        //        return this.userProvidesInterface.getUserLastLogin(username);
    }

    public void setUserEnabled(boolean enabled) {
        webClientUser.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/user/" + this.username + "/enabled")
                    .queryParam("enabled", enabled)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .blockOptional();
    }

    public void addRolesToUser(Role_Type roleUser) {
        webClientUser.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/user/" + this.username + "/addRoles")
                        .queryParam("roleUser", roleUser)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .blockOptional();
    }

    public RegistrationTokenDto getToken() {
        return webClientUser.build()
                .get()
                .uri("/user/" + this.username + "/token")
                .retrieve()
                .bodyToMono(RegistrationTokenDto.class)
                .blockOptional()
                .orElse(null);
    }
}
