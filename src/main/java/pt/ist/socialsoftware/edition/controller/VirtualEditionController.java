package pt.ist.socialsoftware.edition.controller;

import jvstm.Transaction;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateValueException;

@Controller
@RequestMapping("/virtualeditions")
public class VirtualEditionController {

	@RequestMapping(method = RequestMethod.GET)
	public String listVirtualEdition(Model model) {

		model.addAttribute("virtualEditions", LdoD.getInstance()
				.getVirtualEditions());
		return "listVirtualEditions";

	}

	@RequestMapping(method = RequestMethod.GET, value = "/createForm")
	public String showCreateVirtualEdition(Model model) {
		model.addAttribute("error", false);
		return "createVirtualEdition";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createVirtualEdition(Model model,
			@RequestParam("acronym") String acronym,
			@RequestParam("name") String name) {

		String date = "12/1/2013";
		VirtualEdition virtualEdition = null;
		try {
			virtualEdition = new VirtualEdition(LdoD.getInstance(), acronym,
					name, date);
		} catch (LdoDDuplicateValueException ex) {
			Transaction.abort();
			model.addAttribute("error", true);
			return "createVirtualEdition";
		}

		model.addAttribute("virtualEditions", LdoD.getInstance()
				.getVirtualEditions());
		return "listVirtualEditions";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/delete")
	public String deleteVirtualEdition(Model model,
			@RequestParam("externalId") String externalId) {
		VirtualEdition virtualEdition = AbstractDomainObject
				.fromExternalId(externalId);
		if (virtualEdition == null) {
			return "pageNotFound";
		} else {
			virtualEdition.remove();

			model.addAttribute("virtualEditions", LdoD.getInstance()
					.getVirtualEditions());
			return "listVirtualEditions";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/editForm/{id}")
	public String showEditVirtualEdition(Model model, @PathVariable String id) {
		VirtualEdition virtualEdition = AbstractDomainObject.fromExternalId(id);
		if (virtualEdition == null) {
			return "pageNotFound";
		} else {
			model.addAttribute("error", false);
			model.addAttribute("externalId", virtualEdition.getExternalId());
			model.addAttribute("acronym", virtualEdition.getAcronym());
			model.addAttribute("name", virtualEdition.getName());
			return "editVirtualEdition";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/edit/{id}")
	public String editVirtualEdition(Model model, @PathVariable String id,
			@RequestParam("acronym") String acronym,
			@RequestParam("name") String name) {
		VirtualEdition virtualEdition = AbstractDomainObject.fromExternalId(id);
		if (virtualEdition == null) {
			return "pageNotFound";
		}

		try {
			virtualEdition.setAcronym(acronym);
			virtualEdition.setName(name);
		} catch (LdoDDuplicateValueException ex) {
			model.addAttribute("error", true);
			model.addAttribute("externalId", virtualEdition.getExternalId());
			model.addAttribute("acronym", virtualEdition.getAcronym());
			model.addAttribute("name", virtualEdition.getName());
			Transaction.abort();
			return "editVirtualEdition";
		}

		model.addAttribute("virtualEditions", LdoD.getInstance()
				.getVirtualEditions());
		return "listVirtualEditions";
	}

}
