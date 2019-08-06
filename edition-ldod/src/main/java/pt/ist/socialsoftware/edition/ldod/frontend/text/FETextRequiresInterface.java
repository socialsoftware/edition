package pt.ist.socialsoftware.edition.ldod.frontend.text;

import pt.ist.socialsoftware.edition.ldod.frontend.user.FEUserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;

public class FETextRequiresInterface {
    // Uses Frontend User Module
    private final FEUserProvidesInterface feUserProvidesInterface = new FEUserProvidesInterface();

    public String getAuthenticatedUser() {
        return this.feUserProvidesInterface.getAuthenticatedUser();
    }


    // Uses Virtual Edition Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public VirtualEditionDto getVirtualEditionByAcronym(String acronym) {
        return this.virtualProvidesInterface.getVirtualEditionByAcronym(acronym);
    }

}