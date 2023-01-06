package pt.ist.socialsoftware.edition.ldod.bff.search.dto;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;

import java.util.List;
import java.util.Set;

public class AdvancedEditionDto {
    private String acronym;
    private String editor;
    private Set<AdvancedHeteronymDto> heteronyms;
    private String xmlId;
    private boolean dated;
    private boolean inclusion;
    private String beginDate;
    private String endDate;

    public AdvancedEditionDto() {
    }

    public AdvancedEditionDto(ExpertEdition ed, Set<AdvancedHeteronymDto> heteronyms, List<String> sortedYears) {
        setAcronym(ed.getAcronym());
        setEditor(ed.getEditor());
        setHeteronyms(heteronyms);
        setBeginDate(sortedYears.isEmpty() ? null : sortedYears.get(0));
        setEndDate(sortedYears.isEmpty() ? null : sortedYears.get(sortedYears.size() - 1));

    }


    public String getXmlId() {
        return xmlId;
    }

    public boolean isDated() {
        return dated;
    }

    public void setDated(boolean dated) {
        this.dated = dated;
    }

    public boolean isInclusion() {
        return inclusion;
    }

    public void setInclusion(boolean inclusion) {
        this.inclusion = inclusion;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public Set<AdvancedHeteronymDto> getHeteronyms() {
        return heteronyms;
    }

    public void setHeteronyms(Set<AdvancedHeteronymDto> heteronyms) {
        this.heteronyms = heteronyms;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    @Override
    public String toString() {
        return "AdvancedEditionDto{" +
                "acronym='" + acronym + '\'' +
                ", editor='" + editor + '\'' +
                ", heteronyms=" + heteronyms +
                ", xmlId='" + xmlId + '\'' +
                ", dated=" + dated +
                ", inclusion=" + inclusion +
                ", beginDate='" + beginDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
