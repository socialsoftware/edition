package pt.ist.socialsoftware.edition.ldod.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public class VirtualEditionInterListDto {
	private List<VirtualEditionInterDto> virtualEditionInterList = new ArrayList<>();
	private String title;
	private String acronym;
	private boolean pub;
	private TaxonomyDTO taxonomy;
	private List<LdoDUserDTO> members;

	public VirtualEditionInterListDto() {
	}

	public VirtualEditionInterListDto(VirtualEdition virtualEdition) {
		this.setVirtualEditionInterList(
				virtualEdition.getIntersSet().stream().sorted((i1, i2) -> i2.getNumber() - i1.getNumber())
						.map(i -> new VirtualEditionInterDto((VirtualEditionInter) virtualEdition.getFragInterByUrlId(i.getUrlId())))
						.collect(Collectors.toList()));
		this.setTitle(virtualEdition.getTitle());
		this.setAcronym(virtualEdition.getAcronym());
		this.setPub(virtualEdition.getPub());
		TaxonomyDTO taxonomyDTO = new TaxonomyDTO(virtualEdition.getTaxonomy());
		this.setTaxonomy(taxonomyDTO);
		this.setMembers(virtualEdition.getActiveMemberSet().stream().map(member -> new LdoDUserDTO(member.getUser())).collect(Collectors.toList()));

	}

	public List<VirtualEditionInterDto> getVirtualEditionInterList() {
		return this.virtualEditionInterList;
	}

	public void setVirtualEditionInterList(List<VirtualEditionInterDto> virtualEditionInterList) {
		this.virtualEditionInterList = virtualEditionInterList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public boolean isPub() {
		return pub;
	}

	public void setPub(boolean pub) {
		this.pub = pub;
	}

	public TaxonomyDTO getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(TaxonomyDTO taxonomy) {
		this.taxonomy = taxonomy;
	}

	public List<LdoDUserDTO> getMembers() {
		return members;
	}

	public void setMembers(List<LdoDUserDTO> members) {
		this.members = members;
	}
}
