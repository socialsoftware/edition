package pt.ist.socialsoftware.edition.ldod.bff.text.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.MainResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.UploadFragmentDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.service.TextAdminService;
import pt.ist.socialsoftware.edition.ldod.bff.user.controller.UserAdminController;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/text/admin/")
@PreAuthorize("hasPermission('','ADMIN')")
public class TextAdminController {

    private static final Logger logger = LoggerFactory.getLogger(UserAdminController.class);
    @Autowired
    TextAdminService service;

    @PostMapping("/upload-corpus")
    public ResponseEntity<MainResponseDto> loadTEICorpus(@RequestPart(name = "file", required = false) MultipartFile file) {
        logger.debug("upload-corpus file:{}", file);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.loadTEICorpusService(file));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/upload-fragment")
    public ResponseEntity<?> loadTEIFragmentFile(@RequestPart(name = "file", required = false) MultipartFile file) {
        logger.debug("upload-fragment file:{}", file);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.uploadOrOverwriteTEIFragment(file));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getResponseDto(false, e.getMessage()));
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/upload-fragments")
    public ResponseEntity<?> loadTEIFragmentsFiles(@RequestPart(name = "files", required = false) MultipartFile[] files) {
        logger.debug("upload-fragments files:{}", files);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.uploadTEIFragments(files));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getResponseDto(false, e.getMessage()));
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragments")
    public ResponseEntity<List<UploadFragmentDto>> fragments() {
        logger.debug("fragments");
        return ResponseEntity.status(HttpStatus.OK).body(service.getFragmentsSet());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fragment-delete/{externalId}")
    public ResponseEntity<MainResponseDto> deleteFragment(@PathVariable("externalId") String externalId) {
        logger.debug("delete fragment externalId:{}", externalId);
        return ResponseEntity.status(HttpStatus.OK).body(service.removeFragment(externalId));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fragments-delete-all")
    public ResponseEntity<MainResponseDto> deleteAllFragments() {
        logger.debug("deleteAll fragments");
        service.removeFragments();
        return ResponseEntity.status(HttpStatus.OK).body(getResponseDto(true, "Fragments deleted"));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/export-fragments")
    public ResponseEntity<?> exportFragments(@RequestBody String[] externalIds) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.exportFragments(Arrays.asList(externalIds)));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getResponseDto(false, e.getMessage()));
        }
    }

    @GetMapping("/export-all")
    public ResponseEntity<?> exportAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.exportAllFragments());
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getResponseDto(false, e.getMessage()));
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/export-random")
    public ResponseEntity<?> exportRandom() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.exportRandomFrags());
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getResponseDto(false, e.getMessage()));
        }

    }

    private MainResponseDto getResponseDto(boolean status, String msg) {
        return new MainResponseDto(status, msg);
    }

}
