package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant;

public class GameParticipantDto {
    private String username;
    private String externalId;
    private double score;

    public GameParticipantDto(ClassificationGameParticipant participant){
        setUsername(participant.getPlayer().getUser().getUsername());
        setExternalId(participant.getExternalId());
        setScore(participant.getScore());
    }





    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
