package pt.ist.socialsoftware.edition.search.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.search.api.dto.AdvancedSearchResultDto;
import pt.ist.socialsoftware.edition.search.api.dto.SearchDto;
import pt.ist.socialsoftware.edition.search.api.dto.SearchableElementDto;
import pt.ist.socialsoftware.edition.search.api.dto.VirtualEditionSearchOptionDto;
import pt.ist.socialsoftware.edition.search.feature.SearchProcessor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SearchProvidesInterface {
    private static final Logger logger = LoggerFactory.getLogger(SearchProvidesInterface.class);

    @GetMapping("/simpleSearch")
    public Map<String, List<ScholarInterDto>> simpleSearch(@RequestParam(name = "params") String params) {
        logger.debug("simpleSearch: " + params);
        return new SearchProcessor().simpleSearch(params);
    }

    @PostMapping("/advancedSearch")
    public AdvancedSearchResultDto advancedSearch(@RequestBody SearchDto search, @RequestParam(name = "username") String username) {
        logger.debug("advancedSearch: " + username);

        if (username != null) {
            Arrays.stream(search.getSearchOptions())
                .filter(searchOptionDto -> searchOptionDto instanceof VirtualEditionSearchOptionDto)
                .forEach(searchOptionDto -> ((VirtualEditionSearchOptionDto) searchOptionDto).setUsername(username));
        }

        return new SearchProcessor().advancedSearch(search);
    }

}
