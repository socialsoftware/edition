package pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto;

import java.util.List;

public class TopicDTO {
	private String name;
	private List<TopicInterPercentageDTO> inters;

	public TopicDTO() {

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
