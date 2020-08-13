package pt.ist.socialsoftware.edition.ldod.controller.reading;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.controller.ReadingController;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;

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

		TestLoadUtils.loadVirtualEditionsCorpus();
		String[] virtualEditionFragments = {"virtual-Fr001.xml", "virtual-Fr002.xml", "virtual-Fr003.xml"};
		TestLoadUtils.loadVirtualEditionFragments(virtualEditionFragments);
	}

	@AfterAll
	@Atomic(mode = TxMode.WRITE)
	public static void tearDownAll() throws FileNotFoundException {
		TestLoadUtils.cleanDatabaseButCorpus();
	}

	@BeforeEach
	public void setUp() {
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
		MvcResult mvcResult = this.mockMvc.perform(post("/reading/weight")
				.param("type", "heteronym")
				.param("value", "0.5"))
				.andReturn();
		MockHttpSession session = (MockHttpSession) mvcResult
				.getRequest().getSession();

		mvcResult = this.mockMvc.perform(post("/reading/weight").session(session)
				.param("type", "date")
				.param("value", "0.5"))
				.andReturn();
		session = (MockHttpSession) mvcResult
				.getRequest().getSession();

		mvcResult = this.mockMvc.perform(post("/reading/weight").session(session)
				.param("type", "text")
				.param("value", "0.5"))
				.andReturn();
		session = (MockHttpSession) mvcResult
				.getRequest().getSession();

		mvcResult = this.mockMvc.perform(post("/reading/weight").session(session)
				.param("type", "taxonomy")
				.param("value", "0.5"))
				.andReturn();
		session = (MockHttpSession) mvcResult
				.getRequest().getSession();

		this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}", "Fr001", "Fr001_WIT_ED_CRIT_P").session(session))
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

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void readFromInterTest() throws Exception {
		this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/start","Fr001","Fr001_WIT_ED_CRIT_P"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/reading/fragment/Fr001/inter/Fr001_WIT_ED_CRIT_P"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void readFromInterXmlErrorTest() throws Exception {
		this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/start","ERROR","Fr001_WIT_ED_CRIT_P"))
				.andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void readFromInterUrlErrorTest() throws Exception {
		this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/start","Fr001","ERROR"))
				.andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void readNextInterpretationTest() throws Exception {
		this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/next","Fr001","Fr001_WIT_ED_CRIT_P"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("/reading/fragment/*/inter/*"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void readNextInterXmlErrorTest() throws Exception {
		this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/next","ERROR","Fr001_WIT_ED_CRIT_P"))
				.andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void readNextInterUrlErrorTest() throws Exception {
		this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/next", "Fr001", "ERROR"))
				.andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void readPrevInterpretationTest() throws Exception {
		this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/prev","Fr003","Fr003_WIT_ED_CRIT_P"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("/reading/fragment/*/inter/*"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void readPrevInterXmlErrorTest() throws Exception {
		this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/prev","ERROR","Fr001_WIT_ED_CRIT_P"))
				.andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void readPrevInterUrlErrorTest() throws Exception {
		this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/prev", "Fr001", "ERROR"))
				.andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void readPreviousRecommendedFragmentErrorTest() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/reading/edition/{acronym}/start", Edition.ZENITH_EDITION_ACRONYM)).andDo(print())
				.andDo(print())
				.andReturn();
		MockHttpSession session = (MockHttpSession) mvcResult
				.getRequest().getSession();

//		mvcResult = this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}","Fr003","Fr003_WIT_ED_CRIT_Z").session(session))
//				.andDo(print())
//				.andReturn();
//		session = (MockHttpSession) mvcResult
//				.getRequest().getSession();

//		mvcResult = this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}","Fr002","Fr002_WIT_ED_CRIT_P").session(session))
//				.andDo(print())
//				.andReturn();
//		session = (MockHttpSession) mvcResult
//				.getRequest().getSession();

		this.mockMvc.perform(get("/reading/inter/prev/recom").session(session))
				.andDo(print())
				.andReturn();
//				.andExpect(status().is3xxRedirection())
//				.andExpect(redirectedUrlPattern("/reading/fragment/*/inter/*"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void resetPreviousRecommendedFragmentsTest() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}", "Fr001", "Fr001_WIT_ED_CRIT_P"))
				.andReturn();
		MockHttpSession session = (MockHttpSession) mvcResult
				.getRequest().getSession();

		mvcResult = this.mockMvc.perform(get("/reading/fragment/{xmlId}/inter/{urlId}/next","Fr003","Fr003_WIT_ED_CRIT_P").session(session))
				.andReturn();
		session = (MockHttpSession) mvcResult
				.getRequest().getSession();

		this.mockMvc.perform(get("/reading/inter/prev/recom/reset").session(session))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("/reading/fragment/*/inter/*"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void changeWeightHeteronymTest() throws Exception {
		this.mockMvc.perform(post("/reading/weight")
				.param("type", "heteronym")
				.param("value", "0.5"))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void changeWeightDateTest() throws Exception {
		this.mockMvc.perform(post("/reading/weight")
				.param("type", "date")
				.param("value", "0.5"))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void changeWeightTextTest() throws Exception {
		this.mockMvc.perform(post("/reading/weight")
				.param("type", "text")
				.param("value", "0.5"))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void changeWeightTaxonomyTest() throws Exception {
		this.mockMvc.perform(post("/reading/weight")
				.param("type", "taxonomy")
				.param("value", "0.5"))
				.andDo(print())
				.andExpect(status().isOk());
	}


}
