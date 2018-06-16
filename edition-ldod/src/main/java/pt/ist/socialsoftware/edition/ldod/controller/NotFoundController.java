package pt.ist.socialsoftware.edition.ldod.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NotFoundController {

	@RequestMapping(method = RequestMethod.GET, value = "/*")
	public ModelAndView handlePageNotFound() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("utils/errorPage");
		modelAndView.addObject("message", "pagenotfound.message");
		return modelAndView;
	}

}
