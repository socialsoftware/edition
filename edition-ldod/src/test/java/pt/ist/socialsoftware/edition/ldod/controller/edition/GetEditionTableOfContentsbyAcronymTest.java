package pt.ist.socialsoftware.edition.ldod.controller.edition;

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

import pt.ist.socialsoftware.edition.ldod.ControllersTestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.EditionController;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class GetEditionTableOfContentsbyAcronymTest extends ControllersTestWithFragmentsLoading {

	@InjectMocks
	private EditionController editionController;

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
		return this.editionController;
	}

	@Test
	public void getEditionPizzarroTest() throws Exception {
		this.mockMvc.perform(get("/edition/acronym/{acronym}", Edition.PIZARRO_EDITION_ACRONYM)).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("edition/tableOfContents"))
				.andExpect(model().attribute("heteronym", nullValue()))
				.andExpect(model().attribute("editionDto", hasProperty("acronym",equalTo(Edition.PIZARRO_EDITION_ACRONYM))));

	}

	@Test
	public void errorEdition() throws Exception {
		this.mockMvc.perform(get("/edition/acronym/{acronym}", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

}
