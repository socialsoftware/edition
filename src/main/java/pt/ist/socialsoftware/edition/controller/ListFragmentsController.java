package pt.ist.socialsoftware.edition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.socialsoftware.edition.domain.LdoD;

@Controller
@RequestMapping("/fragments")
public class ListFragmentsController {

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String listFragments(Model model) {
		model.addAttribute("fragments", LdoD.getInstance().getFragmentsSet());
		return "list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/list")
	public String getFragmentsList(@RequestParam(value = "detail", required = true) Boolean showDetail, Model model) {
		;
		LdoD ldoD = LdoD.getInstance();
		model.addAttribute("jpcEdition", ldoD.getJPCEdition());
		model.addAttribute("tscEdition", ldoD.getTSCEdition());
		model.addAttribute("rzEdition", ldoD.getRZEdition());
		model.addAttribute("jpEdition", ldoD.getJPEdition());
		model.addAttribute("fragments", ldoD.getFragmentsSet());

		if (showDetail) {
			return "fragment/listDetails";
		} else {
			return "fragment/listSimple";
		}
	}

}
