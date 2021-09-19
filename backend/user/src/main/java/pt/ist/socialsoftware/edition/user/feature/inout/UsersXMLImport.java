package pt.ist.socialsoftware.edition.user.feature.inout;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.joda.time.LocalDate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import pt.ist.socialsoftware.edition.notification.utils.LdoDException;
import pt.ist.socialsoftware.edition.notification.utils.LdoDLoadException;
import pt.ist.socialsoftware.edition.user.domain.*;



import java.io.*;
import java.nio.charset.Charset;

public class UsersXMLImport {

    public void importUsers(InputStream inputStream) {
        SAXBuilder builder = new SAXBuilder();
        builder.setIgnoringElementContentWhitespace(true);

        Document doc;
        try {
            Reader reader = new InputStreamReader(inputStream, Charset.defaultCharset());
            doc = builder.build(reader);
            //doc = builder.build(inputStream);
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

    @Atomic(mode = TxMode.WRITE)
    public void processImport(Document doc) {
        UserModule userModule = UserModule.getInstance();

        importUsers(doc, userModule);
        importUserConnections(doc, userModule);
        importRegistrationTokens(doc, userModule);
    }

    private void importUsers(Document doc, UserModule userModule) {
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xp = xpfac.compile("//users-management/users/user", Filters.element());
        for (Element element : xp.evaluate(doc)) {
            String username = element.getAttributeValue("username");
            User user = userModule.getUser(username);
            if (user == null) {
                String firstName = element.getAttributeValue("firstName");
                String lastName = element.getAttributeValue("lastName");
                String email = element.getAttributeValue("email");

                user = new User(userModule, username, null, firstName, lastName, email);
            }

            // Support null passwords
            if (element.getAttributeValue("password") != null) {
                String password = element.getAttributeValue("password");
                user.setPassword(password);
            }

            if (element.getAttributeValue("lastLogin") != null) {
                user.setLastLogin(LocalDate.parse(element.getAttributeValue("lastLogin")));
            }

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

            importUserRoles(element, user);
        }
    }

    private void importUserRoles(Element element, User user) {
        for (Element roleElement : element.getChild("roles").getChildren("role")) {
            user.addRoles(Role.getRole(convertToRoleType(roleElement.getAttributeValue("type"))));
        }

    }

    private void importUserConnections(Document doc, UserModule userModule) {
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xp = xpfac.compile("//users-management/user-connections/user-connection",
                Filters.element());
        for (Element element : xp.evaluate(doc)) {
            String userId = element.getAttributeValue("userId");
            String providerId = element.getAttributeValue("providerId");
            String providerUserId = element.getAttributeValue("providerUserId");
            int rank = Integer.parseInt(element.getAttributeValue("rank"));
            String displayName = element.getAttributeValue("displayName");
            String profileUrl = element.getAttributeValue("profileUrl");
            String imageUrl = element.getAttributeValue("imageUrl");
            String accessToken = element.getAttributeValue("accessToken");
            String secret = element.getAttributeValue("secret");
            String refreshToken = element.getAttributeValue("refreshToken");
            Long expireTime = null;
            if (element.getAttributeValue("expireTime") != null) {
                expireTime = Long.parseLong(element.getAttributeValue("expireTime"));
            }

            new UserConnection(userModule, userId, providerId, providerUserId, rank, displayName, profileUrl, imageUrl,
                    accessToken, secret, refreshToken, expireTime);
        }
    }

    private void importRegistrationTokens(Document doc, UserModule userModule) {
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xp = xpfac.compile("//users-management/registration-tokens/token", Filters.element());

        for (Element element : xp.evaluate(doc)) {
            String token = element.getAttributeValue("token");
            Long expireTime = Long.parseLong(element.getAttributeValue("expireTime"));
            boolean authorized = convertToBool(element.getAttributeValue("authorized"));
            String user = element.getAttributeValue("user");

            RegistrationToken registrationToken = new RegistrationToken(token, userModule.getUser(user));
            registrationToken.setExpireTime(expireTime);
            registrationToken.setAuthorized(authorized);
        }
    }

    private User.SocialMediaService convertToSocialMediaService(String value) {
        switch (value) {
            case "TWITTER":
                return User.SocialMediaService.TWITTER;
            case "FACEBOOK":
                return User.SocialMediaService.FACEBOOK;
            case "LINKEDIN":
                return User.SocialMediaService.LINKEDIN;
            case "GOOGLE":
                return User.SocialMediaService.GOOGLE;
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

    private Role.RoleType convertToRoleType(String value) {
        switch (value) {
            case "ROLE_USER":
                return Role.RoleType.ROLE_USER;
            case "ROLE_ADMIN":
                return Role.RoleType.ROLE_ADMIN;
            default:
                throw new LdoDException("UsersXMLImport::convertToRoleType " + value);

        }
    }

}
