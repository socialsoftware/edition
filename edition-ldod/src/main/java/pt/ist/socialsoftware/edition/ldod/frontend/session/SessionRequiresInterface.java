package pt.ist.socialsoftware.edition.ldod.frontend.session;

import pt.ist.socialsoftware.edition.ldod.api.event.Event;
import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;

import java.util.ArrayList;
import java.util.List;

public class SessionRequiresInterface {
    private static final List<Event> events = new ArrayList<>();

    public static void processEvents() {
        events.stream().filter(event -> event.getType().equals(Event.EventType.VIRTUAL_EDITION_REMOVE))
                .forEach(event -> FrontendSession.getFrontendSession().removeSelectedVE(event.getIdentifier()));

        events.stream().filter(event -> event.getType().equals(Event.EventType.VIRTUAL_EDITION_UPDATE))
                .forEach(event -> {
                    FrontendSession.getFrontendSession().removeSelectedVE(event.getIdentifier());
                    FrontendSession.getFrontendSession().addSelectedVE(event.getNewAcronym());
                });

        events.clear();
    }

    // Requires asynchronous events
    public void notifyEvent(Event event) {
        if (event.getType().equals(Event.EventType.VIRTUAL_EDITION_REMOVE) ||
                event.getType().equals(Event.EventType.VIRTUAL_EDITION_UPDATE)) {
            events.add(event);
        }
    }

    // Uses Virtual Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public void removeSelectedByUser(String user, String virtualEditionAcronym) {
        this.virtualProvidesInterface.removeVirtualEditionSelectedByUser(user, virtualEditionAcronym);
    }

    public void addSelectedByUser(String user, String virtualEditionAcronym) {
        this.virtualProvidesInterface.addVirtualEditionSelectedByUser(user, virtualEditionAcronym);
    }

    public List<String> getSelectedVirtualEditionsByUser(String username) {
        return this.virtualProvidesInterface.getSelectedVirtualEditionsByUser(username);
    }

    // Uses User Module
    UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

    public UserDto getUser(String user) {
        return this.userProvidesInterface.getUser(user);

    }

}
