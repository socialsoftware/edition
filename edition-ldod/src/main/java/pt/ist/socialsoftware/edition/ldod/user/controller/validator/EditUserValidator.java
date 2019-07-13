package pt.ist.socialsoftware.edition.ldod.user.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pt.ist.socialsoftware.edition.ldod.domain.UserModule;
import pt.ist.socialsoftware.edition.ldod.user.controller.forms.EditUserForm;

public class EditUserValidator implements Validator {
    private static final Logger logger = LoggerFactory.getLogger(EditUserValidator.class);

    @Override
    public boolean supports(Class<?> clazz) {
        return EditUserForm.class.equals(clazz);

    }

    @Override
    public void validate(Object target, Errors errors) {
        EditUserForm form = (EditUserForm) target;

        logger.debug("validate oldUsername:{}, newUsername:{}", form.getOldUsername(), form.getNewUsername());

        if (!form.getNewUsername().equals(form.getOldUsername())
                && UserModule.getInstance().getUser(form.getNewUsername()) != null) {
            errors.rejectValue("newUsername", "Exist.editUserForm");
        }

    }

}
