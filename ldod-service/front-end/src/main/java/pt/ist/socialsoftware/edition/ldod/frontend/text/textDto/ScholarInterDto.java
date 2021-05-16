package pt.ist.socialsoftware.edition.ldod.frontend.text.textDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.frontend.text.baseDto.ScholarInterBaseDto;

import java.util.List;


public class ScholarInterDto {

    private final WebClient.Builder webClient = WebClient.builder().baseUrl("http://localhost:8081/api");
//    private WebClient.Builder webClient = WebClient.builder().baseUrl("http://docker-text:8081/api");

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

    public ScholarInterDto(ScholarInterBaseDto baseDto) {
        this.acronym = baseDto.getAcronym();
        this.completeNumber = baseDto.getCompleteNumber();
        this.editionReference = baseDto.getEditionReference();
        this.endPage = baseDto.getEndPage();
        this.isSourceInter = baseDto.isSourceInter();
        this.isExpertInter = baseDto.isExpertInter();
        this.externalId = baseDto.getExternalId();
        this.xmlId = baseDto.getXmlId();
        this.fragXmlId = baseDto.getFragmentXmlId();
        this.urlId = baseDto.getUrlId();
        this.notes = baseDto.getNotes();
        this.number = baseDto.getNumber();
        this.shortName = baseDto.getShortName();
        this.title = baseDto.getTitle();
        this.volume = baseDto.getVolume();
        this.startPage = baseDto.getStartPage();
        this.reference = baseDto.getReference();
    }

    public ScholarInterDto() {
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


    public LdoDDateDto getLdoDDate() {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + this.xmlId + "/date")
                .retrieve()
                .bodyToMono(LdoDDateDto.class)
                .block();
        //    return this.textProvidesInterface.getScholarInterDate(this.xmlId);
    }


    public HeteronymDto getHeteronym() {
        return webClient.build()
                .get()
                .uri( "/heteronym/scholarInter/" + this.xmlId)
                .retrieve()
                .bodyToMono(HeteronymDto.class)
                .block();
        //    return this.textProvidesInterface.getScholarInterHeteronym(this.xmlId);
    }

    //check
//    public FragInterDto.InterType getType() {
//        //return this.textProvidesInterface.isExpertInter(this.xmlId) ? FragInterDto.InterType.EDITORIAL : FragInterDto.InterType.AUTHORIAL;
//        return this.isExpertInter ? FragInterDto.InterType.EDITORIAL : FragInterDto.InterType.AUTHORIAL;
//    }
    //temp
    public FragScholarInterDto.InterType getType(){
        return isExpertInter() ? FragScholarInterDto.InterType.EDITORIAL : FragScholarInterDto.InterType.AUTHORIAL;
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

    @Atomic(mode = Atomic.TxMode.READ)
    public ExpertEditionDto getExpertEdition() {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + this.xmlId + "/expertEdition")
                .retrieve()
                .bodyToMono(ExpertEditionDto.class)
                .block();
//        return this.textProvidesInterface.getScholarInterExpertEdition(this.xmlId);
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


    public SourceDto getSourceDto() {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + this.xmlId + "/source")
                .retrieve()
                .bodyToMono(SourceDto.class)
                .blockOptional().get();
    }

//    public SourceDto getSourceDto() {
//        return new SourceDto(new TextProvidesInterface().getSourceOfSourceInter(this.xmlId));
//    }

    @Atomic(mode = Atomic.TxMode.READ)
    public String getExpertEditionAcronym() {
        return  webClient.build()
                .get()
                .uri("/expertEdition/" + this.xmlId + "/acronym")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional().get();
        //  return this.textProvidesInterface.getExpertEditionAcronym(this.xmlId);
    }

    @Atomic(mode = Atomic.TxMode.READ)
    public int getNumberOfTimesCited() {
        return  webClient.build()
                .get()
                .uri("/scholarEdition/" + this.xmlId + "/citednumber")
                .retrieve()
                .bodyToMono(Integer.class)
                .blockOptional().get();
        //return this.textProvidesInterface.getNumberOfTimesCited(this.xmlId);
    }

    @Atomic(mode = Atomic.TxMode.READ)
    public int getNumberOfTimesCitedIncludingRetweets() {
        return  webClient.build()
                .get()
                .uri("/scholarEdition/" + this.xmlId + "/citednumberPlusretweets")
                .retrieve()
                .bodyToMono(Integer.class)
                .blockOptional().get();
        //    return this.textProvidesInterface.getNumberOfTimesCitedIncludingRetweets(this.xmlId);
    }


