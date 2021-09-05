//package pt.ist.socialsoftware.edition.recommendation.api.virtualDto;
//
//import org.joda.time.LocalDate;
//import org.springframework.web.reactive.function.client.WebClient;
//import pt.ist.socialsoftware.edition.recommendation.api.userDto.UserDto;
//
//import java.util.Comparator;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public class VirtualEditionDto {
//    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");
////    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://docker-virtual:8083/api");
//
//
//    private  String xmlId;
//    private  String acronym;
//
//    // cached attributes
//    private String externalId;
//    private String title;
//    private String reference;
//    private String synopsis;
//    private boolean isLdoDEdition;
//    private LocalDate date;
//    private boolean pub;
//    private boolean openVocabulary;
//    private String shortAcronym;
//    private int max;
//
//    public VirtualEditionDto() {}
//
//    public String getXmlId() {
//        return this.xmlId;
//    }
//
//    public String getAcronym() {
//        return this.acronym;
//    }
//
//    public String getExternalId() {
//        //return this.virtualProvidesInterface.getVirtualEditionExternalIdByAcronym(this.acronym);
//        return this.externalId;
//    }
//
//    public String getTitle() {
//        //return this.virtualProvidesInterface.getVirtualEditionTitleByAcronym(this.acronym);
//        return this.title;
//    }
//
//    public List<VirtualEditionInterDto> getSortedVirtualEditionInterDtoList() {
//        return webClientVirtual.build()
//                .get()
//                .uri("/virtualEdition/" + this.acronym + "/sortedVirtualEditionInterList")
//                .retrieve()
//                .bodyToFlux(VirtualEditionInterDto.class)
//                .collectList()
//                .block();
//        //        return this.virtualProvidesInterface.getSortedVirtualEditionInterDtoList(this.acronym);
//    }
//
//    public String getReference() {
//        //return this.virtualProvidesInterface.getVirtualEditionReference(this.acronym);
//        return this.reference;
//    }
//
//    public boolean getTaxonomyVocabularyStatus() {
//        //return this.virtualProvidesInterface.getVirtualEditionTaxonomyVocabularyStatus(this.acronym);
//        return this.openVocabulary;
//    }
//
//    public String getShortAcronym() {
//        return shortAcronym;
//    }
//
//    public boolean isLdoDEdition() {
//        //return this.virtualProvidesInterface.isLdoDEdition(this.acronym);
//        return this.isLdoDEdition;
//    }
//
//    public boolean getPub() {
//       //return this.virtualProvidesInterface.getVirtualEditionPub(this.acronym);
//       return this.pub;
//    }
//
//    public LocalDate getDate() {
//        //return this.virtualProvidesInterface.getVirtualEditionDate(this.acronym);
//        return this.date;
//    }
//
//    public boolean getOpenVocabulary() {
//        //return this.virtualProvidesInterface.getOpenVocabulary(this.acronym);
//        return this.openVocabulary;
//    }
//
//    public int getMaxFragNumber() {
//        return max;
//    }
//
//    public String getSynopsis() {
//        return this.synopsis;
//    }
//
//
//    public void setXmlId(String xmlId) {
//        this.xmlId = xmlId;
//    }
//
//    public void setAcronym(String acronym) {
//        this.acronym = acronym;
//    }
//
//    public void setExternalId(String externalId) {
//        this.externalId = externalId;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public void setReference(String reference) {
//        this.reference = reference;
//    }
//
//    public void setSynopsis(String synopsis) {
//        this.synopsis = synopsis;
//    }
//
//    public void setLdoDEdition(boolean ldoDEdition) {
//        isLdoDEdition = ldoDEdition;
//    }
//
//    public void setDate(LocalDate date) {
//        this.date = date;
//    }
//
//    public void setPub(boolean pub) {
//        this.pub = pub;
//    }
//
//    public void setOpenVocabulary(boolean openVocabulary) {
//        this.openVocabulary = openVocabulary;
//    }
//
//    public void setShortAcronym(String shortAcronym) {
//        this.shortAcronym = shortAcronym;
//    }
//
//    public void setMax(int max) {
//        this.max = max;
//    }
//}
