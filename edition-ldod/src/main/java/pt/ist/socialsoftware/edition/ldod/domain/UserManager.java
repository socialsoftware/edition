package pt.ist.socialsoftware.edition.ldod.domain;

import org.joda.time.DateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateUsernameException;

public class UserManager extends UserManager_Base {

    public static UserManager getInstance() {
        return FenixFramework.getDomainRoot().getUserManager();
    }

    public UserManager() {
        FenixFramework.getDomainRoot().setUserManager(this);
    }

    public LdoDUser getUser(String username) {
        for (LdoDUser user : getUsersSet()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public void switchAdmin() {
        setAdmin(!getAdmin());
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public LdoDUser createUser(PasswordEncoder passwordEncoder, String username, String password, String firstName,
                               String lastName, String email, LdoDUser.SocialMediaService socialMediaService, String socialMediaId) {

        removeOutdatedUnconfirmedUsers();

        if (getUser(username) == null) {
            LdoDUser user = new LdoDUser(this, username, passwordEncoder.encode(password), firstName, lastName, email);
            user.setSocialMediaService(socialMediaService);
            user.setSocialMediaId(socialMediaId);

            Role userRole = Role.getRole(Role.RoleType.ROLE_USER);
            user.addRoles(userRole);

            return user;
        } else {
            throw new LdoDDuplicateUsernameException(username);
        }
    }

    public UserConnection getUserConnection(String userId, String providerId, String providerUserId) {
        return getUserConnectionSet().stream().filter(uc -> uc.getUserId().equals(userId)
                && uc.getProviderId().equals(providerId) && uc.getProviderUserId().equals(providerUserId)).findFirst()
                .orElse(null);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createUserConnection(String userId, String providerId, String providerUserId, int rank,
                                     String displayName, String profileUrl, String imageUrl, String accessToken, String secret,
                                     String refreshToken, Long expireTime) {

        new UserConnection(this, userId, providerId, providerUserId, rank, displayName, profileUrl, imageUrl,
                accessToken, secret, refreshToken, expireTime);
    }

    public void removeOutdatedUnconfirmedUsers() {
        DateTime now = DateTime.now();
        getTokenSet().stream().filter(t -> t.getExpireTimeDateTime().isBefore(now)).map(t -> t.getUser())
                .forEach(u -> u.remove());
    }

    public RegistrationToken getTokenSet(String token) {
        return getTokenSet().stream().filter(t -> t.getToken().equals(token)).findFirst().orElse(null);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createTestUsers(PasswordEncoder passwordEncoder) {
        UserManager userManager = UserManager.getInstance();

        Role userRole = Role.getRole(Role.RoleType.ROLE_USER);
        Role admin = Role.getRole(Role.RoleType.ROLE_ADMIN);

        // the bcrypt generator
        // https://www.dailycred.com/blog/12/bcrypt-calculator
        for (int i = 0; i < 6; i++) {
            String username = "zuser" + Integer.toString(i + 1);
            if (UserManager.getInstance().getUser(username) == null) {
                LdoDUser user = new LdoDUser(userManager, username, passwordEncoder.encode(username), "zuser", "zuser",
                        "zuser" + Integer.toString(i + 1) + "@teste.pt");

                user.setEnabled(true);
                user.addRoles(userRole);
            }
        }

    }
}
