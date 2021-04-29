package pt.ist.socialsoftware.edition.api;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.recommendation.api.RecommendationProvidesInterface;
import pt.ist.socialsoftware.edition.recommendation.api.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.recommendation.api.dto.WeightsDto;



import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionInterListDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.ExpertEditionInterListDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.ScholarInterDto;

import java.util.List;
import java.util.Map;

public class VisualRequiresInterface {

    private final WebClient.Builder webClient = WebClient.builder().baseUrl("http://localhost:8081/api");
//    private WebClient.Builder webClient = WebClient.builder().baseUrl("http://docker-text:8081/api");

    // Requires the Text Module

    //Check later
    public List<ExpertEditionInterListDto> getEditionInterListDto() {
        return webClient.build()
                .get()
                .uri("/expertEditionInters")
                .retrieve()
                .bodyToFlux(ExpertEditionInterListDto.class)
                .collectList()
                .block();
        //  return this.textProvidesInterface.getEditionInterListDto();
    }

    public ExpertEditionDto getExpertEditionDto(String acronym) {
        return webClient.build()
                .get()
                .uri("/expertEdition/acronym/" + acronym)
                .retrieve()
                .bodyToMono(ExpertEditionDto.class)
                .blockOptional().orElse(null);
        //  return this.textProvidesInterface.getExpertEditionDto(acronym);
    }

    public List<ScholarInterDto> getExpertEditionScholarInterDtoList(String acronym) {
        return webClient.build()
                .get()
                .uri("/expertEdition/" + acronym + "/scholarInterList")
                .retrieve()
                .bodyToFlux(ScholarInterDto.class)
                .collectList()
                .block();
        //  return this.textProvidesInterface.getExpertEditionScholarInterDtoList(acronym);
    }

    public String getWriteFromPlainHtmlWriter4OneInter(String xmlId, boolean highlightDiff) {
        return webClient.build()
                .get()
                .uri( uriBuilder -> uriBuilder
                        .path("/writeFromPlainHtmlWriter4OneInter/")
                        .queryParam("xmlId", xmlId)
                        .queryParam("highlightDiff", highlightDiff)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


    public ScholarInterDto getScholarInterbyExternalId(String interId) {
        return webClient.build()
                .get()
                .uri("/scholarinter/ext/" + interId)
                .retrieve()
                .bodyToMono(ScholarInterDto.class)
                .blockOptional().orElse(null);
    }

    public List<Map.Entry<String, Double>> getScholarInterTermFrequency(ScholarInterDto scholarInterDto) {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + scholarInterDto.getXmlId() + "/termFrequency")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map.Entry<String, Double>>() {})
                .collectList()
                .block();
        //  return this.textProvidesInterface.getScholarInterTermFrequency(scholarInterDto);
    }


    // Requires the Virtual Module
    VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public List<VirtualEditionInterListDto> getPublicVirtualEditionInterListDto() {
        return this.virtualProvidesInterface.getPublicVirtualEditionInterListDto();
    }

    public VirtualEditionDto getVirtualEdition(String acronym) {
        return this.virtualProvidesInterface.getVirtualEdition(acronym);
    }

    public ScholarInterDto getScholarInterByExternalIdOfInter(String interId) {
        ScholarInterDto scholarInterDto =
                this.virtualProvidesInterface.getScholarInterbyExternalId(interId);

        if (scholarInterDto == null) {
            scholarInterDto = getScholarInterbyExternalId(interId);
        }

        return scholarInterDto;
    }





    // Requires the Recommendation Module
    RecommendationProvidesInterface recommendationProvidesInterface = new RecommendationProvidesInterface();

    public List<InterIdDistancePairDto> getIntersByDistance(String externalId, WeightsDto weights) {

        ScholarInterDto scholarInterDto = getScholarInterbyExternalId(externalId);
        if (scholarInterDto != null) {
            return this.recommendationProvidesInterface.getIntersByDistance(scholarInterDto, weights);
        }

        VirtualEditionInterDto virtualEditionInterDto = this.virtualProvidesInterface.getVirtualEditionInterByExternalId(externalId);
        if (virtualEditionInterDto != null) {
            return this.recommendationProvidesInterface.getIntersByDistance(virtualEditionInterDto, weights);
        }
        return null;
    }

}
