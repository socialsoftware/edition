package pt.ist.socialsoftware.edition.ldod.bff.social.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.socialsoftware.edition.ldod.bff.social.dtos.CitationDto;
import pt.ist.socialsoftware.edition.ldod.bff.social.service.SocialService;
import pt.ist.socialsoftware.edition.ldod.bff.user.controller.LdoDUserController;

import java.util.List;

@RestController
@RequestMapping("/api/social")
public class SocialController {


    @Autowired
    private SocialService service;

    private static final Logger logger = LoggerFactory.getLogger(LdoDUserController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/twitter-citations")
    public ResponseEntity<List<CitationDto>> getCitationsList() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCitationsList());
    }


}
