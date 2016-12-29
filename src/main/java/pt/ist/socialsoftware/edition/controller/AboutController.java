package pt.ist.socialsoftware.edition.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/about")
public class AboutController {
	private static Logger logger = LoggerFactory.getLogger(AboutController.class);

	@RequestMapping(method = RequestMethod.GET, value = "/faq")
	public String showFAQ(Model model) {
		// files converted in https://wordhtml.com/
		Locale locale = LocaleContextHolder.getLocale();
		if (locale.getLanguage().equals("es")) {
			return "about/faq-es";
		}
		// default
		return "about/faq-pt";
	}
}
