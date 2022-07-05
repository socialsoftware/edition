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
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.controller.SourceController;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class SourcePerformanceTest {
    private MockMvc mockMvc;

    @InjectMocks
    SourceController sourceController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.sourceController).setControllerAdvice(new LdoDExceptionHandler())
                .addFilters(new TransactionFilter()).build();
    }

    @Test
    public void getSourcesTest() throws Exception {
        for (int i = 0; i < 1; i++) {
            this.mockMvc.perform(get("/source/list")).andDo(print())
                    .andExpect(status().isOk()).andExpect(view().name("source/listSources"));
        }

    }
}