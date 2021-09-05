package pt.ist.socialsoftware.edition.notification.dtos.search;

import java.util.Map;

public class ExpertEditionForSearchDto {

    private String editor;
    private final String acronym;
    private Map<String, String> heteronyms;
    private String beginDate;
    private String endDate;

    public ExpertEditionForSearchDto(String acronym) {
        this.acronym = acronym;
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
