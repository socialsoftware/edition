package pt.ist.socialsoftware.edition.ldod.controller.virtual;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.game.api.GameRequiresInterface;
import pt.ist.socialsoftware.edition.game.domain.ClassificationGame;
import pt.ist.socialsoftware.edition.game.domain.ClassificationModule;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.frontend.config.Application;
import pt.ist.socialsoftware.edition.ldod.frontend.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.frontend.game.GameController;
import pt.ist.socialsoftware.edition.ldod.frontend.text.FeTextRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.FeVirtualRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.VirtualEditionController;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.*;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class VirtualEditionTest {
    public static final String ARS = "ars";
    public static String ACRONYM_PREFIX = "LdoD-";

    private final FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();
    private final FeUserRequiresInterface feUserRequiresInterface = new FeUserRequiresInterface();
    private final FeVirtualRequiresInterface feVirtualRequiresInterface = new FeVirtualRequiresInterface();

    @InjectMocks
    VirtualEditionController virtualEditionController;

    @InjectMocks
    GameController gameController;

    protected MockMvc mockMvc;
    protected MockMvc gameMockMvc;

    @BeforeAll
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void setUpAll() throws IOException {
        TestLoadUtils.setUpDatabaseWithCorpus();
        String[] fragments = {"001.xml", "002.xml", "003.xml"};
        TestLoadUtils.loadFragments(fragments);

        TestLoadUtils.loadVirtualEditionsCorpus();

        String[] virtualEditionFragments = {"virtual-Fr001.xml", "virtual-Fr002.xml", "virtual-Fr003.xml"};
        TestLoadUtils.loadVirtualEditionFragments(virtualEditionFragments);
    }

    @AfterAll
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void tearDownAll() {
        TestLoadUtils.cleanDatabase();
    }

    @BeforeEach
    public void setUp() throws FileNotFoundException {

        feVirtualRequiresInterface.cleanVirtualEditionInterMapByUrlIdCache();
        feVirtualRequiresInterface.cleanVirtualEditionInterMapByXmlIdCache();
        feVirtualRequiresInterface.cleanVirtualEditionMapCache();


        this.mockMvc = MockMvcBuilders.standaloneSetup(this.virtualEditionController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();

        this.gameMockMvc = MockMvcBuilders.standaloneSetup(this.gameController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
    }

    @AfterEach
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void tearDown() {

    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = "ars")
    public void listVirtualEditionsTest() throws Exception {
        this.mockMvc.perform(get("/virtualeditions")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("virtual/editions"))
                .andExpect(model().attribute("ldod", notNullValue()))
                .andExpect(model().attribute("user", "ars"))
                .andExpect(model().attribute("expertEditions", hasSize(4)))
                .andExpect(model().attribute("virtualEditions", hasSize(2)));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = "ars")
    public void manageVirtualEditionsTest() throws Exception {
        String id = feVirtualRequiresInterface.getArchiveEdition().getExternalId();

        this.mockMvc.perform(get("/virtualeditions/restricted/manage/{externalId}", id)).andDo(print())
                .andExpect(status().isOk()).andExpect(view().name("virtual/manage"))
                .andExpect(model().attribute("user", "ars"))
                .andExpect(model().attribute("virtualEdition", notNullValue()))
                .andExpect(model().attribute("countriesList", hasSize(8)));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = "ars")
    public void manageVirtualEditionsErrorTest() throws Exception {
        this.mockMvc.perform(get("/virtualeditions/restricted/manage/{externalId}", "ERROR")).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }


    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void editFormVirtualEditionTest() throws Exception {
        VirtualEditionDto ve = feVirtualRequiresInterface.getArchiveEdition();

        this.mockMvc.perform(get("/virtualeditions/restricted/editForm/{externalId}", ve.getExternalId()))
                .andDo(print()).andExpect(status().isOk()).andExpect(view().name("virtual/edition"))
                .andExpect(model().attribute("virtualEdition", notNullValue()))
                .andExpect(model().attribute("externalId", equalTo(ve.getExternalId())))
                .andExpect(model().attribute("acronym", ve.getShortAcronym()))
                .andExpect(model().attribute("title", ve.getTitle()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void editFormVirtualEditionErrorTest() throws Exception {
        this.mockMvc.perform(get("/virtualeditions/restricted/editForm/{externalId}", "ERROR")).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void showTaxonomyTest() throws Exception {
        VirtualEditionDto ve = feVirtualRequiresInterface.getArchiveEdition();

        this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/taxonomy", ve.getExternalId()))
                .andDo(print()).andExpect(status().isOk()).andExpect(view().name("virtual/taxonomy"))
                .andExpect(model().attribute("virtualEdition", notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void showTaxonomyErrorTest() throws Exception {
        this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/taxonomy", "ERROR")).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void showFragInterTest() throws Exception {
        String id = feVirtualRequiresInterface.getVirtualEditionInterByXmlId("Fr001.WIT.ED.VIRT.LdoD-Arquivo.1")
                .getExternalId();

        this.mockMvc.perform(get("/virtualeditions/restricted/fraginter/{fragInterId}", id)).andDo(print())
                .andExpect(status().isOk()).andExpect(view().name("virtual/fragInter"))
                .andExpect(model().attribute("fragInter", notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void showFragInterErrorTest() throws Exception {
        this.mockMvc.perform(get("/virtualeditions/restricted/fraginter/{fragInterId}", "ERROR")).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = "ars")
    public void createVirtualEditionTest() throws Exception {
        this.mockMvc
                .perform(post("/virtualeditions/restricted/create").param("acronym", "Test").param("title", "Test")
                        .param("pub", "true").param("use", "no"))
                .andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));

        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX + "Test");

        assertEquals("Test", testEdition.getShortAcronym());
        assertEquals("Test", testEdition.getTitle());

        testEdition.removeByExternalId();
    }


    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void editVirtualEditionIsSAVETest() throws Exception {

        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

        this.mockMvc
                .perform(post("/virtualeditions/restricted/edit/{externalId}", testEdition.getExternalId())
                        .param("acronym", "NEW")
                        .param("title", "Title")
                        .param("synopsis", "Synopsis")
                        .param("pub", "false")
                        .param("management", "true")
                        .param("vocabulary", "true")
                        .param("annotation", "true")
                        .param("mediasource", "Twitter")
                        .param("begindate", "")
                        .param("enddate", "")
                        .param("geolocation", "Spain")
                        .param("frequency", "45")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/virtualeditions/restricted/manage/" + testEdition.getExternalId()));

        assertEquals("Title", testEdition.getTitle());
        assertFalse(testEdition.getPub());
        assertEquals("Synopsis", testEdition.getSynopsis());

        testEdition.removeByExternalId();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void editVirtualEditionIsNotSAVETest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

        this.mockMvc
                .perform(post("/virtualeditions/restricted/edit/{externalId}", testEdition.getExternalId())
                        .param("acronym", "NEW")
                        .param("title", "Title")
                        .param("synopsis", "Synopsis")
                        .param("pub", "false")
                        .param("management", "true")
                        .param("vocabulary", "true")
                        .param("annotation", "true")
                        .param("mediasource", "noMediaSource")
                        .param("begindate", "")
                        .param("enddate", "")
                        .param("geolocation", "Spain")
                        .param("frequency", "45")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/virtualeditions/restricted/manage/" + testEdition.getExternalId()));

        assertEquals("Title", testEdition.getTitle());
        assertFalse(testEdition.getPub());
        assertEquals("Synopsis", testEdition.getSynopsis());

        testEdition.removeByExternalId();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteVirtualEditionTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

        String id = testEdition.getExternalId();

        this.mockMvc.perform(post("/virtualeditions/restricted/delete").param("externalId", id)).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));

        assertNull(feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX + "Test"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteVirtualEditionErrorTest() throws Exception {
        this.mockMvc.perform(post("/virtualeditions/restricted/delete").param("externalId", "ERROR")).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }


    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = "ars")
    public void toggleVirtualEditionTest() throws Exception {
        String id = feVirtualRequiresInterface.getArchiveEdition().getExternalId();

        this.mockMvc.perform(post("/virtualeditions/toggleselection").param("externalId", id)).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = "ars")
    public void toggleVirtualEditionErrorTest() throws Exception {
        this.mockMvc.perform(post("/virtualeditions/toggleselection").param("externalId", "ERROR")).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void reorderVirtualEditionTest() throws Exception {
        String id = feVirtualRequiresInterface.getArchiveEdition().getExternalId();

        this.mockMvc.perform(post("/virtualeditions/restricted/reorder/{externalId}", id).param("fraginters", ""))
                .andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void reorderErrorVirtualEditionTest() throws Exception {
        this.mockMvc.perform(post("/virtualeditions/restricted/reorder/{externalId}", "ERROR").param("fraginters", ""))
                .andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void showParticipantsTest() throws Exception {
        VirtualEditionDto ve = feVirtualRequiresInterface.getArchiveEdition();

        this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/participants", ve.getExternalId()))
                .andDo(print()).andExpect(status().isOk()).andExpect(view().name("virtual/participants"))
                .andExpect(model().attribute("virtualEdition", notNullValue()))
                .andExpect(model().attribute("username", nullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void showParticipantsErrorTest() throws Exception {
        this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/participants", "ERROR")).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }


    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = ARS)
    public void submitParticipantTest() throws Exception {
        UserDto userDto = feUserRequiresInterface.createTestUser("other", "password", "Tó", "Zé", "a@a.a");
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

        this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/submit", testEdition.getExternalId())).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));

        assertEquals(1,testEdition.getParticipantSet().size());
        assertEquals(2,testEdition.getMemberSet().size());
        testEdition.removeByExternalId();
        userDto.removeUser();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = "ars")
    public void submitParticipantErrorTest() throws Exception {
        this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/submit", "ERROR"))
                .andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = "ars")
    public void cancelParticipantTest() throws Exception {
        UserDto userDto = feUserRequiresInterface.createTestUser("other", "password", "Tó", "Zé", "a@a.a");
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");
//        testEdition.addMember("other", Member.MemberRole.ADMIN, false);
        testEdition.addMemberAdminByExternalId("other", false);

        this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/cancel", testEdition.getExternalId())).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));

        assertEquals(1,testEdition.getParticipantSet().size());
        assertEquals(1,testEdition.getMemberSet().size());
        testEdition.removeByExternalId();
        userDto.removeUser();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = "ars")
    public void cancelParticipantErrorTest() throws Exception {
        this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/cancel", "ERROR"))
                .andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }


    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void approveParticipantTest() throws Exception {
        UserDto userDto = feUserRequiresInterface.createTestUser("other", "password", "Tó", "Zé", "a@a.a");
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");
        testEdition.addMemberAdminByExternalId("ars", false);

        this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/approve", testEdition.getExternalId())
                .param("username", "ars"))
                .andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions/restricted/" + testEdition.getExternalId() + "/participants"));

        assertEquals(2,testEdition.getParticipantSet().size());
        assertEquals(2,testEdition.getMemberSet().size());

        testEdition.removeByExternalId();
        userDto.removeUser();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void addParticipantTest() throws Exception {
        UserDto userDto = feUserRequiresInterface.createTestUser("other", "password", "Tó", "Zé", "a@a.a");
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

        this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/add", testEdition.getExternalId())
                .param("username", "ars"))
                .andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions/restricted/" + testEdition.getExternalId() + "/participants"));

        assertEquals(2,testEdition.getParticipantSet().size());
        assertEquals(2,testEdition.getMemberSet().size());

        testEdition.removeByExternalId();
        userDto.removeUser();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = "ars")
    public void switchRoleTest() throws Exception {
        UserDto userDto = feUserRequiresInterface.createTestUser("other", "password", "Tó", "Zé", "a@a.a");
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");
        testEdition.addMemberAdminByExternalId(ARS, true);

        this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/role", testEdition.getExternalId())
                .param("username", ARS))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/virtualeditions/restricted/" + testEdition.getExternalId() + "/participants"));

        testEdition.removeByExternalId();
        userDto.removeUser();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = "ars")
    public void removeParticipantTest() throws Exception {
        UserDto userDto = feUserRequiresInterface.createTestUser("other", "password", "Tó", "Zé", "a@a.a");
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");
        testEdition.addMemberAdminByExternalId("ars", false);
        testEdition.addMemberByExternalId(userDto.getUsername(), true);

        this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/remove", testEdition.getExternalId())
                .param("user", "other"))
                .andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions/restricted/" + testEdition.getExternalId() + "/participants"));

        assertEquals(1,testEdition.getParticipantSet().size());
        assertEquals(2,testEdition.getMemberSet().size());

        testEdition.removeByExternalId();
        userDto.removeUser();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getTaxonomyTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

        this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/taxonomy", testEdition.getExternalId()))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(view().name("virtual/taxonomy"))
                .andExpect(model().attribute("virtualEdition", notNullValue()));

        testEdition.removeByExternalId();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getTaxonomyErrorTest() throws Exception {
        this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/taxonomy", "ERROR"))
                .andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void editTaxonomyTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

        this.mockMvc
                .perform(post("/virtualeditions/restricted/{externalId}/taxonomy/edit", testEdition.getExternalId())
                        .param("management", "true")
                        .param("vocabulary", "true")
                        .param("annotation", "true"))
                .andDo(print()).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/virtualeditions/restricted/" + testEdition.getExternalId() + "/taxonomy"));

        testEdition.removeByExternalId();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteTaxonomyTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

        this.mockMvc
                .perform(post("/virtualeditions/restricted/{externalId}/taxonomy/clean", testEdition.getExternalId())
                        .param("taxonomyExternalId", testEdition.getTaxonomy().getExternalId()))
                .andDo(print()).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/virtualeditions/restricted/" + testEdition.getExternalId() + "/taxonomy"));

        testEdition.removeByExternalId();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteErrorTaxonomyTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

        this.mockMvc
                .perform(post("/virtualeditions/restricted/{externalId}/taxonomy/clean", testEdition.getExternalId())
                        .param("taxonomyExternalId", "ERROR"))
                .andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));

        testEdition.removeByExternalId();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createCategoryTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

        this.mockMvc
                .perform(post("/virtualeditions/restricted/category/create")
                        .param("externalId", testEdition.getExternalId())
                        .param("name", "test"))
                .andDo(print()).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/virtualeditions/restricted/" + testEdition.getExternalId() + "/taxonomy"));

        testEdition.removeByExternalId();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createCategoryErrorTest() throws Exception {
        this.mockMvc
                .perform(post("/virtualeditions/restricted/category/create")
                        .param("externalId", "ERROR")
                        .param("name","test"))
                .andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void deleteCategoryTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

        CategoryDto category = testEdition.getTaxonomy().createTestCategory("first");

		this.mockMvc
				.perform(post("/virtualeditions/restricted/category/delete")
						.param("categoryId", category.getExternalId()))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/virtualeditions/restricted/" + testEdition.getExternalId() + "/taxonomy"));

		testEdition.removeByExternalId();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void showCategoryTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

        CategoryDto category = testEdition.getTaxonomy().createTestCategory("first");

		this.mockMvc
				.perform(get("/virtualeditions/restricted/category/{categoryId}",  category.getExternalId()))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("virtual/category"));

		testEdition.removeByExternalId();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void mergeCategoriesMergeTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

		String[] categories = {
                testEdition.getTaxonomy().createTestCategory("first").getExternalId(),
                testEdition.getTaxonomy().createTestCategory("second").getExternalId()
		};

		this.mockMvc
				.perform(post("/virtualeditions/restricted/category/mulop")
						.param("taxonomyId", testEdition.getTaxonomy().getExternalId())
						.param("type", "merge")
						.param("categories[]", categories))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("/virtualeditions/restricted/category/*"));

		testEdition.removeByExternalId();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void extractCategoryTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

		String categoryId = testEdition.getTaxonomy().createTestCategory("first-to-extract").getExternalId();

		String[] inters = {
                testEdition.getIntersSet().stream().findFirst().get().getExternalId(),
		};

		this.mockMvc
				.perform(post("/virtualeditions/restricted/category/extract")
						.param("categoryId", categoryId)
						.param("inters[]", inters))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("/virtualeditions/restricted/category/*"));

		testEdition.removeByExternalId();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = "ars")
	public void associateCategoryTest() throws Exception {
//        VirtualEdition testEdition = VirtualModule.getInstance().createVirtualEdition(ARS,
//                VirtualEdition.ACRONYM_PREFIX +"NEWT",
//                "titleX", LocalDate.now(), false, VirtualModule.getInstance().getArchiveEdition().getAcronym());
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, feVirtualRequiresInterface.getArchiveEdition().getAcronym());
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

		String[] categories = {
                testEdition.getTaxonomy().createTestCategory("first-to-associate").getName()
		};

		this.mockMvc
				.perform(post("/virtualeditions/restricted/tag/associate")
						.param("fragInterId", testEdition.getIntersSet().stream().findFirst().get().getExternalId())
						.param("categories[]", categories))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("/fragments/fragment/inter/*"));

		testEdition.removeByExternalId();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = "ars")
	public void dissociateCategoryTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, feVirtualRequiresInterface.getArchiveEdition().getAcronym());
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");
		CategoryDto category = testEdition.getTaxonomy().createTestCategory("first-to-dissociate");
		Set<String> categories = new HashSet<String>();
		categories.add(category.getName());

		VirtualEditionInterDto inter = testEdition.getIntersSet().stream().findFirst().get();
		inter.associate("ars", categories);

		this.mockMvc
				.perform(get("/virtualeditions/restricted/fraginter/{fragInterId}/tag/dissociate/{categoryId}",inter.getExternalId(), category.getExternalId()))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("/fragments/fragment/inter/*"));

		testEdition.removeByExternalId();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void mergeCategoriesDeleteTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, null);
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");
		String[] categories = {
                testEdition.getTaxonomy().createTestCategory("first").getExternalId(),
		};

		this.mockMvc
				.perform(post("/virtualeditions/restricted/category/mulop")
						.param("taxonomyId", testEdition.getTaxonomy().getExternalId())
						.param("type", "delete")
						.param("categories[]", categories))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/virtualeditions/restricted/" + testEdition.getExternalId() + "/taxonomy"));

		testEdition.removeByExternalId();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = "ars")
	public void generateTopicModellingTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, feVirtualRequiresInterface.getArchiveEdition().getAcronym());
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

		this.mockMvc
				.perform(get("/virtualeditions/restricted/{externalId}/taxonomy/generateTopics", testEdition.getExternalId())
						.param("numTopics", "5")
						.param("numWords", "2")
						.param("thresholdCategories", "5")
						.param("numIterations", "5"))
				.andDo(print())
				.andExpect(status().isOk());

		testEdition.removeByExternalId();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void createTopicModellingTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, feVirtualRequiresInterface.getArchiveEdition().getAcronym());
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

		VirtualEditionInterDto virtualEditionInter = testEdition.getIntersSet().stream().findFirst().get();

		TopicListDTO topicList = new TopicListDTO();
		topicList.setTaxonomyExternalId(testEdition.getTaxonomy().getExternalId());
		topicList.setUsername("ars");
		TopicDTO topicDto = new TopicDTO();
		topicDto.setName("first");
		TopicInterPercentageDTO topicInter = new TopicInterPercentageDTO();
		topicInter.setExternalId(virtualEditionInter.getExternalId());
		topicInter.setTitle("Title");
		topicInter.setPercentage(34);
		List<TopicInterPercentageDTO> topicInters = new ArrayList<>();
		topicInters.add(topicInter);
		topicDto.setInters(topicInters);
		List<TopicDTO> topics = new ArrayList<>();
		topics.add(topicDto);
		topicList.setTopics(topics);

		this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/taxonomy/createTopics", testEdition.getExternalId()))
				.andDo(print())
				.andExpect(status().isOk());

		testEdition.removeByExternalId();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void addInterTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, feVirtualRequiresInterface.getArchiveEdition().getAcronym());
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

		VirtualEditionInterDto virtualEditionInter = testEdition.getIntersSet().stream().findFirst().get();
		String interExternalId = virtualEditionInter.getUsesInter() != null
                ? virtualEditionInter.getUsesInter().getExternalId()
//                : TextModule.getInstance().getScholarInterByXmlId(virtualEditionInter.getUsesScholarInterId()).getExternalId();
                : feTextRequiresInterface.getScholarInterByXmlId(virtualEditionInter.getUsesScholarInterId()).getExternalId();

		virtualEditionInter.removeInter();

		this.mockMvc
				.perform(post("/virtualeditions/restricted/addinter/{veId}/{interId}", testEdition.getExternalId(), interExternalId))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("/fragments/fragment/inter/*"));

		testEdition.removeByExternalId();
	}

	// TODO included in a different controller related with Classification Game

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void classificationGameTest() throws Exception {
        VirtualEditionDto ve = feVirtualRequiresInterface.getArchiveEdition();

		this.gameMockMvc
				.perform(get("/virtualeditions/restricted/{externalId}/classificationGame", ve.getExternalId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("virtual/classificationGame"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void createClassificationGameFormTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, feVirtualRequiresInterface.getArchiveEdition().getAcronym());
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

		this.gameMockMvc
				.perform(get("/virtualeditions/restricted/{externalId}/classificationGame/create", testEdition.getExternalId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("virtual/createClassificationGame"));

        testEdition.removeByExternalId();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = ARS)
	public void createClassificationGameTest() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, feVirtualRequiresInterface.getArchiveEdition().getAcronym());
        VirtualEditionDto testEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ACRONYM_PREFIX +"NEWT");

        int number = ClassificationModule.getInstance().getClassificationGameSet().size();

        this.gameMockMvc
				.perform(post("/virtualeditions/restricted/{externalId}/classificationGame/create", testEdition.getExternalId())
						.param("description", "Description")
						.param("date", "08/08/2020 09:00")
						.param("interExternalId", testEdition.getIntersSet().stream().findFirst().get().getExternalId()))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/virtualeditions/restricted/" + testEdition.getExternalId() + "/classificationGame"));

		assertEquals(number + 1, ClassificationModule.getInstance().getClassificationGameSet().size());
        testEdition.removeByExternalId();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = ARS)
	public void removeClassificationGame() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, feVirtualRequiresInterface.getArchiveEdition().getAcronym());
        pt.ist.socialsoftware.edition.game.api.virtualDto.VirtualEditionDto testEdition = GameRequiresInterface.getInstance().getVirtualEdition(ACRONYM_PREFIX +"NEWT");

		ClassificationGame classificationGame = new ClassificationGame(testEdition, "Description", DateTime.now(), testEdition.getIntersSet().stream().findFirst().get(), ARS);

		int number = ClassificationModule.getInstance().getClassificationGameSet().size();

		this.gameMockMvc
				.perform(post("/virtualeditions/restricted/{externalId}/classificationGame/{gameId}/remove", testEdition.getExternalId(), classificationGame.getExternalId()))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/virtualeditions/restricted/" + testEdition.getExternalId() + "/classificationGame"));

        assertEquals(number -1, ClassificationModule.getInstance().getClassificationGameSet().size());
        testEdition.removeByExternalId();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void getClassificationGame() throws Exception {
        feVirtualRequiresInterface.createVirtualEdition(ARS,
                ACRONYM_PREFIX +"NEWT",
                "titleX", LocalDate.now(), true, feVirtualRequiresInterface.getArchiveEdition().getAcronym());
        pt.ist.socialsoftware.edition.game.api.virtualDto.VirtualEditionDto testEdition = GameRequiresInterface.getInstance().getVirtualEdition(ACRONYM_PREFIX +"NEWT");

        ClassificationGame classificationGame = new ClassificationGame(testEdition, "Description", DateTime.now(), testEdition.getIntersSet().stream().findFirst().get(), ARS);

        classificationGame.addParticipant("ars");

		this.gameMockMvc
				.perform(get("/virtualeditions/{externalId}/classificationGame/{gameId}", testEdition.getExternalId(), classificationGame.getExternalId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("virtual/classificationGameContent"));

        testEdition.removeByExternalId();
	}

}
