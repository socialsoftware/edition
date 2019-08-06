package pt.ist.socialsoftware.edition.ldod.game.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.api.event.Event;
import pt.ist.socialsoftware.edition.ldod.api.event.EventTagRemove;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.game.feature.classification.inout.WriteGamestoFile;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.TagDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

import java.util.Set;

public class GameRequiresInterface {
    private static final Logger logger = LoggerFactory.getLogger(GameRequiresInterface.class);

    public void notifyEvent(Event event) {
        if (event.getType().equals(Event.EventType.USER_REMOVE)) {
            String username = event.getIdentifier();

            ClassificationModule.getInstance().getPlayerSet().stream().filter(player -> player.getUser().equals(username))
                    .forEach(Player::remove);
            ClassificationModule.getInstance().getClassificationGameSet().stream()
                    .filter(classificationGame -> classificationGame.getResponsible().equals(username))
                    .forEach(ClassificationGame::remove);
        }
        else if (event.getType().equals(Event.EventType.VIRTUAL_EDITION_REMOVE)){
            String editionId = event.getIdentifier();

            ClassificationModule.getInstance().getClassificationGameSet().stream()
                    .filter(classificationGame -> classificationGame.getEditionId().equals(editionId))
                    .forEach(ClassificationGame::remove);
        }
        else if (event.getType().equals(Event.EventType.VIRTUAL_INTER_REMOVE)){
            String interId = event.getIdentifier();

            ClassificationModule.getInstance().getClassificationGameSet().stream()
                    .filter(classificationGame -> classificationGame.getInterId().equals(interId))
                    .forEach(ClassificationGame::remove);
        }

        else if (event.getType().equals(Event.EventType.TAG_REMOVE)){
            EventTagRemove tagRemove = (EventTagRemove) event;

            String urlId = tagRemove.getIdentifier();
            String interId = tagRemove.getInterId();

            ClassificationModule.getInstance().getClassificationGameSet().stream()
                    .filter(classificationGame -> classificationGame.getTagId().equals(urlId) && classificationGame.getInterId().equals(interId))
                    .forEach(ClassificationGame::remove);
        }
        else if (event.getType().equals(Event.EventType.VIRTUAL_EXPORT)){
            WriteGamestoFile writeGamestoFile = new WriteGamestoFile();
            writeGamestoFile.exportToVirtualZip(event.getIdentifier());
        }
    }

    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public void removeTag(String externalId){
        this.virtualProvidesInterface.removeTagFromInter(externalId);
    }

    public VirtualEditionDto getVirtualEdition(String acronym){
        return this.virtualProvidesInterface.getVirtualEdition(acronym);
    }

    public VirtualEditionInterDto getVirtualEditionInter(String xmlId){
        return this.virtualProvidesInterface.getVirtualEditionInter(xmlId);
    }

    public Set<VirtualEditionDto> getVirtualEditionUserIsParticipant(String username){
        return this.virtualProvidesInterface.getVirtualEditionsUserIsParticipant(username);
    }

    public Set<VirtualEditionInterDto> getVirtualEditionInterDtoSet(String acronym){
        return this.virtualProvidesInterface.getVirtualEditionInterSet(acronym);
    }

    public boolean getOpenVocabularyStatus(String acronym){
        return this.virtualProvidesInterface.getVirtualEditionTaxonomyVocabularyStatus(acronym);
    }

    public boolean getOpenAnnotationStatus(String acronym){
        return this.virtualProvidesInterface.getVirtualEditionTaxonomyAnnotationStatus(acronym);
    }

    public TagDto createTag(String editionId, String interId, String tagName, String user){
        return this.virtualProvidesInterface.createTagInInter(editionId, interId, tagName, user);
    }

    public boolean isUserParticipant(String acronym, String username){
        return this.virtualProvidesInterface.isUserParticipant(acronym, username);
    }
}
