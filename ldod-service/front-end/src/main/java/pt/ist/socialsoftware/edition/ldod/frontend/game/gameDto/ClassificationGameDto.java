package pt.ist.socialsoftware.edition.ldod.frontend.game.gameDto;



import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class ClassificationGameDto {

    private final WebClient.Builder webClientGame = WebClient.builder().baseUrl("http://localhost:8085/api");

    private String editionId;
    private String interId;
    private String tagId;
    private String externalId;
    private String description;
    private String responsible;
    private boolean canBeRemoved;

    public ClassificationGameDto() {}

    public String getEditionId() {
        return editionId;
    }

    public void setEditionId(String editionId) {
        this.editionId = editionId;
    }

    public String getInterId() {
        return interId;
    }

    public void setInterId(String interId) {
        this.interId = interId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public boolean canBeRemoved() {
        return canBeRemoved;
    }

    public void setCanBeRemoved(boolean removed) {
        this.canBeRemoved = removed;
    }

    public void remove() {
        webClientGame.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/removeClassificationGame")
                    .queryParam("externalId", externalId)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.gameProvidesInterface.removeClassificationGame(this.externalId);
    }

    @JsonIgnore
    public List<ClassificationGameParticipantDto> getClassificationGameParticipantSet() {
        return webClientGame.build()
                .get()
                .uri("/classificationGameParticipant/ext/" + this.externalId)
                .retrieve()
                .bodyToFlux(ClassificationGameParticipantDto.class)
                .collectList()
                .block();
        //        return this.gameProvidesInterface.getClassificationGameParticipantByExternalId(this.externalId);
    }

    public void addParticipant(String username) {
        webClientGame.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/classificationGame/" + this.externalId + "/addParticipant")
                    .queryParam("username", username)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
