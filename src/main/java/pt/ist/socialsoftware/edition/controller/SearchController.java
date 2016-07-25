package pt.ist.socialsoftware.edition.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.Edition.EditionType;
import pt.ist.socialsoftware.edition.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.Heteronym;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.domain.ManuscriptSource.Medium;
import pt.ist.socialsoftware.edition.domain.PrintedSource;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.Source.SourceType;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.search.json.AuthoralJson;
import pt.ist.socialsoftware.edition.search.json.DatesJson;
import pt.ist.socialsoftware.edition.search.json.EditionJson;
import pt.ist.socialsoftware.edition.search.options.AuthoralSearchOption;
import pt.ist.socialsoftware.edition.search.options.DateSearchOption;
import pt.ist.socialsoftware.edition.search.options.EditionSearchOption;
import pt.ist.socialsoftware.edition.search.options.HeteronymSearchOption;
import pt.ist.socialsoftware.edition.search.options.ManuscriptSearchOption;
import pt.ist.socialsoftware.edition.search.options.PublicationSearchOption;
import pt.ist.socialsoftware.edition.search.options.Search;
import pt.ist.socialsoftware.edition.search.options.SearchOption;
import pt.ist.socialsoftware.edition.search.options.TaxonomySearchOption;
import pt.ist.socialsoftware.edition.search.options.TextSearchOption;
import pt.ist.socialsoftware.edition.search.options.TypescriptSearchOption;
import pt.ist.socialsoftware.edition.search.options.VirtualEditionSearchOption;

@Controller
@RequestMapping("/search")
public class SearchController {
	private static Logger logger = LoggerFactory.getLogger(SearchController.class);

