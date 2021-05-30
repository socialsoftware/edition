package pt.ist.socialsoftware.edition.game.api.dtoc.wrapper;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import pt.ist.socialsoftware.edition.game.api.virtualDto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.game.api.virtualDto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.game.config.CustomDateTimeDeserializer;


public class CreateGameWrapper {

    private VirtualEditionDto virtualEditionBaseDto;
    private String description;
    private DateTime parse;
    private VirtualEditionInterDto inter;
    private String authenticatedUser;

    public CreateGameWrapper(VirtualEditionDto virtualEditionBaseDto, String description, DateTime parse, VirtualEditionInterDto inter, String authenticatedUser) {
        this.virtualEditionBaseDto = virtualEditionBaseDto;
        this.description = description;
        this.parse = parse;
        this.inter = inter;
        this.authenticatedUser = authenticatedUser;
    }

    public CreateGameWrapper() {}

    public VirtualEditionDto getVirtualEditionBaseDto() {
        return virtualEditionBaseDto;
    }

    public void setVirtualEditionBaseDto(VirtualEditionDto virtualEditionBaseDto) {
        this.virtualEditionBaseDto = virtualEditionBaseDto;
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
