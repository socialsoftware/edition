package pt.ist.socialsoftware.edition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.Heteronym;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

@Controller
@RequestMapping("/edition")
public class EditionController {

	@RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}")
	public String getEditionTableOfContentsbyAcronym(Model model,
			@PathVariable String acronym) {

		Edition edition = LdoD.getInstance().getEdition(acronym);

		if (edition == null) {
			throw new LdoDException("Não existe uma ediçao com a sigla "
					+ acronym);
		} else {
			model.addAttribute("heteronym", null);
			model.addAttribute("edition", edition);

			return "editionTableOfContents";
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/internalid/{id}")
	public String getEditionTableOfContentsbyId(Model model,
			@PathVariable String id) {

		Edition edition = AbstractDomainObject.fromExternalId(id);

		if (edition == null) {
			throw new LdoDException(
					"Não existe uma ediçao com o identificador interno " + id);
		} else {
			model.addAttribute("heteronym", null);
			model.addAttribute("edition", edition);

			return "editionTableOfContents";
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/internalid/heteronym/{id1}/{id2}")
	public String getEditionTableOfContents4Heteronym(Model model,
			@PathVariable String id1, @PathVariable String id2) {

		Edition edition = AbstractDomainObject.fromExternalId(id1);
		Heteronym heteronym = AbstractDomainObject.fromExternalId(id2);

		if (edition == null) {
			throw new LdoDException(
					"Não existe uma ediçao com o identificador interno " + id1);
		} else if (heteronym == null) {
			throw new LdoDException(
					"Não existe uma heterónimo com o identificador interno "
							+ id2);
		} else {
			model.addAttribute("heteronym", heteronym);
			model.addAttribute("edition", edition);

			return "editionTableOfContents";
		}

	}

}
