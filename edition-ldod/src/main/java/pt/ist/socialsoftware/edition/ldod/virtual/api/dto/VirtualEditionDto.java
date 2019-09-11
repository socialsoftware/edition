package pt.ist.socialsoftware.edition.ldod.virtual.api.dto;

import org.joda.time.LocalDate;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;

import java.util.List;
import java.util.Set;

public class VirtualEditionDto {
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    private final String xmlId;
    private final String acronym;

    // cached attributes
    private String externalId;
    private String title;
    private String reference;
    private Boolean isLdoDEdition;
    private Boolean pub;
    private LocalDate date; // ?????
    private Boolean openVocabulary;

    public VirtualEditionDto(VirtualEdition virtualEdition) {
        this.xmlId = virtualEdition.getXmlId();
        this.acronym = virtualEdition.getAcronym();
    }

    public String getXmlId() {
        return this.xmlId;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public String getExternalId() {
        if (externalId == null)
            externalId = this.virtualProvidesInterface.getVirtualEditionExternalIdByAcronym(this.acronym);
        return externalId;
    }

    public String getTitle() {
        if (title == null)
            title = this.virtualProvidesInterface.getVirtualEditionTitleByAcronym(this.acronym);
        return title;
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
        if (reference == null)
            reference = this.virtualProvidesInterface.getVirtualEditionReference(this.acronym);
        return reference;
    }

    public boolean getTaxonomyVocabularyStatus() {
        return this.virtualProvidesInterface.getVirtualEditionTaxonomyVocabularyStatus(this.acronym);
    }

    public boolean isLdoDEdition() {
        if (isLdoDEdition == null)
            isLdoDEdition = this.virtualProvidesInterface.isLdoDEdition(this.acronym);
        return isLdoDEdition;
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

    public Set<String> getPendingSet() {
        return this.virtualProvidesInterface.getVirtualEditionPendingSet(this.acronym);
    }

    public boolean getPub() {
        if (pub == null)
            pub = this.virtualProvidesInterface.getVirtualEditionPub(this.acronym);
        return pub;
    }

    public LocalDate getDate() {
        if (date == null)
            date = this.virtualProvidesInterface.getVirtualEditionDate(this.acronym);
        return date;
    }

    public boolean canAddFragInter(String interXmlId) {
        return this.virtualProvidesInterface.canAddFragInter(this.acronym, interXmlId);
    }

    public boolean canManipulateAnnotation(String username) {
        return this.virtualProvidesInterface.canManipulateAnnotation(this.acronym, username);
    }

    public boolean getOpenVocabulary() {
        if (openVocabulary == null)
            openVocabulary = this.virtualProvidesInterface.getOpenVocabulary(this.acronym);
        return openVocabulary;
    }

    public boolean canManipulateTaxonomy(String username) {
        return this.virtualProvidesInterface.canManipulateTaxonomy(this.acronym, username);
    }
}
