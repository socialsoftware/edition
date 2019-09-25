package pt.ist.socialsoftware.edition.ldod.frontend.user.session;

import pt.ist.socialsoftware.edition.ldod.api.event.Event;
import pt.ist.socialsoftware.edition.ldod.api.event.EventVirtualEditionUpdate;
import pt.ist.socialsoftware.edition.ldod.frontend.config.HttpSessionConfig;
import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;

import java.util.List;

public class SessionRequiresInterface {
    // Requires asynchronous events
    public void notifyEvent(Event event) {
        if (event.getType().equals(Event.EventType.VIRTUAL_EDITION_REMOVE) ||
                event.getType().equals(Event.EventType.VIRTUAL_EDITION_UPDATE)) {
            HttpSessionConfig.getSessions().values().stream()
                    .map(FrontendSession.class::cast)
                    .forEach(frontendSession -> {
                        if (event.getType().equals(Event.EventType.VIRTUAL_EDITION_REMOVE)) {
                            frontendSession.removeSelectedVE(event.getIdentifier());
                        }
                        if (event.getType().equals(Event.EventType.VIRTUAL_EDITION_UPDATE)) {
                            EventVirtualEditionUpdate eventVirtualEditionUpdate = (EventVirtualEditionUpdate) event;
                            frontendSession.removeSelectedVE(eventVirtualEditionUpdate.getIdentifier());
                            frontendSession.addSelectedVE(eventVirtualEditionUpdate.getNewAcronym());
                        }
                    });
        }
    }

    // Uses Virtual Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public VirtualEditionDto getVirtualEditionByAcronym(String acronym) {
        return this.virtualProvidesInterface.getVirtualEditionByAcronym(acronym);
    }

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
