package pt.ist.socialsoftware.edition.text.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.text.domain.Fragment;
import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FragmentDto {

    private String xmlId;

    //cached attributes
    private String title;
    private String externalId;

    private Set<SourceDto> embeddedSourceDtos = new HashSet<>(  );

    private Set<ScholarInterDto> embeddedScholarInterDtos = new HashSet<>();

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

    public String getTitle() {
        //return this.textProvidesInterface.getFragmentTitle(getXmlId());
        return this.title;
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


}
