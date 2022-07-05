package pt.ist.socialsoftware.edition.ldod.dto;

import java.util.ArrayList;
import java.util.List;

public class GameTagDto implements  Comparable{
    private String authorId;
    private String gameId;
    private String fragmentUrlId;
    private String content;
    private double score = 0;
    private List<String> coAuthors = new ArrayList<>();
    private List<String> voters = new ArrayList<>();

    public GameTagDto() {
    }

    public GameTagDto(String authorId, String gameId, String fragmentUrlId, String content, double score) {
        this.authorId = authorId;
        this.gameId = gameId;
        this.fragmentUrlId = fragmentUrlId;
        this.content = content;
        this.score = score;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getFragmentUrlId() {
        return fragmentUrlId;
    }

    public void setFragmentUrlId(String fragmentUrlId) {
        this.fragmentUrlId = fragmentUrlId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public List<String> getCoAuthors() {
        return coAuthors;
    }

    public void setCoAuthors(List<String> coAuthors) {
        this.coAuthors = coAuthors;
    }

    public void addCoAuthor(String coAuthor){
        if(!this.coAuthors.contains(coAuthor) && !coAuthor.equals(authorId)){
            this.coAuthors.add(coAuthor);
        }
    }

    public void removeCoAuthor(String coAuthor){
        this.voters.remove(this.coAuthors.indexOf(coAuthor));
    }

    public List<String> getVoters() {
        return voters;
    }

    public void setVoters(List<String> voters) {
        this.voters = voters;
    }

    public void addVoter(String voter){
        if(!this.voters.contains(voter) && !voter.equals(authorId)){
            this.voters.add(voter);
        }
    }

    public void removeVoter(String voter){
        this.voters.remove(this.voters.indexOf(voter));
    }

    @Override
    public int compareTo(Object o) {
        double score = ((GameTagDto) o).getScore();
        return Double.compare(this.getScore(), score);
    }
}
