package pt.ist.socialsoftware.edition.ldod.text.api.dto;

import pt.ist.socialsoftware.edition.ldod.api.ui.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.ScholarInter;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;

import java.util.List;


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

    public ExpertEditionDto getExpertEdition() {
        return this.textProvidesInterface.getScholarInterExpertEdition(this.xmlId);
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

    public String getVolume() {
        return this.textProvidesInterface.getExpertEditionInterVolume(this.xmlId);
    }

    public String getTranscription() {
        return this.textProvidesInterface.getScholarInterTranscription(this.xmlId);
    }

    public String getSourceTranscription(boolean diff, boolean del, boolean ins,
                                         boolean subst, boolean notes) {
        return this.textProvidesInterface.getSourceInterTranscription(this.xmlId, diff, del, ins, subst, notes);
    }

    public String getExpertTranscription(boolean diff) {
        return this.textProvidesInterface.getExpertInterTranscription(this.xmlId, diff);
    }

    public String getCompleteNumber() {
        return this.textProvidesInterface.getExpertInterCompleteNumber(this.xmlId);
    }

    public int getStartPage() {
        return this.textProvidesInterface.getExpertEditionInterStartPage(this.xmlId);
    }

    public int getEndPage() {
        return this.textProvidesInterface.getExpertEditionInterEndPage(this.xmlId);
    }

    public String getNotes() {
        return this.textProvidesInterface.getExpertEditionInterNotes(this.xmlId);
    }

    public List<AnnexNoteDto> getSortedAnnexNote() {
        return this.textProvidesInterface.getScholarInterSortedAnnexNotes(this.xmlId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScholarInterDto other = (ScholarInterDto) o;
        return this.xmlId.equals(other.getXmlId());
    }

    @Override
    public int hashCode() {
        return this.xmlId.hashCode();
    }
}


