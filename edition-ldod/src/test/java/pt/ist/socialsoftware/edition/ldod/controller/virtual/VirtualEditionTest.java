package pt.ist.socialsoftware.edition.ldod.controller.virtual;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import pt.ist.socialsoftware.edition.ldod.ControllersTestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.controller.VirtualEditionController;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;

import java.io.FileNotFoundException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class VirtualEditionTest extends ControllersTestWithFragmentsLoading {

    @InjectMocks
    VirtualEditionController virtualEditionController;

    @Override
    protected Object getController() {
        return this.virtualEditionController;
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
    public void listVirtualEditionsTest() throws Exception {
        this.mockMvc.perform(get("/virtualeditions")).andDo(print())
                .andExpect(status().isOk()).andExpect(view().name("virtual/editions"))
                .andExpect(model().attribute("ldod",notNullValue()))
                .andExpect(model().attribute("user",nullValue()))
                .andExpect(model().attribute("expertEditions",hasSize(4)))
                .andExpect(model().attribute("virtualEditions",hasSize(1)));
    }

    /*@Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createVirtualEditionTest() throws Exception {
        this.mockMvc.perform(get("/virtualeditions/restricted/create")
                .param("acronym", "Test")
                .param("title", "Test")
                .param("pub","true")
                .param("use","no")).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/virtualeditions"));
    }*/

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void manageVirtualEditionsTest() throws Exception {

        String id = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM).getExternalId();

        this.mockMvc.perform(get("/virtualeditions/restricted/manage/{externalId}",id)).andDo(print())
                .andExpect(status().isOk()).andExpect(view().name("virtual/manage"))
                .andExpect(model().attribute("user",nullValue()))
                .andExpect(model().attribute("virtualEdition",notNullValue()))
                .andExpect(model().attribute("countriesList",hasSize(8)));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void manageVirtualEditionsErrorTest() throws Exception {

        this.mockMvc.perform(get("/virtualeditions/restricted/manage/{externalId}","ERROR"))
                .andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void editVirtualEditionTest() throws Exception {

        VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

        this.mockMvc.perform(get("/virtualeditions/restricted/editForm/{externalId}",ve.getExternalId()))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(view().name("virtual/edition"))
                .andExpect(model().attribute("virtualEdition",notNullValue()))
                .andExpect(model().attribute("externalId",equalTo(ve.getExternalId())))
                .andExpect(model().attribute("acronym",ve.getShortAcronym()))
                .andExpect(model().attribute("title",ve.getTitle()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void editVirtualEditionErrorTest() throws Exception {
        this.mockMvc.perform(get("/virtualeditions/restricted/editForm/{externalId}","ERROR"))
                .andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }
}
