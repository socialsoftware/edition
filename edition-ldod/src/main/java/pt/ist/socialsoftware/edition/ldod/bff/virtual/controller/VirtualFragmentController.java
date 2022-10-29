package pt.ist.socialsoftware.edition.ldod.bff.virtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.MainResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.service.VirtualFragmentService;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.HashSet;
import java.util.List;

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


}

