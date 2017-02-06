package pt.ist.socialsoftware.edition.export;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;

public class UsersXMLExport {

	@Atomic
	public String export() {
		LdoD ldoD = LdoD.getInstance();

		Element element = createHeader();

		for (LdoDUser user : ldoD.getUsersSet()) {
			exportUser(element, user);
		}

		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		System.out.println(xml.outputString(element));

		return xml.outputString(element);
	}

	public Element createHeader() {
		Document jdomDoc = new Document();
		Element rootElement = new Element("users");

		jdomDoc.setRootElement(rootElement);
		return rootElement;
	}

	private void exportUser(Element element, LdoDUser user) {
		Element userElement = new Element("user");
		userElement.setAttribute("username", user.getUsername());
		userElement.setAttribute("enabled", exportBoolean(user.getEnabled()));
		userElement.setAttribute("active", exportBoolean(user.getActive()));
		userElement.setAttribute("password", user.getPassword());
		userElement.setAttribute("firstName", user.getFirstName());
		userElement.setAttribute("lastName", user.getLastName());
		userElement.setAttribute("email", user.getEmail());
		if (user.getSocialMediaService() != null) {
			userElement.setAttribute("socialMediaService", user.getSocialMediaService().toString());
		}
		if (user.getSocialMediaId() != null) {
			userElement.setAttribute("socialMediaId", user.getSocialMediaId());
		}

		element.addContent(userElement);
	}

	private String exportBoolean(boolean value) {
		if (value) {
			return "true";
		} else {
			return "false";
		}
	}

}
