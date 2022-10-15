package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.dto.LdoDUserDto;

import java.util.List;

public class VirtualEditionsDto {
    private List<VirtualEditionDto> virtualEditions;
    private LdoDUserDto user;

    public VirtualEditionsDto(List<VirtualEditionDto> ves, LdoDUser user) {
        setVirtualEditions(ves);
        setUser(user);
    }

    public List<VirtualEditionDto> getVirtualEditions() {
        return virtualEditions;
    }

    public void setVirtualEditions(List<VirtualEditionDto> virtualEditions) {
        this.virtualEditions = virtualEditions;
    }

    public LdoDUserDto getUser() {
        return user;
    }

    public void setUser(LdoDUser user) {
        this.user = user != null ? new LdoDUserDto(user) : null;
    }
}
