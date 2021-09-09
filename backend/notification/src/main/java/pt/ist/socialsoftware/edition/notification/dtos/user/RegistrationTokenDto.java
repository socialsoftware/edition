package pt.ist.socialsoftware.edition.notification.dtos.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.joda.time.DateTime;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.notification.config.CustomDateTimeDeserializer;
import pt.ist.socialsoftware.edition.notification.utils.Emailer;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.USER_SERVICE_URL;

public class RegistrationTokenDto {


    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl(USER_SERVICE_URL);

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

    @JsonIgnore
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

    @JsonIgnore
    public HashMap<String, String> requestAuthorization(HttpServletRequest servletRequest, Emailer emailer) throws MessagingException {
       return webClientUser.build()
                .post()
                .uri( uriBuilder -> uriBuilder
                    .path("/requestAuthorization")
                    .queryParam("token", token)
                    .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<HashMap<String, String>>() {})
                .block();
        //        this.userProvidesInterface.requestAuthorization(this.token, servletRequest, emailer);
    }

    @JsonIgnore
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

    @JsonIgnore
    public HashMap<String, String> requestConfirmation() throws MessagingException {
       return webClientUser.build()
                .post()
                .uri( uriBuilder -> uriBuilder
                        .path("/requestConfirmation")
                        .queryParam("token", token)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<HashMap<String, String>>() {})
                .block();
        //        this.userProvidesInterface.requestConfirmation(this.token, servletRequest, emailer);
    }

    @JsonIgnore
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

    @JsonIgnore
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
