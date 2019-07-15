package pt.ist.socialsoftware.edition.ldod.text.api.dto;

import pt.ist.socialsoftware.edition.ldod.api.ui.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.ScholarInter;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;


public class ScholarInterDto {
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

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

    public String getExternalId() {
        return this.textProvidesInterface.getScholarInterExternalId(this.xmlId);
    }

    public LdoDDateDto getLdoDDate() {
        return this.textProvidesInterface.getScholarInterDate(this.xmlId);
    }

    public HeteronymDto getHeteronym() {
        return this.textProvidesInterface.getScholarInterHeteronym(this.xmlId);
    }

    public FragInterDto.InterType getType() {
        return this.textProvidesInterface.isExpertInter(this.xmlId) ? FragInterDto.InterType.EDITORIAL : FragInterDto.InterType.AUTHORIAL;
    }

    public String getFragmentXmlId() {
        return this.textProvidesInterface.getFragmentOfScholarInterDto(this).getXmlId();
    }

    public String getReference() {
        return this.textProvidesInterface.getScholarInterReference(this.xmlId);
    }

    public String getEditionReference() {
        return this.textProvidesInterface.getScholarInterEditionReference(this.xmlId);
    }

    public int getNumber() {
        return this.textProvidesInterface.getScholarInterNumber(this.xmlId);
    }

    public boolean isSourceInter() {
        return !this.textProvidesInterface.isExpertInter(this.xmlId);
    }

    public SourceDto getSourceDto() {
        return this.textProvidesInterface.getSourceOfSourceInter(this.xmlId);
    }

    public String getExpertEditionAcronym() {
        return this.textProvidesInterface.getExpertEditionAcronym(this.xmlId);
    }

    public int getNumberOfTimesCited() {
        return this.textProvidesInterface.getNumberOfTimesCited(this.xmlId);
    }

    public int getNumberOfTimesCitedIncludingRetweets() {
        return this.textProvidesInterface.getNumberOfTimesCitedIncludingRetweets(this.xmlId);
    }

    public FragmentDto getFragmentDto() {
        return this.textProvidesInterface.getFragmentOfScholarInterDto(this);
    }

    public String getTitle() {
        return this.textProvidesInterface.getScholarInterTitle(this.xmlId);
    }

    public String getUrlId() {
        return this.textProvidesInterface.getScholarInterUrlId(this.xmlId);
    }

    public String getShortName() {
        return this.textProvidesInterface.getScholarInterShortName(this.xmlId);
    }
    
    public ScholarInterDto getNextScholarInter() {
        return this.textProvidesInterface.getNextScholarInter(this.xmlId);
    }

    public ScholarInterDto getPrevScholarInter() {
        return this.textProvidesInterface.getPrevScholarInter(this.xmlId);
    }
}
