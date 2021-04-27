package pt.ist.socialsoftware.edition.ldod.frontend.reading;



import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.recommendation.api.RecommendationProvidesInterface;
import pt.ist.socialsoftware.edition.recommendation.api.dto.WeightsDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.FragmentDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.ScholarInterDto;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FeReadingRequiresInterface {

    public WebClient.Builder webClient = WebClient.builder().baseUrl("http://localhost:8081/api");
//    public WebClient.Builder webClient = WebClient.builder().baseUrl("http://docker-text:8081/api");

    // Uses Text Module

    public List<ExpertEditionDto> getSortedExpertEditionsDto() {
        return   webClient.build()
                .get()
                .uri("/sortedExpertEditions")
                .retrieve()
                .bodyToFlux(ExpertEditionDto.class).toStream().collect(Collectors.toList());
        //   return this.textProvidesInterface.getSortedExpertEditionsDto();
    }

    public FragmentDto getFragmentByXmlId(String xmlId) {
        return   webClient.build()
                .get()
                .uri("/fragment/xmlId/" + xmlId)
                .retrieve()
                .bodyToMono(FragmentDto.class)
                .blockOptional().orElse(null);
        //    return this.textProvidesInterface.getFragmentByXmlId(xmlId);
    }

    public ScholarInterDto getExpertEditionFirstInterpretation(String acronym) {
        return   webClient.build()
                .get()
                .uri("/expertEdition/" + acronym + "/firstInterpretation")
                .retrieve()
                .bodyToMono(ScholarInterDto.class)
                .blockOptional().get();
        //  return this.textProvidesInterface.getExpertEditionFirstInterpretation(acronym);
    }

    public ScholarInterDto getScholarInterbyExternalId(String expertEditionInterId) {
        return   webClient.build()
                .get()
                .uri("/scholarinter/ext/" + expertEditionInterId)
                .retrieve()
                .bodyToMono(ScholarInterDto.class)
                .blockOptional().get();
        //    return this.textProvidesInterface.getScholarInterbyExternalId(expertEditionInterId);
    }

    public Set<FragmentDto> getFragmentDtoSet() {
        return   webClient.build()
                .get()
                .uri("/fragments")
                .retrieve()
                .bodyToFlux(FragmentDto.class).toStream().collect(Collectors.toSet());
 //       return this.textProvidesInterface.getFragmentDtoSet();
    }

    // Uses Recommendation Module
    private final RecommendationProvidesInterface recommendationProvidesInterface = new RecommendationProvidesInterface();

    public List<Map.Entry<FragmentDto, Double>> getMostSimilarFragmentsOfGivenFragment(FragmentDto toReadFragment, Set<FragmentDto> toBeRecommended, WeightsDto weightsDto) {
        return this.recommendationProvidesInterface.getMostSimilarFragmentsOfGivenFragment(toReadFragment, toBeRecommended, weightsDto);
    }


}
