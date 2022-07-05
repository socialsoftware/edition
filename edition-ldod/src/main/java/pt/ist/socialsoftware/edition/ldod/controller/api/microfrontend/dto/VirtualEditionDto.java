package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import org.joda.time.LocalDate;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

import java.util.List;
import java.util.stream.Collectors;

public class VirtualEditionDto {

    private boolean participantSetContains;
    private List<VirtualEditionInterDto> sortedInter4Frag;
    private String acronym;
    private String externalId;
    private boolean isLdoDEdition;
    private boolean isAdmin;
    private boolean isMember;
    private boolean isPending;
    private boolean isPublic;
    private String title;
    private String date;
    private String shortAcronym;
    private boolean openManagement;
    private boolean openAnnotation;
    private boolean openVocabulary;
    private String mediaSource;
    private LocalDate beginDate;
    private LocalDate endDate;
    private boolean containsEveryCountry;
    private List<CountryDto> countryContains;
    private String synopsis;
    private int frequency;
    private List<MemberDto> activeMemberSet;
    private List<MemberDto> pendingMemberSet;
    private List<FragInterDto> sortedInterps;
    private List<ParticipantDto> participantSet;
    private List<CategoryDto> categorySet;
    private List<String> annotationTextList;
    private Boolean canAddFragInter;


    public VirtualEditionDto(VirtualEdition vEdition, Fragment fragment, LdoDUser user, FragInter frag) {
        this.setSortedInter4Frag(vEdition.getSortedInter4Frag(fragment).stream().map(VirtualEditionInterDto::new).collect(Collectors.toList()));
        this.setAcronym(vEdition.getAcronym());
        if (user != null) {
            this.setParticipantSetContains(vEdition.getParticipantSet().contains(user));
        }
        this.setExternalId(vEdition.getExternalId());
        if (frag != null) {
            this.setCanAddFragInter(vEdition.canAddFragInter(frag));
        }
    }

    public VirtualEditionDto(VirtualEdition vEdition, LdoDUser user) {
        this.setExternalId(vEdition.getExternalId());
        this.setAcronym(vEdition.getAcronym());
        this.setLdoDEdition(vEdition.isLdoDEdition());
        this.setAdmin(vEdition.getAdminSet().contains(user));
        this.setMember(vEdition.getParticipantSet().contains(user));
        this.setPending(vEdition.getPendingSet().contains(user));
        this.setPublic(vEdition.getPub());
        this.setTitle(vEdition.getTitle());
        this.setDate(vEdition.getDate().toString("dd-MM-yyyy"));
    }

    public VirtualEditionDto(VirtualEdition vEdition, LdoDUser user, int Flag) {
        this.setExternalId(vEdition.getExternalId());
        this.setAcronym(vEdition.getAcronym());
        this.setLdoDEdition(vEdition.isLdoDEdition());
        this.setAdmin(vEdition.getAdminSet().contains(user));
        this.setMember(vEdition.getParticipantSet().contains(user));
        this.setPending(vEdition.getPendingSet().contains(user));
        this.setPublic(vEdition.getPub());
        this.setTitle(vEdition.getTitle());
        this.setDate(vEdition.getDate().toString("dd-MM-yyyy"));
        this.setActiveMemberSet(vEdition.getActiveMemberSet().stream().map(member -> new MemberDto(member, vEdition, user)).collect(Collectors.toList()));
        this.setPendingMemberSet(vEdition.getPendingMemberSet().stream().map(member -> new MemberDto(member, vEdition, user)).collect(Collectors.toList()));
    }


    public VirtualEditionDto(VirtualEdition vEdition, LdoDUser user, List<String> countriesList) {
        this.setExternalId(vEdition.getExternalId());
        this.setAcronym(vEdition.getAcronym());
        this.setLdoDEdition(vEdition.isLdoDEdition());
        this.setAdmin(vEdition.getAdminSet().contains(user));
        this.setMember(vEdition.getParticipantSet().contains(user));
        this.setPending(vEdition.getPendingSet().contains(user));
        this.setPublic(vEdition.getPub());
        this.setTitle(vEdition.getTitle());
        this.setDate(vEdition.getDate().toString("dd-MM-yyyy"));
        this.setShortAcronym(vEdition.getShortAcronym());
        this.setOpenManagement(vEdition.getTaxonomy().getOpenManagement());
        this.setOpenAnnotation(vEdition.getTaxonomy().getOpenAnnotation());
        this.setOpenVocabulary(vEdition.getTaxonomy().getOpenVocabulary());
        if (vEdition.getMediaSource() != null) {
            this.setMediaSource(vEdition.getMediaSource().getName());
        }
        if (vEdition.getTimeWindow() != null) {
            this.setBeginDate(vEdition.getTimeWindow().getBeginDate());
            this.setEndDate(vEdition.getTimeWindow().getEndDate());
        }
        if (vEdition.getGeographicLocation() != null) {
            this.setContainsEveryCountry(vEdition.getGeographicLocation().containsEveryCountry());
            this.setCountryContains(countriesList.stream().map(country -> new CountryDto(country, vEdition.getGeographicLocation().containsCountry(country))).collect(Collectors.toList()));
        }
        this.setSynopsis(vEdition.getSynopsis());
        if (vEdition.getFrequency() != null) {
            this.setFrequency(vEdition.getFrequency().getFrequency());
        }
    }

