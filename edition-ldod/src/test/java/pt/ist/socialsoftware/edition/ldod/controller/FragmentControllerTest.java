package pt.ist.socialsoftware.edition.ldod.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;
import pt.ist.socialsoftware.edition.text.domain.Fragment;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(Runner.class)
@WebAppConfiguration
public class FragmentControllerTest extends ControllerTest{

    FragmentController fragmentController = new FragmentController();


    @BeforeEach
    public void configuration(){
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(fragmentController).alwaysDo(print()).build();
    }

    @Test
    public void contextLoads() throws Exception{
        Map<String, Object> model = mvc.perform(get("/fragments")).andReturn().getModelAndView().getModel();

        Set<Fragment> fragmentSet = (Set) model.get("fragments");
        assertEquals(fragmentSet.size(), NUMBER_OF_FRAGS, "Number of fragments found");

        assertNotNull(model.get("jpcEdition"), "JPC Edition not found");
        assertNotNull(model.get("tscEdition"), "TSC Edition not found");
        assertNotNull(model.get("rzEdition"), "RZ Edition not found");
        assertNotNull(model.get("jpEdition"), "JP Edition not found");
    }

    @Test
    public void oneFragment() throws Exception{
        MvcResult result = mvc.perform(get("/fragments/fragment/Fr013")).andReturn();
        ModelAndView modelAndView = result.getModelAndView();
        Map model = modelAndView.getModel();
//        Map<String, Object> model = result.getModelAndView();

        assertNotNull(model.get("virtualManager"), "VirtualManager not found");
        assertNotNull(model.get("collectionManager"), "collectionManager not found");
        assertNotNull(model.get("fragment"), "Fragment not found");
        assertNotNull(model.get("inters"), "Inters not found");
    }

    @Test
    public void oneInter() throws Exception{
        MvcResult result = mvc.perform(get("/fragments/fragment/Fr012/inter/Fr012_WIT_ED_CRIT_SC")).andReturn();

        Map model = result.getModelAndView().getModel();


        assertNotNull(model.get("virtualManager"), "VirtualManager not found");
        assertNotNull(model.get("collectionManager"), "CollectionManager not found");
        assertNotNull(model.get("fragment"), "Fragment not found");
        assertNotNull(model.get("inters"), "Inter not found");
        assertNotNull(model.get("writer"), "Writer not found");
    }

    @Test
    public void nextFrag() throws Exception{
        MvcResult result = mvc.perform(get("/fragments/fragment/Fr008/inter/Fr008_WIT_ED_CRIT_C/next")).andReturn();

        String view = result.getModelAndView().getViewName();

        assertEquals("redirect:/fragments/fragment/Fr014/inter/Fr014_WIT_ED_CRIT_C", view);
    }

    @Test
    public void prevFrag() throws Exception{
        MvcResult result = mvc.perform(get("/fragments/fragment/Fr014/inter/Fr014_WIT_ED_CRIT_C/prev")).andReturn();

        String view = result.getModelAndView().getViewName();

        assertEquals("redirect:/fragments/fragment/Fr008/inter/Fr008_WIT_ED_CRIT_C", view);
    }

}
