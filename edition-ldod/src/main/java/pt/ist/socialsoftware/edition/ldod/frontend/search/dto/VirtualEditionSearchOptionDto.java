package pt.ist.socialsoftware.edition.ldod.frontend.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.frontend.search.FESearchRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.search.feature.options.VirtualEditionSearchOption;

public final class VirtualEditionSearchOptionDto extends SearchOptionDto {

    private final FESearchRequiresInterface feSearchRequiresInterface = new FESearchRequiresInterface();

    private final String virtualEditionAcronym;
    private final boolean inclusion;
    private final String username;


    public VirtualEditionSearchOptionDto(@JsonProperty("inclusion") String inclusion,
                                         @JsonProperty("edition") String virtualEditionAcronym) {
        if (inclusion.equals("in")) {
            this.inclusion = true;
        } else {
            this.inclusion = false;
        }
        this.virtualEditionAcronym = virtualEditionAcronym;

        this.username = this.feSearchRequiresInterface.getAuthenticatedUser();
    }

    @Override
    public VirtualEditionSearchOption createSearchOption() {
        return new VirtualEditionSearchOption(this);
    }

    public String getVirtualEditionAcronym() {
        return this.virtualEditionAcronym;
    }

    public boolean isInclusion() {
        return this.inclusion;
    }

    public String getUsername() {
        return this.username;
    }
}
