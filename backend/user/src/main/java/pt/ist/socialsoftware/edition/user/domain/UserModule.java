package pt.ist.socialsoftware.edition.user.domain;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.notification.utils.LdoDDuplicateUsernameException;


public class UserModule extends UserModule_Base {
    public static Logger logger = LoggerFactory.getLogger(UserModule.class);

    public static UserModule getInstance() {
        return FenixFramework.getDomainRoot()
                .getUserModule();
    }

    public UserModule() {
        FenixFramework.getDomainRoot().setUserModule(this);
    }

    public void remove() {
        getUsersSet().forEach(u -> u.remove());
        getRolesSet().forEach(r -> r.remove());
        getTokenSet().forEach(t -> t.remove());
        getUserConnectionSet().forEach(c -> c.remove());

        setRoot(null);

        deleteDomainObject();
    }

    public User getUser(String username) {
        for (User user : getUsersSet()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Atomic(mode = TxMode.WRITE)
    public void switchAdmin() {
        setAdmin(!getAdmin());
    }

//    @Atomic(mode = TxMode.WRITE)
//    public User createUser(PasswordEncoder passwordEncoder, String username, String password, String firstName,
//                           String lastName, String email, User.SocialMediaService socialMediaService, String socialMediaId) {
//
//        removeOutdatedUnconfirmedUsers();
//
//        if (getUser(username) == null) {
//            User user = new User(this, username, passwordEncoder.encode(password), firstName, lastName, email);
//            user.setSocialMediaService(socialMediaService);
//            user.setSocialMediaId(socialMediaId);
//
//            Role userRole = Role.getRole(Role.RoleType.ROLE_USER);
//            user.addRoles(userRole);
//
//            return user;
//        } else {
//            throw new LdoDDuplicateUsernameException(username);
//        }
//    }


    @Atomic(mode = TxMode.WRITE)
    public User createUser(String username, String password, String firstName,
                           String lastName, String email, User.SocialMediaService socialMediaService, String socialMediaId) {

        removeOutdatedUnconfirmedUsers();

        if (getUser(username) == null) {
            User user = new User(this, username, password, firstName, lastName, email);
            user.setSocialMediaService(socialMediaService);
            user.setSocialMediaId(socialMediaId);

            Role userRole = Role.getRole(Role.RoleType.ROLE_USER);
            user.addRoles(userRole);

            return user;
        } else {
            throw new LdoDDuplicateUsernameException(username);
        }
    }

    public void removeOutdatedUnconfirmedUsers() {
        DateTime now = DateTime.now();
        getTokenSet().stream().filter(t -> t.getExpireTimeDateTime().isBefore(now)).map(t -> t.getUser())
                .forEach(u -> u.remove());
    }

    public RegistrationToken getTokenSet(String token) {
        return getTokenSet().stream().filter(t -> t.getToken().equals(token)).findFirst().orElse(null);
    }


    public UserConnection getUserConnection(String userId, String providerId, String providerUserId) {
        return getUserConnectionSet().stream().filter(uc -> uc.getUserId().equals(userId)
                && uc.getProviderId().equals(providerId) && uc.getProviderUserId().equals(providerUserId)).findFirst()
                .orElse(null);
    }

    @Atomic(mode = TxMode.WRITE)
    public void createUserConnection(String userId, String providerId, String providerUserId, int rank,
                                     String displayName, String profileUrl, String imageUrl, String accessToken, String secret,
                                     String refreshToken, Long expireTime) {

        new UserConnection(this, userId, providerId, providerUserId, rank, displayName, profileUrl, imageUrl,
                accessToken, secret, refreshToken, expireTime);
    }

    @Atomic(mode = TxMode.WRITE)
    public void createTestUsers(PasswordEncoder passwordEncoder) {
        UserModule userModule = UserModule.getInstance();

        Role userRole = Role.getRole(Role.RoleType.ROLE_USER);
        Role admin = Role.getRole(Role.RoleType.ROLE_ADMIN);

        PasswordEncoder passwordEncoder1 = new BCryptPasswordEncoder(11);

        // the bcrypt generator
        // https://www.dailycred.com/blog/12/bcrypt-calculator
        for (int i = 0; i < 6; i++) {
            String username = "zuser" + Integer.toString(i + 1);
            if (userModule.getUser(username) == null) {
//                User user = new User(userModule, username, passwordEncoder.encode(username), "zuser", "zuser",
//                        "zuser" + Integer.toString(i + 1) + "@teste.pt");
                User user = new User(userModule, username, passwordEncoder1.encode(username), "zuser", "zuser",
                        "zuser" + Integer.toString(i + 1) + "@teste.pt");

                user.setEnabled(true);
                user.addRoles(userRole);
            }
        }
    }


}
