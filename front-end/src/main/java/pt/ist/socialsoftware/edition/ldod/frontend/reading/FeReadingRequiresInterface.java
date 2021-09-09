package pt.ist.socialsoftware.edition.ldod.frontend.reading;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.notification.dtos.recommendation.WeightsDto;
import pt.ist.socialsoftware.edition.notification.dtos.recommendation.wrappers.MostSimilarFragments;
import pt.ist.socialsoftware.edition.notification.dtos.text.ExpertEditionDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.*;

public class FeReadingRequiresInterface {

    public WebClient.Builder webClient = WebClient.builder().baseUrl(TEXT_SERVICE_URL);

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
    public WebClient.Builder webClientRecommendation = WebClient.builder().baseUrl(RECOMMENDATION_SERVICE_URL);


    public List<Map.Entry<String, Double>> getMostSimilarFragmentsOfGivenFragment(FragmentDto toReadFragment, Set<FragmentDto> toBeRecommended, WeightsDto weightsDto) {
        return webClientRecommendation.build()
                .post()
                .uri("/mostSimilarFragmentsOfGivenFragment")
                .bodyValue(new MostSimilarFragments(toReadFragment, toBeRecommended, weightsDto))
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map.Entry<String, Double>>() {})
                .collectList()
                .block();
        //        return this.recommendationProvidesInterface.getMostSimilarFragmentsOfGivenFragment(toReadFragment, toBeRecommended, weightsDto);
    }


    public void initializeRecommendationModule() {
        webClientRecommendation.build()
                .post()
                .uri("/initializeRecommendationModule")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void loadRecommendationCache() {
        webClientRecommendation.build()
                .post()
                .uri("/loadRecommendationCache")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void removeRecommendationModule() {
        webClientRecommendation.build()
                .post()
                .uri("/removeRecommendationModule")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
