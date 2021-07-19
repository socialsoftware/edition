package pt.ist.socialsoftware.edition.virtual.api.textDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FragmentDto {

      private final WebClient.Builder webClient = WebClient.builder().baseUrl("http://localhost:8081/api");
//    private WebClient.Builder webClient = WebClient.builder().baseUrl("http://docker-text:8081/api");


    private String xmlId;

    //cached attributes
    private String title;
    private String externalId;

    private Set<SourceDto> embeddedSourceDtos = new HashSet<>(  );

    private Set<ScholarInterDto> embeddedScholarInterDtos = new HashSet<>();

    public FragmentDto() { super(); }


    public String getXmlId() {
        return this.xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public Set<SourceDto> getEmbeddedSourceDtos() {
        return embeddedSourceDtos;
    }

    public void setEmbeddedSourceDtos(Set<SourceDto> embeddedSourceDtos) {
        this.embeddedSourceDtos = embeddedSourceDtos;
    }

    public Set<ScholarInterDto> getEmbeddedScholarInterDtos() {
        return embeddedScholarInterDtos;
    }

    public void setEmbeddedScholarInterDtos(Set<ScholarInterDto> embeddedScholarInterDtos) {
        this.embeddedScholarInterDtos = embeddedScholarInterDtos;
    }

    public String getTitle() {
        //return this.textProvidesInterface.getFragmentTitle(getXmlId());
        return this.title;
    }


    public Set<ScholarInterDto> getScholarInterDtoSet() {
        return webClient.build().get()
                .uri("/fragment/" + getXmlId() + "/scholarInters")
                .retrieve()
                .bodyToFlux(ScholarInterDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //    return this.textProvidesInterface.getScholarInterDto4FragmentXmlId(getXmlId());
    }


    public Set<ScholarInterDto> getEmbeddedScholarInterDtoSetForExpertEdition(String acronym) {
        return this.embeddedScholarInterDtos.stream()
                .filter(scholarInterDto -> scholarInterDto.getAcronym().equals(acronym)).collect(Collectors.toSet());
    }


    public List<ScholarInterDto> getSortedSourceInter() {
        return webClient.build()
                .get()
                .uri("/fragment/" + getXmlId() + "/sortedSourceInter")
                .retrieve()
                .bodyToFlux(ScholarInterDto.class)
                .collectSortedList()
                .block();
        //   return this.textProvidesInterface.getFragmentSortedSourceInter(this.xmlId);
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<ScholarInterDto> getEmbeddedSourceInter() {
        return this.embeddedScholarInterDtos.stream()
                .filter(scholarInterDto -> scholarInterDto.isSourceInter()).collect(Collectors.toList());
    }

    public ScholarInterDto getScholarInterByXmlId(String xmlId) {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + xmlId)
                .retrieve()
                .bodyToMono(ScholarInterDto.class)
                .block();
        //    return this.textProvidesInterface.getScholarInter(xmlId);
    }

    // Only necessary due to manual ordering of virtual edition javascript code
    public String getExternalId() {
        //return this.textProvidesInterface.getFragmentExternalId(getXmlId());
        return this.externalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FragmentDto other = (FragmentDto) o;
        return this.xmlId.equals(other.getXmlId());
    }

    @Override
    public int hashCode() {
        return this.xmlId.hashCode();
    }


    public Set<CitationDto> getCitationSet() {
        return webClient.build()
                .get()
                .uri( "/fragment/citations/" + this.xmlId)
                .retrieve()
                .bodyToFlux(CitationDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //    return this.textProvidesInterface.getFragmentCitationSet(this.xmlId);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }


}
