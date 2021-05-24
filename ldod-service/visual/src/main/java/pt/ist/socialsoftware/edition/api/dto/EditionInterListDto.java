package pt.ist.socialsoftware.edition.api.dto;


import pt.ist.socialsoftware.edition.api.textDto.ExpertEditionInterListDto;
import pt.ist.socialsoftware.edition.api.virtualDto.LdoDUserViewDto;
import pt.ist.socialsoftware.edition.api.virtualDto.TaxonomyDto;
import pt.ist.socialsoftware.edition.api.virtualDto.VirtualEditionInterGameDto;
import pt.ist.socialsoftware.edition.api.virtualDto.VirtualEditionInterListDto;

import java.util.ArrayList;
import java.util.List;

public class EditionInterListDto {
    private List<VirtualEditionInterGameDto> virtualEditionInterList = new ArrayList<>();
    private String title;
    private String acronym;
    private String type;
    private boolean pub;
    private int numberOfInters;
    private TaxonomyDto taxonomy;
    private List<LdoDUserViewDto> members;


    public EditionInterListDto(ExpertEditionInterListDto expertEditionList){
        this.title = expertEditionList.getTitle();
        this.acronym = expertEditionList.getAcronym();
        this.type = expertEditionList.getType();
        this.pub = expertEditionList.isPub();
        this.numberOfInters = expertEditionList.getNumberOfInters();
        TaxonomyDto taxonomyDto = new TaxonomyDto();
        taxonomyDto.setHasCategories(false);
        this.taxonomy = taxonomyDto;
    }

    public EditionInterListDto(VirtualEditionInterListDto virtualEditionList){
        this.virtualEditionInterList = virtualEditionList.getVirtualEditionInterList();
        this.title = virtualEditionList.getTitle();
        this.acronym=virtualEditionList.getAcronym();
        this.type=virtualEditionList.getType();
        this.pub = virtualEditionList.isPub();
        this.numberOfInters = virtualEditionList.getNumberOfInters();
        this.taxonomy = virtualEditionList.getTaxonomy();
        this.members = virtualEditionList.getMembers();
    }

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
