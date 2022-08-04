package pt.ist.socialsoftware.edition.ldod.bff.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.dto.LdoDUserDto;
import pt.ist.socialsoftware.edition.ldod.dto.ldodMfes.SignupDto;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateUsernameException;

import java.util.Optional;


@Service
public class UserService {
    @Autowired
    PasswordEncoder passwordEncoder;

    public LdoDUserDto getUserService(LdoDUserDetails user) {
        return new LdoDUserDto(user.getUser());
    }

    public Optional<LdoDUserDto> getUserByUsernameService(String username) {
        LdoDUser user = LdoD.getInstance().getUser(username);
        return user != null ? Optional.of(new LdoDUserDto(user)) : Optional.empty();
    }

    public Optional<LdoDUser> findUserBySocialId(String socialId) {
        return LdoD.getInstance()
                .getUsersSet()
                .stream()
                .filter(ldoDUser -> ldoDUser.getSocialMediaId() != null && ldoDUser.getSocialMediaId().equals(socialId))
                .findFirst();
    }

    public LdoDUser createUserService(SignupDto signupForm) throws LdoDDuplicateUsernameException {
        return LdoD.getInstance().createUser(this.passwordEncoder, signupForm);
    }
}
