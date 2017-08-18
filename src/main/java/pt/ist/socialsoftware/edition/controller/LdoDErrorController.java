package pt.ist.socialsoftware.edition.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LdoDErrorController implements ErrorController {

	private static final String PATH = "/error";

	@RequestMapping(value = PATH)
	public String error() {
		return "utils/pageNotFound";
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}
}