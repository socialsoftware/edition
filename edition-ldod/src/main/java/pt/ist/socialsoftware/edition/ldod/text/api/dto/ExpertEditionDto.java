package pt.ist.socialsoftware.edition.ldod.text.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;

import java.util.Map;

public class ExpertEditionDto {

    private String editor;
    private final String acronym;
    private Map<String, String> heteronyms;
    private String beginDate;
    private String endDate;

    public ExpertEditionDto(String acronym) {
        this.acronym = acronym;
    }

    public ExpertEditionDto(String acronym, String editor) {
        this.acronym = acronym;
        this.editor = editor;
    }

    public ExpertEditionDto(ExpertEdition expertEdition) {
        this.acronym = expertEdition.getAcronym();
        this.editor = expertEdition.getEditor();
    }

    public String getEditor() {
        return this.editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public String getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(int i) {
        this.beginDate = Integer.toString(i);
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(int i) {
        this.endDate = Integer.toString(i);
    }

    public Map<String, String> getHeteronyms() {
        return this.heteronyms;
    }

    public void setHeteronyms(Map<String, String> heteronyms) {
        this.heteronyms = heteronyms;
    }
}
