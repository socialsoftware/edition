package pt.ist.socialsoftware.edition.search.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.search.api.dto.AdvancedSearchResultDto;
import pt.ist.socialsoftware.edition.search.api.dto.SearchDto;
import pt.ist.socialsoftware.edition.search.api.dto.SearchableElementDto;
import pt.ist.socialsoftware.edition.search.api.dto.VirtualEditionSearchOptionDto;
import pt.ist.socialsoftware.edition.search.api.textDto.ScholarInterDto;
import pt.ist.socialsoftware.edition.search.feature.SearchProcessor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SearchProvidesInterface {
    private final SearchRequiresInterface searchRequiresInterface = new SearchRequiresInterface();

    @GetMapping("/simpleSearch")
    public Map<String, List<ScholarInterDto>> simpleSearch(@RequestParam(name = "params") String params) {
        System.out.println("simpleSearch: " + params);
        return new SearchProcessor().simpleSearch(params);
    }

    @PostMapping("/advancedSearch")
    public AdvancedSearchResultDto advancedSearch(@RequestBody SearchDto search, @RequestParam(name = "username") String username) {
        System.out.println("advancedSearch: " + username);
        if (username != null) {
            Arrays.stream(search.getSearchOptions())
                .filter(searchOptionDto -> searchOptionDto instanceof VirtualEditionSearchOptionDto)
                .forEach(searchOptionDto -> ((VirtualEditionSearchOptionDto) searchOptionDto).setUsername(username));
        }

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ObjectReader or = new ObjectMapper().reader();
        try {
            String json = ow.writeValueAsString(new SearchProcessor().advancedSearch(search).getResults().entrySet().stream().findFirst().get().getValue().keySet().stream().findFirst().get());
            System.out.println(json);
            Map<String, Map<SearchableElementDto,  List<String>>> test = new ObjectMapper().readValue(json, new TypeReference<Map<String, Map<SearchableElementDto,  List<String>>>>() {});
            System.out.println(test);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new SearchProcessor().advancedSearch(search);
    }

}
