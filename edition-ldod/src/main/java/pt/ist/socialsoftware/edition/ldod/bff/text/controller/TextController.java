package pt.ist.socialsoftware.edition.ldod.bff.text.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.SourceInterDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.service.TextService;

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
    public ResponseEntity<List<SourceInterDto>> getSources() {
        return ResponseEntity.status(HttpStatus.OK).body(textService.getSources());
    }

}
