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
import pt.ist.socialsoftware.edition.ldod.controller.virtual.VirtualEditionTest;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.frontend.config.Application;
import pt.ist.socialsoftware.edition.ldod.frontend.filters.TransactionFilter;

import pt.ist.socialsoftware.edition.api.remote.VisualRemoteController;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.recommendation.api.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.recommendation.api.dto.WeightsDto;
import pt.ist.socialsoftware.edition.text.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.text.domain.TextModule;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule;

import java.io.FileNotFoundException;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    public static void tearDownAll() {
        TestLoadUtils.cleanDatabase();
    }

    @BeforeEach
    public void setUp()  {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.visualRemoteController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getFragmentsExpertTest() throws Exception {
        this.mockMvc.perform(get("/visual/editions/acronym/{acronym}/fragments", TextModule.getInstance().getTSCEdition().getAcronym()))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getVirtualFragmentsVirtualTest() throws Exception {
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
    public void getIntersByDistanceExpertEditionTest() throws Exception {
        ExpertEditionInter expertEditionInter = TextModule.getInstance().getRZEdition().getSortedInterps().get(0);

        WeightsDto dto = new WeightsDto(0.0f, 0.0f, 0.0f, 0.0f);

        String res = this.mockMvc.perform(post("/visual/editions/{externalId}/intersByDistance", expertEditionInter.getExternalId())
                .contentType(MediaType.APPLICATION_JSON)
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
            if (pair.getInterId().equals(expertEditionInter.getExternalId())) {
                assertEquals(1.0, pair.getDistance());
            } else {
                assertEquals(0.0, pair.getDistance());
            }
        }
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getIntersByDistanceVirtualEditionTest() throws Exception {
        VirtualEditionInter vi = VirtualModule.getInstance().getArchiveEdition()
                .getAllDepthVirtualEditionInters().get(0);

        WeightsDto dto = new WeightsDto(0.0f, 0.0f, 0.0f, 0.0f);

        String res = this.mockMvc.perform(post("/visual/editions/{externalId}/intersByDistance", vi.getExternalId())
                .contentType(MediaType.APPLICATION_JSON)
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

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getEditionsTest() throws Exception {
        this.mockMvc.perform(get("/visual/editions/public"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getInterIdTFIDFTermsTest() throws Exception {
        VirtualEdition ve = VirtualModule.getInstance().getArchiveEdition();
        VirtualEditionInter inter = ve.getAllDepthVirtualEditionInters().get(0);

        this.mockMvc.perform(get("/visual/editions/acronym/{acronym}/interId/{interId}/tfidf", VirtualEdition.ARCHIVE_EDITION_ACRONYM, inter.getExternalId()))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(notNullValue()));
    }

}
