package pt.ist.socialsoftware.edition.recommendation.api.wrappers;

import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.recommendation.api.dto.PropertyDto;

import pt.ist.socialsoftware.edition.recommendation.feature.properties.Property;

import java.util.List;

public class RecommendationVirtualEditionInter {

    private VirtualEditionInterDto inter;
    private String username;
    private VirtualEditionDto virtualEditionDto;
    private List<PropertyDto> properties;

    public RecommendationVirtualEditionInter(VirtualEditionInterDto inter,
                                             String username,
                                             VirtualEditionDto virtualEdition,
                                             List<PropertyDto> properties) {
        this.inter = inter;
        this.username = username;
        this.virtualEditionDto = virtualEdition;
        this.properties = properties;
    }

    public RecommendationVirtualEditionInter() {}

    public VirtualEditionInterDto getInter() {
        return inter;
    }

    public void setInter(VirtualEditionInterDto inter) {
        this.inter = inter;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public VirtualEditionDto getVirtualEditionDto() {
        return virtualEditionDto;
    }

    public void setVirtualEditionDto(VirtualEditionDto virtualEditionDto) {
        this.virtualEditionDto = virtualEditionDto;
    }

    public List<PropertyDto> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyDto> properties) {
        this.properties = properties;
    }
}
