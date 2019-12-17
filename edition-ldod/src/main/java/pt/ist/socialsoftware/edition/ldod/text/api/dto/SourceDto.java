package pt.ist.socialsoftware.edition.ldod.text.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;

import java.util.*;
import java.util.stream.Collectors;

public class SourceDto {
    private String xmlId;

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

    public String getFormattedDimensions() {
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getDimensionsSet)
                .orElse(new HashSet<>())
                .stream().map(dimensions -> dimensions.getHeight() + "x" + dimensions.getWidth())
                .collect(Collectors.joining(";"));
    }

    public List<DimensionsDto> getSortedDimensionsDto() {
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getSortedDimensions)
                .orElse(new ArrayList<>())
                .stream().map(DimensionsDto::new)
                .collect(Collectors.toList());
    }

    public List<AbstractMap.SimpleEntry<String, String>> getFormattedHandNote() {
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getHandNoteSet)
                .orElse(new HashSet<>())
                .stream().map(handNote ->
                        new AbstractMap.SimpleEntry<>((handNote.getMedium() != null ? handNote.getMedium().getDesc() : ""), handNote.getNote()))
                .collect(Collectors.toList());
    }

    public Set<ManuscriptNote> getHandNoteSet() {
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getHandNoteSet)
                .orElse(new HashSet<>())
                .stream().map(ManuscriptNote::new)
                .collect(Collectors.toSet());
    }

    public List<AbstractMap.SimpleEntry<String, String>> getFormattedTypeNote() {
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getTypeNoteSet)
                .orElse(new HashSet<>())
                .stream().map(typeNote ->
                        new AbstractMap.SimpleEntry<>((typeNote.getMedium() != null ? typeNote.getMedium().getDesc() : ""), typeNote.getNote()))
                .collect(Collectors.toList());
    }

    public Set<ManuscriptNote> getTypeNoteSet() {
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getHandNoteSet)
                .orElse(new HashSet<>())
                .stream().map(ManuscriptNote::new)
                .collect(Collectors.toSet());
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

    public LdoDDateDto getLdoDDate() {
        return getSourceByXmlId(this.xmlId)
                .map(source -> source.getLdoDDate() != null ? new LdoDDateDto(source.getLdoDDate()) : null)
                .orElse(null);
    }

    public LdoDDateDto getLdoDDateDto() {
        return getSourceByXmlId(this.xmlId)
                .map(Source_Base::getLdoDDate)
                .map(LdoDDateDto::new)
                .orElse(null);
    }

    public List<SurfaceDto> getSurfaces() {
        List<Surface> surfaces = getSourceByXmlId(this.xmlId)
                .map(Source_Base::getFacsimile)
                .map(Facsimile::getSurfaces).orElse(new ArrayList<>());
        return surfaces.stream().map(SurfaceDto::new).collect(Collectors.toList());
    }

    public Source.SourceType getType() {
        //return getSourceByXmlId(this.xmlId).map(source -> source.getType()).orElse(null);
        return this.type;
    }

    public HeteronymDto getHeteronym() {
        return getSourceByXmlId(this.xmlId)
                .map(Source::getHeteronym)
                .map(HeteronymDto::new)
                .orElse(null);
    }

    public ManuscriptSource.Form getForm() {
       /* return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getForm)
                .orElse(null);*/
       return this.form;
    }

    public Set<ScholarInterDto> getSourceIntersSet() {
        Set<SourceInter> sourceInters = getSourceByXmlId(this.xmlId).map(Source::getSourceIntersSet).orElse(new HashSet<>());
        return sourceInters.stream().map(ScholarInterDto::new).collect(Collectors.toSet());
    }

    private Optional<Source> getSourceByXmlId(String xmlId) {
        return TextModule.getInstance().getFragmentsSet().stream()
                .flatMap(fragment -> fragment.getSourcesSet().stream())
                .filter(source -> source.getXmlId().equals(xmlId))
                .findAny();
    }
}