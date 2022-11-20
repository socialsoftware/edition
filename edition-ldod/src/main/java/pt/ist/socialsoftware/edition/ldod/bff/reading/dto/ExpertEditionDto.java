package pt.ist.socialsoftware.edition.ldod.bff.reading.dto;

import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.EditorialInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;

import java.util.List;

public class ExpertEditionDto {

    private String externalId;
    private String acronym;
    private String editor;

    private String xmlId;
    private String urlId;

    private String transcript;
    private String transcriptTitle;

    private List<EditorialInterDto> editorialInters;


    public ExpertEditionDto(ExpertEdition expertEdition) {
        setExternalId(expertEdition.getExternalId());
        setAcronym(expertEdition.getAcronym());
        setEditor(expertEdition.getEditor());
        setXmlId(expertEdition.getFirstInterpretation().getFragment().getXmlId());
        setUrlId(expertEdition.getFirstInterpretation().getUrlId());
    }

    public String getTranscriptTitle() {
        return transcriptTitle;
    }

    public void setTranscriptTitle(String transcriptTitle) {
        this.transcriptTitle = transcriptTitle;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getXmlId() {
        return xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public List<EditorialInterDto> getEditorialInters() {
        return editorialInters;
    }

    public void setEditorialInters(List<EditorialInterDto> editorialInters) {
        this.editorialInters = editorialInters;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public static final class ExpertEditionDtoBuilder {
        private final ExpertEditionDto expertEditionDto;


        private ExpertEditionDtoBuilder(ExpertEdition edition) {
            expertEditionDto = new ExpertEditionDto(edition);
        }

        public static ExpertEditionDtoBuilder anExpertEditionDto(ExpertEdition edition) {
            return new ExpertEditionDtoBuilder(edition);
        }

        public ExpertEditionDtoBuilder editorialInters(List<EditorialInterDto> editorialInter) {
            expertEditionDto.setEditorialInters(editorialInter);
            return this;
        }

        public ExpertEditionDtoBuilder transcriptTitle(String transcriptTitle) {
            expertEditionDto.setTranscriptTitle(transcriptTitle);
            return this;
        }

        public ExpertEditionDtoBuilder transcript(String transcription) {
            expertEditionDto.setTranscript(transcription);
            return this;
        }


        public ExpertEditionDto build() {
            return expertEditionDto;
        }
    }
}
