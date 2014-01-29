package pt.ist.socialsoftware.edition.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jvstm.Transaction;

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
import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateValueException;
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

		VirtualEdition virtualEdition = null;

		VirtualEditionValidator validator = new VirtualEditionValidator(
				virtualEdition, acronym, title);
		validator.validate();

		List<String> errors = validator.getErrors();
		Map<String, Object> values = validator.getValues();

		if (errors.size() > 0) {
			model.addAttribute("errors", errors);
			model.addAttribute(
					"acronym",
					values.get("acronym") == null ? acronym : values
							.get("acronym"));
			model.addAttribute("title", values.get("title") == null ? title
					: values.get("title"));
			model.addAttribute("pub", pub);
			model.addAttribute("virtualEditions", LdoD.getInstance()
					.getVirtualEditions4User(LdoDUser.getUser(), ldoDSession));
			model.addAttribute("user", LdoDUser.getUser());
			return "virtual/list";
		}

		try {
			virtualEdition = new VirtualEdition(LdoD.getInstance(),
					LdoDUser.getUser(), acronym, title, date, pub);
		} catch (LdoDDuplicateValueException ex) {
			errors.add("virtualedition.acronym.duplicate");
			model.addAttribute("errors", errors);
			model.addAttribute("acronym", "");
			model.addAttribute("title", title);
			model.addAttribute("pub", pub);
			model.addAttribute("user", LdoDUser.getUser());
			Transaction.abort();
			return "virtual/list";
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
			virtualEdition.remove();

			if (ldoDSession.hasSelectedVE(virtualEdition)) {
				ldoDSession.removeSelectedVE(virtualEdition);
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

		VirtualEditionValidator validator = new VirtualEditionValidator(
				virtualEdition, acronym, title);
		validator.validate();

		List<String> errors = validator.getErrors();
		Map<String, Object> values = validator.getValues();

		if (errors.size() > 0) {
			model.addAttribute("errors", errors);
			model.addAttribute("externalId", virtualEdition.getExternalId());
			model.addAttribute(
					"acronym",
					values.get("acronym") == null ? acronym : values
							.get("acronym"));
			model.addAttribute("title", values.get("title") == null ? title
					: values.get("title"));
			model.addAttribute("date", virtualEdition.getDate());
			model.addAttribute("pub", pub);
			return "virtual/edit";
		}

		try {
			virtualEdition.setAcronym(acronym);
			virtualEdition.setTitle(title);
			virtualEdition.setPub(pub);
		} catch (LdoDDuplicateValueException ex) {
			errors.add("virtualedition.acronym.duplicate");
			model.addAttribute("errors", errors);
			model.addAttribute("externalId", virtualEdition.getExternalId());
			model.addAttribute("acronym", virtualEdition.getAcronym());
			model.addAttribute("title", title);
			model.addAttribute("date", virtualEdition.getDate());
			model.addAttribute("pub", pub);
			Transaction.abort();
			return "virtual/edit";
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
		VirtualEdition virtualEdition = FenixFramework
				.getDomainObject(externalId);

		if (virtualEdition == null)
			return "utils/pageNotFound";

		LdoDUser user = LdoDUser.getUser();

		if (ldoDSession.hasSelectedVE(virtualEdition)) {
			ldoDSession.removeSelectedVE(virtualEdition);
			if (user != null)
				user.removeSelectedVirtualEditions(virtualEdition);
		} else {
			ldoDSession.addSelectedVE(virtualEdition);
			if (user != null)
				user.addSelectedVirtualEditions(virtualEdition);
		}

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
			if (!user.getMyVirtualEditionsSet().contains(virtualEdition)) {
				user.addMyVirtualEditions(virtualEdition);
			}
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
			user.removeMyVirtualEditions(virtualEdition);
			user.removeSelectedVirtualEditions(virtualEdition);

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
		} else if (virtualEdition.canAddFragInter(inter)) {
			VirtualEditionInter addInter = new VirtualEditionInter(
					virtualEdition, inter);
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
