package pt.ist.socialsoftware.edition.recommendation.api.textDto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class ExpertEditionDto {

//    private final WebClient.Builder webClient = WebClient.builder().baseUrl("http://localhost:8081/api");
    private WebClient.Builder webClient = WebClient.builder().baseUrl("http://docker-text:8081/api");


    private final String acronym;

    // cached attributes
    private String editor;
    private String externalId;
    private String author;
    private String title;



    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ExpertEditionDto(@JsonProperty("acronym") String acronym) {
        this.acronym = acronym;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public String getEditor() {
        //return this.textProvidesInterface.getExpertEditionEditorByEditionAcronym(this.acronym);
        return this.editor;
    }

    public String getExternalId() {
        return externalId;
    }


    public List<ScholarInterDto> getExpertEditionInters() {
        return webClient.build()
                .get()
                .uri("/expertEdition/" + this.acronym + "/scholarInterList")
                .retrieve()
                .bodyToFlux(ScholarInterDto.class)
                .collectList()
                .block();
        //    return this.textProvidesInterface.getExpertEditionScholarInterDtoList(this.acronym);
    }

    public List<ScholarInterDto> getSortedInter4Frag(String fragmentXmlId) {
        return webClient.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/fragment/" + fragmentXmlId + "/expertEditionSortedInter")
                        .queryParam("acronym", this.acronym)
                        .build()
                )
                .retrieve()
                .bodyToFlux(ScholarInterDto.class)
                .collectList()
                .block();
        //  return this.textProvidesInterface.getExpertEditionSortedInter4Frag(this.acronym, fragmentXmlId);
    }

    public boolean isExpertEdition() {
        return true;
    }

    public boolean isVirtualEdition() {
        return false;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
