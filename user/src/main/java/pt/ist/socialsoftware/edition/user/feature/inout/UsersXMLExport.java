package pt.ist.socialsoftware.edition.user.feature.inout;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.user.domain.*;

public class UsersXMLExport {

    @Atomic
    public String export() {
        UserModule userModule = UserModule.getInstance();

        Element element = createHeader();

        exportUsers(element, userModule);
        exportUserConnections(element, userModule);
        exportRegistrationTokens(element, userModule);

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

    private void exportUsers(Element element, UserModule userModule) {
        Element usersElement = new Element("users");

        for (User user : userModule.getUsersSet()) {
            exportUser(usersElement, user);
        }

        element.addContent(usersElement);
    }

    private void exportUser(Element element, User user) {
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

        element.addContent(userElement);
    }

    private void exportUserRoles(Element element, User user) {
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

    private void exportUserConnections(Element element, UserModule userModule) {
        Element userConnectionsElement = new Element("user-connections");

        for (UserConnection userConnection : userModule.getUserConnectionSet()) {
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

    private void exportRegistrationTokens(Element element, UserModule userModule) {
        Element registrationTokensElement = new Element("registration-tokens");

        for (RegistrationToken registrationToken : userModule.getTokenSet()) {
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


}
