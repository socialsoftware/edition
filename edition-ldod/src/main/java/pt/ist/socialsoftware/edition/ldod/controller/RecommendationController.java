package pt.ist.socialsoftware.edition.ldod.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.api.ui.UiInterface;
import pt.ist.socialsoftware.edition.ldod.domain.Edition.EditionType;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.ldod.domain.Section;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.dto.InterDistancePairDto;
import pt.ist.socialsoftware.edition.ldod.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.ldod.dto.WeightsDto;
import pt.ist.socialsoftware.edition.ldod.recommendation.dto.RecommendVirtualEditionParam;
import pt.ist.socialsoftware.edition.ldod.recommendation.dto.SectionVirtualEditionParam;
import pt.ist.socialsoftware.edition.ldod.recommendation.dto.VirtualEditionWithSectionsDTO;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDCreateVirtualEditionException;
import pt.ist.socialsoftware.edition.ldod.validator.VirtualEditionValidator;

@Controller
@RequestMapping("/recommendation")
public class RecommendationController {
	private static Logger logger = LoggerFactory.getLogger(RecommendationController.class);

	/*
	 * Sets all the empty boxes to null instead of the empty string ""
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String presentEditionWithRecommendation(Model model, @PathVariable String externalId) {
		// logger.debug("presentEditionWithRecommendation");

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "redirect:/error";
		} else {
			// logger.debug("presentEditionWithRecommendation sections: {}",
			// virtualEdition.getSectionsSet().stream().map(s ->
			// s.print(1)).collect(Collectors.joining()));

			RecommendationWeights recommendationWeights = LdoDUser.getAuthenticatedUser()
					.getRecommendationWeights(virtualEdition);

			recommendationWeights.setWeightsZero();

			// logger.debug("presentEditionWithRecommendation sections: {}",
			// virtualEdition.getSectionsSet().stream().map(s ->
			// s.print(1)).collect(Collectors.joining()));

			model.addAttribute("edition", virtualEdition);
			model.addAttribute("taxonomyWeight", 0.0);
			model.addAttribute("heteronymWeight", 0.0);
			model.addAttribute("dateWeight", 0.0);
			model.addAttribute("textWeight", 0.0);

			if (!virtualEdition.getAllDepthVirtualEditionInters().isEmpty()) {
				VirtualEditionInter inter = virtualEdition.getAllDepthVirtualEditionInters().get(0);

				List<VirtualEditionInter> recommendedEdition = virtualEdition.generateRecommendation(inter,
						recommendationWeights);

				model.addAttribute("inters", recommendedEdition);
				model.addAttribute("selected", inter.getExternalId());
			}

			return "recommendation/tableOfContents";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{externalId}/intersByDistance")
	@PreAuthorize("hasPermission(#externalId, 'fragInter.public')")
	public @ResponseBody ResponseEntity<InterIdDistancePairDto[]> getIntersByDistance(Model model,
			@PathVariable String externalId, @RequestBody WeightsDto weights) {
		logger.debug("getIntersByDistance externalId: {}, weights: {}", externalId,
				"(" + weights.getHeteronymWeight() + "," + weights.getTextWeight() + "," + weights.getDateWeight() + ","
						+ weights.getTaxonomyWeight() + ")");

		VirtualEditionInter virtualEditionInter = FenixFramework.getDomainObject(externalId);
		if (virtualEditionInter == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<InterDistancePairDto> intersByDistance = virtualEditionInter.getVirtualEdition()
				.getIntersByDistance(virtualEditionInter, weights);

		return new ResponseEntity<InterIdDistancePairDto[]>(intersByDistance.stream()
				.map(p -> new InterIdDistancePairDto(p.getInter().getExternalId(), p.getDistance()))
				.toArray(size -> new InterIdDistancePairDto[size]), HttpStatus.OK);
	}

	@RequestMapping(value = "/linear", method = RequestMethod.POST, headers = {
			"Content-type=application/json;charset=UTF-8" })
	public String setLinearVirtualEdition(Model model, @RequestBody RecommendVirtualEditionParam params) {
		logger.debug("setLinearVirtualEdition acronym:{}, id:{}, properties:{}", params.getAcronym(), params.getId(),
				params.getProperties().stream()
						.map(p -> p.getClass().getName().substring(p.getClass().getName().lastIndexOf(".") + 1) + " "
								+ p.getWeight())
						.collect(Collectors.joining(";")));

		VirtualEdition virtualEdition = (VirtualEdition) LdoD.getInstance().getEdition(params.getAcronym());

		LdoDUser user = LdoDUser.getAuthenticatedUser();
		RecommendationWeights recommendationWeights = user.getRecommendationWeights(virtualEdition);
		recommendationWeights.setWeights(params.getProperties());

		if (params.getId() != null && !params.getId().equals("")) {
			VirtualEditionInter inter = FenixFramework.getDomainObject(params.getId());

			List<VirtualEditionInter> recommendedEdition = virtualEdition.generateRecommendation(inter,
					recommendationWeights);

			model.addAttribute("inters", recommendedEdition);
			model.addAttribute("selected", params.getId());
		}
		model.addAttribute("edition", virtualEdition);
		model.addAttribute("uiInterface", new UiInterface());

		return "recommendation/virtualTable";
	}

	@RequestMapping(value = "/linear/save", method = RequestMethod.POST)
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.participant')")
	public String saveLinearVirtualEdition(Model model, @RequestParam("acronym") String acronym,
			@RequestParam(value = "inter[]", required = false) String[] inters) {
		// logger.debug("saveLinearVirtualEdition");

		LdoD ldod = LdoD.getInstance();
		VirtualEdition virtualEdition = (VirtualEdition) ldod.getEdition(acronym);
		if (inters != null && virtualEdition.getSourceType().equals(EditionType.VIRTUAL)) {
			Section section = virtualEdition.createSection(Section.DEFAULT);
			VirtualEditionInter VirtualEditionInter;
			int i = 0;
			for (String externalId : inters) {
				VirtualEditionInter = FenixFramework.getDomainObject(externalId);
				section.addVirtualEditionInter(VirtualEditionInter, ++i);
			}
			virtualEdition.clearEmptySections();
		}

		return "redirect:/recommendation/restricted/" + virtualEdition.getExternalId();
	}

	@RequestMapping(value = "/linear/create", method = RequestMethod.POST)
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.participant')")
	public String createLinearVirtualEdition(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("acronym") String acronym, @RequestParam("title") String title,
			@RequestParam("pub") boolean pub, @RequestParam("inter[]") String[] inters) {

		title = title == null ? "" : title.trim();
		acronym = acronym == null ? "" : acronym.trim();

		VirtualEditionValidator validator = new VirtualEditionValidator(null, acronym, title);
		validator.validate();

		List<String> errors = validator.getErrors();

		if (errors.size() > 0) {
			throw new LdoDCreateVirtualEditionException(errors, acronym, title, pub,
					LdoD.getInstance().getVirtualEditions4User(LdoDUser.getAuthenticatedUser(), ldoDSession),
					LdoDUser.getAuthenticatedUser());
		}

		VirtualEdition virtualEdition = LdoD.getInstance().createVirtualEdition(LdoDUser.getAuthenticatedUser(),
				VirtualEdition.ACRONYM_PREFIX + acronym, title, new LocalDate(), pub, null);
		VirtualEditionInter virtualInter;
		for (int i = 0; i < inters.length; i++) {
			virtualInter = FenixFramework.getDomainObject(inters[i]);
			virtualEdition.createVirtualEditionInter(virtualInter, i + 1);
		}
		return "redirect:/recommendation/restricted/" + virtualEdition.getExternalId();
	}

	// THE FOLLOWING CONTROLLERS ARE NOT CURRENTLY BEING USED BECAUSE CLUSTERING
	// IS NOT SUPPORTED

	@RequestMapping(value = "/section", method = RequestMethod.POST, headers = {
			"Content-type=application/json;charset=UTF-8" })
	public String setSectionVirtualEdition(Model model, @RequestBody SectionVirtualEditionParam params) {
		// logger.debug("setSectionVirtualEdition properties:{}",
		// params.getPropertiesWithLevel().stream()
		// .map(p -> "(" + p.getProperty().getWeight() + "," + p.getLevel() +
		// ")").collect(Collectors.joining()));

		// VirtualEdition virtualEdition = (VirtualEdition)
		// LdoD.getInstance().getEdition(params.getAcronym());
		// VirtualEditionInter inter = FenixFramework.getDomainObject(params.getId());
		//
		// List<VirtualEditionInter> inters =
		// virtualEdition.getAllDepthVirtualEditionInters();
		// VSMVirtualEditionInterRecommender recommender = new
		// VSMVirtualEditionInterRecommender();
		// Map<Integer, Collection<Property>> map = new HashMap<>();
		// LdoDUser user = LdoDUser.getAuthenticatedUser();
		// RecommendationWeights recommendationWeights =
		// user.getRecommendationWeights(virtualEdition);
		// recommendationWeights.setWeights(params.getProperties());
		// for (PropertyWithLevel property : params.getPropertiesWithLevel()) {
		// if (property.getProperty().getWeight() > 0) {
		// if (!map.containsKey(property.getLevel())) {
		// map.put(property.getLevel(), new ArrayList<Property>());
		// }
		// map.get(property.getLevel()).add(property.getProperty());
		// }
		// }
		// Cluster cluster = recommender.getCluster(inter, inters, map);
		//
		// model.addAttribute("edition", virtualEdition);
		// model.addAttribute("cluster", cluster);
		// model.addAttribute("selected", params.getId());

		return "recommendation/virtualTableWithSections";
	}

	@RequestMapping(value = "/section/save", method = RequestMethod.POST, headers = {
			"Content-type=application/json;charset=UTF-8" })
	public String saveSectionVirtualEdition(Model model,
			@RequestBody VirtualEditionWithSectionsDTO virtualEditionWithSectionsDTO) {
		// logger.debug("saveSectionVirtualEdition {}",
		// virtualEditionWithSectionsDTO.getSections().stream()
		// .map(s -> s.getInter() + ":" +
		// s.getSections().stream().collect(Collectors.joining(",")))
		// .collect(Collectors.joining("\n")));

		VirtualEdition virtualEdition = (VirtualEdition) LdoD.getInstance()
				.getEdition(virtualEditionWithSectionsDTO.getAcronym());
		if (virtualEdition == null) {
			return "redirect:/error";
		} else {
			// int i = 1;
			// for (SectionDTO sectionDTO : virtualEditionWithSectionsDTO.getSections()) {
			// List<String> sections = sectionDTO.getSections();
			//
			// Section section = virtualEdition.getSection(sections.get(0)) == null
			// ? virtualEdition.createSection(sections.get(0))
			// : virtualEdition.getSection(sections.get(0));
			// for (int j = 1; j < sections.size(); j++) {
			// section = section.getSection(sections.get(j)) == null ?
			// section.createSection(sections.get(j))
			// : section.getSection(sections.get(j));
			// }
			// VirtualEditionInter virtualEditionInter =
			// FenixFramework.getDomainObject(sectionDTO.getInter());
			// section.addVirtualEditionInter(virtualEditionInter, i++);
			// }
			// virtualEdition.clearEmptySections();
		}

		return "redirect:/recommendation/restricted/" + virtualEdition.getExternalId();
	}

	@RequestMapping(value = "/section/create", method = RequestMethod.POST)
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.participant')")
	public String createSectionVirtualEdition(Model model, @RequestParam("acronym") String acronym,
			@RequestParam("title") String title, @RequestParam("pub") boolean pub,
			@RequestParam("inter[]") String[] inters, @RequestParam("depth[]") String[] depth) {

		LdoDUser user = LdoDUser.getAuthenticatedUser();
		VirtualEdition virtualEdition = LdoD.getInstance().createVirtualEdition(user, acronym, title, new LocalDate(),
				pub, null);

		RecommendationWeights recommendationWeights = user.getRecommendationWeights(virtualEdition);

		for (int i = 0; i < inters.length; i++) {
			String inter = inters[i];
			// System.out.println(depth[i]);
			String[] sections = depth[i].split("\\|");
			// for(String s : sections){
			// System.out.println(s);
			// }
			Section section = virtualEdition.getSection(sections[0]) == null ? virtualEdition.createSection(sections[0])
					: virtualEdition.getSection(sections[0]);

			for (int j = 1; j < sections.length; j++) {
				section = section.getSection(sections[j]) == null ? section.createSection(sections[j])
						: section.getSection(sections[j]);
			}

			VirtualEditionInter virtualEditionInter = FenixFramework.getDomainObject(inter);
			section.createVirtualEditionInter(virtualEditionInter, i + 1);
		}
		virtualEdition.clearEmptySections();
		return "redirect:/recommendation/restricted/" + virtualEdition.getExternalId();
	}

}
