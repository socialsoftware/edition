package pt.ist.socialsoftware.edition.user.api;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

import pt.ist.socialsoftware.edition.notification.utils.LdoDException;
import pt.ist.socialsoftware.edition.user.api.dto.RegistrationTokenDto;
import pt.ist.socialsoftware.edition.user.api.dto.UserConnectionDto;
import pt.ist.socialsoftware.edition.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.user.domain.*;
import pt.ist.socialsoftware.edition.user.feature.inout.UsersXMLExport;
import pt.ist.socialsoftware.edition.user.feature.inout.UsersXMLImport;
import pt.ist.socialsoftware.edition.user.utils.Emailer;
import pt.ist.socialsoftware.edition.user.utils.UserBootstrap;


import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserProvidesInterface {
    private static final Logger logger = LoggerFactory.getLogger(UserProvidesInterface.class);

    @Inject
    private Emailer emailer;

    @GetMapping("/user")
    @Atomic(mode = Atomic.TxMode.READ)
    public UserDto getUser(@RequestParam(name = "username") String username) {
        logger.debug("getUser: " + username);
        //new UserModule();
        User user = UserModule.getInstance().getUser(username);

        return user != null ? new UserDto(user) : null;
    }

    @GetMapping("/registrationToken/{token}")
    @Atomic(mode = Atomic.TxMode.READ)
    public RegistrationTokenDto getRegistrationToken(@PathVariable("token") String token) {
        logger.debug("getRegistrationToken: " + token);
        RegistrationToken registrationToken = UserModule.getInstance().getTokenSet(token);
        return registrationToken != null ? new RegistrationTokenDto(registrationToken) : null;
    }

    @GetMapping("/user/{username}/first")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getFirstName(@PathVariable("username") String username) {
        logger.debug("getFirstName:" + username);
        return getUserByUsername(username).orElse(null).getFirstName();
    }

    @GetMapping("/user/{username}/last")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getLastName(@PathVariable("username") String username) {
        logger.debug("getLastName: " + username);
        return getUserByUsername(username).orElse(null).getLastName();
    }

    @GetMapping("/user/{username}/isEnabled")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean isEnabled(@PathVariable("username") String username) {
        logger.debug("isEnabled: " + username);
        return getUserByUsername(username).orElse(null).getEnabled();
    }

    @GetMapping("/user/{username}/isActive")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean isActive(@PathVariable("username") String username) {
        logger.debug("isActive: " + username);
        return getUserByUsername(username).orElse(null).getActive();
    }

    @PostMapping("/user/{username}/enabled")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void setUserEnabled(@PathVariable("username") String username, @RequestParam(name = "enabled") boolean enabled) {
        logger.debug("setUserEnabled: " + username + ", " + enabled);
        getUserByUsername(username).orElse(null).setEnabled(enabled);
    }

    @PostMapping("/user/{username}/addRoles")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void addRolesToUser(@PathVariable("username") String username, @RequestParam(name = "roleUser") Role.RoleType roleUser) {
        logger.debug("addRolesToUser:" + username + ", " + roleUser);
        getUserByUsername(username).orElse(null).addRoles(Role.getRole(roleUser));
    }

    @GetMapping("/user/{username}/email")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getEmail(@PathVariable("username") String username) {
        logger.debug("getEmail: " + username);
        return getUserByUsername(username).orElse(null).getEmail();
    }

    @GetMapping("/user/{username}/socialMediaId")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getSocialMediaId(@PathVariable("username") String username) {
        logger.debug("getSocialMediaId: " +  username);
        return getUserByUsername(username).orElse(null).getSocialMediaId();
    }

    @GetMapping("/users")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<UserDto> getUsersSet() {
        logger.debug("getUsersSet");
        return UserModule.getInstance().getUsersSet().stream().map(UserDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/isAdmin")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean getAdmin() {
        logger.debug("getAdmin");
        return UserModule.getInstance().getAdmin();
    }

    @PostMapping("/updatePassword")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void updatePassword(@RequestParam(name = "username") String username, @RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword) {
        logger.debug("updatePassword: " + username);
        getUserByUsername(username).orElseThrow(LdoDException::new)
                .updatePassword(null, currentPassword, newPassword);
    }

    @PostMapping("/setLastLogin")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void setUserLastLogin(@RequestParam(name = "username") String username, @RequestParam(name = "now") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate now) {
        logger.debug("setUserLastLogin: " + username);
        getUserByUsername(username).orElseThrow(() -> new LdoDException("User not found")).setLastLogin(now);
    }

    @GetMapping("/userConnections")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<UserConnectionDto> getUserConnections(@RequestParam(name = "userId") String userId) {
        logger.debug("getUserConnections: " + userId);
        return UserModule.getInstance().getUserConnectionSet().stream().filter(uc -> uc.getUserId().equals(userId)).sorted((uc1, uc2) -> compareByProviderIdAndRank(uc1, uc2)).map(UserConnectionDto::new).collect(Collectors.toList());
    }

    @GetMapping("/userConnectionsByProviderId")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<UserConnectionDto> getUserConnectionsByProviderId(@RequestParam(name = "userId") String userId, @RequestParam(name = "providerId") String providerId) {
        logger.debug("getUserConnectionsByProviderId: " + userId + ", " + providerId);
        return UserModule.getInstance().getUserConnectionSet().stream()
                .filter(uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(providerId))
                .sorted((uc1, uc2) -> Integer.compare(uc1.getRank(), uc2.getRank()))
                .map(UserConnectionDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/userConnectionByUserProviderId")
    @Atomic(mode = Atomic.TxMode.READ)
    public UserConnectionDto getUserConnectionsByUserProviderId(@RequestParam(name = "userId") String userId, @RequestParam(name = "key") String key, @RequestParam(name = "u") String u) {
       // return new UserConnectionDto(UserModule.getInstance().getUserConnection(userId, key, u));
        logger.debug("getUserConnectionsByUserProviderId: " + userId + ", " + key + ", " + u);
        UserConnection uc = UserModule.getInstance().getUserConnection(userId, key, u);
        return  uc != null ? new UserConnectionDto(uc) : null;
    }

    @PostMapping("/createUserConnection")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createUserConnection(@RequestParam(name = "userId") String userId, @RequestParam(name = "providerId") String providerId, @RequestParam(name = "providerUserId") String providerUserId, @RequestParam(name = "nextRank") int nextRank, @RequestParam(name = "displayName") String displayName, @RequestParam(name = "profileUrl") String profileUrl, @RequestParam(name = "imageUrl") String imageUrl, @RequestParam(name = "encrypt") String encrypt, @RequestParam(name = "encrypt1") String encrypt1, @RequestParam(name = "encrypt2") String encrypt2, @RequestParam(name = "expireTime") Long expireTime) {
        logger.debug("createUserConnection: " + userId);
        UserModule.getInstance().createUserConnection(userId, providerId, providerUserId, nextRank, displayName, profileUrl, imageUrl, encrypt, encrypt1, encrypt2, expireTime);
    }

    @PostMapping("/updateUserConnection")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void updateUserConnection(@RequestParam(name = "userId") String userId, @RequestParam(name = "providerId") String providerId, @RequestParam(name = "providerUserId") String providerUserId, @RequestParam(name = "displayName") String displayName, @RequestParam(name = "profileUrl") String profileUrl, @RequestParam(name = "imageUrl") String imageUrl, @RequestParam(name = "accessToken") String accessToken, @RequestParam(name = "secret") String secret, @RequestParam(name = "refreshToken") String refreshToken, @RequestParam(name = "expireTime") Long expireTime) {
        logger.debug("updateUserConnection: " + userId);
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

    @GetMapping("/hasRoleAdmin")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean hasRoleTypeAdmin(@RequestParam(name = "username") String username) {
        logger.debug("hasRoleTypeAdmin: " + username);
        return UserModule.getInstance().getUser(username).getRolesSet().contains(Role.getRole(Role.RoleType.ROLE_ADMIN));
    }

    @GetMapping("/user/{username}/roles")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<String> getRolesFromUser(@PathVariable("username") String username) {
        logger.debug("getRolesFromUser: " + username);
        return UserModule.getInstance().getUser(username).getRolesSet().stream().map(role -> role.getType().name()).collect(Collectors.toList());
    }

    @GetMapping("/userIdsWithConnections")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<String> getUserIdsWithConnections(@RequestParam(name = "providerId") String providerId, @RequestParam(name = "providerUserId") String providerUserId) {
        logger.debug("getUserIdsWithConnections: " + providerId);
        return UserModule.getInstance().getUserConnectionSet().stream().filter(uc -> uc.getProviderId().equals(providerId)
                && uc.getProviderUserId().equals(providerUserId))
                .map(uc -> uc.getUserId()).collect(Collectors.toList());
    }

    @GetMapping("/userIdsConnectedTo")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<String> getUserIdsConnectedTo(@RequestParam(name = "providerId") String providerId, @RequestParam(name = "providerUserIds") Set<String> providerUserIds) {
        logger.debug("getUserIdsConnectedTo: " + providerId);
        return UserModule.getInstance().getUserConnectionSet().stream()
                .filter(uc -> uc.getProviderId().equals(providerId) && providerUserIds.contains(uc.getProviderUserId()))
                .map(uc -> uc.getUserId()).collect(Collectors.toSet());
    }

//    @PostMapping("/createUser")
//    @Atomic(mode = Atomic.TxMode.WRITE)
//    public UserDto createUser(@RequestBody PasswordEncoder passwordEncoder, @RequestParam(name = "username") String username, @RequestParam(name = "password") String password, @RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName, @RequestParam(name = "email") String email, @RequestParam(name = "socialMediaServiceForm") String socialMediaServiceForm, @RequestParam(name = "socialMediaId") String socialMediaId) {
//        User.SocialMediaService socialMediaService = socialMediaServiceForm.equals("") ? null
//                : User.SocialMediaService.valueOf(socialMediaServiceForm.toUpperCase());
//        User user = UserModule.getInstance().createUser(passwordEncoder, username, password, firstName, lastName, email, socialMediaService, socialMediaId);
//        return new UserDto(user);
//    }

    @PostMapping("/createUser")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public UserDto createUser(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password, @RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName, @RequestParam(name = "email") String email, @RequestParam(name = "socialMediaServiceForm", required = false) String socialMediaServiceForm, @RequestParam(name = "socialMediaId", required = false) String socialMediaId) {
        logger.debug("createUser: " + username);
        User.SocialMediaService socialMediaService = socialMediaServiceForm != null ? socialMediaServiceForm.equals("") ? null
                : User.SocialMediaService.valueOf(socialMediaServiceForm.toUpperCase()) : null;
        User user = UserModule.getInstance().createUser(username, password, firstName, lastName, email, socialMediaService, socialMediaId);
        return new UserDto(user);
    }

    @PostMapping("/createRegistrationToken")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public RegistrationTokenDto createRegistrationToken(@RequestParam(name = "username") String username, @RequestParam(name = "toString") String toString) {
        logger.debug("createRegistrationToken: " + username + ", " + toString);
        RegistrationToken token = UserModule.getInstance().getUser(username).createRegistrationToken(toString);
        return new RegistrationTokenDto(token);
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

//    @PostMapping("/requestAuthorization")
//    @Atomic(mode = Atomic.TxMode.WRITE)
//    public void requestAuthorization(@RequestParam(name = "token") String token, @RequestBody TokenRequestDto tokenRequestDto) throws MessagingException {
//        UserModule.getInstance().getTokenSet(token).requestAuthorization(tokenRequestDto.getServletRequest(), tokenRequestDto.getEmailer());
//    }

    @PostMapping("/requestAuthorization")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public HashMap<String, String> requestAuthorization(@RequestParam(name = "token") String token, HttpServletRequest httpServletRequest) throws MessagingException {
        logger.debug("requestAuthorization: " + token);
        return UserModule.getInstance().getTokenSet(token).requestAuthorization(httpServletRequest, this.emailer);
    }

    @PostMapping("/setAuthorized")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void setAuthorized(@RequestParam(name = "token") String token, @RequestParam(name = "authorized") boolean authorized) {
        logger.debug("setAuthorized: " + token + ", " + authorized);
        UserModule.getInstance().getTokenSet(token).setAuthorized(authorized);
    }

    @PostMapping("/requestConfirmation")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public HashMap<String, String> requestConfirmation(@RequestParam(name = "token") String token, HttpServletRequest httpServletRequest) throws MessagingException {
        logger.debug("requestConfirmation: " + token);
        return UserModule.getInstance().getTokenSet(token).requestConfirmation(httpServletRequest, this.emailer);
    }

    @GetMapping("/registrationToken/{token}/user")
    @Atomic(mode = Atomic.TxMode.READ)
    public UserDto getUserFromRegistrationToken(@PathVariable("token") String token) {
        logger.debug("getUserFromRegistrationToken: " + token);
        return new UserDto(UserModule.getInstance().getTokenSet(token).getUser());
    }

    @PostMapping("/user/{username}/enableUnconfirmed")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void enableUnconfirmedUser(@PathVariable("username") String username) {
        logger.debug("enableUnconfirmedUser: " + username);
        UserModule.getInstance().getUser(username).enableUnconfirmedUser();
    }

    @PostMapping("/switchAdmin")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void switchAdmin() {
        logger.debug("switchAdmin");
        UserModule.getInstance().switchAdmin();
    }

    @GetMapping("/user/ext/{externalId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public UserDto getUserFromExternalId(@PathVariable("externalId") String externalId) {
        logger.debug("getUserFromExternalId: " + externalId);
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof User) {
            return new UserDto((User) domainObject);
        } else {
            return null;
        }
    }

    @GetMapping("/hasRoleUser")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean hasRoleTypeUser(@RequestParam(name = "username") String username) {
        logger.debug("hasRoleTypeUser: " + username);
        return UserModule.getInstance().getUser(username).getRolesSet().contains(Role.getRole(Role.RoleType.ROLE_USER));
    }

    @PostMapping("/updateUserInfo")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void updateUserInfo(@RequestParam(name = "username") String username, @RequestParam(name = "oldUsername") String oldUsername, @RequestParam(name = "newUsername") String newUsername, @RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName, @RequestParam(name = "email") String email, @RequestParam(name = "newPassword") String newPassword, @RequestParam(name = "user") boolean user, @RequestParam(name = "admin") boolean admin, @RequestParam(name = "enabled") boolean enabled) {
        logger.debug("updateUserInfo: " + username);
        UserModule.getInstance().getUser(username).update(null, oldUsername, newUsername, firstName, lastName, email, newPassword, user, admin, enabled);
    }

    @PostMapping("/switchUserActive")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void switchUserActive(@RequestParam(name = "username") String username) {
        logger.debug("switchUserActive: " + username);
        UserModule.getInstance().getUser(username).switchActive();
    }

    @DeleteMapping("/removeUser")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeUser(@RequestParam(name = "username") String username) {
        logger.debug("removeUser: " + username);
        UserModule.getInstance().getUser(username).remove();
    }

    @GetMapping("/exportXMLUsers")
    @Atomic(mode = Atomic.TxMode.READ)
    public String exportXMLUsers() {
        logger.debug("exportXMLUsers");
        UsersXMLExport generator = new UsersXMLExport();
        return generator.export();
    }

    @PostMapping("/importUsersFromXML")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void importUsersFromXML(@RequestBody byte[] inputStream) {
        logger.debug("importUsersFromXML");
        UsersXMLImport loader = new UsersXMLImport();
        loader.importUsers(new ByteArrayInputStream(inputStream));
    }

    @PostMapping("/createTestUsers")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createTestUsers() {
        logger.debug("createTestUsers");
        UserModule.getInstance().createTestUsers(null);
    }

    @GetMapping("/user/{username}/listOfRoles")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getUserListofRoleAsStrings(@PathVariable("username") String username) {
       logger.debug("getUserListofRoleAsStrings: " + username);
        return UserModule.getInstance().getUser(username).getListOfRolesAsStrings();
    }

    @GetMapping("/user/{username}/lastLogin")
    @Atomic(mode = Atomic.TxMode.READ)
    public LocalDate getUserLastLogin(@PathVariable("username") String username) {
        logger.debug("getUserLastLogin: " + username);
        return UserModule.getInstance().getUser(username).getLastLogin();
    }

    @DeleteMapping("/removeAllUserConnectionsByProviderId")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeAllUserConnectionsByProviderId(@RequestParam(name = "userId") String userId, @RequestParam(name = "providerId") String providerId) {
        logger.debug("removeAllUserConnectionsByProviderId: " + userId);
        Set<UserConnection> userConnections = UserModule.getInstance().getUserConnectionSet();
        Set<UserConnection> toRemoveUserConnections = userConnections.stream()
                .filter(uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(providerId))
                .collect(Collectors.toSet());
        userConnections.removeAll(toRemoveUserConnections);
    }

    @DeleteMapping("/removeAllUserConnectionsByConnectionKey")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeAllUserConnectionsByConnectionKey(@RequestParam(name = "userId") String userId, @RequestParam(name = "providerId") String providerId, @RequestParam(name = "providerUserId") String providerUserId) {
        logger.debug("removeAllUserConnectionsByConnectionKey: " + userId);
        Set<UserConnection> userConnections = UserModule.getInstance().getUserConnectionSet();
        Set<UserConnection> toRemoveUserConnections = userConnections.stream()
                .filter(uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(providerId)
                        && uc.getProviderUserId().equals(providerUserId))
                .collect(Collectors.toSet());
        userConnections.removeAll(toRemoveUserConnections);
    }

    @GetMapping("/role/{role}")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getRoleName(@PathVariable("role") String role) {
        logger.debug("getRoleName: " + role);
        if (role.equals("Admin")) {
           return Role.RoleType.ROLE_ADMIN.name();
        } else {
           return Role.RoleType.ROLE_USER.name();
        }
    }

    @PostMapping("/initializeUserModule")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public boolean initializeUserModule() {
        logger.debug("initializeUserModule");
       return UserBootstrap.initializeUserModule();
    }

    // Testing purposes
    @PostMapping("/createTestUser")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public UserDto createTestUser(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password, @RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName, @RequestParam(name = "email") String email) {
        logger.debug("createTestUser: " + username);
        return new UserDto(new User(UserModule.getInstance(), username, password, firstName, lastName, email));
    }

    @GetMapping("/getTokensSet")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<RegistrationTokenDto> getTokensSet() {
        logger.debug("getTokensSet");
        return UserModule.getInstance().getTokenSet().stream().map(RegistrationTokenDto::new).collect(Collectors.toSet());
    }

    @PostMapping("/removeOutdatedUnconfirmedUsers")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeOutdatedUnconfirmedUsers() {
        logger.debug("removeOutdatedUnconfirmedUsers");
        UserModule.getInstance().removeOutdatedUnconfirmedUsers();
    }

    @GetMapping("/user/{username}/token")
    @Atomic(mode = Atomic.TxMode.READ)
    public RegistrationTokenDto getTokenFromUsername(@PathVariable("username") String username) {
        logger.debug("getTokenFromUsername: " + username);
        return new RegistrationTokenDto(getUserByUsername(username).orElse(null).getToken());
    }

    @GetMapping("/getUserConnectionSet")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<UserConnectionDto> getUserConnectionSet() {
        logger.debug("getUserConnectionSet");
        return UserModule.getInstance().getUserConnectionSet().stream().map(UserConnectionDto::new).collect(Collectors.toSet());
    }

    @PostMapping("/removeUserConnection")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeUserConnection(@RequestParam(name = "userId") String userId, @RequestParam(name = "providerId") String providerId, @RequestParam(name = "providerUserId") String providerUserId) {
        logger.debug("removeUserConnection: " + userId);
        UserConnection userConnection = UserModule.getInstance().getUserConnection(userId, providerId, providerUserId);
        userConnection.remove();
    }

    @PostMapping("/removeToken")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeToken(@RequestParam(name = "token") String token) {
       logger.debug("removeToken: " + token);
        UserModule.getInstance().getTokenSet(token);
    }

    @PostMapping("/removeUserModule")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeUserModule() {
        logger.debug("removeUserModule");
        UserModule userModule = UserModule.getInstance();
        if (userModule != null) {
            userModule.remove();
        }
    }

    @PostMapping("/token/{token}/updateExpireTimeDateTime")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void updateExpireTimeDateTime(@PathVariable("token") String token, @RequestParam(name = "i") int i, @RequestParam(name = "i1") int i1, @RequestParam(name = "i2") int i2, @RequestParam(name = "i3") int i3, @RequestParam(name = "i4") int i4, @RequestParam(name = "i5") int i5, @RequestParam(name = "i6") int i6) {
        UserModule.getInstance().getTokenSet(token).setExpireTimeDateTime(new DateTime(i, i1, i2, i3, i4, i5, i6));
    }

}
