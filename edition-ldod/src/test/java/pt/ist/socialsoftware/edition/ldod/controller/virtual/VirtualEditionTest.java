package pt.ist.socialsoftware.edition.ldod.controller.virtual;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.controller.VirtualEditionController;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;

import java.io.FileNotFoundException;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class VirtualEditionTest {
    private static final Logger log = LoggerFactory.getLogger(VirtualEditionTest.class);

    @InjectMocks
    VirtualEditionController virtualEditionController;

    protected MockMvc mockMvc;

    @BeforeAll
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void setUpAll() throws FileNotFoundException {
        TestLoadUtils.setUpDatabaseWithCorpus();

        String[] fragments = {"001.xml", "002.xml", "003.xml"};
        TestLoadUtils.loadFragments(fragments);
    }

    @AfterAll
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void tearDownAll() throws FileNotFoundException {
        TestLoadUtils.cleanDatabaseButCorpus();
    }

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.virtualEditionController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void listVirtualEditionsTest() throws Exception {
        this.mockMvc.perform(get("/virtualeditions")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("virtual/editions")).andExpect(model().attribute("ldod", notNullValue()))
                .andExpect(model().attribute("user", nullValue()))
                .andExpect(model().attribute("expertEditions", hasSize(4)))
                .andExpect(model().attribute("virtualEditions", hasSize(1)));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void manageVirtualEditionsTest() throws Exception {

        String id = LdoD.getInstance().getArchiveEdition().getExternalId();

        this.mockMvc.perform(get("/virtualeditions/restricted/manage/{externalId}", id)).andDo(print())
                .andExpect(status().isOk()).andExpect(view().name("virtual/manage"))
                .andExpect(model().attribute("user", nullValue()))
                .andExpect(model().attribute("virtualEdition", notNullValue()))
                .andExpect(model().attribute("countriesList", hasSize(8)));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void manageVirtualEditionsErrorTest() throws Exception {

        this.mockMvc.perform(get("/virtualeditions/restricted/manage/{externalId}", "ERROR")).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void editFormVirtualEditionTest() throws Exception {

        VirtualEdition ve = LdoD.getInstance().getArchiveEdition();

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

        VirtualEdition ve = LdoD.getInstance().getArchiveEdition();

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

        String id = LdoD.getInstance().getVirtualEditionInterByXmlId("Fr001.WIT.ED.VIRT.LdoD-Arquivo.1")
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
    @WithUserDetails("ars")
    public void createVirtualEditionTest() throws Exception {

        this.mockMvc
                .perform(post("/virtualeditions/restricted/create").param("acronym", "Test").param("title", "Test")
                        .param("pub", "true").param("use", "no"))
                .andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));

        VirtualEdition testEdition = LdoD.getInstance().getVirtualEdition(VirtualEdition.ACRONYM_PREFIX + "Test");

        assertEquals("Test", testEdition.getShortAcronym());
        assertEquals("Test", testEdition.getTitle());
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void deleteVirtualEditionTest() throws Exception {
        this.mockMvc
                .perform(post("/virtualeditions/restricted/create").param("acronym", "Test").param("title", "Test")
                        .param("pub", "true").param("use", "no"))
                .andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));

        VirtualEdition testEdition = LdoD.getInstance().getVirtualEdition(VirtualEdition.ACRONYM_PREFIX + "Test");

        String id = testEdition.getExternalId();

        this.mockMvc.perform(post("/virtualeditions/restricted/delete").param("externalId", id)).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));

        assertNull(LdoD.getInstance().getVirtualEdition(VirtualEdition.ACRONYM_PREFIX + "Test"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void deleteVirtualEditionErrorTest() throws Exception {
        this.mockMvc.perform(post("/virtualeditions/restricted/delete").param("externalId", "ERROR")).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void toggleVirtualEditionTest() throws Exception {

        String id = LdoD.getInstance().getArchiveEdition().getExternalId();

        this.mockMvc.perform(post("/virtualeditions/toggleselection").param("externalId", id)).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void toggleVirtualEditionErrorTest() throws Exception {

        this.mockMvc.perform(post("/virtualeditions/toggleselection").param("externalId", "ERROR")).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void reorderVirtualEditionTest() throws Exception {

        String id = LdoD.getInstance().getArchiveEdition().getExternalId();

        this.mockMvc.perform(post("/virtualeditions/restricted/reorder/{externalId}", id).param("fraginters", ""))
                .andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void reorderErrorVirtualEditionTest() throws Exception {

        this.mockMvc.perform(post("/virtualeditions/restricted/reorder/{externalId}", "ERROR").param("fraginters", ""))
                .andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getFragmentsTest() throws Exception {

        this.mockMvc.perform(get("/virtualeditions/acronym/{acronym}/fragments", Edition.ARCHIVE_EDITION_ACRONYM))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getFragmentsErrorTest() throws Exception {

        this.mockMvc.perform(get("/virtualeditions/acronym/{acronym}/fragments", "ERROR")).andDo(print())
                .andExpect(status().is4xxClientError()).andExpect(content().string(""));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getTranscriptionsTest() throws Exception {

        this.mockMvc.perform(get("/virtualeditions/acronym/{acronym}/transcriptions", Edition.ARCHIVE_EDITION_ACRONYM))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getTranscriptionsErrorTest() throws Exception {

        this.mockMvc.perform(get("/virtualeditions/acronym/{acronym}/transcriptions", "ERROR")).andDo(print())
                .andExpect(status().is4xxClientError()).andExpect(content().string(""));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void showParticipantsTest() throws Exception {

        VirtualEdition ve = LdoD.getInstance().getArchiveEdition();

        this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/participants", ve.getExternalId()))
                .andDo(print()).andExpect(status().isOk()).andExpect(view().name("virtual/participants"))
                .andExpect(model().attribute("virtualEdition", notNullValue()))
                .andExpect(model().attribute("username", nullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void showParticipantsErrorTest() throws Exception {
        this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/participants", "ERROR")).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void submitParticipantTest() throws Exception {

        String id = LdoD.getInstance().getArchiveEdition().getExternalId();

        this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/submit", id)).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void submitParticipantErrorTest() throws Exception {

        this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/submit", "ERROR"))
                .andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void cancelParticipantTest() throws Exception {

        String id = LdoD.getInstance().getArchiveEdition().getExternalId();

        this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/cancel", id)).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void cancelParticipantErrorTest() throws Exception {

        this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/cancel", "ERROR"))
                .andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getTaxonomyTest() throws Exception {

        String id = LdoD.getInstance().getArchiveEdition().getExternalId();

        this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/taxonomy", id)).andDo(print())
                .andExpect(status().isOk()).andExpect(view().name("virtual/taxonomy"))
                .andExpect(model().attribute("virtualEdition", notNullValue()));

    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getTaxonomyErrorTest() throws Exception {

        this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/taxonomy", "ERROR")).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));

    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void deleteTaxonomyTest() throws Exception {

        VirtualEdition ve = LdoD.getInstance().getArchiveEdition();

        this.mockMvc
                .perform(post("/virtualeditions/restricted/{externalId}/taxonomy/clean", ve.getExternalId())
                        .param("taxonomyExternalId", ve.getTaxonomy().getExternalId()))
                .andDo(print()).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/virtualeditions/restricted/" + ve.getExternalId() + "/taxonomy"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void deleteErrorTaxonomyTest() throws Exception {

        VirtualEdition ve = LdoD.getInstance().getArchiveEdition();

        this.mockMvc
                .perform(post("/virtualeditions/restricted/{externalId}/taxonomy/clean", ve.getExternalId())
                        .param("taxonomyExternalId", "ERROR"))
                .andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void createCategoryTest() throws Exception {

        VirtualEdition ve = LdoD.getInstance().getArchiveEdition();

        this.mockMvc
                .perform(post("/virtualeditions/restricted/category/create").param("externalId", ve.getExternalId())
                        .param("name", "test"))
                .andDo(print()).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/virtualeditions/restricted/" + ve.getExternalId() + "/taxonomy"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void createCategoryErrorTest() throws Exception {

        VirtualEdition ve = LdoD.getInstance().getArchiveEdition();

        this.mockMvc
                .perform(post("/virtualeditions/restricted/category/create").param("externalId", "ERROR").param("name",
                        "test"))
                .andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }
}
