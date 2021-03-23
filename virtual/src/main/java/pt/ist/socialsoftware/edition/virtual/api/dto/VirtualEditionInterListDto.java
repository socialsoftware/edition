package pt.ist.socialsoftware.edition.virtual.api.dto;

import pt.ist.socialsoftware.edition.virtual.domain.VirtualEdition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VirtualEditionInterListDto {

    private List<VirtualEditionInterGameDto> virtualEditionInterList = new ArrayList<>();
    private String title;
    private String acronym;
    private String type;
    private boolean pub;
    private int numberOfInters;
    private TaxonomyDto taxonomy;
    private List<LdoDUserViewDto> members;

    public VirtualEditionInterListDto(VirtualEdition virtualEdition, boolean deep) {
        if (deep) {
            this.setVirtualEditionInterList(virtualEdition.getIntersSet().stream().sorted()
                    .map(i -> new VirtualEditionInterGameDto(new VirtualEditionInterDto(
                            virtualEdition.getFragInterByUrlId(i.getUrlId()))))
                    .collect(Collectors.toList()));
        }
        this.setTitle(virtualEdition.getTitle());
        this.setAcronym(virtualEdition.getAcronym());
        this.type = "virtual";
        this.setPub(virtualEdition.getPub());
        this.numberOfInters = virtualEdition.getIntersSet().size();
        TaxonomyDto taxonomyDto = new TaxonomyDto(virtualEdition.getTaxonomy());
        this.setTaxonomy(taxonomyDto);
        if (deep) {
            this.setMembers(virtualEdition.getActiveMemberSet().stream()
                    .map(member -> new LdoDUserViewDto(member.getUser())).collect(Collectors.toList()));
        }
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
