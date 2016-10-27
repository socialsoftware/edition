package pt.ist.socialsoftware.edition.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.Edition.EditionType;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.Section;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.recommendation.Cluster;
import pt.ist.socialsoftware.edition.recommendation.VSMVirtualEditionInterRecommender;
import pt.ist.socialsoftware.edition.recommendation.dto.PropertyWithLevel;
import pt.ist.socialsoftware.edition.recommendation.dto.RecommendVirtualEditionParam;
import pt.ist.socialsoftware.edition.recommendation.dto.SectionDTO;
import pt.ist.socialsoftware.edition.recommendation.dto.SectionVirtualEditionParam;
import pt.ist.socialsoftware.edition.recommendation.dto.VirtualEditionWithSectionsDTO;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

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
	public String presentEditionWithRecommendation(Model model, @PathVariable String externalId) {
		logger.debug("presentEditionWithRecommendation");

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {
			logger.debug("presentEditionWithRecommendation sections: {}",
					virtualEdition.getSectionsSet().stream().map(s -> s.print(1)).collect(Collectors.joining()));

			RecommendationWeights recommendationWeights = LdoDUser.getAuthenticatedUser()
					.getRecommendationWeights(virtualEdition);

			logger.debug("presentEditionWithRecommendation sections: {}",
					virtualEdition.getSectionsSet().stream().map(s -> s.print(1)).collect(Collectors.joining()));

			model.addAttribute("edition", virtualEdition);
			model.addAttribute("taxonomyWeight", recommendationWeights.getTaxonomyWeight());
			model.addAttribute("taxonomyLevel", recommendationWeights.getTaxonomyLevel());
			model.addAttribute("heteronymWeight", recommendationWeights.getHeteronymWeight());
			model.addAttribute("heteronymLevel", recommendationWeights.getHeteronymLevel());
			model.addAttribute("dateWeight", recommendationWeights.getDateWeight());
			model.addAttribute("dateLevel", recommendationWeights.getDateLevel());
			model.addAttribute("textWeight", recommendationWeights.getTextWeight());
			model.addAttribute("textLevel", recommendationWeights.getTextLevel());

			if (!virtualEdition.getVirtualEditionInters().isEmpty()) {
				VirtualEditionInter inter = virtualEdition.getVirtualEditionInters().get(0);
				List<VirtualEditionInter> inters = virtualEdition.getVirtualEditionInters();
				VSMVirtualEditionInterRecommender recommender = new VSMVirtualEditionInterRecommender();

				if (!virtualEdition.hasMultipleSections()) {

					inters.remove(inter);

					List<Property> properties = recommendationWeights.getProperties();
					List<FragInter> recommendedEdition = new ArrayList<>();
					recommendedEdition.add(inter);
					recommendedEdition.addAll(recommender.getMostSimilarItemsAsList(inter, inters, properties));

					model.addAttribute("inters", recommendedEdition);
				} else {
					Map<Integer, Collection<Property>> map = new HashMap<>();
					for (PropertyWithLevel property : recommendationWeights.getPropertiesWithLevel()) {
						if (property.getProperty().getWeight() > 0) {
							if (!map.containsKey(property.getLevel())) {
								map.put(property.getLevel(), new ArrayList<Property>());
							}
							map.get(property.getLevel()).add(property.getProperty());
						}
					}
					Cluster cluster = recommender.getCluster(inter, inters, map);
					model.addAttribute("cluster", cluster);
				}

				model.addAttribute("selected", inter.getExternalId());
			}

			return "recommendation/tableOfContents";
		}
	}

	@RequestMapping(value = "/linear", method = RequestMethod.POST, headers = {
			"Content-type=application/json;charset=UTF-8" })
	public String setLinearVirtualEdition(Model model, @RequestBody RecommendVirtualEditionParam params) {
		logger.debug(
				"setLinearVirtualEdition acronym:{}, id:{}, properties:{}", params
						.getAcronym(),
				params.getId(),
				params.getProperties().stream()
						.map(p -> p.getClass().getName().substring(p.getClass().getName().lastIndexOf(".") + 1) + " "
								+ p.getWeight())
						.collect(Collectors.joining(";")));
		VirtualEdition edition = (VirtualEdition) LdoD.getInstance().getEdition(params.getAcronym());

		LdoDUser user = LdoDUser.getAuthenticatedUser();
		RecommendationWeights recommendationWeights = user.getRecommendationWeights(edition);
		recommendationWeights.setWeights(params.getProperties());

		if (params.getId() != null && !params.getId().equals("")) {
			VirtualEditionInter inter = FenixFramework.getDomainObject(params.getId());
			List<VirtualEditionInter> inters = edition.getVirtualEditionInters();

			inters.remove(inter);

			VSMVirtualEditionInterRecommender recommender = new VSMVirtualEditionInterRecommender();
			List<Property> properties = recommendationWeights.getProperties();
			List<FragInter> recommendedEdition = new ArrayList<>();
			recommendedEdition.add(inter);
			recommendedEdition.addAll(recommender.getMostSimilarItemsAsList(inter, inters, properties));

			model.addAttribute("inters", recommendedEdition);
			model.addAttribute("selected", params.getId());
		}
		model.addAttribute("edition", edition);

		return "recommendation/virtualTable";
	}

	@RequestMapping(value = "/linear/save", method = RequestMethod.POST)
	public String saveLinearVirtualEdition(Model model, @RequestParam("acronym") String acronym,
			@RequestParam(value = "inter[]", required = false) String[] inters) {
		logger.debug("saveLinearVirtualEdition");

		LdoD ldod = LdoD.getInstance();
		VirtualEdition edition = (VirtualEdition) ldod.getEdition(acronym);
		if (inters != null && edition.getSourceType().equals(EditionType.VIRTUAL)) {
			Section section = edition.createSection(Section.DEFAULT);
			VirtualEditionInter VirtualEditionInter;
			int i = 0;
			for (String externalId : inters) {
				VirtualEditionInter = FenixFramework.getDomainObject(externalId);
				section.addVirtualEditionInter(VirtualEditionInter, ++i);
			}
			edition.clearEmptySections();

			List<FragInter> sortedInters = edition.getSortedInterps();
			model.addAttribute("inters", sortedInters);
			model.addAttribute("selected", sortedInters.get(0).getExternalId());
		}

		model.addAttribute("edition", edition);

		return "recommendation/virtualTable";
	}

	@RequestMapping(value = "/linear/create", method = RequestMethod.POST)
	public String createLinearVirtualEdition(Model model, @RequestParam("acronym") String acronym,
			@RequestParam("title") String title, @RequestParam("pub") boolean pub,
			@RequestParam("inter[]") String[] inters) {
		logger.debug("createLinearVirtualEdition");
		VirtualEdition virtualEdition = LdoD.getInstance().createVirtualEdition(LdoDUser.getAuthenticatedUser(),
				acronym, title, new LocalDate(), pub, null);
		VirtualEditionInter virtualInter;
		for (int i = 0; i < inters.length; i++) {
			virtualInter = FenixFramework.getDomainObject(inters[i]);
			virtualEdition.createVirtualEditionInter(virtualInter, i + 1);
		}
		return "redirect:/recommendation/restricted/" + virtualEdition.getExternalId();
	}

	@RequestMapping(value = "/section", method = RequestMethod.POST, headers = {
			"Content-type=application/json;charset=UTF-8" })
	public String setSectionVirtualEdition(Model model, @RequestBody SectionVirtualEditionParam params) {
		logger.debug("setSectionVirtualEdition");

		VirtualEdition edition = (VirtualEdition) LdoD.getInstance().getEdition(params.getAcronym());
		VirtualEditionInter inter = FenixFramework.getDomainObject(params.getId());
		List<VirtualEditionInter> inters = edition.getVirtualEditionInters();
		VSMVirtualEditionInterRecommender recommender = new VSMVirtualEditionInterRecommender();
		Map<Integer, Collection<Property>> map = new HashMap<>();
		LdoDUser user = LdoDUser.getAuthenticatedUser();
		RecommendationWeights recommendationWeights = user.getRecommendationWeights(edition);
		recommendationWeights.setWeightsAndLevels(params.getProperties());
		for (PropertyWithLevel property : params.getProperties()) {
			if (property.getProperty().getWeight() > 0) {
				if (!map.containsKey(property.getLevel())) {
					map.put(property.getLevel(), new ArrayList<Property>());
				}
				map.get(property.getLevel()).add(property.getProperty());
			}
		}
		Cluster cluster = recommender.getCluster(inter, inters, map);

		model.addAttribute("edition", edition);
		model.addAttribute("cluster", cluster);
		model.addAttribute("selected", params.getId());

		return "recommendation/virtualTableWithSections";
	}

	@RequestMapping(value = "/section/save", method = RequestMethod.POST, headers = {
			"Content-type=application/json;charset=UTF-8" })
	public String saveSectionVirtualEdition(Model model,
			@RequestBody VirtualEditionWithSectionsDTO virtualEditionWithSectionsDTO) {
		logger.debug("saveSectionVirtualEdition");

		Edition edition = LdoD.getInstance().getEdition(virtualEditionWithSectionsDTO.getAcronym());
		if (edition == null || !(edition instanceof VirtualEdition)) {
			return "utils/pageNotFound";
		} else {
			VirtualEdition virtualEdition = (VirtualEdition) edition;
			int i = 1;
			for (SectionDTO sectionDTO : virtualEditionWithSectionsDTO.getSections()) {
				List<String> sections = sectionDTO.getSections();

				// logger.debug("saveSectionVirtualEdition sections: {}",
				// virtualEdition.getSectionsSet().stream().map(s ->
				// s.print(1)).collect(Collectors.joining()));

				Section section = virtualEdition.getSection(sections.get(0)) == null
						? virtualEdition.createSection(sections.get(0)) : virtualEdition.getSection(sections.get(0));
				for (int j = 1; j < sections.size(); j++) {
					section = section.getSection(sections.get(j)) == null ? section.createSection(sections.get(j))
							: section.getSection(sections.get(j));
				}
				String inter = sectionDTO.getInter();
				VirtualEditionInter virtualEditionInter = FenixFramework.getDomainObject(inter);
				section.addVirtualEditionInter(virtualEditionInter, i++);
			}
			virtualEdition.clearEmptySections();

			return "OK";
		}
	}

	@RequestMapping(value = "/section/create", method = RequestMethod.POST)
	public String createSectionVirtualEdition(Model model, @RequestParam("acronym") String acronym,
			@RequestParam("title") String title, @RequestParam("pub") boolean pub,
			@RequestParam("inter[]") String[] inters, @RequestParam("depth[]") String[] depth) {
		logger.debug("createSectionVirtualEdition");

		VirtualEdition virtualEdition = LdoD.getInstance().createVirtualEdition(LdoDUser.getAuthenticatedUser(),
				acronym, title, new LocalDate(), pub, null);

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
