package pt.ist.socialsoftware.edition.notification.dtos.text;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.notification.enums.Form;
import pt.ist.socialsoftware.edition.notification.enums.Material;
import pt.ist.socialsoftware.edition.notification.enums.SourceType;

import java.util.AbstractMap;
import java.util.List;
import java.util.Set;

import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.TEXT_SERVICE_URL;


public class SourceDto {

    private final WebClient.Builder webClient = WebClient.builder().baseUrl(TEXT_SERVICE_URL);

    private String xmlId;

    //cached attributes
    private String name;
    private SourceType type;
    private String title;
    private String idno;
    private String altIdentifier;
    private String journal;
    private String issue;
    private String pubPlace;
    private String notes;
    private int startPage;
    private int endPage;
    private Material material;
    private Form form;
    private int columns;
    private boolean ldoDLabel;
    private boolean hasHandNoteSet;
    private boolean hasTypeNoteSet;

    private LdoDDateDto ldoDDate;
    private Set<ManuscriptNote> handNoteSet;
    private Set<ManuscriptNote> typeNoteSet;
    private List<SurfaceDto> surfaces;
    private List<DimensionsDto> sortedDimensionsDto;
    private Set<ScholarInterDto> sourceInters;




    public SourceDto() {
        super();
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

    public Material getMaterial() {
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

    public SourceType getType() {
        //return getSourceByXmlId(this.xmlId).map(source -> source.getType()).orElse(null);
        return this.type;
    }


    public Form getForm() {
       /* return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getForm)
                .orElse(null);*/
       return this.form;
    }

    @JsonIgnore
    public String getRepository() {
        return  webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/repository")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional().get();
    }

    @JsonIgnore
    public HeteronymDto getHeteronym() {
        return webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/heteronym")
                .retrieve()
                .bodyToMono(HeteronymDto.class)
                .blockOptional().get();
        //   return this.textProvidesInterface.getHeteronym(this.xmlId);
    }

    @JsonIgnore
    public String getFormattedDimensions() {
        return  webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/formattedDimensions")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional().get();
        //   return this.textProvidesInterface.getFormattedDimensions(this.xmlId);
    }


    @JsonIgnore
    public List<AbstractMap.SimpleEntry<String, String>> getFormattedHandNote() {
        return webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/formattedHandNotes")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<AbstractMap.SimpleEntry<String, String>>() {})
                .collectSortedList()
                .block();
        //    return this.textProvidesInterface.getFormattedHandNote(this.xmlId);
    }


    @JsonIgnore
    public List<AbstractMap.SimpleEntry<String, String>> getFormattedTypeNote() {
        return webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/formattedTypeNotes")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<AbstractMap.SimpleEntry<String, String>>() {})
                .collectSortedList()
                .block();
        //  return this.textProvidesInterface.getFormattedTypeNote(this.xmlId);
    }

    @JsonIgnore
    public String getSettlement() {
        return  webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/settlement")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional().get();
    }


    public LdoDDateDto getLdoDDate() {
        return ldoDDate;
    }

    public Set<ManuscriptNote> getHandNoteSet() {
        return handNoteSet;
    }

    public Set<ManuscriptNote> getTypeNoteSet() {
        return typeNoteSet;
    }

    public List<SurfaceDto> getSurfaces() {
        return surfaces;
    }

    public List<DimensionsDto> getSortedDimensionsDto() {
        return sortedDimensionsDto;
    }

    public Set<ScholarInterDto> getSourceIntersSet() {
        return sourceInters;
    }

    public void setLdoDDate(LdoDDateDto ldoDDate) {
        this.ldoDDate = ldoDDate;
    }

    public void setSurfaces(List<SurfaceDto> surfaces) {
        this.surfaces = surfaces;
    }

    public void setSortedDimensionsDto(List<DimensionsDto> sortedDimensionsDto) {
        this.sortedDimensionsDto = sortedDimensionsDto;
    }

    public void setSourceIntersSet(Set<ScholarInterDto> sourceInters) {
        this.sourceInters = sourceInters;
    }

    public void setHandNoteSet(Set<ManuscriptNote> handNoteSet) {
        this.handNoteSet = handNoteSet;
    }

    public void setTypeNoteSet(Set<ManuscriptNote> typeNoteSet) {
        this.typeNoteSet = typeNoteSet;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(SourceType type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public void setAltIdentifier(String altIdentifier) {
        this.altIdentifier = altIdentifier;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public void setPubPlace(String pubPlace) {
        this.pubPlace = pubPlace;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setLdoDLabel(boolean ldoDLabel) {
        this.ldoDLabel = ldoDLabel;
    }

    public void setHasHandNoteSet(boolean hasHandNoteSet) {
        this.hasHandNoteSet = hasHandNoteSet;
    }

    public void setHasTypeNoteSet(boolean hasTypeNoteSet) {
        this.hasTypeNoteSet = hasTypeNoteSet;
    }

}