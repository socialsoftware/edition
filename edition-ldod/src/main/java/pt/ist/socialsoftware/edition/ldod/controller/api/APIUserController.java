package pt.ist.socialsoftware.edition.ldod.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.ist.socialsoftware.edition.ldod.domain.LdoDUserManager;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.dto.LdoDUserDTO;
import pt.ist.socialsoftware.edition.text.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.user.security.LdoDUserDetails;

@RestController
@RequestMapping("/api/user")
public class APIUserController {
	private static Logger logger = LoggerFactory.getLogger(APIUserController.class);

	@GetMapping
	public LdoDUserDTO getCurrentUser(@AuthenticationPrincipal LdoDUserDetails currentUser) {
		logger.debug("getCurrentUser {}", currentUser == null ? "null" : currentUser.getUsername());
		if (currentUser != null) {
			return new LdoDUserDTO((LdoDUser) currentUser.getUser());
		}
		throw new LdoDException("No current user!");
	}

	@GetMapping(value = "/{username}")
	public LdoDUserDTO getUserProfile(@PathVariable(value = "username") String username) {
		logger.debug("getUserProfile");
		LdoDUser user = (LdoDUser) LdoDUserManager.getInstance().getUser(username);
		if (user != null) {
			return new LdoDUserDTO(user);
		}
		throw new LdoDException("User not found!");

	}
}
