package pt.ist.socialsoftware.edition.ldod.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.lucene.queryparser.classic.ParseException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.domain.Member.MemberRole;
import pt.ist.socialsoftware.edition.ldod.dto.EditionFragmentsDto;
import pt.ist.socialsoftware.edition.ldod.dto.EditionTranscriptionsDto;
import pt.ist.socialsoftware.edition.ldod.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.dto.TranscriptionDto;
import pt.ist.socialsoftware.edition.ldod.dto.VirtualEditionInterListDto;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.search.Indexer;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDCreateClassificationGameException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDCreateVirtualEditionException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateAcronymException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateNameException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDEditVirtualEditionException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDExceptionNonAuthorized;
import pt.ist.socialsoftware.edition.ldod.social.aware.AwareAnnotationFactory;
import pt.ist.socialsoftware.edition.ldod.topicmodeling.TopicModeler;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.ldod.utils.TopicListDTO;
import pt.ist.socialsoftware.edition.ldod.validator.ClassificationGameValidator;
import pt.ist.socialsoftware.edition.ldod.validator.VirtualEditionValidator;

@Controller
@SessionAttributes({ "ldoDSession" })
@RequestMapping("/virtualeditions")
public class VirtualEditionController {

	private static Logger logger = LoggerFactory.getLogger(VirtualEditionController.class);

