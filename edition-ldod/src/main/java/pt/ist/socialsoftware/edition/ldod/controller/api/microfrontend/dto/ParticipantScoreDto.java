package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant;

public class ParticipantScoreDto {
	
	private String userName;
	private double score;


	public ParticipantScoreDto(ClassificationGameParticipant participant) {
		this.setUserName(participant.getPlayer().getUser().getUsername());
		this.setScore(participant.getScore());
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}


}
