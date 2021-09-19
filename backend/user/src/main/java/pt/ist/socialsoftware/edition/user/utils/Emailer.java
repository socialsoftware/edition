package pt.ist.socialsoftware.edition.user.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.notification.utils.PropertiesManager;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Emailer {
    private static final Logger logger = LoggerFactory.getLogger(Emailer.class);

    public void sendEmail(String to, String subject, String msg, String from)
            throws MessagingException {
        Properties properties = setMailProperties();

        Session getMailSession = Session.getDefaultInstance(properties, null);

        Message generateMailMessage = createMessage(from, to, subject, msg, getMailSession);

        sendMessage(getMailSession, generateMailMessage);
    }

    private void sendMessage(Session getMailSession, Message generateMailMessage) throws MessagingException {
        Transport transport = getMailSession.getTransport("smtp");
        System.out.println((String) PropertiesManager.getProperties().get("registration.confirmation.mail.smtp.host"));
        System.out.println((String) PropertiesManager.getProperties().get("registration.confirmation.email.user"));
        System.out.println((String) PropertiesManager.getProperties().get("registration.confirmation.email.password"));
        transport.connect((String) PropertiesManager.getProperties().get("registration.confirmation.mail.smtp.host"),
                (String) PropertiesManager.getProperties().get("registration.confirmation.email.user"),
                (String) PropertiesManager.getProperties().get("registration.confirmation.email.password"));
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }

    private Message createMessage(String from, String to, String subject, String msg, Session getMailSession)
            throws MessagingException {
        Message generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        generateMailMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(from));
        generateMailMessage.setSubject(subject);
        generateMailMessage.setContent(msg, "text/html");
        return generateMailMessage;
    }

    private Properties setMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host",
                PropertiesManager.getProperties().get("registration.confirmation.mail.smtp.host"));
        properties.put("mail.smtp.port",
                PropertiesManager.getProperties().get("registration.confirmation.mail.smtp.port"));
        return properties;
    }
}
