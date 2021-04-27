package pt.ist.socialsoftware.edition.ldod.frontend.user;

import org.joda.time.LocalDate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.RegistrationTokenDto;
import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.UserConnectionDto;
import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.UserDto;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.FragmentDto;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FeUserRequiresInterface {

    // Uses User Module
    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl("http://localhost:8082/api");
//    public WebClient.Builder webClient = WebClient.builder().baseUrl("http://docker-text:8081/api");


    public UserDto getUser(String username) {
        return webClientUser.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/user")
                    .queryParam("username", username)
                    .build())
                .retrieve()
                .bodyToMono(UserDto.class)
                .blockOptional()
                .orElse(null);
        //        return this.userProvidesInterface.getUser(username);
    }

    public void setLastLogin(String username, LocalDate now) {
        webClientUser.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/setLastLogin")
                    .queryParam("username", username)
                    .queryParam("now", now)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .blockOptional();
        //        this.userProvidesInterface.setUserLastLogin(username, now);
    }

    public Set<UserDto> getUsersSet() {
        return webClientUser.build()
                .get()
                .uri("/users")
                .retrieve()
                .bodyToFlux(UserDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.userProvidesInterface.getUsersSet();
    }

    public boolean getAdmin() {
        return webClientUser.build()
                .get()
                .uri("/isAdmin")
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional()
                .orElse(false);
        //        return this.userProvidesInterface.getAdmin();
    }

    public List<UserConnectionDto> getUserConnections(String userId) {
        return webClientUser.build()
                .get()
                .uri("/isAdmin")
                .retrieve()
                .bodyToFlux(UserConnectionDto.class)
                .collectList()
                .block();
        //        return this.userProvidesInterface.getUserConnections(userId);
    }

    public List<UserConnectionDto> getUserConnectionsByProviderId(String userId, String providerId) {
        return webClientUser.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/userConnections")
                    .queryParam("userId", userId)
                    .queryParam("providerId", providerId)
                    .build())
                .retrieve()
                .bodyToFlux(UserConnectionDto.class)
                .collectList()
                .block();
        //        return this.userProvidesInterface.getUserConnectionsByProviderId(userId, providerId);
    }

    public UserConnectionDto getUserConnectionByUserProviderId(String userId, String key, String u) {
        return webClientUser.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/userConnectionByUserProviderId")
                    .queryParam("userId", userId)
                    .queryParam("key", key)
                    .queryParam("u", u)
                    .build())
                .retrieve()
                .bodyToMono(UserConnectionDto.class)
                .block();
        //        return this.userProvidesInterface.getUserConnectionsByUserProviderId(userId, key, u);
    }

    public void createUserConnection(String userId, String providerId, String providerUserId, int nextRank, String displayName, String profileUrl, String imageUrl, String encrypt, String encrypt1, String encrypt2, Long expireTime) {
        webClientUser.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/createUserConnection")
                    .queryParam("userId", userId)
                    .queryParam("providerId", providerId)
                    .queryParam("providerUserId", providerUserId)
                    .queryParam("nextRank", nextRank)
                    .queryParam("displayName", displayName)
                    .queryParam("profileUrl", profileUrl)
                    .queryParam("imageUrl", imageUrl)
                    .queryParam("encrypt", encrypt)
                    .queryParam("encrypt1", encrypt1)
                    .queryParam("encrypt2", encrypt2)
                    .queryParam("expireTime", expireTime)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.userProvidesInterface.createUserConnection(userId, providerId, providerUserId, nextRank, displayName, profileUrl, imageUrl, encrypt, encrypt1, encrypt2, expireTime);
    }

    public List<String> getUserIdsWithConnections(String providerId, String providerUserId) {
        return webClientUser.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/userIdsWithConnections")
                    .queryParam("providerId", providerId)
                    .queryParam("providerUserId", providerUserId)
                    .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
        //        return this.userProvidesInterface.getUserIdsWithConnections(providerId, providerUserId);
    }

    public Set<String> getUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        return webClientUser.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/userIdsConnectedTo")
                    .queryParam("providerId", providerId)
                    .queryParam("providerUserIds", providerUserIds)
                    .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Set<String>>() {})
                .block();

        //        return this.userProvidesInterface.getUserIdsConnectedTo(providerId, providerUserIds);
    }

    public UserDto createUser(PasswordEncoder passwordEncoder, String username, String password, String firstName, String lastName, String email, String socialMediaService, String socialMediaId) {
        return webClientUser.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/createUser")
                    .queryParam("username", username)
                    .queryParam("password", passwordEncoder.encode(password))
                    .queryParam("firstName", firstName)
                    .queryParam("lastName", lastName)
                    .queryParam("email", email)
                    .queryParam("socialMediaService", socialMediaService)
                    .queryParam("socialMediaId", socialMediaId)
                    .build())
                .retrieve()
                .bodyToMono(UserDto.class)
                .blockOptional()
                .orElse(null);
        //        return this.userProvidesInterface.createUser(passwordEncoder, username, password, firstName, lastName, email, socialMediaService, socialMediaId);
    }

    public RegistrationTokenDto getRegistrationToken(String token) {
        return webClientUser.build()
                .get()
                .uri("/registrationToken/" + token)
                .retrieve()
                .bodyToMono(RegistrationTokenDto.class)
                .blockOptional()
                .orElse(null);
        //        return this.userProvidesInterface.getRegistrationToken(token);
    }

    public void switchAdmin() {
        webClientUser.build()
                .post()
                .uri("/switchAdmin")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.userProvidesInterface.switchAdmin();
    }

    public UserDto getUserFromExternalId(String externalId) {
        return webClientUser.build()
                .get()
                .uri("/user/ext/" + externalId)
                .retrieve()
                .bodyToMono(UserDto.class)
                .blockOptional()
                .orElse(null);
        //        return this.userProvidesInterface.getUserFromExternalId(externalId);
    }

    public String exportXMLUsers() {
        return webClientUser.build()
                .get()
                .uri("/exportXMLUsers")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional()
                .orElse(null);
        //        return this.userProvidesInterface.exportXMLUsers();
    }

    public void importUsersFromXML(InputStream inputStream) {
        try {
            webClientUser.build().post()
                    .uri("/importUsersFromXML")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .bodyValue(inputStream.readAllBytes())
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //        this.userProvidesInterface.importUsersFromXML(inputStream);
    }

    public void createTestUsers(PasswordEncoder passwordEncoder) {
        webClientUser.build()
                .post()
                .uri("/createTestUsers")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.userProvidesInterface.createTestUsers(passwordEncoder);
    }

    public void removeAllUserConnectionsByProviderId(String userId, String providerId) {
        webClientUser.build()
                .delete()
                .uri(uriBuilder -> uriBuilder
                    .path("/removeAllUserConnectionsByProviderId")
                    .queryParam("userId", userId)
                    .queryParam("providerId", providerId)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.userProvidesInterface.removeAllUserConnectionsByProviderId(userId, providerId);
    }

    public void removeAllUserConnectionsByConnectionKey(String userId, String providerId, String providerUserId) {
        webClientUser.build()
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/removeAllUserConnectionsByConnectionKey")
                        .queryParam("userId", userId)
                        .queryParam("providerId", providerId)
                        .queryParam("providerUserId", providerUserId)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.userProvidesInterface.removeAllUserConnectionsByConnectionKey(userId, providerId, providerUserId);
    }

    public String getRoleName(String role) {
       return webClientUser.build()
                .get()
                .uri("/role/" + role)
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional()
                .orElse("");
        //        return this.userProvidesInterface.getRoleName(role);
    }

    public RegistrationTokenDto createRegistrationToken(String username, String toString) {
        return webClientUser.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/createRegistrationToken")
                    .queryParam("username", username)
                    .queryParam("toString", toString)
                    .build())
                .retrieve()
                .bodyToMono(RegistrationTokenDto.class)
                .blockOptional()
                .orElse(null);
    }

    public UserDto createTestUser(String username, String password, String firstName, String lastName, String email) {
        return webClientUser.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/createTestUser")
                    .queryParam("username", username)
                    .queryParam("password", password)
                    .queryParam("firstName", firstName)
                    .queryParam("lastName", lastName)
                    .queryParam("email", email)
                    .build())
                .retrieve()
                .bodyToMono(UserDto.class)
                .blockOptional().orElse(null);
    }

    public Set<RegistrationTokenDto> getTokensSet() {
        return webClientUser.build()
                .get()
                .uri("/getTokensSet")
                .retrieve()
                .bodyToFlux(RegistrationTokenDto.class)
                .toStream()
                .collect(Collectors.toSet());
    }

    public void removeOutdatedUnconfirmedUsers() {
        webClientUser.build()
                .post()
                .uri("/removeOutdatedUnconfirmedUsers")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public Set<UserConnectionDto> getUserConnectionSet() {
        return webClientUser.build()
                .get()
                .uri("/getUserConnectionSet")
                .retrieve()
                .bodyToFlux(UserConnectionDto.class)
                .toStream()
                .collect(Collectors.toSet());
    }


    // User Text Module

    public WebClient.Builder webClient = WebClient.builder().baseUrl("http://localhost:8081/api");
//    public WebClient.Builder webClient = WebClient.builder().baseUrl("http://docker-text:8081/api");

    public FragmentDto getFragmentByXmlId(String xmlId) {
        return   webClient.build()
                .get()
                .uri("/fragment/xmlId/" + xmlId)
                .retrieve()
                .bodyToMono(FragmentDto.class)
                .blockOptional().get();
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
        return webClientUser.build()
                .post()
                .uri("/initializeUserModule")
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional()
                .orElse(false);
        //       return this.userProvidesInterface.initializeUserModule();
    }

    public void removeUserModule() {
        webClientUser.build()
                .post()
                .uri("/removeUserModule")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}