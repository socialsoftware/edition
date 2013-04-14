package pt.ist.socialsoftware.edition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;

@Controller
@RequestMapping("/fragments/fragment")
public class FragmentController {

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public String getFragment(Model model, @PathVariable String id) {
		Fragment fragment = AbstractDomainObject.fromExternalId(id);

		if (fragment == null) {
			return "fragmentNotFound";
		} else {
			model.addAttribute("fragment", fragment);
			model.addAttribute("transcription",
					"Selecione um testemunho para obter a transcrição");

			return "fragment";
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getInterpretation(
			@RequestParam(value = "interp", required = true) String interID,
			Model model) {
		FragInter fragInter = AbstractDomainObject.fromExternalId(interID);
		Fragment fragment = fragInter.getFragment();

		model.addAttribute("fragment", fragment);
		model.addAttribute("inter", fragInter);
		return "fragmentInterpretation";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/textual")
	public String getTextual(
			@RequestParam(value = "interp", required = true) String interID,
			@RequestParam(value = "interp2Compare", required = true) String interID2Compare,
			Model model) {
		FragInter fragInter = AbstractDomainObject.fromExternalId(interID);
		FragInter fragInter2Compare = AbstractDomainObject
				.fromExternalId(interID2Compare);

		if (interID.equals(interID2Compare)) {
			model.addAttribute("inter", fragInter);
			return "fragmentTextual";
		} else {
			model.addAttribute("inter", fragInter);
			model.addAttribute("inter2Compare", fragInter2Compare);
			return "fragmentTextualCompare";
		}
	}

}
