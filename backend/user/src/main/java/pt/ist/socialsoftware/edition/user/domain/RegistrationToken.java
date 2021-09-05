package pt.ist.socialsoftware.edition.user.domain;

import org.joda.time.DateTime;
import org.joda.time.Period;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.notification.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.user.domain.RegistrationToken_Base;
import pt.ist.socialsoftware.edition.user.utils.Emailer;


import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public class RegistrationToken extends RegistrationToken_Base {
    private static final int EXPIRATION = 1440;

    public RegistrationToken(String token, User user) {
        setUser(user);
        setUserModule(user.getUserModule());
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
        setUserModule(null);

        deleteDomainObject();
    }

    public HashMap<String, String> requestAuthorization(HttpServletRequest request, Emailer emailer) throws AddressException, MessagingException {

        HashMap<String, String> map = new HashMap<>();

        String recipientAddress = PropertiesManager.getProperties()
                .getProperty("registration.authorization.email.address");

        String subject = "VirtualModule - Autorização de Registo";

        String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
        String authorizationUrl = path + "/signup/registrationAuthorization?token=" + getToken();

        map.put("to", recipientAddress);
        map.put("subject", subject);
        map.put("msg", "Autorize o registo no arquivo do VirtualModule do utilizador " + getUser().getFirstName() + " "
                        + getUser().getLastName() + " com username " + getUser().getUsername()
                        + " com o endereço de email " + getUser().getEmail() + " nesta ligação <a href=\""
                        + authorizationUrl + "\">" + authorizationUrl + "</a>");
        map.put("from", PropertiesManager.getProperties().getProperty("registration.confirmation.email.address"));
        return map;

//        emailer.sendEmail(recipientAddress, subject,
//                "Autorize o registo no arquivo do VirtualModule do utilizador " + getUser().getFirstName() + " "
//                        + getUser().getLastName() + " com username " + getUser().getUsername()
//                        + " com o endereço de email " + getUser().getEmail() + " nesta ligação <a href=\""
//                        + authorizationUrl + "\">" + authorizationUrl + "</a>",
//                PropertiesManager.getProperties().getProperty("registration.confirmation.email.address"));
    }

    public HashMap<String, String> requestConfirmation(HttpServletRequest request, Emailer emailer) throws AddressException, MessagingException {
        HashMap<String, String> map = new HashMap<>();

        String recipientAddress = getUser().getEmail();

        String subject = "VirtualModule - Confirmação de Registo";

        String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
        String confirmationUrl = path + "/signup/registrationConfirm?token=" + getToken();

        map.put("to", recipientAddress);
        map.put("subject", subject);
        map.put("msg", "Confirme o registo no arquivo do VirtualModule do utilizador " + getUser().getUsername()
                + " com o endereço de email " + getUser().getEmail() + " nesta ligação <a href=\""
                + confirmationUrl + "\">" + confirmationUrl + "</a>");
        map.put("from", PropertiesManager.getProperties().getProperty("registration.confirmation.email.address"));

        return map;
//        emailer.sendEmail(recipientAddress, subject,
//                "Confirme o registo no arquivo do VirtualModule do utilizador " + getUser().getUsername()
//                        + " com o endereço de email " + getUser().getEmail() + " nesta ligação <a href=\""
//                        + confirmationUrl + "\">" + confirmationUrl + "</a>",
//                PropertiesManager.getProperties().getProperty("registration.confirmation.email.address"));
    }

}
