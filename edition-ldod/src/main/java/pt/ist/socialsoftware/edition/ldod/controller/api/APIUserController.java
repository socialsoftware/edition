package pt.ist.socialsoftware.edition.ldod.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.dto.LdoDUserDTO;
import pt.ist.socialsoftware.edition.ldod.dto.VirtualEditionInterListDto;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class APIUserController {
	private static Logger logger = LoggerFactory.getLogger(APIUserController.class);

	@GetMapping
	public LdoDUserDTO getCurrentUser(@AuthenticationPrincipal LdoDUserDetails currentUser) {
		logger.debug("getCurrentUser {}", currentUser == null ? "null" : currentUser.getUsername());
		return new LdoDUserDTO(currentUser.getUser());

	}

	@GetMapping(value = "/{username}")
	public LdoDUserDTO getUserProfile(@PathVariable(value = "username") String username) {
		logger.debug("getUserProfile");
		LdoDUser user = LdoD.getInstance().getUser(username);
		if (user != null) {
			return new LdoDUserDTO(user);
		}
		throw new LdoDException("User not found!");

	}
}
