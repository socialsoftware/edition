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

import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.security.LdoDSession;
import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateValueException;
import pt.ist.socialsoftware.edition.validator.VirtualEditionValidator;

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
					.getSelectedVirtualEditions()) {
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
		return "listVirtualEditions";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/create")
	public String createVirtualEdition(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("acronym") String acronym,
			@RequestParam("title") String title,
			@RequestParam("public") boolean pub) {

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
			model.addAttribute("public", pub);
			model.addAttribute("virtualEditions", LdoD.getInstance()
					.getVirtualEditions4User(LdoDUser.getUser(), ldoDSession));
			model.addAttribute("user", LdoDUser.getUser());
			return "listVirtualEditions";
		}

		try {
			virtualEdition = new VirtualEdition(LdoD.getInstance(),
					LdoDUser.getUser(), acronym, title, date, pub);
		} catch (LdoDDuplicateValueException ex) {
			errors.add("virtualedition.acronym.duplicate");
			model.addAttribute("errors", errors);
			model.addAttribute("acronym", "");
			model.addAttribute("title", title);
			model.addAttribute("public", pub);
			model.addAttribute("user", LdoDUser.getUser());
			Transaction.abort();
			return "listVirtualEditions";
		}

		model.addAttribute("virtualEditions", LdoD.getInstance()
				.getVirtualEditions4User(LdoDUser.getUser(), ldoDSession));
		model.addAttribute("user", LdoDUser.getUser());
		return "listVirtualEditions";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/delete")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String deleteVirtualEdition(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("externalId") String externalId) {
		VirtualEdition virtualEdition = AbstractDomainObject
				.fromExternalId(externalId);
		if (virtualEdition == null) {
			return "pageNotFound";
		} else {
			virtualEdition.remove();

			if (ldoDSession.hasSelectedVE(virtualEdition)) {
				ldoDSession.removeSelectedVE(virtualEdition);
			}

			model.addAttribute("virtualEditions", LdoD.getInstance()
					.getVirtualEditions4User(LdoDUser.getUser(), ldoDSession));
			model.addAttribute("user", LdoDUser.getUser());
			return "listVirtualEditions";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/editForm/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String showEditVirtualEdition(Model model,
			@PathVariable String externalId) {
		VirtualEdition virtualEdition = AbstractDomainObject
				.fromExternalId(externalId);
		if (virtualEdition == null) {
			return "pageNotFound";
		} else {
			model.addAttribute("externalId", virtualEdition.getExternalId());
			model.addAttribute("acronym", virtualEdition.getAcronym());
			model.addAttribute("title", virtualEdition.getTitle());
			model.addAttribute("date", virtualEdition.getDate());
			model.addAttribute("public", virtualEdition.getPub());
			return "editVirtualEdition";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/edit/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String editVirtualEdition(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String externalId,
			@RequestParam("acronym") String acronym,
			@RequestParam("title") String title,
			@RequestParam("public") boolean pub) {
		VirtualEdition virtualEdition = AbstractDomainObject
				.fromExternalId(externalId);
		if (virtualEdition == null) {
			return "pageNotFound";
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
			model.addAttribute("public", pub);
			return "editVirtualEdition";
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
			model.addAttribute("public", pub);
			Transaction.abort();
			return "editVirtualEdition";
		}

		model.addAttribute("virtualEditions", LdoD.getInstance()
				.getVirtualEditions4User(LdoDUser.getUser(), ldoDSession));
		model.addAttribute("user", LdoDUser.getUser());

		return "listVirtualEditions";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/toggleselection")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.private')")
	public String toggleSelectedVirtualEdition(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("externalId") String externalId) {
		VirtualEdition virtualEdition = AbstractDomainObject
				.fromExternalId(externalId);

		if (virtualEdition == null)
			return "pageNotFound";

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
		return "listVirtualEditions";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/participantsForm/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String showParticipantsForm(Model model,
			@PathVariable String externalId) {
		VirtualEdition virtualEdition = AbstractDomainObject
				.fromExternalId(externalId);
		if (virtualEdition == null) {
			return "pageNotFound";
		} else {
			model.addAttribute("virtualedition", virtualEdition);
			return "manageVirtualEditionParticipants";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/addparticipant")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String addParticipant(Model model,
			@RequestParam("externalId") String externalId,
			@RequestParam("username") String username) {

		VirtualEdition virtualEdition = AbstractDomainObject
				.fromExternalId(externalId);
		if (virtualEdition == null) {
			return "pageNotFound";
		}

		LdoD ldoD = LdoD.getInstance();
		LdoDUser user = ldoD.getUser(username);
		if (user == null) {
			List<String> errors = new ArrayList<String>();
			errors.add("user.unknown");
			model.addAttribute("errors", errors);
			model.addAttribute("username", username);
			model.addAttribute("virtualedition", virtualEdition);
			return "manageVirtualEditionParticipants";
		} else {
			if (!user.getMyVirtualEditionsSet().contains(virtualEdition)) {
				user.addMyVirtualEditions(virtualEdition);
			}
			model.addAttribute("virtualedition", virtualEdition);
			return "manageVirtualEditionParticipants";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/removeparticipant")
	@PreAuthorize("hasPermission(#veId, 'virtualedition.participant')")
	public String removeParticipant(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("veId") String veId,
			@RequestParam("userId") String userId) {

		VirtualEdition virtualEdition = AbstractDomainObject
				.fromExternalId(veId);
		LdoDUser user = AbstractDomainObject.fromExternalId(userId);

		if ((virtualEdition == null) || (user == null)) {
			return "pageNotFound";
		}

		if (virtualEdition.getParticipantCount() == 1) {
			List<String> errors = new ArrayList<String>();
			errors.add("user.one");
			model.addAttribute("errors", errors);
			model.addAttribute("virtualedition", virtualEdition);
			return "manageVirtualEditionParticipants";
		} else {
			user.removeMyVirtualEditions(virtualEdition);
			user.removeSelectedVirtualEditions(virtualEdition);

			if (user == LdoDUser.getUser()) {
				model.addAttribute(
						"virtualEditions",
						LdoD.getInstance().getVirtualEditions4User(
								LdoDUser.getUser(), ldoDSession));
				model.addAttribute("user", LdoDUser.getUser());
				return "listVirtualEditions";
			} else {
				model.addAttribute("virtualedition", virtualEdition);
				return "manageVirtualEditionParticipants";
			}
		}
	}

}
