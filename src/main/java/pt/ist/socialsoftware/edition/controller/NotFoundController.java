package pt.ist.socialsoftware.edition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class NotFoundController {

	@RequestMapping(method = RequestMethod.GET, value = "/*")
	public String handlePageNotFound() {
		return "utils/pageNotFound";
	}

}
