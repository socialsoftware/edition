package pt.ist.socialsoftware.edition.ldod.text.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;

import java.util.Set;

public class FragmentDto {
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    private String xmlId;

    public FragmentDto(Fragment fragment) {
        setXmlId(fragment.getXmlId());
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
        return this.textProvidesInterface.getFragmentTitle(getXmlId());
    }

    public Set<ScholarInterDto> getScholarInterDtoSet() {
        return this.textProvidesInterface.getScholarInterDto4FragmentXmlId(getXmlId());
    }

    // Only necessary due to manual ordering of virtual edition javascript code
    public String getExternalId() {
        return this.textProvidesInterface.getFragmentExternalId(getXmlId());
    }

}
