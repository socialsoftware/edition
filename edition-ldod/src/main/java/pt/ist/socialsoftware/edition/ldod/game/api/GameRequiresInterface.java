package pt.ist.socialsoftware.edition.ldod.game.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.api.event.Event;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationModule;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;

public class GameRequiresInterface {
    private static final Logger logger = LoggerFactory.getLogger(GameRequiresInterface.class);

    public void notifyEvent(Event event) {
        if (event.getType().equals(Event.EventType.USER_REMOVE)) {
            String username = event.getIdentifier();

            ClassificationModule.getInstance().getPlayerSet().stream().filter(player -> player.getUser().equals(username)).forEach(player -> player.remove());
        }
        else if (event.getType().equals(Event.EventType.VIRTUAL_EDITION_REMOVE)){
            String editionId = event.getIdentifier();

        }
        else if (event.getType().equals(Event.EventType.VIRTUAL_INTER_REMOVE)){
            String interId = event.getIdentifier();

        }

        else if (event.getType().equals(Event.EventType.TAG_REMOVE)){
            String externalId = event.getIdentifier();
        }
    }

    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public void removeTag(String externalId){
        this.virtualProvidesInterface.removeTagFromInter(externalId);
    }

}