	@ModelAttribute("ldoDSession")
	public LdoDSession getLdoDSession() {
		return LdoDSession.getLdoDSession();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String listVirtualEdition(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession) {

		model.addAttribute("ldod", LdoD.getInstance());
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
			virtualEdition = LdoD.getInstance().createVirtualEdition(LdoDUser.getAuthenticatedUser(),
					VirtualEdition.ACRONYM_PREFIX + acronym, title, date, pub, usedEdition);

		} catch (LdoDDuplicateAcronymException ex) {
			errors.add("virtualedition.acronym.duplicate");
			throw new LdoDCreateVirtualEditionException(errors, acronym, title, pub,
					LdoD.getInstance().getVirtualEditions4User(LdoDUser.getAuthenticatedUser(), ldoDSession),
					LdoDUser.getAuthenticatedUser());
		}

		return "redirect:/virtualeditions";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/delete")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String deleteVirtualEdition(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("externalId") String externalId) {
		logger.debug("deleteVirtualEdition externalId:{}", externalId);
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "redirect:/error";
		} else {

			String acronym = virtualEdition.getAcronym();

			virtualEdition.remove();

			if (ldoDSession.hasSelectedVE(acronym)) {
				ldoDSession.removeSelectedVE(acronym);
			}

			return "redirect:/virtualeditions";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/manage/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String manageVirtualEdition(Model model, @PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		logger.debug("manageVirtualEdition externalId:{}", externalId);
		if (virtualEdition == null) {
			return "redirect:/error";
		} else {
			model.addAttribute("virtualEdition", virtualEdition);
			model.addAttribute("user", LdoDUser.getAuthenticatedUser());

			List<String> countriesList = new ArrayList<String>();
			countriesList.add("Portugal");
			countriesList.add("Brazil");
			countriesList.add("Spain");
			countriesList.add("United Kingdom");
			countriesList.add("United States");
			countriesList.add("Lebanon");
			countriesList.add("Angola");
			countriesList.add("Mozambique");
			model.addAttribute("countriesList", countriesList);
			return "virtual/manage";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/editForm/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String showEditVirtualEdition(Model model, @PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "redirect:/error";
		}
		logger.debug("showEditVirtualEditionn externalId:{}, acronym:{}, title:{}, pub:{}, fraginters.size:{}",
				externalId, virtualEdition.getAcronym(), virtualEdition.getTitle(), virtualEdition.getPub(),
				virtualEdition.getIntersSet().size());
		model.addAttribute("virtualEdition", virtualEdition);
		model.addAttribute("externalId", virtualEdition.getExternalId());
		model.addAttribute("acronym", virtualEdition.getShortAcronym());
		model.addAttribute("title", virtualEdition.getTitle());
		model.addAttribute("date", virtualEdition.getDate().toString("dd-MM-yyyy"));
		model.addAttribute("pub", virtualEdition.getPub());
		return "virtual/edition";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/edit/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
	public String editVirtualEdition(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String externalId, @RequestParam("acronym") String acronym,
			@RequestParam("title") String title, @RequestParam("synopsis") String synopsis,
			@RequestParam("pub") boolean pub, @RequestParam("management") boolean management,
			@RequestParam("vocabulary") boolean vocabulary, @RequestParam("annotation") boolean annotation,
			@RequestParam("mediasource") String mediaSource, @RequestParam("begindate") String beginDate,
			@RequestParam("enddate") String endDate, @RequestParam("geolocation") String geoLocation,
			@RequestParam("frequency") String frequency) {

		logger.info("mediaSource:{}", mediaSource);
		logger.info("beginDate:{}", beginDate);
		logger.info("endDate:{}", endDate);
		logger.info("geoLocation:{}", geoLocation);
		logger.info("frequency:{}", frequency);

		logger.debug(
				"editVirtualEdition externalId:{}, acronym:{}, title:{}, pub:{}, management:{}, vocabulary:{}, annotation:{}",
				externalId, acronym, title, pub, management, vocabulary, annotation);

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "redirect:/error";
		}

		title = title.trim();
		acronym = acronym.trim();

		VirtualEditionValidator validator = new VirtualEditionValidator(virtualEdition, acronym, title);
		validator.validate();

		List<String> errors = validator.getErrors();

		if (errors.size() > 0) {
			throw new LdoDEditVirtualEditionException(errors, virtualEdition, acronym, title, pub);
		}

		try {
			virtualEdition.edit(VirtualEdition.ACRONYM_PREFIX + acronym, title, synopsis, pub, management, vocabulary,
					annotation, mediaSource, beginDate, endDate, geoLocation, frequency);
		} catch (LdoDDuplicateAcronymException ex) {
			errors.add("virtualedition.acronym.duplicate");
			throw new LdoDEditVirtualEditionException(errors, virtualEdition, acronym, title, pub);
		}

		AwareAnnotationFactory awareFactory = new AwareAnnotationFactory();
		if (virtualEdition.isSAVE()) {
			awareFactory.searchForAwareAnnotations(virtualEdition);
		}
		// this virtual edition is not SAVE anymore, therefore we have to remove all the
		// aware annotations
		else {
			for (VirtualEditionInter inter : virtualEdition.getAllDepthVirtualEditionInters()) {
				awareFactory.removeAllAwareAnnotationsFromVEInter(inter);
			}
		}

		return "redirect:/virtualeditions/restricted/manage/" + externalId;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/reorder/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String reorderVirtualEdition(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String externalId, @RequestParam("fraginters") String fraginters) {
		logger.debug("reorderVirtualEdition externalId:{}, fraginters:{}", externalId, fraginters);

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "redirect:/error";
		}

		virtualEdition.updateVirtualEditionInters(fraginters);

		return "redirect:/virtualeditions";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/toggleselection")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.public')")
	public String toggleSelectedVirtualEdition(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("externalId") String externalId) {
		final VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);

		if (virtualEdition == null) {
			return "redirect:/error";
		}

		LdoDUser user = LdoDUser.getAuthenticatedUser();

		ldoDSession.toggleSelectedVirtualEdition(user, virtualEdition);

		return "redirect:/virtualeditions";
	}

	@GetMapping("/public")
	public @ResponseBody ResponseEntity<List<VirtualEditionInterListDto>> getEditions() {
		logger.debug("getEditions");
		
		List<VirtualEditionInterListDto> result =
				Stream.concat(LdoD.getInstance().getExpertEditionsSet().stream().map(expertEdition -> new VirtualEditionInterListDto(expertEdition)),
						LdoD.getInstance().getVirtualEditionsSet().stream().filter(virtualEdition -> virtualEdition.getPub())
								.map(virtualEdition -> new VirtualEditionInterListDto(virtualEdition, false)))
				.collect(Collectors.toList());

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}/fragments")
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
	public @ResponseBody ResponseEntity<EditionFragmentsDto> getFragments(@PathVariable String acronym) {
		logger.debug("getFragments acronym:{}", acronym);

		Stream<FragInter> inters;
		EditionFragmentsDto editionFragments = new EditionFragmentsDto();
		ExpertEdition expertEdition = LdoD.getInstance().getExpertEdition(acronym);
		if (expertEdition != null) {
			editionFragments.setCategories(new ArrayList<>());

			List<FragmentDto> fragments = new ArrayList<>();

			expertEdition.getExpertEditionIntersSet().stream().sorted(Comparator.comparing(FragInter::getTitle))
					.forEach(inter -> {
						PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);
						writer.write(false);
						FragmentDto fragment = new FragmentDto(inter, writer.getTranscription());

						fragments.add(fragment);
					});

			editionFragments.setFragments(fragments);

			return new ResponseEntity<>(editionFragments, HttpStatus.OK);
		} else {
			VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(acronym);
			if (virtualEdition != null) {
				editionFragments.setCategories(virtualEdition.getTaxonomy().getSortedCategories().stream()
						.map(c -> c.getName()).collect(Collectors.toList()));

				List<FragmentDto> fragments = new ArrayList<>();

				virtualEdition.getAllDepthVirtualEditionInters().stream().sorted(Comparator.comparing(FragInter::getTitle))
						.forEach(inter -> {
							PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getLastUsed());
							writer.write(false);
							FragmentDto fragment = new FragmentDto(inter, writer.getTranscription());

							fragments.add(fragment);
						});

				editionFragments.setFragments(fragments);

				return new ResponseEntity<>(editionFragments, HttpStatus.OK);
			}

				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}/interId/{interId}/tfidf")
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
	public @ResponseBody ResponseEntity<List<Entry<String, Double>>> getInterIdTFIDFTerms(
			@PathVariable String acronym, @PathVariable String interId) throws IOException, ParseException {
		logger.debug("getInterIdTFIDFTerms acronym:{}", acronym);

		FragInter fragInter = FenixFramework.getDomainObject(interId);

		if (fragInter == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<Entry<String, Double>> result = Indexer.getIndexer()
				.getTermFrequency(fragInter.getLastUsed()).entrySet().stream()
				.sorted(Comparator.comparing(Map.Entry::getValue)).collect(Collectors.toList());

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}/transcriptions")
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
	public @ResponseBody ResponseEntity<EditionTranscriptionsDto> getTranscriptions(Model model,
			@PathVariable String acronym) {
		logger.debug("getTranscriptions acronym:{}", acronym);

		VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(acronym);

		if (virtualEdition == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			String intersFilesPath = PropertiesManager.getProperties().getProperty("inters.dir");
			List<TranscriptionDto> transcriptions = new ArrayList<>();

			virtualEdition.getIntersSet().stream().sorted(Comparator.comparing(FragInter::getTitle)).forEach(inter -> {
				FragInter lastInter = inter.getLastUsed();
				String title = lastInter.getTitle();
				String text;
				try {
					text = new String(
							Files.readAllBytes(Paths.get(intersFilesPath + lastInter.getExternalId() + ".txt")));
				} catch (IOException e) {
					throw new LdoDException("VirtualEditionController::getTranscriptions IOException");
				}

				transcriptions.add(new TranscriptionDto(title, text));
			});

			return new ResponseEntity<>(new EditionTranscriptionsDto(transcriptions), HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}/{category}/transcriptions")
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
	public @ResponseBody ResponseEntity<EditionTranscriptionsDto> getTranscriptionsTag(Model model,
			@PathVariable String acronym, @PathVariable String category) {
		logger.debug("getTranscriptionsTag acronym:{}, category:{}", acronym, category);

		VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(acronym);

		if (virtualEdition == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			List<TranscriptionDto> transcriptions = new ArrayList<>();

			Category categoryObject = virtualEdition.getTaxonomy().getCategory(category);

			if (categoryObject == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			for (Tag tag : categoryObject.getTagSet()) {
				if (tag.getAnnotation() != null) {
					String title = tag.getInter().getTitle();
					String text = tag.getAnnotation().getQuote();

					transcriptions.add(new TranscriptionDto(title, text));
				}
			}

			return new ResponseEntity<>(new EditionTranscriptionsDto(transcriptions), HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}/participants")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String showParticipants(Model model, @PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "redirect:/error";
		} else {
			List<String> errors = (List<String>) model.asMap().get("errors");
			String username = (String) model.asMap().get("username");
			model.addAttribute("errors", errors);
			model.addAttribute("username", username);
			model.addAttribute("virtualEdition", virtualEdition);
			return "virtual/participants";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/submit")
	public String submitParticipation(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		LdoDUser user = LdoDUser.getAuthenticatedUser();

		if (virtualEdition == null || user == null) {
			return "redirect:/error";
		} else {
			virtualEdition.addMember(user, MemberRole.MEMBER, false);
			return "redirect:/virtualeditions";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/cancel")
	public String cancelParticipationSubmission(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		LdoDUser user = LdoDUser.getAuthenticatedUser();

		if (virtualEdition == null || user == null) {
			return "redirect:/error";
		} else {
			virtualEdition.cancelParticipationSubmission(user);
			return "redirect:/virtualeditions";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/approve")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
	public String approveParticipant(RedirectAttributes redirectAttributes,
			@PathVariable("externalId") String externalId, @RequestParam("username") String username) {

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "redirect:/error";
		}

		LdoD ldoD = LdoD.getInstance();
		LdoDUser user = ldoD.getUser(username);
		if (user == null) {
			List<String> errors = new ArrayList<>();
			errors.add("user.unknown");
			redirectAttributes.addFlashAttribute("errors", errors);
			redirectAttributes.addFlashAttribute("username", username);
			return "redirect:/virtualeditions/restricted/" + externalId + "/participants";
		} else {
			virtualEdition.addApprove(user);
			return "redirect:/virtualeditions/restricted/" + externalId + "/participants";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/add")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
	public String addParticipant(RedirectAttributes redirectAttributes, @PathVariable("externalId") String externalId,
			@RequestParam("username") String username) {

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "redirect:/error";
		}

		LdoD ldoD = LdoD.getInstance();
		LdoDUser user = ldoD.getUser(username);
		if (user == null) {
			List<String> errors = new ArrayList<>();
			errors.add("user.unknown");
			redirectAttributes.addFlashAttribute("errors", errors);
			redirectAttributes.addFlashAttribute("username", username);
			return "redirect:/virtualeditions/restricted/" + externalId + "/participants";
		} else {
			virtualEdition.addMember(user, MemberRole.MEMBER, true);
			return "redirect:/virtualeditions/restricted/" + externalId + "/participants";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/role")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
	public String switchRole(Model model, @PathVariable("externalId") String externalId,
			@RequestParam("username") String username) {

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "redirect:/error";
		}

		LdoD ldoD = LdoD.getInstance();
		LdoDUser user = ldoD.getUser(username);

		if (!virtualEdition.canSwitchRole(LdoDUser.getAuthenticatedUser(), user)) {
			throw new LdoDExceptionNonAuthorized();
		}

		virtualEdition.switchRole(user);

		return "redirect:/virtualeditions/restricted/" + externalId + "/participants";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/remove")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String removeParticipant(RedirectAttributes redirectAttributes,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession, @PathVariable("externalId") String externalId,
			@RequestParam("userId") String userId) {
		logger.debug("removeParticipant userId:{}", userId);

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		LdoDUser user = FenixFramework.getDomainObject(userId);

		if (virtualEdition == null || user == null) {
			return "redirect:/error";
		}

		if (!virtualEdition.canRemoveMember(LdoDUser.getAuthenticatedUser(), user)) {
			throw new LdoDExceptionNonAuthorized();
		}

		LdoDUser admin = null;
		if (virtualEdition.getAdminSet().size() == 1) {
			admin = virtualEdition.getAdminSet().iterator().next();
		}

		if (admin != null && admin == user) {
			List<String> errors = new ArrayList<>();
			errors.add("user.one");
			redirectAttributes.addFlashAttribute("errors", errors);
			return "redirect:/virtualeditions/restricted/" + externalId + "/participants";

		} else {
			virtualEdition.removeMember(user);

			if (user == LdoDUser.getAuthenticatedUser()) {
				return "redirect:/virtualeditions";
			} else {
				return "redirect:/virtualeditions/restricted/" + externalId + "/participants";
			}
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/addinter/{veId}/{interId}")
	@PreAuthorize("hasPermission(#veId, 'virtualedition.participant')")
	public String addInter(Model model, @PathVariable String veId, @PathVariable String interId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(veId);
		FragInter inter = FenixFramework.getDomainObject(interId);
		if (virtualEdition == null || inter == null) {
			return "redirect:/error";
		}

		VirtualEditionInter addInter = virtualEdition.createVirtualEditionInter(inter,
				virtualEdition.getMaxFragNumber() + 1);

		if (addInter == null) {
			return "redirect:/error";
		} else {
			return "redirect:/fragments/fragment/inter/" + interId;
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}/classificationGame")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String classificationGame(Model model, @PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "redirect:/error";
		} else {
			model.addAttribute("virtualEdition", virtualEdition);
			model.addAttribute("games", virtualEdition.getClassificationGameSet().stream()
					.sorted((g1, g2) -> g1.getDateTime().compareTo(g2.getDateTime())).collect(Collectors.toList()));
			return "virtual/classificationGame";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}/classificationGame/create")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
	public String createClassificationGameForm(Model model, @PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "redirect:/error";
		} else {
			model.addAttribute("virtualEdition", virtualEdition);
			return "virtual/createClassificationGame";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/classificationGame/create")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
	public String createClassificationGame(Model model, @PathVariable String externalId,
			@RequestParam("description") String description, @RequestParam("date") String date,
			@RequestParam("interExternalId") String interExternalId) {
		logger.debug("createClassificationGame description: {}, date: {}, inter:{}", description, date,
				interExternalId);
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		VirtualEditionInter inter = FenixFramework.getDomainObject(interExternalId);
		if (virtualEdition == null) {
			return "redirect:/error";
		} else {
			ClassificationGameValidator validator = new ClassificationGameValidator(description, interExternalId);
			validator.validate();

			List<String> errors = validator.getErrors();
			if (errors.size() > 0) {
				throw new LdoDCreateClassificationGameException(errors, description, date, interExternalId,
						virtualEdition);
			}
			virtualEdition.createClassificationGame(description,
					DateTime.parse(date, DateTimeFormat.forPattern("dd/MM/yyyy HH:mm")), inter,
					LdoDUser.getAuthenticatedUser());

			return "redirect:/virtualeditions/restricted/" + externalId + "/classificationGame";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/classificationGame/{gameExternalId}/remove")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
	public String removeClassificationGame(Model model, @PathVariable String externalId,
			@PathVariable String gameExternalId) {
		logger.debug("removeClassificationGame externalId: {}, gameExternalId: {}", externalId, gameExternalId);
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		ClassificationGame game = FenixFramework.getDomainObject(gameExternalId);

		if (virtualEdition == null || game == null) {
			return "redirect:/error";
		} else {
			if (game.canBeRemoved()) {
				game.remove();
			} else {
				throw new LdoDException("Cannot remove finished game");
			}
			return "redirect:/virtualeditions/restricted/" + externalId + "/classificationGame";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{externalId}/classificationGame/{gameId}")
	public String getClassificationGameContent(Model model, @PathVariable String externalId,
			@PathVariable String gameId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		ClassificationGame game = FenixFramework.getDomainObject(gameId);
		if (virtualEdition == null || game == null) {
			return "redirect:/error";
		} else {
			model.addAttribute("virtualEdition", virtualEdition);
			model.addAttribute("game", game);
			model.addAttribute("participants",
					game.getClassificationGameParticipantSet().stream()
							.sorted(Comparator.comparing(ClassificationGameParticipant::getScore).reversed())
							.collect(Collectors.toList()));
			return "virtual/classificationGameContent";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}/taxonomy")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String taxonomy(Model model, @PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "redirect:/error";
		} else {
			List<String> errors = (List<String>) model.asMap().get("categoryErrors");
			model.addAttribute("categoryErrors", errors);
			model.addAttribute("virtualEdition", virtualEdition);
			return "virtual/taxonomy";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/taxonomy/edit")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
	public String editTaxonomy(Model model, @PathVariable("externalId") String externalId,
			@RequestParam("management") boolean management, @RequestParam("vocabulary") boolean vocabulary,
			@RequestParam("annotation") boolean annotation) {
		logger.debug("editTaxonomy externalId:{}, management:{}, vocabulary:{}, annotation:{}", externalId, management,
				vocabulary, annotation);

		VirtualEdition edition = FenixFramework.getDomainObject(externalId);
		if (edition == null) {
			return "redirect:/error";
		} else {
			edition.getTaxonomy().edit(management, vocabulary, annotation);
			return "redirect:/virtualeditions/restricted/" + externalId + "/taxonomy";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}/taxonomy/generateTopics")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.taxonomy')")
	public String generateTopicModelling(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String externalId, @RequestParam("numTopics") int numTopics,
			@RequestParam("numWords") int numWords, @RequestParam("thresholdCategories") int thresholdCategories,
			@RequestParam("numIterations") int numIterations) throws IOException {
		logger.debug(
				"generateTopicModelling externalId:{}, numTopics:{}, numWords:{}, thresholdCategories:{}, numIterations:{}",
				externalId, numTopics, numWords, thresholdCategories, numIterations);

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "redirect:/error";
		} else {
			LdoDUserDetails userDetails = (LdoDUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();

			List<String> topicErrors = new ArrayList<>();
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

	// necessary to allow a deep @ModelAttribute("topicList") in
	// createTopicModelling
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.setAutoGrowCollectionLimit(1000);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/taxonomy/createTopics")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.taxonomy')")
	public String createTopicModelling(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String externalId, @ModelAttribute("topicList") TopicListDTO topicList) throws IOException {
		logger.debug("createTopicModelling externalId:{}, username:{}", externalId, topicList.getUsername());

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "redirect:/error";
		} else {
			Taxonomy taxonomy = virtualEdition.getTaxonomy();
			taxonomy.createGeneratedCategories(topicList);

			return "redirect:/virtualeditions/restricted/" + externalId + "/taxonomy";
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/taxonomy/clean")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.taxonomy')")
	public String deleteTaxonomy(Model model, @PathVariable("externalId") String externalId,
			@RequestParam("taxonomyExternalId") String taxonomyExternalId) {
		Taxonomy taxonomy = FenixFramework.getDomainObject(taxonomyExternalId);
		if (taxonomy == null) {
			return "redirect:/error";
		} else {
			VirtualEdition edition = taxonomy.getEdition();
			taxonomy.remove();
			edition.setTaxonomy(new Taxonomy());

			return "redirect:/virtualeditions/restricted/" + externalId + "/taxonomy";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/create")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.taxonomy')")
	public String createCategory(RedirectAttributes redirectAttributes, @RequestParam("externalId") String externalId,
			@RequestParam("name") String name) {
		VirtualEdition edition = FenixFramework.getDomainObject(externalId);
		List<String> errors = new ArrayList<>();
		if (edition == null) {
			return "redirect:/error";
		} else {
			try {
				edition.getTaxonomy().createCategory(name);
			} catch (LdoDDuplicateNameException ex) {
				errors.add("general.category.exists");
			}

			if (errors.isEmpty()) {
				return "redirect:/virtualeditions/restricted/" + externalId + "/taxonomy";
			} else {
				redirectAttributes.addFlashAttribute("categoryErrors", errors);
				return "redirect:/virtualeditions/restricted/" + externalId + "/taxonomy";
			}
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/update")
	@PreAuthorize("hasPermission(#categoryId, 'category.taxonomy')")
	public String updateCategoryName(RedirectAttributes redirectAttributes,
			@RequestParam("categoryId") String categoryId, @RequestParam("name") String name) {
		Category category = FenixFramework.getDomainObject(categoryId);
		List<String> errors = new ArrayList<>();
		if (category == null) {
			return "redirect:/error";
		} else {
			try {
				category.setName(name);
			} catch (LdoDDuplicateNameException ex) {
				errors.add("general.category.exists");
			}
			redirectAttributes.addFlashAttribute("errors", errors);
			return "redirect:/virtualeditions/restricted/category/" + categoryId;
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/delete")
	@PreAuthorize("hasPermission(#categoryId, 'category.taxonomy')")
	public String deleteCategory(Model model, @RequestParam("categoryId") String categoryId) {
		Category category = FenixFramework.getDomainObject(categoryId);
		if (category == null) {
			return "redirect:/error";
		}
		VirtualEdition virtualEdition = category.getTaxonomy().getEdition();
		category.remove();

		return "redirect:/virtualeditions/restricted/" + virtualEdition.getExternalId() + "/taxonomy";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/category/{categoryId}")
	@PreAuthorize("hasPermission(#categoryId, 'category.participant')")
	public String showCategory(Model model, @PathVariable String categoryId) {
		Category category = FenixFramework.getDomainObject(categoryId);
		if (category == null) {
			return "redirect:/error";
		} else {
			List<String> errors = (List<String>) model.asMap().get("errors");
			model.addAttribute("errors", errors);
			model.addAttribute("category", category);
			return "virtual/category";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/mulop")
	@PreAuthorize("hasPermission(#taxonomyId, 'taxonomy.taxonomy')")
	public String mergeCategories(Model model, @RequestParam("taxonomyId") String taxonomyId,
			@RequestParam("type") String type,
			@RequestParam(value = "categories[]", required = false) String categoriesIds[]) {
		Taxonomy taxonomy = FenixFramework.getDomainObject(taxonomyId);
		if (taxonomy == null) {
			return "redirect:/error";
		}

		if (categoriesIds == null) {
			return "redirect:/virtualeditions/restricted/" + taxonomy.getEdition().getExternalId() + "/taxonomy";
		}

		List<Category> categories = new ArrayList<>();
		for (String categoryId : categoriesIds) {
			Category category = FenixFramework.getDomainObject(categoryId);
			categories.add(category);
		}

		if (type.equals("merge") && categories.size() > 1) {
			Category category = taxonomy.merge(categories);
			return "redirect:/virtualeditions/restricted/category/" + category.getExternalId();
		}

		if (type.equals("delete") && categories.size() >= 1) {
			taxonomy.delete(categories);
			return "redirect:/virtualeditions/restricted/" + taxonomy.getEdition().getExternalId() + "/taxonomy";
		}

		return "redirect:/virtualeditions/restricted/" + taxonomy.getEdition().getExternalId() + "/taxonomy";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/category/extractForm")
	@PreAuthorize("hasPermission(#categoryId, 'category.taxonomy')")
	public String extractForm(Model model, @RequestParam("categoryId") String categoryId) {
		Category category = FenixFramework.getDomainObject(categoryId);
		if (category == null) {
			return "redirect:/error";
		}

		model.addAttribute("category", category);
		return "virtual/extractForm";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/extract")
	@PreAuthorize("hasPermission(#categoryId, 'category.taxonomy')")
	public String extractCategory(Model model, @RequestParam("categoryId") String categoryId,
			@RequestParam(value = "inters[]", required = false) String interIds[]) {
		Category category = FenixFramework.getDomainObject(categoryId);
		if (category == null) {
			return "redirect:/error";
		}

		if (interIds == null || interIds.length == 0) {
			return "redirect:/virtualeditions/restricted/category/" + category.getExternalId();
		}

		Set<VirtualEditionInter> inters = new HashSet<>();
		for (String interId : interIds) {
			VirtualEditionInter inter = FenixFramework.getDomainObject(interId);
			inters.add(inter);
		}

		Category extractedCategory = category.getTaxonomy().extract(category, inters);

		return "redirect:/virtualeditions/restricted/category/" + extractedCategory.getExternalId();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/fraginter/{fragInterId}")
	@PreAuthorize("hasPermission(#fragInterId, 'fragInter.participant')")
	public String showFragmentInterpretation(Model model, @PathVariable String fragInterId) {
		FragInter fragInter = FenixFramework.getDomainObject(fragInterId);
		if (fragInter == null) {
			return "redirect:/error";
		} else {
			model.addAttribute("fragInter", fragInter);
			return "virtual/fragInter";
		}
	}

	// no access control because the only tags removed are from the logged user
	@RequestMapping(method = RequestMethod.GET, value = "/restricted/fraginter/{fragInterId}/tag/dissociate/{categoryId}")
	public String dissociate(Model model, @PathVariable String fragInterId, @PathVariable String categoryId) {
		VirtualEditionInter inter = FenixFramework.getDomainObject(fragInterId);

		Category category = FenixFramework.getDomainObject(categoryId);
		if (inter == null || category == null) {
			return "redirect:/error";
		}

		LdoDUser user = LdoDUser.getAuthenticatedUser();

		inter.dissociate(user, category);

		return "redirect:/fragments/fragment/inter/" + inter.getExternalId();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/tag/associate")
	@PreAuthorize("hasPermission(#fragInterId, 'fragInter.annotation')")
	public String associateCategory(Model model, @RequestParam("fragInterId") String fragInterId,
			@RequestParam(value = "categories[]", required = false) String[] categories) {
		logger.debug("associateCategory categories[]:{}",
				categories != null ? Arrays.stream(categories).collect(Collectors.joining(",")) : "null");

		VirtualEditionInter inter = FenixFramework.getDomainObject(fragInterId);

		if (inter == null) {
			return "redirect:/error";
		}

		if (categories != null && categories.length > 0) {
			inter.associate(LdoDUser.getAuthenticatedUser(), Arrays.stream(categories).collect(Collectors.toSet()));
		}

		return "redirect:/fragments/fragment/inter/" + inter.getExternalId();

	}
}
