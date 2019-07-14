package pt.ist.socialsoftware.edition.ldod.virtual.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;

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

}
