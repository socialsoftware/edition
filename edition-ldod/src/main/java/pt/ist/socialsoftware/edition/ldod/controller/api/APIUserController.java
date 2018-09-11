package pt.ist.socialsoftware.edition.ldod.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.dto.LdoDUserDto;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;


@RestController
@RequestMapping("/api/user")
public class APIUserController {
	private static Logger logger = LoggerFactory.getLogger(APIUserController.class);

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@GetMapping
	public ResponseEntity<LdoDUserDto> getCurrentUser(@AuthenticationPrincipal LdoDUserDetails currentUser) {
		logger.debug("getCurrentUser {}", currentUser == null ? "null" : currentUser.getUsername());
		if (currentUser != null) {
			LdoDUserDto userDTO = new LdoDUserDto(currentUser.getUser());
			return new ResponseEntity<LdoDUserDto>(userDTO, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	@GetMapping(value = "/{username}")
	public ResponseEntity<LdoDUserDto> getUserProfile(@PathVariable(value = "username") String username) {
		logger.debug("getUserProfile");
		LdoDUser user = LdoD.getInstance().getUser(username);
		if (user != null) {
			LdoDUserDto userDTO = new LdoDUserDto(user);
			return new ResponseEntity<LdoDUserDto>(userDTO, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}
}
