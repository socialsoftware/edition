package pt.ist.socialsoftware.edition.ldod.frontend.user;

import org.joda.time.LocalDate;
import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;

import java.util.Set;

public class FEUserRequiresInterface {
    // Uses User Module
    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

    public UserDto getUser(String username) {
        return this.userProvidesInterface.getUser(username);
    }

    public void setLastLogin(String username, LocalDate now) {
        this.userProvidesInterface.setUserLastLogin(username, now);
    }

    // User Virtual Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();


    public Set<VirtualEditionDto> getPublicVirtualEditionsOrUserIsParticipant(String username) {
        return this.virtualProvidesInterface.getPublicVirtualEditionsOrUserIsParticipant(username);
    }

}