    public VirtualEditionDto(VirtualEdition vEdition) {
        this.setAcronym(vEdition.getAcronym());
        this.setExternalId(vEdition.getExternalId());
        this.setPublic(vEdition.getPub());
        this.setTitle(vEdition.getTitle());
        this.setDate(vEdition.getDate().toString("dd-MM-yyyy"));
        this.setLdoDEdition(vEdition.isLdoDEdition());
        this.setSortedInterps(vEdition.getSortedInterps().stream().map(FragInterDto::new).collect(Collectors.toList()));
        this.setParticipantSet(vEdition.getParticipantSet().stream().map(ParticipantDto::new).collect(Collectors.toList()));
        this.setCategorySet(vEdition.getTaxonomy().getCategoriesSet().stream().map(CategoryDto::new).collect(Collectors.toList()));
        this.setAnnotationTextList(vEdition.getAnnotationTextList());
    }

    public VirtualEditionDto(VirtualEdition vEdition, CategoryDto.PresentationTarget type) {
        this.setAcronym(vEdition.getAcronym());
        this.setExternalId(vEdition.getExternalId());
        this.setTitle(vEdition.getTitle());
        this.setParticipantSet(vEdition.getParticipantSet().stream().map(ParticipantDto::new).collect(Collectors.toList()));
        this.setCategorySet(vEdition.getTaxonomy().getCategoriesSet().stream().map(cat -> new CategoryDto(cat.getName())).collect(Collectors.toList()));
        this.setAnnotationTextList(vEdition.getAnnotationTextList());
    }

    public VirtualEditionDto(VirtualEdition vEdition, boolean noUser) {
        this.setExternalId(vEdition.getExternalId());
        this.setAcronym(vEdition.getAcronym());
        this.setLdoDEdition(vEdition.isLdoDEdition());
        this.setAdmin(false);
        this.setMember(false);
        this.setPending(false);
        this.setPublic(vEdition.getPub());
        this.setTitle(vEdition.getTitle());
        this.setDate(vEdition.getDate().toString("dd-MM-yyyy"));
    }

    public boolean isParticipantSetContains() {
        return participantSetContains;
    }

    public void setParticipantSetContains(boolean participantSetContains) {
        this.participantSetContains = participantSetContains;
    }

    public List<VirtualEditionInterDto> getSortedInter4Frag() {
        return sortedInter4Frag;
    }

    public void setSortedInter4Frag(List<VirtualEditionInterDto> sortedInter4Frag) {
        this.sortedInter4Frag = sortedInter4Frag;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public boolean isLdoDEdition() {
        return isLdoDEdition;
    }

    public void setLdoDEdition(boolean isLdoDEdition) {
        this.isLdoDEdition = isLdoDEdition;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean isMember) {
        this.isMember = isMember;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean isPending) {
        this.isPending = isPending;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShortAcronym() {
        return shortAcronym;
    }

    public void setShortAcronym(String shortAcronym) {
        this.shortAcronym = shortAcronym;
    }

    public boolean isOpenManagement() {
        return openManagement;
    }

    public void setOpenManagement(boolean openManagement) {
        this.openManagement = openManagement;
    }

    public boolean isOpenAnnotation() {
        return openAnnotation;
    }

    public void setOpenAnnotation(boolean openAnnotation) {
        this.openAnnotation = openAnnotation;
    }

    public boolean isOpenVocabulary() {
        return openVocabulary;
    }

    public void setOpenVocabulary(boolean openVocabulary) {
        this.openVocabulary = openVocabulary;
    }

    public String getMediaSource() {
        return mediaSource;
    }

    public void setMediaSource(String mediaSource) {
        this.mediaSource = mediaSource;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isContainsEveryCountry() {
        return containsEveryCountry;
    }

    public void setContainsEveryCountry(boolean containsEveryCountry) {
        this.containsEveryCountry = containsEveryCountry;
    }

    public List<CountryDto> getCountryContains() {
        return countryContains;
    }

    public void setCountryContains(List<CountryDto> countryContains) {
        this.countryContains = countryContains;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public List<MemberDto> getActiveMemberSet() {
        return activeMemberSet;
    }

    public void setActiveMemberSet(List<MemberDto> activeMemberSet) {
        this.activeMemberSet = activeMemberSet;
    }

    public List<MemberDto> getPendingMemberSet() {
        return pendingMemberSet;
    }

    public void setPendingMemberSet(List<MemberDto> pendingMemberSet) {
        this.pendingMemberSet = pendingMemberSet;
    }

    public List<FragInterDto> getSortedInterps() {
        return sortedInterps;
    }

    public void setSortedInterps(List<FragInterDto> sortedInterps) {
        this.sortedInterps = sortedInterps;
    }

    public List<ParticipantDto> getParticipantSet() {
        return participantSet;
    }

    public void setParticipantSet(List<ParticipantDto> participantSet) {
        this.participantSet = participantSet;
    }

    public List<CategoryDto> getCategorySet() {
        return categorySet;
    }

    public void setCategorySet(List<CategoryDto> categorySet) {
        this.categorySet = categorySet;
    }

    public List<String> getAnnotationTextList() {
        return annotationTextList;
    }

    public void setAnnotationTextList(List<String> annotationTextList) {
        this.annotationTextList = annotationTextList;
    }

    public Boolean getCanAddFragInter() {
        return canAddFragInter;
    }

    public void setCanAddFragInter(Boolean canAddFragInter) {
        this.canAddFragInter = canAddFragInter;
    }
}
