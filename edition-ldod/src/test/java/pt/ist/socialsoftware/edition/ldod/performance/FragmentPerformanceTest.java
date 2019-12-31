package pt.ist.socialsoftware.edition.ldod.performance;

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
import pt.ist.socialsoftware.edition.ldod.frontend.config.Application;
import pt.ist.socialsoftware.edition.ldod.frontend.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.frontend.text.FragmentController;
import pt.ist.socialsoftware.edition.ldod.utils.controller.LdoDExceptionHandler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class FragmentPerformanceTest {
    private MockMvc mockMvc;

    @InjectMocks
    FragmentController fragmentController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.fragmentController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void getFragmentsListTest() throws Exception {
        for (int i = 0; i < 100_000; i++) {
            this.mockMvc.perform(get("/fragments")).andDo(print()).andExpect(status().isOk())
                    .andExpect(view().name("fragment/list"))
                    .andExpect(model().attributeExists("jpcEdition", "tscEdition", "rzEdition", "jpEdition", "fragments"));
        }
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getFragInterByExternalId() throws Exception {
        for (int i = 0; i < 100_000; i++) {
            this.mockMvc.perform(get("/fragments/fragment/Fr381/inter/Fr381_WIT_ED_CRIT_Z")).andDo(print())
                    .andExpect(status().isOk());
        }
    }

}
