package pt.ist.socialsoftware.edition.ldod.bff.user.controller;

import com.google.auth.oauth2.TokenVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.AuthResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.SigninRequestDto;
import pt.ist.socialsoftware.edition.ldod.bff.user.services.UserAuthService;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.dto.JWTAuthenticationDto;
import pt.ist.socialsoftware.edition.ldod.dto.ldodMfes.SignupDto;
import pt.ist.socialsoftware.edition.ldod.forms.ChangePasswordForm;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserAuthController {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthController.class);

    @Autowired
    UserAuthService userAuthService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticationController(@Valid @RequestBody SigninRequestDto signinRequestDto) {
        logger.debug("auth request {}", signinRequestDto.toString());
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userAuthService.authenticationService(signinRequestDto));
        } catch (AuthenticationException e) {
            return getResponse(HttpStatus.UNAUTHORIZED, false, e.getMessage());
        }
    }


    @PostMapping("/google")
    public ResponseEntity<?> googleAuthenticationController(@RequestBody JWTAuthenticationDto tokenDto) {
        logger.debug("google auth request {}", tokenDto.toString());
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userAuthService.googleAuthenticationService(tokenDto));
        } catch (TokenVerifier.VerificationException | IllegalArgumentException e) {
            return getResponse(HttpStatus.UNAUTHORIZED, false, e.getMessage());
        }
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<AuthResponseDto> userSignup(@RequestBody SignupDto signupForm, HttpServletRequest servletRequest)
            throws javax.mail.MessagingException {
        logger.debug("signup form :{}", signupForm.toString());
        try {
            userAuthService.signupService(signupForm, servletRequest);
        } catch (MessagingException | LdoDException e) {
            return getResponse(HttpStatus.BAD_REQUEST, false, e.getMessage());
        }
        return getResponse(HttpStatus.OK, true, "authorization");

    }

    @RequestMapping(value = "/signup/auth", method = RequestMethod.GET)
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AuthResponseDto> authorizeRegistration(HttpServletRequest servletRequest, @RequestParam("token") String token) {
        logger.debug("authorizeRegistration");
        try {
            userAuthService.registerTokenService(token, servletRequest);
        } catch (LdoDException | javax.mail.MessagingException e) {
            return getResponse(HttpStatus.BAD_REQUEST, false, e.getMessage());
        }
        return getResponse(HttpStatus.OK, true, "tokenAuthorized");
    }

    @RequestMapping(value = "/signup/confirm", method = RequestMethod.GET)
    public ResponseEntity<AuthResponseDto> confirmRegistration(@RequestParam("token") String token) {
        logger.debug("confirmRegistration");
        Optional<LdoDUser> ldoDUser;
        try {
            ldoDUser = userAuthService.confirmationTokenService(token);
        } catch (LdoDException e) {
            return getResponse(HttpStatus.UNAUTHORIZED, false, e.getMessage());
        }
        return ldoDUser.isPresent()
                ? getResponse(HttpStatus.OK, true, "tokenConfirmed")
                : getResponse(HttpStatus.UNAUTHORIZED, false, "tokenUnauthorized");

    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<AuthResponseDto> changePassword(@RequestBody ChangePasswordForm form, BindingResult formBinding) {
        logger.debug("changePassword username:{}", form.getUsername());
        Optional<LdoDUser> user = userAuthService.changePasswordService(form, formBinding);
        return user.isPresent()
                ? getResponse(HttpStatus.OK, true, "passwordChanged")
                : getResponse(HttpStatus.BAD_REQUEST, false, "badCredentials");

    }
    private ResponseEntity<AuthResponseDto> getResponse(HttpStatus status, boolean ok, String message) {
        return ResponseEntity
                .status(status)
                .body(new AuthResponseDto
                        .AuthResponseDtoBuilder(false)
                        .message(message)
                        .build());
    }
}
