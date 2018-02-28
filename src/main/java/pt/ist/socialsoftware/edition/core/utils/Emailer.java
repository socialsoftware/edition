package pt.ist.socialsoftware.edition.core.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Emailer {
	private static Logger logger = LoggerFactory.getLogger(Emailer.class);

	public static void sendEmail(String to, String subject, String msg, String from)
			throws AddressException, MessagingException {
		Properties properties = setMailProperties();

		Session getMailSession = Session.getDefaultInstance(properties, null);

		Message generateMailMessage = createMessage(from, to, subject, msg, getMailSession);

		sendMessage(getMailSession, generateMailMessage);
	}

	private static void sendMessage(Session getMailSession, Message generateMailMessage) throws MessagingException {
		Transport transport = getMailSession.getTransport("smtp");
		transport.connect("smtp.gmail.com",
				(String) PropertiesManager.getProperties().get("registration.confirmation.email.user"),
				(String) PropertiesManager.getProperties().get("registration.confirmation.email.password"));
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
	}

	private static Message createMessage(String from, String to, String subject, String msg, Session getMailSession)
			throws MessagingException, AddressException {
		Message generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		generateMailMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(from));
		generateMailMessage.setSubject(subject);
		generateMailMessage.setContent(msg, "text/html");
		return generateMailMessage;
	}

	private static Properties setMailProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		return properties;
	}
}
