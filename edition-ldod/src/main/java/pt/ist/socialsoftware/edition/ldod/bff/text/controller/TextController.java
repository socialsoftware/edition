package pt.ist.socialsoftware.edition.ldod.bff.text.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.MainResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.FragInterRequestBodyDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.service.TextService;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.List;


@RestController
@RequestMapping("/api/text")
public class TextController {

    @Autowired
    private TextService textService;


    @GetMapping("/fragments")
    public ResponseEntity<List<FragmentDto>> getFragments() {
        return ResponseEntity.status(HttpStatus.OK).body(textService.getFragments());
    }

    @GetMapping("/sources")
    public ResponseEntity<List<?>> getSources() {
        return ResponseEntity.status(HttpStatus.OK).body(textService.getSources());
    }

    @GetMapping("/acronym/{acronym}")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public ResponseEntity<?> getEditionTableByAcrn(@PathVariable String acronym) {
        //TODO ensure proper response to invalid acrn
        return ResponseEntity.status(HttpStatus.OK).body(textService.getEditionByAcrn(acronym));
    }

    @GetMapping("/fragment/{xmlId}")
    public ResponseEntity<?> getFragment(@PathVariable String xmlId) {
        //TODO ensure proper response to invalid xmlId
        return ResponseEntity.status(HttpStatus.OK).body(textService.getFragmentByXmlId(xmlId));
    }

    @PostMapping(consumes = "application/json", value = "/fragment/{xmlId}/inter/{urlId}")
    public ResponseEntity<?> getFragmentInter(@PathVariable String xmlId, @PathVariable String urlId, @RequestBody FragInterRequestBodyDto bodyDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(textService.getFragmentInter(xmlId, urlId, bodyDto)
            );
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }

    }

    @PostMapping(consumes = "application/json", value = "/fragment/{xmlId}/inters")
    public ResponseEntity<?> getFragmentInters(@PathVariable String xmlId, @RequestBody FragInterRequestBodyDto fragInterRequestBodyDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    fragInterRequestBodyDto.getInters().size() == 1
                            ? textService.getFragmentInters(xmlId, fragInterRequestBodyDto, fragInterRequestBodyDto.getInters().get(0))
                            : textService.getFragmentInters(xmlId, fragInterRequestBodyDto, fragInterRequestBodyDto.getInters())
            );
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }

    }

}
