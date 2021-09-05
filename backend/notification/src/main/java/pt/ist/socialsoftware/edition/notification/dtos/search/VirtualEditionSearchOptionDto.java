package pt.ist.socialsoftware.edition.notification.dtos.search;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class VirtualEditionSearchOptionDto extends SearchOptionDto {

    private final String virtualEditionAcronym;
    private final boolean inclusion;
    private  String username;


    public VirtualEditionSearchOptionDto(@JsonProperty("inclusion") String inclusion,
                                         @JsonProperty("edition") String virtualEditionAcronym) {
        if (inclusion.equals("in")) {
            this.inclusion = true;
        } else {
            this.inclusion = false;
        }
        this.virtualEditionAcronym = virtualEditionAcronym;

    }

//    @Override
//    public VirtualEditionSearchOption createSearchOption() {
//        return new VirtualEditionSearchOption(this);
//    }

    public String getVirtualEditionAcronym() {
        return this.virtualEditionAcronym;
    }

    public boolean isInclusion() {
        return this.inclusion;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) { this.username = username; }



}
