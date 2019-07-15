package pt.ist.socialsoftware.edition.ldod.virtual.api.dto;

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

    public String getTitle() {
        return this.virtualProvidesInterface.getVirtualEditionTitleByAcronym(this.acronym);
    }

    public List<String> getSortedCategorySet() {
        return this.virtualProvidesInterface.getVirtualEditionSortedCategoryList(this.acronym);
    }

    public Set<VirtualEditionInterDto> getVirtualEditionInterDtoSet() {
        return this.virtualProvidesInterface.getVirtualEditionInterDtoSet(this.acronym);
    }
}
