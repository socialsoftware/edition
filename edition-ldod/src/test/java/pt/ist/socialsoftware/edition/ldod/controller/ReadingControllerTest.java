package pt.ist.socialsoftware.edition.ldod.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class ReadingControllerTest extends ControllerTest {

    ReadingController readingController = new ReadingController();




    @BeforeEach
    public void configuration(){
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(readingController).alwaysDo(print()).build();
    }

    @Test
    public void getList() throws Exception{
        MvcResult result = mvc.perform(get("/reading")).andReturn();

        Map model = result.getModelAndView().getModel();
        assertNotNull(model.get("virtualManager"), "VirtualManager not found");
        assertNotNull(model.get("collectionManager"), "CollectionManager not found");
        assertNull(model.get("inter"), "Inter not null");
    }

    @Test
    public void oneFrag() throws Exception{
        ModelAndView modelAndView = mvc.perform(get("/reading/fragment/Fr006/inter/Fr006_WIT_ED_CRIT_C")).andReturn().getModelAndView();

        Map model = modelAndView.getModel();


        assertNotNull(model.get("virtualManager"), "VirtualManager not found");
        assertNotNull(model.get("collectionManager"), "CollectionManager not found");
        assertNotNull(model.get("inter"), "Inter not found");
        assertNotNull(model.get("recommendations"), "Recommendations not found");
        assertNotNull(model.get("writer"), "Writer not found");

        assertEquals("reading/readingMain", modelAndView.getViewName());
    }

    @Test
    public void startReading() throws Exception{
        String viewName = mvc.perform(get("/reading/edition/TSC/start")).andReturn().getModelAndView().getViewName();


        assertTrue(viewName.matches("redirect:/reading/fragment/Fr\\d\\d\\d/inter/Fr\\d\\d\\d_WIT_ED_CRIT_SC"), "Wrong redirect: " + viewName);
    }

    @Test
    public void startReadingFromInter() throws Exception{
        String viewName = mvc.perform(get("/reading/fragment/Fr010/inter/Fr010_WIT_ED_CRIT_Z/start")).andReturn().getModelAndView().getViewName();

        assertTrue(viewName.equals("redirect:/reading/fragment/Fr010/inter/Fr010_WIT_ED_CRIT_Z"));
    }

    @Test
    public void readNextInterpretation() throws Exception{
        String viewName = mvc.perform(get("/reading/fragment/Fr008/inter/Fr008_WIT_ED_CRIT_SC/next")).andReturn().getModelAndView().getViewName();

        assertTrue(viewName.equals("redirect:/reading/fragment/Fr009/inter/Fr009_WIT_ED_CRIT_SC"), "Wrong redirect");
    }

    @Test
    public void readPrevInterpretation() throws Exception{
        String viewName = mvc.perform(get("/reading/fragment/Fr009/inter/Fr009_WIT_ED_CRIT_SC/prev")).andReturn().getModelAndView().getViewName();

        assertTrue(viewName.equals("redirect:/reading/fragment/Fr008/inter/Fr008_WIT_ED_CRIT_SC"), "Wrong redirect");
    }
}