    @JsonIgnore
    public FragmentDto getFragmentDto() {
        return webClient.build()
                .get()
                .uri("/scholarInter/fragment/" + this.xmlId)
                .retrieve()
                .bodyToMono(FragmentDto.class)
                .blockOptional().orElse(null);
        //   return this.textProvidesInterface.getFragmentOfScholarInterDto(this);
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

    @JsonIgnore
    public ScholarInterDto getNextScholarInter() {
        return  webClient.build()
                .get()
                .uri("/scholarInter/" + this.xmlId + "/next")
                .retrieve()
                .bodyToMono(ScholarInterDto.class)
                .blockOptional().get();
        //    return this.textProvidesInterface.getScholarInterNextNumberInter(this.xmlId);
    }


    @JsonIgnore
    public ScholarInterDto getPrevScholarInter() {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + this.xmlId + "/prev")
                .retrieve()
                .bodyToMono(ScholarInterDto.class)
                .blockOptional().get();
        //    return this.textProvidesInterface.getScholarInterPrevNumberInter(this.xmlId);
    }

    public String getVolume() {
        //return this.textProvidesInterface.getExpertEditionInterVolume(this.xmlId);
        return this.volume;
    }

    @Atomic(mode = Atomic.TxMode.READ)
    public String getTranscription() {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + this.xmlId + "/transcription")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional().get();
        //    return this.textProvidesInterface.getScholarInterTranscription(this.xmlId);
    }

    @Atomic(mode = Atomic.TxMode.READ)
    public String getSourceTranscription(boolean diff, boolean del, boolean ins,
                                         boolean subst, boolean notes) {
    return  webClient.build()
            .get()
            .uri(uriBuilder -> uriBuilder
                    .path("/sourceInter/" + this.xmlId + "/transcription")
                    .queryParam("diff", diff)
                    .queryParam("del", del)
                    .queryParam("ins", ins)
                    .queryParam("subst", subst)
                    .queryParam("notes", notes)
                    .build()
            )
            .retrieve()
            .bodyToMono(String.class)
            .blockOptional().get();
        //    return this.textProvidesInterface.getSourceInterTranscription(this.xmlId, diff, del, ins, subst, notes);
    }

    @Atomic(mode = Atomic.TxMode.READ)
    public String getExpertTranscription(boolean diff) {
        return  webClient.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/expertInter/" + this.xmlId + "/transcription")
                        .queryParam("diff", diff)
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional().get();
        //   return this.textProvidesInterface.getExpertInterTranscription(this.xmlId, diff);
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

    @Atomic(mode = Atomic.TxMode.READ)
    public List<AnnexNoteDto> getSortedAnnexNote() {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + this.xmlId + "/sortedAnnexNotes")
                .retrieve()
                .bodyToFlux(AnnexNoteDto.class)
                .collectList()
                .block();
        //   return this.textProvidesInterface.getScholarInterSortedAnnexNotes(this.xmlId);
    }


    @JsonIgnore
    public ScholarInterDto getNextNumberInter() {
       return webClient.build()
                .get()
                .uri("/scholarInter/" + this.xmlId + "/next")
                .retrieve()
                .bodyToMono(ScholarInterDto.class)
                .blockOptional().get();
        //  return this.textProvidesInterface.getScholarInterNextNumberInter(this.xmlId);
    }

    @JsonIgnore
    public ScholarInterDto getPrevNumberInter() {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + this.xmlId + "/prev")
                .retrieve()
                .bodyToMono(ScholarInterDto.class)
                .blockOptional().get();
    }

    public void remove() {
        webClient.build()
                .post()
                .uri("/scholarInter/" + this.xmlId + "/remove")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
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

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setExpertInter(boolean expertInter) {
        isExpertInter = expertInter;
    }

    public void setSourceInter(boolean sourceInter) {
        isSourceInter = sourceInter;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setEditionReference(String editionReference) {
        this.editionReference = editionReference;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setFragmentXmlId(String fragXmlId) {
        this.fragXmlId = fragXmlId;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public void setCompleteNumber(String completeNumber) {
        this.completeNumber = completeNumber;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}


