package pt.ist.socialsoftware.edition.ldod.frontend.virtual.assistedordering;

import org.joda.time.LocalDate;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserProvidesInterface;

import pt.ist.socialsoftware.edition.recommendation.api.RecommendationProvidesInterface;
import pt.ist.socialsoftware.edition.recommendation.api.dto.PropertyDto;
import pt.ist.socialsoftware.edition.recommendation.api.virtualDto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.recommendation.api.virtualDto.VirtualEditionInterDto;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FEAssistedOrderingRequiresInterface {
    // Uses User Module
    private final FeUserProvidesInterface feUserProvidesInterface = new FeUserProvidesInterface();

    public String getAuthenticatedUser() {
        return this.feUserProvidesInterface.getAuthenticatedUser();
    }


    // Uses Virtual Module
    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");
//    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://docker-virtual:8083/api");


    public VirtualEditionDto getVirtualEditionByAcronym(String acronym) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/acronym/" + acronym)
                .retrieve()
                .bodyToMono(VirtualEditionDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionByAcronym(acronym);
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

    public void saveVirtualEdition(String acronym, String[] inters) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/saveVirtualEdition")
                    .queryParam("acronym", acronym)
                    .queryParam("inters", inters)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.saveVirtualEdition(acronym, inters);
    }

    public void createVirtualEdition(String username, String acronym, String title, LocalDate localDate, boolean pub, String[] inters) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/createVirtualEdition")
                    .queryParam("username", username)
                    .queryParam("acronym", acronym)
                    .queryParam("title", title)
                    .queryParam("localDate", localDate)
                    .queryParam("pub", pub)
                    .queryParam("inters", inters)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.createVirtualEdition(username, acronym, title, localDate, pub, null, inters);
    }

    // Uses Recommendation Module
    private final RecommendationProvidesInterface recommendationProvidesInterface = new RecommendationProvidesInterface();

    public List<VirtualEditionInterDto> generateRecommendationFromVirtualEditionInter(VirtualEditionInterDto virtualEditionInterDto, String username, VirtualEditionDto virtualEdition, List<PropertyDto> properties) {
        return this.recommendationProvidesInterface.generateRecommendationFromVirtualEditionInter(virtualEditionInterDto, username, virtualEdition, properties.stream().map(PropertyDto::getProperty).collect(Collectors.toList()));
    }


    public List<pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionDto> getVirtualEditionsUserIsParticipant(String username) {
//        return new ArrayList<>(this.virtualProvidesInterface.getVirtualEditionsUserIsParticipant(username));
        return new ArrayList<>(webClientVirtual.build()
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/virtualEditionsInters/getVirtualEditionIntersUserIsContributor")
                .queryParam("username", username)
                .build())
            .retrieve()
            .bodyToFlux(pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionDto.class)
            .collectList()
            .blockOptional().get());

    }

}