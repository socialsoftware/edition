package pt.ist.socialsoftware.edition.user.api.dto;

import org.joda.time.DateTime;
import pt.ist.socialsoftware.edition.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.user.domain.RegistrationToken;
import pt.ist.socialsoftware.edition.user.utils.Emailer;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

public class RegistrationTokenDto {

    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

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

    public DateTime getExpireTimeDateTime() {
        return expireTimeDateTime;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void requestAuthorization(HttpServletRequest servletRequest, Emailer emailer) throws MessagingException {
        this.userProvidesInterface.requestAuthorization(this.token, servletRequest, emailer);
    }

    public void setAuthorized(boolean authorized) {
        this.userProvidesInterface.setAuthorized(this.token, authorized);
    }

    public void requestConfirmation(HttpServletRequest servletRequest, Emailer emailer) throws MessagingException {
        this.userProvidesInterface.requestConfirmation(this.token, servletRequest, emailer);
    }

    public UserDto getUser() {
        return this.userProvidesInterface.getUserFromRegistrationToken(this.token);
    }
}
