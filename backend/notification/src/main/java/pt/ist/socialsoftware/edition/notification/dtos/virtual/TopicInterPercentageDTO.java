package pt.ist.socialsoftware.edition.notification.dtos.virtual;

public class TopicInterPercentageDTO {
	private String externalId;
	private String title;
	private int percentage;

	public TopicInterPercentageDTO() {}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

}
