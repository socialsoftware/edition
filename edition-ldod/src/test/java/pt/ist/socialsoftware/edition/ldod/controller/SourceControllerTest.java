package pt.ist.socialsoftware.edition.ldod.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class SourceControllerTest extends ControllerTest {

    private SourceController sourceController = new SourceController();

    @BeforeEach
    public void configuration() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(sourceController).alwaysDo(print()).build();
    }

    @Test
    public void list() throws Exception{
        ModelAndView modelAndView = mvc.perform(get("/source/list")).andReturn().getModelAndView();

        Map model = modelAndView.getModel();
        List sources = (List) model.get("sources");
        assertNotNull(sources, "Sources not found");
        assertEquals(sources.size(), NUMBER_OF_FRAGS);
    }
}
