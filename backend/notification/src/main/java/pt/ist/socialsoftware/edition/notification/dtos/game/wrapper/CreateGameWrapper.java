package pt.ist.socialsoftware.edition.notification.dtos.game.wrapper;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;

import pt.ist.socialsoftware.edition.notification.config.CustomDateTimeSerializer;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;

public class CreateGameWrapper {

    private VirtualEditionDto VirtualEditionDto;
    private String description;
    private DateTime parse;
    private VirtualEditionInterDto inter;
    private String authenticatedUser;

    public CreateGameWrapper(VirtualEditionDto VirtualEditionDto, String description, DateTime parse, VirtualEditionInterDto inter, String authenticatedUser) {
        this.VirtualEditionDto = VirtualEditionDto;
        this.description = description;
        this.parse = parse;
        this.inter = inter;
        this.authenticatedUser = authenticatedUser;
    }

    public VirtualEditionDto getVirtualEditionDto() {
        return VirtualEditionDto;
    }

    public void setVirtualEditionDto(VirtualEditionDto VirtualEditionDto) {
        this.VirtualEditionDto = VirtualEditionDto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonSerialize(using = CustomDateTimeSerializer.class)
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
