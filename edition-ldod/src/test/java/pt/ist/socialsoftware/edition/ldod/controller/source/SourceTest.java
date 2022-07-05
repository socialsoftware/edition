package pt.ist.socialsoftware.edition.ldod.controller.source;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.ist.socialsoftware.edition.ldod.ControllersTestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.SourceController;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class SourceTest extends ControllersTestWithFragmentsLoading {

    @InjectMocks
    SourceController sourceController;

    @Override
    protected Object getController() {
        return this.sourceController;
    }

    @Override
    protected void populate4Test() {

    }

    @Override
    protected void unpopulate4Test() {

    }

    @Override
    protected String[] fragmentsToLoad4Test() {
        String[] fragments = { "001.xml", "002.xml", "003.xml" };

        return fragments;
    }

    @Test
    public void getSourcesTest() throws Exception {
        this.mockMvc.perform(get("/source/list")).andDo(print())
            .andExpect(status().isOk()).andExpect(view().name("source/listSources"))
            .andExpect(model().attribute("sources",hasSize(3)));
    }

}
