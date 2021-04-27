package pt.ist.socialsoftware.edition.user.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;
import pt.ist.socialsoftware.edition.user.config.CustomDateSerializer;
import pt.ist.socialsoftware.edition.user.domain.RegistrationToken;


public class RegistrationTokenDto {

    private String token;
    private DateTime expireTimeDateTime;
    private boolean authorized;

    public RegistrationTokenDto(RegistrationToken registrationToken) {
        this.token = registrationToken.getToken();
        this.expireTimeDateTime = registrationToken.getExpireTimeDateTime();
        this.authorized = registrationToken.getAuthorized();
    }

    public String getToken() {
        return this.token;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    public DateTime getExpireTimeDateTime() {
        return expireTimeDateTime;
    }

    public boolean isAuthorized() {
        return authorized;
    }

}
