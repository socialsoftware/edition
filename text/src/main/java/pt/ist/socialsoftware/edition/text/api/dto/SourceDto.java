package pt.ist.socialsoftware.edition.text.api.dto;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.text.domain.*;

import java.util.*;
import java.util.stream.Collectors;

public class SourceDto {

    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    private String xmlId;
    private Set<ScholarInterDto> sourceInters = new HashSet<>();

    //cached attributes
    private String name;
    private Source.SourceType type;
    private String title;
    private String idno;
    private String altIdentifier;
    private String journal;
    private String issue;
    private String pubPlace;
    private String notes;
    private int startPage;
    private int endPage;
    private ManuscriptSource.Material material;
    private ManuscriptSource.Form form;
    private int columns;
    private boolean ldoDLabel;
    private boolean hasHandNoteSet;
    private boolean hasTypeNoteSet;

    public SourceDto(Source source) {
        setXmlId(source.getXmlId());
        this.name = source.getName();
        this.idno = source.getIdno();
        this.altIdentifier = source.getAltIdentifier();
        this.type = source.getType();

        if(source.getType() == Source.SourceType.MANUSCRIPT){
            ManuscriptSource manuscriptSource = (ManuscriptSource) source;
            this.material = manuscriptSource.getMaterial();
            this.columns = manuscriptSource.getColumns();
            this.ldoDLabel = manuscriptSource.getHasLdoDLabel();
            this.hasHandNoteSet = !manuscriptSource.getHandNoteSet().isEmpty();
            this.hasTypeNoteSet = !manuscriptSource.getTypeNoteSet().isEmpty();
            this.notes = manuscriptSource.getNotes();
            this.form = manuscriptSource.getForm();
        }
        else{
            PrintedSource printedSource = (PrintedSource) source;
            this.title = printedSource.getTitle();
            this.journal = printedSource.getJournal();
            this.issue = printedSource.getIssue();
            this.startPage = printedSource.getStartPage();
            this.endPage = printedSource.getEndPage();
            this.pubPlace = printedSource.getPubPlace();
        }
    }

    public String getXmlId() {
        return this.xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getName(){
        return this.name;
    }

    public String getTitle() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(printedSource -> printedSource.getTitle())
                .orElseThrow(LdoDException::new);*/
        return this.title;
    }

    public String getIdno() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getIdno)
                .orElse(null);*/
        return this.idno;
    }

    public ManuscriptSource.Material getMaterial() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getMaterial)
                .orElseThrow(LdoDException::new);*/
        return this.material;
    }


    public int getColumns() {
       /* return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getColumns)
                .orElseThrow(LdoDException::new);*/
       return this.columns;
    }

    public String getJournal() {
       /* return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(PrintedSource::getJournal)
                .orElse(null);*/
       return this.journal;
    }

    public String getIssue() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(PrintedSource::getIssue)
                .orElse(null);*/
        return this.issue;
    }

    public String getAltIdentifier() {
       /* return getSourceByXmlId(this.xmlId)
                .map(Source::getAltIdentifier)
                .orElse(null);*/
       return this.altIdentifier;
    }

    public String getNotes() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getNotes)
                .orElse(null);*/
        return this.notes;
    }

    public int getStartPage() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(PrintedSource::getStartPage)
                .orElseThrow(LdoDException::new);*/
        return this.startPage;
    }

    public int getEndPage() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(PrintedSource::getEndPage)
                .orElseThrow(LdoDException::new);*/
        return this.endPage;
    }

    public String getPubPlace() {
       /* return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(PrintedSource::getPubPlace)
                .orElse(null);*/
       return this.pubPlace;
    }

    public boolean getHasLdoDLabel() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getHasLdoDLabel)
                .orElseThrow(LdoDException::new);*/
        return this.ldoDLabel;
    }

    public boolean hasHandNoteSet() {
       /* return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(manuscriptSource -> !manuscriptSource.getHandNoteSet().isEmpty())
                .orElseThrow(LdoDException::new);*/
       return this.hasHandNoteSet;
    }

    public boolean hasTypeNoteSet() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(manuscriptSource -> !manuscriptSource.getTypeNoteSet().isEmpty())
                .orElseThrow(LdoDException::new);*/
        return this.hasTypeNoteSet;
    }

    public Source.SourceType getType() {
        //return getSourceByXmlId(this.xmlId).map(source -> source.getType()).orElse(null);
        return this.type;
    }



    public ManuscriptSource.Form getForm() {
       /* return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getForm)
                .orElse(null);*/
       return this.form;
    }


    @Atomic(mode = Atomic.TxMode.READ)
    public List<DimensionsDto> getSortedDimensionsDto() {
            return this.textProvidesInterface.getSortedDimensionsDto(this.xmlId);
    }

    @Atomic(mode = Atomic.TxMode.READ)
    public Set<ManuscriptNote> getHandNoteSet() {
        return this.textProvidesInterface.getHandNoteSet(this.xmlId);
    }

    @Atomic(mode = Atomic.TxMode.READ)
    public Set<ManuscriptNote> getTypeNoteSet() {
          return this.textProvidesInterface.getTypeNoteSet(this.xmlId);
    }

    @Atomic(mode = Atomic.TxMode.READ)
    public LdoDDateDto getLdoDDate() {
           return this.textProvidesInterface.getLdoDDate(this.xmlId);
    }

    public List<SurfaceDto> getSurfaces() {
        return this.textProvidesInterface.getSurfaces(this.xmlId);
    }

    @Atomic(mode = Atomic.TxMode.READ)
    public Set<ScholarInterDto> getSourceIntersSet() {
        return sourceInters;
        //        return this.textProvidesInterface.getSourceIntersSet(this.xmlId);
    }

    public void setSourceInters(Set<ScholarInterDto> sourceInters) {
        this.sourceInters = sourceInters;
    }
}