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

	@RequestMapping(method = RequestMethod.GET)
	public void fragment() {
	}

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

	@RequestMapping(method = RequestMethod.POST)
	public String processSubmit(
			@RequestParam(value = "interp", required = true) String interID,
			Model model) {
		FragInter fragInter = AbstractDomainObject.fromExternalId(interID);
		Fragment fragment = fragInter.getFragment();

		System.out.println(interID + fragInter + fragment);

		model.addAttribute("fragment", fragment);
		model.addAttribute("interpretation", fragInter);
		model.addAttribute("name", fragInter.getName());
		model.addAttribute("transcription", fragInter.getTranscription());
		return "fragmentInterpretation";

	}

}
