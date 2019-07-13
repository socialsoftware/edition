package pt.ist.socialsoftware.edition.ldod.frontend.user;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.Role;
import pt.ist.socialsoftware.edition.ldod.domain.Role.RoleType;
import pt.ist.socialsoftware.edition.ldod.domain.User;
import pt.ist.socialsoftware.edition.ldod.domain.UserModule;
import pt.ist.socialsoftware.edition.ldod.frontend.user.forms.EditUserForm;
import pt.ist.socialsoftware.edition.ldod.frontend.user.validator.EditUserValidator;
import pt.ist.socialsoftware.edition.ldod.user.feature.inout.UsersXMLExport;
import pt.ist.socialsoftware.edition.ldod.user.feature.inout.UsersXMLImport;
import pt.ist.socialsoftware.edition.ldod.user.feature.security.UserModuleUserDetails;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDLoadException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class UserAdminController {
    private static final Logger logger = LoggerFactory.getLogger(UserAdminController.class);

    @Inject
    private SessionRegistry sessionRegistry;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.POST, value = "/switch")
    public String switchAdminMode() {
        logger.debug("switchAdminMode");

        UserModule.getInstance().switchAdmin();

        return "redirect:/admin/user/list";
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.POST, value = "/sessions/delete")
    public String deleteUserSessons() {
        logger.debug("deleteUserSessons");

        List<SessionInformation> activeSessions = new ArrayList<>();
        for (Object principal : this.sessionRegistry.getAllPrincipals()) {
            activeSessions.addAll(this.sessionRegistry.getAllSessions(principal, false));
        }

        for (SessionInformation session : activeSessions) {
            if (session.getPrincipal() instanceof UserModuleUserDetails) {
                User user = ((UserModuleUserDetails) session.getPrincipal()).getUser();

                if (user != User.getAuthenticatedUser()) {
                    session.expireNow();
                }
                // if
                // (!user.getRolesSet().contains(Role.getRole(RoleType.ROLE_ADMIN)))
                // {
                // session.expireNow();
                // }
            }
        }

        return "redirect:/admin/user/list";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String listUser(Model model) {
        List<SessionInformation> activeSessions = new ArrayList<>();
        for (Object principal : this.sessionRegistry.getAllPrincipals()) {
            activeSessions.addAll(this.sessionRegistry.getAllSessions(principal, false));
        }
        activeSessions.stream().sorted((s1, s2) -> s1.getLastRequest().compareTo(s2.getLastRequest()));

        model.addAttribute("userModule", UserModule.getInstance());
        model.addAttribute("users",
                UserModule.getInstance().getUsersSet().stream()
                        .sorted((u1, u2) -> u1.getFirstName().toLowerCase().compareTo(u2.getFirstName().toLowerCase()))
                        .collect(Collectors.toList()));
        model.addAttribute("sessions", activeSessions.stream()
                .sorted((s1, s2) -> s2.getLastRequest().compareTo(s1.getLastRequest())).collect(Collectors.toList()));
        return "admin/listUsers";

    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.GET, value = "/user/edit")
    public EditUserForm editUserForm(@RequestParam("externalId") String externalId) {
        logger.debug("editUserForm externalId:{}", externalId);

        User user = FenixFramework.getDomainObject(externalId);

        EditUserForm form = new EditUserForm();
        form.setOldUsername(user.getUsername());
        form.setNewUsername(user.getUsername());
        form.setFirstName(user.getFirstName());
        form.setLastName(user.getLastName());
        form.setEmail(user.getEmail());
        form.setUser(user.getRolesSet().contains(Role.getRole(RoleType.ROLE_USER)));
        form.setAdmin(user.getRolesSet().contains(Role.getRole(RoleType.ROLE_ADMIN)));
        form.setEnabled(user.getEnabled());

        return form;
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.POST, value = "/user/edit")
    public String editUser(@Valid EditUserForm form, BindingResult formBinding) {
        logger.debug("editUser username:{}", form.getOldUsername());

        EditUserValidator validator = new EditUserValidator();
        validator.validate(form, formBinding);

        if (formBinding.hasErrors()) {
            return null;
        }

        User user = UserModule.getInstance().getUser(form.getOldUsername());

        user.update(this.passwordEncoder, form.getOldUsername(), form.getNewUsername(), form.getFirstName(),
                form.getLastName(), form.getEmail(), form.getNewPassword(), form.isUser(), form.isAdmin(),
                form.isEnabled());

        return "redirect:/admin/user/list";
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.POST, value = "/user/active")
    public String activeUser(@RequestParam("externalId") String externalId) {
        User user = FenixFramework.getDomainObject(externalId);

        user.switchActive();

        return "redirect:/admin/user/list";
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.POST, value = "/user/delete")
    public String removeUser(@RequestParam("externalId") String externalId) {
        User user = FenixFramework.getDomainObject(externalId);

        user.remove();

        return "redirect:/admin/user/list";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/export/users")
    public void exportUsers(HttpServletResponse response) {
        UsersXMLExport generator = new UsersXMLExport();

        try {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            // get your file as InputStream
            InputStream is = IOUtils.toInputStream(generator.export(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=users-" + timeStamp + ".xml");
            response.setContentType("application/xml");
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            System.out.println("Error writing file to output stream. Filename was '{}'");
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/load/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadUsersXML(RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile file)
            throws LdoDLoadException {
        if (file == null) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Deve escolher um ficheiro");
            return "redirect:/admin/loadForm";
        }

        UsersXMLImport loader = new UsersXMLImport();
        try {
            loader.importUsers(file.getInputStream());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Problemas com o ficheiro, tipo ou formato");
            return "redirect:/admin/loadForm";
        }

        redirectAttributes.addFlashAttribute("error", false);
        redirectAttributes.addFlashAttribute("message", "Utilizadores Carregados");
        return "redirect:/admin/loadForm";
    }


    @RequestMapping(method = RequestMethod.POST, value = "/createTestUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createTestUsers(Model model) {
        logger.debug("createTestUsers");
        UserModule.getInstance().createTestUsers(this.passwordEncoder);
        return "redirect:/admin/user/list";
    }

}
