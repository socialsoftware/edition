package pt.ist.socialsoftware.edition.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.joda.time.LocalDate;
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
import pt.ist.socialsoftware.edition.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.domain.ManuscriptSource.Medium;
import pt.ist.socialsoftware.edition.domain.PrintedSource;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.Source.SourceType;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.utils.search.json.AuthoralJson;
import pt.ist.socialsoftware.edition.utils.search.json.DatesJson;
import pt.ist.socialsoftware.edition.utils.search.json.EditionJson;
import pt.ist.socialsoftware.edition.utils.search.json.FragInterJson;
import pt.ist.socialsoftware.edition.utils.search.json.FragmentJson;
import pt.ist.socialsoftware.edition.utils.search.options.DactiloscryptSearchOption;
import pt.ist.socialsoftware.edition.utils.search.options.ManuscriptSearchOption;
import pt.ist.socialsoftware.edition.utils.search.options.Search;
import pt.ist.socialsoftware.edition.utils.search.options.SearchOption;
import pt.ist.socialsoftware.edition.utils.search.options.SearchOption.Mode;

@Controller
@RequestMapping("/search")
public class SearchController {

	/*
	 * Sets all the empty boxes to null instead of the empty string ""
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

	// Advanced Search
	@RequestMapping(method = RequestMethod.GET, value = "/advanced")
	public String advancedSearch(Model model) {
		return "search/advanced";
	}


	// Handles a search built by the user
	@RequestMapping(value = "/advanced/result", method = RequestMethod.POST,
			headers = { "Content-type=application/json" })
	@ResponseBody
	public List<FragmentJson> advancedSearchResult(
			Model model, @RequestBody Search search) {
		if(search.getMode().equals(Mode.AND))
			return search(search, true);
		else
			return search(search, false);
	}

	private List<FragmentJson> search(Search search, boolean and) {

		SearchOption[] options = search.getSearchOptions();
		Mode mode = search.getMode();
		List<FragmentJson> resultSet = new ArrayList<FragmentJson>();
		// Set<FragInterJson> fragInterJsonSet;
		Set<FragInter> fragInterSet;
		boolean working;
		boolean belongsToResulSet = true;

		for(Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
			fragInterSet = new TreeSet<FragInter>();
			belongsToResulSet = and;
			for(SearchOption option : options) {
				working = false;
				for(FragInter fragInter : fragment.getFragmentInterSet()) {
					if(fragInter.accept(option)) {
						//fragInterJsonSet.add(new FragInterJson(fragInter));
						fragInterSet.add(fragInter);
						working = true;
					}
				}
				belongsToResulSet = SearchOption.chooseMode(mode, belongsToResulSet, working);
			}

			if(belongsToResulSet) {
				List<FragInterJson> fragInterJsonSet = new LinkedList<FragInterJson>();

				for(FragInter fragInter : fragInterSet)
					fragInterJsonSet.add(new FragInterJson(fragInter));
				
				resultSet.add(new FragmentJson(fragment, fragInterJsonSet));
			}
		}
		return resultSet;
	}

	@RequestMapping(value = "/getEditions")
	@ResponseBody
	public Map<String,String> getEditions() {
		//LinkedHashMap keeps insertion order.
		Map<String,String> editions = new LinkedHashMap<String,String>();

		for (ExpertEdition expertEdition : LdoD.getInstance()
				.getSortedExpertEdition()) {

			editions.put(expertEdition.getAcronym(),expertEdition.getEditor());
		}

		return editions;
	}

	@RequestMapping(value = "/getEdition")
	@ResponseBody
	public EditionJson getEdition(
			@RequestParam(value = "edition", required = true) String acronym) {

		Edition edition = LdoD.getInstance().getEdition(acronym);
		Map<String,String> heteronyms = new HashMap<String,String>();
		LocalDate beginDate = null;
		LocalDate endDate = null;

		for (FragInter fragInter : edition.getIntersSet()) {
			if (!heteronyms.containsKey(fragInter.getHeteronym().getName())) {
				heteronyms.put(fragInter.getHeteronym().getName(),fragInter.getHeteronym().getXmlId());
			}

			if (beginDate == null) {
				beginDate = fragInter.getDate();
			}
			if (endDate == null) {
				endDate = fragInter.getDate();
			}
			if (fragInter.getDate() != null
					&& fragInter.getDate().isBefore(beginDate)) {
				beginDate = fragInter.getDate();
			}
			if (fragInter.getDate() != null
					&& fragInter.getDate().isAfter(endDate)) {
				endDate = fragInter.getDate();
			}

		}

		EditionJson editionJson = new EditionJson(acronym);
		if (heteronyms.size() > 0	) {
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
						publications.add(((PrintedSource) source).getPubPlace());
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

		for(int i = 0; i < values.length; i++) {
			array[i] = values[i].getDesc();
		}

		for(Fragment frag : LdoD.getInstance().getFragmentsSet()) {
			for(FragInter inter : frag.getFragmentInterSet()) {
				if(inter.getSourceType().equals(EditionType.AUTHORIAL)) {
					SourceType type = ((SourceInter) inter).getSource().getType();
					if(type.equals(SourceType.MANUSCRIPT)) {
						ManuscriptSource source = (ManuscriptSource) ((SourceInter) inter).getSource();

						if(!source.getNotes().toLowerCase().contains(mode)) {
							break;
						}
						if(beginDate == null) {
							beginDate = source.getDate();
						}
						if(endDate == null) {
							endDate = source.getDate();
						}
						if(source.getDate() != null && source.getDate().isBefore(beginDate)) {
							beginDate = source.getDate();
						}
						if(source.getDate() != null && source.getDate().isAfter(endDate)) {
							endDate = source.getDate();
						}
					}
				}
			}
		}

		json.setMediums(array);

		DatesJson dates = new DatesJson();
		if(endDate != null && beginDate != null && endDate != beginDate) {
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
		return getAuthoral(DactiloscryptSearchOption.DATILOSCRIPTID);
	}


	@RequestMapping(value = "/getHeteronyms")
	@ResponseBody
	public Map<String,String> getHeteronyms() {
		Map<String,String> heteronyms = new HashMap<String,String>();
		for(Heteronym heteronym : LdoD.getInstance().getHeteronymsSet()) {
			heteronyms.put(heteronym.getName(),heteronym.getXmlId());
		}
		return heteronyms;
	}

	@RequestMapping(value = "/getDates")
	@ResponseBody
	public DatesJson getDates() {

		LocalDate beginDate = null;
		LocalDate endDate = null;

		for (Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
			for(FragInter fragInter : fragment.getFragmentInterSet()){
				if (beginDate == null) {
					beginDate = fragInter.getDate();
				}
				if (endDate == null) {
					endDate = fragInter.getDate();
				}
				if (fragInter.getDate() != null
						&& fragInter.getDate().isBefore(beginDate)) {
					beginDate = fragInter.getDate();
				}
				if (fragInter.getDate() != null
						&& fragInter.getDate().isAfter(endDate)) {
					endDate = fragInter.getDate();
				}
			}

			for(Source source : fragment.getSourcesSet()){
				if(source.getType().equals(SourceType.MANUSCRIPT)){
					ManuscriptSource manu = (ManuscriptSource) source;
					if (beginDate == null) {
						beginDate = manu.getDate();
					}
					if (endDate == null) {
						endDate = manu.getDate();
					}
					if (manu.getDate() != null
							&& manu.getDate().isBefore(beginDate)) {
						beginDate = manu.getDate();
					}
					if (manu.getDate() != null
							&& manu.getDate().isAfter(endDate)) {
						endDate = manu.getDate();
					}
				}else if(source.getType().equals(SourceType.PRINTED)){
					PrintedSource print = (PrintedSource) source;
					if (beginDate == null) {
						beginDate = print.getDate();
					}
					if (endDate == null) {
						endDate = print.getDate();
					}
					if (print.getDate() != null
							&& print.getDate().isBefore(beginDate)) {
						beginDate = print.getDate();
					}
					if (print.getDate() != null
							&& print.getDate().isAfter(endDate)) {
						endDate = print.getDate();
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
}