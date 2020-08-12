package pt.ist.socialsoftware.edition.ldod.controller.search;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.controller.SearchController;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;

import java.io.FileNotFoundException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class SearchTest {

    @InjectMocks
    SearchController searchController;

    protected MockMvc mockMvc;

    @BeforeAll
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void setUpAll() throws FileNotFoundException {
        TestLoadUtils.setUpDatabaseWithCorpus();

        String[] fragments = { "001.xml", "002.xml", "003.xml", "181.xml", "560.xml", "593.xml" };
        TestLoadUtils.loadFragments(fragments);

        TestLoadUtils.loadVirtualEditionsCorpus();
        String[] virtualEditionFragments = {"virtual-Fr001.xml", "virtual-Fr002.xml", "virtual-Fr003.xml"};
        TestLoadUtils.loadVirtualEditionFragments(virtualEditionFragments);
    }

    @AfterAll
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void tearDownAll() throws FileNotFoundException {
        TestLoadUtils.cleanDatabaseButCorpus();
    }

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.searchController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
    }

    @Test
    public void getSimpleSearchTest() throws Exception {
        this.mockMvc.perform(get("/search/simple")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("search/simple"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void searchSimpleTest() throws Exception {
        this.mockMvc.perform(post("/search/simple/result").contentType(MediaType.TEXT_PLAIN)
                .content("arte&&"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("search/simpleResultTable"))
                .andExpect(model().attribute("fragCount",is(1)))
                .andExpect(model().attribute("interCount",is(5)))
                .andExpect(model().attribute("results",notNullValue()));
    }


    @Test
    public void getAdvancedSearchTest() throws Exception {
        this.mockMvc.perform(get("/search/advanced"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("search/advanced"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void searchAdvancedTest() throws Exception {

    /*{"mode":"and","options":[{"type":"edition","inclusion":"in","edition":"all","heteronym":null,"date":null},{"type":"date","option":"all","begin":null,"end":null},{"type":"text","text":"arte"}]}*/

        this.mockMvc.perform(post("/search/advanced/result")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"mode\":\"and\"," +
                        "\"options\":[{\"type\":\"edition\",\"inclusion\":\"in\",\"edition\":\"all\",\"heteronym\":null,\"date\":null},{\"type\":\"date\",\"option\":\"all\",\"begin\":null,\"end\":null},{\"type\":\"text\",\"text\":\"arte\"}]}")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("search/resultTable"))
                .andExpect(model().attribute("interCount", is(5)))
                .andExpect(model().attribute("fragCount",is(1)));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getEditionsTest() throws Exception {
        String body = this.mockMvc.perform(get("/search/getEditions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject response = new JSONObject(body);

        assertNotNull(response);
        assertEquals(4,response.length());
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getEditionTest() throws Exception {
        String body = this.mockMvc.perform(get("/search/getEdition")
                .param("edition", Edition.PIZARRO_EDITION_ACRONYM))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject response = new JSONObject(body);

        assertNotNull(response);
        assertEquals(Edition.PIZARRO_EDITION_ACRONYM,response.getString("acronym"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getPublicationDatesTest() throws Exception {
        String body = this.mockMvc.perform(get("/search/getPublicationsDates"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject response = new JSONObject(body);

        assertNotNull(response);
        assertEquals(2,response.length());
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getVirtualEditionsTest() throws Exception {
        String body = this.mockMvc.perform(get("/search/getVirtualEditions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject response = new JSONObject(body);

        assertNotNull(response);
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getManuscriptTest() throws Exception {
        String body = this.mockMvc.perform(get("/search/getManuscriptsDates"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject response = new JSONObject(body);

        assertNotNull(response);
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getDatiloscriptTest() throws Exception {
        String body = this.mockMvc.perform(get("/search/getDactiloscriptsDates"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject response = new JSONObject(body);

        assertNotNull(response);
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getHeteronymsTest() throws Exception {
        String body = this.mockMvc.perform(get("/search/getHeteronyms"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject response = new JSONObject(body);

        assertNotNull(response);
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getDatesTest() throws Exception {
        String body = this.mockMvc.perform(get("/search/getDates"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject response = new JSONObject(body);

        assertNotNull(response);
    }

}
