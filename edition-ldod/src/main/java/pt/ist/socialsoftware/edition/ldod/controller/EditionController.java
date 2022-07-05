package pt.ist.socialsoftware.edition.ldod.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.dto.EditionDto;
import pt.ist.socialsoftware.edition.ldod.dto.LdoDUserDto;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/edition")
public class EditionController {
	private static Logger logger = LoggerFactory.getLogger(EditionController.class);

	@ModelAttribute("ldoDSession")
	public LdoDSession getLdoDSession() {
		return LdoDSession.getLdoDSession();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String editionIntro(Model model) {
		return "edition/introduction-main";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}")
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
	public String getEditionTableOfContentsbyAcronym(Model model, @PathVariable String acronym) {

		Edition edition = LdoD.getInstance().getEdition(acronym);

		if (edition == null) {
			return "redirect:/error";
		} else {
			model.addAttribute("heteronym", null);
			model.addAttribute("edition", edition);
			model.addAttribute("editionDto",new EditionDto(edition));

			return "edition/tableOfContents";
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/internalid/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'edition.public')")
	public String getEditionTableOfContentsbyId(Model model, @PathVariable String externalId) {

		Edition edition = FenixFramework.getDomainObject(externalId);

		if (edition == null) {
			return "redirect:/error";
		} else {
			return "redirect:/edition/acronym/" + edition.getAcronym();
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/internalid/heteronym/{id1}/{id2}")
	public String getEditionTableOfContents4Heteronym(Model model, @PathVariable String id1, @PathVariable String id2) {

		ExpertEdition edition = FenixFramework.getDomainObject(id1);
		Heteronym heteronym = FenixFramework.getDomainObject(id2);

		if (edition == null) {
			return "redirect:/error";
		} else if (heteronym == null) {
			return "redirect:/error";
		} else {
			model.addAttribute("heteronym", heteronym);
			model.addAttribute("edition", edition);
			model.addAttribute("editionDto", new EditionDto(edition));

			return "edition/tableOfContents";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/user/{username}")
	public String getUserContributions(Model model, @PathVariable String username) {

		LdoDUser user = LdoD.getInstance().getUser(username);

		if (user != null) {
			model.addAttribute("user", user);
			model.addAttribute("userDto", new LdoDUserDto(user));
			if (user.getPlayer() != null) {
				List<ClassificationGame> games = user.getPlayer().getClassificationGameParticipantSet().stream().map
						(ClassificationGameParticipant::getClassificationGame).collect(Collectors.toList());
				model.addAttribute("games", games);
				model.addAttribute("position", LdoD.getInstance().getOverallUserPosition(user.getUsername()));
			}
			return "edition/userContributions";
		} else {
			return "redirect:/error";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}/taxonomy")
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
	public String getTaxonomyTableOfContents(Model model, @PathVariable String acronym) {
		Taxonomy taxonomy = LdoD.getInstance().getVirtualEdition(acronym).getTaxonomy();
		if (taxonomy != null) {
			model.addAttribute("taxonomy", taxonomy);
			return "edition/taxonomyTableOfContents";
		} else {
			return "redirect:/error";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}/category/{urlId}")
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
	public String getCategoryTableOfContents(Model model, @PathVariable String acronym, @PathVariable String urlId) {

		VirtualEdition virtualEdition = (VirtualEdition) LdoD.getInstance().getEdition(acronym);
		if (virtualEdition == null) {
			return "redirect:/error";
		}

		Category category = virtualEdition.getTaxonomy().getCategoryByUrlId(urlId);
		if (category == null) {
			return "redirect:/error";
		}

		model.addAttribute("category", category);
		return "edition/categoryTableOfContents";
	}

}
