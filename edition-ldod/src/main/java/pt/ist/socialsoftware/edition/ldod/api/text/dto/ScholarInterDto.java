package pt.ist.socialsoftware.edition.ldod.api.text.dto;

import pt.ist.socialsoftware.edition.ldod.api.text.TextInterface;
import pt.ist.socialsoftware.edition.ldod.api.ui.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.ScholarInter;

public class ScholarInterDto {
    private final TextInterface textInterface = new TextInterface();

    private String xmlId;

    public ScholarInterDto(String xmlId) {
        setXmlId(xmlId);
    }

    public ScholarInterDto(ScholarInter scholarInter) {
        setXmlId(scholarInter.getXmlId());
    }

    public String getXmlId() {
        return this.xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public LdoDDateDto getLdoDDate() {
        return this.textInterface.getScholarInterDate(this.xmlId);
    }

    public HeteronymDto getHeteronym() {
        return this.textInterface.getScholarInterHeteronym(this.xmlId);
    }

    public FragInterDto.InterType getType() {
        return this.textInterface.isExpertInter(this.xmlId) ? FragInterDto.InterType.EDITORIAL : FragInterDto.InterType.AUTHORIAL;
    }

    public String getFragmentXmlId() {
        return this.textInterface.getFragmentOfScholarInterDto(this).getXmlId();
    }

    public String getReference() {
        return this.textInterface.getScholarInterReference(this.xmlId);
    }

    public String getEditionReference() {
        return this.textInterface.getScholarInterEditionReference(this.xmlId);
    }

    public int getNumber() {
        return this.textInterface.getScholarInterNumber(this.xmlId);
    }

    public boolean isSourceInter() {
        return this.textInterface.isSourceInter(this.xmlId);
    }

    public String getExpertEditionAcronym() {
        return this.textInterface.getExpertEditionAcronym(this.xmlId);
    }

    public int getNumberOfTimesCited() {
        return this.textInterface.getNumberOfTimesCited(this.xmlId);
    }

    public int getNumberOfTimesCitedIncludingRetweets() {
        return this.textInterface.getNumberOfTimesCitedIncludingRetweets(this.xmlId);

    }
}
