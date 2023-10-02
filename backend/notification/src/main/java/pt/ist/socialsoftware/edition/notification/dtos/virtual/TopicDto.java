package pt.ist.socialsoftware.edition.notification.dtos.virtual;

import java.util.List;

public class TopicDto {
	private String name;
	private List<TopicInterPercentageDTO> inters;

	public TopicDto() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TopicInterPercentageDTO> getInters() {
		return inters;
	}

	public void setInters(List<TopicInterPercentageDTO> inters) {
		this.inters = inters;
	}

}
