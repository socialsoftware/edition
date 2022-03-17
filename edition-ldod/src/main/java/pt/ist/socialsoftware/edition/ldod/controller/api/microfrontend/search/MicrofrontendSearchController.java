package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.search;

import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.AdvancedSearchDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.SimpleSearchDto;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.search.options.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/microfrontend/search")
public class MicrofrontendSearchController {

    @PostMapping(value = "/simple")
    public SimpleSearchDto getSimpleSearch(@RequestBody SimpleSearchDto simpleSearchDto) {
        TextSearchOption searchOption = new TextSearchOption(TextSearchOption.purgeSearchText(simpleSearchDto.getSearchTerm()));
        return new SimpleSearchDto(searchOption.search()
                .stream()
                .filter(inter -> simpleSearchDto.getSearchSource().equals("")
                        || inter.getShortName().toLowerCase().contains(simpleSearchDto.getSearchSource().toLowerCase()))
                .filter(inter -> simpleSearchDto.getSearchType().equals("")
                        || inter.getTitle().toLowerCase().contains(simpleSearchDto.getSearchTerm().toLowerCase()))
                .collect(Collectors.toMap(FragInter_Base::getFragment, Arrays::asList, (exist, replace) -> Stream
                        .concat(exist.stream(), replace.stream())
                        .collect(Collectors.toList()))));
   }

    @RequestMapping(value = "/simple/result", method = RequestMethod.POST, headers = {
            "Content-type=text/plain;charset=UTF-8"})
    public SimpleSearchDto simpleSearchResult(@RequestBody String params) {

        String split;
        if (params.contains("&")) {
            split = "&";
        } else {
            split = "%26";
        }
        String search = params.substring(0, params.indexOf(split));
        params = params.substring(params.indexOf(split) + 1);
        String searchType = params.substring(0, params.indexOf(split));
        params = params.substring(params.indexOf(split) + 1);
        String searchSource = params;

        search = TextSearchOption.purgeSearchText(search);
        TextSearchOption textSearchOption = new TextSearchOption(search);
        List<FragInter> matches = textSearchOption.search();

        Map<Fragment, List<FragInter>> results = new HashMap<>();
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


        return new SimpleSearchDto(results, interCount);

    }

    @RequestMapping(value = "/advanced/result", method = RequestMethod.POST, headers = {
            "Content-type=application/json"})
    public AdvancedSearchDto advancedSearchResultNew(@RequestBody Search search) {
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
                        if (!op.getEdition().equals(SearchOption.ALL)) {
                            showEdition = true;
                        }
                        if (op.hasHeteronym()) {
                            showHeteronym = true;
                        }
                        if (op.hasDate()) {
                            showDate = true;
                        }
                    } else if (option instanceof AuthoralSearchOption) {
                        showSource = true;
                        showSourceType = true;
                        AuthoralSearchOption op = (AuthoralSearchOption) option;
                        if (op.hasLdoDMark()) {
                            showLdoD = true;
                        }
                        if (op.hasDate()) {
                            showDate = true;
                        }
                    } else if (option instanceof PublicationSearchOption) {
                        showSource = true;
                        PublicationSearchOption op = (PublicationSearchOption) option;
                        if (op.hasDate()) {
                            showDate = true;
                        }
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

        SearchOption[] searchOptions = search.getSearchOptions();
        return new AdvancedSearchDto(showEdition, showHeteronym, showDate, showLdoD, showSource, showSourceType, showTaxonomy, fragCount, interCount, fragCountNotAdded, interCountNotAdded,
                results, searchOptions, searchOptions.length);


    }

    @RequestMapping(value = "/getVirtualEditions")
    public Map<String, String> getVirtualEditions() {
        Stream<VirtualEdition> virtualEditionStream = LdoD.getInstance().getVirtualEditionsSet().stream().filter(Edition_Base::getPub);

        LdoDUser user = LdoDUser.getAuthenticatedUser();
        if (user != null) {
            virtualEditionStream = Stream.concat(virtualEditionStream, user.getSelectedVirtualEditionsSet().stream()).distinct();
        }
        return virtualEditionStream.collect(Collectors.toMap(VirtualEdition::getAcronym, VirtualEdition::getTitle));
    }

}
