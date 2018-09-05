package pt.ist.socialsoftware.edition.ldod.dto;

import java.util.ArrayList;
import java.util.List;

public class GameDTO {

    private String gameId;
    private String owner;
    private String virtualEdition;
    private String fragmentId;
    private boolean isActive;
    private boolean ended;
    private List<String> participants = new ArrayList<>();

    public GameDTO(){

    }

    public GameDTO(String gameId, String owner, String virtualEdition, boolean isActive) {
        this.gameId = gameId;
        this.owner = owner;
        this.virtualEdition = virtualEdition;
        this.isActive = isActive;
    }


    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getVirtualEdition() {
        return virtualEdition;
    }

    public void setVirtualEdition(String virtualEdition) {
        this.virtualEdition = virtualEdition;
    }

    public String getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(String fragmentId) {
        this.fragmentId = fragmentId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean hasEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public void addParticipant(String participant){
        if(!this.participants.contains(participant)){
            this.participants.add(participant);
        }
    }

    public void removeParticipant(String participant){
            this.participants.remove(this.participants.indexOf(participant));
    }
}
