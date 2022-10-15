package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.admin;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.UploadFragmentDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.*;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/microfrontend/admin")
public class MicrofrontendAdminController {

    private static Logger logger = LoggerFactory.getLogger(MicrofrontendAdminController.class);

    @Inject
    private SessionRegistry sessionRegistry;

    @Inject
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/load/corpus")
    @PreAuthorize("hasPermission('', 'admin')")
    public ResponseEntity<String> loadTEICorpus(@RequestBody MultipartFile file) throws LdoDLoadException {
        if (file == null) {
            return new ResponseEntity<>("Deve escolher um ficheiro", HttpStatus.CONFLICT);
        }

        LoadTEICorpus loader = new LoadTEICorpus();
        try {
            loader.loadTEICorpus(file.getInputStream());
        } catch (IOException e) {
            return new ResponseEntity<>("Problemas com o ficheiro, tipo ou formato", HttpStatus.CONFLICT);
        } catch (LdoDException ldodE) {
            return new ResponseEntity<>(ldodE.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("Corpus carregado", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/load/fragmentsAtOnce")
    @PreAuthorize("hasPermission('', 'admin')")
    public ResponseEntity<String> loadTEIFragmentsAtOnce(@RequestBody MultipartFile file) throws LdoDLoadException {
        String message = null;
        if (file == null) {
            return new ResponseEntity<>("Deve escolher um ficheiro", HttpStatus.CONFLICT);
        }

        List<UploadFragmentDto> uploadFragmentDtoList;

        try {
            uploadFragmentDtoList = new LoadTEIFragments().loadFragmentsAtOnce(file.getInputStream());
        } catch (IOException e) {
            return new ResponseEntity<>("Problemas com o ficheiro, tipo ou formato", HttpStatus.CONFLICT);
        } catch (LdoDException ldodE) {
            return new ResponseEntity<>(ldodE.getMessage(), HttpStatus.BAD_REQUEST);
        }

        String loadedResult = getLoadedResult(uploadFragmentDtoList);
        String unloadedResult = getUnloadedResult(uploadFragmentDtoList);
        return new ResponseEntity<>(String.format("%s <br> %s", loadedResult, unloadedResult), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/load/fragmentsStepByStep")
    @PreAuthorize("hasPermission('', 'admin')")
    public ResponseEntity<String> loadTEIFragmentsStepByStep(@RequestBody MultipartFile[] files) throws LdoDLoadException {

        if (files == null) {
            return new ResponseEntity<>("Deve escolher um ficheiro", HttpStatus.CONFLICT);
        }
        List<UploadFragmentDto> uploadFragmentDtoList = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                new LoadTEIFragments().loadFragmentsStepByStep(file.getInputStream(), uploadFragmentDtoList);

            } catch (IOException e) {
                return new ResponseEntity<>("Problemas com o ficheiro, tipo ou formato", HttpStatus.CONFLICT);
            } catch (LdoDException ldodE) {
                return new ResponseEntity<>(ldodE.getMessage(), HttpStatus.CONFLICT);
            }
        }
        String loadedResult = getLoadedResult(uploadFragmentDtoList);
        String unloadedResult = getUnloadedResult(uploadFragmentDtoList);
        return new ResponseEntity<>(String.format("%s <br> %s", loadedResult, unloadedResult), HttpStatus.OK);
    }

    private String getUnloadedResult(List<UploadFragmentDto> uploadFragsList) {

        List<UploadFragmentDto> notUploadedFragmentsList = uploadFragsList.stream()
                .filter(uploadFragmentDto -> !uploadFragmentDto.isUploaded())
                .collect(Collectors.toList());

        String unloadedString = String.format("<br>Not uploaded fragments: %d", notUploadedFragmentsList.size());
        return !notUploadedFragmentsList.isEmpty()
                ? uploadFragsList
                .stream()
                .map(uploadFragmentDto -> String.format("<br>%s", uploadFragmentDto.toString()))
                .reduce(unloadedString, (result, unLoadedFrag) -> String.format("%s %s", result, unLoadedFrag))
                : "";
    }


    private String getLoadedResult(List<UploadFragmentDto> uploadFragsList) {

        List<UploadFragmentDto> uploadedFragmentsList = uploadFragsList.stream()
                .filter(UploadFragmentDto::isUploaded)
                .collect(Collectors.toList());

        String loadedString = String.format("Uploaded fragments: %d", uploadedFragmentsList.size());

        return uploadedFragmentsList
                .stream()
                .map(uploadFragmentDto -> String.format("<br>%s", uploadFragmentDto.toString()))
                .reduce(loadedString, (result, loadedFrag) -> String.format("%s %s", result, loadedFrag)).concat("<br>");
    }

    private Map<String, List<UploadFragmentDto>> getUploadFragmentsMap() {
        Map<String, List<UploadFragmentDto>> uploadFragmentsMap = new HashMap<>();
        uploadFragmentsMap.put("loaded", new ArrayList<>());
        uploadFragmentsMap.put("unloaded", new ArrayList<>());
        return uploadFragmentsMap;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/load/users")
    @PreAuthorize("hasPermission('', 'admin')")
    public ResponseEntity<String> loadUsersXML(@RequestBody MultipartFile file) throws LdoDLoadException {
        if (file == null) {
            return new ResponseEntity<>("Deve escolher um ficheiro", HttpStatus.CONFLICT);
        }

        UsersXMLImport loader = new UsersXMLImport();
        try {
            loader.importUsers(file.getInputStream());
        } catch (IOException e) {
            return new ResponseEntity<>("Problemas com o ficheiro, tipo ou formato", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>("Utilizadores Carregados", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/load/virtual-corpus")
    @PreAuthorize("hasPermission('', 'admin')")
    public ResponseEntity<String> loadVirtualCorpus(@RequestBody MultipartFile file) throws LdoDLoadException {
        if (file == null) {
            return new ResponseEntity<>("Deve escolher um ficheiro", HttpStatus.CONFLICT);
        }

        VirtualEditionsTEICorpusImport loader = new VirtualEditionsTEICorpusImport();
        try {
            loader.importVirtualEditionsCorpus(file.getInputStream());
        } catch (IOException e) {
            return new ResponseEntity<>("Problemas com o ficheiro, tipo ou formato", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>("Corpus das edições virtuais carregado", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/load/virtual-fragments")
    @PreAuthorize("hasPermission('', 'admin')")
    public ResponseEntity<String> loadVirtualFragments(@RequestBody MultipartFile[] files) throws LdoDLoadException {
        if (files == null) {
            return new ResponseEntity<>("Deve escolher um ficheiro", HttpStatus.CONFLICT);
        }

        VirtualEditionFragmentsTEIImport loader = new VirtualEditionFragmentsTEIImport();

        String list = "";
        int total = 0;
        for (MultipartFile file : files) {
            try {
                list = list + "<br/>" + loader.importFragmentFromTEI(file.getInputStream());
                total++;
            } catch (IOException e) {
                return new ResponseEntity<>("Problemas com o ficheiro, tipo ou formato", HttpStatus.CONFLICT);
            } catch (LdoDException ldodE) {
                return new ResponseEntity<>(ldodE.getMessage(), HttpStatus.CONFLICT);
            }
        }

        return new ResponseEntity<>("Fragmentos das edições virtuais carregados: " + total + "<br>" + list, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/exportSearch")
    @PreAuthorize("hasPermission('', 'admin')")
    public List<FragmentDto> exportSearch(@RequestParam("query") String query) {

        LdoD ldoD = LdoD.getInstance();

        List<String> frags = new ArrayList<>();
        List<FragmentDto> fragmentList = new ArrayList<>();
        int n = 0;

        if (query.compareTo("") != 0) {
            for (Fragment frag : ldoD.getFragmentsSet()) {
                if (frag.getTitle().contains(query)) {
                    String s = "<p href=\"/fragments/fragment/" + frag.getExternalId() + "\">" + frag.getTitle().replace(query, "<b><u>" + query + "</u></b>") + "</p>";
                    frags.add(s);
                    n++;
                    fragmentList.add(new FragmentDto(frag, s));
                }
            }
        }

        return fragmentList;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/exportSearchResult")
    @PreAuthorize("hasPermission('', 'admin')")
    public void exportSearchResult(HttpServletResponse response, @RequestParam("query") String query) {

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
    @PreAuthorize("hasPermission('', 'admin')")
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
    @PreAuthorize("hasPermission('', 'admin')")
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
    @PreAuthorize("hasPermission('', 'admin')")
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

    @RequestMapping(method = RequestMethod.GET, value = "/export/virtualeditions")
    @PreAuthorize("hasPermission('', 'admin')")
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

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/list")
    @PreAuthorize("hasPermission('', 'admin')")
    public List<FragmentDto> deleteFragmentsList() {
        return LdoD.getInstance().getFragmentsSet().stream().map(FragmentDto::new).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fragment/delete")
    @PreAuthorize("hasPermission('', 'admin')")
    public List<FragmentDto> deleteFragment(@RequestParam("externalId") String externalId) {
        Fragment fragment = FenixFramework.getDomainObject(externalId);
        if (fragment == null) {
            return null;
        } else if (LdoD.getInstance().getFragmentsSet().size() >= 1) {
            fragment.remove();
        }
        return deleteFragmentsList();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fragment/deleteAll")
    @PreAuthorize("hasPermission('', 'admin')")
    public List<FragmentDto> deleteAllFragments() {
        for (Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
            fragment.remove();
        }
        return deleteFragmentsList();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/switch")
    @PreAuthorize("hasPermission('', 'admin')")
    public UserListDto switchAdminMode() {
        logger.debug("switchAdminMode");

        LdoD ldoD = LdoD.getInstance();
        ldoD.switchAdmin();

        return this.listUser();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/sessions/delete")
    @PreAuthorize("hasPermission('', 'admin')")
    public UserListDto deleteUserSessons() {
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

            }
        }

        return this.listUser();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/list")
    @PreAuthorize("hasPermission('', 'admin')")
    public UserListDto listUser() {
        List<SessionInformation> activeSessions = new ArrayList<>();
        for (Object principal : this.sessionRegistry.getAllPrincipals()) {
            activeSessions.addAll(this.sessionRegistry.getAllSessions(principal, false));
        }
        return UserListDto.UserListDtoBuilder.anUserListDto()
                .ldoDAdmin(LdoD.getInstance().getAdmin())
                .userList(LdoD.getInstance()
                        .getUsersSet()
                        .stream()
                        .sorted(Comparator.comparing(u -> u.getFirstName().toLowerCase()))
                        .map(UserDto::new)
                        .collect(Collectors.toList()))
                .sessionList(activeSessions
                        .stream()
                        .sorted((s1, s2) -> s2.getLastRequest().compareTo(s1.getLastRequest()))
                        .map(SessionDto::new)
                        .collect(Collectors.toList())).build();

    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/edit")
    @PreAuthorize("hasPermission('', 'admin')")
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

    @RequestMapping(method = RequestMethod.POST, value = "/user/edit")
    @PreAuthorize("hasPermission('', 'admin')")
    public String editUser(@RequestBody EditUserForm form, BindingResult formBinding) {
        logger.debug("editUser username:{}", form.getOldUsername());

        EditUserValidator validator = new EditUserValidator();
        validator.validate(form, formBinding);

        if (formBinding.hasErrors()) {
            return "not valid";
        }

        LdoDUser user = LdoD.getInstance().getUser(form.getOldUsername());

        user.update(this.passwordEncoder, form.getOldUsername(), form.getNewUsername(), form.getFirstName(), form.getLastName(), form.getEmail(), form.getNewPassword(), form.isUser(), form.isAdmin(), form.isEnabled());

        return "valid";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/active")
    @PreAuthorize("hasPermission('', 'admin')")
    public UserListDto activeUser(@RequestParam("externalId") String externalId) {
        LdoDUser user = FenixFramework.getDomainObject(externalId);

        user.switchActive();

        return this.listUser();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/delete")
    @PreAuthorize("hasPermission('', 'admin')")
    public UserListDto removeUser(@RequestParam("externalId") String externalId) {
        LdoDUser user = FenixFramework.getDomainObject(externalId);

        user.remove();

        return this.listUser();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/virtual/list")
    @PreAuthorize("hasPermission('', 'admin')")
    public List<VirtualEditionDto> manageVirtualEditions() {
        return LdoD.getInstance().getVirtualEditionsSet().stream().sorted(Comparator.comparing(Edition_Base::getAcronym)).map(ve -> new VirtualEditionDto(ve, CategoryDto.PresentationTarget.MANAGE_VIRTUAL)).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/virtual/delete")
    @PreAuthorize("hasPermission('', 'admin')")
    public List<VirtualEditionDto> deleteVirtualEdition(@RequestParam("externalId") String externalId) {
        VirtualEdition edition = FenixFramework.getDomainObject(externalId);
        if (edition == null) {
            return null;
        } else {
            edition.remove();
        }
        return this.manageVirtualEditions();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tweets")
    @PreAuthorize("hasPermission('', 'admin')")
    public TweetListDto manageTweets() {
        logger.debug("manageTweets");

        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");

        return new TweetListDto(LdoD.getInstance().getAllTwitterCitation().stream().sorted((c1, c2) -> java.time.LocalDateTime.parse(c2.getDate(), formater).compareTo(java.time.LocalDateTime.parse(c1.getDate(), formater))).collect(Collectors.toList()), LdoD.getInstance().getTweetSet(), LdoD.getInstance().getNumberOfCitationsWithInfoRanges());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tweets/removeTweets")
    @PreAuthorize("hasPermission('', 'admin')")
    public TweetListDto removeTweets() {
        logger.debug("removeTweets");
        LdoD.getInstance().removeTweets();
        return this.manageTweets();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tweets/generateCitations")
    @PreAuthorize("hasPermission('', 'admin')")
    public TweetListDto generateCitations(Model model) throws IOException {
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

        return this.manageTweets();
    }
}
