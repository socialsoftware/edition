package pt.ist.socialsoftware.edition.loaders;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.LdoDUser.SocialMediaService;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.shared.exception.LdoDLoadException;

public class UsersXMLImport {

	public void importUsers(InputStream inputStream) {
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);

		Document doc;
		try {
			doc = builder.build(inputStream);
		} catch (FileNotFoundException e) {
			throw new LdoDLoadException("Ficheiro não encontrado");
		} catch (JDOMException e) {
			throw new LdoDLoadException("Ficheiro com problemas de codificação TEI");
		} catch (IOException e) {
			throw new LdoDLoadException("Problemas com o ficheiro, tipo ou formato");
		}

		if (doc == null) {
			LdoDLoadException ex = new LdoDLoadException("Ficheiro inexistente ou sem formato TEI");
			throw ex;
		}

		processImport(doc);
	}

	public void importUsers(String usersXML) {
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);

		InputStream stream = new ByteArrayInputStream(usersXML.getBytes());

		importUsers(stream);
	}

	public void processImport(Document doc) {
		LdoD ldoD = LdoD.getInstance();

		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("//users/user", Filters.element());
		for (Element element : xp.evaluate(doc)) {
			String username = element.getAttributeValue("username");
			if (ldoD.getUser(username) == null) {
				String password = element.getAttributeValue("password");
				String firstName = element.getAttributeValue("firstName");
				String lastName = element.getAttributeValue("lastName");
				String email = element.getAttributeValue("email");

				LdoDUser user = new LdoDUser(ldoD, username, password, firstName, lastName, email);

				user.setActive(convertToBool(element.getAttributeValue("active")));
				user.setEnabled(convertToBool(element.getAttributeValue("enabled")));

				String socialMediaService = element.getAttributeValue("socialMediaService");
				if (socialMediaService != null) {
					user.setSocialMediaService(convertToSocialMediaService(socialMediaService));
				}

				String socialMediaId = element.getAttributeValue("socialMediaId");
				if (socialMediaId != null) {
					user.setSocialMediaId(socialMediaId);
				}
			}
		}
	}

	private SocialMediaService convertToSocialMediaService(String value) {
		switch (value) {
		case "TWITTER":
			return SocialMediaService.TWITTER;
		case "FACEBOOK":
			return SocialMediaService.FACEBOOK;
		case "LINKEDIN":
			return SocialMediaService.LINKEDIN;
		case "GOOGLE":
			return SocialMediaService.GOOGLE;
		default:
			throw new LdoDException("UsersXMLImport::convertToSocialMediaService " + value);

		}
	}

	private boolean convertToBool(String value) {
		if (value.equals("true")) {
			return true;
		}

		if (value.equals("false")) {
			return false;
		}

		throw new LdoDException("UsersXMLImport::convertToBool " + value);
	}

}
