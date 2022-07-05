package pt.ist.socialsoftware.edition.ldod.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;

@Controller
public class HomeController {
	private static Logger logger = LoggerFactory.getLogger(HomeController.class);

	@ModelAttribute("ldoDSession")
	public LdoDSession getLdoDSession() {
		return LdoDSession.getLdoDSession();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String showHome(Model model) {
		return "home";
	}

}