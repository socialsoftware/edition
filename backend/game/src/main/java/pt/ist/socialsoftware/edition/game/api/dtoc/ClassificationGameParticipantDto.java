package pt.ist.socialsoftware.edition.game.api.dtoc;

import pt.ist.socialsoftware.edition.game.domain.ClassificationGameParticipant;

public class ClassificationGameParticipantDto {

    private double score;
    private String playerUsername;

    public ClassificationGameParticipantDto(ClassificationGameParticipant participant) {
        this.score = participant.getScore();
        this.playerUsername = participant.getPlayer().getUser();
    }

    public double getScore() {
        return score;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }
}
