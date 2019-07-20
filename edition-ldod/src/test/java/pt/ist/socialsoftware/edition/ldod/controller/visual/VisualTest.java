package pt.ist.socialsoftware.edition.ldod.controller.visual;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.virtual.VirtualEditionTest;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule;
import pt.ist.socialsoftware.edition.ldod.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.recommendation.api.dto.WeightsDto;
import pt.ist.socialsoftware.edition.ldod.utils.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.visual.api.remote.VisualRemoteController;

import java.io.FileNotFoundException;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class VisualTest {
    private static final Logger log = LoggerFactory.getLogger(VirtualEditionTest.class);

    @InjectMocks
    VisualRemoteController visualRemoteController;

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
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.visualRemoteController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getFragmentsTest() throws Exception {

        this.mockMvc.perform(get("/visual/editions/acronym/{acronym}/fragments", VirtualEdition.ARCHIVE_EDITION_ACRONYM))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getFragmentsErrorTest() throws Exception {

        this.mockMvc.perform(get("/visual/editions/acronym/{acronym}/fragments", "ERROR")).andDo(print())
                .andExpect(status().is4xxClientError()).andExpect(content().string(""));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getIntersByDistanceTest() throws Exception {

        VirtualEditionInter vi = VirtualModule.getInstance().getArchiveEdition()
                .getAllDepthVirtualEditionInters().get(0);

        WeightsDto dto = new WeightsDto(0.0f, 0.0f, 0.0f, 0.0f);

        String res = this.mockMvc.perform(post("/visual/editions/{externalId}/intersByDistance", vi.getExternalId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestLoadUtils.jsonBytes(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(not(""))).andReturn().getResponse().getContentAsString();

        res = res.replace("[", "").replace("]", "")
                .replace("},{", "};{");

        String[] frags = res.split(";");
        System.out.println(res);
        assertEquals(3, frags.length);
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < 3; i++) {
            InterIdDistancePairDto pair = mapper.readValue(frags[i], InterIdDistancePairDto.class);
            if (pair.getInterId().equals(vi.getExternalId())) {
                assertEquals(1.0, pair.getDistance());
            } else {
                assertEquals(0.0, pair.getDistance());
            }
        }
    }


}
