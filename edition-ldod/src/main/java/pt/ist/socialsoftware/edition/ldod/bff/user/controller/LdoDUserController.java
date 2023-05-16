package pt.ist.socialsoftware.edition.ldod.bff.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.MainResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.user.dtos.LdoDUserDto;
import pt.ist.socialsoftware.edition.ldod.bff.user.services.LdoDUserService;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.forms.ChangePasswordForm;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;
import pt.ist.socialsoftware.edition.ldod.shared.exception.Message;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class LdoDUserController {
    private static final Logger logger = LoggerFactory.getLogger(LdoDUserController.class);

    @Autowired
    LdoDUserService service;

    @GetMapping
    public ResponseEntity<?> getCurrentUserController(@AuthenticationPrincipal LdoDUserDetails currentUser) {
        logger.debug("getCurrentUser {}", currentUser == null ? "null" : currentUser.getUsername());
        return currentUser != null
                ? ResponseEntity.status(HttpStatus.OK).body(service.getUserService(currentUser))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MainResponseDto(false, Message.USER_NOT_FOUND.getLabel()));
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<LdoDUserDto> getUserProfileController(@PathVariable(value = "username") String username) {
        logger.debug("getUserProfile");
        Optional<LdoDUserDto> userDto = service.getUserByUsernameService(username);
        return userDto
                .map(ldoDUserDto -> ResponseEntity.status(HttpStatus.OK).body(ldoDUserDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<MainResponseDto> changePassword(@RequestBody ChangePasswordForm form, BindingResult formBinding) {
        logger.debug("changePassword username:{}", form.getUsername());
        Optional<LdoDUser> user = service.changePasswordService(form, formBinding);
        return user.isPresent()
                ? getResponse(HttpStatus.OK, true, Message.PASSWORD_CHANGED.getLabel())
                : getResponse(HttpStatus.BAD_REQUEST, false, Message.BAD_CREDENTIALS.getLabel());

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
