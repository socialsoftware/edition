package pt.ist.socialsoftware.edition.notification.dtos.game;

public class ClassificationGameParticipantDto {

    private double score;
    private String playerUsername;

    public ClassificationGameParticipantDto() {}

    public double getScore() {
        return score;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

}
