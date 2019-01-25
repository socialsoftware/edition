package pt.ist.socialsoftware.edition.ldod.controller.fragment;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.FileNotFoundException;

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
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.FragmentController;
import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class FragmentTest {
	@InjectMocks
	FragmentController fragmentController;

	protected MockMvc mockMvc;

	@BeforeAll
	@Atomic(mode = TxMode.WRITE)
	public static void setUpAll() throws FileNotFoundException {
		TestLoadUtils.setUpDatabaseWithCorpus();

		String[] fragments = { "001.xml", "002.xml", "003.xml" };
		TestLoadUtils.loadFragments(fragments);
	}

	@AfterAll
	@Atomic(mode = TxMode.WRITE)
	public static void tearDownAll() throws FileNotFoundException {
		TestLoadUtils.cleanDatabaseButCorpus();
	}

	@BeforeEach
	public void setUp() throws FileNotFoundException {
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.fragmentController)
				.setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void getFragmentsListTest() throws Exception {
		this.mockMvc.perform(get("/fragments")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("fragment/list"))
				.andExpect(model().attributeExists("jpcEdition", "tscEdition", "rzEdition", "jpEdition", "fragments"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void getFragmentByXmlId() throws Exception {
		this.mockMvc.perform(get("/fragments/fragment/{xmlId}", "Fr001")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("fragment/main")).andExpect(model().attribute("fragment", notNullValue()))
				.andExpect(model().attribute("fragmentDto", hasProperty("xmlId", equalTo("Fr001"))));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void getFragInterFromId() throws Exception {
		this.mockMvc.perform(get("/fragments/fragment/{xmlId}/inter/{urlId}", "Fr001", "Fr001_WIT_MS_Fr001a_1"))
				.andDo(print()).andExpect(status().isOk()).andExpect(view().name("fragment/main"))
				.andExpect(model().attribute("fragment", notNullValue()))
				.andExpect(model().attribute("inters", notNullValue()));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void getFragInterFromErrorId() throws Exception {
		this.mockMvc.perform(get("/fragments/fragment/{xmlId}/inter/{urlId}", "Fr001", "Error")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void getFragInterByExternalId() throws Exception {
		FragInter fragInter = LdoD.getInstance().getFragmentByXmlId("Fr001")
				.getFragInterByUrlId("Fr001_WIT_MS_Fr001a_1");

		this.mockMvc.perform(get("/fragments/fragment//inter/{externalId}", fragInter.getExternalId())).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/fragments/fragment/"
						+ fragInter.getFragment().getXmlId() + "/inter/" + fragInter.getUrlId()));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void getFragInterByExternalIdError() throws Exception {
		this.mockMvc.perform(get("/fragments/fragment//inter/{externalId}", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void getFragmentErrorId() throws Exception {
		this.mockMvc.perform(get("/fragments/fragment/{xmlId}", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

}
