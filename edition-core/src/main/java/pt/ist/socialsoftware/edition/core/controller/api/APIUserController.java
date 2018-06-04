package pt.ist.socialsoftware.edition.core.controller.api;

import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.socialsoftware.edition.core.domain.LdoD;
import pt.ist.socialsoftware.edition.core.domain.LdoDUser;
import pt.ist.socialsoftware.edition.core.dto.LdoDUserDTO;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;


import static pt.ist.socialsoftware.edition.core.security.SecurityConstants.HEADER_STRING;
import static pt.ist.socialsoftware.edition.core.security.SecurityConstants.SECRET;
import static pt.ist.socialsoftware.edition.core.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/user")
public class APIUserController {
    private static Logger logger = LoggerFactory.getLogger(APIUserController.class);

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        logger.debug("getAuthentication");
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            String user = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody().getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }

    @GetMapping
    public LdoDUserDTO getCurrentUser(HttpServletRequest request) {
        logger.debug("getCurrentUser");
        LdoD ldoD = LdoD.getInstance();
        LdoDUser user = ldoD.getUser(getAuthentication(request).getPrincipal().toString());
        logger.debug("TEST: " + user.getEmail());
        return new LdoDUserDTO(user.getUsername(), "");
    }

    @GetMapping(value = "/{username}")
    public LdoDUserDTO getUserProfile(@PathVariable(value = "username") String username) {
        logger.debug("getUserProfile");
        LdoDUserDTO user = new LdoDUserDTO(username,"pass");
        return user;
    }

}
