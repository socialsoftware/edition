package pt.ist.socialsoftware.edition.ldod.api.text.dto;

import pt.ist.socialsoftware.edition.ldod.api.text.TextInterface;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;

public class FragmentDto {
    private final TextInterface textInterface = new TextInterface();

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
        return this.textInterface.getScholarInterDtoByFragmentXmlIdAndUrlId(getXmlId(), urlId);
    }

    public String getTitle() {
        return this.textInterface.getFragmentTitle(getXmlId());
    }

}
