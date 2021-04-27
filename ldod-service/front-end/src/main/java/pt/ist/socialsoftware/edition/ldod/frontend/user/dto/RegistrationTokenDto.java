package pt.ist.socialsoftware.edition.ldod.frontend.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.joda.time.DateTime;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.ldod.frontend.config.CustomDateTimeDeserializer;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.Emailer;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

public class RegistrationTokenDto {

    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl("http://localhost:8082/api");


    private String token;

    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime expireTimeDateTime;

    private boolean authorized;

    public RegistrationTokenDto() {
    }

    public String getToken() {
        return this.token;
    }


    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
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

    public void updateExpireTimeDateTime(int i, int i1, int i2, int i3, int i4, int i5, int i6) {
        webClientUser.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                .path("/token/" + this.token + "/updateExpireTimeDateTime")
                .queryParam("i", i)
                .queryParam("i1", i1)
                .queryParam("i2", i2)
                .queryParam("i3", i3)
                .queryParam("i4", i4)
                .queryParam("i5", i5)
                        .queryParam("i6", i6)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void requestAuthorization(HttpServletRequest servletRequest, Emailer emailer) throws MessagingException {
        webClientUser.build()
                .post()
                .uri( uriBuilder -> uriBuilder
                    .path("/requestAuthorization")
                    .queryParam("token", token)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.userProvidesInterface.requestAuthorization(this.token, servletRequest, emailer);
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

    public void requestConfirmation(HttpServletRequest servletRequest, Emailer emailer) throws MessagingException {
        webClientUser.build()
                .post()
                .uri( uriBuilder -> uriBuilder
                        .path("/requestConfirmation")
                        .queryParam("token", token)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.userProvidesInterface.requestConfirmation(this.token, servletRequest, emailer);
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

    public void removeToken() {
        webClientUser.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/removeToken")
                    .queryParam("token", token)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
