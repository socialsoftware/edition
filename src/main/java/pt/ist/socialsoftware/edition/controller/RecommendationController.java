package pt.ist.socialsoftware.edition.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
import pt.ist.socialsoftware.edition.recommendation.dto.IterativeSortVirtualEditionParam;
import pt.ist.socialsoftware.edition.recommendation.dto.PropertyWithLevel;
import pt.ist.socialsoftware.edition.recommendation.dto.RecommendVirtualEditionParam;
import pt.ist.socialsoftware.edition.recommendation.dto.SectionDTO;
import pt.ist.socialsoftware.edition.recommendation.dto.VirtualEditionWithSectionsDTO;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.visitors.PlainHtmlWriter4OneInter;

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

	@RequestMapping(value = "/sortedEdition", method = RequestMethod.POST, headers = {
			"Content-type=application/json;charset=UTF-8" })
	public String getSortedRecommendedVirtualEdition(Model model, @RequestBody RecommendVirtualEditionParam params) {
		logger.debug(
				"getSortedRecommendedVirtualEdition acronym:{}, id:{}, properties:{}", params
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
			List<FragInter> recommendedEdition = new ArrayList<FragInter>();
			recommendedEdition.add(inter);
			recommendedEdition.addAll(recommender.getMostSimilarItemsAsList(inter, inters, properties));

			model.addAttribute("inters", recommendedEdition);
			model.addAttribute("selected", params.getId());
		}
		model.addAttribute("heteronym", null);
		model.addAttribute("edition", edition);

		return "recommendation/virtualTable";
	}

	@RequestMapping(value = "/iterativeSort", method = RequestMethod.POST, headers = {
			"Content-type=application/json;charset=UTF-8" })
	public String getIterativeSortVirtualEdition(Model model, @RequestBody IterativeSortVirtualEditionParam params) {
		logger.debug("getIterativeSortVirtualEdition");

		VirtualEdition edition = (VirtualEdition) LdoD.getInstance().getEdition(params.getAcronym());
		VirtualEditionInter inter = FenixFramework.getDomainObject(params.getId());
		List<VirtualEditionInter> inters = edition.getVirtualEditionInters();
		VSMVirtualEditionInterRecommender recommender = new VSMVirtualEditionInterRecommender();
		Map<Integer, Collection<Property>> map = new HashMap<Integer, Collection<Property>>();
		LdoDUser user = LdoDUser.getAuthenticatedUser();
		RecommendationWeights recommendationWeights = user.getRecommendationWeights(edition);
		recommendationWeights.setWeights(params.getNormalizeProperties());
		for (PropertyWithLevel property : params.getProperties()) {
			if (property.getProperty().getWeight() > 0) {
				if (!map.containsKey(property.getLevel())) {
					map.put(property.getLevel(), new ArrayList<Property>());
				}
				map.get(property.getLevel()).add(property.getProperty());
			}
		}
		Cluster cluster = recommender.getCluster(inter, inters, map);

		model.addAttribute("heteronym", null);
		model.addAttribute("edition", edition);
		model.addAttribute("cluster", cluster);
		model.addAttribute("selected", params.getId());

		return "recommendation/cluster";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String getSaveSortVirtualEdition(Model model, @RequestParam("acronym") String acronym,
			@RequestParam("inter[]") String[] inters) {
		logger.debug("getSaveSortVirtualEdition");

		LdoD ldod = LdoD.getInstance();
		VirtualEdition edition = (VirtualEdition) ldod.getEdition(acronym);
		Section section = edition.createSection("default");
		if (edition.getSourceType().equals(EditionType.VIRTUAL)) {
			VirtualEditionInter VirtualEditionInter;
			for (int i = 0; i < inters.length; i++) {
				VirtualEditionInter = FenixFramework.getDomainObject(inters[i]);
				section.addVirtualEditionInter(VirtualEditionInter, i + 1);
			}
		}
		edition.clearEmptySections();

		model.addAttribute("heteronym", null);
		model.addAttribute("edition", edition);
		model.addAttribute("inters", edition.getSortedInterps());

		return "recommendation/virtualTable";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String getCreateSortVirtualEdition(Model model, @RequestParam("acronym") String acronym,
			@RequestParam("title") String title, @RequestParam("pub") boolean pub,
			@RequestParam("inter[]") String[] inters) {
		logger.debug("getCreateSortVirtualEdition");
		VirtualEdition virtualEdition = LdoD.getInstance().createVirtualEdition(LdoDUser.getAuthenticatedUser(),
				acronym, title, new LocalDate(), pub, null);
		VirtualEditionInter virtualInter;
		for (int i = 0; i < inters.length; i++) {
			virtualInter = FenixFramework.getDomainObject(inters[i]);
			virtualEdition.createVirtualEditionInter(virtualInter, i + 1);
		}
		return "redirect:/recommendation/restricted/" + virtualEdition.getExternalId();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}")
	public String getSimilarVirtualEdition(Model model, @PathVariable String externalId) {
		logger.debug("getSimilarVirtualEdition");

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {
			logger.debug("getSimilarVirtualEdition sections: {}",
					virtualEdition.getSectionsSet().stream().map(s -> s.print(1)).collect(Collectors.joining()));

			RecommendationWeights recommendationWeights = LdoDUser.getAuthenticatedUser()
					.getRecommendationWeights(virtualEdition);

			logger.debug("getSimilarVirtualEdition sections: {}",
					virtualEdition.getSectionsSet().stream().map(s -> s.print(1)).collect(Collectors.joining()));

			model.addAttribute("heteronym", null);
			model.addAttribute("edition", virtualEdition);
			model.addAttribute("heteronymWeight", recommendationWeights.getHeteronymWeight());
			model.addAttribute("dateWeight", recommendationWeights.getDateWeight());
			model.addAttribute("textWeight", recommendationWeights.getTextWeight());

			if (!virtualEdition.getVirtualEditionInters().isEmpty()) {
				VirtualEditionInter inter = virtualEdition.getVirtualEditionInters().get(0);
				List<VirtualEditionInter> inters = virtualEdition.getVirtualEditionInters();

				inters.remove(inter);

				VSMVirtualEditionInterRecommender recommender = new VSMVirtualEditionInterRecommender();
				List<Property> properties = recommendationWeights.getProperties();
				List<FragInter> recommendedEdition = new ArrayList<FragInter>();
				recommendedEdition.add(inter);
				recommendedEdition.addAll(recommender.getMostSimilarItemsAsList(inter, inters, properties));

				model.addAttribute("inters", recommendedEdition);
				model.addAttribute("selected", inter.getExternalId());
			}

			return "recommendation/tableOfContents";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/{edition}/{inter}")
	public String getSimilarOfInter(Model model, @PathVariable String edition, @PathVariable String inter) {
		logger.debug("getSimilarOfInter");

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(edition);
		VirtualEditionInter virtualEditionInter = FenixFramework.getDomainObject(inter);
		if (virtualEdition == null || virtualEditionInter == null) {
			return "utils/pageNotFound";
		} else {
			VSMVirtualEditionInterRecommender recommender = new VSMVirtualEditionInterRecommender();
			List<Property> properties = LdoDUser.getAuthenticatedUser().getRecommendationWeights(virtualEdition)
					.getProperties();
			List<VirtualEditionInter> mostSimilarItems = new ArrayList<VirtualEditionInter>();
			List<VirtualEditionInter> inters = virtualEdition.getVirtualEditionInters();
			inters.remove(virtualEditionInter);
			mostSimilarItems.add(virtualEditionInter);
			mostSimilarItems.addAll(recommender.getMostSimilarItemsAsList(virtualEditionInter, inters, properties));
			RecommendationWeights recommendationWeights = LdoDUser.getAuthenticatedUser()
					.getRecommendationWeights(virtualEdition);

			model.addAttribute("heteronym", null);
			model.addAttribute("edition", virtualEdition);
			model.addAttribute("inters", mostSimilarItems);
			model.addAttribute("taxonomyWeight", recommendationWeights.getTaxonomyWeight());
			model.addAttribute("heteronymWeight", recommendationWeights.getHeteronymWeight());
			model.addAttribute("dateWeight", recommendationWeights.getDateWeight());
			model.addAttribute("textWeight", recommendationWeights.getTextWeight());
			model.addAttribute("selected", inter);

			return "recommendation/tableOfContents";
		}
	}

	@RequestMapping(value = "/inter/next/{id}", method = RequestMethod.POST)
	public String getNextRecommendedFragment(Model model, @PathVariable String id,
			@RequestParam("acronym") String acronym, @RequestParam("current") String currentId,
			@RequestParam(value = "id[]", required = false) String[] ids) {
		logger.debug("getNextRecommendedFragment");

		Edition edition = LdoD.getInstance().getEdition(acronym);
		VirtualEditionInter virtualEditionInter = FenixFramework.getDomainObject(id);
		if (virtualEditionInter == null || !(edition instanceof VirtualEdition)) {
			return "utils/pageNotFound";
		} else {
			VirtualEdition virtualEdition = (VirtualEdition) edition;
			VirtualEditionInter current = FenixFramework.getDomainObject(currentId);
			List<String> interList = new ArrayList<String>();
			if (ids != null) {
				interList.addAll(Arrays.asList(ids));
			}
			interList.add(currentId);

			List<VirtualEditionInter> selectionSet = virtualEdition.getVirtualEditionInters();
			for (String interId : interList) {
				selectionSet.remove(FenixFramework.getDomainObject(interId));
			}

			VSMVirtualEditionInterRecommender recommender = new VSMVirtualEditionInterRecommender();
			LdoDUser user = LdoDUser.getAuthenticatedUser();
			List<Property> properties = user.getRecommendationWeights(virtualEdition).getProperties();
			VirtualEditionInter mostSimilar = recommender.getMostSimilarItem(virtualEditionInter, selectionSet,
					properties);

			PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(virtualEditionInter);
			writer.write(false);
			List<FragInter> inters = new ArrayList<FragInter>();
			inters.add(virtualEditionInter);

			model.addAttribute("ldoD", LdoD.getInstance());
			model.addAttribute("user", user);
			model.addAttribute("fragment", virtualEditionInter.getFragment());
			model.addAttribute("inters", inters);
			model.addAttribute("previousList", interList);
			model.addAttribute("prev", current);
			model.addAttribute("acronym", acronym);
			model.addAttribute("writer", writer);
			model.addAttribute("next", mostSimilar);
			if (mostSimilar == null) {
				model.addAttribute("last", true);
			}
			model.addAttribute("recommender", true);

			return "fragment/main";
		}
	}

	@RequestMapping(value = "/inter/prev/{id}", method = RequestMethod.POST)
	public String getPreviousRecommendedFragment(Model model, @PathVariable String id,
			@RequestParam("current") String currentId, @RequestParam("acronym") String acronym,
			@RequestParam(value = "id[]", required = false) String[] ids) {
		logger.debug("getPreviousRecommendedFragment");

		Edition edition = LdoD.getInstance().getEdition(acronym);
		VirtualEditionInter virtualEditionInter = FenixFramework.getDomainObject(id);
		if (virtualEditionInter == null || !(edition instanceof VirtualEdition)) {
			return "utils/pageNotFound";
		} else {
			List<String> previousList = new ArrayList<>();
			if (ids != null) {
				previousList.addAll(Arrays.asList(ids));
				if (!previousList.isEmpty()) {
					previousList.remove(previousList.size() - 1);
					if (!previousList.isEmpty()) {
						VirtualEditionInter previous = FenixFramework
								.getDomainObject(previousList.get(previousList.size() - 1));
						model.addAttribute("prev", previous);
					}
				}
			}
			LdoDUser user = LdoDUser.getAuthenticatedUser();
			VirtualEditionInter next = FenixFramework.getDomainObject(currentId);
			PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(virtualEditionInter);
			writer.write(false);
			List<FragInter> inters = new ArrayList<FragInter>();
			inters.add(virtualEditionInter);

			model.addAttribute("ldoD", LdoD.getInstance());
			model.addAttribute("user", user);
			model.addAttribute("fragment", virtualEditionInter.getFragment());
			model.addAttribute("inters", inters);
			model.addAttribute("previousList", previousList);
			model.addAttribute("next", next);
			model.addAttribute("acronym", acronym);
			model.addAttribute("writer", writer);
			model.addAttribute("recommender", true);

			return "fragment/main";
		}
	}

	@RequestMapping(value = "/iterativesort/save", method = RequestMethod.POST, headers = {
			"Content-type=application/json;charset=UTF-8" })
	public String saveItertativeSort(Model model,
			@RequestBody VirtualEditionWithSectionsDTO virtualEditionWithSectionsDTO) {
		logger.debug("saveItertativeSort");

		Edition edition = LdoD.getInstance().getEdition(virtualEditionWithSectionsDTO.getAcronym());
		if (edition == null || !(edition instanceof VirtualEdition)) {
			return "utils/pageNotFound";
		} else {
			VirtualEdition virtualEdition = (VirtualEdition) edition;
			int i = 1;
			for (SectionDTO sectionDTO : virtualEditionWithSectionsDTO.getSections()) {
				List<String> sections = sectionDTO.getSections();

				logger.debug("saveItertativeSort sections: {}",
						virtualEdition.getSectionsSet().stream().map(s -> s.print(1)).collect(Collectors.joining()));

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

			model.addAttribute("heteronym", null);
			model.addAttribute("edition", virtualEdition);
			model.addAttribute("inters", virtualEdition.getSortedInterps());

			return "recommendation/virtualTableWithSections";
		}

	}

	@RequestMapping(value = "/iterativesort/create", method = RequestMethod.POST)
	public String createIterativeSort(Model model, @RequestParam("acronym") String acronym,
			@RequestParam("title") String title, @RequestParam("pub") boolean pub,
			@RequestParam("inter[]") String[] inters, @RequestParam("depth[]") String[] depth) {
		logger.debug("createIterativeSort");

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
