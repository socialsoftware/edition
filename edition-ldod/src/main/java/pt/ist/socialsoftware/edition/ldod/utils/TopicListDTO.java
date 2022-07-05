package pt.ist.socialsoftware.edition.ldod.utils;

import java.util.List;

public class TopicListDTO {
	private String taxonomyExternalId;
	private String username;
	private List<TopicDTO> topics;

	public String getTaxonomyExternalId() {
		return taxonomyExternalId;
	}

	public void setTaxonomyExternalId(String taxonomyExternalId) {
		this.taxonomyExternalId = taxonomyExternalId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<TopicDTO> getTopics() {
		return topics;
	}

	public void setTopics(List<TopicDTO> topics) {
		this.topics = topics;
	}

}
