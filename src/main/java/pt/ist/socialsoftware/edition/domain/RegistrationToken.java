package pt.ist.socialsoftware.edition.domain;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.joda.time.Period;

import pt.ist.socialsoftware.edition.utils.Emailer;
import pt.ist.socialsoftware.edition.utils.PropertiesManager;

public class RegistrationToken extends RegistrationToken_Base {
	private static final int EXPIRATION = 60;

	public RegistrationToken(String token, LdoDUser user) {
		setUser(user);
		setLdoD(user.getLdoD());
		setToken(token);
		setExpireTimeDateTime(DateTime.now().plus(new Period().withMinutes(EXPIRATION)));
	}

	public void setExpireTimeDateTime(DateTime date) {
		setExpireTime(date.getMillis());
	}

	public DateTime getExpireTimeDateTime() {
		return new DateTime(getExpireTime());
	}

	public void remove() {
		setUser(null);
		setLdoD(null);

		deleteDomainObject();
	}

	public void confirmRegistration(HttpServletRequest request) throws AddressException, MessagingException {
		// String recipientAddress = token.getUser().getEmail();
		// TODO: replace by line above
		String recipientAddress = PropertiesManager.getProperties()
				.getProperty("registration.confirmation.email.address");

		String subject = "Registration Confirmation";

		String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
		String confirmationUrl = path + "/signup/registrationConfirm?token=" + getToken();

		Emailer.sendEmail(recipientAddress, subject,
				"Confirm your LdoD-archive registration for user: " + getUser().getUsername() + " here: <a href=\""
						+ confirmationUrl + "\">" + confirmationUrl + "</a>",
				PropertiesManager.getProperties().getProperty("registration.confirmation.email.address"));
	}

}
