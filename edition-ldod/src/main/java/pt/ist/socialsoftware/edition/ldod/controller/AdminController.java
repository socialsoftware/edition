package pt.ist.socialsoftware.edition.ldod.controller;

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
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.UploadFragmentDto;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.domain.Role.RoleType;
import pt.ist.socialsoftware.edition.ldod.export.ExpertEditionTEIExport;
import pt.ist.socialsoftware.edition.ldod.export.UsersXMLExport;
import pt.ist.socialsoftware.edition.ldod.export.WriteVirtualEditionsToFile;
import pt.ist.socialsoftware.edition.ldod.forms.EditUserForm;
import pt.ist.socialsoftware.edition.ldod.loaders.*;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDLoadException;
import pt.ist.socialsoftware.edition.ldod.social.aware.AwareAnnotationFactory;
import pt.ist.socialsoftware.edition.ldod.social.aware.CitationDetecter;
import pt.ist.socialsoftware.edition.ldod.social.aware.TweetFactory;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.ldod.validator.EditUserValidator;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Inject
    private SessionRegistry sessionRegistry;

    @Inject
    private PasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.GET, value = "/loadForm")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadForm(Model model) {
        Boolean error = (Boolean) model.asMap().get("error");
        model.addAttribute("error", error);
        String message = (String) model.asMap().get("message");
        model.addAttribute("message", message);
        return "admin/loadForm";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/load/corpus")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadTEICorpus(RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile file)
            throws LdoDLoadException {

        if (file == null) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Deve escolher um ficheiro");
            return "redirect:/admin/loadForm";
        }

        LoadTEICorpus loader = new LoadTEICorpus();
        try {
            loader.loadTEICorpus(file.getInputStream());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Problemas com o ficheiro, tipo ou formato");
            return "redirect:/admin/loadForm";
        } catch (LdoDException ldodE) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", ldodE.getMessage());
            return "redirect:/admin/loadForm";
        }

        redirectAttributes.addFlashAttribute("error", false);
        redirectAttributes.addFlashAttribute("message", "Corpus carregado");
        return "redirect:/admin/loadForm";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/load/fragmentsAtOnce")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadTEIFragmentsAtOnce(RedirectAttributes redirectAttributes,
                                         @RequestParam("file") MultipartFile file) throws LdoDLoadException {


        if (file == null) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Deve escolher um ficheiro");
            return "redirect:/admin/loadForm";
        }
        List<UploadFragmentDto> uploadFragmentDtoList;

        try {
            uploadFragmentDtoList = new LoadTEIFragments().loadFragmentsAtOnce(file.getInputStream());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Problemas com o ficheiro, tipo ou formato");
            return "redirect:/admin/loadForm";
        } catch (LdoDException ldodE) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", ldodE.getMessage());
            return "redirect:/admin/loadForm";
        }

        String loadedResult = getLoadedResult(uploadFragmentDtoList);
        String unloadedResult = getUnloadedResult(uploadFragmentDtoList);

        redirectAttributes.addFlashAttribute("error", false);
        redirectAttributes.addFlashAttribute("message", String.format("%s %s", loadedResult, unloadedResult));
        return "redirect:/admin/loadForm";

    }

    @RequestMapping(method = RequestMethod.POST, value = "/load/fragmentsStepByStep")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadTEIFragmentsStepByStep(RedirectAttributes redirectAttributes,
                                             @RequestParam("files") MultipartFile[] files) throws LdoDLoadException {


        if (files == null) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Deve escolher um ficheiro");
            return "redirect:/admin/loadForm";
        }

        List<UploadFragmentDto> uploadFragmentDtoList = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                new LoadTEIFragments().loadFragmentsStepByStep(file.getInputStream(), uploadFragmentDtoList);

            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", true);
                redirectAttributes.addFlashAttribute("message", "Problemas com o ficheiro, tipo ou formato");
                return "redirect:/admin/loadForm";
            } catch (LdoDException ldodE) {
                redirectAttributes.addFlashAttribute("error", true);
                redirectAttributes.addFlashAttribute("message", ldodE.getMessage());
                return "redirect:/admin/loadForm";
            }
        }
        String loadedResult = getLoadedResult(uploadFragmentDtoList);
        String unloadedResult = getUnloadedResult(uploadFragmentDtoList);

        redirectAttributes.addFlashAttribute("error", false);
        redirectAttributes.addFlashAttribute("message", String.format("%s %s", loadedResult, unloadedResult));
        return "redirect:/admin/loadForm";
    }

    private String getUnloadedResult(List<UploadFragmentDto> uploadFragsList) {

        List<UploadFragmentDto> notUploadedFragmentsList = uploadFragsList.stream()
                .filter(uploadFragmentDto -> !uploadFragmentDto.isUploaded())
                .collect(Collectors.toList());

        String unloadedString = String.format("<br>Already uploaded fragments: %d", notUploadedFragmentsList.size());
        return !notUploadedFragmentsList.isEmpty()
                ? notUploadedFragmentsList
                .stream()
                .map(uploadFragmentDto -> String.format("<br>%s", uploadFragmentDto.toString()))
                .reduce(unloadedString, (result, unLoadedFrag) -> String.format("%s %s", result, unLoadedFrag))
                : "";
    }

    private String getLoadedResult(List<UploadFragmentDto> uploadFragsList) {

        List<UploadFragmentDto> uploadedFragmentsList = uploadFragsList.stream()
                .filter(UploadFragmentDto::isUploaded)
                .collect(Collectors.toList());

        String loadedString = String.format("New uploaded fragments: %d", uploadedFragmentsList.size());

        return uploadedFragmentsList
                .stream()
                .map(uploadFragmentDto -> String.format("<br>%s", uploadFragmentDto.toString()))
                .reduce(loadedString, (result, loadedFrag) -> String.format("%s %s", result, loadedFrag)).concat("<br>");
    }


    @RequestMapping(method = RequestMethod.GET, value = "/fragment/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteFragmentsList(Model model) {
        model.addAttribute("fragments", LdoD.getInstance().getFragmentsSet());
        return "admin/deleteFragment";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fragment/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteFragment(Model model, @RequestParam("externalId") String externalId) {
        Fragment fragment = FenixFramework.getDomainObject(externalId);
        if (fragment == null) {
            return "redirect:/error";
        } else if (LdoD.getInstance().getFragmentsSet().size() >= 1) {
            fragment.remove();
        }
        return "redirect:/admin/fragment/list";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fragment/deleteAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteAllFragments(Model model) {
        for (Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
            fragment.remove();
        }
        return "redirect:/admin/fragment/list";
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.POST, value = "/switch")
    public String switchAdminMode() {
        logger.debug("switchAdminMode");

        LdoD ldoD = LdoD.getInstance();
        ldoD.switchAdmin();

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
            if (session.getPrincipal() instanceof LdoDUserDetails) {
                LdoDUser ldoDUser = ((LdoDUserDetails) session.getPrincipal()).getUser();

                if (ldoDUser != LdoDUser.getAuthenticatedUser()) {
                    session.expireNow();
                }
                // if
                // (!ldoDUser.getRolesSet().contains(Role.getRole(RoleType.ROLE_ADMIN)))
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

        model.addAttribute("ldoD", LdoD.getInstance());
        model.addAttribute("users",
                LdoD.getInstance().getUsersSet().stream()
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

        LdoDUser user = FenixFramework.getDomainObject(externalId);

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

        LdoDUser user = LdoD.getInstance().getUser(form.getOldUsername());

        user.update(this.passwordEncoder, form.getOldUsername(), form.getNewUsername(), form.getFirstName(),
                form.getLastName(), form.getEmail(), form.getNewPassword(), form.isUser(), form.isAdmin(),
                form.isEnabled());

        return "redirect:/admin/user/list";
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.POST, value = "/user/active")
    public String activeUser(@RequestParam("externalId") String externalId) {
        LdoDUser user = FenixFramework.getDomainObject(externalId);

        user.switchActive();

        return "redirect:/admin/user/list";
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.POST, value = "/user/delete")
    public String removeUser(@RequestParam("externalId") String externalId) {
        LdoDUser user = FenixFramework.getDomainObject(externalId);

        user.remove();

        return "redirect:/admin/user/list";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exportForm")
    public String exportForm(Model model) {
        return "admin/exportForm";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/exportSearch")
    public String exportSearch(Model model, @RequestParam("query") String query) {

        LdoD ldoD = LdoD.getInstance();

        List<String> frags = new ArrayList<>();
        int n = 0;

        if (query.compareTo("") != 0) {
            for (Fragment frag : ldoD.getFragmentsSet()) {
                if (frag.getTitle().contains(query)) {
                    frags.add("<a href=\"/fragments/fragment/" + frag.getXmlId() + "\">"
                            + frag.getTitle().replace(query, "<b><u>" + query + "</u></b>") + "</a>");
                    n++;
                }
            }
        }

        model.addAttribute("query", query);
        model.addAttribute("nResults", n);
        model.addAttribute("frags", frags);

        return "admin/exportForm";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/exportSearchResult")
    public void exportSearchResult(HttpServletResponse response, Model model, @RequestParam("query") String query) {

        LdoD ldoD = LdoD.getInstance();

        Map<Fragment, Set<FragInter>> searchResult = new HashMap<>();

        for (Fragment frag : ldoD.getFragmentsSet()) {
            if (frag.getTitle().contains(query)) {
                Set<FragInter> inters = new HashSet<>();
                for (FragInter inter : frag.getFragmentInterSet()) {
                    if (inter.getSourceType() != Edition.EditionType.VIRTUAL) {
                        inters.add(inter);
                    }
                }
                searchResult.put(frag, inters);
            }
        }

        ExpertEditionTEIExport teiGenerator = new ExpertEditionTEIExport();
        teiGenerator.generate(searchResult);

        try {
            // get your file as InputStream
            InputStream is = IOUtils.toInputStream(teiGenerator.getXMLResult(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=tei.xml");
            response.setContentType("application/tei+xml");
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exportAll")
    public void exportAll(HttpServletResponse response) {

        LdoD ldoD = LdoD.getInstance();

        Map<Fragment, Set<FragInter>> searchResult = new HashMap<>();

        for (Fragment frag : ldoD.getFragmentsSet()) {
            Set<FragInter> inters = new HashSet<>();

            for (FragInter inter : frag.getFragmentInterSet()) {
                if (inter.getSourceType() != Edition.EditionType.VIRTUAL) {

                    inters.add(inter);
                }
            }
            searchResult.put(frag, inters);
        }

        ExpertEditionTEIExport teiGenerator = new ExpertEditionTEIExport();
        teiGenerator.generate(searchResult);

        try {
            // get your file as InputStream
            InputStream is = IOUtils.toInputStream(teiGenerator.getXMLResult(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=tei.xml");
            response.setContentType("application/tei+xml");
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exportRandom")
    public void exportRandom(HttpServletResponse response) {

        LdoD ldoD = LdoD.getInstance();

        Map<Fragment, Set<FragInter>> searchResult = new HashMap<>();

        List<Fragment> fragments = new ArrayList<>(LdoD.getInstance().getFragmentsSet());

        List<String> fragsRandom = new ArrayList<>();

        int size = fragments.size();

        int fragPos = 0;
        Fragment frag = null;

        for (int i = 0; i < 3; i++) {
            fragPos = (int) (Math.random() * size);
            frag = fragments.get(fragPos);

            fragsRandom.add("<a href=\"/fragments/fragment/" + frag.getExternalId() + "\">" + frag.getTitle() + "</a>");

            Set<FragInter> inters = new HashSet<>();
            for (FragInter inter : frag.getFragmentInterSet()) {
                if (inter.getSourceType() != Edition.EditionType.VIRTUAL) {

                    inters.add(inter);
                }
            }
            searchResult.put(frag, inters);
        }

        ExpertEditionTEIExport teiGenerator = new ExpertEditionTEIExport();
        teiGenerator.generate(searchResult);

        try {
            // get your file as InputStream
            InputStream is = IOUtils.toInputStream(teiGenerator.getXMLResult(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=tei.xml");
            response.setContentType("application/tei+xml");
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
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

    @RequestMapping(method = RequestMethod.GET, value = "/export/virtualeditions")
    public void exportVirtualEditions(HttpServletResponse response) throws IOException {
        WriteVirtualEditionsToFile write = new WriteVirtualEditionsToFile();
        String filename = write.export();

        String exportDir = PropertiesManager.getProperties().getProperty("export.dir");
        File directory = new File(exportDir);
        File file = new File(directory, filename);
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setHeader("Content-Type", "application/zip");
        InputStream is = new FileInputStream(file);
        FileCopyUtils.copy(IOUtils.toByteArray(is), response.getOutputStream());
        response.flushBuffer();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/load/virtual-corpus")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadVirtualCorpus(RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile file)
            throws LdoDLoadException {
        if (file == null) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Deve escolher um ficheiro");
            return "redirect:/admin/loadForm";
        }

        VirtualEditionsTEICorpusImport loader = new VirtualEditionsTEICorpusImport();
        try {
            loader.importVirtualEditionsCorpus(file.getInputStream());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Problemas com o ficheiro, tipo ou formato");
            return "redirect:/admin/loadForm";
        }

        redirectAttributes.addFlashAttribute("error", false);
        redirectAttributes.addFlashAttribute("message", "Corpus das edições virtuais carregado");
        return "redirect:/admin/loadForm";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/load/virtual-fragments")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadVirtualFragments(RedirectAttributes redirectAttributes,
                                       @RequestParam("files") MultipartFile[] files) throws LdoDLoadException {
        if (files == null) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Deve escolher um ficheiro");
            return "redirect:/admin/loadForm";
        }

        VirtualEditionFragmentsTEIImport loader = new VirtualEditionFragmentsTEIImport();

        String list = "";
        int total = 0;
        for (MultipartFile file : files) {
            try {
                list = list + "<br/>" + loader.importFragmentFromTEI(file.getInputStream());
                total++;
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", true);
                redirectAttributes.addFlashAttribute("message", "Problemas com o ficheiro, tipo ou formato");
                return "redirect:/admin/loadForm";
            } catch (LdoDException ldodE) {
                redirectAttributes.addFlashAttribute("error", true);
                redirectAttributes.addFlashAttribute("message", ldodE.getMessage());
                return "redirect:/admin/loadForm";
            }
        }

        redirectAttributes.addFlashAttribute("error", false);
        redirectAttributes.addFlashAttribute("message",
                "Fragmentos das edições virtuais carregados: " + total + "<br>" + list);
        return "redirect:/admin/loadForm";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/virtual/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String manageVirtualEditions(Model model) {
        model.addAttribute("editions", LdoD.getInstance().getVirtualEditionsSet().stream()
                .sorted((v1, v2) -> v1.getAcronym().compareTo(v2.getAcronym())).collect(Collectors.toList()));

        return "admin/listVirtualEditions";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/virtual/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteVirtualEdition(Model model, @RequestParam("externalId") String externalId) {
        VirtualEdition edition = FenixFramework.getDomainObject(externalId);
        if (edition == null) {
            return "redirect:/error";
        } else {
            edition.remove();
        }
        return "redirect:/admin/virtual/list";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/createTestUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createTestUsers(Model model) {
        logger.debug("createTestUsers");
        LdoD.getInstance().createTestUsers(this.passwordEncoder);
        return "redirect:/admin/user/list";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tweets")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String manageTweets(Model model) {
        logger.debug("manageTweets");

        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        model.addAttribute("citations",
                LdoD.getInstance().getAllTwitterCitation().stream()
                        .sorted((c1, c2) -> java.time.LocalDateTime.parse(c2.getDate(), formater)
                                .compareTo(java.time.LocalDateTime.parse(c1.getDate(), formater)))
                        .collect(Collectors.toList()));
        model.addAttribute("tweets", LdoD.getInstance().getTweetSet());
        model.addAttribute("numberOfCitationsWithInfoRange", LdoD.getInstance().getNumberOfCitationsWithInfoRanges());
        return "admin/manageTweets";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tweets/removeTweets")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String removeTweets(Model model) {
        logger.debug("removeTweets");
        LdoD.getInstance().removeTweets();
        return "redirect:/admin/tweets";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tweets/generateCitations")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String generateCitations(Model model) throws IOException {
        logger.debug("generateCitations");
        CitationDetecter detecter = new CitationDetecter();
        detecter.detect();

        TweetFactory tweetFactory = new TweetFactory();
        tweetFactory.create();

        AwareAnnotationFactory awareFactory = new AwareAnnotationFactory();
        awareFactory.generate();

        LdoD.dailyRegenerateTwitterCitationEdition();

        // Repeat to update edition
        awareFactory.generate();

        return "redirect:/admin/tweets";
    }

}
