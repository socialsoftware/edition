package pt.ist.socialsoftware.edition.ldod.bff.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.LdoDUserDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.UserListDto;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.Role;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateUsernameException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAdminService {
    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private PasswordEncoder encoder;

    public void switchToAdminMode() {
        LdoD.getInstance().switchAdmin();
    }

    public void deleteUserSessionsService() {
        getSessionInformationList()
                .stream()
                .filter(session -> session.getPrincipal() instanceof LdoDUserDetails)
                .filter(session -> ((LdoDUserDetails) session.getPrincipal()).getUser() != LdoDUser.getAuthenticatedUser())
                .forEach(SessionInformation::expireNow);
    }

    private List<SessionInformation> getSessionInformationList() {
        return this.sessionRegistry
                .getAllPrincipals()
                .stream()
                .flatMap(principal -> this.sessionRegistry.getAllSessions(principal, false)
                        .stream()).collect(Collectors.toList());
    }

    public UserListDto listUserService() {
        return new UserListDto(
                LdoD.getInstance(),
                LdoD.getInstance()
                        .getUsersSet()
                        .stream()
                        .sorted(Comparator.comparing(u -> u.getFirstName().toLowerCase()))
                        .collect(Collectors.toList()),
                getSessionInformationList()
                        .stream()
                        .sorted(Comparator.comparing(SessionInformation::getLastRequest))
                        .collect(Collectors.toList()));
    }


    public LdoDUserDto getUserToEdit(String id) {
        LdoDUser user = FenixFramework.getDomainObject(id);
        return LdoDUserDto.LdoDUserDtoBuilder.aLdoDUserDto()
                .admin(user.getRolesSet().contains(Role.getRole(Role.RoleType.ROLE_ADMIN)))
                .user(user.getRolesSet().contains(Role.getRole(Role.RoleType.ROLE_USER)))
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .oldUsername(user.getUsername())
                .newUsername(user.getUsername())
                .build();
    }

    public void editUserService(LdoDUserDto userEdit) throws LdoDDuplicateUsernameException {
        if (!userEdit.getNewUsername().equals(userEdit.getOldUsername())
                && LdoD.getInstance().getUser(userEdit.getNewUsername()) != null)
            throw new LdoDDuplicateUsernameException(String.format("Duplicated username %s", userEdit.getNewPassword()));

        LdoDUser user = LdoD.getInstance().getUser(userEdit.getOldUsername());
        user.update(this.encoder, userEdit);

    }

    public UserListDto activeUserService(String externalId) {
        LdoDUser user = FenixFramework.getDomainObject(externalId);
        user.switchActive();
        return this.listUserService();
    }

    public UserListDto removeUserService(String externalId) {
        LdoDUser user = FenixFramework.getDomainObject(externalId);
        user.remove();
        return this.listUserService();
    }
}
