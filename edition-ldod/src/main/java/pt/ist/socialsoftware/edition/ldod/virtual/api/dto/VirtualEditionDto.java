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
        return this.virtualProvidesInterface.getVirtualEditionExternalIdByAcronym(this.acronym);
    }

    public String getTitle() {
        return this.virtualProvidesInterface.getVirtualEditionTitleByAcronym(this.acronym);
    }

    public List<String> getSortedCategorySet() {
        return this.virtualProvidesInterface.getVirtualEditionSortedCategoryList(this.acronym);
    }

    public List<VirtualEditionInterDto> getSortedVirtualEditionInterDtoList() {
        return this.virtualProvidesInterface.getSortedVirtualEditionInterDtoList(this.acronym);
    }

    public Set<VirtualEditionInterDto> getVirtualEditionInterSetForFragment(String xmlId) {
        return this.virtualProvidesInterface.getVirtualEditionInterOfFragmentForVirtualEdition(this.acronym, xmlId);
    }

    public String getReference() {
        return this.virtualProvidesInterface.getVirtualEditionReference(this.acronym);
    }

    public boolean getTaxonomyVocabularyStatus() {
        return this.virtualProvidesInterface.getVirtualEditionTaxonomyVocabularyStatus(this.acronym);
    }

    public boolean isLdoDEdition() {
        return this.virtualProvidesInterface.isLdoDEdition(this.acronym);
    }

    public boolean isPublicOrIsParticipant() {
        return this.virtualProvidesInterface.isVirtualEditionPublicOrIsUserParticipant(this.acronym);
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
        return this.virtualProvidesInterface.getVirtualEditionPub(this.acronym);
    }

    public LocalDate getDate() {
        return this.virtualProvidesInterface.getVirtualEditionDate(this.acronym);
    }


}
