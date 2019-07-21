package pt.ist.socialsoftware.edition.ldod.api.event;

public class Event {

    public enum EventType {
        FRAGMENT_REMOVE, SCHOLAR_INTER_REMOVE, USER_REMOVE, VIRTUAL_EDITION_REMOVE, VIRTUAL_EDITION_UPDATE,
        VIRTUAL_INTER_REMOVE, TAG_REMOVE
    }

    EventType type;

    String identifier;

    String newAcronym;

    public Event(EventType type, String identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    public EventType getType() {
        return this.type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getNewAcronym() {
        return this.newAcronym;
    }

    public void setNewAcronym(String newAcronym) {
        this.newAcronym = newAcronym;
    }

}
