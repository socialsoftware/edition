package pt.ist.socialsoftware.edition.virtual.api.textDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ScholarInterBaseDto {

    private static final Logger logger = LoggerFactory.getLogger(ScholarInterDto.class);

    private String acronym;

    private String xmlId;

    //cached attributes
    private String externalId;
    private String title;
    private String urlId;
    private String shortName;
    private boolean isExpertInter;
    private boolean isSourceInter;
    private String reference;
    private String editionReference;
    private int number;
    private String fragXmlId;
    private String volume;
    private int startPage;
    private int endPage;
    private String completeNumber;
    private String notes;

    public ScholarInterBaseDto(ScholarInterDto scholarInterDto) {
        this.acronym = scholarInterDto.getAcronym();
        this.externalId = scholarInterDto.getExternalId();
        this.completeNumber = scholarInterDto.getCompleteNumber();
        this.title = scholarInterDto.getTitle();
        this.urlId = scholarInterDto.getUrlId();
        this.shortName = scholarInterDto.getShortName();
        this.isExpertInter = scholarInterDto.isExpertInter();
        this.isSourceInter = scholarInterDto.isSourceInter();
        this.reference = scholarInterDto.getReference();
        this.editionReference = scholarInterDto.getEditionReference();
        this.number = scholarInterDto.getNumber();
        this.fragXmlId = scholarInterDto.getFragmentXmlId();
        this.volume = scholarInterDto.getVolume();
        this.startPage = scholarInterDto.getStartPage();
        this.endPage = scholarInterDto.getEndPage();
        this.notes = scholarInterDto.getNotes();
    }

    public ScholarInterBaseDto() {
        super();
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

    public int getNumber() {
        //return this.textProvidesInterface.getScholarInterNumber(this.xmlId);
        return this.number;
    }

    @JsonProperty(value = "isSourceInter")
    public boolean isSourceInter() {
        //return !this.textProvidesInterface.isExpertInter(this.xmlId);
        return this.isSourceInter;
    }

    @JsonProperty(value = "isExpertInter")
    public boolean isExpertInter() {
        //return !this.textProvidesInterface.isExpertInter(this.xmlId);
        return this.isExpertInter;
    }

//    public SourceDto getSourceDto() {
//        return this.textProvidesInterface.getSourceOfSourceInter(this.xmlId);
//    }


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

    public String getVolume() {
        //return this.textProvidesInterface.getExpertEditionInterVolume(this.xmlId);
        return this.volume;
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


