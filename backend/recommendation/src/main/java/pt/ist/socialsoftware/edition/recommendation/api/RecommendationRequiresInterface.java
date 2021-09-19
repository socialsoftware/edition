package pt.ist.socialsoftware.edition.recommendation.api;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.HeteronymDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;
import pt.ist.socialsoftware.edition.notification.event.Event;
import pt.ist.socialsoftware.edition.notification.event.EventInterface;
import pt.ist.socialsoftware.edition.notification.event.SubscribeInterface;

import pt.ist.socialsoftware.edition.recommendation.domain.RecommendationModule;



import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.TEXT_SERVICE_URL;
import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.VIRTUAL_SERVICE_URL;

@Component
public class RecommendationRequiresInterface implements SubscribeInterface {

    private final WebClient.Builder webClient = WebClient.builder().baseUrl(TEXT_SERVICE_URL);

    private static RecommendationRequiresInterface instance;

    public static RecommendationRequiresInterface getInstance() {
        if (instance == null) {
            instance = new RecommendationRequiresInterface();
        }
        return instance;
    }

    protected RecommendationRequiresInterface() {
    }

    @JmsListener(id = "3", containerFactory = "jmsListenerContainerFactory", destination = "test-topic")
    public void listener(Event message){
        this.notify(message);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
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
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
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
    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl(VIRTUAL_SERVICE_URL);


    public List<String> getVirtualEditionSortedCategoryList(String acronym) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/sortedCategory")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionSortedCategoryList(acronym);
    }

    public List<String> getFragmentCategoriesInVirtualEditon(String acronym, String xmlId) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEdition/" + acronym + "/fragmentCategories")
                    .queryParam("xmlId", xmlId)
                    .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
        //        return this.virtualProvidesInterface.getFragmentCategoriesInVirtualEditon(acronym, xmlId);
    }

    public VirtualEditionDto getArchiveEdition() {
        return webClientVirtual.build()
                .get()
                .uri("/archiveVirtualEdition")
                .retrieve()
                .bodyToMono(VirtualEditionDto.class)
                .block();
//        return this.virtualProvidesInterface.getArchiveVirtualEdition();
    }

    public VirtualEditionDto getVirtualEditionByAcronym(String acronym) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym)
                .retrieve()
                .bodyToMono(VirtualEditionDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEdition(acronym);
    }

}
