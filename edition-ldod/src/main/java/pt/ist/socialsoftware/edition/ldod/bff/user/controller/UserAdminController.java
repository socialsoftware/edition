package pt.ist.socialsoftware.edition.ldod.bff.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.AuthResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.LdoDUserDto;
import pt.ist.socialsoftware.edition.ldod.bff.user.services.UserAdminService;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.UserListDto;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateUsernameException;

@RestController
@RequestMapping("/api/user/admin")
public class UserAdminController {
    private static final Logger logger = LoggerFactory.getLogger(MfesUserController.class);
    @Autowired
    UserAdminService service;

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.POST, value = "/switch")
    public ResponseEntity<?> switchAdminMode() {
        logger.debug("switchAdminMode");
        service.switchToAdminMode();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.POST, value = "/sessions/delete")
    public ResponseEntity<?> deleteUserSessions() {
        logger.debug("deleteUserSessions");
        service.deleteUserSessionsService();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserListDto> listUser(Model model) {
        logger.debug("List users");
        return ResponseEntity.status(HttpStatus.OK).body(service.listUserService());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<LdoDUserDto> editUser(@RequestParam("externalId") String externalId) {
        logger.debug("editUserForm externalId:{}", externalId);
        return ResponseEntity.status(HttpStatus.OK).body(service.getUserToEdit(externalId));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> editUser(LdoDUserDto userEdit) {
        logger.debug("editUser username:{}", userEdit.getOldUsername());
        try {
            service.editUserService(userEdit);
        } catch (LdoDDuplicateUsernameException e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new AuthResponseDto
                            .AuthResponseDtoBuilder(false)
                            .message(e.getMessage())
                            .build());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/active")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserListDto> activeUser(@RequestParam("externalId") String externalId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.activeUserService(externalId));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserListDto> removeUser(@RequestParam("externalId") String externalId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.removeUserService(externalId));
    }
}
