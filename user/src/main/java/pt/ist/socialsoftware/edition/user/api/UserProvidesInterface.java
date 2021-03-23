package pt.ist.socialsoftware.edition.user.api;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.user.api.dto.RegistrationTokenDto;
import pt.ist.socialsoftware.edition.user.api.dto.UserConnectionDto;
import pt.ist.socialsoftware.edition.user.domain.*;
import pt.ist.socialsoftware.edition.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.user.feature.inout.UsersXMLExport;
import pt.ist.socialsoftware.edition.user.feature.inout.UsersXMLImport;
import pt.ist.socialsoftware.edition.user.utils.Emailer;
import pt.ist.socialsoftware.edition.user.utils.LdoDException;
import pt.ist.socialsoftware.edition.user.utils.UserBootstrap;


import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserProvidesInterface {
    private static final Logger logger = LoggerFactory.getLogger(UserProvidesInterface.class);


    public UserDto getUser(String username) {
        //new UserModule();
        User user = UserModule.getInstance().getUser(username);

        return user != null ? new UserDto(user) : null;
    }

    public RegistrationTokenDto getRegistrationToken(String token) {
        RegistrationToken registrationToken = UserModule.getInstance().getTokenSet(token);
        return registrationToken != null ? new RegistrationTokenDto(registrationToken) : null;
    }

    public String getFirstName(String username) {
        return getUserByUsername(username).orElse(null).getFirstName();
    }

    public String getLastName(String username) {
        return getUserByUsername(username).orElse(null).getLastName();
    }

    public boolean isEnabled(String username) {
        return getUserByUsername(username).orElse(null).getEnabled();
    }

    public boolean isActive(String username) {
        return getUserByUsername(username).orElse(null).getActive();
    }

    public String getEmail(String username) {
        return getUserByUsername(username).orElse(null).getEmail();
    }

    public String getSocialMediaId(String username) {
        return getUserByUsername(username).orElse(null).getSocialMediaId();
    }

    public Set<UserDto> getUsersSet() {
        return UserModule.getInstance().getUsersSet().stream().map(UserDto::new).collect(Collectors.toSet());
    }

    public boolean getAdmin() {
        return UserModule.getInstance().getAdmin();
    }

    public void updatePassword(String username, PasswordEncoder passwordEncoder, String currentPassword, String newPassword) {
        getUserByUsername(username).orElseThrow(LdoDException::new)
                .updatePassword(passwordEncoder, currentPassword, newPassword);
    }

    public void setUserLastLogin(String username, LocalDate now) {
        getUserByUsername(username).orElseThrow(() -> new LdoDException("User not found")).setLastLogin(now);
    }

    public List<UserConnectionDto> getUserConnections(String userId) {
        return UserModule.getInstance().getUserConnectionSet().stream().filter(uc -> uc.getUserId().equals(userId)).sorted((uc1, uc2) -> compareByProviderIdAndRank(uc1, uc2)).map(UserConnectionDto::new).collect(Collectors.toList());
    }

    public List<UserConnectionDto> getUserConnectionsByProviderId(String userId, String providerId) {
        return UserModule.getInstance().getUserConnectionSet().stream()
                .filter(uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(providerId))
                .sorted((uc1, uc2) -> Integer.compare(uc1.getRank(), uc2.getRank()))
                .map(UserConnectionDto::new)
                .collect(Collectors.toList());
    }

    public UserConnectionDto getUserConnectionsByUserProviderId(String userId, String key, String u) {
       // return new UserConnectionDto(UserModule.getInstance().getUserConnection(userId, key, u));
        UserConnection uc = UserModule.getInstance().getUserConnection(userId, key, u);
        return  uc != null ? new UserConnectionDto(uc) : null;
    }

    public void createUserConnection(String userId, String providerId, String providerUserId, int nextRank, String displayName, String profileUrl, String imageUrl, String encrypt, String encrypt1, String encrypt2, Long expireTime) {
        UserModule.getInstance().createUserConnection(userId, providerId, providerUserId, nextRank, displayName, profileUrl, imageUrl, encrypt, encrypt1, encrypt2, expireTime);
    }

    public void updateUserConnection(String userId, String providerId, String providerUserId, String displayName, String profileUrl, String imageUrl, String accessToken, String secret, String refreshToken, Long expireTime) {
        UserConnection userConnection = UserModule.getInstance().getUserConnection(userId, providerId, providerUserId);
        if (userConnection != null) {
            userConnection.setDisplayName(displayName);
            userConnection.setProfileUrl(profileUrl);
            userConnection.setImageUrl(imageUrl);
            userConnection.setAccessToken(accessToken);
            userConnection.setSecret(secret);
            userConnection.setRefreshToken(refreshToken);
            userConnection.setExpireTime(expireTime);
        }
    }

    public boolean hasRoleTypeAdmin(String username) {
        return UserModule.getInstance().getUser(username).getRolesSet().contains(Role.getRole(Role.RoleType.ROLE_ADMIN));
    }

    public String[] getRolesFromUser(String username) {
        return UserModule.getInstance().getUser(username).getRolesSet().stream().map(role -> role.getType().name()).toArray(String[]::new);
    }

    public List<String> getUserIdsWithConnections(String providerId, String providerUserId) {
        return UserModule.getInstance().getUserConnectionSet().stream().filter(uc -> uc.getProviderId().equals(providerId)
                && uc.getProviderUserId().equals(providerUserId))
                .map(uc -> uc.getUserId()).collect(Collectors.toList());
    }

    public Set<String> getUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        return UserModule.getInstance().getUserConnectionSet().stream()
                .filter(uc -> uc.getProviderId().equals(providerId) && providerUserIds.contains(uc.getProviderUserId()))
                .map(uc -> uc.getUserId()).collect(Collectors.toSet());
    }

    public UserDto createUser(PasswordEncoder passwordEncoder, String username, String password, String firstName, String lastName, String email, String socialMediaServiceForm, String socialMediaId) {
        User.SocialMediaService socialMediaService = socialMediaServiceForm.equals("") ? null
                : User.SocialMediaService.valueOf(socialMediaServiceForm.toUpperCase());
        User user = UserModule.getInstance().createUser(passwordEncoder, username, password, firstName, lastName, email, socialMediaService, socialMediaId);
        return new UserDto(user);
    }

    public RegistrationTokenDto createRegistrationToken(String username, String toString) {
        return new RegistrationTokenDto(UserModule.getInstance().getUser(username).createRegistrationToken(toString));
    }

    private Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(UserModule.getInstance().getUser(username));
    }

    private int compareByProviderIdAndRank(UserConnection uc1, UserConnection uc2) {
        if (uc1.getProviderId().compareTo(uc2.getProviderId()) != 0) {
            return uc1.getProviderId().compareTo(uc2.getProviderId());
        }
        return Integer.compare(uc1.getRank(), uc2.getRank());
    }

    public void requestAuthorization(String token, HttpServletRequest servletRequest, Emailer emailer) throws MessagingException {
        UserModule.getInstance().getTokenSet(token).requestAuthorization(servletRequest, emailer);
    }

    public void setAuthorized(String token, boolean authorized) {
        UserModule.getInstance().getTokenSet(token).setAuthorized(authorized);
    }

    public void requestConfirmation(String token, HttpServletRequest servletRequest, Emailer emailer) throws MessagingException {
        UserModule.getInstance().getTokenSet(token).requestConfirmation(servletRequest, emailer);
    }

    public UserDto getUserFromRegistrationToken(String token) {
        return new UserDto(UserModule.getInstance().getTokenSet(token).getUser());
    }

    public void enableUnconfirmedUser(String username) {
        UserModule.getInstance().getUser(username).enableUnconfirmedUser();
    }

    public void switchAdmin() {
        UserModule.getInstance().switchAdmin();
    }

    public UserDto getUserFromExternalId(String externalId) {
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof User) {
            return new UserDto((User) domainObject);
        } else {
            return null;
        }
    }

    public boolean hasRoleTypeUser(String username) {
        return UserModule.getInstance().getUser(username).getRolesSet().contains(Role.getRole(Role.RoleType.ROLE_USER));
    }

    public void updateUserInfo(String username, PasswordEncoder passwordEncoder, String oldUsername, String newUsername, String firstName, String lastName, String email, String newPassword, boolean user, boolean admin, boolean enabled) {
        UserModule.getInstance().getUser(username).update(passwordEncoder, oldUsername, newUsername, firstName, lastName, email, newPassword, user, admin, enabled);
    }

    public void switchUserActive(String username) {
        UserModule.getInstance().getUser(username).switchActive();
    }

    public void removeUser(String username) {
        UserModule.getInstance().getUser(username).remove();
    }

    public String exportXMLUsers() {
        UsersXMLExport generator = new UsersXMLExport();
        return generator.export();
    }

    public void importUsersFromXML(InputStream inputStream) {
        UsersXMLImport loader = new UsersXMLImport();
        loader.importUsers(inputStream);
    }

    public void createTestUsers(PasswordEncoder passwordEncoder) {
        UserModule.getInstance().createTestUsers(passwordEncoder);
    }

    public String getUserListofRoleAsStrings(String username) {
       return UserModule.getInstance().getUser(username).getListOfRolesAsStrings();
    }

    public LocalDate getUserLastLogin(String username) {
        return UserModule.getInstance().getUser(username).getLastLogin();
    }

    public void removeAllUserConnectionsByProviderId(String userId, String providerId) {
        Set<UserConnection> userConnections = UserModule.getInstance().getUserConnectionSet();
        Set<UserConnection> toRemoveUserConnections = userConnections.stream()
                .filter(uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(providerId))
                .collect(Collectors.toSet());
        userConnections.removeAll(toRemoveUserConnections);
    }

    public void removeAllUserConnectionsByConnectionKey(String userId, String providerId, String providerUserId) {
        Set<UserConnection> userConnections = UserModule.getInstance().getUserConnectionSet();
        Set<UserConnection> toRemoveUserConnections = userConnections.stream()
                .filter(uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(providerId)
                        && uc.getProviderUserId().equals(providerUserId))
                .collect(Collectors.toSet());
        userConnections.removeAll(toRemoveUserConnections);
    }

    public String getRoleName(String role) {
        if (role.equals("Admin")) {
           return Role.RoleType.ROLE_ADMIN.name();
        } else {
           return Role.RoleType.ROLE_USER.name();
        }
    }

    public boolean initializeUserModule() {
       return UserBootstrap.initializeUserModule();
    }

}
