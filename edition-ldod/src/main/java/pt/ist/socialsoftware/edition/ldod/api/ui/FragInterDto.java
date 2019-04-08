package pt.ist.socialsoftware.edition.ldod.api.ui;

import pt.ist.socialsoftware.edition.ldod.domain.ScholarInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public class FragInterDto {

    private String fragmentXmlId;
    private String urlId;
    private String shortName;
    private String externalId;


    public FragInterDto(VirtualEditionInter uses) {
        setFragmentXmlId(uses.getFragment().getXmlId());
        setUrlId(uses.getUrlId());
        setShortName(uses.getShortName());
        setExternalId(uses.getExternalId());
    }

    public FragInterDto(ScholarInter uses) {
        setFragmentXmlId(uses.getFragment().getXmlId());
        setUrlId(uses.getUrlId());
        setShortName(uses.getShortName());
        setExternalId(uses.getExternalId());
    }

    public String getFragmentXmlId() {
        return this.fragmentXmlId;
    }

    public void setFragmentXmlId(String fragmentXmlId) {
        this.fragmentXmlId = fragmentXmlId;
    }

    public String getUrlId() {
        return this.urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
