package pt.ist.socialsoftware.edition.ldod.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public class VirtualEditionInterListDto {
	private List<VirtualEditionInterDto> virtualEditionInterList = new ArrayList<>();
	private String title;
	private String acronym;
	private String type;
	private boolean pub;
	private int numberOfInters;
	private TaxonomyDto taxonomy;
	private List<LdoDUserDto> members;

	public VirtualEditionInterListDto() {
	}

	public VirtualEditionInterListDto(ExpertEdition expertEdition) {
		this.setTitle(expertEdition.getTitle() + ", Edição de " + expertEdition.getEditor());
		this.setAcronym(expertEdition.getAcronym());
		this.type = "perito";
		this.setPub(true);
		this.numberOfInters = expertEdition.getIntersSet().size();
		TaxonomyDto taxonomyDto = new TaxonomyDto();
		taxonomyDto.setHasCategories(false);
		this.taxonomy = taxonomyDto;
	}

	public VirtualEditionInterListDto(VirtualEdition virtualEdition, boolean deep) {
		if (deep) {
			this.setVirtualEditionInterList(virtualEdition.getIntersSet().stream().sorted()
					.map(i -> new VirtualEditionInterDto(
							(VirtualEditionInter) virtualEdition.getFragInterByUrlId(i.getUrlId())))
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
					.map(member -> new LdoDUserDto(member.getUser())).collect(Collectors.toList()));
		}
	}

	public List<VirtualEditionInterDto> getVirtualEditionInterList() {
		return this.virtualEditionInterList;
	}

	public void setVirtualEditionInterList(List<VirtualEditionInterDto> virtualEditionInterList) {
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
		return numberOfInters;
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

	public List<LdoDUserDto> getMembers() {
		return this.members;
	}

	public void setMembers(List<LdoDUserDto> members) {
		this.members = members;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
