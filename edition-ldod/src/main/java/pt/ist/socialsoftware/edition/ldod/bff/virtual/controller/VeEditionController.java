package pt.ist.socialsoftware.edition.ldod.bff.virtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.MainResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.service.VirtualService;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

@RestController
@RequestMapping("/api/virtual/edition")
public class VeEditionController {

    @Autowired
    private VirtualService virtualService;

    @GetMapping("/acronym/{acronym}")
    public ResponseEntity<?> getVirtualEdition(@PathVariable String acronym) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.getVirtualEditionByAcronym(acronym));
        } catch (
                LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserVeData(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(virtualService.getUserVeData(username));
    }

    @GetMapping("/acronym/{acronym}/category/{category}")
    public ResponseEntity<?> getVirtualEditionCategories(@PathVariable String acronym, @PathVariable String category) {
        return ResponseEntity.status(HttpStatus.OK).body(virtualService.getVirtualEditionCategories(acronym, category));
    }

    @GetMapping("/acronym/{acronym}/taxonomy")
    public ResponseEntity<?> getVirtualEditionTaxonomy(@PathVariable String acronym) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.getVirtualEditionTaxonomy(acronym));
        } catch (
                LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }


}
