package pt.ist.socialsoftware.edition.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.security.LdoDSession;
import pt.ist.socialsoftware.edition.shared.exception.LdoDCreateVirtualEditionException;
import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateAcronymException;
import pt.ist.socialsoftware.edition.shared.exception.LdoDEditVirtualEditionException;
import pt.ist.socialsoftware.edition.validator.VirtualEditionValidator;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter4OneInter;

@Controller
@SessionAttributes({ "ldoDSession" })
@RequestMapping("/virtualeditions")
public class VirtualEditionController {

	@ModelAttribute("ldoDSession")
	public LdoDSession getLdoDSession() {
		LdoDSession ldoDSession = new LdoDSession();

		System.out.println("VirtualEditionController:getLdoDSession()");

		LdoDUser user = LdoDUser.getUser();
		if (user != null) {
			for (VirtualEdition virtualEdition : user
					.getSelectedVirtualEditionsSet()) {
				ldoDSession.addSelectedVE(virtualEdition);
			}
		}
		return ldoDSession;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String listVirtualEdition(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession) {

		model.addAttribute("virtualEditions", LdoD.getInstance()
				.getVirtualEditions4User(LdoDUser.getUser(), ldoDSession));
		model.addAttribute("user", LdoDUser.getUser());
		return "virtual/list";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/create")
	public String createVirtualEdition(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("acronym") String acronym,
			@RequestParam("title") String title,
			@RequestParam("pub") boolean pub) {

		/**
		 * WORKAROUND: to create a user without regenerating the data base LdoD
		 * ldod = LdoD.getInstance(); LdoDUser tiago = new LdoDUser(ldod,
		 * "tiago",
		 * "de968c78d0e50dbfd5083e1994492548baf4159f7112242acb02f773dc308ac9");
		 * for (Role role : ldod.getRolesSet()) { tiago.addRoles(role); }
		 **/

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date today = Calendar.getInstance().getTime();
		String date = df.format(today);

		title = title.trim();
		acronym = acronym.trim();

		VirtualEdition virtualEdition = null;

		VirtualEditionValidator validator = new VirtualEditionValidator(
				virtualEdition, acronym, title);
		validator.validate();

		List<String> errors = validator.getErrors();

		if (errors.size() > 0) {
			throw new LdoDCreateVirtualEditionException(errors, acronym, title,
					pub, LdoD.getInstance().getVirtualEditions4User(
							LdoDUser.getUser(), ldoDSession),
					LdoDUser.getUser());
		}

		try {
			virtualEdition = LdoD.getInstance().createVirtualEdition(
					LdoDUser.getUser(), acronym, title, date, pub);

		} catch (LdoDDuplicateAcronymException ex) {
			errors.add("virtualedition.acronym.duplicate");
			throw new LdoDCreateVirtualEditionException(errors, acronym, title,
					pub, LdoD.getInstance().getVirtualEditions4User(
							LdoDUser.getUser(), ldoDSession),
					LdoDUser.getUser());
		}

		model.addAttribute("virtualEditions", LdoD.getInstance()
				.getVirtualEditions4User(LdoDUser.getUser(), ldoDSession));
		model.addAttribute("user", LdoDUser.getUser());
		return "virtual/list";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/delete")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String deleteVirtualEdition(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("externalId") String externalId) {
		VirtualEdition virtualEdition = FenixFramework
				.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {

			String acronym = virtualEdition.getAcronym();

			virtualEdition.remove();

			if (ldoDSession.hasSelectedVE(externalId)) {
				ldoDSession.removeSelectedVE(externalId, acronym);
			}

			model.addAttribute("virtualEditions", LdoD.getInstance()
					.getVirtualEditions4User(LdoDUser.getUser(), ldoDSession));
			model.addAttribute("user", LdoDUser.getUser());
			return "virtual/list";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/editForm/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String showEditVirtualEdition(Model model,
			@PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework
				.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("externalId", virtualEdition.getExternalId());
			model.addAttribute("acronym", virtualEdition.getAcronym());
			model.addAttribute("title", virtualEdition.getTitle());
			model.addAttribute("date", virtualEdition.getDate());
			model.addAttribute("pub", virtualEdition.getPub());
			return "virtual/edit";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/edit/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String editVirtualEdition(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String externalId,
			@RequestParam("acronym") String acronym,
			@RequestParam("title") String title,
			@RequestParam("pub") boolean pub) {
		VirtualEdition virtualEdition = FenixFramework
				.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		}

		title = title.trim();
		acronym = acronym.trim();

		VirtualEditionValidator validator = new VirtualEditionValidator(
				virtualEdition, acronym, title);
		validator.validate();

		List<String> errors = validator.getErrors();

		if (errors.size() > 0) {
			throw new LdoDEditVirtualEditionException(errors, virtualEdition,
					acronym, title, pub);
		}

		try {
			virtualEdition.edit(acronym, title, pub);
		} catch (LdoDDuplicateAcronymException ex) {
			errors.add("virtualedition.acronym.duplicate");
			throw new LdoDEditVirtualEditionException(errors, virtualEdition,
					acronym, title, pub);
		}

		model.addAttribute("virtualEditions", LdoD.getInstance()
				.getVirtualEditions4User(LdoDUser.getUser(), ldoDSession));
		model.addAttribute("user", LdoDUser.getUser());

		return "virtual/list";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/toggleselection")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.private')")
	public String toggleSelectedVirtualEdition(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("externalId") String externalId) {
		final VirtualEdition virtualEdition = FenixFramework
				.getDomainObject(externalId);

		if (virtualEdition == null)
			return "utils/pageNotFound";

		LdoDUser user = LdoDUser.getUser();

		ldoDSession.toggleSelectedVirtualEdition(user, virtualEdition);

		model.addAttribute("virtualEditions", LdoD.getInstance()
				.getVirtualEditions4User(LdoDUser.getUser(), ldoDSession));
		model.addAttribute("user", user);
		return "virtual/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/participantsForm/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String showParticipantsForm(Model model,
			@PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework
				.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("virtualedition", virtualEdition);
			return "virtual/manageParticipants";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/addparticipant")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String addParticipant(Model model,
			@RequestParam("externalId") String externalId,
			@RequestParam("username") String username) {

		VirtualEdition virtualEdition = FenixFramework
				.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		}

		LdoD ldoD = LdoD.getInstance();
		LdoDUser user = ldoD.getUser(username);
		if (user == null) {
			List<String> errors = new ArrayList<String>();
			errors.add("user.unknown");
			model.addAttribute("errors", errors);
			model.addAttribute("username", username);
			model.addAttribute("virtualedition", virtualEdition);
			return "virtual/manageParticipants";
		} else {
			user.addToVirtualEdition(virtualEdition);
			model.addAttribute("virtualedition", virtualEdition);
			return "virtual/manageParticipants";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/removeparticipant")
	@PreAuthorize("hasPermission(#veId, 'virtualedition.participant')")
	public String removeParticipant(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("veId") String veId,
			@RequestParam("userId") String userId) {

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(veId);
		LdoDUser user = FenixFramework.getDomainObject(userId);

		if ((virtualEdition == null) || (user == null)) {
			return "utils/pageNotFound";
		}

		if (virtualEdition.getParticipantSet().size() == 1) {
			List<String> errors = new ArrayList<String>();
			errors.add("user.one");
			model.addAttribute("errors", errors);
			model.addAttribute("virtualedition", virtualEdition);
			return "virtual/manageParticipants";
		} else {
			user.removeVirtualEdition(virtualEdition);

			if (user == LdoDUser.getUser()) {
				model.addAttribute(
						"virtualEditions",
						LdoD.getInstance().getVirtualEditions4User(
								LdoDUser.getUser(), ldoDSession));
				model.addAttribute("user", LdoDUser.getUser());
				return "virtual/list";
			} else {
				model.addAttribute("virtualedition", virtualEdition);
				return "virtual/manageParticipants";
			}
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/addinter/{veId}/{interId}")
	@PreAuthorize("hasPermission(#veId, 'virtualedition.participant')")
	public String addInter(Model model, @PathVariable String veId,
			@PathVariable String interId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(veId);
		FragInter inter = FenixFramework.getDomainObject(interId);
		if ((virtualEdition == null) && (inter == null)) {
			return "utils/pageNotFound";
		}

		VirtualEditionInter addInter = virtualEdition
				.createVirtualEditionInter(inter);

		if (addInter != null) {
			List<FragInter> inters = new ArrayList<FragInter>();
			inters.add(addInter);

			HtmlWriter4OneInter writer = new HtmlWriter4OneInter(
					addInter.getLastUsed());
			writer.write(false);

			model.addAttribute("writer", writer);

			model.addAttribute("ldoD", LdoD.getInstance());
			model.addAttribute("user", LdoDUser.getUser());
			model.addAttribute("fragment", inter.getFragment());
			model.addAttribute("inters", inters);
			return "fragment/main";
		} else {
			return "utils/pageNotFound";
		}
	}

}
