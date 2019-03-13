package pt.ist.socialsoftware.edition.ldod.controller.edition;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
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
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.ControllersTestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.EditionController;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.Text;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class GetEditionControllerTest extends ControllersTestWithFragmentsLoading {

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
				.andExpect(model().attribute("heteronym", nullValue())).andExpect(model().attribute("editionDto",
						hasProperty("acronym", equalTo(Edition.PIZARRO_EDITION_ACRONYM))));

	}

	@Test
	public void errorEdition() throws Exception {
		this.mockMvc.perform(get("/edition/acronym/{acronym}", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void getEditionByExternalId() throws Exception {
		String id = Text.getInstance().getEdition(Edition.PIZARRO_EDITION_ACRONYM).getExternalId();

		this.mockMvc.perform(get("/edition/internalid/{externalId}", id)).andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/edition/acronym/" + Edition.PIZARRO_EDITION_ACRONYM));
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void getEditionWithHeteronym() throws Exception {
		String editionId = Text.getInstance().getEdition(Edition.PIZARRO_EDITION_ACRONYM).getExternalId();
		String hetronymId = Text.getInstance().getSortedHeteronyms().get(0).getExternalId();

		this.mockMvc.perform(get("/edition/internalid/heteronym/{id1}/{id2}", editionId, hetronymId)).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("edition/tableOfContents"))
				.andExpect(model().attribute("heteronym", notNullValue()))
				.andExpect(model().attribute("edition", notNullValue())).andExpect(model().attribute("editionDto",
						hasProperty("acronym", equalTo(Edition.PIZARRO_EDITION_ACRONYM))));
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void getEditionWithErrorAcronymId() throws Exception {
		String hetronymId = Text.getInstance().getSortedHeteronyms().get(0).getExternalId();

		this.mockMvc.perform(get("/edition/internalid/heteronym/{id1}/{id2}", "ERROR", hetronymId)).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void getEditionWithErrorHeteronym() throws Exception {
		String editionId = Text.getInstance().getEdition(Edition.PIZARRO_EDITION_ACRONYM).getExternalId();

		this.mockMvc.perform(get("/edition/internalid/heteronym/{id1}/{id2}", editionId, "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void getUserContributionsTest() throws Exception {
		this.mockMvc.perform(get("/edition/user/{username}", "ars")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("edition/userContributions")).andExpect(model().attribute("games", nullValue()))
				.andExpect(model().attribute("position", nullValue()))
				.andExpect(model().attribute("userDto", hasProperty("username", equalTo("ars"))));
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void getErrorUserContributionsTest() throws Exception {
		this.mockMvc.perform(get("/edition/user/{username}", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

}
