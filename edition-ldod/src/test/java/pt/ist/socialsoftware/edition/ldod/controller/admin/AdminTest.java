package pt.ist.socialsoftware.edition.ldod.controller.admin;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.AdminController;
import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.Role;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.forms.EditUserForm;
import pt.ist.socialsoftware.edition.ldod.utils.Bootstrap;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class AdminTest {

    @Mock
    SessionRegistry sessionRegistry;

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

        if(LdoD.getInstance() != null)
            LdoD.getInstance().remove();

        Bootstrap.initializeSystem();
    }

    @AfterEach
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void tearDown() throws FileNotFoundException {
        if(LdoD.getInstance() != null)
            LdoD.getInstance().remove();
    }

    @Test
    public void getLoadFormTest() throws Exception {

        this.mockMvc.perform(get("/admin/loadForm"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/loadForm"))
                .andExpect(model().attribute("message",nullValue()))
                .andExpect(model().attribute("error",nullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void loadCorpusTest() throws Exception {

        File directory = new File(PropertiesManager.getProperties().getProperty("test.files.dir"));

        File frag = new File(directory,"corpus.xml");

        FileInputStream fis = new FileInputStream(frag);

        MockMultipartFile mockFile = new MockMultipartFile("mockFile",fis);

        this.mockMvc.perform(multipart("/admin/load/corpus")
                .file("file",mockFile.getBytes())
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

        File frag = new File(directory,"001.xml");

        FileInputStream fis = new FileInputStream(frag);

        MockMultipartFile mockFile = new MockMultipartFile("mockFile",fis);

        this.mockMvc.perform(multipart("/admin/load/fragmentsAtOnce")
                .file("file",mockFile.getBytes())
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

        File frag1 = new File(directory,"001.xml");
        FileInputStream fis1 = new FileInputStream(frag1);
        MockMultipartFile mockFile1 = new MockMultipartFile("mockFile1",fis1);

        File frag2 = new File(directory,"002.xml");
        FileInputStream fis2 = new FileInputStream(frag2);
        MockMultipartFile mockFile2 = new MockMultipartFile("mockFile2",fis2);

        this.mockMvc.perform(multipart("/admin/load/fragmentsStepByStep")
                .file("files",mockFile1.getBytes())
                .file("files",mockFile2.getBytes())
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
                .andExpect(model().attribute("fragments",notNullValue()));

    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteFragmentTest() throws Exception {

        TestLoadUtils.loadCorpus();
        String[] fragments = {"001.xml"};
        TestLoadUtils.loadFragments(fragments);

        String id = LdoD.getInstance().getFragmentByXmlId("Fr001").getExternalId();

        this.mockMvc.perform(post("/admin/fragment/delete")
                .param("externalId",id))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/fragment/list"));

        assertNull(LdoD.getInstance().getFragmentByXmlId("Fr001"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteFragmentErrorTest() throws Exception {

        this.mockMvc.perform(post("/admin/fragment/delete")
                .param("externalId","ERROR"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteAllFragmentsTest() throws Exception {

        TestLoadUtils.loadCorpus();
        String[] fragments = {"001.xml","002.xml","003.xml"};
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
    public void switchAdminTest() throws Exception {

        boolean original = LdoD.getInstance().getAdmin();

        this.mockMvc.perform(post("/admin/switch")).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/list"));

        assertNotEquals(original, LdoD.getInstance().getAdmin());
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteUserSessionsTest() throws Exception {

        when(sessionRegistry.getAllPrincipals()).thenReturn(new ArrayList<>());

        this.mockMvc.perform(post("/admin/sessions/delete"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/list"));

    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getUserListTest() throws Exception {

        this.mockMvc.perform(get("/admin/user/list")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/listUsers"))
                .andExpect(model().attribute("users",hasSize(2)))
                .andExpect(model().attribute("sessions",notNullValue()));

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
                .andExpect(model().attribute("editUserForm",isA(EditUserForm.class))).andReturn();

        EditUserForm form = (EditUserForm) res.getModelAndView().getModel().get("editUserForm");

        assertEquals(user.getFirstName(), form.getFirstName());
        assertEquals(user.getLastName(), form.getLastName());
        assertEquals(user.getUsername(), form.getNewUsername());
        assertEquals(user.getUsername(), form.getOldUsername());
        assertEquals(user.getEmail(),form.getEmail());

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
                .param("oldUsername","temp")
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
                .param("externalId",ldoDUser.getExternalId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/list"));

        assertNotEquals(original,ldoDUser.getActive());

        this.mockMvc.perform(post("/admin/user/active")
                .param("externalId",ldoDUser.getExternalId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/list"));

        assertEquals(original,ldoDUser.getActive());
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

        this.mockMvc.perform(post("/admin/exportSearch")
                .param("query", "query"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/exportForm"))
                .andExpect(model().attribute("query",is("query")))
                .andExpect(model().attribute("nResults",is(0)))
                .andExpect(model().attribute("frags",hasSize(0)));

    }

   /* @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void exportSpecificFragsTest() throws Exception {

        TestLoadUtils.loadCorpus();
        String[] fragments = {"001.xml","002.xml","003.xml"};
        TestLoadUtils.loadFragments(fragments);


    }*/
}
