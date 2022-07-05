package pt.ist.socialsoftware.edition.ldod.controller.admin;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.AdminController;
import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.export.UsersXMLExport;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.forms.EditUserForm;
import pt.ist.socialsoftware.edition.ldod.loaders.VirtualEditionsTEICorpusImport;
import pt.ist.socialsoftware.edition.ldod.utils.Bootstrap;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class AdminTest {

    @Mock
    SessionRegistry sessionRegistry;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AdminController adminController;

    protected MockMvc mockMvc;

    @BeforeAll
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void setUpAll() throws FileNotFoundException {

    }

    @AfterAll
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void tearDownAll() throws FileNotFoundException {
    }

    @BeforeEach
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void setUp() throws FileNotFoundException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.adminController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();

        if (LdoD.getInstance() != null) {
            LdoD.getInstance().remove();
        }

        Bootstrap.initializeSystem();
    }

    @AfterEach
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void tearDown() throws FileNotFoundException {
        if (LdoD.getInstance() != null) {
            LdoD.getInstance().remove();
        }
    }

    @Test
    public void getLoadFormTest() throws Exception {
        this.mockMvc.perform(get("/admin/loadForm"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/loadForm"))
                .andExpect(model().attribute("message", nullValue()))
                .andExpect(model().attribute("error", nullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void loadCorpusTest() throws Exception {

        File directory = new File(PropertiesManager.getProperties().getProperty("test.files.dir"));

        File frag = new File(directory, "corpus.xml");

        FileInputStream fis = new FileInputStream(frag);

        MockMultipartFile mockFile = new MockMultipartFile("mockFile", fis);

        this.mockMvc.perform(multipart("/admin/load/corpus")
                .file("file", mockFile.getBytes())
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/loadForm"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void loadFragmentTest() throws Exception {

        //Make sure corpus is present in db before loading fragments
        TestLoadUtils.loadCorpus();

        File directory = new File(PropertiesManager.getProperties().getProperty("test.files.dir"));

        File frag = new File(directory, "001.xml");

        FileInputStream fis = new FileInputStream(frag);

        MockMultipartFile mockFile = new MockMultipartFile("mockFile", fis);

        this.mockMvc.perform(multipart("/admin/load/fragmentsAtOnce")
                .file("file", mockFile.getBytes())
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/loadForm"));

        assertNotNull(LdoD.getInstance().getFragmentByXmlId("Fr001"));
    }


    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void loadMultipleFragmentsTest() throws Exception {

        //Make sure corpus is present in db before loading fragments
        TestLoadUtils.loadCorpus();

        File directory = new File(PropertiesManager.getProperties().getProperty("test.files.dir"));

        File frag1 = new File(directory, "001.xml");
        FileInputStream fis1 = new FileInputStream(frag1);
        MockMultipartFile mockFile1 = new MockMultipartFile("mockFile1", fis1);

        File frag2 = new File(directory, "002.xml");
        FileInputStream fis2 = new FileInputStream(frag2);
        MockMultipartFile mockFile2 = new MockMultipartFile("mockFile2", fis2);

        this.mockMvc.perform(multipart("/admin/load/fragmentsStepByStep")
                .file("files", mockFile1.getBytes())
                .file("files", mockFile2.getBytes())
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/loadForm"));

        assertNotNull(LdoD.getInstance().getFragmentByXmlId("Fr001"));
        assertNotNull(LdoD.getInstance().getFragmentByXmlId("Fr002"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getFragmentDeleteListTest() throws Exception {
        this.mockMvc.perform(get("/admin/fragment/list")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/deleteFragment"))
                .andExpect(model().attribute("fragments", notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteFragmentTest() throws Exception {
        TestLoadUtils.loadCorpus();
        String[] fragments = {"001.xml"};
        TestLoadUtils.loadFragments(fragments);

        String id = LdoD.getInstance().getFragmentByXmlId("Fr001").getExternalId();

        this.mockMvc.perform(post("/admin/fragment/delete")
                .param("externalId", id))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/fragment/list"));

        assertNull(LdoD.getInstance().getFragmentByXmlId("Fr001"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteFragmentErrorTest() throws Exception {

        this.mockMvc.perform(post("/admin/fragment/delete")
                .param("externalId", "ERROR"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteAllFragmentsTest() throws Exception {
        TestLoadUtils.loadCorpus();
        String[] fragments = {"001.xml", "002.xml", "003.xml"};
        TestLoadUtils.loadFragments(fragments);


        this.mockMvc.perform(post("/admin/fragment/deleteAll"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/fragment/list"));

        assertNull(LdoD.getInstance().getFragmentByXmlId("Fr001"));
        assertNull(LdoD.getInstance().getFragmentByXmlId("Fr002"));
        assertNull(LdoD.getInstance().getFragmentByXmlId("Fr003"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteAllFragmentsAnVirtualInterpretationsTest() throws Exception {
        TestLoadUtils.loadCorpus();
        String[] fragments = {"001.xml", "002.xml", "003.xml"};
        TestLoadUtils.loadFragments(fragments);

        TestLoadUtils.loadVirtualEditionsCorpus();
        String[] virtualEditionFragments = {"virtual-Fr001.xml", "virtual-Fr002.xml", "virtual-Fr003.xml"};
        TestLoadUtils.loadVirtualEditionFragments(virtualEditionFragments);

        this.mockMvc.perform(post("/admin/fragment/deleteAll"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/fragment/list"));

        assertNull(LdoD.getInstance().getFragmentByXmlId("Fr001"));
        assertNull(LdoD.getInstance().getFragmentByXmlId("Fr002"));
        assertNull(LdoD.getInstance().getFragmentByXmlId("Fr003"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void switchAdminTest() throws Exception {

        boolean original = LdoD.getInstance().getAdmin();

        this.mockMvc.perform(post("/admin/switch")).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/list"));

        assertNotEquals(original, LdoD.getInstance().getAdmin());
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
  //  @WithUserDetails("ars")
    public void deleteUserSessionsTest() throws Exception {
        when(sessionRegistry.getAllPrincipals()).thenReturn(new ArrayList<>());
        this.mockMvc.perform(post("/admin/sessions/delete"))
                .andDo(print())
                .andExpect(redirectedUrl("/admin/user/list"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getUserListTest() throws Exception {
        this.mockMvc.perform(get("/admin/user/list")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/listUsers"))
                .andExpect(model().attribute("users", hasSize(2)))
                .andExpect(model().attribute("sessions", notNullValue()));

    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getEditUserFormTest() throws Exception {

        LdoDUser user = LdoD.getInstance().getUser("ars");

        MvcResult res = this.mockMvc.perform(get("/admin/user/edit")
                .param("externalId", user.getExternalId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/edit"))
                .andExpect(model().attribute("editUserForm", isA(EditUserForm.class))).andReturn();

        EditUserForm form = (EditUserForm) res.getModelAndView().getModel().get("editUserForm");

        assertEquals(user.getFirstName(), form.getFirstName());
        assertEquals(user.getLastName(), form.getLastName());
        assertEquals(user.getUsername(), form.getNewUsername());
        assertEquals(user.getUsername(), form.getOldUsername());
        assertEquals(user.getEmail(), form.getEmail());

    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void postEditUserFormTest() throws Exception {

        Role user = Role.getRole(Role.RoleType.ROLE_USER);
        Role admin = Role.getRole(Role.RoleType.ROLE_ADMIN);

        LdoDUser temp = new LdoDUser(LdoD.getInstance(), "temp", "$2a$11$FqP6hxx1OzHeP/MHm8Ccier/ZQjEY5opPTih37DR6nQE1XFc0lzqW",
                "Temp", "Temp", "temp@temp.temp");

        temp.setEnabled(true);
        temp.addRoles(user);
        temp.addRoles(admin);

        this.mockMvc.perform(post("/admin/user/edit")
                .param("oldUsername", "temp")
                .param("newUsername", "newtemp")
                .param("firstName", "Temp")
                .param("lastName", "Temp")
                .param("email", "temp@temp.temp")
                .sessionAttr("userForm", new EditUserForm()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/list"));

        assertNull(LdoD.getInstance().getUser("temp"));
        assertNotNull(LdoD.getInstance().getUser("newtemp"));


        LdoD.getInstance().getUser("newtemp").remove();

    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void switchActiveTest() throws Exception {

        LdoDUser ldoDUser = LdoD.getInstance().getUser("ars");

        boolean original = ldoDUser.getActive();

        this.mockMvc.perform(post("/admin/user/active")
                .param("externalId", ldoDUser.getExternalId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/list"));

        assertNotEquals(original, ldoDUser.getActive());

        this.mockMvc.perform(post("/admin/user/active")
                .param("externalId", ldoDUser.getExternalId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/list"));

        assertEquals(original, ldoDUser.getActive());
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeUserTest() throws Exception {

        Role user = Role.getRole(Role.RoleType.ROLE_USER);
        Role admin = Role.getRole(Role.RoleType.ROLE_ADMIN);

        LdoDUser temp = new LdoDUser(LdoD.getInstance(), "temp", "$2a$11$FqP6hxx1OzHeP/MHm8Ccier/ZQjEY5opPTih37DR6nQE1XFc0lzqW",
                "Temp", "Temp", "temp@temp.temp");

        temp.setEnabled(true);
        temp.addRoles(user);
        temp.addRoles(admin);

        this.mockMvc.perform(post("/admin/user/delete")
                .param("externalId", temp.getExternalId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/list"));

        assertNull(LdoD.getInstance().getUser("temp"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getExportFormTest() throws Exception {

        this.mockMvc.perform(get("/admin/exportForm"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/exportForm"));

    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void exportSearchFragTest() throws Exception {

        TestLoadUtils.loadCorpus();
        String[] fragments = {"001.xml"};
        TestLoadUtils.loadFragments(fragments);

        this.mockMvc.perform(post("/admin/exportSearch")
                .param("query", "arte"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/exportForm"))
                .andExpect(model().attribute("query", is("arte")))
                .andExpect(model().attribute("nResults", is(1)))
                .andExpect(model().attribute("frags", hasSize(1)));

    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void exportSpecificFragsTest() throws Exception {

        TestLoadUtils.loadCorpus();
        String[] fragments = {"001.xml"};
        TestLoadUtils.loadFragments(fragments);

        this.mockMvc.perform(post("/admin/exportSearchResult")
                .param("query", "arte"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/tei+xml"))
                .andExpect(content().string(containsString("arte")))
                .andExpect(content().string(containsString("Fr001"))).andReturn().getResponse();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void exportAllFragsTest() throws Exception {

        TestLoadUtils.loadCorpus();
        String[] fragments = {"001.xml", "002.xml"};
        TestLoadUtils.loadFragments(fragments);

        this.mockMvc.perform(get("/admin/exportAll"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/tei+xml"))
                .andExpect(content().string(containsString("Fr001")))
                .andExpect(content().string(containsString("Fr002")));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void exportRandomFragsTest() throws Exception {

        TestLoadUtils.loadCorpus();
        String[] fragments = {"001.xml", "002.xml", "003.xml", "181.xml", "593.xml"};
        TestLoadUtils.loadFragments(fragments);

        String response = this.mockMvc.perform(get("/admin/exportRandom"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/tei+xml"))
                .andReturn().getResponse().getContentAsString();


       /* List<String> xmlIds = Arrays.asList("Fr001","Fr002","Fr003","Fr181","Fr593");
        int count = 0;

        for (String id : xmlIds){
            if (response.contains(id))
                count++;
        }

        assertEquals(3,count);*/
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void exportUsersTest() throws Exception {

        this.mockMvc.perform(get("/admin/export/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/xml"))
                .andExpect(content().string(containsString("username=\"ars\"")))
                .andExpect(content().string(containsString("username=\"Twitter\"")));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void importUsersTest() throws Exception {

        UsersXMLExport exporter = new UsersXMLExport();

        String usersXml = exporter.export();

        LdoD.getInstance().getUser("ars").remove();
        LdoD.getInstance().getUser("Twitter").remove();

        InputStream stream = new ByteArrayInputStream(usersXml.getBytes(StandardCharsets.UTF_8));

        MockMultipartFile mockFile = new MockMultipartFile("import", stream);

        this.mockMvc.perform(multipart("/admin/load/users")
                .file("file", mockFile.getBytes())
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/loadForm"));

        assertNotNull(LdoD.getInstance().getUser("ars"));
        assertNotNull(LdoD.getInstance().getUser("Twitter"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void exportVirtualEditionsTest() throws Exception {
        TestLoadUtils.loadCorpus();
        String[] fragments = {"001.xml", "002.xml", "003.xml"};
        TestLoadUtils.loadFragments(fragments);

        TestLoadUtils.loadVirtualEditionsCorpus();
        String[] virtualEditionFragments = {"virtual-Fr001.xml", "virtual-Fr002.xml", "virtual-Fr003.xml"};
        TestLoadUtils.loadVirtualEditionFragments(virtualEditionFragments);

        this.mockMvc.perform(get("/admin/export/virtualeditions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/zip"));
    }


    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void loadVirtualEditionCorpusTest() throws Exception {

        File directory = new File(PropertiesManager.getProperties().getProperty("test.files.dir"));

        File corpus = new File(directory, "virtual-corpus.xml");
        FileInputStream fis1 = new FileInputStream(corpus);
        MockMultipartFile mockFile1 = new MockMultipartFile("mockFile1", fis1);

        this.mockMvc.perform(multipart("/admin/load/virtual-corpus")
                .file("file", mockFile1.getBytes())
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/loadForm"));

        assertNotNull(LdoD.getInstance().getVirtualEdition("LdoD-Teste"));

    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void loadVirtualEditionFragmentsTest() throws Exception {

        // load original frags and corpus
        TestLoadUtils.loadCorpus();
        String[] fragments = {"001.xml"};
        TestLoadUtils.loadFragments(fragments);

        File directory = new File(PropertiesManager.getProperties().getProperty("test.files.dir"));

        // load corpus file and import
        File corpus = new File(directory, "virtual-corpus.xml");
        FileInputStream fis1 = new FileInputStream(corpus);

        VirtualEditionsTEICorpusImport loader = new VirtualEditionsTEICorpusImport();

        loader.importVirtualEditionsCorpus(fis1);

        // load virtual fragment for mocking
        File frag1 = new File(directory, "virtual-Fr001.xml");
        FileInputStream fisfrag = new FileInputStream(frag1);
        MockMultipartFile mockFrag1 = new MockMultipartFile("frag1", fisfrag);

        this.mockMvc.perform(multipart("/admin/load/virtual-fragments")
                .file("files", mockFrag1.getBytes())
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/loadForm"));

        assertNotEquals(0, LdoD.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet().size());
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void manageVirtualEditionsTest() throws Exception {
        TestLoadUtils.loadCorpus();
        String[] fragments = {"001.xml", "002.xml", "003.xml"};
        TestLoadUtils.loadFragments(fragments);

        TestLoadUtils.loadVirtualEditionsCorpus();
        String[] virtualEditionFragments = {"virtual-Fr001.xml", "virtual-Fr002.xml", "virtual-Fr003.xml"};
        TestLoadUtils.loadVirtualEditionFragments(virtualEditionFragments);

        this.mockMvc.perform(get("/admin/virtual/list")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/listVirtualEditions"))
                .andExpect(model().attribute("editions", hasSize(2)));

    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteVirtualEditionTest() throws Exception {
        TestLoadUtils.setUpDatabaseWithCorpus();

        String[] fragments = {"001.xml"};
        TestLoadUtils.loadFragments(fragments);

        TestLoadUtils.loadTestVirtualEdition();

        this.mockMvc.perform(post("/admin/virtual/delete")
                .param("externalId", LdoD.getInstance().getArchiveEdition().getExternalId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/virtual/list"));

        assertNull(LdoD.getInstance().getArchiveEdition());

    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteVirtualEditionErrorTest() throws Exception {
        TestLoadUtils.setUpDatabaseWithCorpus();

        String[] fragments = {"001.xml"};
        TestLoadUtils.loadFragments(fragments);

        TestLoadUtils.loadTestVirtualEdition();

        this.mockMvc.perform(post("/admin/virtual/delete")
                .param("externalId", "ERROR"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));

    }


    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createTestUsersTest() throws Exception {
        when(this.passwordEncoder.encode(anyString())).thenReturn(anyString());

        this.mockMvc.perform(post("/admin/createTestUsers"))
                .andDo(print())
                .andExpect(redirectedUrl("/admin/user/list"));

        for (int i = 0; i < 6; i++) {
            String username = "zuser" + Integer.toString(i + 1);

            assertNotNull(LdoD.getInstance().getUser(username));
        }

    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void manageTweetsTest() throws Exception {
        TestLoadUtils.setUpDatabaseWithCorpus();

        String[] fragments = {"001.xml"};
        TestLoadUtils.loadFragments(fragments);

        TestLoadUtils.loadTestVirtualEdition();

        TwitterCitation twitterCitation = new TwitterCitation(LdoD.getInstance().getFragmentByXmlId("Fr001"),
                "A", "B", "C", "D", 23,"E", "F", "ars", "H", "I");
        new Tweet(LdoD.getInstance(), "sourceLink", "12/12/2020", "tweetText", 34,  "location",
                "country", "ars", "profURL","profImgURL", -1,  false, twitterCitation);

        this.mockMvc.perform(get("/admin/tweets")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/manageTweets"))
                .andExpect(model().attribute("citations", notNullValue()))
                .andExpect(model().attribute("tweets", notNullValue()))
                .andExpect(model().attribute("numberOfCitationsWithInfoRange", isA(Integer.class)));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteTweetsTest() throws Exception {
        TestLoadUtils.setUpDatabaseWithCorpus();

        String[] fragments = {"001.xml"};
        TestLoadUtils.loadFragments(fragments);

        TestLoadUtils.loadTestVirtualEdition();

        TwitterCitation twitterCitation = new TwitterCitation(LdoD.getInstance().getFragmentByXmlId("Fr001"),
                "A", "B", "C", "D", 23,"E", "F", "ars", "H", "I");
        new Tweet(LdoD.getInstance(), "sourceLink", "12/12/2020", "tweetText", 34,  "location",
                "country", "ars", "profURL","profImgURL", -1,  false, twitterCitation);


        this.mockMvc.perform(post("/admin/tweets/removeTweets"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tweets"));

        assertEquals(0, LdoD.getInstance().getTweetSet().size());
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void generateCitationsTest() throws Exception {
        TestLoadUtils.setUpDatabaseWithCorpus();

        String[] fragments = {"001.xml"};
        TestLoadUtils.loadFragments(fragments);

        TestLoadUtils.loadTestVirtualEdition();

        VirtualEdition ldoDArchiveEdition = new VirtualEdition(LdoD.getInstance(), LdoD.getInstance().getUser("Twitter"), LdoD.TWITTER_EDITION_ACRONYM,
               "Twitter", new LocalDate(), true, null);

        TwitterCitation twitterCitation = new TwitterCitation(LdoD.getInstance().getFragmentByXmlId("Fr001"),
                "A", "B", "C", "D", 23,"E", "F", "ars", "H", "I");
        new Tweet(LdoD.getInstance(), "sourceLink", "12/12/2020", "tweetText", 34,  "location",
                "country", "ars", "profURL","profImgURL", -1,  false, twitterCitation);


        this.mockMvc.perform(post("/admin//tweets/generateCitations"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tweets"));
    }

}
