package pt.ist.socialsoftware.edition.recommendation.api.baseDto;



import pt.ist.socialsoftware.edition.recommendation.api.textDto.FragmentDto;

import java.util.HashSet;
import java.util.Set;

public class FragmentBaseDto {

    private String xmlId;

    //cached attributes
    private String title;
    private String externalId;



    public FragmentBaseDto() {  }

    public FragmentBaseDto(FragmentDto fragmentDto) {
        this.xmlId = fragmentDto.getXmlId();
        this.title = fragmentDto.getTitle();
        this.externalId = fragmentDto.getExternalId();
    }

    public String getXmlId() {
        return this.xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
