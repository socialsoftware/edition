package pt.ist.socialsoftware.edition.ldod.bff.annotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.MainResponseDto;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

@RestController
@RequestMapping("/api/annotations")
public class AnnotationsController {

    @Autowired
    private AnnotationService service;

    @GetMapping("/inter/{externalId}")
    public ResponseEntity<?> getVirtualInterAnnotations(@PathVariable String externalId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getAnnotations(externalId));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @PostMapping("/inter/{veExternalId}/annotation/create")
    public ResponseEntity<?> createAnnotation(@RequestBody AnnotationDto annDto, @PathVariable String veExternalId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.createAnnotation(annDto, veExternalId));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }
    }


    @PostMapping("/inter/annotation/{externalId}")
    public ResponseEntity<?> updateAnnotation(
            @PathVariable String externalId,
            @RequestBody AnnotationDto annDto) {

        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.updateAnnotation(annDto, externalId));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }
    }


    @PostMapping("/inter/{veExternalId}/annotation/{externalId}/delete")
    public ResponseEntity<?> deleteAnnotation(@PathVariable String veExternalId, @PathVariable String externalId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.deleteAnnotation(veExternalId, externalId));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }
    }
}
