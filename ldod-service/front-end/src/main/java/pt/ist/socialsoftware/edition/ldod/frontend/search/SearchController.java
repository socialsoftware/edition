package pt.ist.socialsoftware.edition.ldod.frontend.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.frontend.user.session.FrontendSession;
import pt.ist.socialsoftware.edition.search.api.dto.AdvancedSearchResultDto;
import pt.ist.socialsoftware.edition.search.api.dto.SearchDto;
import pt.ist.socialsoftware.edition.search.api.dto.SearchOptionDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.ScholarInterDto;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/search")
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    FeSearchRequiresInterface frontendRequiresInterface = new FeSearchRequiresInterface();


    @ModelAttribute("frontendSession")
    public FrontendSession getLdoDSession() {
        return FrontendSession.getFrontendSession();
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
        model.addAttribute("frontendRequiresInterface", new FeSearchRequiresInterface());
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
    public String advancedSearchResultNew(Model model, @RequestBody SearchDto search) {
        logger.debug("advancedSearchResultNew");

        String username = this.frontendRequiresInterface.getAuthenticatedUser();
        AdvancedSearchResultDto advancedSearchResultDto = this.frontendRequiresInterface.advancedSearch(search, username);

        model.addAttribute("showEdition", advancedSearchResultDto.isShowEdition());
        model.addAttribute("showHeteronym", advancedSearchResultDto.isShowHeteronym());
        model.addAttribute("showDate", advancedSearchResultDto.isShowDate());
        model.addAttribute("showLdoD", advancedSearchResultDto.isShowLdoD());
        model.addAttribute("showSource", advancedSearchResultDto.isShowSource());
        model.addAttribute("showSourceType", advancedSearchResultDto.isShowSourceType());
        model.addAttribute("showTaxonomy", advancedSearchResultDto.isShowTaxonomy());
        model.addAttribute("fragCount", advancedSearchResultDto.getFragCount());
        model.addAttribute("interCount", advancedSearchResultDto.getInterCount());
        model.addAttribute("fragCountNotAdded", advancedSearchResultDto.getFragCountNotAdded());
        model.addAttribute("interCountNotAdded", advancedSearchResultDto.getInterCountNotAdded());
        model.addAttribute("results", advancedSearchResultDto.getResults());

        SearchOptionDto[] searchOptions = search.getSearchOptions();

        model.addAttribute("search", searchOptions);
        model.addAttribute("searchLenght", searchOptions.length);
        model.addAttribute("frontendRequiresInterface", new FeSearchRequiresInterface());
        return "search/resultTable";
    }

}