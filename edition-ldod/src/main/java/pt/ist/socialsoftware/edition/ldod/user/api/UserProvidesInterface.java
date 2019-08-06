package pt.ist.socialsoftware.edition.ldod.user.api;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.ist.socialsoftware.edition.ldod.domain.User;
import pt.ist.socialsoftware.edition.ldod.domain.UserModule;
import pt.ist.socialsoftware.edition.ldod.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;

import java.util.Optional;

public class UserProvidesInterface {
    private static final Logger logger = LoggerFactory.getLogger(UserProvidesInterface.class);

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

    public String getEmail(String username) {
        return getUserByUsername(username).orElse(null).getEmail();
    }

    public String getSocialMediaId(String username) {
        return getUserByUsername(username).orElse(null).getSocialMediaId();
    }

    public void updatePassword(String username, PasswordEncoder passwordEncoder, String currentPassword, String newPassword) {
        getUserByUsername(username).orElseThrow(LdoDException::new)
                .updatePassword(passwordEncoder, currentPassword, newPassword);
    }

    public void setUserLastLogin(String username, LocalDate now) {
        getUserByUsername(username).orElseThrow(() -> new LdoDException("User not found")).setLastLogin(now);
    }

    private Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(UserModule.getInstance().getUser(username));
    }

}
