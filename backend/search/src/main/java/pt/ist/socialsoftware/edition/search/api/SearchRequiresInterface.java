package pt.ist.socialsoftware.edition.search.api;

import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.LdoDDateDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.SourceDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.TEXT_SERVICE_URL;
import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.VIRTUAL_SERVICE_URL;

public class SearchRequiresInterface {


    private final WebClient.Builder webClient = WebClient.builder().baseUrl(TEXT_SERVICE_URL);

    // Requires from Text Module


    public boolean isSourceInter(String xmlId) {
        return !webClient.build()
                .get()
                .uri("/scholarInter/" + xmlId + "/isExpert")
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //    return !this.textProvidesInterface.isExpertInter(xmlId);
    }

    public SourceDto getSourceOfSourceInter(String xmlId) {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + xmlId + "/source")
                .retrieve()
                .bodyToMono(SourceDto.class)
                .block();
        //  return this.textProvidesInterface.getSourceOfSourceInter(xmlId);
    }

    public String getSourceInterType(String xmlId) {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + xmlId + "/sourceInterType")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //  return this.textProvidesInterface.getSourceInterType(xmlId);
    }

    public LdoDDateDto getScholarInterDate(String xmlId) {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + xmlId + "/date")
                .retrieve()
                .bodyToMono(LdoDDateDto.class)
                .block();
        //    return this.textProvidesInterface.getScholarInterDate(xmlId);
    }

    public boolean isExpertInter(String xmlId) {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + xmlId + "/isExpert")
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //   return this.textProvidesInterface.isExpertInter(xmlId);
    }

    public String getEditionAcronymOfInter(String xmlId) {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + xmlId + "/editionAcronym")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //    return this.textProvidesInterface.getEditionAcronymOfInter(xmlId);
    }

    public String getHeteronymXmlId(String xmlId) {
        return webClient.build()
                .get()
                .uri("/heteronym/xmlId/" + xmlId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //   return this.textProvidesInterface.getHeteronymXmlId(xmlId);
    }

    public List<ScholarInterDto> searchScholarInterForWords(String words) {
        return webClient.build()
                .get()
                .uri( uriBuilder -> uriBuilder
                        .path("/scholarInter/search")
                        .queryParam("words", words)
                        .build())
                .retrieve()
                .bodyToFlux(ScholarInterDto.class)
                .collectList()
                .block();
        //   return this.textProvidesInterface.searchScholarInterForWords(words);
    }

    public Set<FragmentDto> getFragmentDtoSet() {
        return webClient.build()
                .get()
                .uri("/fragments")
                .retrieve()
                .bodyToFlux(FragmentDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //   return this.textProvidesInterface.getFragmentDtoSet();
    }


    // Requires from Virtual Module
    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl(VIRTUAL_SERVICE_URL);


    public Set<VirtualEditionInterDto> getVirtualEditionInterSet() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInterSet")
                .retrieve()
                .bodyToFlux(VirtualEditionInterDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getVirtualEditionInterSet();
    }

    public List<String> getVirtualEditionSortedCategoryList(String xmlId) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + xmlId + "/sortedCategory")
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionSortedCategoryList(xmlId);
    }

    public boolean isInterInVirtualEdition(String interXmlId, String virtualEditionAcronym) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/isInterInVirtualEdition")
                    .queryParam("xmlId", interXmlId)
                    .queryParam("acronym", virtualEditionAcronym)
                    .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //        return this.virtualProvidesInterface.isInterInVirtualEdition(interXmlId, virtualEditionAcronym);
    }

    public String getVirtualEditionAcronymByVirtualEditionInterXmlId(String interXmlId) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionsInter/" + interXmlId + "/virtualEditionAcronym")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionAcronymByVirtualEditionInterXmlId(interXmlId);
    }

    public Set<VirtualEditionDto> getPublicVirtualEditionsOrUserIsParticipant(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEditions/getPublicVirtualEditionsOrUserIsParticipant")
                    .queryParam("username", username)
                    .build())
                .retrieve()
                .bodyToFlux(VirtualEditionDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getPublicVirtualEditionsOrUserIsParticipant(username);
    }

}