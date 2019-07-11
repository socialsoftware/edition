package pt.ist.socialsoftware.edition.ldod.frontend.serverside.search.dto;

import java.util.Map;

public class EditionJson {

    private String editor;
    private final String acronym;
    private Map<String, String> heteronyms;
    private String beginDate;
    private String endDate;

    public EditionJson(String acronym) {
        this.acronym = acronym;
    }

    public EditionJson(String acronym, String editor) {
        this.acronym = acronym;
        this.editor = editor;
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
