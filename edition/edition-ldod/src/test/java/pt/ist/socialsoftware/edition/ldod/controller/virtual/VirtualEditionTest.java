package pt.ist.socialsoftware.edition.ldod.controller.virtual;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.controller.VirtualEditionController;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.utils.TopicDTO;
import pt.ist.socialsoftware.edition.ldod.utils.TopicInterPercentageDTO;
import pt.ist.socialsoftware.edition.ldod.utils.TopicListDTO;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class VirtualEditionTest {

	@InjectMocks
	VirtualEditionController virtualEditionController;

	protected MockMvc mockMvc;

	@BeforeEach
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void setUp() throws FileNotFoundException {
		TestLoadUtils.setUpDatabaseWithCorpus();

		String[] fragments = { "001.xml", "002.xml", "003.xml" };
		TestLoadUtils.loadFragments(fragments);

		TestLoadUtils.loadVirtualEditionsCorpus();
		String[] virtualEditionFragments = {"virtual-Fr001.xml", "virtual-Fr002.xml", "virtual-Fr003.xml"};
		TestLoadUtils.loadVirtualEditionFragments(virtualEditionFragments);

		this.mockMvc = MockMvcBuilders.standaloneSetup(this.virtualEditionController)
				.setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
	}

	@AfterEach
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void tearDown() throws FileNotFoundException {
		TestLoadUtils.cleanDatabaseButCorpus();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void listVirtualEditionsTest() throws Exception {
		this.mockMvc.perform(get("/virtualeditions")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("virtual/editions"))
				.andExpect(model().attribute("ldod", notNullValue()))
				.andExpect(model().attribute("user", nullValue()))
				.andExpect(model().attribute("expertEditions", hasSize(4)))
				.andExpect(model().attribute("virtualEditions", hasSize(2)));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void manageVirtualEditionsTest() throws Exception {

		String id = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM).getExternalId();

		this.mockMvc.perform(get("/virtualeditions/restricted/manage/{externalId}", id)).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("virtual/manage"))
				.andExpect(model().attribute("user", nullValue()))
				.andExpect(model().attribute("virtualEdition", notNullValue()))
				.andExpect(model().attribute("countriesList", hasSize(8)));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void manageVirtualEditionsErrorTest() throws Exception {

		this.mockMvc.perform(get("/virtualeditions/restricted/manage/{externalId}", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void editFormVirtualEditionTest() throws Exception {

		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		this.mockMvc.perform(get("/virtualeditions/restricted/editForm/{externalId}", ve.getExternalId()))
				.andDo(print()).andExpect(status().isOk()).andExpect(view().name("virtual/edition"))
				.andExpect(model().attribute("virtualEdition", notNullValue()))
				.andExpect(model().attribute("externalId", equalTo(ve.getExternalId())))
				.andExpect(model().attribute("acronym", ve.getShortAcronym()))
				.andExpect(model().attribute("title", ve.getTitle()));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void editFormVirtualEditionErrorTest() throws Exception {
		this.mockMvc.perform(get("/virtualeditions/restricted/editForm/{externalId}", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void showTaxonomyTest() throws Exception {

		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/taxonomy", ve.getExternalId()))
				.andDo(print()).andExpect(status().isOk()).andExpect(view().name("virtual/taxonomy"))
				.andExpect(model().attribute("virtualEdition", notNullValue()));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void showTaxonomyErrorTest() throws Exception {
		this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/taxonomy", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void showFragInterTest() throws Exception {

		String id = LdoD.getInstance().getFragmentByXmlId("Fr001").getFragInterByUrlId("Fr001_WIT_MS_Fr001a_1")
				.getExternalId();

		this.mockMvc.perform(get("/virtualeditions/restricted/fraginter/{fragInterId}", id)).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("virtual/fragInter"))
				.andExpect(model().attribute("fragInter", notNullValue()));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void showFragInterErrorTest() throws Exception {
		this.mockMvc.perform(get("/virtualeditions/restricted/fraginter/{fragInterId}", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void createVirtualEditionTest() throws Exception {
		this.mockMvc
				.perform(post("/virtualeditions/restricted/create").param("acronym", "Test").param("title", "Test")
						.param("pub", "true").param("use", "no"))
				.andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));

		VirtualEdition testEdition = LdoD.getInstance().getVirtualEdition(VirtualEdition.ACRONYM_PREFIX + "Test");

		assertEquals("Test", testEdition.getShortAcronym());
		assertEquals("Test", testEdition.getTitle());
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void editVirtualEditionIsSAVETest() throws Exception {
		VirtualEdition testEdition = LdoD.getInstance().getVirtualEdition(VirtualEdition.ACRONYM_PREFIX + "Teste");

		this.mockMvc
				.perform(post("/virtualeditions/restricted/edit/{externalId}", testEdition.getExternalId())
						.param("acronym", "Teste")
						.param("title", "Title")
						.param("synopsis", "Synopsis")
						.param("pub", "false")
						.param("management", "true")
						.param("vocabulary", "true")
						.param("annotation", "true")
						.param("mediasource", "Twitter")
						.param("begindate", "")
						.param("enddate", "")
						.param("geolocation", "Spain")
						.param("frequency", "45")
				)
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/virtualeditions/restricted/manage/" + testEdition.getExternalId()));

		assertEquals("Title", testEdition.getTitle());
		assertFalse(testEdition.getPub());
		assertEquals("Synopsis", testEdition.getSynopsis());
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void editVirtualEditionIsNotSAVETest() throws Exception {
		VirtualEdition testEdition = LdoD.getInstance().getVirtualEdition(VirtualEdition.ACRONYM_PREFIX + "Teste");

		this.mockMvc
				.perform(post("/virtualeditions/restricted/edit/{externalId}", testEdition.getExternalId())
						.param("acronym", "Teste")
						.param("title", "Title")
						.param("synopsis", "Synopsis")
						.param("pub", "false")
						.param("management", "true")
						.param("vocabulary", "true")
						.param("annotation", "true")
						.param("mediasource", "noMediaSource")
						.param("begindate", "")
						.param("enddate", "")
						.param("geolocation", "Spain")
						.param("frequency", "45")
				)
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/virtualeditions/restricted/manage/" + testEdition.getExternalId()));

		assertEquals("Title", testEdition.getTitle());
		assertFalse(testEdition.getPub());
		assertEquals("Synopsis", testEdition.getSynopsis());
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void deleteVirtualEditionTest() throws Exception {
		this.mockMvc
				.perform(post("/virtualeditions/restricted/create").param("acronym", "Test").param("title", "Test")
						.param("pub", "true").param("use", "no"))
				.andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));

		VirtualEdition testEdition = LdoD.getInstance().getVirtualEdition(VirtualEdition.ACRONYM_PREFIX + "Test");

		String id = testEdition.getExternalId();

		this.mockMvc.perform(post("/virtualeditions/restricted/delete").param("externalId", id)).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));

		assertNull(LdoD.getInstance().getVirtualEdition(VirtualEdition.ACRONYM_PREFIX + "Test"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void deleteVirtualEditionErrorTest() throws Exception {
		this.mockMvc.perform(post("/virtualeditions/restricted/delete").param("externalId", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void toggleVirtualEditionTest() throws Exception {

		String id = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM).getExternalId();

		this.mockMvc.perform(post("/virtualeditions/toggleselection").param("externalId", id)).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void toggleVirtualEditionErrorTest() throws Exception {

		this.mockMvc.perform(post("/virtualeditions/toggleselection").param("externalId", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void reorderVirtualEditionTest() throws Exception {

		String id = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM).getExternalId();

		this.mockMvc.perform(post("/virtualeditions/restricted/reorder/{externalId}", id).param("fraginters", ""))
				.andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void reorderErrorVirtualEditionTest() throws Exception {

		this.mockMvc.perform(post("/virtualeditions/restricted/reorder/{externalId}", "ERROR").param("fraginters", ""))
				.andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void getEditionsTest() throws Exception {
		this.mockMvc.perform(get("/virtualeditions/public"))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(notNullValue()));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void getFragmentsOfVirtualEditionTest() throws Exception {
		this.mockMvc.perform(get("/virtualeditions/acronym/{acronym}/fragments", Edition.ARCHIVE_EDITION_ACRONYM))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(notNullValue()));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void getFragmentsOfExpertEditionTest() throws Exception {
		this.mockMvc.perform(get("/virtualeditions/acronym/{acronym}/fragments", Edition.COELHO_EDITION_ACRONYM))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(notNullValue()));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void getFragmentsErrorTest() throws Exception {

		this.mockMvc.perform(get("/virtualeditions/acronym/{acronym}/fragments", "ERROR")).andDo(print())
				.andExpect(status().is4xxClientError()).andExpect(content().string(""));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void getInterIdTFIDFTermsTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);
		VirtualEditionInter inter = ve.getAllDepthVirtualEditionInters().get(0);

		this.mockMvc.perform(get("/virtualeditions/acronym/{acronym}/interId/{interId}/tfidf", Edition.ARCHIVE_EDITION_ACRONYM, inter.getExternalId()))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(notNullValue()));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void getTranscriptionsTest() throws Exception {
		this.mockMvc.perform(get("/virtualeditions/acronym/{acronym}/transcriptions", Edition.ARCHIVE_EDITION_ACRONYM))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(notNullValue()));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void getTranscriptionsErrorTest() throws Exception {

		this.mockMvc.perform(get("/virtualeditions/acronym/{acronym}/transcriptions", "ERROR")).andDo(print())
				.andExpect(status().is4xxClientError()).andExpect(content().string(""));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void showParticipantsTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/participants", ve.getExternalId()))
				.andDo(print()).andExpect(status().isOk()).andExpect(view().name("virtual/participants"))
				.andExpect(model().attribute("virtualEdition", notNullValue()))
				.andExpect(model().attribute("username", nullValue()));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void showParticipantsErrorTest() throws Exception {
		this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/participants", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void submitParticipantTest() throws Exception {
		String id = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM).getExternalId();

		this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/submit", id)).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void submitParticipantErrorTest() throws Exception {

		this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/submit", "ERROR"))
				.andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void cancelParticipantTest() throws Exception {

		String id = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM).getExternalId();

		this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/cancel", id)).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void cancelParticipantErrorTest() throws Exception {

		this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/cancel", "ERROR"))
				.andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void approveParticipantTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/approve", ve.getExternalId())
				.param("username", ve.getParticipantList().get(0).getUsername()))
				.andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions/restricted/" + ve.getExternalId() + "/participants"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void addParticipantTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		LdoDUser user = new LdoDUser(LdoD.getInstance(), "ola", "xpto", "A", "Z", "a@a.a");

		this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/add", ve.getExternalId())
				.param("username", user.getUsername()))
				.andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions/restricted/" + ve.getExternalId() + "/participants"));

		user.remove();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void switchRoleTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		LdoDUser user = new LdoDUser(LdoD.getInstance(), "ola", "xpto", "A", "Z", "a@a.a");
		ve.addMember(user, Member.MemberRole.MEMBER, true);

		this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/role", ve.getExternalId())
				.param("username", user.getUsername()))
				.andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions/restricted/" + ve.getExternalId() + "/participants"));

		user.remove();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void removeParticipantTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		LdoDUser user = new LdoDUser(LdoD.getInstance(), "ola", "xpto", "A", "Z", "a@a.a");
		ve.addMember(user, Member.MemberRole.MEMBER, true);

		this.mockMvc.perform(post("/virtualeditions/restricted/{externalId}/participants/remove", ve.getExternalId())
				.param("userId", user.getExternalId()))
				.andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/virtualeditions/restricted/" + ve.getExternalId() + "/participants"));

		user.remove();
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void getTaxonomyTest() throws Exception {
		String id = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM).getExternalId();

		this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/taxonomy", id)).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("virtual/taxonomy"))
				.andExpect(model().attribute("virtualEdition", notNullValue()));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void getTaxonomyErrorTest() throws Exception {
		this.mockMvc.perform(get("/virtualeditions/restricted/{externalId}/taxonomy", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void editTaxonomyTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		this.mockMvc
				.perform(post("/virtualeditions/restricted/{externalId}/taxonomy/edit", ve.getExternalId())
								.param("management", "true")
								.param("vocabulary", "true")
								.param("annotation", "true"))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/virtualeditions/restricted/" + ve.getExternalId() + "/taxonomy"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void deleteTaxonomyTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		this.mockMvc
				.perform(post("/virtualeditions/restricted/{externalId}/taxonomy/clean", ve.getExternalId())
						.param("taxonomyExternalId", ve.getTaxonomy().getExternalId()))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/virtualeditions/restricted/" + ve.getExternalId() + "/taxonomy"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void deleteErrorTaxonomyTest() throws Exception {

		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		this.mockMvc
				.perform(post("/virtualeditions/restricted/{externalId}/taxonomy/clean", ve.getExternalId())
						.param("taxonomyExternalId", "ERROR"))
				.andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void createCategoryTest() throws Exception {

		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		this.mockMvc
				.perform(post("/virtualeditions/restricted/category/create").param("externalId", ve.getExternalId())
						.param("name", "test"))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/virtualeditions/restricted/" + ve.getExternalId() + "/taxonomy"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void createCategoryErrorTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		this.mockMvc
				.perform(post("/virtualeditions/restricted/category/create").param("externalId", "ERROR").param("name",
						"test"))
				.andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void updateCategoryTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		Category category = ve.getTaxonomy().createCategory("first");

		this.mockMvc
				.perform(post("/virtualeditions/restricted/category/update").
						param("categoryId", category.getExternalId())
						.param("name", "second"))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/virtualeditions/restricted/category/" + category.getExternalId()));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void deleteCategoryTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		Category category = ve.getTaxonomy().createCategory("first");

		this.mockMvc
				.perform(post("/virtualeditions/restricted/category/delete")
						.param("categoryId", category.getExternalId()))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/virtualeditions/restricted/" + ve.getExternalId() + "/taxonomy"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void showCategoryTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		Category category = ve.getTaxonomy().createCategory("first");

		this.mockMvc
				.perform(get("/virtualeditions/restricted/category/{categoryId}",  category.getExternalId()))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("virtual/category"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void mergeCategoriesMergeTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		String[] categories = {
				ve.getTaxonomy().createCategory("first").getExternalId(),
				ve.getTaxonomy().createCategory("second").getExternalId()
		};

		this.mockMvc
				.perform(post("/virtualeditions/restricted/category/mulop")
						.param("taxonomyId", ve.getTaxonomy().getExternalId())
						.param("type", "merge")
						.param("categories[]", categories))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("/virtualeditions/restricted/category/*"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void extractCategoryTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		String categoryId = ve.getTaxonomy().createCategory("first-to-extract").getExternalId();

		String[] inters = {
				ve.getAllDepthVirtualEditionInters().get(0).getExternalId(),
		};

		this.mockMvc
				.perform(post("/virtualeditions/restricted/category/extract")
						.param("categoryId", categoryId)
						.param("inters[]", inters))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("/virtualeditions/restricted/category/*"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void associateCategoryTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		String[] categories = {
				ve.getTaxonomy().createCategory("first-to-associate").getName()
		};

		this.mockMvc
				.perform(post("/virtualeditions/restricted/tag/associate")
						.param("fragInterId", ve.getAllDepthVirtualEditionInters().get(0).getExternalId())
						.param("categories[]", categories))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("/fragments/fragment/inter/*"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void dissociateCategoryTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);
		Category category = ve.getTaxonomy().createCategory("first-to-dissociate");
		Set<String> categories = new HashSet<String>();
		categories.add(category.getName());

		VirtualEditionInter inter = ve.getAllDepthVirtualEditionInters().get(0);
		inter.associate(LdoDUser.getAuthenticatedUser(), categories);

		this.mockMvc
				.perform(get("/virtualeditions/restricted/fraginter/{fragInterId}/tag/dissociate/{categoryId}",inter.getExternalId(), category.getExternalId()))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("/fragments/fragment/inter/*"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void mergeCategoriesDeleteTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		String[] categories = {
				ve.getTaxonomy().createCategory("first").getExternalId(),
		};

		this.mockMvc
				.perform(post("/virtualeditions/restricted/category/mulop")
						.param("taxonomyId", ve.getTaxonomy().getExternalId())
						.param("type", "delete")
						.param("categories[]", categories))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/virtualeditions/restricted/" + ve.getExternalId() + "/taxonomy"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void generateTopicModellingTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		this.mockMvc
				.perform(get("/virtualeditions/restricted/{externalId}/taxonomy/generateTopics", ve.getExternalId())
						.param("numTopics", "5")
						.param("numWords", "2")
						.param("thresholdCategories", "5")
						.param("numIterations", "5"))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void createTopicModellingTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		VirtualEditionInter virtualEditionInter = ve.getAllDepthVirtualEditionInters().get(0);

		TopicListDTO topicList = new TopicListDTO();
		topicList.setTaxonomyExternalId(ve.getTaxonomy().getExternalId());
		topicList.setUsername("ars");
		TopicDTO topicDto = new TopicDTO();
		topicDto.setName("first");
		TopicInterPercentageDTO topicInter = new TopicInterPercentageDTO();
		topicInter.setExternalId(virtualEditionInter.getExternalId());
		topicInter.setTitle("Title");
		topicInter.setPercentage(34);
		List<TopicInterPercentageDTO> topicInters = new ArrayList<>();
		topicInters.add(topicInter);
		topicDto.setInters(topicInters);
		List<TopicDTO> topics = new ArrayList<>();
		topics.add(topicDto);
		topicList.setTopics(topics);

		this.mockMvc
				.perform(get("/virtualeditions/restricted/{externalId}/taxonomy/createTopics", ve.getExternalId()))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void getTranscriptionsTagTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition("LdoD-Teste");

		this.mockMvc
				.perform(get("/virtualeditions/acronym/{acronym}/{category}/transcriptions", ve.getAcronym(), ve.getTaxonomy().getSortedCategories().get(0).getName())
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void addInterTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		VirtualEditionInter virtualEditionInter = ve.getAllDepthVirtualEditionInters().get(0);
		FragInter fragInter = virtualEditionInter.getUses();
		virtualEditionInter.remove();

		this.mockMvc
				.perform(post("/virtualeditions/restricted/addinter/{veId}/{interId}", ve.getExternalId(), fragInter.getExternalId()))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("/fragments/fragment/inter/*"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void classificationGameTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		this.mockMvc
				.perform(get("/virtualeditions/restricted/{externalId}/classificationGame", ve.getExternalId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("virtual/classificationGame"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void createClassificationGameFormTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		this.mockMvc
				.perform(get("/virtualeditions/restricted/{externalId}/classificationGame/create", ve.getExternalId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("virtual/createClassificationGame"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void createClassificationGameTest() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		this.mockMvc
				.perform(post("/virtualeditions/restricted/{externalId}/classificationGame/create", ve.getExternalId())
						.param("description", "Description")
						.param("date", "08/08/2020 09:00")
						.param("interExternalId", ve.getAllDepthVirtualEditionInters().get(0).getExternalId()))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/virtualeditions/restricted/" + ve.getExternalId() + "/classificationGame"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void removeClassificationGame() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		ClassificationGame classificationGame = new ClassificationGame(ve, "Description", DateTime.now(), ve.getAllDepthVirtualEditionInters().get(0), LdoDUser.getAuthenticatedUser());

		this.mockMvc
				.perform(post("/virtualeditions/restricted/{externalId}/classificationGame/{gameId}/remove", ve.getExternalId(), classificationGame.getExternalId()))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/virtualeditions/restricted/" + ve.getExternalId() + "/classificationGame"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void getClassificationGame() throws Exception {
		VirtualEdition ve = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);

		LdoDUser user = LdoD.getInstance().getUser("ars");

		ClassificationGame classificationGame = new ClassificationGame(ve, "Description", DateTime.now(), ve.getAllDepthVirtualEditionInters().get(0), user);
		classificationGame.addParticipant("ars");

		this.mockMvc
				.perform(get("/virtualeditions/{externalId}/classificationGame/{gameId}", ve.getExternalId(), classificationGame.getExternalId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("virtual/classificationGameContent"));
	}



}
