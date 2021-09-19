package pt.ist.socialsoftware.edition.ldod.frontend.search;

import org.joda.time.LocalDate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserProvidesInterface;
import pt.ist.socialsoftware.edition.notification.dtos.search.AdvancedSearchResultDto;
import pt.ist.socialsoftware.edition.notification.dtos.search.SearchDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.*;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.*;

public class FeSearchRequiresInterface {

    public WebClient.Builder webClient = WebClient.builder().baseUrl(TEXT_SERVICE_URL);

    // Requires from User Module
    private final FeUserProvidesInterface feUserProvidesInterface = new FeUserProvidesInterface();

    public String getAuthenticatedUser() {
        return this.feUserProvidesInterface.getAuthenticatedUser();
    }


    // Requires from Search Module
    public WebClient.Builder webClientSearch = WebClient.builder().baseUrl(SEARCH_SERVICE_URL);

    public Map<String, List<ScholarInterDto>> getSimpleSearch(String params) {
        return webClientSearch.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/simpleSearch")
                    .queryParam("params", params)
                    .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, List<ScholarInterDto>>>() {})
                .block();
        //        return this.searchProvidesInterface.simpleSearch(params);
    }

    public AdvancedSearchResultDto advancedSearch(SearchDto search, String username) {
        return webClientSearch.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/advancedSearch")
                    .queryParam("username", username)
                    .build())
                .bodyValue(search)
                .retrieve()
                .bodyToMono(AdvancedSearchResultDto.class)
                .block();
        //        return this.searchProvidesInterface.advancedSearch(search, username);
    }


    // Requires from Text Module

    public String getFragmentTitle(String fragmentXmlId) {
        return   webClient.build()
                .get()
                .uri("/fragment/" + fragmentXmlId + "/title")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional().get();
        //  return this.textProvidesInterface.getFragmentTitle(fragmentXmlId);
    }

    public List<ScholarInterDto> getExpertEditionScholarInterDtoList(String acronym) {
        return   webClient.build()
                .get()
                .uri("/expertEdition/" + acronym + "/scholarInterList")
                .retrieve()
                .bodyToFlux(ScholarInterDto.class)
                .collectList()
                .block();
        //   return this.textProvidesInterface.getExpertEditionScholarInterDtoList(acronym);
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

    public boolean hasTypeNoteSet(String xmlId) {
        return getSourceOfSourceInter(xmlId).hasTypeNoteSet();
    }

    public boolean hasHandNoteSet(String xmlId) {
        return getSourceOfSourceInter(xmlId).hasHandNoteSet();
    }

    public String getSourceInterType(String xmlId) {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + xmlId + "/sourceInterType")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional().get();
   //     return this.textProvidesInterface.getSourceInterType(xmlId);
    }

    public SourceDto getSourceOfSourceInter(String xmlId) {
       return webClient.build()
                .get()
                .uri("/scholarInter/" + xmlId + "/source")
                .retrieve()
                .bodyToMono(SourceDto.class)
                .blockOptional().get();
//        return this.textProvidesInterface.getSourceOfSourceInter(xmlId);
    }

    public String getExpertEditionEditor(String xmlId) {
        return webClient.build()
                .get()
                .uri("/expertEdition/" + xmlId + "/editor")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional().get();
        //    return this.textProvidesInterface.getExpertEditionEditorByScholarInter(xmlId);
    }

    public LocalDate getScholarInterDate(String xmlId) {
//        LdoDDateDto date = this.textProvidesInterface.getScholarInterDate(xmlId);
        LdoDDateDto date = webClient.build()
                .get()
                .uri("/scholarInter/" + xmlId + "/date")
                .retrieve()
                .bodyToMono(LdoDDateDto.class)
                .blockOptional().orElse(null);

        return date != null ? date.getDate() : null;
    }

    //check
//    public FragInterDto.InterType getTypeOfFragInter(String xmlId) {
//        TextProvidesInterface textProvidesInterface = new TextProvidesInterface();
//        ScholarInterDto scholarInterDto = textProvidesInterface.getScholarInter(xmlId);
//        if (scholarInterDto != null) {
//            return scholarInterDto.getType();
//        } else {
//            return FragInterDto.InterType.VIRTUAL;
//        }
//    }

    public FragScholarInterDto.InterType getTypeOfFragInter(String xmlId) {

        ScholarInterDto scholarInterDto = webClient.build()
                .get()
                .uri("/scholarInter/" + xmlId)
                .retrieve()
                .bodyToMono(ScholarInterDto.class)
                .blockOptional().orElse(null);

        if (scholarInterDto != null) {
            return scholarInterDto.getType();
        } else {
            return FragScholarInterDto.InterType.VIRTUAL;
        }
    }

    public String getHeteronymName(String xmlId) {
//        HeteronymDto heteronym = this.textProvidesInterface.getScholarInterHeteronym(xmlId);
        HeteronymDto heteronym = webClient.build()
                .get()
                .uri("/heteronym/scholarInter/" + xmlId)
                .retrieve()
                .bodyToMono(HeteronymDto.class)
                .blockOptional().orElse(null);
        if (heteronym != null) {
            return heteronym.getName();
        }
        return null;
    }

    public List<ExpertEditionDto> getSortedExpertEditionsDto() {
        return webClient.build()
                .get()
                .uri("/sortedExpertEditions")
                .retrieve()
                .bodyToFlux(ExpertEditionDto.class).toStream().collect(Collectors.toList());
        //   return this.textProvidesInterface.getSortedExpertEditionsDto();
    }

    public Set<FragmentDto> getFragmentDtoSet() {
        return webClient.build()
                .get()
                .uri("/fragments")
                .retrieve()
                .bodyToFlux(FragmentDto.class).toStream().collect(Collectors.toSet());
        //   return this.textProvidesInterface.getFragmentDtoSet();
    }

    public Set<HeteronymDto> getHeteronymDtoSet() {
        return  webClient.build()
                .get()
                .uri("/heteronyms")
                .retrieve()
                .bodyToFlux(HeteronymDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //  return this.textProvidesInterface.getHeteronymDtoSet();
    }


    // Requires from Virtual Module
    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl(VIRTUAL_SERVICE_URL);


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
