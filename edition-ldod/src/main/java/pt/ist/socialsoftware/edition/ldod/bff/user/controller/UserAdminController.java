package pt.ist.socialsoftware.edition.ldod.bff.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.MainResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.user.dtos.LdoDUserDto;
import pt.ist.socialsoftware.edition.ldod.bff.user.services.UserAdminService;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.UserListDto;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateUsernameException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDLoadException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.Message;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/admin/user")
@PreAuthorize("hasPermission('','ADMIN')")
public class UserAdminController {
    private static final Logger logger = LoggerFactory.getLogger(UserAdminController.class);
    @Autowired
    UserAdminService service;

    @RequestMapping(value = "/sign-up-authorization", method = RequestMethod.GET)
    public ResponseEntity<MainResponseDto> authorizeRegistration(HttpServletRequest servletRequest, @RequestParam("token") String token) {
        logger.debug("authorizeRegistration");
        try {
            service.registerTokenService(token, servletRequest);
        } catch (LdoDException | javax.mail.MessagingException e) {
            return getResponse(HttpStatus.BAD_REQUEST, false, e.getMessage());
        }
        return getResponse(HttpStatus.OK, true, Message.TOKEN_AUTHORIZED.getLabel());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/switch")
    public ResponseEntity<MainResponseDto> switchAdminMode() {
        logger.debug("switchAdminMode");
        return getResponse(HttpStatus.OK, service.switchToAdminMode(), "");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/sessions-delete")
    public ResponseEntity<?> deleteUserSessions(@AuthenticationPrincipal LdoDUserDetails currentUser) {
        logger.debug("deleteUserSessions");
        return ResponseEntity.status(HttpStatus.OK).body(service.deleteUserSessionsService(currentUser.getUser()));

    }

    @GetMapping("/list")
    public ResponseEntity<?> listUser() {
        logger.debug("List users");
        return ResponseEntity.status(HttpStatus.OK).body(service.listUserAndSessionsService());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/edit/{externalId}")
    public ResponseEntity<LdoDUserDto> editUser(@PathVariable("externalId") String externalId) {
        logger.debug("editUserForm externalId:{}", externalId);
        return ResponseEntity.status(HttpStatus.OK).body(service.getUserToEdit(externalId));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/edit")
    public ResponseEntity<?> editUser(@RequestBody LdoDUserDto userEdit) {
        logger.debug("editUser:{}", userEdit.toString());
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.editUserService(userEdit));
        } catch (LdoDDuplicateUsernameException e) {
            return getResponse(HttpStatus.OK, false, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/active/{externalId}")
    public ResponseEntity<MainResponseDto> activeUser(@PathVariable("externalId") String externalId) {
        return getResponse(HttpStatus.OK, service.activeUserService(externalId), "");
    }


    @RequestMapping(method = RequestMethod.POST, value = "/delete/{externalId}")
    public ResponseEntity<?> removeUser(@PathVariable("externalId") String externalId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.removeUserService(externalId));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/upload-users")
    public ResponseEntity<?> uploadUsersXML(@RequestParam("file") MultipartFile file)
            throws LdoDLoadException {
        try {
            service.uploadUsersXMLService(file);
        } catch (LdoDException ex) {
            return getResponse(HttpStatus.BAD_REQUEST, false, ex.getMessage());
        }
        return getResponse(HttpStatus.OK, true, "Users uploaded");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/export-users")
    public ResponseEntity<?> exportUsers() throws IOException {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(service.exportUsersXMLService());
        } catch (IOException | LdoDException ex) {
            return getResponse(HttpStatus.BAD_REQUEST, false, ex.getMessage());
        }
    }

    private ResponseEntity<MainResponseDto> getResponse(HttpStatus status, boolean ok, String message) {
        return ResponseEntity
                .status(status)
                .body(new MainResponseDto(ok, message));
    }
}