	/*
	 * EditionController Sets all the empty boxes to null instead of the empty
	 * string ""
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	// List of all fragments
	@RequestMapping(method = RequestMethod.GET, value = "/fragments")
	public String listFragments(Model model) {
		model.addAttribute("fragments", LdoD.getInstance().getFragmentsSet());
		return "fragment/list";
	}

	// Simple search
	@RequestMapping(method = RequestMethod.GET, value = "/simple")
	public String simpleSearch(Model model) {
		return "search/simple";
	}

	@RequestMapping(value = "/simple/result", method = RequestMethod.POST, headers = {
			"Content-type=text/plain;charset=UTF-8" })
	public String simpleSearchResult(Model model, @RequestBody String params) {
		logger.debug("params {}", params);

		String search = params.substring(0, params.indexOf("&"));
		params = params.substring(params.indexOf("&") + 1);
		String searchType = params.substring(0, params.indexOf("&"));
		params = params.substring(params.indexOf("&") + 1);
		String searchSource = params;

		TextSearchOption textSearchOption = new TextSearchOption(search);
		List<FragInter> matches = textSearchOption.search();

		Map<Fragment, List<FragInter>> results = new HashMap<Fragment, List<FragInter>>();
		int interCount = 0;
		for (FragInter inter : matches) {
			Fragment fragment = inter.getFragment();
			if ((searchSource.compareTo("") == 0
					|| inter.getShortName().toLowerCase().contains(searchSource.toLowerCase()))
					&& (searchType.compareTo("") == 0
							|| inter.getTitle().toLowerCase().contains(search.toLowerCase()))) {

				if (!results.containsKey(fragment)) {
					results.put(fragment, new ArrayList<FragInter>());
				}

				if (!results.get(fragment).contains(inter)) {
					results.get(fragment).add(inter);
					interCount++;
				}
			}
		}

		model.addAttribute("fragCount", results.size());
		model.addAttribute("interCount", interCount);
		model.addAttribute("results", results);
		return "search/simpleResultTable";
	}

	// Advanced Search
	@RequestMapping(method = RequestMethod.GET, value = "/advanced")
	public String advancedSearch(Model model) {
		return "search/advanced";
	}

	@RequestMapping(value = "/advanced/result", method = RequestMethod.POST, headers = {
			"Content-type=application/json" })
	public String advancedSearchResultNew(Model model, @RequestBody Search search) {
		Map<Fragment, Map<FragInter, List<SearchOption>>> results = search.search();

		int fragCount = 0;
		int fragCountNotAdded = 0;
		int interCount = 0;
		int interCountNotAdded = 0;
		boolean showSource = false;
		boolean showSourceType = false;
		boolean showEdition = false;
		boolean showHeteronym = false;
		boolean showDate = false;
		boolean showLdoD = false;
		boolean showPubPlace = false;
		boolean fragAdd = false;
		boolean showTaxonomy = false;
		for (Map.Entry<Fragment, Map<FragInter, List<SearchOption>>> entry : results.entrySet()) {
			fragAdd = false;
			for (Map.Entry<FragInter, List<SearchOption>> entry2 : entry.getValue().entrySet()) {
				if (entry2.getValue().size() >= 1) {
					interCount++;
					fragAdd = true;
				} else {
					interCountNotAdded++;
				}
				for (SearchOption option : entry2.getValue()) {
					if (option instanceof EditionSearchOption) {
						showSource = true;
						EditionSearchOption op = (EditionSearchOption) option;
						if (!op.getEdition().equals(SearchOption.ALL))
							showEdition = true;
						if (op.hasHeteronym())
							showHeteronym = true;
						if (op.hasDate())
							showDate = true;
					} else if (option instanceof AuthoralSearchOption) {
						showSource = true;
						showSourceType = true;
						AuthoralSearchOption op = (AuthoralSearchOption) option;
						if (op.hasLdoDMark())
							showLdoD = true;
						if (op.hasDate())
							showDate = true;
					} else if (option instanceof PublicationSearchOption) {
						showSource = true;
						PublicationSearchOption op = (PublicationSearchOption) option;
						if (op.hasTitle())
							showPubPlace = true;
					} else if (option instanceof HeteronymSearchOption) {
						showHeteronym = true;
					} else if (option instanceof DateSearchOption) {
						showDate = true;
					} else if (option instanceof TaxonomySearchOption) {
						showTaxonomy = true;
					} else if (option instanceof VirtualEditionSearchOption) {
						showSource = true;
					}
				}
			}
			if (fragAdd) {
				fragCount++;
			} else {
				fragCountNotAdded++;
			}
		}
		model.addAttribute("showEdition", showEdition);
		model.addAttribute("showHeteronym", showHeteronym);
		model.addAttribute("showDate", showDate);
		model.addAttribute("showLdoD", showLdoD);
		model.addAttribute("showPubPlace", showPubPlace);
		model.addAttribute("showSource", showSource);
		model.addAttribute("showSourceType", showSourceType);
		model.addAttribute("showTaxonomy", showTaxonomy);
		model.addAttribute("fragCount", fragCount);
		model.addAttribute("interCount", interCount);
		model.addAttribute("fragCountNotAdded", fragCountNotAdded);
		model.addAttribute("interCountNotAdded", interCountNotAdded);
		model.addAttribute("results", results);
		SearchOption[] searchOptions = search.getSearchOptions();
		model.addAttribute("search", searchOptions);
		model.addAttribute("searchLenght", searchOptions.length);
		return "search/resultTable";
	}

	@RequestMapping(value = "/getEditions")
	@ResponseBody
	public Map<String, String> getEditions() {
		// LinkedHashMap keeps insertion order.
		Map<String, String> editions = new LinkedHashMap<String, String>();
		for (ExpertEdition expertEdition : LdoD.getInstance().getSortedExpertEdition()) {

			editions.put(expertEdition.getAcronym(), expertEdition.getEditor());
		}
		return editions;
	}

	@RequestMapping(value = "/getEdition")
	@ResponseBody
	public EditionJson getEdition(@RequestParam(value = "edition", required = true) String acronym) {
		Edition edition = LdoD.getInstance().getEdition(acronym);
		Map<String, String> heteronyms = new HashMap<String, String>();
		LocalDate beginDate = null;
		LocalDate endDate = null;
		for (FragInter fragInter : edition.getIntersSet()) {
			if (!heteronyms.containsKey(fragInter.getHeteronym().getName())) {
				heteronyms.put(fragInter.getHeteronym().getName(), fragInter.getHeteronym().getXmlId());
			}
			if (fragInter.getLdoDDate() != null) {
				beginDate = getIsBeforeDate(beginDate, fragInter.getLdoDDate().getDate());
				endDate = getIsAfterDate(endDate, fragInter.getLdoDDate().getDate());
			}
		}
		EditionJson editionJson = new EditionJson(acronym);
		if (heteronyms.size() > 0) {
			editionJson.setHeteronyms(heteronyms);
		}
		if (endDate != null && beginDate != null && endDate != beginDate) {
			editionJson.setBeginDate(beginDate.getYear());
			editionJson.setEndDate(endDate.getYear());
		}
		return editionJson;
	}

	@RequestMapping(value = "/getPublications")
	@ResponseBody
	public List<String> getPublications() {
		List<String> publications = new ArrayList<String>();
		for (Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
			for (Source source : fragment.getSourcesSet()) {
				if (source.getType().equals(SourceType.PRINTED)) {
					if (!publications.contains(((PrintedSource) source).getPubPlace())) {
						publications.add(((PrintedSource) source).getTitle());
					}
				}
			}
		}
		return publications;
	}

	private AuthoralJson getAuthoral(String mode) {
		AuthoralJson json = new AuthoralJson();
		LocalDate beginDate = null;
		LocalDate endDate = null;
		Medium[] values = ManuscriptSource.Medium.values();
		String[] array = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			array[i] = values[i].getDesc();
		}
		for (Fragment frag : LdoD.getInstance().getFragmentsSet()) {
			for (FragInter inter : frag.getFragmentInterSet()) {
				if (inter.getSourceType().equals(EditionType.AUTHORIAL)) {
					SourceType type = ((SourceInter) inter).getSource().getType();
					if (type.equals(SourceType.MANUSCRIPT)) {
						ManuscriptSource source = (ManuscriptSource) ((SourceInter) inter).getSource();
						if (!source.getNotes().toLowerCase().contains(mode)) {
							break;
						}
						if (source.getLdoDDate() != null) {
							beginDate = getIsBeforeDate(beginDate, source.getLdoDDate().getDate());
							endDate = getIsAfterDate(endDate, source.getLdoDDate().getDate());
						}
					}
				}
			}
		}
		json.setMediums(array);
		DatesJson dates = new DatesJson();
		if (endDate != null && beginDate != null && endDate != beginDate) {
			dates.setBeginDate(beginDate.getYear());
			dates.setEndDate(endDate.getYear());
		}
		json.setDates(dates);
		return json;
	}

	@RequestMapping(value = "/getManuscripts")
	@ResponseBody
	public AuthoralJson getManuscript() {
		return getAuthoral(ManuscriptSearchOption.MANUSCRIPTID);
	}

	@RequestMapping(value = "/getDactiloscripts")
	@ResponseBody
	public AuthoralJson getDatiloscript() {
		return getAuthoral(TypescriptSearchOption.TYPESCRIPT);
	}

	@RequestMapping(value = "/getHeteronyms")
	@ResponseBody
	public Map<String, String> getHeteronyms() {
		Map<String, String> heteronyms = new HashMap<String, String>();
		for (Heteronym heteronym : LdoD.getInstance().getHeteronymsSet()) {
			heteronyms.put(heteronym.getName(), heteronym.getXmlId());
		}
		return heteronyms;
	}

	@RequestMapping(value = "/getDates")
	@ResponseBody
	public DatesJson getDates() {
		LocalDate beginDate = null;
		LocalDate endDate = null;
		for (Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
			for (FragInter fragInter : fragment.getFragmentInterSet()) {
				if (fragInter.getLdoDDate() != null) {
					beginDate = getIsBeforeDate(beginDate, fragInter.getLdoDDate().getDate());
					endDate = getIsAfterDate(endDate, fragInter.getLdoDDate().getDate());
				}
			}
			for (Source source : fragment.getSourcesSet()) {
				if (source.getType().equals(SourceType.MANUSCRIPT)) {
					ManuscriptSource manu = (ManuscriptSource) source;
					if (manu.getLdoDDate() != null) {
						beginDate = getIsBeforeDate(beginDate, manu.getLdoDDate().getDate());
						endDate = getIsAfterDate(endDate, manu.getLdoDDate().getDate());
					}
				} else if (source.getType().equals(SourceType.PRINTED)) {
					PrintedSource print = (PrintedSource) source;
					if (print.getLdoDDate() != null) {
						beginDate = getIsBeforeDate(beginDate, print.getLdoDDate().getDate());
						endDate = getIsAfterDate(endDate, print.getLdoDDate().getDate());
					}
				}
			}
		}
		DatesJson dates = new DatesJson();
		if (endDate != null && beginDate != null && endDate != beginDate) {
			dates.setBeginDate(beginDate.getYear());
			dates.setEndDate(endDate.getYear());
		}
		return dates;
	}

	@RequestMapping(value = "/getVirtualEditions")
	@ResponseBody
	public Map<String, String> getVirtualEditions(Model model) {
		Map<String, String> virtualEditionMap = new HashMap<String, String>();
		LdoDUser user = LdoDUser.getAuthenticatedUser();
		for (VirtualEdition virtualEdition : user.getSelectedVirtualEditionsSet()) {
			if (!virtualEditionMap.containsKey(virtualEdition.getAcronym())) {
				virtualEditionMap.put(virtualEdition.getAcronym(), virtualEdition.getTitle());
			}
		}
		return virtualEditionMap;
	}

	private LocalDate getIsBeforeDate(LocalDate date1, LocalDate date2) {
		if (date1 == null)
			return date2;
		else if (date2 == null)
			return date1;
		else if (date1.isBefore(date2))
			return date1;
		else
			return date2;
	}

	private LocalDate getIsAfterDate(LocalDate date1, LocalDate date2) {
		if (date1 == null)
			return date2;
		else if (date2 == null)
			return date1;
		else if (date1.isAfter(date2))
			return date1;
		else
			return date2;
	}

}
