package pt.ist.socialsoftware.edition.ldod.api.event;

import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualRequiresInterface;

public class EventInterface {
    private final VirtualRequiresInterface virtualRequiresInterface = new VirtualRequiresInterface();

    public void publish(Event event) {
        this.virtualRequiresInterface.notifyEvent(event);
    }
}
