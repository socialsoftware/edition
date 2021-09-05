//package pt.ist.socialsoftware.edition.recommendation.api.textDto;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public class FragmentDto {
//
//      private final WebClient.Builder webClient = WebClient.builder().baseUrl("http://localhost:8081/api");
////    private WebClient.Builder webClient = WebClient.builder().baseUrl("http://docker-text:8081/api");
//
//
//    private String xmlId;
//
//    //cached attributes
//    private String title;
//    private String externalId;
//
//    private Set<SourceDto> embeddedSourceDtos = new HashSet<>(  );
//
//    private Set<ScholarInterDto> embeddedScholarInterDtos = new HashSet<>();
//
//
//    public FragmentDto() { super(); }
//
//    public String getXmlId() {
//        return this.xmlId;
//    }
//
//    public void setXmlId(String xmlId) {
//        this.xmlId = xmlId;
//    }
//
//    @JsonIgnore
//    public Set<SourceDto> getEmbeddedSourceDtos() {
//        return embeddedSourceDtos;
//    }
//
//    public void setEmbeddedSourceDtos(Set<SourceDto> embeddedSourceDtos) {
//        this.embeddedSourceDtos = embeddedSourceDtos;
//    }
//
//    @JsonIgnore
//    public Set<ScholarInterDto> getEmbeddedScholarInterDtos() {
//        return embeddedScholarInterDtos;
//    }
//
//    public void setEmbeddedScholarInterDtos(Set<ScholarInterDto> embeddedScholarInterDtos) {
//        this.embeddedScholarInterDtos = embeddedScholarInterDtos;
//    }
//
//    public String getTitle() {
//        //return this.textProvidesInterface.getFragmentTitle(getXmlId());
//        return this.title;
//    }
//
//    @JsonIgnore
//    public Set<ScholarInterDto> getScholarInterDtoSet() {
//        return webClient.build().get()
//                .uri("/fragment/" + getXmlId() + "/scholarInters")
//                .retrieve()
//                .bodyToFlux(ScholarInterDto.class)
//                .toStream()
//                .collect(Collectors.toSet());
//        //    return this.textProvidesInterface.getScholarInterDto4FragmentXmlId(getXmlId());
//    }
//
//
//    @JsonIgnore
//    public Set<ScholarInterDto> getEmbeddedScholarInterDtoSetForExpertEdition(String acronym) {
//        return this.embeddedScholarInterDtos.stream()
//                .filter(scholarInterDto -> scholarInterDto.getAcronym().equals(acronym)).collect(Collectors.toSet());
//    }
//
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    public List<ScholarInterDto> getEmbeddedSourceInter() {
//        return this.embeddedScholarInterDtos.stream()
//                .filter(scholarInterDto -> scholarInterDto.isSourceInter()).collect(Collectors.toList());
//    }
//
//    // Only necessary due to manual ordering of virtual edition javascript code
//    public String getExternalId() {
//        //return this.textProvidesInterface.getFragmentExternalId(getXmlId());
//        return this.externalId;
//    }
//
//    @JsonIgnore
//    public Set<SourceDto> getSourcesSet() {
//        return webClient.build()
//                .get()
//                .uri("/fragment/" + this.xmlId + "/sources")
//                .retrieve()
//                .bodyToFlux(SourceDto.class)
//                .toStream()
//                .collect(Collectors.toSet());
//        //   return this.textProvidesInterface.getFragmentSourceSet(this.xmlId);
//    }
//
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        FragmentDto other = (FragmentDto) o;
//        return this.xmlId.equals(other.getXmlId());
//    }
//
//    @Override
//    public int hashCode() {
//        return this.xmlId.hashCode();
//    }
//
//    @Override
//    public String toString() {
//        return xmlId;
//    }
//
//}
