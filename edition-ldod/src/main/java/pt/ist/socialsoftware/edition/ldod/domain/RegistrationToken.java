package pt.ist.socialsoftware.edition.ldod.domain;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.joda.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.utils.Emailer;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

public class RegistrationToken extends RegistrationToken_Base {
	private static final int EXPIRATION = 1440;

//	@Autowired
//	private Emailer emailer;

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

	public void requestAuthorization(HttpServletRequest request) throws AddressException, MessagingException {
		String recipientAddress = PropertiesManager.getProperties()
				.getProperty("registration.authorization.email.address");

		String subject = "LdoD - Autorização de Registo";

		String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
		String authorizationUrl = path + "/signup/registrationAuthorization?token=" + getToken();

		Emailer emailer = new Emailer();
		emailer.sendEmail(recipientAddress, subject,
				"Autorize o registo no arquivo do LdoD do utilizador " + getUser().getFirstName() + " "
						+ getUser().getLastName() + " com username " + getUser().getUsername()
						+ " com o endereço de email " + getUser().getEmail() + " nesta ligação <a href=\""
						+ authorizationUrl + "\">" + authorizationUrl + "</a>",
				PropertiesManager.getProperties().getProperty("registration.confirmation.email.address"));
	}

	public void requestConfirmation(HttpServletRequest request) throws AddressException, MessagingException {
		String recipientAddress = getUser().getEmail();

		String subject = "LdoD - Confirmação de Registo";

		String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
		String confirmationUrl = path + "/signup/registrationConfirm?token=" + getToken();

		Emailer emailer = new Emailer();
		emailer.sendEmail(recipientAddress, subject,
				"Confirme o registo no arquivo do LdoD do utilizador " + getUser().getUsername()
						+ " com o endereço de email " + getUser().getEmail() + " nesta ligação <a href=\""
						+ confirmationUrl + "\">" + confirmationUrl + "</a>",
				PropertiesManager.getProperties().getProperty("registration.confirmation.email.address"));
	}

}
