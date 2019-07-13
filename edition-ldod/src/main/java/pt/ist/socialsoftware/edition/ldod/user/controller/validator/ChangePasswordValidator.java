package pt.ist.socialsoftware.edition.ldod.user.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pt.ist.socialsoftware.edition.ldod.domain.User;
import pt.ist.socialsoftware.edition.ldod.domain.UserModule;
import pt.ist.socialsoftware.edition.ldod.user.controller.forms.ChangePasswordForm;

public class ChangePasswordValidator implements Validator {
    private static final Logger logger = LoggerFactory.getLogger(ChangePasswordValidator.class);

    private final PasswordEncoder passwordEncoder;

    public ChangePasswordValidator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ChangePasswordForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChangePasswordForm changePasswordForm = (ChangePasswordForm) target;
        if (!changePasswordForm.getNewPassword().equals(changePasswordForm.getRetypedPassword())) {
            errors.rejectValue("newPassword", "Different.changePasswordForm");
            errors.rejectValue("retypedPassword", "Different.changePasswordForm");
        }

        logger.debug("validate username:{}, passwordEncoder:{}", changePasswordForm.getUsername(), this.passwordEncoder);
        User user = UserModule.getInstance().getUser(changePasswordForm.getUsername());

        if (!this.passwordEncoder.matches(changePasswordForm.getCurrentPassword(), user.getPassword())) {
            errors.rejectValue("currentPassword", "DoNotMatch.changePasswordForm");
        }

    }

}
