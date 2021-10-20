package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.utils.TopicListDTO;

public class VirtualTaxonomyDto {
	private String title;
	private String externalId;
	private String acronym;
	private TaxoDto taxonomy;
	private TopicListDTO topicList;

	public VirtualTaxonomyDto(VirtualEdition vEdition, LdoDUser user) {
		this.setTitle(vEdition.getTitle());
		this.setExternalId(vEdition.getExternalId());
		this.setAcronym(vEdition.getAcronym());
		this.setTaxonomy(new TaxoDto(vEdition, user));
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public TaxoDto getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(TaxoDto taxonomy) {
		this.taxonomy = taxonomy;
	}

	public TopicListDTO getTopicList() {
		return topicList;
	}

	public void setTopicList(TopicListDTO topicList) {
		this.topicList = topicList;
	}
}
