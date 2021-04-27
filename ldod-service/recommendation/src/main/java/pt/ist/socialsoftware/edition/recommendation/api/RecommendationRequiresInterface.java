package pt.ist.socialsoftware.edition.recommendation.api;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.notification.event.Event;
import pt.ist.socialsoftware.edition.notification.event.EventInterface;
import pt.ist.socialsoftware.edition.notification.event.SubscribeInterface;
import pt.ist.socialsoftware.edition.recommendation.domain.RecommendationModule;

import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.textDto.FragmentDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.HeteronymDto;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RecommendationRequiresInterface implements SubscribeInterface {

    private WebClient.Builder webClient = WebClient.builder().baseUrl("http://localhost:8081/api");
//    private WebClient.Builder webClient = WebClient.builder().baseUrl("http://docker-text:8081/api");


    private static RecommendationRequiresInterface instance;

    public static RecommendationRequiresInterface getInstance() {
        if (instance == null) {
            instance = new RecommendationRequiresInterface();
        }
        return instance;
    }

    protected RecommendationRequiresInterface() {
        EventInterface.getInstance().subscribe(this);
    }

    public void notify(Event event) {
        if (event.getType().equals(Event.EventType.USER_REMOVE)) {
            String username = event.getIdentifier();
            RecommendationModule recommendationModule = RecommendationModule.getInstance();

            recommendationModule.getRecommendationWeightsSet().stream()
                    .filter(recommendationWeights -> recommendationWeights.getUser().equals(username))
                    .forEach(recommendationWeights -> recommendationWeights.remove());
        } else if (event.getType().equals(Event.EventType.VIRTUAL_EDITION_REMOVE)) {
            String virtualEditionAcronym = event.getIdentifier();
            RecommendationModule recommendationModule = RecommendationModule.getInstance();

            recommendationModule.getRecommendationWeightsSet().stream()
                    .filter(recommendationWeights -> recommendationWeights.getVirtualEditionAcronym().equals(virtualEditionAcronym))
                    .forEach(recommendationWeights -> recommendationWeights.remove());
        }

    }

    // Uses Text Module
 //   private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    public Set<FragmentDto> getFragments() {
        return webClient.build()
                .get()
                .uri("/fragments")
                .retrieve()
                .bodyToFlux(FragmentDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //  return this.textProvidesInterface.getFragmentDtoSet();
    }

    public List<HeteronymDto> getSortedHeteronymsList() {
      //  return this.textProvidesInterface.getSortedHeteronymList();
        return webClient.build()
                .get()
                .uri("/sortedHeteronyms")
                .retrieve()
                .bodyToFlux(HeteronymDto.class)
                .collectList()
                .block();
    }

    public Map<String, Double> getFragmentTFIDF(String xmlId, List<String> commonTerms) {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/fragment/" + xmlId + "/TFIDF")
                        .queryParam("terms", commonTerms)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Double>>() {})
                .block();
        //   return this.textProvidesInterface.getFragmentTFIDF(xmlId, commonTerms);
    }

    public List<String> getFragmentTFIDF(String xmlId, int numberOfTerms) {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/fragment/" + xmlId + "/TFIDF")
                        .queryParam("numberOfTerms", numberOfTerms)
                        .build())
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
        //  return this.textProvidesInterface.getFragmentTFIDF(xmlId, numberOfTerms);
    }

    public void cleanTermsTFIDFCache() {
         webClient.build()
                .get()
                .uri("/clearTermsTFIDFCache")
                .retrieve();
        //   this.textProvidesInterface.clearTermsTFIDFCache();
    }


    // Uses Virtual Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public List<String> getVirtualEditionSortedCategoryList(String acronym) {
        return this.virtualProvidesInterface.getVirtualEditionSortedCategoryList(acronym);
    }

    public List<String> getFragmentCategoriesInVirtualEditon(String acronym, String xmlId) {
        return this.virtualProvidesInterface.getFragmentCategoriesInVirtualEditon(acronym, xmlId);
    }

}
