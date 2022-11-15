package pt.ist.socialsoftware.edition.ldod.bff.reading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.linkedin.api.Recommendation;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.recommendation.ReadingRecommendation;


@RestController
@RequestMapping("/api/reading")
public class MfeReadingController {

    @Autowired
    private ReadingService service;


    @GetMapping
    public ResponseEntity<?> getExpertEditions() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getExpertEditions());
    }

    @PostMapping("/{acronym}/fragment/{xmlId}/inter/{urlId}")
    public ResponseEntity<?> getExpertEditionInterByAcronym(@PathVariable String acronym,
                                                            @PathVariable String xmlId,
                                                            @PathVariable String urlId,
                                                            @RequestBody ReadingRecommendation recommendation
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getExpertEditionInterByAcronym(acronym, xmlId, urlId, recommendation));
    }


}
