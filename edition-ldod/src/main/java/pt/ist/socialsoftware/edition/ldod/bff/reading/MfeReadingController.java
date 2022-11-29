package pt.ist.socialsoftware.edition.ldod.bff.reading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.MainResponseDto;
import pt.ist.socialsoftware.edition.ldod.recommendation.ReadingRecommendation;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;


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

    @PostMapping("/fragment/{xmlId}/inter/{urlId}")
    public ResponseEntity<?> getExpertEditionInterByAcronym(
            @PathVariable String xmlId,
            @PathVariable String urlId,
            @RequestBody ReadingRecommendation recommendation
    ) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(service.getExpertEditionInterByAcronym(xmlId, urlId, recommendation));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }
    }


}
