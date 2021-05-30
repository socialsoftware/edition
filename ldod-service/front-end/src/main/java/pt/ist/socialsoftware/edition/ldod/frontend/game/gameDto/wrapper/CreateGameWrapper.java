package pt.ist.socialsoftware.edition.ldod.frontend.game.gameDto.wrapper;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;
import pt.ist.socialsoftware.edition.ldod.frontend.config.CustomDateSerializer;
import pt.ist.socialsoftware.edition.ldod.frontend.text.baseDto.VirtualEditionBaseDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionInterDto;

public class CreateGameWrapper {

    private VirtualEditionBaseDto virtualEditionBaseDto;
    private String description;
    private DateTime parse;
    private VirtualEditionInterDto inter;
    private String authenticatedUser;

    public CreateGameWrapper(VirtualEditionBaseDto virtualEditionBaseDto, String description, DateTime parse, VirtualEditionInterDto inter, String authenticatedUser) {
        this.virtualEditionBaseDto = virtualEditionBaseDto;
        this.description = description;
        this.parse = parse;
        this.inter = inter;
        this.authenticatedUser = authenticatedUser;
    }

    public VirtualEditionBaseDto getVirtualEditionBaseDto() {
        return virtualEditionBaseDto;
    }

    public void setVirtualEditionBaseDto(VirtualEditionBaseDto virtualEditionBaseDto) {
        this.virtualEditionBaseDto = virtualEditionBaseDto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    public DateTime getParse() {
        return parse;
    }

    public void setParse(DateTime parse) {
        this.parse = parse;
    }

    public VirtualEditionInterDto getInter() {
        return inter;
    }

    public void setInter(VirtualEditionInterDto inter) {
        this.inter = inter;
    }

    public String getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(String authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }
}
