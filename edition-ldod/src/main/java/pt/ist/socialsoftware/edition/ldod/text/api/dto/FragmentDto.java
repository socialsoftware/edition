package pt.ist.socialsoftware.edition.ldod.text.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;

import java.util.List;
import java.util.Set;

public class FragmentDto {
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    private String xmlId;

    //cached attributes
    private String title;
    private String externalId;

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

    public ScholarInterDto getScholarInterByUrlId(String urlId) {
        return this.textProvidesInterface.getFragmentScholarInterByUrlId(this.xmlId, urlId);
    }

    public List<ScholarInterDto> getSortedSourceInter() {
        return this.textProvidesInterface.getFragmentSortedSourceInter(this.xmlId);
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


}
