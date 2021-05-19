package pt.ist.socialsoftware.edition.ldod.frontend.game;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.game.api.GameProvidesInterface;
import pt.ist.socialsoftware.edition.game.api.dtoc.ClassificationGameDto;
import pt.ist.socialsoftware.edition.game.api.virtualDto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.game.api.virtualDto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.CategoryDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.TagDto;


import java.io.InputStream;
import java.util.List;
import java.util.Set;


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
    private final GameProvidesInterface gameProvidesInterface = new GameProvidesInterface();

    public Set<ClassificationGameDto> getClassificationGamesForEdition(String acronym) {
        return this.gameProvidesInterface.getClassificationGamesForEdition(acronym);
    }

    public void createClassificationGame(VirtualEditionDto virtualEdition, String description, DateTime parse, VirtualEditionInterDto inter, String authenticatedUser) {
        this.gameProvidesInterface.createClassificationGame(virtualEdition, description, parse, inter, authenticatedUser);
    }

    public ClassificationGameDto getClassificationGameByExternalId(String externalId) {
        return this.gameProvidesInterface.getClassificationGameByExternalId(externalId);
    }

    public void importGamesFromTEI(InputStream inputStream) {
        this.gameProvidesInterface.importGamesFromTEI(inputStream);
    }

    public void initializeGameModule() {
        this.gameProvidesInterface.initializeGameModule();
    }

    public void startGameRunner(String id) {
        this.gameProvidesInterface.startGameRunner(id);
    }

    public List<String> getGamesForScheduledTasks(DateTime now) {
        return this.gameProvidesInterface.getGamesForScheduledTasks(now);
    }

    public void manageDailyClassificationGames() {
        this.gameProvidesInterface.manageDailyClassificationGames();
    }
}
