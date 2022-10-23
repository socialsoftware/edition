package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame;

import java.util.List;
import java.util.stream.Collectors;

public class VeClassGameDto {

    private String veTitle;
    private String winnerUsername;
    private String categoryName;


    private List<GameParticipantDto> participants;

    public VeClassGameDto(ClassificationGame game) {
        setParticipants(game.getClassificationGameParticipantSet().stream().map(GameParticipantDto::new).collect(Collectors.toList()));
        setVeTitle(game.getVirtualEditionInter().getTitle());
        if(game.getTag() != null) {
            setWinnerUsername(game.getTag().getContributor().getUsername());
            setCategoryName(game.getTag().getCategory().getName());
        }

    }

    public String getVeTitle() {
        return veTitle;
    }

    public void setVeTitle(String veTitle) {
        this.veTitle = veTitle;
    }

    public String getWinnerUsername() {
        return winnerUsername;
    }

    public void setWinnerUsername(String winnerUsername) {
        this.winnerUsername = winnerUsername;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<GameParticipantDto> getParticipants() {
        return participants;
    }

    public void setParticipants(List<GameParticipantDto> participants) {
        this.participants = participants;
    }
}
