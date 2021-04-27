package pt.ist.socialsoftware.edition.ldod.frontend.search;

import org.joda.time.LocalDate;

import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserProvidesInterface;

import pt.ist.socialsoftware.edition.search.api.SearchProvidesInterface;
import pt.ist.socialsoftware.edition.search.api.dto.AdvancedSearchResultDto;
import pt.ist.socialsoftware.edition.search.api.dto.SearchDto;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.*;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FeSearchRequiresInterface {

    public WebClient.Builder webClient = WebClient.builder().baseUrl("http://localhost:8081/api");
//    public WebClient.Builder webClient = WebClient.builder().baseUrl("http://docker-text:8081/api");

    // Requires from User Module
    private final FeUserProvidesInterface feUserProvidesInterface = new FeUserProvidesInterface();

    public String getAuthenticatedUser() {
        return this.feUserProvidesInterface.getAuthenticatedUser();
    }


    // Requires from Search Module
    SearchProvidesInterface searchProvidesInterface = new SearchProvidesInterface();

    public Map<String, List<ScholarInterDto>> getSimpleSearch(String params) {
        return this.searchProvidesInterface.simpleSearch(params);
    }

    public AdvancedSearchResultDto advancedSearch(SearchDto search, String username) {
        return this.searchProvidesInterface.advancedSearch(search, username);
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
                .uri("/scholarinter/xmlId/" + xmlId)
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
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public Set<VirtualEditionDto> getPublicVirtualEditionsOrUserIsParticipant(String username) {
        return this.virtualProvidesInterface.getPublicVirtualEditionsOrUserIsParticipant(username);
    }

}
