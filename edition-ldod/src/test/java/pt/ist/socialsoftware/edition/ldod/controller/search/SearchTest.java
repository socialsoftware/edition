package pt.ist.socialsoftware.edition.ldod.controller.search;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.frontend.search.SearchFrontendController;
import pt.ist.socialsoftware.edition.ldod.text.api.remote.search.TextProvidesSearchController;
import pt.ist.socialsoftware.edition.ldod.utils.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.virtual.api.remote.search.VirtualProvidesSearchController;

import java.io.FileNotFoundException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class SearchTest {

    @InjectMocks
    SearchFrontendController searchFrontEndController;

    @InjectMocks
    TextProvidesSearchController textProvidesSearchController;

    @InjectMocks
    VirtualProvidesSearchController virtualProvidesSearchController;

    protected MockMvc mockMvcFrontend;
    protected MockMvc mockMvcText;
    protected MockMvc mockMvcVirtual;

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
        this.mockMvcFrontend = MockMvcBuilders.standaloneSetup(this.searchFrontEndController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
        this.mockMvcText = MockMvcBuilders.standaloneSetup(this.textProvidesSearchController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
        this.mockMvcVirtual = MockMvcBuilders.standaloneSetup(this.virtualProvidesSearchController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
    }

    @Test
    public void getSimpleSearchTest() throws Exception {

        this.mockMvcFrontend.perform(get("/search/simple")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("search/simple"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void searchSimpleTest() throws Exception {

        this.mockMvcFrontend.perform(post("/search/simple/result").contentType(MediaType.TEXT_PLAIN)
                .content("arte&&"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("search/simpleResultTable"))
                .andExpect(model().attribute("fragCount", is(1)))
                .andExpect(model().attribute("interCount", is(5)))
                .andExpect(model().attribute("results", notNullValue()));
    }


    @Test
    public void getAdvancedSearchTest() throws Exception {

        this.mockMvcFrontend.perform(get("/search/advanced"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("search/advanced"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void searchAdvancedTest() throws Exception {

        /*{"mode":"and","options":[{"type":"edition","inclusion":"in","edition":"all","heteronym":null,"date":null},{"type":"date","option":"all","begin":null,"end":null},{"type":"text","text":"arte"}]}*/

        this.mockMvcFrontend.perform(post("/search/advanced/result")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"mode\":\"and\"," +
                        "\"options\":[{\"type\":\"edition\",\"inclusion\":\"in\",\"edition\":\"all\",\"heteronym\":null,\"date\":null},{\"type\":\"date\",\"option\":\"all\",\"begin\":null,\"end\":null},{\"type\":\"text\",\"text\":\"arte\"}]}")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("search/resultTable"))
                .andExpect(model().attribute("interCount", is(5)))
                .andExpect(model().attribute("fragCount", is(1)));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getEditionsTest() throws Exception {

        String body = this.mockMvcText.perform(get("/search/getEditions")).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject response = new JSONObject(body);

        assertNotNull(response);
        assertEquals(4, response.length());
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getEditionTest() throws Exception {

        String body = this.mockMvcText.perform(get("/search/getEdition")
                .param("edition", ExpertEdition.PIZARRO_EDITION_ACRONYM))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject response = new JSONObject(body);

        assertNotNull(response);
        assertEquals(ExpertEdition.PIZARRO_EDITION_ACRONYM, response.getString("acronym"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getPublicationDatesTest() throws Exception {

        String body = this.mockMvcText.perform(get("/search/getPublicationsDates"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject response = new JSONObject(body);

        assertNotNull(response);
        assertEquals(2, response.length());
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getVirtualEditionsTest() throws Exception {

        String body = this.mockMvcVirtual.perform(get("/search/getVirtualEditions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject response = new JSONObject(body);

        assertNotNull(response);
    }

}
