package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import pt.ist.socialsoftware.edition.ldod.recommendation.properties.Property;

import java.util.List;

public class VirtualEditionAssistedSortDto {

    private String selected;
    private List<VirtualEditionInterDto> inters;

    private List<Property> properties;

    public VirtualEditionAssistedSortDto(String selected, List<VirtualEditionInterDto> inters, List<Property> properties) {
        setInters(inters);
        setSelected(selected);
        setProperties(properties);

    }

    public VirtualEditionAssistedSortDto() {}

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public List<VirtualEditionInterDto> getInters() {
        return inters;
    }

    public void setInters(List<VirtualEditionInterDto> inters) {
        this.inters = inters;
    }
}
