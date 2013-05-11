package pt.ist.socialsoftware.edition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
	@RequestMapping(method = RequestMethod.GET, value = "/login")
	public String displayLoginPage() {
		return "login";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/login", params = "errorLogin")
	public String directToLoginPageWithError(Model model) {
		// Adding an attribute to flag that an error happened at login
		model.addAttribute("loginFailed", true);
		return "login";
	}
}
