package pt.ist.socialsoftware.edition.ldod.bff.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.bff.search.dto.SimpleSearchDto;
import pt.ist.socialsoftware.edition.ldod.search.options.Search;

@RestController
@RequestMapping("/api/search")
public class MfeSearchController {

    private static Logger logger = LoggerFactory.getLogger(MfeSearchController.class);

    @Autowired
    private SearchService service;

    @PostMapping(value = "/simple-search")
    public ResponseEntity<?> getSimpleSearch(@RequestBody SimpleSearchDto simpleSearchDto) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getSimpleSearch(simpleSearchDto));
    }

    @GetMapping(value = "/advanced-search")
    public ResponseEntity<?> getAdvancedSearch() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAdvancedSearch());
    }

    @PostMapping(value = "/advanced-search")
    public ResponseEntity<?> performAdvancedSearch(@RequestBody Search search) {
        logger.debug("AdvancedSearchResult params:{}", search);
        return ResponseEntity.status(HttpStatus.OK).body(service.performAdvSearch(search));
    }

}
