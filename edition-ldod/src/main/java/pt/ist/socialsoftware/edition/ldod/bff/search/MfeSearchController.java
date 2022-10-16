package pt.ist.socialsoftware.edition.ldod.bff.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class MfeSearchController {
    @Autowired
    private SearchService service;

    @PostMapping(value = "/simple-search")
    public ResponseEntity<?> getSimpleSearch(@RequestBody SimpleSearchDto simpleSearchDto) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getSimpleSearch(simpleSearchDto));
    }

}
