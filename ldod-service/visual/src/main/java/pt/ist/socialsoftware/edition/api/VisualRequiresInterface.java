package pt.ist.socialsoftware.edition.api;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.api.recommendationDto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.api.recommendationDto.WeightsDto;
import pt.ist.socialsoftware.edition.api.recommendationDto.wrappers.IntersByDistance;
import pt.ist.socialsoftware.edition.api.textDto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.api.textDto.ExpertEditionInterListDto;
import pt.ist.socialsoftware.edition.api.textDto.ScholarInterDto;
import pt.ist.socialsoftware.edition.api.virtualDto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.api.virtualDto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.api.virtualDto.VirtualEditionInterListDto;


import java.util.List;
import java.util.Map;

public class VisualRequiresInterface {

//    private final WebClient.Builder webClient = WebClient.builder().baseUrl("http://localhost:8081/api");
    private WebClient.Builder webClient = WebClient.builder().baseUrl("http://docker-text:8081/api");

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
//    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");
    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://docker-virtual:8083/api");


    public List<VirtualEditionInterListDto> getPublicVirtualEditionInterListDto() {
        return webClientVirtual.build()
                .get()
                .uri("/publicVirtualEditionInterList")
                .retrieve()
                .bodyToFlux(VirtualEditionInterListDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getPublicVirtualEditionInterListDto();
    }

    public VirtualEditionDto getVirtualEdition(String acronym) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym)
                .retrieve()
                .bodyToMono(VirtualEditionDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEdition(acronym);
    }

    public ScholarInterDto getScholarInterByExternalIdOfInter(String interId) {
        ScholarInterDto scholarInterDto =
                this.webClientVirtual.build()
                        .get()
                        .uri("/scholarInter/ext/" + interId)
                        .retrieve()
                        .bodyToMono(ScholarInterDto.class)
                        .block();

//                        .getScholarInterbyExternalId(interId);

        if (scholarInterDto == null) {
            scholarInterDto = getScholarInterbyExternalId(interId);
        }

        return scholarInterDto;
    }


    // Requires the Recommendation Module
//    RecommendationProvidesInterface recommendationProvidesInterface = new RecommendationProvidesInterface();
//    private final WebClient.Builder webClientRecommendation = WebClient.builder().baseUrl("http://localhost:8084/api");
        private final WebClient.Builder webClientRecommendation = WebClient.builder().baseUrl("http://docker-recommendation:8084/api");

    public List<InterIdDistancePairDto> getIntersByDistance(String externalId, WeightsDto weights) {

        ScholarInterDto scholarInterDto = getScholarInterbyExternalId(externalId);
        if (scholarInterDto != null) {
            return getScholarIntersByDistance(scholarInterDto, weights);
        }

        VirtualEditionInterDto virtualEditionInterDto = webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/ext/" + externalId)
                .retrieve()
                .bodyToMono(VirtualEditionInterDto.class)
                .block();
//                this.virtualProvidesInterface.getVirtualEditionInterByExternalId(externalId);
        if (virtualEditionInterDto != null) {
            return geVirtualEditiontIntersByDistance(virtualEditionInterDto, weights);
        }
        return null;
    }


    private List<InterIdDistancePairDto> getScholarIntersByDistance(ScholarInterDto scholarInterDto, WeightsDto weightsDto) {
        return webClientRecommendation.build()
                .post()
                .uri("/scholarIntersByDistance")
                .bodyValue(new IntersByDistance(scholarInterDto, weightsDto))
                .retrieve()
                .bodyToFlux(InterIdDistancePairDto.class)
                .collectList()
                .block();
    }

    private List<InterIdDistancePairDto> geVirtualEditiontIntersByDistance(VirtualEditionInterDto virtualEditionInterDto, WeightsDto weightsDto) {
        return webClientRecommendation.build()
                .post()
                .uri("/virtualEditionIntersByDistance")
                .bodyValue(new IntersByDistance(virtualEditionInterDto, weightsDto))
                .retrieve()
                .bodyToFlux(InterIdDistancePairDto.class)
                .collectList()
                .block();
    }
}
