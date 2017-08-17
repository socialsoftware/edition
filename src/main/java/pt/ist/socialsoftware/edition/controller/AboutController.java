package pt.ist.socialsoftware.edition.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/about")
public class AboutController {
	private static Logger logger = LoggerFactory.getLogger(AboutController.class);

	@RequestMapping(method = RequestMethod.GET, value = "/archive")
	public String showArchive() {
		return "about/archive-main";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/faq")
	public String showFAQ() {
		return "about/faq-main";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/articles")
	public String showArticles() {
		return "about/articles-main";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/conduct")
	public String showConduct() {
		return "about/conduct-main";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/team")
	public String showTeam() {
		return "about/team-main";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/acknowledgements")
	public String showAcknowledgements() {
		return "about/acknowledgements-main";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/copyright")
	public String showCopyright() {
		return "about/copyright-main";
	}

}
