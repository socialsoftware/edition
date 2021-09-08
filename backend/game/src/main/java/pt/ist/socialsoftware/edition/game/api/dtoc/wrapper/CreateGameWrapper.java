package pt.ist.socialsoftware.edition.game.api.dtoc.wrapper;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import pt.ist.socialsoftware.edition.game.config.CustomDateTimeDeserializer;
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

    public CreateGameWrapper() {}

    public VirtualEditionDto getVirtualEditionDto() {
        return VirtualEditionDto;
    }

    public void setVirtualEditionDto(VirtualEditionDto virtualEditionBaseDto) {
        this.VirtualEditionDto = virtualEditionBaseDto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
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
