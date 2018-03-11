package pt.ist.socialsoftware.edition.core.controller;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ist.socialsoftware.edition.core.domain.Edition;
import pt.ist.socialsoftware.edition.core.domain.LdoD;
import pt.ist.socialsoftware.edition.core.domain.LdoDUser;
import pt.ist.socialsoftware.edition.core.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.core.forms.ChangePasswordForm;
import pt.ist.socialsoftware.edition.core.validator.ChangePasswordValidator;

@Controller
@RequestMapping("/user")
public class UserController {
	private static Logger log = LoggerFactory.getLogger(UserController.class);

	@Inject
	private PasswordEncoder passwordEncoder;

	@RequestMapping(method = RequestMethod.GET, value = "/changePassword")
	public ChangePasswordForm passwordForm() {
		return new ChangePasswordForm();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/changePassword")
	public String changePassword(@Valid ChangePasswordForm form, BindingResult formBinding) {
		log.debug("changePassword username:{}", form.getUsername());

		ChangePasswordValidator validator = new ChangePasswordValidator(this.passwordEncoder);
		validator.validate(form, formBinding);

		if (formBinding.hasErrors()) {
			return null;
		}

		LdoDUser user = LdoD.getInstance().getUser(form.getUsername());

		user.updatePassword(this.passwordEncoder, form.getCurrentPassword(), form.getNewPassword());

		return "redirect:/";
	}

	/*@RequestMapping(method = RequestMethod.GET, value = "/profile/{username}")
	public String userContributions(Model model, @PathVariable String username) {

		LdoDUser user = LdoD.getInstance().getUser(username);

		if (user != null) {
			model.addAttribute("user", user);
			return "edition/userContributions";
		} else {
			return "redirect:/error";
		}
	}*/
    @RequestMapping(method = RequestMethod.GET, value = "/profile/{username}")
    public String profile(Model model,  @PathVariable String username) {

        LdoDUser user = LdoD.getInstance().getUser(username);

        for(VirtualEdition ve: user.getPublicEditionList()){
        	log.debug("TESTE"+ve.toString());
		}


        if (user != null) {
			model.addAttribute("user", user);
			return "social/profile";
        } else {
            return "redirect:/error";
        }
    }
}
