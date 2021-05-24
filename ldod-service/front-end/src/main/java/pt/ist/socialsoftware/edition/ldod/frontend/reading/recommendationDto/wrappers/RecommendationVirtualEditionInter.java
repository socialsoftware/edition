package pt.ist.socialsoftware.edition.ldod.frontend.reading.recommendationDto.wrappers;




import pt.ist.socialsoftware.edition.ldod.frontend.reading.recommendationDto.PropertyDto;
import pt.ist.socialsoftware.edition.ldod.frontend.text.baseDto.VirtualEditionBaseDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionInterDto;

import java.util.List;

public class RecommendationVirtualEditionInter {

    private VirtualEditionInterDto inter;
    private String username;
    private VirtualEditionBaseDto virtualEditionDto;
    private List<PropertyDto> properties;

    public RecommendationVirtualEditionInter(VirtualEditionInterDto inter,
                                             String username,
                                             VirtualEditionBaseDto virtualEdition,
                                             List<PropertyDto> properties) {
        this.inter = inter;
        this.username = username;
        this.virtualEditionDto = virtualEdition;
        this.properties = properties;
    }

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

    public VirtualEditionBaseDto getVirtualEditionDto() {
        return virtualEditionDto;
    }

    public void setVirtualEditionDto(VirtualEditionBaseDto virtualEditionDto) {
        this.virtualEditionDto = virtualEditionDto;
    }

    public List<PropertyDto> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyDto> properties) {
        this.properties = properties;
    }
}
