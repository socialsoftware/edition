package pt.ist.socialsoftware.edition.ldod.game.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame;

public class ClassificationGameDto {

    private String editionId;
    private String interId;
    private String tagId;
    private String description;
    private String responsible;

    public ClassificationGameDto(ClassificationGame game){
        setEditionId(game.getEditionId());
        setInterId(game.getInterId());
        setTagId(game.getTagId());
        setDescription(game.getDescription());
        setResponsible(game.getResponsible());
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
}
