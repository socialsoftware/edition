package pt.ist.socialsoftware.edition.ldod.frontend.virtual;

import pt.ist.socialsoftware.edition.ldod.frontend.user.FEUserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;

import java.util.Set;

public class FEVirtualRequiresInterface {
    // Uses Frontend User Module
    private final FEUserProvidesInterface feUserProvidesInterface = new FEUserProvidesInterface();

    public String getAuthenticatedUser() {
        return this.feUserProvidesInterface.getAuthenticatedUser();
    }


    // Uses User Module
    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

    public UserDto getUser(String username) {
        return this.userProvidesInterface.getUser(username);
    }


    // User Virtual Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public Set<VirtualEditionDto> getPublicVirtualEditionsOrUserIsParticipant(String username) {
        return this.virtualProvidesInterface.getPublicVirtualEditionsOrUserIsParticipant(username);
    }

}