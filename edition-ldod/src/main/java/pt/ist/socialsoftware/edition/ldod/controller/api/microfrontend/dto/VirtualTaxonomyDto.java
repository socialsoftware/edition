package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.utils.TopicListDTO;

public class VirtualTaxonomyDto {
	private String title;
	private String externalId;
	private String acronym;
	private TaxonomyMicrofrontendDto taxonomy;
	private TopicListDTO topicList;

	public VirtualTaxonomyDto(VirtualEdition vEdition, LdoDUser user) {
		this.setTitle(vEdition.getTitle());
		this.setExternalId(vEdition.getExternalId());
		this.setAcronym(vEdition.getAcronym());
		this.setTaxonomy(new TaxonomyMicrofrontendDto(vEdition, user));
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

	public TaxonomyMicrofrontendDto getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(TaxonomyMicrofrontendDto taxonomy) {
		this.taxonomy = taxonomy;
	}

	public TopicListDTO getTopicList() {
		return topicList;
	}

	public void setTopicList(TopicListDTO topicList) {
		this.topicList = topicList;
	}
}
