package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

public class ClassificationGameContentDto {
	
	
	private String gameTitle;
	private String userName;
	private String name;
	private List<ParticipantScoreDto> participantList;

	public ClassificationGameContentDto(VirtualEdition ve, ClassificationGame game, List<ClassificationGameParticipant> participants) {
			this.setGameTitle(game.getVirtualEditionInter().getTitle());
			if(game.getTag() != null) {
				this.setUserName(game.getTag().getContributor().getUsername());
				this.setName(game.getTag().getCategory().getName());
			}
			this.setParticipantList(participants.stream().map(ParticipantScoreDto::new).collect(Collectors.toList()));
	}

	public String getGameTitle() {
		return gameTitle;
	}

	public void setGameTitle(String gameTitle) {
		this.gameTitle = gameTitle;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ParticipantScoreDto> getParticipantList() {
		return participantList;
	}

	public void setParticipantList(List<ParticipantScoreDto> participantList) {
		this.participantList = participantList;
	}
}
