package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.ldod.domain.PrintedSource;
import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.domain.SourceInter;

public class SourceInterDto {
    private String sourceType;
    private String title;


    public SourceInterDto(SourceInter sourceInter) {
        this.sourceType = sourceInter.getSourceType().name();

        if (sourceInter.getSource().getType().equals(Source.SourceType.MANUSCRIPT)) {
            ManuscriptSource source = (ManuscriptSource) sourceInter.getSource();
            this.title = "";
        } else {
            PrintedSource source = (PrintedSource) sourceInter.getSource();
            this.title = source.getTitle();
        }
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
