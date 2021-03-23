package pt.ist.socialsoftware.edition.notification.event;

public class EventTagRemove extends Event {
    private String interId;

    public EventTagRemove(String tagId, String interId) {
        super(EventType.TAG_REMOVE, tagId);
        this.interId = interId;
    }

    public String getInterId() {
        return interId;
    }
}
