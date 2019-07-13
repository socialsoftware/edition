package pt.ist.socialsoftware.edition.ldod.controller.reading;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.ReadingController;

import java.io.FileNotFoundException;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ReadingTest {

    @InjectMocks
    ReadingController readingController;

    protected MockMvc mockMvc;

    @BeforeAll
    @Atomic(mode = TxMode.WRITE)
    public static void setUpAll() throws FileNotFoundException {
        TestLoadUtils.setUpDatabaseWithCorpus();

        String[] fragments = {"001.xml", "002.xml", "003.xml"};
        TestLoadUtils.loadFragments(fragments);
    }

    @AfterAll
    @Atomic(mode = TxMode.WRITE)
    public static void tearDownAll() throws FileNotFoundException {
        TestLoadUtils.cleanDatabaseButCorpus();
    }

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.readingController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
    }

    @Test
    public void readingMainTest() throws Exception {
        this.mockMvc.perform(get("/reading")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("reading/readingMain")).andExpect(model().attribute("inter", nullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void readInterpretationTest() throws Exception {
        this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}", "Fr001", "Fr001_WIT_ED_CRIT_P"))
                .andDo(print()).andExpect(status().isOk()).andExpect(view().name("reading/readingMain"))
                .andExpect(model().attribute("inter", notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void readInterpretationFragErrorTest() throws Exception {
        this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}", "ERROR", "Fr001_WIT_ED_CRIT_P"))
                .andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void readInterpretationInterErrorTest() throws Exception {
        this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}", "Fr001", "ERROR")).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void startReadEditionTest() throws Exception {
        this.mockMvc.perform(get("/reading/edition/{acronym}/start", ExpertEdition.PIZARRO_EDITION_ACRONYM)).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reading/fragment/Fr001/inter/Fr001_WIT_ED_CRIT_P"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void readFromInterTest() throws Exception {
        this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/start", "Fr001", "Fr001_WIT_ED_CRIT_P"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reading/fragment/Fr001/inter/Fr001_WIT_ED_CRIT_P"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void readFromInterXmlErrorTest() throws Exception {
        this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/start", "ERROR", "Fr001_WIT_ED_CRIT_P"))
                .andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void readFromInterUrlErrorTest() throws Exception {
        this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/start", "Fr001", "ERROR"))
                .andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void readNextInterXmlErrorTest() throws Exception {
        this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/next", "ERROR", "Fr001_WIT_ED_CRIT_P"))
                .andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void readNextInterUrlErrorTest() throws Exception {
        this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/next", "Fr001", "ERROR"))
                .andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void readPrevInterXmlErrorTest() throws Exception {
        this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/prev", "ERROR", "Fr001_WIT_ED_CRIT_P"))
                .andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void readPrevInterUrlErrorTest() throws Exception {
        this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/prev", "Fr001", "ERROR"))
                .andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }


}
