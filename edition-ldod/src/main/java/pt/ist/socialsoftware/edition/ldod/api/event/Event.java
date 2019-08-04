package pt.ist.socialsoftware.edition.ldod.api.event;

public class Event {

    public enum EventType {
        FRAGMENT_REMOVE, SCHOLAR_INTER_REMOVE, USER_REMOVE, VIRTUAL_EDITION_REMOVE, VIRTUAL_EDITION_UPDATE,
        VIRTUAL_INTER_REMOVE, TAG_REMOVE, VIRTUAL_EXPORT
    }

    EventType type;

    String identifier;


    public Event(EventType type, String identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    public EventType getType() {
        return this.type;
    }

    public String getIdentifier() {
        return this.identifier;
    }

}
