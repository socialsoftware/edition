package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import pt.ist.socialsoftware.edition.ldod.recommendation.properties.Property;

import java.util.List;

public class VirtualEditionLinearBodyDto {
    private String selected;

    private List<String> inters;
    private List<Property> properties;

    public VirtualEditionLinearBodyDto() {
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public List<String> getInters() {
        return inters;
    }

    public void setInters(List<String> inters) {
        this.inters = inters;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}


