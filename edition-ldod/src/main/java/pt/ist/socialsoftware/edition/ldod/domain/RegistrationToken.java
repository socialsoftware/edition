package pt.ist.socialsoftware.edition.ldod.domain;

import org.joda.time.DateTime;
import org.joda.time.Period;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.utils.Emailer;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

public class RegistrationToken extends RegistrationToken_Base {
    private static final int EXPIRATION = 1440;

    public RegistrationToken(String token, LdoDUser user) {
        setUser(user);
        setLdoD(user.getLdoD());
        setToken(token);
        setExpireTimeDateTime(DateTime.now().plus(new Period().withMinutes(EXPIRATION)));
    }

    public DateTime getExpireTimeDateTime() {
        return new DateTime(getExpireTime());
    }

    public void setExpireTimeDateTime(DateTime date) {
        setExpireTime(date.getMillis());
    }

    @Override
    @Atomic(mode = TxMode.WRITE)
    public void setAuthorized(boolean authorized) {
        super.setAuthorized(authorized);
    }

    public void remove() {
        setUser(null);
        setLdoD(null);

        deleteDomainObject();
    }

    public String getAuthUrl(HttpServletRequest request, String target) {


        String referer = request.getHeader("Referer");
        String host = request.getHeader("Host");

        boolean isFromMFES = referer != null && !referer.contains(host);
        return isFromMFES
                ? referer + "user/" + target + "?token=" + getToken()
                : request.getScheme() + "://" + host + "/signup/" + target + "?token=" + getToken();
    }

    public void requestAuthorization(HttpServletRequest request, Emailer emailer) throws MessagingException {
        System.out.println(request.getContextPath());
        System.out.println(request.getServerName());
        System.out.println(request.getServletPath());


        String recipientAddress = PropertiesManager.getProperties()
                .getProperty("registration.authorization.email.address");
        String subject = "LdoD - Autorização de Registo";
        emailer.sendEmail(recipientAddress, subject,
                "Autorize o registo no arquivo do LdoD do utilizador " + getUser().getFirstName() + " "
                        + getUser().getLastName() + " com username " + getUser().getUsername()
                        + " com o endereço de email " + getUser().getEmail() + " nesta ligação <a href=\""
                        + getAuthUrl(request, "sign-up-authorization") + "\">" + getAuthUrl(request, "sign-up-authorization") + "</a>",
                PropertiesManager.getProperties().getProperty("registration.confirmation.email.address"));
    }

    public void requestConfirmation(HttpServletRequest request, Emailer emailer) throws MessagingException {
        String recipientAddress = getUser().getEmail();
        String subject = "LdoD - Confirmação de Registo";
        emailer.sendEmail(recipientAddress, subject,
                "Confirme o registo no arquivo do LdoD do utilizador " + getUser().getUsername()
                        + " com o endereço de email " + getUser().getEmail() + " nesta ligação <a href=\""
                        + getAuthUrl(request, "sign-up-confirmation") + "\">" + getAuthUrl(request, "sign-up-confirmation") + "</a>",
                PropertiesManager.getProperties().getProperty("registration.confirmation.email.address"));
    }

}
