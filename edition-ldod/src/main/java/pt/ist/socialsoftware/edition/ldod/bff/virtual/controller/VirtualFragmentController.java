package pt.ist.socialsoftware.edition.ldod.bff.virtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.MainResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.fragment.VirtualFragmentNavBodyDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.service.VirtualFragmentService;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/virtual/fragment/")
public class VirtualFragmentController {
    @Autowired
    private VirtualFragmentService service;


    @GetMapping("/restricted/frag-inter/{fragInterId}/tag/dissociate/{categoryId}")
    public ResponseEntity<?> dissociate(@PathVariable String fragInterId, @PathVariable String categoryId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.dissociate(fragInterId, categoryId));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @PostMapping("/restricted/frag-inter/{fragInterId}/tag/associate")
    @PreAuthorize("hasPermission(#fragInterId, 'fragInter.annotation')")
    public ResponseEntity<?> associateCategory(@PathVariable String fragInterId, @RequestBody List<String> categoriesIds) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.associate(fragInterId, new HashSet<>(categoriesIds)));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }
    }


    @PostMapping("/{xmlId}")
    public ResponseEntity<?> getFragment(@PathVariable String xmlId, @RequestBody VirtualFragmentNavBodyDto body) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getFragmentInters(xmlId, body));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @GetMapping("/{xmlId}/inter/{urlId}")
    @PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
    public ResponseEntity<?> getFragmentInter(@PathVariable String xmlId, @PathVariable String urlId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getFragmentInter(xmlId, urlId));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @PostMapping("/{xmlId}/inters")
    public ResponseEntity<?> getVirtualFragmentInters(@PathVariable String xmlId, @RequestBody List<String> ids) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getVirtualFragmentInters(xmlId, ids));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @PostMapping("/{xmlId}/restricted/add-inter/{veId}/{interId}")
    @PreAuthorize("hasPermission(#veId, 'virtualedition.participant')")
    public ResponseEntity<?> addInter(@PathVariable String xmlId,
                                      @PathVariable String veId,
                                      @PathVariable String interId,
                                      @RequestBody VirtualFragmentNavBodyDto body) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.addInterToVe(xmlId, veId, interId, body));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }
    }


}

