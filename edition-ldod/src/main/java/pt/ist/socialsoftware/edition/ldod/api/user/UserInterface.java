package pt.ist.socialsoftware.edition.ldod.api.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.api.user.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.domain.User;
import pt.ist.socialsoftware.edition.ldod.domain.UserModule;

import java.util.Optional;

public class UserInterface {
    private static final Logger logger = LoggerFactory.getLogger(UserInterface.class);

    public UserDto getUser(String username) {
        User user = UserModule.getInstance().getUser(username);

        return user != null ? new UserDto(user) : null;
    }

    public String getFirstName(String username) {
        return getUserByUsername(username).orElse(null).getFirstName();
    }

    public String getLastName(String username) {
        return getUserByUsername(username).orElse(null).getLastName();
    }

    public boolean isEnabled(String username) {
        return getUserByUsername(username).orElse(null).getEnabled();
    }

    public boolean isActive(String username) {
        return getUserByUsername(username).orElse(null).getActive();
    }

    public String getAuthenticatedUser() {
        User user = User.getAuthenticatedUser();
        return user != null ? user.getUsername() : null;
    }

    private Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(UserModule.getInstance().getUser(username));
    }

}
