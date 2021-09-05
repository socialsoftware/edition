package pt.ist.socialsoftware.edition.notification.dtos.virtual;


import java.util.ArrayList;
import java.util.List;

public class VirtualEditionInterListDto {

    private List<VirtualEditionInterGameDto> virtualEditionInterList = new ArrayList<>();
    private String title;
    private String acronym;
    private String type;
    private boolean pub;
    private int numberOfInters;
    private TaxonomyDto taxonomy;
    private List<LdoDUserViewDto> members;

    public VirtualEditionInterListDto() {}

    public List<VirtualEditionInterGameDto> getVirtualEditionInterList() {
        return this.virtualEditionInterList;
    }

    public void setVirtualEditionInterList(List<VirtualEditionInterGameDto> virtualEditionInterList) {
        this.virtualEditionInterList = virtualEditionInterList;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public boolean isPub() {
        return this.pub;
    }

    public void setPub(boolean pub) {
        this.pub = pub;
    }

    public int getNumberOfInters() {
        return this.numberOfInters;
    }

    public void setNumberOfInters(int numberOfInters) {
        this.numberOfInters = numberOfInters;
    }


    public TaxonomyDto getTaxonomy() {
        return this.taxonomy;
    }

    public void setTaxonomy(TaxonomyDto taxonomy) {
        this.taxonomy = taxonomy;
    }

    public List<LdoDUserViewDto> getMembers() {
        return this.members;
    }

    public void setMembers(List<LdoDUserViewDto> members) {
        this.members = members;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
