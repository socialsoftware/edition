package pt.ist.socialsoftware.edition.ldod.controller.reading;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
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
import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.controller.ReadingController;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ReadingTest {

	@InjectMocks
	ReadingController readingController;

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
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.readingController)
				.setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
	}

	@Test
	public void readingMainTest() throws Exception {
		this.mockMvc.perform(get("/reading")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("reading/readingMain")).andExpect(model().attribute("inter", nullValue()));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void readInterpretationTest() throws Exception {
		this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}", "Fr001", "Fr001_WIT_ED_CRIT_P"))
				.andDo(print()).andExpect(status().isOk()).andExpect(view().name("reading/readingMain"))
				.andExpect(model().attribute("inter", notNullValue()));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void readInterpretationFragErrorTest() throws Exception {
		this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}", "ERROR", "Fr001_WIT_ED_CRIT_P"))
				.andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void readInterpretationInterErrorTest() throws Exception {
		this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}", "Fr001", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void startReadEditionTest() throws Exception {
		this.mockMvc.perform(get("/reading/edition/{acronym}/start", Edition.PIZARRO_EDITION_ACRONYM)).andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/reading/fragment/Fr001/inter/Fr001_WIT_ED_CRIT_P"));
	}
}
