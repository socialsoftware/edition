package pt.ist.socialsoftware.edition.notification.dtos.game;


public class PlayerDto {

    private String user;
    private double score;

    public PlayerDto() {}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
