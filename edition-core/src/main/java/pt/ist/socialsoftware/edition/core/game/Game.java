package pt.ist.socialsoftware.edition.core.game;

public class Game {

    private final long id;
    private String content;
    private String username;
    private String virtualEdition;
    private long points;
    private int rankingPosition;

    public Game() {
        this.id = -1;
        this.content = "";
    }

    public Game(long id, String content, String username, String virtualEdition) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.virtualEdition = virtualEdition;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public int getRankingPosition() {
        return rankingPosition;
    }

    public void setRankingPosition(int rankingPosition) {
        this.rankingPosition = rankingPosition;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVirtualEdition() {
        return virtualEdition;
    }

    public void setVirtualEdition(String virtualEdition) {
        this.virtualEdition = virtualEdition;
    }
}