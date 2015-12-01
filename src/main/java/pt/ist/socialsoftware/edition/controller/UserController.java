package pt.ist.socialsoftware.edition.controller;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.forms.ChangePasswordForm;
import pt.ist.socialsoftware.edition.validator.ChangePasswordValidator;

@Controller
@RequestMapping("/user")
public class UserController {
	private static Logger log = LoggerFactory.getLogger(UserController.class);

	@Inject
	private PasswordEncoder passwordEncoder;

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(method = RequestMethod.GET, value = "/changePassword")
	public ChangePasswordForm passwordForm() {
		return new ChangePasswordForm();
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(method = RequestMethod.POST, value = "/changePassword")
	public String changePassword(@Valid ChangePasswordForm form, BindingResult formBinding) {
		log.debug("changePassword username:{}", form.getUsername());

		ChangePasswordValidator validator = new ChangePasswordValidator(passwordEncoder);
		validator.validate(form, formBinding);

		if (formBinding.hasErrors()) {
			return null;
		}

		LdoDUser user = LdoD.getInstance().getUser(form.getUsername());

		user.updatePassword(passwordEncoder, form.getCurrentPassword(), form.getNewPassword());

		return "redirect:/";
	}

}
