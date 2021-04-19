package pt.ist.socialsoftware.edition.notification.event;

import java.io.Serializable;

public class Event implements Serializable {


    public enum EventType implements Serializable {
        FRAGMENT_REMOVE, SCHOLAR_INTER_REMOVE, USER_REMOVE, VIRTUAL_EDITION_REMOVE, VIRTUAL_EDITION_UPDATE,
        VIRTUAL_INTER_REMOVE, TAG_REMOVE, VIRTUAL_EXPORT, SIMPLE_TEXT_REMOVE
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

    public void setType(EventType type) {
        this.type = type;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
