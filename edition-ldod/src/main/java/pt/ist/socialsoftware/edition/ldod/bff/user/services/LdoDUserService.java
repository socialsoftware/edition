package pt.ist.socialsoftware.edition.ldod.bff.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import pt.ist.socialsoftware.edition.ldod.bff.user.dtos.LdoDUserDto;
import pt.ist.socialsoftware.edition.ldod.bff.user.dtos.SignupDto;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.forms.ChangePasswordForm;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateUsernameException;
import pt.ist.socialsoftware.edition.ldod.validator.ChangePasswordValidator;

import java.util.Optional;


@Service
public class LdoDUserService {
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

    public Optional<LdoDUser> changePasswordService(ChangePasswordForm form, BindingResult formBinding) {
        ChangePasswordValidator validator = new ChangePasswordValidator(this.passwordEncoder);
        validator.validate(form, formBinding);
        if (formBinding.hasErrors()) return Optional.empty();
        LdoDUser user = LdoD.getInstance().getUser(form.getUsername());
        user.updatePassword(this.passwordEncoder, form.getCurrentPassword(), form.getNewPassword());
        return Optional.of(user);
    }
}
