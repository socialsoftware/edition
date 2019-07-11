package pt.ist.socialsoftware.edition.ldod.frontend.serverside.search;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.frontend.serverside.search.dto.AuthoralJson;
import pt.ist.socialsoftware.edition.ldod.frontend.serverside.search.dto.DatesJson;
import pt.ist.socialsoftware.edition.ldod.frontend.serverside.search.dto.EditionJson;
import pt.ist.socialsoftware.edition.ldod.search.api.dto.AdvancedSearchResultDto;
import pt.ist.socialsoftware.edition.ldod.search.feature.options.ManuscriptSearchOption;
import pt.ist.socialsoftware.edition.ldod.search.feature.options.Search;
import pt.ist.socialsoftware.edition.ldod.search.feature.options.SearchOption;
import pt.ist.socialsoftware.edition.ldod.search.feature.options.TypescriptSearchOption;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.HeteronymDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.LdoDDateDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/search")
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    SearchFrontendRequiresInterface frontendRequiresInterface = new SearchFrontendRequiresInterface();


    @ModelAttribute("ldoDSession")
    public LdoDSession getLdoDSession() {
        return LdoDSession.getLdoDSession();
    }

    /*
     * EditionController Sets all the empty boxes to null instead of the empty string ""
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    // Simple search
    @RequestMapping(method = RequestMethod.GET, value = "/simple")
    public String simpleSearch(Model model) {
        return "search/simple";
    }

    @RequestMapping(value = "/simple/result", method = RequestMethod.POST, headers = {
            "Content-type=text/plain;charset=UTF-8"})
    public String simpleSearchResult(Model model, @RequestBody String params) {
        logger.debug("simpleSearchResult params:{}", params);

        Map<String, List<ScholarInterDto>> results = this.frontendRequiresInterface.getSimpleSearch(params);

        model.addAttribute("fragCount", results.size());
        model.addAttribute("interCount", results.values().stream().mapToInt(List::size).sum());
        model.addAttribute("results", results);
        model.addAttribute("frontendRequiresInterface", new SearchFrontendRequiresInterface());
        return "search/simpleResultTable";
    }

    // Advanced SearchProcessor
    @RequestMapping(method = RequestMethod.GET, value = "/advanced")
    public String advancedSearch(Model model) {
        logger.debug("advancedSearch");
        return "search/advanced";
    }

    @RequestMapping(value = "/advanced/result", method = RequestMethod.POST, headers = {
            "Content-type=application/json"})
    public String advancedSearchResultNew(Model model, @RequestBody Search search) {
        logger.debug("advancedSearchResultNew");

        AdvancedSearchResultDto advancedSearchResultDto = this.frontendRequiresInterface.advancedSearch(search);

        model.addAttribute("isShowEdition", advancedSearchResultDto.isShowEdition());
        model.addAttribute("isShowHeteronym", advancedSearchResultDto.isShowHeteronym());
        model.addAttribute("isShowDate", advancedSearchResultDto.isShowDate());
        model.addAttribute("isShowLdoD", advancedSearchResultDto.isShowLdoD());
        model.addAttribute("isShowSource", advancedSearchResultDto.isShowSource());
        model.addAttribute("isShowSourceType", advancedSearchResultDto.isShowSourceType());
        model.addAttribute("isShowTaxonomy", advancedSearchResultDto.isShowTaxonomy());
        model.addAttribute("fragCount", advancedSearchResultDto.getFragCount());
        model.addAttribute("interCount", advancedSearchResultDto.getInterCount());
        model.addAttribute("fragCountNotAdded", advancedSearchResultDto.getFragCountNotAdded());
        model.addAttribute("interCountNotAdded", advancedSearchResultDto.getInterCountNotAdded());
        model.addAttribute("results", advancedSearchResultDto.getResults());

        SearchOption[] searchOptions = search.getSearchOptions();

        model.addAttribute("search", searchOptions);
        model.addAttribute("searchLenght", searchOptions.length);
        model.addAttribute("frontendRequiresInterface", new SearchFrontendRequiresInterface());
        return "search/resultTable";
    }

    @RequestMapping(value = "/getEditions")
    @ResponseBody
    public Map<String, String> getEditions() {
        // LinkedHashMap keeps insertion order.
        Map<String, String> editions = new LinkedHashMap<>();
        for (ExpertEdition expertEdition : TextModule.getInstance().getSortedExpertEdition()) {

            editions.put(expertEdition.getAcronym(), expertEdition.getEditor());
        }
        return editions;
    }

    @RequestMapping(value = "/getEdition")
    @ResponseBody
    public EditionJson getEdition(@RequestParam(value = "edition", required = true) String acronym) {
        logger.debug("getEdition");
        List<ScholarInterDto> scholarInterDtos = this.frontendRequiresInterface.getExpertEditionScholarInterDtoList(acronym);

        Map<String, String> heteronyms = new HashMap<>();
        LocalDate beginDate = null;
        LocalDate endDate = null;
        for (ScholarInterDto scholarInterDto : scholarInterDtos) {
            HeteronymDto heteronymDto = scholarInterDto.getHeteronym();
            if (!heteronyms.containsKey(heteronymDto.getName())) {
                heteronyms.put(heteronymDto.getName(), heteronymDto.getXmlId());
            }

            LdoDDateDto ldoDDateDto = scholarInterDto.getLdoDDate();
            if (ldoDDateDto != null) {
                beginDate = getIsBeforeDate(beginDate, ldoDDateDto.getDate());
                endDate = getIsAfterDate(endDate, ldoDDateDto.getDate());
            }
        }
        EditionJson editionJson = new EditionJson(acronym);
        if (heteronyms.size() > 0) {
            editionJson.setHeteronyms(heteronyms);
        }
        if (endDate != null && beginDate != null) {
            editionJson.setBeginDate(beginDate.getYear());
            editionJson.setEndDate(endDate.getYear());
        }
        return editionJson;
    }

    @RequestMapping(value = "/getPublicationsDates")
    @ResponseBody
    public DatesJson getPublicationsDates() {
        logger.debug("getPublicationsDates");
        LocalDate beginDate = null;
        LocalDate endDate = null;
        for (Fragment fragment : TextModule.getInstance().getFragmentsSet()) {
            for (Source source : fragment.getSourcesSet()) {
                if (source.getType().equals(Source.SourceType.PRINTED)) {
                    if (source.getLdoDDate() != null) {
                        beginDate = getIsBeforeDate(beginDate, source.getLdoDDate().getDate());
                        endDate = getIsAfterDate(endDate, source.getLdoDDate().getDate());
                    }
                }
            }
        }
        DatesJson datesJson = new DatesJson();
        if (endDate != null && beginDate != null) {
            datesJson.setBeginDate(beginDate.getYear());
            datesJson.setEndDate(endDate.getYear());
        }
        return datesJson;
    }

    private AuthoralJson getAuthoralDates(String mode) {
        AuthoralJson json = new AuthoralJson();
        LocalDate beginDate = null;
        LocalDate endDate = null;
        ManuscriptSource.Medium[] values = ManuscriptSource.Medium.values();
        String[] array = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = values[i].getDesc();
        }
        for (Fragment frag : TextModule.getInstance().getFragmentsSet()) {
            for (ScholarInter scholarInter : frag.getScholarInterSet()) {
                if (!scholarInter.isExpertInter()) {
                    Source.SourceType type = ((SourceInter) scholarInter).getSource().getType();
                    if (type.equals(Source.SourceType.MANUSCRIPT)) {
                        ManuscriptSource source = (ManuscriptSource) ((SourceInter) scholarInter).getSource();
                        if (mode.equals(ManuscriptSearchOption.MANUSCRIPTID)
                                && source.getHandNoteSet().isEmpty()
                                || (mode.equals(TypescriptSearchOption.TYPESCRIPT)
                                && source.getTypeNoteSet().isEmpty())) {
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
        if (endDate != null && beginDate != null) {
            dates.setBeginDate(beginDate.getYear());
            dates.setEndDate(endDate.getYear());
        }
        json.setDates(dates);
        return json;
    }

    @RequestMapping(value = "/getManuscriptsDates")
    @ResponseBody
    public AuthoralJson getManuscript() {
        logger.debug("getManuscript");
        return getAuthoralDates(ManuscriptSearchOption.MANUSCRIPTID);
    }

    @RequestMapping(value = "/getDactiloscriptsDates")
    @ResponseBody
    public AuthoralJson getDatiloscript() {
        return getAuthoralDates(TypescriptSearchOption.TYPESCRIPT);
    }

    @RequestMapping(value = "/getHeteronyms")
    @ResponseBody
    public Map<String, String> getHeteronyms() {
        Map<String, String> heteronyms = new HashMap<>();
        for (Heteronym heteronym : TextModule.getInstance().getHeteronymsSet()) { //TODO: change this so it gets heteronym info from the interface
            heteronyms.put(heteronym.getName(), heteronym.getXmlId());
        }
        return heteronyms;
    }

    @RequestMapping(value = "/getDates")
    @ResponseBody
    public DatesJson getDates() {
        LocalDate beginDate = null;
        LocalDate endDate = null;
        for (Fragment fragment : TextModule.getInstance().getFragmentsSet()) {
            for (ScholarInter scholarInter : fragment.getScholarInterSet()) {
                if (scholarInter.getLdoDDate() != null) {
                    beginDate = getIsBeforeDate(beginDate, scholarInter.getLdoDDate().getDate());
                    endDate = getIsAfterDate(endDate, scholarInter.getLdoDDate().getDate());
                }
            }
            for (Source source : fragment.getSourcesSet()) {
                if (source.getLdoDDate() != null) {
                    beginDate = getIsBeforeDate(beginDate, source.getLdoDDate().getDate());
                    endDate = getIsAfterDate(endDate, source.getLdoDDate().getDate());
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
        Stream<VirtualEdition> virtualEditionStream = VirtualModule.getInstance().getVirtualEditionsSet().stream().filter(virtualEdition -> virtualEdition.getPub());

        User user = User.getAuthenticatedUser();
        if (user != null) {
            virtualEditionStream = Stream.concat(virtualEditionStream,
                    VirtualModule.getInstance().getSelectedVirtualEditionsByUser(user.getUsername()).stream()).distinct();
        }
        return virtualEditionStream.collect(Collectors.toMap(VirtualEdition::getAcronym, VirtualEdition::getTitle));
    }

    private LocalDate getIsBeforeDate(LocalDate date1, LocalDate date2) {
        if (date1 == null) {
            return date2;
        } else if (date2 == null) {
            return date1;
        } else if (date1.isBefore(date2)) {
            return date1;
        } else {
            return date2;
        }
    }

    private LocalDate getIsAfterDate(LocalDate date1, LocalDate date2) {
        if (date1 == null) {
            return date2;
        } else if (date2 == null) {
            return date1;
        } else if (date1.isAfter(date2)) {
            return date1;
        } else {
            return date2;
        }
    }

}