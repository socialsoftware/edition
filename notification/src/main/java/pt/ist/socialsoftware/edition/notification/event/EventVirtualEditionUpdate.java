package pt.ist.socialsoftware.edition.notification.event;

public class EventVirtualEditionUpdate extends Event {
    private String newAcronym;

    public EventVirtualEditionUpdate(String identifier, String newAcronym) {
        super(EventType.VIRTUAL_EDITION_UPDATE, identifier);
        this.newAcronym = newAcronym;
    }

    public String getNewAcronym() {
        return this.newAcronym;
    }

}
