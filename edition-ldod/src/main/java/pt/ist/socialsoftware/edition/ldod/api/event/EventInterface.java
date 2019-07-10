package pt.ist.socialsoftware.edition.ldod.api.event;

import pt.ist.socialsoftware.edition.ldod.api.virtual.VirtualInterface;

public class EventInterface {
    private final VirtualInterface virtualInterface = new VirtualInterface();

    public void publish(Event event) {
        this.virtualInterface.notifyEvent(event);
    }
}
