package pt.ist.socialsoftware.edition.api.userDto;

import org.joda.time.DateTime;
import org.springframework.web.reactive.function.client.WebClient;

public class RegistrationTokenDto {

//    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl("http://localhost:8082/api");
    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl("http://docker-user:8082/api");



    private String token;
    private DateTime expireTimeDateTime;
    private boolean authorized;

    public RegistrationTokenDto() {
    }

    public String getToken() {
        return this.token;
    }

    public DateTime getExpireTimeDateTime() {
        return expireTimeDateTime;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpireTimeDateTime(DateTime expireTimeDateTime) {
        this.expireTimeDateTime = expireTimeDateTime;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }



    public void setTokenAuthorized(boolean authorized) {
        webClientUser.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/setAuthorized")
                    .queryParam("token", token)
                    .queryParam("authorized", authorized)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.userProvidesInterface.setAuthorized(this.token, authorized);
    }



    public UserDto getUser() {
        return webClientUser.build()
                .get()
                .uri("/registrationToken/" + this.token + "/user")
                .retrieve()
                .bodyToMono(UserDto.class)
                .blockOptional()
                .orElse(null);
        //        return this.userProvidesInterface.getUserFromRegistrationToken(this.token);
    }
}
