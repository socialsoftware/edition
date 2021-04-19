package pt.ist.socialsoftware.edition.ldod.frontend.user.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.forms.EditUserForm;

public class EditUserValidator implements Validator {
    private static final Logger logger = LoggerFactory.getLogger(EditUserValidator.class);

    private final FeUserRequiresInterface feUserRequiresInterface = new FeUserRequiresInterface();

    @Override
    public boolean supports(Class<?> clazz) {
        return EditUserForm.class.equals(clazz);

    }

    @Override
    public void validate(Object target, Errors errors) {
        EditUserForm form = (EditUserForm) target;

        logger.debug("validate oldUsername:{}, newUsername:{}", form.getOldUsername(), form.getNewUsername());

        if (!form.getNewUsername().equals(form.getOldUsername())
                && this.feUserRequiresInterface.getUser(form.getNewUsername()) != null) {
            errors.rejectValue("newUsername", "Exist.editUserForm");
        }

    }

}
