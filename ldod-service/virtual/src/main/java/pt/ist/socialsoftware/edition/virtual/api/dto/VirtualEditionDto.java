package pt.ist.socialsoftware.edition.virtual.api.dto;

import org.joda.time.LocalDate;
import pt.ist.socialsoftware.edition.virtual.api.userDto.UserDto;
import pt.ist.socialsoftware.edition.virtual.domain.Member;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VirtualEditionDto {
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    private final String xmlId;
    private final String acronym;

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

    public VirtualEditionDto(VirtualEdition virtualEdition) {
        this.xmlId = virtualEdition.getXmlId();
        this.acronym = virtualEdition.getAcronym();
        this.externalId = virtualEdition.getExternalId();
        this.title = virtualEdition.getTitle();
        this.reference = virtualEdition.getReference();
        this.isLdoDEdition = virtualEdition.isLdoDEdition();
        this.date = virtualEdition.getDate();
        this.pub = virtualEdition.getPub();
        this.openVocabulary = virtualEdition.getTaxonomy().getOpenVocabulary();
        this.synopsis = virtualEdition.getSynopsis();
        this.shortAcronym = virtualEdition.getShortAcronym();
        this.max = virtualEdition.getMaxFragNumber();
    }

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
        return this.virtualProvidesInterface.getVirtualEditionSortedCategoryList(this.acronym);
    }

    public List<VirtualEditionInterDto> getSortedVirtualEditionInterDtoList() {
        return this.virtualProvidesInterface.getSortedVirtualEditionInterDtoList(this.acronym);
    }

    public Set<VirtualEditionInterDto> getVirtualEditionInterOfFragmentForVirtualEdition(String xmlId) {
        return this.virtualProvidesInterface.getVirtualEditionInterOfFragmentForVirtualEdition(this.acronym, xmlId);
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
        return this.virtualProvidesInterface.isVirtualEditionPublicOrIsUserParticipant(this.acronym, username);
    }

    public Set<String> getAdminSet() {
        return this.virtualProvidesInterface.getVirtualEditionAdminSet(this.acronym);
    }

    public Set<String> getParticipantSet() {
        return this.virtualProvidesInterface.getVirtualEditionParticipantSet(this.acronym);
    }

    public List<UserDto> getParticipantList() {
        return getParticipantSet().stream().map(participant -> new UserDto(participant)).sorted(Comparator.comparing(UserDto::getFirstName))
                .collect(Collectors.toList());
    }

    public Set<String> getPendingSet() {
        return this.virtualProvidesInterface.getVirtualEditionPendingSet(this.acronym);
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
        return this.virtualProvidesInterface.canAddFragInter(this.acronym, interXmlId);
    }

    public boolean canManipulateAnnotation(String username) {
        return this.virtualProvidesInterface.canManipulateAnnotation(this.acronym, username);
    }

    public boolean getOpenVocabulary() {
        //return this.virtualProvidesInterface.getOpenVocabulary(this.acronym);
        return this.openVocabulary;
    }

    public int getMaxFragNumber() {
        return max;
    }

    public boolean canManipulateTaxonomy(String username) {
        return this.virtualProvidesInterface.canManipulateTaxonomy(this.acronym, username);
    }

    public String getSynopsis() {
        return this.synopsis;
    }

    public TaxonomyDto getTaxonomy() {
        return this.virtualProvidesInterface.getVirtualEditionTaxonomy(this.acronym);
    }

    public String getMediaSourceName() {
        return this.virtualProvidesInterface.getMediaSourceName(this.acronym);
    }

    public LocalDate getTimeWindowBeginDate() {
        return this.virtualProvidesInterface.getTimeWindowBeginDate(acronym);
    }

    public LocalDate getTimeWindowEndDate() {
        return this.virtualProvidesInterface.getTimeWindowEndDate(acronym);
    }

    public boolean containsEveryCountryinGeographicLocation() {
        return this.virtualProvidesInterface.containsEveryCountryinGeographicLocation(this.acronym);
    }

    public boolean containsCountryinGeographicLocation(String country) {
        return this.virtualProvidesInterface.containsCountryinGeographicLocation(this.acronym, country);
    }

    public int getIntegerFrequency() {
        return this.virtualProvidesInterface.getIntegerFrequency(this.acronym);
    }

    public Set<VirtualEditionInterDto> getIntersSet() {
        return this.virtualProvidesInterface.getVirtualEditionInterSet(this.acronym);
    }

    public List<VirtualEditionInterDto> getSortedInterps() {
        return this.virtualProvidesInterface.getSortedVirtualEditionInterDtoList(this.acronym);
    }

    public void edit(String acronym, String title, String synopsis, boolean pub, boolean management, boolean vocabulary, boolean annotation, String mediaSource, String beginDate, String endDate, String geoLocation, String frequency) {
        this.virtualProvidesInterface.editVirtualEdition(this.xmlId, acronym, title, synopsis, pub, management, vocabulary, annotation, mediaSource, beginDate, endDate, geoLocation, frequency);
    }

    public void removeByExternalId() {
        this.virtualProvidesInterface.removeVirtualEditionByExternalId(this.externalId);
    }

    public void updateVirtualEditionIntersFromExternalId(List<String> fragIntersXmlIds) {
        this.virtualProvidesInterface.updateVirtualEditionInters(this.externalId, fragIntersXmlIds);
    }

    public void addMemberByExternalId(String user,  boolean b) {
        this.virtualProvidesInterface.addMemberByExternalId(this.externalId, user, b);
    }

    public void cancelParticipationSubmissionByExternalId(String user) {
        this.virtualProvidesInterface.cancelParticipationSubmissionByExternalId(this.externalId, user);
    }

    public void addApproveByExternalId(String username) {
        this.virtualProvidesInterface.addApproveByExternalId(externalId, username);
    }

    public boolean canSwitchRole(String authenticatedUser, String username) {
       return this.virtualProvidesInterface.canSwitchRole(this.externalId, authenticatedUser, username);
    }

    public void switchRole(String username) {
        this.virtualProvidesInterface.switchRole(this.externalId, username);
    }

    public boolean canRemoveMember(String authenticatedUser, String user) {
        return this.virtualProvidesInterface.canRemoveMember(externalId, authenticatedUser, user);
    }

    public void removeMember(String user) {
        this.virtualProvidesInterface.removeMember(externalId, user);
    }


    public VirtualEditionInterDto createVirtualEditionInterFromScholarInter(String xmlId, int max) {
        return this.virtualProvidesInterface.createVirtualEditionInterFromScholarInter(this.externalId, xmlId, max);
    }

    public VirtualEditionInterDto createVirtualEditionInterFromVirtualEditionInter(String externalId, int max) {
        return this.virtualProvidesInterface.createVirtualEditionInterFromVirtualEditionInter(this.externalId, externalId, max);
    }

    public Set<String> getAdminSetByExternalId() {
        return this.virtualProvidesInterface.getVirtualEditionAdminSetByExternalId(this.externalId);
    }

    public Set<MemberDto> getActiveMemberSet() {
        return this.virtualProvidesInterface.getActiveMembersFromVirtualEdition(this.acronym);
    }

    public Set<MemberDto> getPendingMemberSet() {
        return this.virtualProvidesInterface.getPendingMemberFromVirtualEdition(this.acronym);
    }

    public void getAnnotationTextList() {
        this.virtualProvidesInterface.getAnnotationTextListFromVirtualEdition(this.acronym);
    }

    public VirtualEditionInterDto getFragInterByUrlId(String urlId) {
        return this.virtualProvidesInterface.getVirtualEditionFragInterByUrlId(this.acronym, urlId);
    }
}
