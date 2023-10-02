package pt.ist.socialsoftware.edition.notification.dtos.virtual;

import java.util.List;

public class TopicListDto {
	private String taxonomyExternalId;
	private String username;
	private List<TopicDto> topics;

	public TopicListDto() {}

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

	public List<TopicDto> getTopics() {
		return topics;
	}

	public void setTopics(List<TopicDto> topics) {
		this.topics = topics;
	}

}
