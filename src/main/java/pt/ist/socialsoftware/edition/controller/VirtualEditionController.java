package pt.ist.socialsoftware.edition.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.Tag;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.mallet.TopicModeler;
import pt.ist.socialsoftware.edition.security.LdoDSession;
import pt.ist.socialsoftware.edition.security.LdoDUserDetails;
import pt.ist.socialsoftware.edition.security.LdoDUserDetailsService;
import pt.ist.socialsoftware.edition.shared.exception.LdoDCreateVirtualEditionException;
import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateAcronymException;
import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateNameException;
import pt.ist.socialsoftware.edition.shared.exception.LdoDEditVirtualEditionException;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.utils.TopicListDTO;
import pt.ist.socialsoftware.edition.validator.VirtualEditionValidator;
import pt.ist.socialsoftware.edition.visitors.PlainHtmlWriter4OneInter;

@Controller
@SessionAttributes({ "ldoDSession" })
@RequestMapping("/virtualeditions")
public class VirtualEditionController {
	private static Logger logger = LoggerFactory.getLogger(LdoDUserDetailsService.class);

	@ModelAttribute("ldoDSession")
	public LdoDSession getLdoDSession() {
		LdoDSession ldoDSession = new LdoDSession();

		System.out.println("VirtualEditionController:getLdoDSession()");

		LdoDUser user = LdoDUser.getAuthenticatedUser();
		if (user != null) {
			for (VirtualEdition virtualEdition : user.getSelectedVirtualEditionsSet()) {
				ldoDSession.addSelectedVE(virtualEdition);
			}
		}
		return ldoDSession;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String listVirtualEdition(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession) {

		model.addAttribute("expertEditions", LdoD.getInstance().getSortedExpertEdition());
		model.addAttribute("virtualEditions",
				LdoD.getInstance().getVirtualEditions4User(LdoDUser.getAuthenticatedUser(), ldoDSession));
		model.addAttribute("user", LdoDUser.getAuthenticatedUser());
		return "virtual/editions";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/create")
	public String createVirtualEdition(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("acronym") String acronym, @RequestParam("title") String title,
			@RequestParam("pub") boolean pub, @RequestParam("use") String editionID) {

		Edition usedEdition = null;
		if (!editionID.equals("no")) {
			usedEdition = FenixFramework.getDomainObject(editionID);
		}

		LocalDate date = new LocalDate();

		title = title.trim();
		acronym = acronym.trim();

		VirtualEdition virtualEdition = null;

		VirtualEditionValidator validator = new VirtualEditionValidator(virtualEdition, acronym, title);
		validator.validate();

		List<String> errors = validator.getErrors();

		if (errors.size() > 0) {
			throw new LdoDCreateVirtualEditionException(errors, acronym, title, pub,
					LdoD.getInstance().getVirtualEditions4User(LdoDUser.getAuthenticatedUser(), ldoDSession),
					LdoDUser.getAuthenticatedUser());
		}

		try {
			virtualEdition = LdoD.getInstance().createVirtualEdition(LdoDUser.getAuthenticatedUser(), acronym, title,
					date, pub, usedEdition);

		} catch (LdoDDuplicateAcronymException ex) {
			errors.add("virtualedition.acronym.duplicate");
			throw new LdoDCreateVirtualEditionException(errors, acronym, title, pub,
					LdoD.getInstance().getVirtualEditions4User(LdoDUser.getAuthenticatedUser(), ldoDSession),
					LdoDUser.getAuthenticatedUser());
		}

		model.addAttribute("expertEditions", LdoD.getInstance().getSortedExpertEdition());
		model.addAttribute("virtualEditions",
				LdoD.getInstance().getVirtualEditions4User(LdoDUser.getAuthenticatedUser(), ldoDSession));
		model.addAttribute("user", LdoDUser.getAuthenticatedUser());
		return "virtual/editions";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/delete")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String deleteVirtualEdition(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("externalId") String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {

			String acronym = virtualEdition.getAcronym();

			virtualEdition.remove();

			if (ldoDSession.hasSelectedVE(acronym)) {
				ldoDSession.removeSelectedVE(acronym);
			}

			model.addAttribute("expertEditions", LdoD.getInstance().getSortedExpertEdition());
			model.addAttribute("virtualEditions",
					LdoD.getInstance().getVirtualEditions4User(LdoDUser.getAuthenticatedUser(), ldoDSession));
			model.addAttribute("user", LdoDUser.getAuthenticatedUser());
			return "virtual/editions";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/editForm/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String showEditVirtualEdition(Model model, @PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("virtualEdition", virtualEdition);
			model.addAttribute("externalId", virtualEdition.getExternalId());
			model.addAttribute("acronym", virtualEdition.getAcronym());
			model.addAttribute("title", virtualEdition.getTitle());
			model.addAttribute("date", virtualEdition.getDate().toString("dd-MM-yyyy"));
			model.addAttribute("pub", virtualEdition.getPub());
			return "virtual/edition";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/edit/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String editVirtualEdition(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String externalId, @RequestParam("acronym") String acronym,
			@RequestParam("title") String title, @RequestParam("pub") boolean pub,
			@RequestParam("fraginters") String fraginters) {
		logger.debug("editVirtualEdition externalId:{}, acronym:{}, title:{}, pub:{}, fraginters:{}", externalId,
				acronym, title, pub, fraginters);

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		}

		title = title.trim();
		acronym = acronym.trim();

		System.out.println("FRAGINTERS " + fraginters);

		VirtualEditionValidator validator = new VirtualEditionValidator(virtualEdition, acronym, title);
		validator.validate();

		List<String> errors = validator.getErrors();

		if (errors.size() > 0) {
			throw new LdoDEditVirtualEditionException(errors, virtualEdition, acronym, title, pub);
		}

		// passar nova lista de inters

		try {
			virtualEdition.edit(acronym, title, pub, fraginters);
		} catch (LdoDDuplicateAcronymException ex) {
			errors.add("virtualedition.acronym.duplicate");
			throw new LdoDEditVirtualEditionException(errors, virtualEdition, acronym, title, pub);
		}

		model.addAttribute("expertEditions", LdoD.getInstance().getSortedExpertEdition());
		model.addAttribute("virtualEditions",
				LdoD.getInstance().getVirtualEditions4User(LdoDUser.getAuthenticatedUser(), ldoDSession));
		model.addAttribute("user", LdoDUser.getAuthenticatedUser());

		return "virtual/editions";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/toggleselection")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.public')")
	public String toggleSelectedVirtualEdition(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("externalId") String externalId) {
		final VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);

		if (virtualEdition == null)
			return "utils/pageNotFound";

		LdoDUser user = LdoDUser.getAuthenticatedUser();

		ldoDSession.toggleSelectedVirtualEdition(user, virtualEdition);

		model.addAttribute("expertEditions", LdoD.getInstance().getSortedExpertEdition());
		model.addAttribute("virtualEditions",
				LdoD.getInstance().getVirtualEditions4User(LdoDUser.getAuthenticatedUser(), ldoDSession));
		model.addAttribute("user", user);
		return "virtual/editions";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/participantsForm/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String showParticipantsForm(Model model, @PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {
			Set<LdoDUser> users = virtualEdition.getLdoD4Virtual().getUsersSet();
			model.addAttribute("virtualedition", virtualEdition);
			model.addAttribute("users", users);
			return "virtual/participants";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/addparticipant")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String addParticipant(Model model, @RequestParam("externalId") String externalId,
			@RequestParam("username") String username) {

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		}

		Set<LdoDUser> users = virtualEdition.getLdoD4Virtual().getUsersSet();

		LdoD ldoD = LdoD.getInstance();
		LdoDUser user = ldoD.getUser(username);
		if (user == null) {
			List<String> errors = new ArrayList<String>();
			errors.add("user.unknown");
			model.addAttribute("errors", errors);
			model.addAttribute("username", username);
			model.addAttribute("virtualedition", virtualEdition);
			model.addAttribute("users", users);
			return "virtual/participants";
		} else {
			user.addToVirtualEdition(virtualEdition);
			model.addAttribute("virtualedition", virtualEdition);
			model.addAttribute("users", users);
			return "virtual/participants";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/removeparticipant")
	@PreAuthorize("hasPermission(#veId, 'virtualedition.participant')")
	public String removeParticipant(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("veId") String veId, @RequestParam("userId") String userId) {

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(veId);

		LdoDUser user = FenixFramework.getDomainObject(userId);

		if ((virtualEdition == null) || (user == null)) {
			return "utils/pageNotFound";
		}

		Set<LdoDUser> users = virtualEdition.getLdoD4Virtual().getUsersSet();

		if (virtualEdition.getParticipantSet().size() == 1) {
			List<String> errors = new ArrayList<String>();
			errors.add("user.one");
			model.addAttribute("errors", errors);
			model.addAttribute("virtualedition", virtualEdition);
			model.addAttribute("users", users);
			return "virtual/participants";
		} else {
			user.removeVirtualEdition(virtualEdition);

			if (user == LdoDUser.getAuthenticatedUser()) {
				model.addAttribute("virtualEditions",
						LdoD.getInstance().getVirtualEditions4User(LdoDUser.getAuthenticatedUser(), ldoDSession));
				model.addAttribute("user", LdoDUser.getAuthenticatedUser());
				return "virtual/editions";
			} else {
				model.addAttribute("virtualedition", virtualEdition);
				model.addAttribute("users", users);
				return "virtual/participants";
			}
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/addinter/{veId}/{interId}")
	@PreAuthorize("hasPermission(#veId, 'virtualedition.participant')")
	public String addInter(Model model, @PathVariable String veId, @PathVariable String interId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(veId);
		FragInter inter = FenixFramework.getDomainObject(interId);
		if ((virtualEdition == null) || (inter == null)) {
			return "utils/pageNotFound";
		}

		VirtualEditionInter addInter = virtualEdition.createVirtualEditionInter(inter,
				virtualEdition.getMaxFragNumber() + 1);

		if (addInter != null) {
			List<FragInter> inters = new ArrayList<FragInter>();
			inters.add(addInter);

			PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(addInter.getLastUsed());
			writer.write(false);

			model.addAttribute("ldoD", LdoD.getInstance());
			model.addAttribute("user", LdoDUser.getAuthenticatedUser());
			model.addAttribute("fragment", inter.getFragment());
			model.addAttribute("inters", inters);
			model.addAttribute("writer", writer);
			return "fragment/main";
		} else {
			return "utils/pageNotFound";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}/taxonomy")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.public')")
	public String taxonomy(Model model, @PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("virtualEdition", virtualEdition);
			return "virtual/taxonomy";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}/taxonomy/generateTopics")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String generateTopicModelling(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String externalId, @RequestParam("numTopics") int numTopics,
			@RequestParam("numWords") int numWords, @RequestParam("thresholdCategories") int thresholdCategories,
			@RequestParam("numIterations") int numIterations) throws IOException {
		logger.debug(
				"generateTopicModelling externalId:{}, numTopics:{}, numWords:{}, thresholdCategories:{}, numIterations:{}",
				externalId, numTopics, numWords, thresholdCategories, numIterations);

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {
			LdoDUserDetails userDetails = (LdoDUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();

			List<String> topicErrors = new ArrayList<String>();
			TopicListDTO topicListDTO = null;
			TopicModeler modeler = new TopicModeler();
			try {
				topicListDTO = modeler.generate(userDetails.getUser(), virtualEdition, numTopics, numWords,
						thresholdCategories, numIterations);
			} catch (LdoDException ex) {
				topicErrors.add("Não existe nenhum fragmento associado a esta edição ou é necessário gerar o Corpus");
				model.addAttribute("topicErrors", topicErrors);
				model.addAttribute("topicList", topicListDTO);
				return "virtual/generatedTopics";
			}

			model.addAttribute("virtualEdition", virtualEdition);
			model.addAttribute("topicList", topicListDTO);
			return "virtual/generatedTopics";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/taxonomy/createTopics")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String createTopicModelling(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String externalId, @ModelAttribute("topicList") TopicListDTO topicList) throws IOException {
		logger.debug("createTopicModelling externalId:{}, username:{}", externalId, topicList.getUsername());

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {
			Taxonomy taxonomy = virtualEdition.getTaxonomy();
			taxonomy.createGeneratedCategories(topicList);

			model.addAttribute("virtualEdition", virtualEdition);
			return "virtual/taxonomy";
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/taxonomy/delete")
	@PreAuthorize("hasPermission(#virtualEditionExternalId, 'virtualedition.participant')")
	public String deleteTaxonomy(Model model, @RequestParam("virtualEditionExternalId") String virtualEditionExternalId,
			@RequestParam("taxonomyExternalId") String taxonomyExternalId) {
		Taxonomy taxonomy = FenixFramework.getDomainObject(taxonomyExternalId);
		if (taxonomy == null) {
			return "utils/pageNotFound";
		} else {
			Edition edition = taxonomy.getEdition();

			taxonomy.remove();

			model.addAttribute("virtualEdition", edition);
			return "virtual/taxonomy";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/taxonomy/{taxonomyId}")
	@PreAuthorize("hasPermission(#taxonomyId, 'taxonomy.public')")
	public String showTaxonomy(Model model, @PathVariable String taxonomyId) {
		Taxonomy taxonomy = FenixFramework.getDomainObject(taxonomyId);
		if (taxonomy == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("virtualEdition", taxonomy.getEdition());
			return "virtual/taxonomy";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/fraginter/{fragInterId}")
	@PreAuthorize("hasPermission(#fragInterId, 'fragInter.public')")
	public String showFragmentInterpretation(Model model, @PathVariable String fragInterId) {
		FragInter fragInter = FenixFramework.getDomainObject(fragInterId);
		if (fragInter == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("fragInter", fragInter);
			return "virtual/fragInter";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/create")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String createCategory(Model model, @RequestParam("externalId") String externalId,
			@RequestParam("name") String name) {
		VirtualEdition edition = FenixFramework.getDomainObject(externalId);
		List<String> errors = new ArrayList<String>();
		if (edition == null) {
			return "utils/pageNotFound";
		} else {
			try {
				edition.getTaxonomy().createCategory(name);
			} catch (LdoDDuplicateNameException ex) {
				errors.add("Já existe uma categoria com nome \"" + name + "\"");
			}

			if (errors.isEmpty()) {
				model.addAttribute("virtualEdition", edition);
				return "virtual/taxonomy";
			} else {
				model.addAttribute("categoryErrors", errors);
				model.addAttribute("virtualEdition", edition);
				return "virtual/taxonomy";
			}
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/update")
	@PreAuthorize("hasPermission(#categoryId, 'category.public')")
	public String updateCategoryName(Model model, @RequestParam("categoryId") String categoryId,
			@RequestParam("name") String name) {
		Category category = FenixFramework.getDomainObject(categoryId);
		List<String> errors = new ArrayList<String>();
		if (category == null) {
			return "utils/pageNotFound";
		} else {
			try {
				category.setName(name);
			} catch (LdoDDuplicateNameException ex) {
				errors.add("Já existe uma categoria com nome \"" + name + "\"");

			}
			model.addAttribute("errors", errors);
			model.addAttribute("category", category);
			return "virtual/category";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/delete")
	@PreAuthorize("hasPermission(#categoryId, 'category.public')")
	public String deleteCategory(Model model, @RequestParam("categoryId") String categoryId) {
		Category category = FenixFramework.getDomainObject(categoryId);
		if (category == null) {
			return "utils/pageNotFound";
		}

		VirtualEdition virtualEdition = category.getTaxonomy().getEdition();

		category.remove();

		model.addAttribute("virtualEdition", virtualEdition);
		return "virtual/taxonomy";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/category/{categoryId}")
	@PreAuthorize("hasPermission(#categoryId, 'category.public')")
	public String showCategory(Model model, @PathVariable String categoryId) {
		Category category = FenixFramework.getDomainObject(categoryId);
		if (category == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("category", category);
			return "virtual/category";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/mulop")
	@PreAuthorize("hasPermission(#taxonomyId, 'taxonomy.participant')")
	public String mergeCategories(Model model, @RequestParam("taxonomyId") String taxonomyId,
			@RequestParam("type") String type,
			@RequestParam(value = "categories[]", required = false) String categoriesIds[]) {
		Taxonomy taxonomy = FenixFramework.getDomainObject(taxonomyId);
		if (taxonomy == null) {
			return "utils/pageNotFound";
		}

		List<Category> categories = new ArrayList<Category>();
		for (String categoryId : categoriesIds) {
			Category category = FenixFramework.getDomainObject(categoryId);
			categories.add(category);
		}

		if (type.equals("merge") && categories.size() > 1) {
			Category category = taxonomy.merge(categories);
			model.addAttribute("category", category);
			return "virtual/category";
		}

		if (type.equals("delete") && categories.size() >= 1) {
			taxonomy.delete(categories);
			model.addAttribute("virtualEdition", taxonomy.getEdition());
			return "virtual/taxonomy";
		}

		model.addAttribute("virtualEdition", taxonomy.getEdition());
		return "virtual/taxonomy";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/category/extractForm")
	@PreAuthorize("hasPermission(#categoryId, 'category.participant')")
	public String extractForm(Model model, @RequestParam("categoryId") String categoryId) {
		Category category = FenixFramework.getDomainObject(categoryId);
		if (category == null) {
			return "utils/pageNotFound";
		}

		model.addAttribute("category", category);
		return "virtual/extractForm";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/extract")
	@PreAuthorize("hasPermission(#categoryId, 'category.participant')")
	public String extractCategory(Model model, @RequestParam("categoryId") String categoryId,
			@RequestParam(value = "tags[]", required = false) String tagsIds[]) {
		Category category = FenixFramework.getDomainObject(categoryId);
		if (category == null) {
			return "utils/pageNotFound";
		}

		if ((tagsIds == null) || (tagsIds.length == 0)) {
			model.addAttribute("category", category);
			return "virtual/category";
		}

		Set<Tag> tags = new HashSet<Tag>();
		for (String tagId : tagsIds) {
			Tag tag = FenixFramework.getDomainObject(tagId);
			tags.add(tag);
		}

		Category extractedCategory = category.getTaxonomy().extract(category, tags);

		model.addAttribute("category", extractedCategory);
		return "virtual/category";

	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/fraginter/{fragInterId}/tag/dissociate/{categoryId}")
	@PreAuthorize("hasPermission(#fragInterId, 'fragInter.participant')")
	public String dissociate(Model model, @PathVariable String fragInterId, @PathVariable String categoryId) {
		VirtualEditionInter inter = FenixFramework.getDomainObject(fragInterId);
		Category category = FenixFramework.getDomainObject(categoryId);
		if (inter == null || category == null) {
			return "utils/pageNotFound";
		}

		inter.dissociate(category);

		return "redirect:/fragments/fragment/inter/" + inter.getExternalId();

	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/tag/associateForm/{taxonomyId}/{interId}")
	@PreAuthorize("hasPermission(#interId, 'fragInter.participant')")
	public String associateTagForm(Model model, @PathVariable String taxonomyId, @PathVariable String interId) {
		Taxonomy taxonomy = FenixFramework.getDomainObject(taxonomyId);
		VirtualEditionInter inter = FenixFramework.getDomainObject(interId);
		if ((taxonomy == null) || (inter == null)) {
			return "utils/pageNotFound";
		}

		model.addAttribute("taxonomy", taxonomy);
		model.addAttribute("inter", inter);
		model.addAttribute("categories", inter.getNonAssignedCategories());
		return "virtual/associateForm";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/tag/associate")
	@PreAuthorize("hasPermission(#fragInterId, 'fragInter.participant')")
	public String associateCategory(Model model, @RequestParam("taxonomyId") String taxonomyId,
			@RequestParam("fragInterId") String fragInterId,
			@RequestParam(value = "categories[]", required = false) String[] categories) {
		logger.debug("associateCategory categories[]:{}",
				categories != null ? Arrays.stream(categories).collect(Collectors.joining(",")) : "null");

		Taxonomy taxonomy = FenixFramework.getDomainObject(taxonomyId);
		VirtualEditionInter inter = FenixFramework.getDomainObject(fragInterId);

		if (inter == null) {
			return "utils/pageNotFound";
		}

		if ((categories != null) && (categories.length > 0)) {
			inter.associate(LdoDUser.getAuthenticatedUser(), taxonomy,
					Arrays.stream(categories).collect(Collectors.toSet()));
		}

		return "redirect:/fragments/fragment/inter/" + inter.getExternalId();

	}

}
