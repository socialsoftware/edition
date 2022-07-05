package pt.ist.socialsoftware.edition.ldod.dto;

import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame;

public class ClassificationGameDto {
	private String gameExternalId;
	private String virtualEditionAcronym;
	private String virtualEditionTitle;
	private boolean openAnnotation;
	private long dateTime;
	private VirtualEditionInterDto virtualEditionInterDto;

	public ClassificationGameDto() {

	}

	public ClassificationGameDto(ClassificationGame game) {
		setGameExternalId(game.getExternalId());

		setVirtualEditionAcronym(game.getVirtualEdition().getAcronym());
		setVirtualEditionTitle(game.getVirtualEdition().getTitle());
		setOpenAnnotation(game.getOpenAnnotation());
		setDateTime(game.getDateTime().getMillis());
		setVirtualEditionInterDto(new VirtualEditionInterDto(game.getVirtualEditionInter()));
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

	public VirtualEditionInterDto getVirtualEditionInterDto() {
		return this.virtualEditionInterDto;
	}

	public void setVirtualEditionInterDto(VirtualEditionInterDto virtualEditionInterDto) {
		this.virtualEditionInterDto = virtualEditionInterDto;
	}

}
