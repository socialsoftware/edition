package pt.ist.socialsoftware.edition.ldod.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class 	SigninController {
	private static Logger log = LoggerFactory.getLogger(SigninController.class);

	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public void signin() {log.debug("signin");}

}
