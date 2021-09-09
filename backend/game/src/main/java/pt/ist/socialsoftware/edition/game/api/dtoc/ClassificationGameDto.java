package pt.ist.socialsoftware.edition.game.api.dtoc;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;
import pt.ist.socialsoftware.edition.game.api.GameProvidesInterface;
import pt.ist.socialsoftware.edition.game.config.CustomDateTimeDeserializer;
import pt.ist.socialsoftware.edition.game.domain.ClassificationGame;
import pt.ist.socialsoftware.edition.game.domain.ClassificationGameParticipant;
import pt.ist.socialsoftware.edition.notification.config.CustomDateTimeSerializer;

import java.util.Collection;
import java.util.List;

public class ClassificationGameDto {

    private final GameProvidesInterface gameProvidesInterface = new GameProvidesInterface();

    private String editionId;
    private String interId;
    private String tagId;
    private String externalId;
    private String description;
    private String responsible;
    private boolean canBeRemoved;
    private boolean isActive;
    private DateTime dateTime;
    private boolean openAnnotation;

    public ClassificationGameDto(ClassificationGame game){
        setEditionId(game.getEditionId());
        setInterId(game.getInterId());
        setTagId(game.getTagId());
        setExternalId(game.getExternalId());
        setDescription(game.getDescription());
        setResponsible(game.getResponsible());
        this.canBeRemoved = game.canBeRemoved();
        this.isActive = game.isActive();
        this.dateTime = game.getDateTime();
        this.openAnnotation = game.getOpenAnnotation();
    }

    public String getEditionId() {
        return editionId;
    }

    public void setEditionId(String editionId) {
        this.editionId = editionId;
    }

    public String getInterId() {
        return interId;
    }

    public void setInterId(String interId) {
        this.interId = interId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public boolean canBeRemoved() {
        return canBeRemoved;
    }

    public boolean getCanBeRemoved() { return canBeRemoved; }

    public boolean isActive() {
        return isActive;
    }

    public void setisActive(boolean active) {
        isActive = active;
    }

    public boolean getOpenAnnotation() {
        return openAnnotation;
    }

    public void setOpenAnnotation(boolean openAnnotation) {
        this.openAnnotation = openAnnotation;
    }

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void remove() {
        this.gameProvidesInterface.removeClassificationGame(this.externalId);
    }

    public List<ClassificationGameParticipantDto> getClassificationGameParticipantSet() {
        return this.gameProvidesInterface.getClassificationGameParticipantByExternalId(this.externalId);
    }
}
