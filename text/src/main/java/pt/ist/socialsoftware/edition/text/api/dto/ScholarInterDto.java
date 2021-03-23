package pt.ist.socialsoftware.edition.text.api.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;

import pt.ist.socialsoftware.edition.text.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.text.domain.ScholarInter;
import pt.ist.socialsoftware.edition.text.domain.SourceInter;
import pt.ist.socialsoftware.edition.text.domain.TextModule;

import java.util.List;


public class ScholarInterDto {
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    private static final Logger logger = LoggerFactory.getLogger(ScholarInterDto.class);

    private String acronym;

    private String xmlId;

    //cached attributes
    private String externalId;
    private String title;
    private String urlId;
    private String shortName;
    private boolean isExpertInter;
    private String reference;
    private String editionReference;
    private int number;
    private String fragXmlId;
    private String volume;
    private int startPage;
    private int endPage;
    private String completeNumber;
    private String notes;

    public ScholarInterDto(String xmlId) {
        setXmlId(xmlId);
        ScholarInter scholarInter = TextModule.getInstance().getScholarInterByXmlId(xmlId);
        this.externalId = scholarInter.getExternalId();
        this.title = scholarInter.getTitle();
        this.urlId = scholarInter.getUrlId();

        if ((scholarInter.isExpertInter() && scholarInter.getEdition() != null) ||
                (!scholarInter.isExpertInter() && ((SourceInter) scholarInter).getSource() != null )) {
            this.shortName = scholarInter.getShortName();
            this.reference = scholarInter.getReference();
        }

        this.isExpertInter = scholarInter.isExpertInter();

        if (scholarInter.getEdition() != null) {
            this.acronym = scholarInter.getEdition().getAcronym();
            this.editionReference = scholarInter.getEdition().getReference();
        }

        this.number = scholarInter.getNumber();
        this.fragXmlId = scholarInter.getFragment().getXmlId();
        this.volume = this.isExpertInter ? ((ExpertEditionInter) scholarInter).getVolume() : null;
        this.startPage = this.isExpertInter ? ((ExpertEditionInter) scholarInter).getStartPage() : 0;
        this.endPage = this.isExpertInter ? ((ExpertEditionInter) scholarInter).getEndPage() : 0;
        this.notes = this.isExpertInter ? ((ExpertEditionInter) scholarInter).getNotes() : null;
        this.completeNumber = this.isExpertInter ? ((ExpertEditionInter) scholarInter).getCompleteNumber() : null;
    }

    public ScholarInterDto(ScholarInter scholarInter) {

        setXmlId(scholarInter.getXmlId());
        this.externalId = scholarInter.getExternalId();
        this.title = scholarInter.getTitle();
        this.urlId = scholarInter.getUrlId();

        if ((scholarInter.isExpertInter() && scholarInter.getEdition() != null) ||
                (!scholarInter.isExpertInter() && ((SourceInter) scholarInter).getSource() != null )) {
            this.shortName = scholarInter.getShortName();
            this.reference = scholarInter.getReference();
        }

        if (scholarInter.getEdition() != null) {
            this.acronym = scholarInter.getEdition().getAcronym();
            this.editionReference = scholarInter.getEdition().getReference();
        }

        this.isExpertInter = scholarInter.isExpertInter();
        this.number = scholarInter.getNumber();
        this.fragXmlId = scholarInter.getFragment().getXmlId();
        this.volume = this.isExpertInter ? ((ExpertEditionInter) scholarInter).getVolume() : null;
        this.startPage = this.isExpertInter ? ((ExpertEditionInter) scholarInter).getStartPage() : 0;
        this.endPage = this.isExpertInter ? ((ExpertEditionInter) scholarInter).getEndPage() : 0;
        this.notes = this.isExpertInter ? ((ExpertEditionInter) scholarInter).getNotes() : null;
        this.completeNumber = this.isExpertInter ? ((ExpertEditionInter) scholarInter).getCompleteNumber() : null;

    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getXmlId() {
        return this.xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getExternalId() {
       //return this.textProvidesInterface.getScholarInterExternalId(this.xmlId);
        return this.externalId;
    }

    public LdoDDateDto getLdoDDate() {
        return this.textProvidesInterface.getScholarInterDate(this.xmlId);
    }

    public HeteronymDto getHeteronym() {
        return this.textProvidesInterface.getScholarInterHeteronym(this.xmlId);
    }

    //check
//    public FragInterDto.InterType getType() {
//        //return this.textProvidesInterface.isExpertInter(this.xmlId) ? FragInterDto.InterType.EDITORIAL : FragInterDto.InterType.AUTHORIAL;
//        return this.isExpertInter ? FragInterDto.InterType.EDITORIAL : FragInterDto.InterType.AUTHORIAL;
//    }
    //temp
    public FragScholarInterDto.InterType getType(){
        return this.isExpertInter ? FragScholarInterDto.InterType.EDITORIAL : FragScholarInterDto.InterType.AUTHORIAL;
    }

    public String getFragmentXmlId() {
        //return this.textProvidesInterface.getFragmentOfScholarInterDto(this).getXmlId();
        return this.fragXmlId;
    }

    public String getReference() {
        //return this.textProvidesInterface.getScholarInterReference(this.xmlId);
        return this.reference;
    }

    public String getEditionReference() {
        //return this.textProvidesInterface.getScholarInterEditionReference(this.xmlId);
        return this.editionReference;
    }

    public ExpertEditionDto getExpertEdition() {
        return this.textProvidesInterface.getScholarInterExpertEdition(this.xmlId);
    }

    public int getNumber() {
        //return this.textProvidesInterface.getScholarInterNumber(this.xmlId);
        return this.number;
    }

    public boolean isSourceInter() {
        //return !this.textProvidesInterface.isExpertInter(this.xmlId);
        return !this.isExpertInter;
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
        //return this.textProvidesInterface.getScholarInterTitle(this.xmlId);
        return this.title;
    }

    public String getUrlId() {
        //return this.textProvidesInterface.getScholarInterUrlId(this.xmlId);
        return this.urlId;
    }

    public String getShortName() {
        //return this.textProvidesInterface.getScholarInterShortName(this.xmlId);
        return this.shortName;
    }

    public ScholarInterDto getNextScholarInter() {
        return this.textProvidesInterface.getScholarInterNextNumberInter(this.xmlId);
    }

    public ScholarInterDto getPrevScholarInter() {
        return this.textProvidesInterface.getScholarInterPrevNumberInter(this.xmlId);
    }

    public String getVolume() {
        //return this.textProvidesInterface.getExpertEditionInterVolume(this.xmlId);
        return this.volume;
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
        //return this.textProvidesInterface.getExpertInterCompleteNumber(this.xmlId);
        return this.completeNumber;
    }

    public int getStartPage() {
        //return this.textProvidesInterface.getExpertEditionInterStartPage(this.xmlId);
        return this.startPage;
    }

    public int getEndPage() {
        //return this.textProvidesInterface.getExpertEditionInterEndPage(this.xmlId);
        return this.endPage;
    }

    public String getNotes() {
        //return this.textProvidesInterface.getExpertEditionInterNotes(this.xmlId);
        return this.notes;
    }

    public List<AnnexNoteDto> getSortedAnnexNote() {
        return this.textProvidesInterface.getScholarInterSortedAnnexNotes(this.xmlId);
    }

    public ScholarInterDto getNextNumberInter() {
        return this.textProvidesInterface.getScholarInterNextNumberInter(this.xmlId);
    }

    public ScholarInterDto getPrevNumberInter() {
        return this.textProvidesInterface.getScholarInterPrevNumberInter(this.xmlId);
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


