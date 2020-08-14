package pt.ist.socialsoftware.edition.ldod.performance;

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
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule;
import pt.ist.socialsoftware.edition.ldod.frontend.config.Application;
import pt.ist.socialsoftware.edition.ldod.frontend.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.assistedordering.AssistedOrderingController;
import pt.ist.socialsoftware.edition.ldod.recommendation.api.dto.HeteronymPropertyDto;
import pt.ist.socialsoftware.edition.ldod.recommendation.api.dto.PropertyDto;
import pt.ist.socialsoftware.edition.ldod.recommendation.api.dto.RecommendVirtualEditionParam;
import pt.ist.socialsoftware.edition.ldod.recommendation.api.dto.TextPropertyDto;
import pt.ist.socialsoftware.edition.ldod.utils.controller.LdoDExceptionHandler;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class RecommendationPerformanceTest {
    protected MockMvc mockMvc;

    @InjectMocks
    AssistedOrderingController recommendationController;

    @BeforeAll
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void setUpAll() throws FileNotFoundException {
        TestLoadUtils.setUpDatabaseWithCorpus();

        String[] fragments = {"001.xml", "002.xml", "003.xml"};
        TestLoadUtils.loadFragments(fragments);

        TestLoadUtils.loadTestVirtualEdition();
    }

    @AfterAll
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void tearDownAll() {
        TestLoadUtils.cleanDatabaseButCorpus();
    }

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.recommendationController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void setLinearTest() throws Exception {
        VirtualEditionInter vi = VirtualModule.getInstance().getVirtualEdition(VirtualEdition.ARCHIVE_EDITION_ACRONYM)
                .getAllDepthVirtualEditionInters().get(0);

        List<PropertyDto> propertyList = new ArrayList<>();
        propertyList.add(new HeteronymPropertyDto("1.0", VirtualEdition.ARCHIVE_EDITION_ACRONYM));
//        propertyList.add(new DatePropertyDto("1.0", VirtualEdition.ARCHIVE_EDITION_ACRONYM));
        propertyList.add(new TextPropertyDto("1.0", VirtualEdition.ARCHIVE_EDITION_ACRONYM));
//        propertyList.add(new TaxonomyPropertyDto("1.0", VirtualEdition.ARCHIVE_EDITION_ACRONYM));
        RecommendVirtualEditionParam paramn = new RecommendVirtualEditionParam(VirtualEdition.ARCHIVE_EDITION_ACRONYM,
                vi.getExternalId(), propertyList);

        this.mockMvc.perform(post("/recommendation/linear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestLoadUtils.jsonBytes(paramn)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("recommendation/virtualTable"))
                .andExpect(model().attribute("edition", notNullValue()));

    }

}
