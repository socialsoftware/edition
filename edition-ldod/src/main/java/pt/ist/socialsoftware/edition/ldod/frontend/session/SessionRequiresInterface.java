package pt.ist.socialsoftware.edition.ldod.frontend.session;

import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;

import java.util.List;

public class SessionRequiresInterface {
    // Uses Virtual Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public List<String> getSelectedVirtualEditionsByUser(String username) {
        return this.virtualProvidesInterface.getSelectedVirtualEditionsByUser(username);
    }

    public void materializeVirtualEditions(String username, List<String> selectedAcronyms) {
        this.virtualProvidesInterface.addToUserSelectedVirtualEditions(username, selectedAcronyms);
    }

    // Uses User Module
    UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

    public UserDto getUser(String user) {
        return this.userProvidesInterface.getUser(user);

    }

}
