package pt.ist.socialsoftware.edition.ldod.frontend.game;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import pt.ist.socialsoftware.edition.ldod.frontend.game.gameDto.ClassificationGameDto;
import pt.ist.socialsoftware.edition.ldod.frontend.game.gameDto.PlayerDto;
import pt.ist.socialsoftware.edition.ldod.frontend.game.gameDto.wrapper.CreateGameWrapper;
import pt.ist.socialsoftware.edition.ldod.frontend.text.baseDto.VirtualEditionBaseDto;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.CategoryDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.TagDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionInterDto;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class FeGameRequiresInterface {
    private static final Logger logger = LoggerFactory.getLogger(FeGameRequiresInterface.class);

    // Uses Frontend User Module
    private final FeUserProvidesInterface feUserProvidesInterface = new FeUserProvidesInterface();

    public String getAuthenticatedUser() {
        return this.feUserProvidesInterface.getAuthenticatedUser();
    }


    // Uses User Module
    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl("http://localhost:8082/api");
//    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl("http://docker-user:8082/api");


//    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

    public String getFirstName(String username) {
        return webClientUser.build()
                .get()
                .uri("/user/" + username + "/first")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional()
                .orElse("");
        //        return this.userProvidesInterface.getFirstName(username);
    }

    public String getLastName(String username) {
        return webClientUser.build()
                .get()
                .uri("/user/" + username + "/last")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional()
                .orElse("");
        //        return this.userProvidesInterface.getLastName(username);
    }


    // Uses Virtual Module
    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");
//    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://docker-virtual:8083/api");

    public VirtualEditionDto getVirtualEditionByExternalId(String externalId) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/ext/" + externalId)
                .retrieve()
                .bodyToMono(VirtualEditionDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionByExternalId(externalId);
    }

    public VirtualEditionInterDto getVirtualEditionInterByExternalId(String externalId) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/ext/" + externalId)
                .retrieve()
                .bodyToMono(VirtualEditionInterDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionInterByExternalId(externalId);
    }

    public String getVirtualEditionExternalIdByAcronym(String acronym) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/externalId")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionExternalIdByAcronym(acronym);
    }

    public boolean getVirtualEditionTaxonomyAnnotationStatus(String acronym) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/taxonomyAnnotationStatus")
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //        return this.virtualProvidesInterface.getVirtualEditionTaxonomyAnnotationStatus(acronym);
    }

    public String getVirtualEditionInterTitle(String interId) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/" + interId + "/title")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionInterTitle(interId);
    }

    public TagDto getTagInInter(String interId, String tagId) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEditionInter/" + interId + "/tag")
                    .queryParam("urlId", tagId)
                .build())
                .retrieve()
                .bodyToMono(TagDto.class)
                .block();
        //        return this.virtualProvidesInterface.getTagInInter(interId, tagId);
    }

    public CategoryDto getTagCategory(String interId, String tagId) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                .path("/virtualEditionInter/" + interId + "/tagCategory")
                .queryParam("urlId", tagId)
                .build())
                .retrieve()
                .bodyToMono(CategoryDto.class)
                .block();
        //        return this.virtualProvidesInterface.getTagCategory(interId, tagId);
    }

    //Uses Game Module
    private final WebClient.Builder webClientGame = WebClient.builder().baseUrl("http://localhost:8085/api");
//    private final WebClient.Builder webClientGame = WebClient.builder().baseUrl("http://docker-game:8085/api");

    public Set<ClassificationGameDto> getClassificationGamesForEdition(String acronym) {
        return webClientGame.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/classificationGamesForEdition")
                    .queryParam("acronym", acronym)
                .build())
                .retrieve()
                .bodyToFlux(ClassificationGameDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.gameProvidesInterface.getClassificationGamesForEdition(acronym);
    }

    public void createClassificationGame(VirtualEditionDto virtualEdition, String description, DateTime parse, VirtualEditionInterDto inter, String authenticatedUser) {
        webClientGame.build()
                .post()
                .uri("/createClassificationGame")
                .bodyValue(new CreateGameWrapper(new VirtualEditionBaseDto(virtualEdition), description, parse, inter, authenticatedUser))
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        //        this.gameProvidesInterface.createClassificationGame(virtualEdition, description, parse, inter, authenticatedUser);
    }

    public ClassificationGameDto getClassificationGameByExternalId(String externalId) {
        return webClientGame.build()
                .get()
                .uri("/classificationGame/ext/" + externalId)
                .retrieve()
                .bodyToMono(ClassificationGameDto.class)
                .block();
        //        return this.gameProvidesInterface.getClassificationGameByExternalId(externalId);
    }

    public void importGamesFromTEI(InputStream inputStream) {
        try {
            webClientGame.build()
                    .post()
                    .uri("/importGamesFromTEI")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .bodyValue(inputStream.readAllBytes())
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //        this.gameProvidesInterface.importGamesFromTEI(inputStream);
    }

    public void initializeGameModule() {
        webClientGame.build()
                .post()
                .uri("/initializeGameModule")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.gameProvidesInterface.initializeGameModule();
    }

    public void startGameRunner(String id) {
        System.out.println(id);
        webClientGame.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/startGameRunner")
                    .queryParam("id", id)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.gameProvidesInterface.startGameRunner(id);
    }

    public List<String> getGamesForScheduledTasks(DateTime now) {
        return webClientGame.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/getGamesForScheduledTasks")
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
        //        return this.gameProvidesInterface.getGamesForScheduledTasks(now);
    }

    public void manageDailyClassificationGames() {
        webClientGame.build()
                .post()
                .uri("/manageDailyClassificationGames")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
//        this.gameProvidesInterface.manageDailyClassificationGames();
    }

    public PlayerDto getPlayerByUsername(String username) {
        return webClientGame.build()
                .get()
                .uri("/player/" + username)
                .retrieve()
                .bodyToMono(PlayerDto.class)
                .block();
    }

    public Set<ClassificationGameDto> getClassificationGamesForPlayer(String username) {
        return webClientGame.build()
                .get()
                .uri("/player/" + username + "/classificationGames")
                .retrieve()
                .bodyToFlux(ClassificationGameDto.class)
                .toStream()
                .collect(Collectors.toSet());
    }


    public Set<ClassificationGameDto> getClassificationGames() {
        return webClientGame.build()
                .get()
                .uri("/classificationGames")
                .retrieve()
                .bodyToFlux(ClassificationGameDto.class)
                .toStream()
                .collect(Collectors.toSet());
    }

    public int getOverallUserPosition(String username) {
        return webClientGame.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/overallUserPosition")
                    .queryParam("username", username)
                .build())
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
    }

    // Test Calls
    public void setTestGameRound(String acronym, String username) {
        webClientGame.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/setTestGameRound")
                    .queryParam("acronym", acronym)
                    .queryParam("username", username)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void removeGameModule() {
        webClientGame.build()
                .post()
                .uri("/removeGameModule")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
