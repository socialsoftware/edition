package pt.ist.socialsoftware.edition.ldod.export;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.domain.*;

public class UsersXMLExport {

	@Atomic
	public String export() {
		LdoD ldoD = LdoD.getInstance();

		Element element = createHeader();

		exportUsers(element, ldoD);
		exportUserConnections(element, ldoD);
		exportRegistrationTokens(element, ldoD);

		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		// System.out.println(xml.outputString(element));

		return xml.outputString(element);
	}

	public Element createHeader() {
		Document jdomDoc = new Document();
		Element rootElement = new Element("users-management");

		jdomDoc.setRootElement(rootElement);
		return rootElement;
	}

	private void exportUsers(Element element, LdoD ldoD) {
		Element usersElement = new Element("users");

		for (LdoDUser user : ldoD.getUsersSet()) {
			exportUser(usersElement, user);
		}

		element.addContent(usersElement);
	}

	private void exportUser(Element element, LdoDUser user) {
		Element userElement = new Element("user");
		userElement.setAttribute("username", user.getUsername());
		userElement.setAttribute("enabled", exportBoolean(user.getEnabled()));
		userElement.setAttribute("active", exportBoolean(user.getActive()));

		// TODO: changed because of Social Media User, password cannot be null
		String password = user.getPassword();
		if (password != null) {
			userElement.setAttribute("password", password);
		}

		userElement.setAttribute("firstName", user.getFirstName());
		userElement.setAttribute("lastName", user.getLastName());
		userElement.setAttribute("email", user.getEmail());
		if (user.getLastLogin() != null) {
			userElement.setAttribute("lastLogin", user.getLastLogin().toString());
		}
		if (user.getSocialMediaService() != null) {
			userElement.setAttribute("socialMediaService", user.getSocialMediaService().toString());
		}
		if (user.getSocialMediaId() != null) {
			userElement.setAttribute("socialMediaId", user.getSocialMediaId());
		}

		exportUserRoles(userElement, user);
		exportPlayer(userElement, user);

		element.addContent(userElement);
	}

	private void exportUserRoles(Element element, LdoDUser user) {
		Element userRolesElement = new Element("roles");

		for (Role role : user.getRolesSet()) {
			exportUserRole(userRolesElement, role);
		}

		element.addContent(userRolesElement);

	}

	private void exportUserRole(Element element, Role role) {
		Element roleElement = new Element("role");

		roleElement.setAttribute("type", role.getType().toString());

		element.addContent(roleElement);

	}

	private String exportBoolean(boolean value) {
		if (value) {
			return "true";
		} else {
			return "false";
		}
	}

	private void exportUserConnections(Element element, LdoD ldoD) {
		Element userConnectionsElement = new Element("user-connections");

		for (UserConnection userConnection : ldoD.getUserConnectionSet()) {
			exportUserConnection(userConnectionsElement, userConnection);
		}

		element.addContent(userConnectionsElement);
	}

	private void exportUserConnection(Element element, UserConnection userConnection) {
		Element userConnectionElement = new Element("user-connection");

		userConnectionElement.setAttribute("userId", userConnection.getUserId());
		userConnectionElement.setAttribute("providerId", userConnection.getProviderId());
		userConnectionElement.setAttribute("providerUserId", userConnection.getProviderUserId());
		userConnectionElement.setAttribute("rank", Integer.toString(userConnection.getRank()));
		userConnectionElement.setAttribute("displayName", userConnection.getDisplayName());
		if (userConnection.getProfileUrl() != null) {
			userConnectionElement.setAttribute("profileUrl", userConnection.getProfileUrl());
		}
		userConnectionElement.setAttribute("imageUrl", userConnection.getImageUrl());
		userConnectionElement.setAttribute("accessToken", userConnection.getAccessToken());
		if (userConnection.getSecret() != null) {
			userConnectionElement.setAttribute("secret", userConnection.getSecret());
		}
		if (userConnection.getRefreshToken() != null) {
			userConnectionElement.setAttribute("refreshToken", userConnection.getRefreshToken());
		}
		if (userConnection.getExpireTime() != null) {
			userConnectionElement.setAttribute("expireTime", userConnection.getExpireTime().toString());
		}

		element.addContent(userConnectionElement);
	}

	private void exportRegistrationTokens(Element element, LdoD ldoD) {
		Element registrationTokensElement = new Element("registration-tokens");

		for (RegistrationToken registrationToken : ldoD.getTokenSet()) {
			exportRegistrationToken(registrationTokensElement, registrationToken);
		}

		element.addContent(registrationTokensElement);
	}

	private void exportRegistrationToken(Element element, RegistrationToken registrationToken) {
		Element tokenElement = new Element("token");

		tokenElement.setAttribute("token", registrationToken.getToken());
		tokenElement.setAttribute("expireTime", registrationToken.getExpireTime().toString());
		tokenElement.setAttribute("authorized", exportBoolean(registrationToken.getAuthorized()));
		tokenElement.setAttribute("user", registrationToken.getUser().getUsername());

		element.addContent(tokenElement);
	}

	private void exportPlayer(Element userElement, LdoDUser user) {
		if (user.getPlayer() != null) {
			Player player = user.getPlayer();
			Element playerElement = new Element("player");
			playerElement.setAttribute("score", Double.toString(player.getScore()));
			userElement.addContent(playerElement);
		}
	}
}
