package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;

public class ExpertEditionInterDto {
    private String editionAcronym;
    private String sourceType;
    private String title;

    public ExpertEditionInterDto(ExpertEditionInter expertEditionInter) {
        this.editionAcronym = expertEditionInter.getEdition().getAcronym();
        this.sourceType = expertEditionInter.getSourceType().name();
        this.title = expertEditionInter.getTitle();
    }

    public String getEditionAcronym() {
        return editionAcronym;
    }

    public void setEditionAcronym(String editionAcronym) {
        this.editionAcronym = editionAcronym;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
