package pt.ist.socialsoftware.edition.ldod.api.event;

import pt.ist.socialsoftware.edition.ldod.api.ldod.LdoDInterface;

public class EventInterface {
    private final LdoDInterface ldoDInterface = new LdoDInterface();

    public void publish(Event event) {
        this.ldoDInterface.notifyEvent(event);
    }
}
