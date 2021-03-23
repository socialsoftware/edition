package pt.ist.socialsoftware.edition.text.api.dto;

import pt.ist.socialsoftware.edition.text.domain.Fragment;
import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FragmentDto {
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    private String xmlId;

    //cached attributes
    private String title;
    private String externalId;

    private Set<SourceDto> embeddedSourceDtos;

    private Set<ScholarInterDto> embeddedScholarInterDtos;

    public FragmentDto(Fragment fragment) {
        setXmlId(fragment.getXmlId());
        this.title = fragment.getTitle();
        this.externalId = fragment.getExternalId();
    }

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

    public ScholarInterDto getScholarInterDtoByUrlId(String urlId) {
        return this.textProvidesInterface.getScholarInterDtoByFragmentXmlIdAndUrlId(getXmlId(), urlId);
    }

    public String getTitle() {
        //return this.textProvidesInterface.getFragmentTitle(getXmlId());
        return this.title;
    }

    public Set<ScholarInterDto> getScholarInterDtoSet() {
        return this.textProvidesInterface.getScholarInterDto4FragmentXmlId(getXmlId());
    }

    public Set<ScholarInterDto> getScholarInterDtoSetForExpertEdtion(String acronym) {
        return this.textProvidesInterface.getFragmentScholarInterDtoSetForExpertEdtion(getXmlId(), acronym);
    }

    public Set<ScholarInterDto> getEmbeddedScholarInterDtoSetForExpertEdition(String acronym) {
        return this.embeddedScholarInterDtos.stream()
                .filter(scholarInterDto -> scholarInterDto.getAcronym().equals(acronym)).collect(Collectors.toSet());
    }

    public List<ScholarInterDto> getSortedSourceInter() {
        return this.textProvidesInterface.getFragmentSortedSourceInter(this.xmlId);
    }

    public List<ScholarInterDto> getEmbeddedSourceInter() {
        return this.embeddedScholarInterDtos.stream()
                .filter(scholarInterDto -> scholarInterDto.isSourceInter()).collect(Collectors.toList());
    }

    public ScholarInterDto getScholarInterByUrlId(String urlId) {
        return this.textProvidesInterface.getFragmentScholarInterByUrlId(this.xmlId, urlId);
    }

    public ScholarInterDto getScholarInterByXmlId(String xmlId) {
        return this.textProvidesInterface.getScholarInter(xmlId);
    }

    // Only necessary due to manual ordering of virtual edition javascript code
    public String getExternalId() {
        //return this.textProvidesInterface.getFragmentExternalId(getXmlId());
        return this.externalId;
    }

    public Set<SourceDto> getSourcesSet() {
        return this.textProvidesInterface.getFragmentSourceSet(this.xmlId);
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
        return this.textProvidesInterface.getFragmentCitationSet(this.xmlId);
    }

}
