//package pt.ist.socialsoftware.edition.game.api.virtualDto;
//
//
//
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.client.WebClient;
//import pt.ist.socialsoftware.edition.game.api.textDto.FragmentDto;
//import pt.ist.socialsoftware.edition.game.api.textDto.ScholarInterDto;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//
//public class VirtualEditionInterDto {
//
//    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");
////    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://docker-virtual:8083/api");
//
//
//    private String xmlId;
//
//    // cached attributes
//    private String externalId;
//    private String title;
//    private String fragId;
//    private String urlId;
//    private String shortName;
//    private String reference;
//    private int number;
//    private ScholarInterDto lastUsed;
//
//
//    public VirtualEditionInterDto() {}
//
//    public String getXmlId() {
//        return this.xmlId;
//    }
//
//    public void setXmlId(String xmlId) {
//        this.xmlId = xmlId;
//    }
//
//    public String getExternalId() {
//        //return this.virtualProvidesInterface.getVirtualEditionInterExternalId(this.xmlId);
//        return this.externalId;
//    }
//
//    public String getTitle() {
//        //return this.virtualProvidesInterface.getVirtualEditionInterTitle(this.xmlId);
//        return this.title;
//    }
//
//    public FragInterDto.InterType getType() {
//        return FragInterDto.InterType.VIRTUAL;
//    }
//
//    public String getFragmentXmlId() {
//        //return this.virtualProvidesInterface.getFragmentXmlIdVirtualEditionInter(this.xmlId);
//        return this.fragId;
//    }
//
//    public String getUrlId() {
//        //return this.virtualProvidesInterface.getVirtualEditionInterUrlId(this.xmlId);
//        return this.urlId;
//    }
//
//    public String getShortName() {
//        //return this.virtualProvidesInterface.getVirtualEditionInterShortName(this.xmlId);
//        return this.shortName;
//    }
//
//    public ScholarInterDto getLastUsed() {
//        //return this.virtualProvidesInterface.getVirtualEditionLastUsedScholarInter(this.xmlId);
//        if (this.lastUsed == null)
//            this.lastUsed = webClientVirtual.build()
//                    .get()
//                    .uri("/virtualEditionInter/" + this.xmlId + "/lastUsed")
//                    .retrieve()
//                    .bodyToMono(ScholarInterDto.class)
//                    .block();
////            this.lastUsed = this.virtualProvidesInterface.getVirtualEditionLastUsedScholarInter(this.xmlId);
//
//        return this.lastUsed;
//    }
//
//    public String getReference() {
//        //return this.virtualProvidesInterface.getVirtualEditionInterReference(this.xmlId);
//        return this.reference;
//    }
//
//    public int getNumber() {
//        //return this.virtualProvidesInterface.getVirtualEditionInterNumber(this.xmlId);
//        return this.number;
//    }
//
//    public VirtualEditionDto getVirtualEditionDto() {
//        return webClientVirtual.build()
//                .get()
//                .uri("/virtualEditionInter/" + this.xmlId + "/virtualEdition")
//                .retrieve()
//                .bodyToMono(VirtualEditionDto.class)
//                .block();
//        //        return this.virtualProvidesInterface.getVirtualEditionOfVirtualEditionInter(this.xmlId);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        VirtualEditionInterDto other = (VirtualEditionInterDto) o;
//        return this.xmlId.equals(other.getXmlId());
//    }
//
//    @Override
//    public int hashCode() {
//        return this.xmlId.hashCode();
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
//    public void setFragId(String fragId) {
//        this.fragId = fragId;
//    }
//
//    public void setUrlId(String urlId) {
//        this.urlId = urlId;
//    }
//
//    public void setShortName(String shortName) {
//        this.shortName = shortName;
//    }
//
//    public void setReference(String reference) {
//        this.reference = reference;
//    }
//
//    public void setNumber(int number) {
//        this.number = number;
//    }
//}
