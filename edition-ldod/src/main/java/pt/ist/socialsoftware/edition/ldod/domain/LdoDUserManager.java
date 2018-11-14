package pt.ist.socialsoftware.edition.ldod.domain;

import org.springframework.security.crypto.password.PasswordEncoder;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateUsernameException;
import pt.ist.socialsoftware.edition.user.domain.Role;
import pt.ist.socialsoftware.edition.user.domain.User;
import pt.ist.socialsoftware.edition.user.domain.UserManager;

public class LdoDUserManager extends LdoDUserManager_Base {

    public static UserManager getInstance() {
        return FenixFramework.getDomainRoot().getUserManager();
    }
    
    public LdoDUserManager() {
        super();
    }

    public LdoDUser createUser(PasswordEncoder passwordEncoder, String username, String password, String firstName, String lastName, String email, User.SocialMediaService socialMediaService, String socialMediaId) {
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
}
