package pt.ist.socialsoftware.edition.recommendation.api.textDto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.recommendation.utils.enums.Form;
import pt.ist.socialsoftware.edition.recommendation.utils.enums.Material;
import pt.ist.socialsoftware.edition.recommendation.utils.enums.SourceType;


import java.util.AbstractMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class SourceDto {

//    private final WebClient.Builder webClient = WebClient.builder().baseUrl("http://localhost:8081/api");
    private WebClient.Builder webClient = WebClient.builder().baseUrl("http://docker-text:8081/api");

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

    @Atomic(mode = Atomic.TxMode.READ)
    public String getFormattedDimensions() {
        return  webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/formattedDimensions")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional().get();
        //   return this.textProvidesInterface.getFormattedDimensions(this.xmlId);
    }

    @Atomic(mode = Atomic.TxMode.READ)
    public List<DimensionsDto> getSortedDimensionsDto() {
       return webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/sortedDimensions")
                .retrieve()
                .bodyToFlux(DimensionsDto.class)
                .collectList()
               .block();
        //    return this.textProvidesInterface.getSortedDimensionsDto(this.xmlId);
    }


    @Atomic(mode = Atomic.TxMode.READ)
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


    @Atomic(mode = Atomic.TxMode.READ)
    public Set<ManuscriptNote> getHandNoteSet() {
        return webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/handNotes")
                .retrieve()
                .bodyToFlux(ManuscriptNote.class)
                .toStream()
                .collect(Collectors.toSet());
        //  return this.textProvidesInterface.getHandNoteSet(this.xmlId);
    }

    @JsonIgnore
    @Atomic(mode = Atomic.TxMode.READ)
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

    @Atomic(mode = Atomic.TxMode.READ)
    public Set<ManuscriptNote> getTypeNoteSet() {
        return webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/typeNotes")
                .retrieve()
                .bodyToFlux(ManuscriptNote.class)
                .toStream()
                .collect(Collectors.toSet());
        //  return this.textProvidesInterface.getTypeNoteSet(this.xmlId);
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


    @Atomic(mode = Atomic.TxMode.READ)
    public LdoDDateDto getLdoDDate() {
        return  webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/LdoDDate")
                .retrieve()
                .bodyToMono(LdoDDateDto.class)
                .blockOptional().orElse(null);
        //   return this.textProvidesInterface.getLdoDDate(this.xmlId);
    }


    public LdoDDateDto getLdoDDateDto() {
        return  webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/LdoDDateDto")
                .retrieve()
                .bodyToMono(LdoDDateDto.class)
                .blockOptional().orElse(null);
        //   return this.textProvidesInterface.getLdoDDateDto(this.xmlId);
    }


    public List<SurfaceDto> getSurfaces() {
      return   webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/surfaces")
                .retrieve()
                .bodyToFlux(SurfaceDto.class)
                .collectList()
              .block();
        //    return this.textProvidesInterface.getSurfaces(this.xmlId);
    }

    public SourceType getType() {
        //return getSourceByXmlId(this.xmlId).map(source -> source.getType()).orElse(null);
        return this.type;
    }

    @Atomic(mode = Atomic.TxMode.READ)
    public HeteronymDto getHeteronym() {
        return webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/heteronym")
                .retrieve()
                .bodyToMono(HeteronymDto.class)
                .blockOptional().get();
        //   return this.textProvidesInterface.getHeteronym(this.xmlId);
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
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<ScholarInterDto> getSourceIntersSet() {
        return webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/interSet")
                .retrieve()
                .bodyToFlux(ScholarInterDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //  return this.textProvidesInterface.getSourceIntersSet(this.xmlId);
    }

    public String getSettlement() {
        return  webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/settlement")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional().get();
    }

    public String getRepository() {
        return  webClient.build()
                .get()
                .uri("/source/" + this.xmlId + "/repository")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional().get();
    }

}