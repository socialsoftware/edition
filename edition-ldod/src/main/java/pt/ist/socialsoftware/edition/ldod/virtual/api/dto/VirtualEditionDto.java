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
    private boolean isLdoDEdition;
    private LocalDate date;
    private boolean pub;
    private boolean openVocabulary;

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

    public boolean canManipulateTaxonomy(String username) {
        return this.virtualProvidesInterface.canManipulateTaxonomy(this.acronym, username);
    }
}
