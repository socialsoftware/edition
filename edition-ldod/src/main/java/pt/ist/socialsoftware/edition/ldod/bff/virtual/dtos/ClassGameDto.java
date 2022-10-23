package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.dto.LdoDUserDto;

import java.util.List;
import java.util.stream.Collectors;

public class ClassGameDto {

    private String externalId;
    private String description;
    private String title;
    private String veExternalId;

    private String veTitle;
    private boolean active;
    private String date;


    private String category;
    private List<LdoDUserDto> players;
    private String winner;
    private String responsible;

    private String interExternalId;

    private boolean canBeRemoved;

    public ClassGameDto() {
    }

    public ClassGameDto(ClassificationGame game, LdoDUser principal) {
        setActive(game.isActive());
        setExternalId(game.getExternalId());
        setDescription(game.getDescription());
        setTitle(game.getVirtualEditionInter().getTitle());
        setVeTitle(game.getVirtualEdition().getTitle());
        setVeExternalId(game.getVirtualEdition().getExternalId());
        setDate(game.getDateTime().toString("dd/MM/yyyy HH:mm"));
        if (game.getTag() != null) {
            setCategory(game.getTag().getCategory().getName());
            setWinner(String.format("%s %s", game.getTag().getContributor().getFirstName(), game.getTag().getContributor().getLastName()));
        }
        setInterExternalId(game.getVirtualEditionInter().getExternalId());
        setPlayers(game.getClassificationGameParticipantSet()
                .stream()
                .map(participant -> new LdoDUserDto(participant.getPlayer().getUser()))
                .collect(Collectors.toList()));
        setResponsible(String.format("%s %s", game.getResponsible().getFirstName(), game.getResponsible().getLastName()));
        setCanBeRemoved(game.getVirtualEdition().getAdminSet().contains(principal) && game.canBeRemoved());


    }


    public String getVeTitle() {
        return veTitle;
    }

    public void setVeTitle(String veTitle) {
        this.veTitle = veTitle;
    }

    public String getVeExternalId() {
        return veExternalId;
    }

    public void setVeExternalId(String veExternalId) {
        this.veExternalId = veExternalId;
    }

    public String getExternalId() {
        return externalId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getInterExternalId() {
        return interExternalId;
    }

    public void setInterExternalId(String interExternalId) {
        this.interExternalId = interExternalId;
    }

    public boolean isCanBeRemoved() {
        return canBeRemoved;
    }

    public void setCanBeRemoved(boolean canBeRemoved) {
        this.canBeRemoved = canBeRemoved;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<LdoDUserDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<LdoDUserDto> players) {
        this.players = players;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }
}
