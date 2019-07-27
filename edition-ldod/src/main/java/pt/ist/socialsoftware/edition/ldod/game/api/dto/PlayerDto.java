package pt.ist.socialsoftware.edition.ldod.game.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Player;

public class PlayerDto {

    private String user;
    private double score;

    public PlayerDto(Player player){
        setUser(player.getUser());
        setScore(player.getScore());
    }

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
