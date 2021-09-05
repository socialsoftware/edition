package pt.ist.socialsoftware.edition.notification.dtos.game;


public class ClassificationVirtualGameDto {
	private String gameExternalId;
	private String virtualEditionAcronym;
	private String virtualEditionTitle;
	private boolean openAnnotation;
	private long dateTime;
	private VirtualEditionInterGameDto virtualEditionInterGameDto;

	public ClassificationVirtualGameDto() {
	}

	public String getGameExternalId() {
		return this.gameExternalId;
	}

	public void setGameExternalId(String gameExternalId) {
		this.gameExternalId = gameExternalId;
	}

	public String getVirtualEditionAcronym() {
		return this.virtualEditionAcronym;
	}

	public void setVirtualEditionAcronym(String virtualEditionAcronym) {
		this.virtualEditionAcronym = virtualEditionAcronym;
	}

	public String getVirtualEditionTitle() {
		return this.virtualEditionTitle;
	}

	public void setVirtualEditionTitle(String virtualEditionTitle) {
		this.virtualEditionTitle = virtualEditionTitle;
	}

	public boolean isOpenAnnotation() {
		return this.openAnnotation;
	}

	public void setOpenAnnotation(boolean openAnnotation) {
		this.openAnnotation = openAnnotation;
	}

	public long getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	public VirtualEditionInterGameDto getVirtualEditionInterGameDto() {
		return this.virtualEditionInterGameDto;
	}

	public void setVirtualEditionInterGameDto(VirtualEditionInterGameDto virtualEditionInterGameDto) {
		this.virtualEditionInterGameDto = virtualEditionInterGameDto;
	}

}
