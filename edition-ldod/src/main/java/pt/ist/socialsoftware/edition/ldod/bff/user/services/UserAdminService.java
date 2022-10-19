package pt.ist.socialsoftware.edition.ldod.bff.user.services;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.bff.user.dtos.LdoDUserDto;
import pt.ist.socialsoftware.edition.ldod.bff.user.dtos.ManageUsersDto;
import pt.ist.socialsoftware.edition.ldod.bff.user.dtos.SessionDto;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.Role;
import pt.ist.socialsoftware.edition.ldod.export.UsersXMLExport;
import pt.ist.socialsoftware.edition.ldod.loaders.UsersXMLImport;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateUsernameException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDLoadException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserAdminService {
    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserAuthService userAuthService;


    public void registerTokenService(String token, HttpServletRequest servletRequest) throws MessagingException, LdoDException {
        userAuthService.registerTokenService(token, servletRequest);
    }

    public boolean switchToAdminMode() {
        LdoD.getInstance().switchAdmin();
        return LdoD.getInstance().getAdmin();
    }

    public ManageUsersDto deleteUserSessionsService(LdoDUser user) {

        Optional<SessionInformation> currentSession = getSessionInformationList()
                .stream()
                .filter(sessionInformation -> ((LdoDUserDetails) sessionInformation.getPrincipal()).getUser().equals(user))
                .min(Comparator.comparing(SessionInformation::getLastRequest));

        getSessionInformationList()
                .stream()
                .filter(session -> session.getPrincipal() instanceof LdoDUserDetails)
                .filter(session -> !currentSession.isPresent() || !session.equals(currentSession.get()))
                .forEach(SessionInformation::expireNow);
        return listSessionsService();
    }

    private List<SessionInformation> getSessionInformationList() {
        return this.sessionRegistry
                .getAllPrincipals()
                .stream()
                .flatMap(principal -> this.sessionRegistry.getAllSessions(principal, false)
                        .stream()).collect(Collectors.toList());
    }


    private ManageUsersDto listUsersService() {
        return ManageUsersDto.ManageUsersDtoBuilder.aManageUsersDto()
                .userList(
                        LdoD.getInstance()
                                .getUsersSet()
                                .stream()
                                .sorted(Comparator.comparing(u -> u.getFirstName().toLowerCase()))
                                .map(LdoDUserDto::new)
                                .collect(Collectors.toList()))
                .build();
    }

    private ManageUsersDto listSessionsService() {
        return ManageUsersDto.ManageUsersDtoBuilder.aManageUsersDto().sessionList(getSessionInformationList()
                .stream()
                .sorted(Comparator.comparing(SessionInformation::getLastRequest))
                .map(SessionDto::new)
                .collect(Collectors.toList())).build();
    }

    public ManageUsersDto listUserAndSessionsService() {
        return ManageUsersDto.ManageUsersDtoBuilder.aManageUsersDto().ldoDAdmin(LdoD.getInstance().getAdmin())
                .userList(listUsersService().getUserList())
                .sessionList(listSessionsService().getSessionList())
                .build();
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
                .build();
    }

    public ManageUsersDto editUserService(LdoDUserDto userEdit) throws LdoDDuplicateUsernameException {
        if (!userEdit.getNewUsername().equals(userEdit.getOldUsername())
                && LdoD.getInstance().getUser(userEdit.getNewUsername()) != null)
            throw new LdoDDuplicateUsernameException(String.format("Duplicated username %s", userEdit.getNewPassword()));

        LdoDUser user = LdoD.getInstance().getUser(userEdit.getOldUsername());
        user.update(this.encoder, userEdit);
        return listUsersService();
    }

    public boolean activeUserService(String externalId) {
        LdoDUser user = FenixFramework.getDomainObject(externalId);
        user.switchActive();
        return user.getActive();
    }

    public ManageUsersDto removeUserService(String externalId) {
        LdoDUser user = FenixFramework.getDomainObject(externalId);
        user.remove();
        return listUsersService();
    }

    public void uploadUsersXMLService(MultipartFile file) throws LdoDLoadException {

        try {
            new UsersXMLImport().importUsers(file.getInputStream());
        } catch (LdoDLoadException e) {
            throw new LdoDException(String.format("%s %s", e.getMessage(), file.getOriginalFilename()));
        } catch (IOException e) {
            throw new LdoDException(String.format("Invalid file %s", file.getOriginalFilename()));
        }
    }

    public Map<String, byte[]> exportUsersXMLService() throws IOException {
        return Collections.singletonMap("xmlData", IOUtils.toByteArray(IOUtils.toInputStream(new UsersXMLExport().export(), "UTF-8")));
    }
}
