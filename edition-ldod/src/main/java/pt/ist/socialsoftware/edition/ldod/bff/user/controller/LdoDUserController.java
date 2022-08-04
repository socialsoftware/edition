package pt.ist.socialsoftware.edition.ldod.bff.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.AuthResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.user.services.UserService;
import pt.ist.socialsoftware.edition.ldod.dto.LdoDUserDto;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class MfesUserController {
    private static final Logger logger = LoggerFactory.getLogger(MfesUserController.class);

    @Autowired
    UserService service;

    @GetMapping
    public ResponseEntity<?> getCurrentUserController(@AuthenticationPrincipal LdoDUserDetails currentUser) {
        logger.debug("getCurrentUser {}", currentUser == null ? "null" : currentUser.getUsername());
        return currentUser != null
                ? ResponseEntity.status(HttpStatus.OK).body(service.getUserService(currentUser))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponseDto(false, "userUnauthorized"));
    }

    @GetMapping(value = "/auth/{username}")
    public ResponseEntity<LdoDUserDto> getUserProfileController(@PathVariable(value = "username") String username) {
        logger.debug("getUserProfile");
        Optional<LdoDUserDto> userDto = service.getUserByUsernameService(username);
        return userDto
                .map(ldoDUserDto -> ResponseEntity.status(HttpStatus.OK).body(ldoDUserDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
