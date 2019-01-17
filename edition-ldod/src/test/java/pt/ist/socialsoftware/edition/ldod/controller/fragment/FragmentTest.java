package pt.ist.socialsoftware.edition.ldod.controller.fragment;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.ControllersTestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.FragmentController;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class FragmentTest extends ControllersTestWithFragmentsLoading {

    @InjectMocks
    FragmentController fragmentController;

    @Override
    protected Object getController() {
        return this.fragmentController;
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
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getFragmentsListTest() throws Exception {
        this.mockMvc.perform(get("/fragments")).andDo(print())
            .andExpect(status().isOk()).andExpect(view().name("fragment/list"))
            .andExpect(model().attributeExists("jpcEdition","tscEdition","rzEdition","jpEdition","fragments"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getFragmentByXmlId() throws Exception {
        this.mockMvc.perform(get("/fragments/fragment/{xmlId}","Fr001")).andDo(print())
                .andExpect(status().isOk()).andExpect(view().name("fragment/main"))
                .andExpect(model().attribute("fragment",notNullValue()))
                .andExpect(model().attribute("fragmentDto",hasProperty("xmlId",equalTo("Fr001"))));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getFragInterFromId() throws Exception {
        this.mockMvc.perform(get("/fragments/fragment/{xmlId}/inter/{urlId}","Fr001","Fr001_WIT_MS_Fr001a_1"))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(view().name("fragment/main"))
                .andExpect(model().attribute("fragment",notNullValue()))
                .andExpect(model().attribute("inters",notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getFragInterFromErrorId() throws Exception {
        this.mockMvc.perform(get("/fragments/fragment/{xmlId}/inter/{urlId}","Fr001","Error"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getFragInterByExternalId() throws Exception {
        FragInter fragInter = LdoD.getInstance().getFragmentByXmlId("Fr001").getFragInterByUrlId("Fr001_WIT_MS_Fr001a_1");

        this.mockMvc.perform(get("/fragments/fragment//inter/{externalId}",fragInter.getExternalId())).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/fragments/fragment/" + fragInter.getFragment().getXmlId() +
                        "/inter/" + fragInter.getUrlId()));
    }
    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getFragInterByExternalIdError() throws Exception {
        this.mockMvc.perform(get("/fragments/fragment//inter/{externalId}","ERROR")).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getFragmentErrorId() throws Exception {
        this.mockMvc.perform(get("/fragments/fragment/{xmlId}","ERROR")).andDo(print())
                    .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }




}
