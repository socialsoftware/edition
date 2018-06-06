package pt.ist.socialsoftware.edition.core.controller.api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.core.domain.LdoDUser;
import pt.ist.socialsoftware.edition.core.dto.JWTAuthenticationDTO;
import pt.ist.socialsoftware.edition.core.security.JwtTokenProvider;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@RestController
@RequestMapping("/api/user/session")
public class AuthResource {
    private static Logger log = LoggerFactory.getLogger(AuthResource.class);
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @GetMapping
    public ResponseEntity<JWTAuthenticationDTO> session(Principal user) {
        String name = user == null ? null : user.getName();
        log.debug("resource " + user.getName());
        log.debug("user"+ LdoDUser.getAuthenticatedUser().getUsername());
        log.debug("test " + LdoDUser.getAuthenticatedUser().getSocialMediaId());

        String jwt = this.tokenProvider.generateToken(SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok(new JWTAuthenticationDTO(jwt));
    }

    @DeleteMapping
    public void logout(HttpSession session) {
        session.invalidate();
    }
}

