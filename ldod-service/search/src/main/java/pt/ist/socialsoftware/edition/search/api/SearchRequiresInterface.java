package pt.ist.socialsoftware.edition.search.api;

import org.springframework.web.reactive.function.client.WebClient;

import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.FragmentDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.LdoDDateDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.ScholarInterDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.SourceDto;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchRequiresInterface {


//    public WebClient.Builder webClient = WebClient.builder().baseUrl("http://localhost:8081/api");
    private WebClient.Builder webClient = WebClient.builder().baseUrl("http://docker-text:8081/api");


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
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public Set<VirtualEditionInterDto> getVirtualEditionInterSet() {
        return this.virtualProvidesInterface.getVirtualEditionInterSet();
    }

    public List<String> getVirtualEditionSortedCategoryList(String xmlId) {
        return this.virtualProvidesInterface.getVirtualEditionSortedCategoryList(xmlId);
    }

    public boolean isInterInVirtualEdition(String interXmlId, String virtualEditionAcronym) {
        return this.virtualProvidesInterface.isInterInVirtualEdition(interXmlId, virtualEditionAcronym);
    }

    public String getVirtualEditionAcronymByVirtualEditionInterXmlId(String interXmlId) {
        return this.virtualProvidesInterface.getVirtualEditionAcronymByVirtualEditionInterXmlId(interXmlId);
    }

    public Set<VirtualEditionDto> getPublicVirtualEditionsOrUserIsParticipant(String username) {
        return this.virtualProvidesInterface.getPublicVirtualEditionsOrUserIsParticipant(username);
    }

}