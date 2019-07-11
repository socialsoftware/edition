package pt.ist.socialsoftware.edition.ldod.text.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.domain.TextModule;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.Optional;

public class SourceDto {
    private String xmlId;

    public SourceDto(Source source) {
        setXmlId(source.getXmlId());
    }

    public String getXmlId() {
        return this.xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public boolean hasLdoDLabel() {
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getHasLdoDLabel)
                .orElseThrow(LdoDException::new);
    }

    public boolean hasHandNoteSet() {
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(manuscriptSource -> !manuscriptSource.getHandNoteSet().isEmpty())
                .orElseThrow(LdoDException::new);
    }

    public boolean hasTypeNoteSet() {
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(manuscriptSource -> !manuscriptSource.getTypeNoteSet().isEmpty())
                .orElseThrow(LdoDException::new);
    }


    private Optional<Source> getSourceByXmlId(String xmlId) {
        return TextModule.getInstance().getFragmentsSet().stream()
                .flatMap(fragment -> fragment.getSourcesSet().stream())
                .filter(source -> source.getXmlId().equals(xmlId))
                .findAny();
    }


}
