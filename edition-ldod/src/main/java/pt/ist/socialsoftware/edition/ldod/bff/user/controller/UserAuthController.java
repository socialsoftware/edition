package pt.ist.socialsoftware.edition.ldod.bff.user.controller;

import com.google.auth.oauth2.TokenVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.MainResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.user.dtos.SigninRequestDto;
import pt.ist.socialsoftware.edition.ldod.bff.user.dtos.SignupDto;
import pt.ist.socialsoftware.edition.ldod.bff.user.services.UserAuthService;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.dto.JWTAuthenticationDto;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.Message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserAuthController {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthController.class);

    @Autowired
    UserAuthService userAuthService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticationController(@RequestBody SigninRequestDto signinRequestDto) {
        logger.debug("auth request {}", signinRequestDto.toString());
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userAuthService.authenticationService(signinRequestDto));
        } catch (AuthenticationException e) {
            return getResponse(HttpStatus.UNAUTHORIZED, false, Message.BAD_CREDENTIALS.getLabel());
        }
    }


    @PostMapping("/google")
    public ResponseEntity<?> googleAuthenticationController(@RequestBody JWTAuthenticationDto tokenDto) {
        logger.debug("google auth request {}", tokenDto);
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userAuthService.googleAuthenticationService(tokenDto));
        } catch (TokenVerifier.VerificationException | IllegalArgumentException | UsernameNotFoundException e) {
            return getResponse(HttpStatus.UNAUTHORIZED, false, Message.BAD_CREDENTIALS.getLabel());
        }
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<MainResponseDto> userSignup(@RequestBody SignupDto signupForm, HttpServletRequest servletRequest)
            throws javax.mail.MessagingException {
        logger.debug("signup form :{}", signupForm.toString());
        try {
            userAuthService.signupService(signupForm, servletRequest);
        } catch (MessagingException | LdoDException e) {
            return getResponse(HttpStatus.BAD_REQUEST, false, e.getMessage());
        }
        return getResponse(HttpStatus.OK, true, "authorization");

    }

    @RequestMapping(value = "/sign-up-confirmation", method = RequestMethod.GET)
    public ResponseEntity<MainResponseDto> confirmRegistration(@RequestParam("token") String token) {
        logger.debug("confirmRegistration");
        Optional<LdoDUser> ldoDUser;
        try {
            ldoDUser = userAuthService.confirmationTokenService(token);
        } catch (LdoDException e) {
            return getResponse(HttpStatus.UNAUTHORIZED, false, e.getMessage());
        }
        return ldoDUser.isPresent()
                ? getResponse(HttpStatus.OK, true, Message.TOKEN_CONFIRMED.getLabel())
                : getResponse(HttpStatus.UNAUTHORIZED, false, Message.TOKEN_NOT_AUTHORIZED.getLabel());

    }


    private ResponseEntity<MainResponseDto> getResponse(HttpStatus status, boolean ok, String message) {
        return ResponseEntity
                .status(status)
                .body(new MainResponseDto
                        .AuthResponseDtoBuilder(ok)
                        .message(message)
                        .build());
    }
}
