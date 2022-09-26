package pt.ist.socialsoftware.edition.ldod.loaders;

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
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.SocialMediaService;
import pt.ist.socialsoftware.edition.ldod.domain.Role.RoleType;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDLoadException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

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
            throw new LdoDLoadException("Ficheiro inexistente ou sem formato TEI");
        }

        processImport(doc);
    }

    public void importUsers(String usersXML) {
        SAXBuilder builder = new SAXBuilder();
        builder.setIgnoringElementContentWhitespace(true);

        InputStream stream = new ByteArrayInputStream(usersXML.getBytes());

        importUsers(stream);
    }

    private boolean isUsersManagementTagPresent(Document doc) {
        return !XPathFactory.instance().compile("//users-management", Filters.element())
                .evaluate(doc)
                .isEmpty();
    }

    @Atomic(mode = TxMode.WRITE)
    public void processImport(Document doc) {
        LdoD ldoD = LdoD.getInstance();

        if (!isUsersManagementTagPresent(doc)) throw new LdoDLoadException("No <users-management> is present in file");

        importUsers(doc, ldoD);
        importUserConnections(doc, ldoD);
        importRegistrationTokens(doc, ldoD);
    }

    private void importUsers(Document doc, LdoD ldoD) {
        List<Element> users = XPathFactory.instance().compile("//users-management/users/user", Filters.element()).evaluate(doc);
        for (Element element : users) {
            String username = element.getAttributeValue("username");
            LdoDUser user = ldoD.getUser(username);
            if (user == null) {
                String firstName = element.getAttributeValue("firstName");
                String lastName = element.getAttributeValue("lastName");
                String email = element.getAttributeValue("email");

                user = new LdoDUser(ldoD, username, null, firstName, lastName, email);
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
            importPlayers(element, user);
        }
    }

    private void importUserRoles(Element element, LdoDUser user) {
        for (Element roleElement : element.getChild("roles").getChildren("role")) {
            user.addRoles(Role.getRole(convertToRoleType(roleElement.getAttributeValue("type"))));
        }

    }

    private void importUserConnections(Document doc, LdoD ldoD) {
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

            if (!Optional.ofNullable(LdoD.getInstance().getUserConnection(userId, providerId, providerUserId)).isPresent()) {
                new UserConnection(ldoD, userId, providerId, providerUserId, rank, displayName, profileUrl, imageUrl,
                        accessToken, secret, refreshToken, expireTime);
            }

        }
    }

    private void importRegistrationTokens(Document doc, LdoD ldoD) {
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xp = xpfac.compile("//users-management/registration-tokens/token", Filters.element());

        for (Element element : xp.evaluate(doc)) {
            String token = element.getAttributeValue("token");
            Long expireTime = Long.parseLong(element.getAttributeValue("expireTime"));
            boolean authorized = convertToBool(element.getAttributeValue("authorized"));
            String user = element.getAttributeValue("user");


            if (!Optional.ofNullable(LdoD.getInstance().getTokenSet(token)).isPresent()) {
                RegistrationToken registrationToken = new RegistrationToken(token, LdoD.getInstance().getUser(user));
                registrationToken.setExpireTime(expireTime);
                registrationToken.setAuthorized(authorized);
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

    private RoleType convertToRoleType(String value) {
        switch (value) {
            case "ROLE_USER":
                return RoleType.ROLE_USER;
            case "ROLE_ADMIN":
                return RoleType.ROLE_ADMIN;
            default:
                throw new LdoDException("UsersXMLImport::convertToRoleType " + value);

        }
    }

    private void importPlayers(Element element, LdoDUser user) {
        if (user.getPlayer() == null) {
            // Check if the user exported had a Player and import
            Element playerElement = element.getChild("player");
            if (playerElement != null) {
                double score = Double.parseDouble(playerElement.getAttributeValue("score"));
                Player player = new Player(user);
                player.setScore(score);
            }
        }
    }

}
