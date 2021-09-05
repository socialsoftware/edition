package pt.ist.socialsoftware.edition.ldod.controller.recommendation;

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
import pt.ist.socialsoftware.edition.ldod.frontend.config.Application;
import pt.ist.socialsoftware.edition.ldod.frontend.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.FeVirtualRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.assistedordering.AssistedOrderingController;
import pt.ist.socialsoftware.edition.notification.dtos.recommendation.RecommendVirtualEditionParam;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class RecommendationTest {
    public static final String ARCHIVE_EDITION_ACRONYM = "LdoD-Arquivo";

    private final FeVirtualRequiresInterface feVirtualRequiresInterface = new FeVirtualRequiresInterface();

    @InjectMocks
    AssistedOrderingController assistedOrderingController;

    protected MockMvc mockMvc;

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
    public static void tearDownAll() throws FileNotFoundException {
        TestLoadUtils.cleanDatabase();
    }

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.assistedOrderingController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getRecommendationTest() throws Exception {
        this.mockMvc.perform(get("/recommendation/restricted/{acronym}", ARCHIVE_EDITION_ACRONYM))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("recommendation/tableOfContents"))
                .andExpect(model().attribute("edition", notNullValue()))
                .andExpect(model().attribute("taxonomyWeight", is(0.0)))
                .andExpect(model().attribute("heteronymWeight", is(0.0)))
                .andExpect(model().attribute("dateWeight", is(0.0)))
                .andExpect(model().attribute("textWeight", is(0.0)));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void setLinearTest() throws Exception {
        VirtualEditionInterDto vi = feVirtualRequiresInterface.getArchiveEdition()
                .getIntersSet().stream().findFirst().get();

        RecommendVirtualEditionParam paramn = new RecommendVirtualEditionParam(ARCHIVE_EDITION_ACRONYM,
                vi.getExternalId(), new ArrayList<>());

        this.mockMvc.perform(post("/recommendation/linear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestLoadUtils.jsonBytes(paramn)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("recommendation/virtualTable"))
                .andExpect(model().attribute("edition", notNullValue()));

    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void saveLinearTest() throws Exception {
        VirtualEditionDto virtualEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ARCHIVE_EDITION_ACRONYM);

        String[] inter = virtualEdition.getIntersSet().stream()
                .map(VirtualEditionInterDto::getExternalId)
                .sorted()
                .toArray(String[]::new);

        this.mockMvc.perform(post("/recommendation/linear/save")
                .param("acronym", ARCHIVE_EDITION_ACRONYM)
                .param("inter[]", inter))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recommendation/restricted/" +
                        feVirtualRequiresInterface.getArchiveEdition().getAcronym()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void createLinearTest() throws Exception {
        VirtualEditionDto virtualEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym(ARCHIVE_EDITION_ACRONYM);

        String[] inter = virtualEdition.getIntersSet().stream()
                .map(VirtualEditionInterDto::getExternalId)
                .sorted()
                .toArray(String[]::new);

        this.mockMvc.perform(post("/recommendation/linear/create")
                .param("acronym", "temp")
                .param("title","Temp")
                .param("pub","true")
                .param("inter[]", inter))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

}
