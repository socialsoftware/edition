package pt.ist.socialsoftware.edition.ldod.bff.virtual.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.MainResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.CategoryDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.CategoryRequestBodyDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.GenerateTopicsDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.service.VirtualTaxonomyService;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.utils.TopicDTO;

import java.util.List;

@RestController
@RequestMapping("/api/virtual/taxonomy")
public class VeTaxonomyController {

    @Autowired
    private VirtualTaxonomyService service;

    private static Logger logger = LoggerFactory.getLogger(VirtualController.class);


    @GetMapping("/restricted/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public ResponseEntity<?> getVeTaxonomy(@PathVariable String externalId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getVeTaxonomy(externalId));
        } catch (
                LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }


    @PostMapping("/restricted/{externalId}/createTopics")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.taxonomy')")
    public ResponseEntity<?> createTopicModelling(@PathVariable String externalId, @RequestBody List<TopicDTO> topicList) {
        try {
            service.createTaxonomyTopics(externalId, topicList);
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(true,""));
        } catch (
                LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @PostMapping("/restricted/{externalId}/generate")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.taxonomy')")
    public ResponseEntity<?> generateTopicModelling(@PathVariable String externalId, @RequestBody GenerateTopicsDto generateTopicsDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.generateTaxonomyTopics(externalId, generateTopicsDto));
        } catch (
                LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @DeleteMapping("/restricted/delete/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.taxonomy')")
    public ResponseEntity<?> deleteTaxonomy(@PathVariable("externalId") String externalId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.deleteTaxonomy(externalId));
        } catch (
                LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @PostMapping("/restricted/{veExternalId}/category/create")
    @PreAuthorize("hasPermission(#veExternalId, 'virtualedition.taxonomy')")
    public ResponseEntity<?> createCategory(@PathVariable String veExternalId, @RequestBody CategoryRequestBodyDto categoryDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.createCategory(veExternalId, categoryDto));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @PostMapping("/restricted/category/{categoryId}/update/")
    @PreAuthorize("hasPermission(#categoryId, 'category.taxonomy')")
    public ResponseEntity<?> updateCategoryName(@PathVariable String categoryId, @RequestBody CategoryDto categoryDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.updateCategory(categoryId, categoryDto));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }
    }


    @PostMapping("/restricted/{taxonomyId}/categories/merge")
    @PreAuthorize("hasPermission(#taxonomyId, 'taxonomy.taxonomy')")
    public ResponseEntity<?> mergeCategories(@PathVariable String taxonomyId, @RequestBody List<String> categoriesIds) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.mergeCategories(taxonomyId, categoriesIds));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @PostMapping("/restricted/{taxonomyId}/categories/delete")
    @PreAuthorize("hasPermission(#taxonomyId, 'taxonomy.taxonomy')")
    public ResponseEntity<?> deleteCategories(@PathVariable String taxonomyId, @RequestBody List<String> categoriesIds) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.deleteCategories(taxonomyId, categoriesIds));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }
    }


    @PostMapping("/restricted/category/{categoryId}/extract")
    @PreAuthorize("hasPermission(#categoryId, 'category.taxonomy')")
    public ResponseEntity<?> extractCategory(@PathVariable String categoryId, @RequestBody List<String> categoriesIds) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.extractCategories(categoryId, categoriesIds));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }

    }

}



