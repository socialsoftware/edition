package pt.ist.socialsoftware.edition.ldod.bff.social.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.MainResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.social.dtos.TweetListDto;
import pt.ist.socialsoftware.edition.ldod.bff.social.service.SocialService;
import pt.ist.socialsoftware.edition.ldod.bff.user.controller.LdoDUserController;

import java.io.IOException;

@RestController
@PreAuthorize("hasPermission('','ADMIN')")
@RequestMapping("/api/admin/social")
public class SocialAdminController {
    @Autowired
    private SocialService service;
    private static final Logger logger = LoggerFactory.getLogger(LdoDUserController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/tweets")
    public ResponseEntity<TweetListDto> manageTweets() {
        logger.debug("manageTweets");
        return ResponseEntity.status(HttpStatus.OK).body(service.getTweetsToManage());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/remove-tweets")
    public ResponseEntity<TweetListDto> removeTweets() {
        logger.debug("removeTweets");
        return ResponseEntity.status(HttpStatus.OK).body(service.removeTweets());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tweets/generateCitations")
    public ResponseEntity<?> generateCitations() throws IOException {
        logger.debug("generateCitations");
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.generateCitations());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MainResponseDto(false, e.getMessage()));
        }
    }
}
