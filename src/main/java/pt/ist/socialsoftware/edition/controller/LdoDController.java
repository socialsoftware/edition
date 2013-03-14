package pt.ist.socialsoftware.edition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.services.LoadLdoDFromTEIService;

@Controller
public class LdoDController {

	@RequestMapping(method = RequestMethod.GET, value = "/search")
	public String showHome(Model model) {
		return "search";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/load")
	public String loadTEIFile(Model model) {
		LoadLdoDFromTEIService service = new LoadLdoDFromTEIService();
		service.execution();
		return "home";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/search/fragments")
	public String listRecipes(Model model) {
		model.addAttribute("fragments", LdoD.getInstance().getFragmentsSet());

		return "listFragments";
	}

}
