package pt.ist.socialsoftware.edition.ldod.controller.citation;

import static org.hamcrest.Matchers.notNullValue;
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
import pt.ist.socialsoftware.edition.ldod.controller.CitationController;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ListCitationsTest extends ControllersTestWithFragmentsLoading {

	@InjectMocks
	CitationController citationController;

	@Override
	protected void populate4Test() {
	}

	@Override
	protected void unpopulate4Test() {
	}

	@Override
	protected String[] fragmentsToLoad4Test() {
		return new String[0];
	}

	@Override
	protected Object getController() {
		return this.citationController;
	}

	@Test
	public void listCitationsTest() throws Exception {
		this.mockMvc.perform(get("/citations")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("reading/citations")).andExpect(model().attribute("citations", notNullValue()));
	}
}
