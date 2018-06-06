package pt.ist.socialsoftware.edition.core.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.socialsoftware.edition.core.domain.LdoDUser;
import pt.ist.socialsoftware.edition.core.dto.LdoDUserDTO;


@RestController
@RequestMapping("/api/user")
public class APIUserController {
    private static Logger logger = LoggerFactory.getLogger(APIUserController.class);

    @GetMapping
    public LdoDUserDTO getCurrentUser() {
        logger.debug("getCurrentUser" + LdoDUser.getAuthenticatedUser().getUsername());
        return new LdoDUserDTO(LdoDUser.getAuthenticatedUser().getUsername(), "");
    }

    @GetMapping(value = "/{username}")
    public LdoDUserDTO getUserProfile(@PathVariable(value = "username") String username) {
        logger.debug("getUserProfile");
        LdoDUserDTO user = new LdoDUserDTO(username,"");
        return user;
    }

}
