package pt.ist.socialsoftware.edition.ldod.bff.virtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.MainResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.service.VirtualAdminService;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

@RestController
@PreAuthorize("hasPermission('','ADMIN')")
@RequestMapping("/api/virtual/admin")
public class VirtualAdminController {

    @Autowired
    private VirtualAdminService service;

    @GetMapping("/export-virtual-editions")
    public ResponseEntity<?> exportVirtualEditions() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.exportVirtualEditions());
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @PostMapping("/upload-virtual-corpus")
    public ResponseEntity<MainResponseDto> loadVirtualCorpus(@RequestPart(name = "file", required = false) MultipartFile file) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(service.uploadVirtualCorpus(file));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @PostMapping("/upload-virtual-fragments")
    public ResponseEntity<MainResponseDto> loadVirtualFragments(@RequestPart(name = "files", required = false) MultipartFile[] files) {
        return ResponseEntity.status(HttpStatus.OK).body(service.uploadVirtualFragments(files));

    }

    @GetMapping("/virtual-editions")
    public ResponseEntity<?> getVirtualEditions() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getVirtualEditionsSorted());
    }

    @GetMapping("/virtual-edition-delete/{externalId}")
    public ResponseEntity<?> deleteVirtualEdition(@PathVariable("externalId") String externalId) {
        try {
            service.deleteVirtualEdition(externalId);
            return ResponseEntity.status(HttpStatus.OK).body(service.getVirtualEditionsSorted());
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @GetMapping("/virtual-edition-delete-all")
    public ResponseEntity<?> deleteAllVirtualEdition() {
        try {
            service.deleteAllVirtualEditions();
            return ResponseEntity.status(HttpStatus.OK).body(service.getVirtualEditionsSorted());
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }
}
