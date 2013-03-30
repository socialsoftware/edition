package pt.ist.socialsoftware.edition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;

@Controller
@RequestMapping("/fragments")
public class FragmentsController {

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String listFragments(Model model) {
		model.addAttribute("fragments", LdoD.getInstance().getFragmentsSet());
		return "listFragments";
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

	@RequestMapping(method = RequestMethod.GET, value = "/{id1}/{id2}")
	public String getFragmentInter(Model model, @PathVariable String id1,
			@PathVariable String id2) {
		Fragment fragment = AbstractDomainObject.fromExternalId(id1);
		FragInter fragInter = AbstractDomainObject.fromExternalId(id2);

		if (fragment == null) {
			return "fragmentNotFound";
		} else if (fragInter == null) {
			return "fragmentNotFound";
		} else {
			model.addAttribute("fragment", fragment);
			model.addAttribute("interpretation", fragInter);
			model.addAttribute("name", fragInter.getName());
			model.addAttribute("transcription", fragInter.getTranscription());
			return "fragment";
		}

	}

}
