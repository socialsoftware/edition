package pt.ist.socialsoftware.edition.ldod.frontend.user;

import org.joda.time.LocalDate;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.user.api.dto.RegistrationTokenDto;
import pt.ist.socialsoftware.edition.user.api.dto.UserConnectionDto;
import pt.ist.socialsoftware.edition.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionInterDto;


import java.io.InputStream;
import java.util.List;
import java.util.Set;

public class FeUserRequiresInterface {
    // Uses User Module
    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();


    public UserDto getUser(String username) {
        return this.userProvidesInterface.getUser(username);
    }

    public void setLastLogin(String username, LocalDate now) {
        this.userProvidesInterface.setUserLastLogin(username, now);
    }

    public Set<UserDto> getUsersSet() {
        return this.userProvidesInterface.getUsersSet();
    }

    public boolean getAdmin() {
        return this.userProvidesInterface.getAdmin();
    }

    public List<UserConnectionDto> getUserConnections(String userId) {
        return this.userProvidesInterface.getUserConnections(userId);
    }

    public List<UserConnectionDto> getUserConnectionsByProviderId(String userId, String providerId) {
        return this.userProvidesInterface.getUserConnectionsByProviderId(userId, providerId);
    }

    public UserConnectionDto getUserConnectionByUserProviderId(String userId, String key, String u) {
        return this.userProvidesInterface.getUserConnectionsByUserProviderId(userId, key, u);
    }

    public void createUserConnection(String userId, String providerId, String providerUserId, int nextRank, String displayName, String profileUrl, String imageUrl, String encrypt, String encrypt1, String encrypt2, Long expireTime) {
        this.userProvidesInterface.createUserConnection(userId, providerId, providerUserId, nextRank, displayName, profileUrl, imageUrl, encrypt, encrypt1, encrypt2, expireTime);
    }

    public List<String> getUserIdsWithConnections(String providerId, String providerUserId) {
        return this.userProvidesInterface.getUserIdsWithConnections(providerId, providerUserId);
    }

    public Set<String> getUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        return this.userProvidesInterface.getUserIdsConnectedTo(providerId, providerUserIds);
    }

    public UserDto createUser(PasswordEncoder passwordEncoder, String username, String password, String firstName, String lastName, String email, String socialMediaService, String socialMediaId) {
        return this.userProvidesInterface.createUser(passwordEncoder, username, password, firstName, lastName, email, socialMediaService, socialMediaId);
    }

    public RegistrationTokenDto getRegistrationToken(String token) {
        return this.userProvidesInterface.getRegistrationToken(token);
    }

    public void switchAdmin() {
        this.userProvidesInterface.switchAdmin();
    }

    public UserDto getUserFromExternalId(String externalId) {
        return this.userProvidesInterface.getUserFromExternalId(externalId);
    }

    public String exportXMLUsers() {
        return this.userProvidesInterface.exportXMLUsers();
    }

    public void importUsersFromXML(InputStream inputStream) {
        this.userProvidesInterface.importUsersFromXML(inputStream);
    }

    public void createTestUsers(PasswordEncoder passwordEncoder) {
        this.userProvidesInterface.createTestUsers(passwordEncoder);
    }

    public void removeAllUserConnectionsByProviderId(String userId, String providerId) {
        this.userProvidesInterface.removeAllUserConnectionsByProviderId(userId, providerId);
    }

    public void removeAllUserConnectionsByConnectionKey(String userId, String providerId, String providerUserId) {
        this.userProvidesInterface.removeAllUserConnectionsByConnectionKey(userId, providerId, providerUserId);
    }

    public String getRoleName(String role) {
        return this.userProvidesInterface.getRoleName(role);
    }

    // User Text Module
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    public FragmentDto getFragmentByXmlId(String xmlId) {
        return this.textProvidesInterface.getFragmentByXmlId(xmlId);
    }


    // Uses Virtual Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();


    public Set<VirtualEditionDto> getPublicVirtualEditionsOrUserIsParticipant(String username) {
        return this.virtualProvidesInterface.getPublicVirtualEditionsOrUserIsParticipant(username);
    }

    public VirtualEditionDto getVirtualEditionByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionByExternalId(externalId);
    }

    public VirtualEditionDto getVirtualEditionByAcronym(String acronym) {
        return this.virtualProvidesInterface.getVirtualEditionByAcronym(acronym);
    }

    public VirtualEditionInterDto getVirtualEditionInterByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionInterByExternalId(externalId);
    }

    public VirtualEditionDto getVirtualEditionOfTaxonomyByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionOfTaxonomyByExternalId(externalId);
    }

    public VirtualEditionDto getVirtualEditionOfCategoryByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionOfCategoryByExternalId(externalId);
    }

    public VirtualEditionDto getVirtualEditionOfTagByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionOfTagByExternalId(externalId);
    }

    public VirtualEditionInterDto getVirtualEditionInterByUrlId(String urlId) {
        return this.virtualProvidesInterface.getVirtualEditionInterByUrlId(urlId);
    }

    public boolean initializeUserModule() {
       return this.userProvidesInterface.initializeUserModule();
    }
}