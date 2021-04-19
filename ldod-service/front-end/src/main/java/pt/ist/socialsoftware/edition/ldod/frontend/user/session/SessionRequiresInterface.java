package pt.ist.socialsoftware.edition.ldod.frontend.user.session;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pt.ist.socialsoftware.edition.notification.event.Event;
import pt.ist.socialsoftware.edition.notification.event.EventInterface;
import pt.ist.socialsoftware.edition.notification.event.EventVirtualEditionUpdate;
import pt.ist.socialsoftware.edition.notification.event.SubscribeInterface;
import pt.ist.socialsoftware.edition.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionDto;

import java.util.List;

@Component
public class SessionRequiresInterface implements SubscribeInterface {

    private static SessionRequiresInterface instance;

    public static SessionRequiresInterface getInstance() {
        if (instance == null) {
            instance = new SessionRequiresInterface();
        }
        return instance;
    }

    protected SessionRequiresInterface() {
        EventInterface.getInstance().subscribe(this);
    }


    // Requires asynchronous events
    public void notify(Event event) {
        if (event.getType().equals(Event.EventType.VIRTUAL_EDITION_REMOVE) ||
                event.getType().equals(Event.EventType.VIRTUAL_EDITION_UPDATE)) {
//            HttpSessionConfig.getSessions().values().stream()
//                    .map(FrontendSession.class::cast)
//                    .forEach(frontendSession -> {
            FrontendSession frontendSession = FrontendSession.getFrontendSession();
            if (frontendSession == null) {
                return;
            }
            if (event.getType().equals(Event.EventType.VIRTUAL_EDITION_REMOVE)) {
                frontendSession.removeSelectedVE(event.getIdentifier());
            }
            if (event.getType().equals(Event.EventType.VIRTUAL_EDITION_UPDATE)) {
                EventVirtualEditionUpdate eventVirtualEditionUpdate = (EventVirtualEditionUpdate) event;
                frontendSession.removeSelectedVE(eventVirtualEditionUpdate.getIdentifier());
                frontendSession.addSelectedVE(eventVirtualEditionUpdate.getNewAcronym());
            }
//            ;                   })
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
