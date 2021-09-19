package pt.ist.socialsoftware.edition.notification.dtos.virtual;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.LocalDate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.notification.config.CustomLocalDateDeserializer;
import pt.ist.socialsoftware.edition.notification.config.CustomLocalDateSerializer;
import pt.ist.socialsoftware.edition.notification.dtos.user.UserDto;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.VIRTUAL_SERVICE_URL;

public class VirtualEditionDto {
    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl(VIRTUAL_SERVICE_URL);

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

    private Set<String> participants;

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

    @JsonIgnore
    public List<String> getSortedCategorySet() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + this.acronym + "/sortedCategory")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionSortedCategoryList(this.acronym);
    }

    @JsonIgnore
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

    @JsonIgnore
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

    @JsonIgnore
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

    @JsonIgnore
    public Set<String> getAdminSet() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/adminSet")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Set<String>>() {})
                .block();

        //        return this.virtualProvidesInterface.getVirtualEditionAdminSet(this.acronym);
    }

//    @JsonIgnore
//    public Set<String> getParticipantSet() {
//        return webClientVirtual.build()
//                .get()
//                .uri("/virtualEdition/" + this.acronym + "/participants")
//                .retrieve()
//                .bodyToMono(new ParameterizedTypeReference<Set<String>>() {})
//                .block();
//
//        //        return this.virtualProvidesInterface.getVirtualEditionParticipantSet(this.acronym);
//    }


    public Set<String> getParticipantSet() {
        return participants;
    }

    @JsonIgnore
    public List<UserDto> getParticipantList() {
        return getParticipantSet().stream().map(participant -> new UserDto(participant)).sorted(Comparator.comparing(UserDto::getFirstName))
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public Set<String> getPendingSet() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + this.acronym + "/pending")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Set<String>>() {})
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionPendingSet(this.acronym);
    }

    public boolean getPub() {
       //return this.virtualProvidesInterface.getVirtualEditionPub(this.acronym);
       return this.pub;
    }

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    public LocalDate getDate() {
        //return this.virtualProvidesInterface.getVirtualEditionDate(this.acronym);
        return this.date;
    }

    @JsonIgnore
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

    @JsonIgnore
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

    @JsonIgnore
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

    @JsonIgnore
    public TaxonomyDto getTaxonomy() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/taxonomy")
                .retrieve()
                .bodyToMono(TaxonomyDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionTaxonomy(this.acronym);
    }

    @JsonIgnore
    public String getMediaSourceName() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/mediaSource")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //        return this.virtualProvidesInterface.getMediaSourceName(this.acronym);
    }

    @JsonIgnore
    public LocalDate getTimeWindowBeginDate() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/timeWindowBeginDate")
                .retrieve()
                .bodyToMono(LocalDate.class)
                .block();
        //        return this.virtualProvidesInterface.getTimeWindowBeginDate(acronym);
    }

    @JsonIgnore
    public LocalDate getTimeWindowEndDate() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/timeWindowEndDate")
                .retrieve()
                .bodyToMono(LocalDate.class)
                .block();
        //        return this.virtualProvidesInterface.getTimeWindowEndDate(acronym);
    }

    @JsonIgnore
    public boolean containsEveryCountryinGeographicLocation() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/containsEveryCountryinGeographicLocation")
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //        return this.virtualProvidesInterface.containsEveryCountryinGeographicLocation(this.acronym);
    }

    @JsonIgnore
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

    @JsonIgnore
    public int getIntegerFrequency() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/integerFrequency")
                .retrieve()
                .bodyToMono(Integer.class)
                .blockOptional().orElse(0);
        //        return this.virtualProvidesInterface.getIntegerFrequency(this.acronym);
    }

    @JsonIgnore
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

    @JsonIgnore
    public List<VirtualEditionInterDto> getSortedInterps() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/sortedVirtualEditionInterList" )
                .retrieve()
                .bodyToFlux(VirtualEditionInterDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getSortedVirtualEditionInterDtoList(this.acronym);
    }

    @JsonIgnore
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

    @JsonIgnore
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

    @JsonIgnore
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

    @JsonIgnore
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

    @JsonIgnore
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

    @JsonIgnore
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

    @JsonIgnore
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

    @JsonIgnore
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

    @JsonIgnore
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

    @JsonIgnore
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


    @JsonIgnore
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

    @JsonIgnore
    public VirtualEditionInterDto createVirtualEditionInterFromVirtualEditionInter(String externalId, int max) {
        return webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/createVirtualEditionInterFromVirtualEditionInter")
                        .queryParam("externalId", this.externalId)
                        .queryParam("interExternalId", externalId)
                        .queryParam("max", max)
                        .build())
                .retrieve()
                .bodyToMono(VirtualEditionInterDto.class)
                .block();
        //        return this.virtualProvidesInterface.createVirtualEditionInterFromVirtualEditionInter(this.externalId, externalId, max);
    }

    @JsonIgnore
    public Set<String> getAdminSetByExternalId() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + this.externalId + "/adminSetByExternalId")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Set<String>>() {})
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionAdminSetByExternalId(this.externalId);
    }

    @JsonIgnore
    public Set<MemberDto> getActiveMemberSet() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/activeMembers")
                .retrieve()
                .bodyToFlux(MemberDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getActiveMembersFromVirtualEdition(this.acronym);
    }

    @JsonIgnore
    public Set<MemberDto> getPendingMemberSet() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/pendingMember")
                .retrieve()
                .bodyToFlux(MemberDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getPendingMemberFromVirtualEdition(this.acronym);
    }

    @JsonIgnore
    public List<String> getAnnotationTextList() {
        return webClientVirtual.exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build())
                .build()
                .get()
                .uri("/virtualEdition/" + this.acronym + "/annotationTextList")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
        //        this.virtualProvidesInterface.getAnnotationTextListFromVirtualEdition(this.acronym);
    }

    @JsonIgnore
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

    @JsonIgnore
    public Set<MemberDto> getMemberSet() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/members")
                .retrieve()
                .bodyToFlux(MemberDto.class)
                .toStream()
                .collect(Collectors.toSet());

    }

    @JsonIgnore
    public void addMemberAdminByExternalId(String user, boolean b) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/virtualEdition/" + externalId + "/addMemberAdmin")
                        .queryParam("user", user)
                        .queryParam("b", b)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @JsonIgnore
    public int getNumberOfCriterias() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/criteriaSize")
                .retrieve()
                .bodyToMono(Integer.class)
                .blockOptional().orElse(0);
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

    public void setParticipantSet(Set<String> participants) {
        this.participants = participants;
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

    public void setisLdoDEdition(boolean ldoDEdition) {
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
