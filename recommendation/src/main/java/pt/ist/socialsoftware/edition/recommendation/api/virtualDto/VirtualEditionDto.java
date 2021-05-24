package pt.ist.socialsoftware.edition.recommendation.api.virtualDto;

import org.joda.time.LocalDate;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.recommendation.api.userDto.UserDto;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VirtualEditionDto {
    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");
//    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://docker-virtual:8083/api");


    private  String xmlId;
    private  String acronym;

    // cached attributes
    private String externalId;
    private String title;
    private String reference;
    private String synopsis;
    private boolean isLdoDEdition;
    private LocalDate date;
    private boolean pub;
    private boolean openVocabulary;
    private String shortAcronym;
    private int max;

    public VirtualEditionDto() {}

    public String getXmlId() {
        return this.xmlId;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public String getExternalId() {
        //return this.virtualProvidesInterface.getVirtualEditionExternalIdByAcronym(this.acronym);
        return this.externalId;
    }

    public String getTitle() {
        //return this.virtualProvidesInterface.getVirtualEditionTitleByAcronym(this.acronym);
        return this.title;
    }

    public List<String> getSortedCategorySet() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + this.acronym + "/sortedCategory")
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionSortedCategoryList(this.acronym);
    }

    public List<VirtualEditionInterDto> getSortedVirtualEditionInterDtoList() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + this.acronym + "/sortedVirtualEditionInterList")
                .retrieve()
                .bodyToFlux(VirtualEditionInterDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getSortedVirtualEditionInterDtoList(this.acronym);
    }

    public Set<VirtualEditionInterDto> getVirtualEditionInterOfFragmentForVirtualEdition(String xmlId) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEdition/" + this.acronym + "/virtualEditionInterOfFragment")
                    .queryParam("xmlId", xmlId)
                    .build())
                .retrieve()
                .bodyToFlux(VirtualEditionInterDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getVirtualEditionInterOfFragmentForVirtualEdition(this.acronym, xmlId);
    }

    public String getReference() {
        //return this.virtualProvidesInterface.getVirtualEditionReference(this.acronym);
        return this.reference;
    }

    public boolean getTaxonomyVocabularyStatus() {
        //return this.virtualProvidesInterface.getVirtualEditionTaxonomyVocabularyStatus(this.acronym);
        return this.openVocabulary;
    }

    public String getShortAcronym() {
        return shortAcronym;
    }

    public boolean isLdoDEdition() {
        //return this.virtualProvidesInterface.isLdoDEdition(this.acronym);
        return this.isLdoDEdition;
    }

    public boolean isPublicOrIsParticipant(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEdition/" + this.acronym + "/isPublicOrIsUserParticipant")
                    .queryParam("username", username)
                    .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional()
                .orElse(false);
        //        return this.virtualProvidesInterface.isVirtualEditionPublicOrIsUserParticipant(this.acronym, username);
    }

    public Set<String> getAdminSet() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/adminSet")
                .retrieve()
                .bodyToFlux(String.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getVirtualEditionAdminSet(this.acronym);
    }

    public Set<String> getParticipantSet() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + this.acronym + "/participants")
                .retrieve()
                .bodyToFlux(String.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getVirtualEditionParticipantSet(this.acronym);
    }

    public List<UserDto> getParticipantList() {
        return getParticipantSet().stream().map(participant -> new UserDto(participant)).sorted(Comparator.comparing(UserDto::getFirstName))
                .collect(Collectors.toList());
    }

    public Set<String> getPendingSet() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + this.acronym + "/pending")
                .retrieve()
                .bodyToFlux(String.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getVirtualEditionPendingSet(this.acronym);
    }

    public boolean getPub() {
       //return this.virtualProvidesInterface.getVirtualEditionPub(this.acronym);
       return this.pub;
    }

    public LocalDate getDate() {
        //return this.virtualProvidesInterface.getVirtualEditionDate(this.acronym);
        return this.date;
    }

    public boolean canAddFragInter(String interXmlId) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEdition/" + this.acronym + "canAddFragInter")
                    .queryParam("interXmlId", interXmlId)
                    .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //        return this.virtualProvidesInterface.canAddFragInter(this.acronym, interXmlId);
    }

    public boolean canManipulateAnnotation(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEdition/" + acronym + "/canManipulateAnnotation")
                    .queryParam("username", username)
                    .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional()
                .orElse(false);
        //        return this.virtualProvidesInterface.canManipulateAnnotation(this.acronym, username);
    }

    public boolean getOpenVocabulary() {
        //return this.virtualProvidesInterface.getOpenVocabulary(this.acronym);
        return this.openVocabulary;
    }

    public int getMaxFragNumber() {
        return max;
    }

    public boolean canManipulateTaxonomy(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/virtualEdition/" + acronym + "/canManipulateTaxonomy")
                        .queryParam("username", username)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional()
                .orElse(false);
        //        return this.virtualProvidesInterface.canManipulateTaxonomy(this.acronym, username);
    }

    public String getSynopsis() {
        return this.synopsis;
    }

    public TaxonomyDto getTaxonomy() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/taxonomy")
                .retrieve()
                .bodyToMono(TaxonomyDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionTaxonomy(this.acronym);
    }

    public String getMediaSourceName() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/mediaSource")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //        return this.virtualProvidesInterface.getMediaSourceName(this.acronym);
    }

    public LocalDate getTimeWindowBeginDate() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/timeWindowBeginDate")
                .retrieve()
                .bodyToMono(LocalDate.class)
                .block();
        //        return this.virtualProvidesInterface.getTimeWindowBeginDate(acronym);
    }

    public LocalDate getTimeWindowEndDate() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/timeWindowEndDate")
                .retrieve()
                .bodyToMono(LocalDate.class)
                .block();
        //        return this.virtualProvidesInterface.getTimeWindowEndDate(acronym);
    }

    public boolean containsEveryCountryinGeographicLocation() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/containsEveryCountryinGeographicLocation")
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //        return this.virtualProvidesInterface.containsEveryCountryinGeographicLocation(this.acronym);
    }

    public boolean containsCountryinGeographicLocation(String country) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/virtualEdition/" + acronym + "/containsCountryinGeographicLocation")
                        .queryParam("country", country)
                .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //        return this.virtualProvidesInterface.containsCountryinGeographicLocation(this.acronym, country);
    }

    public int getIntegerFrequency() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdtion/" + acronym + "/integerFrequency")
                .retrieve()
                .bodyToMono(Integer.class)
                .blockOptional().orElse(0);
        //        return this.virtualProvidesInterface.getIntegerFrequency(this.acronym);
    }

    public Set<VirtualEditionInterDto> getIntersSet() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInterSet/" + acronym )
                .retrieve()
                .bodyToFlux(VirtualEditionInterDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getVirtualEditionInterSet(this.acronym);
    }

    public List<VirtualEditionInterDto> getSortedInterps() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdtion/" + acronym + "/sortedVirtualEditionInterList" )
                .retrieve()
                .bodyToFlux(VirtualEditionInterDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getSortedVirtualEditionInterDtoList(this.acronym);
    }

    public void edit(String acronym, String title, String synopsis, boolean pub, boolean management, boolean vocabulary, boolean annotation, String mediaSource, String beginDate, String endDate, String geoLocation, String frequency) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEdition/" + xmlId + "/edit")
                    .queryParam("acronym", acronym)
                    .queryParam("title", title)
                    .queryParam("synopsis", synopsis)
                    .queryParam("pub", pub)
                    .queryParam("management", management)
                    .queryParam("vocabulary", vocabulary)
                    .queryParam("annotation", annotation)
                    .queryParam("mediaSource", mediaSource)
                    .queryParam("beginDate", beginDate)
                    .queryParam("endDate", endDate)
                    .queryParam("geoLocation", geoLocation)
                    .queryParam("frequency", frequency)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.editVirtualEdition(this.xmlId, acronym, title, synopsis, pub, management, vocabulary, annotation, mediaSource, beginDate, endDate, geoLocation, frequency);
    }

    public void removeByExternalId() {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/removeVirtualEdition")
                    .queryParam("externalId", externalId)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.removeVirtualEditionByExternalId(this.externalId);
    }

    public void updateVirtualEditionIntersFromExternalId(List<String> fragIntersXmlIds) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEdition/" + externalId + "/updateVirtualEditionInters")
                    .queryParam("fragIntersXmlIds", fragIntersXmlIds)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.updateVirtualEditionInters(this.externalId, fragIntersXmlIds);
    }

    public void addMemberByExternalId(String user,  boolean b) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEdition/" + externalId + "/addMember")
                    .queryParam("user", user)
                    .queryParam("b", b)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.addMemberByExternalId(this.externalId, user, b);
    }

    public void cancelParticipationSubmissionByExternalId(String user) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                .path("/virtualEdition/" + externalId + "/cancelParticipationSubmission")
                .queryParam("user", user)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.cancelParticipationSubmissionByExternalId(this.externalId, user);
    }

    public void addApproveByExternalId(String username) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/virtualEdition/" + externalId + "/addApprove")
                        .queryParam("username", username)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.addApproveByExternalId(externalId, username);
    }

    public boolean canSwitchRole(String authenticatedUser, String username) {
        return webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/virtualEdition/" + externalId + "/canSwitchRole")
                        .queryParam("authenticatedUser", authenticatedUser)
                        .queryParam("username", username)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //       return this.virtualProvidesInterface.canSwitchRole(this.externalId, authenticatedUser, username);
    }

    public void switchRole(String username) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEdition/" + externalId + "/switchRole")
                    .queryParam("username", username)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.switchRole(this.externalId, username);
    }

    public boolean canRemoveMember(String authenticatedUser, String user) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEdition/" + externalId + "/canRemoveMember")
                    .queryParam("authenticatedUser", authenticatedUser)
                    .queryParam( "user", user)
                .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //        return this.virtualProvidesInterface.canRemoveMember(externalId, authenticatedUser, user);
    }

    public void removeMember(String user) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEdition/" + externalId + "/removeMember")
                    .queryParam("user", user)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.removeMember(externalId, user);
    }


    public VirtualEditionInterDto createVirtualEditionInterFromScholarInter(String xmlId, int max) {
        return webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/createVirtualEditionInterFromScholarInter")
                    .queryParam("xmlId", xmlId)
                    .queryParam("externalId", externalId)
                    .queryParam("max", max)
                .build())
                .retrieve()
                .bodyToMono(VirtualEditionInterDto.class)
                .block();
        //        return this.virtualProvidesInterface.createVirtualEditionInterFromScholarInter(this.externalId, xmlId, max);
    }

    public VirtualEditionInterDto createVirtualEditionInterFromVirtualEditionInter(String externalId, int max) {
        return webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/createVirtualEditionInterFromScholarInter")
                        .queryParam("externalId", this.externalId)
                        .queryParam("interExternalId", externalId)
                        .queryParam("max", max)
                        .build())
                .retrieve()
                .bodyToMono(VirtualEditionInterDto.class)
                .block();
        //        return this.virtualProvidesInterface.createVirtualEditionInterFromVirtualEditionInter(this.externalId, externalId, max);
    }

    public Set<String> getAdminSetByExternalId() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + this.externalId + "/adminSet")
                .retrieve()
                .bodyToFlux(String.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getVirtualEditionAdminSetByExternalId(this.externalId);
    }

    public Set<MemberDto> getActiveMemberSet() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdtion/" + acronym + "/activeMembers")
                .retrieve()
                .bodyToFlux(MemberDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getActiveMembersFromVirtualEdition(this.acronym);
    }

    public Set<MemberDto> getPendingMemberSet() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdtion/" + acronym + "/pendingMember")
                .retrieve()
                .bodyToFlux(MemberDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getPendingMemberFromVirtualEdition(this.acronym);
    }

    public List<String> getAnnotationTextList() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + this.acronym + "/annotationTextList")
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
        //        this.virtualProvidesInterface.getAnnotationTextListFromVirtualEdition(this.acronym);
    }

    public VirtualEditionInterDto getFragInterByUrlId(String urlId) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEdition/" + acronym + "/fragInter")
                    .queryParam("urlId", urlId)
                    .build())
                .retrieve()
                .bodyToMono(VirtualEditionInterDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionFragInterByUrlId(this.acronym, urlId);
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setLdoDEdition(boolean ldoDEdition) {
        isLdoDEdition = ldoDEdition;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setPub(boolean pub) {
        this.pub = pub;
    }

    public void setOpenVocabulary(boolean openVocabulary) {
        this.openVocabulary = openVocabulary;
    }

    public void setShortAcronym(String shortAcronym) {
        this.shortAcronym = shortAcronym;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
