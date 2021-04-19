package pt.ist.socialsoftware.edition.ldod.frontend.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.LdoDUserViewDto;
import pt.ist.socialsoftware.edition.ldod.frontend.user.security.UserModuleUserDetails;
import pt.ist.socialsoftware.edition.user.api.dto.UserDto;


@RestController
@RequestMapping("/api/user")
public class APIUserController {
    private static final Logger logger = LoggerFactory.getLogger(APIUserController.class);

    FeUserRequiresInterface feUserRequiresInterface = new FeUserRequiresInterface();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public ResponseEntity<LdoDUserViewDto> getCurrentUser(@AuthenticationPrincipal UserModuleUserDetails currentUser) {
        logger.debug("getCurrentUser {}", currentUser == null ? "null" : currentUser.getUsername());
        if (currentUser != null) {
            LdoDUserViewDto userDTO = new LdoDUserViewDto(currentUser.getUser());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<LdoDUserViewDto> getUserProfile(@PathVariable(value = "username") String username) {
        logger.debug("getUserProfile");
        UserDto user = this.feUserRequiresInterface.getUser(username);
        if (user != null) {
            LdoDUserViewDto userDTO = new LdoDUserViewDto(user);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
