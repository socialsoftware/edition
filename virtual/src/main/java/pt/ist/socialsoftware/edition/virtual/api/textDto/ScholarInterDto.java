package pt.ist.socialsoftware.edition.virtual.api.textDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.virtual.api.VirtualRequiresInterface;


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
    private LdoDDateDto ldoDDateDto;
    private HeteronymDto heteronymDto;

    public ScholarInterDto(String xmlId) {
        setXmlId(xmlId);
        ScholarInterDto scholarInter = VirtualRequiresInterface.getInstance().getScholarInterByXmlId(xmlId);
        this.externalId = scholarInter.getExternalId();
        this.title = scholarInter.getTitle();
        this.urlId = scholarInter.getUrlId();


        this.shortName = scholarInter.getShortName();
        this.reference = scholarInter.getReference();


        this.isExpertInter = scholarInter.isExpertInter();
        this.isSourceInter = !scholarInter.isExpertInter();

        this.acronym = scholarInter.getAcronym();
        this.editionReference = scholarInter.getEditionReference();


        this.number = scholarInter.getNumber();
        this.fragXmlId = scholarInter.getFragmentXmlId();
        this.volume = scholarInter.getVolume();
        this.startPage = scholarInter.getStartPage();
        this.endPage = scholarInter.getEndPage();
        this.notes = scholarInter.getNotes();
        this.completeNumber = scholarInter.getCompleteNumber();
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
        return this.ldoDDateDto;
    }

    public HeteronymDto getHeteronym() {
        return webClient.build()
                .get()
                .uri( "/heteronym/scholarInter/" + this.xmlId)
                .retrieve()
                .bodyToMono(HeteronymDto.class)
                .blockOptional().orElse(null);
        //    return this.textProvidesInterface.getScholarInterHeteronym(this.xmlId);
    }

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


    @JsonIgnore
    @Atomic(mode = Atomic.TxMode.READ)
    public String getExpertEditionAcronym() {
        return  webClient.build()
                .get()
                .uri("/expertEdition/" + this.xmlId + "/acronym")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional().orElse("");
        //  return this.textProvidesInterface.getExpertEditionAcronym(this.xmlId);
    }

    @JsonIgnore
    @Atomic(mode = Atomic.TxMode.READ)
    public int getNumberOfTimesCited() {
        return  webClient.build()
                .get()
                .uri("/scholarEdition/" + this.xmlId + "/citednumber")
                .retrieve()
                .bodyToMono(Integer.class)
                .blockOptional().orElse(0);
        //return this.textProvidesInterface.getNumberOfTimesCited(this.xmlId);
    }

    @JsonIgnore
    @Atomic(mode = Atomic.TxMode.READ)
    public int getNumberOfTimesCitedIncludingRetweets() {
        return  webClient.build()
                .get()
                .uri("/scholarEdition/" + this.xmlId + "/citednumberPlusretweets")
                .retrieve()
                .bodyToMono(Integer.class)
                .blockOptional().orElse(0);
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

    @JsonIgnore
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

    public void setisExpertInter(boolean expertInter) {
        isExpertInter = expertInter;
    }

    public void setisSourceInter(boolean sourceInter) {
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

    public void setLdoDDateDto(LdoDDateDto ldoDDateDto) {
        this.ldoDDateDto = ldoDDateDto;
    }

    public void setHeteronymDto(HeteronymDto heteronymDto) {
        this.heteronymDto = heteronymDto;
    }
}


