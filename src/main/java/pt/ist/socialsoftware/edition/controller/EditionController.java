package pt.ist.socialsoftware.edition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

@Controller
@RequestMapping("/edition")
public class EditionController {

	@RequestMapping(method = RequestMethod.GET, value = "/{acronym}")
	public String getEditionTableOfContents(Model model,
			@PathVariable String acronym) {

		Edition edition = LdoD.getInstance().getEdition(acronym);

		if (edition == null) {
			throw new LdoDException("Não existe uma ediçao com a sigla "
					+ acronym);
		} else {
			model.addAttribute("edition", edition);

			return "editionTableOfContents";
		}

	}

}
