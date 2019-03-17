package pt.ist.socialsoftware.edition.ldod.api.event;

public class Event {

    public enum EventType {
        FRAG_INTER_REMOVE,
        FRAGMENT_REMOVE
    }

    EventType type;

    String identifier;

    public Event(EventType type, String identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
