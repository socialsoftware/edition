package pt.ist.socialsoftware.edition.ldod.controller.visual;

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
import pt.ist.socialsoftware.edition.ldod.controller.virtual.VirtualEditionTest;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.utils.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.visual.api.remote.VisualProvidesController;

import java.io.FileNotFoundException;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class VisualTest {
    private static final Logger log = LoggerFactory.getLogger(VirtualEditionTest.class);

    @InjectMocks
    VisualProvidesController visualProvidesController;

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
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.visualProvidesController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getFragmentsTest() throws Exception {

        this.mockMvc.perform(get("/visual/editions/acronym/{acronym}/fragments", ExpertEdition.ARCHIVE_EDITION_ACRONYM))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getFragmentsErrorTest() throws Exception {

        this.mockMvc.perform(get("/virtualeditions/acronym/{acronym}/fragments", "ERROR")).andDo(print())
                .andExpect(status().is4xxClientError()).andExpect(content().string(""));
    }

}
