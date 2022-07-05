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
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.ControllersTestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.EditionController;
import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.domain.Category;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;

import java.io.FileNotFoundException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class EditionControllerTest {
	@InjectMocks
	private EditionController editionController;

	protected MockMvc mockMvc;

	@BeforeAll
	@Atomic(mode = TxMode.WRITE)
	public static void setUpAll() throws FileNotFoundException {
		TestLoadUtils.setUpDatabaseWithCorpus();

		String[] fragments = { "001.xml", "002.xml", "003.xml" };
		TestLoadUtils.loadFragments(fragments);

		TestLoadUtils.loadVirtualEditionsCorpus();
		String[] virtualEditionFragments = {"virtual-Fr001.xml", "virtual-Fr002.xml", "virtual-Fr003.xml"};
		TestLoadUtils.loadVirtualEditionFragments(virtualEditionFragments);
	}

	@AfterAll
	@Atomic(mode = TxMode.WRITE)
	public static void tearDownAll() {
		TestLoadUtils.cleanDatabaseButCorpus();
	}

	@BeforeEach
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.editionController)
				.setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void getEditionPizzarroTest() throws Exception {
		this.mockMvc.perform(get("/edition/acronym/{acronym}", Edition.PIZARRO_EDITION_ACRONYM)).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("edition/tableOfContents"))
				.andExpect(model().attribute("heteronym", nullValue())).andExpect(model().attribute("editionDto",
						hasProperty("acronym", equalTo(Edition.PIZARRO_EDITION_ACRONYM))));

	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void errorEdition() throws Exception {
		this.mockMvc.perform(get("/edition/acronym/{acronym}", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void getEditionByExternalId() throws Exception {
		String id = LdoD.getInstance().getEdition(Edition.PIZARRO_EDITION_ACRONYM).getExternalId();

		this.mockMvc.perform(get("/edition/internalid/{externalId}", id)).andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/edition/acronym/" + Edition.PIZARRO_EDITION_ACRONYM));
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void getEditionWithHeteronym() throws Exception {
		String editionId = LdoD.getInstance().getEdition(Edition.PIZARRO_EDITION_ACRONYM).getExternalId();
		String hetronymId = LdoD.getInstance().getSortedHeteronyms().get(0).getExternalId();

		this.mockMvc.perform(get("/edition/internalid/heteronym/{id1}/{id2}", editionId, hetronymId)).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("edition/tableOfContents"))
				.andExpect(model().attribute("heteronym", notNullValue()))
				.andExpect(model().attribute("edition", notNullValue())).andExpect(model().attribute("editionDto",
						hasProperty("acronym", equalTo(Edition.PIZARRO_EDITION_ACRONYM))));
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void getEditionWithErrorAcronymId() throws Exception {
		String hetronymId = LdoD.getInstance().getSortedHeteronyms().get(0).getExternalId();

		this.mockMvc.perform(get("/edition/internalid/heteronym/{id1}/{id2}", "ERROR", hetronymId)).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void getEditionWithErrorHeteronym() throws Exception {
		String editionId = LdoD.getInstance().getEdition(Edition.PIZARRO_EDITION_ACRONYM).getExternalId();

		this.mockMvc.perform(get("/edition/internalid/heteronym/{id1}/{id2}", editionId, "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void getUserContributionsTest() throws Exception {
		this.mockMvc.perform(get("/edition/user/{username}", "ars"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("edition/userContributions"))
				.andExpect(model().attribute("games", notNullValue()))
				.andExpect(model().attribute("position", notNullValue()))
				.andExpect(model().attribute("userDto", hasProperty("username", equalTo("ars"))));
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void getErrorUserContributionsTest() throws Exception {
		this.mockMvc.perform(get("/edition/user/{username}", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void getTaxonomyTableOfContentsTest() throws Exception {
		this.mockMvc.perform(get("/edition/acronym/{acronym}/taxonomy", Edition.ARCHIVE_EDITION_ACRONYM))
				.andDo(print())
				.andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("edition/taxonomyTableOfContents"));
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void getCategoryTableOfContentsTest() throws Exception {
		Category category = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM)
				.getTaxonomy().getCategoriesSet().stream()
				.findAny()
				.orElse(null);

		this.mockMvc.perform(get("/edition/acronym/{acronym}/category/{urlId}", Edition.ARCHIVE_EDITION_ACRONYM, category.getUrlId()))
				.andDo(print())
				.andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("edition/categoryTableOfContents"));
	}

}
