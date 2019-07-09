package pt.ist.socialsoftware.edition.ldod.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.api.event.Event;
import pt.ist.socialsoftware.edition.ldod.api.event.EventInterface;
import pt.ist.socialsoftware.edition.ldod.domain.Role.RoleType;
import pt.ist.socialsoftware.edition.ldod.security.UserModuleUserDetails;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateUsernameException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.stream.Collectors;

public class User extends User_Base {
    private static final Logger logger = LoggerFactory.getLogger(User.class);
    public static String USER_TWITTER = "Twitter";
    public static String USER_ARS = "ars";

    public enum SocialMediaService {
        TWITTER, FACEBOOK, LINKEDIN, GOOGLE
    }

    @Override
    public void setFirstName(String firstName) {
        if (!firstName.matches("^[\\p{L}\\s]+$")) {
            throw new LdoDException(firstName);
        }
        super.setFirstName(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        if (!lastName.matches("^[\\p{L}\\s]+$")) {
            throw new LdoDException(lastName);
        }
        super.setLastName(lastName);
    }

    @Override
    public void setUsername(String username) {
        if (!username.matches("^[A-Za-z0-9_\\s\\-]+$")) {
            throw new LdoDException(username);
        }
        checkUniqueUsername(username);
        super.setUsername(username);
    }

    public static User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserModuleUserDetails userDetails = null;
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserModuleUserDetails) {
                userDetails = (UserModuleUserDetails) principal;
                return userDetails.getUser();
            }
        }
        return null;
    }

    @Atomic(mode = TxMode.WRITE)
    public void remove() {
        EventInterface eventInterface = new EventInterface();
        eventInterface.publish(new Event(Event.EventType.USER_REMOVE, getUsername()));

        getUserModule().getUserConnectionSet().stream().filter(uc -> uc.getUserId().equals(getUsername()))
                .forEach(uc -> uc.remove());
        if (getToken() != null) {
            getToken().remove();
        }
        getRolesSet().stream().forEach(r -> removeRoles(r));
        setUserModule(null);

        deleteDomainObject();
    }

    public User(UserModule userModule, String username, String password, String firstName, String lastName, String email) {
        setEnabled(false);
        setActive(true);
        setUserModule(userModule);
        setUsername(username);
        setPassword(password);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    private void checkUniqueUsername(String username) {
        if (getUserModule().getUsersSet().stream().filter(u -> u.getUsername() != null && u.getUsername().equals(username))
                .findFirst().isPresent()) {
            throw new LdoDDuplicateUsernameException(username);
        }

    }

    @Atomic(mode = TxMode.WRITE)
    public RegistrationToken createRegistrationToken(String token) {
        return new RegistrationToken(token, this);
    }

    @Atomic(mode = TxMode.WRITE)
    public void enableUnconfirmedUser() {
        setEnabled(true);
        if (getToken() != null) {
            getToken().remove();
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public void updatePassword(PasswordEncoder passwordEncoder, String currentPassword, String newPassword) {
        if (!passwordEncoder.matches(currentPassword, getPassword())) {
            throw new LdoDException();
        }

        setPassword(passwordEncoder.encode(newPassword));
    }

    public String getListOfRolesAsStrings() {
        return getRolesSet().stream().map(r -> r.getType().name()).collect(Collectors.joining(", "));
    }

    @Atomic(mode = TxMode.WRITE)
    public void switchActive() {
        if (getActive()) {
            setActive(false);
        } else {
            setActive(true);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public void update(PasswordEncoder passwordEncoder, String oldUsername, String newUsername, String firstName,
                       String lastName, String email, String newPassword, boolean isUser, boolean isAdmin, boolean isEnabled) {

        if (!oldUsername.equals(newUsername)) {
            changeUsername(oldUsername, newUsername);
        }

        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);

        getRolesSet().clear();

        if (isUser) {
            addRoles(Role.getRole(RoleType.ROLE_USER));
        }

        if (isAdmin) {
            addRoles(Role.getRole(RoleType.ROLE_ADMIN));
        }

        setEnabled(isEnabled);

        if (newPassword != null && !newPassword.trim().equals("")) {
            setPassword(passwordEncoder.encode(newPassword));
        }

    }

    private void changeUsername(String oldUsername, String newUsername) {
        setUsername(newUsername);

        UserConnection userConnection = getUserModule().getUserConnectionSet().stream()
                .filter(uc -> uc.getUserId().equals(oldUsername)).findFirst().orElse(null);

        if (userConnection != null) {
            userConnection.setUserId(newUsername);
        }

    }

}
