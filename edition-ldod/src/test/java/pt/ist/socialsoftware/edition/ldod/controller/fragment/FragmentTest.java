package pt.ist.socialsoftware.edition.ldod.controller.fragment;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.FragmentController;
import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.loaders.VirtualEditionFragmentsTEIImport;
import pt.ist.socialsoftware.edition.ldod.loaders.VirtualEditionsTEICorpusImport;
import pt.ist.socialsoftware.edition.ldod.utils.AnnotationDTO;
import pt.ist.socialsoftware.edition.ldod.utils.PermissionDTO;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.ldod.utils.RangeJson;

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
	@Atomic(mode = TxMode.WRITE)
	public void setUp() throws FileNotFoundException {
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.fragmentController)
				.setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();

		TestLoadUtils.loadTestVirtualEdition();
	}

	@AfterEach
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {

		TestLoadUtils.deleteTestVirtualEdition();

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

		this.mockMvc.perform(get("/fragments/fragment/inter/{externalId}", fragInter.getExternalId())).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/fragments/fragment/"
						+ fragInter.getFragment().getXmlId() + "/inter/" + fragInter.getUrlId()));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void getFragInterByExternalIdError() throws Exception {
		this.mockMvc.perform(get("/fragments/fragment/inter/{externalId}", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}

	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void getFragmentErrorId() throws Exception {
		this.mockMvc.perform(get("/fragments/fragment/{xmlId}", "ERROR")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
	}


	@Test
	@Atomic(mode = Atomic.TxMode.WRITE)
	@WithUserDetails("ars")
	public void getFragInterTaxonomyTest() throws Exception {

		Set<FragInter> fragInterSet = LdoD.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

		List<FragInter> frags = new ArrayList<>(fragInterSet);

		FragInter fragInter = frags.get(0);

		this.mockMvc.perform(get("/fragments/fragment/inter/{externalId}/taxonomy",fragInter.getExternalId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("fragment/taxonomy"))
				.andExpect(model().attribute("ldoD", notNullValue()))
				.andExpect(model().attribute("user", notNullValue()))
				.andExpect(model().attribute("inters", notNullValue()));

	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	@WithUserDetails("ars")
	public void getInterTest() throws Exception {

		Fragment frag = LdoD.getInstance().getFragmentByXmlId("Fr001");

		this.mockMvc.perform(get("/fragments/fragment/inter")
				.param("fragment", frag.getExternalId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("fragment/main"))
				.andExpect(model().attribute("ldoD",notNullValue()))
				.andExpect(model().attribute("fragment",notNullValue()))
				.andExpect(model().attribute("user",notNullValue()))
				.andExpect(model().attribute("inters",notNullValue()));

	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	public void getInterEditorialTest() throws Exception {

		FragInter fragInter = LdoD.getInstance().getFragmentByXmlId("Fr001")
				.getFragInterByUrlId("Fr001_WIT_MS_Fr001a_1");

		this.mockMvc.perform(get("/fragments/fragment/inter/editorial")
				.param("interp[]", fragInter.getExternalId())
				.param("diff", "true"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("fragment/transcription"))
				.andExpect(model().attribute("inters",hasSize(1)))
				.andExpect(model().attribute("writer",notNullValue()));

	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	public void getInterAuthorialTest() throws Exception {

		FragInter fragInter = LdoD.getInstance().getFragmentByXmlId("Fr001")
				.getFragInterByUrlId("Fr001_WIT_MS_Fr001a_1");

		this.mockMvc.perform(get("/fragments/fragment/inter/authorial")
				.param("interp[]", fragInter.getExternalId())
				.param("diff", "true")
				.param("del", "true")
				.param("ins", "true")
				.param("subst", "true")
				.param("notes", "true")
				.param("facs", "true"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("fragment/facsimile"))
				.andExpect(model().attribute("inters",hasSize(1)))
				.andExpect(model().attribute("surface",notNullValue()))
				.andExpect(model().attribute("prevsurface",nullValue()))
				.andExpect(model().attribute("nextsurface",nullValue()))
				.andExpect(model().attribute("prevpb",nullValue()))
				.andExpect(model().attribute("nextpb",nullValue()))
				.andExpect(model().attribute("writer",notNullValue()));

	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	public void getInterAuthorialNoFacsTest() throws Exception {

		FragInter fragInter = LdoD.getInstance().getFragmentByXmlId("Fr001")
				.getFragInterByUrlId("Fr001_WIT_MS_Fr001a_1");

		this.mockMvc.perform(get("/fragments/fragment/inter/authorial")
				.param("interp[]", fragInter.getExternalId())
				.param("diff", "true")
				.param("del", "true")
				.param("ins", "true")
				.param("subst", "true")
				.param("notes", "true")
				.param("facs", "false"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("fragment/transcription"))
				.andExpect(model().attribute("inters",hasSize(1)))
				.andExpect(model().attribute("surface",nullValue()))
				.andExpect(model().attribute("prevsurface",nullValue()))
				.andExpect(model().attribute("nextsurface",nullValue()))
				.andExpect(model().attribute("prevpb",nullValue()))
				.andExpect(model().attribute("nextpb",nullValue()))
				.andExpect(model().attribute("writer",notNullValue()));

	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	public void getInterCompareTest() throws Exception {

		FragInter fragInter = LdoD.getInstance().getFragmentByXmlId("Fr001")
				.getFragInterByUrlId("Fr001_WIT_MS_Fr001a_1");

		this.mockMvc.perform(get("/fragments/fragment/inter/compare")
				.param("inters[]", fragInter.getExternalId())
				.param("spaces", "true")
				.param("line", "false"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("fragment/inter2CompareSideBySide"))
				.andExpect(model().attribute("fragment", notNullValue()))
				.andExpect(model().attribute("lineByLine", is(false)))
				.andExpect(model().attribute("inters", hasSize(1)))
				.andExpect(model().attribute("writer", notNullValue()));

	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	public void searchAnnotationsTest() throws Exception {

		Set<FragInter> fragInterSet = LdoD.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

		List<FragInter> frags = new ArrayList<>(fragInterSet);

		FragInter fragInter = frags.get(0);

		String response = this.mockMvc.perform(get("/fragments/fragment/search")
					.param("limit", "2")
					.param("uri", fragInter.getExternalId()))
					.andDo(print())
					.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		ObjectMapper mapper = new ObjectMapper();

		Map<String,Object> map = new HashMap<>();

		map = mapper.readValue(response, new TypeReference<Map<String, Object>>(){});

		assertEquals(0, map.get("total"));
	}


	@Test
	@Atomic(mode = TxMode.WRITE)
	@WithUserDetails("ars")
	public void createAnnotationTest() throws Exception {

        Set<FragInter> fragInterSet = LdoD.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

        List<FragInter> frags = new ArrayList<>(fragInterSet);

        FragInter fragInter = frags.get(0);

	    LdoD ldod = LdoD.getInstance();

		// create permissionDTO
        VirtualEdition ve = ldod.getArchiveEdition();
        LdoDUser user = ldod.getUser("ars");

        PermissionDTO permissionDTO = new PermissionDTO(ve,user);

		// create annotationDTO

        AnnotationDTO annotationDTO = new AnnotationDTO();
        annotationDTO.setPermissions(permissionDTO);
        annotationDTO.setUri(fragInter.getExternalId());
        annotationDTO.setQuote("A arte é um esquivar-se a agir");
        annotationDTO.setText("Interesting");
        annotationDTO.setUser("ars");

		RangeJson rj = new RangeJson();
		rj.setStart("/div[1]/div[1]/p[3]");
		rj.setStartOffset(3);
		rj.setEnd("/div[1]/div[1]/p[3]");
		rj.setEndOffset(7);

		List<RangeJson> list = new ArrayList<>();
		list.add(rj);

        annotationDTO.setRanges(list);
        annotationDTO.setTags(Arrays.asList("tag1","tag2"));

		// send request to mock
		this.mockMvc.perform(post("/fragments/fragment/annotations")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestLoadUtils.jsonBytes(annotationDTO)))
				.andDo(print())
				.andExpect(status().isCreated());
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	@WithUserDetails("ars")
	public void getAnnotationTest() throws Exception {

		Set<FragInter> fragInterSet = LdoD.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

		List<FragInter> frags = new ArrayList<>(fragInterSet);

		VirtualEditionInter fragInter = (VirtualEditionInter) frags.get(0);

		createTestAnnotation();

		HumanAnnotation a = new ArrayList<>(fragInter.getAllDepthHumanAnnotations()).get(0);

		AnnotationDTO dto = new AnnotationDTO();

		dto.setText("Even more interesting");
		dto.setTags(Arrays.asList("tag3","tag4"));

		this.mockMvc.perform(get("/fragments/fragment/annotations/{id}", a.getExternalId()))
					.andDo(print())
					.andExpect(status().isOk());
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	@WithUserDetails("ars")
	public void getAnnotationNotFoundTest() throws Exception{

		this.mockMvc.perform(get("/fragments/fragment/annotations/{id}", "ERROR"))
				.andDo(print())
				.andExpect(status().isNotFound());

	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	@WithUserDetails("ars")
	public void updateAnnotationTest() throws Exception {

		Set<FragInter> fragInterSet = LdoD.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

		List<FragInter> frags = new ArrayList<>(fragInterSet);

		VirtualEditionInter fragInter = (VirtualEditionInter) frags.get(0);

		createTestAnnotation();

		HumanAnnotation a = new ArrayList<>(fragInter.getAllDepthHumanAnnotations()).get(0);

		AnnotationDTO dto = new AnnotationDTO();

		dto.setText("Even more interesting");
		dto.setTags(Arrays.asList("tag3","tag4"));

		this.mockMvc.perform(put("/fragments/fragment/annotations/{id}", a.getExternalId())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestLoadUtils.jsonBytes(dto)))
				.andDo(print())
				.andExpect(status().isOk());


		assertEquals("Even more interesting", a.getText());
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	@WithUserDetails("ars")
	public void updateAnnotationNotFoundTest() throws Exception{

		AnnotationDTO dto = new AnnotationDTO();

		this.mockMvc.perform(put("/fragments/fragment/annotations/{id}", "ERROR")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestLoadUtils.jsonBytes(dto)))
				.andDo(print())
				.andExpect(status().isNotFound());

	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	@WithUserDetails("ars")
	public void deleteAnnotationTest() throws Exception {

		Set<FragInter> fragInterSet = LdoD.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

		List<FragInter> frags = new ArrayList<>(fragInterSet);

		VirtualEditionInter fragInter = (VirtualEditionInter) frags.get(0);

		createTestAnnotation();

		HumanAnnotation a = new ArrayList<>(fragInter.getAllDepthHumanAnnotations()).get(0);

		this.mockMvc.perform(delete("/fragments/fragment/annotations/{id}", a.getExternalId())
				.content(TestLoadUtils.jsonBytes(new AnnotationDTO()))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isNoContent());

		assertTrue(fragInter.getAllDepthHumanAnnotations().isEmpty());
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	@WithUserDetails("ars")
	public void deleteAnnotationNotFoundTest() throws Exception {

		this.mockMvc.perform(delete("/fragments/fragment/annotations/{id}", "ERROR")
				.content(TestLoadUtils.jsonBytes(new AnnotationDTO()))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	@WithUserDetails("ars")
	public void getAnnotationInterTest() throws Exception {
		Set<FragInter> fragInterSet = LdoD.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

		List<FragInter> frags = new ArrayList<>(fragInterSet);

		VirtualEditionInter fragInter = (VirtualEditionInter) frags.get(0);

		createTestAnnotation();

		HumanAnnotation a = new ArrayList<>(fragInter.getAllDepthHumanAnnotations()).get(0);

		this.mockMvc.perform(get("/fragments/fragment/annotation/{annotationId}/categories",a.getExternalId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(content().string(containsString("tag1")))
				.andExpect(content().string(containsString("tag2")));
	}

	private void createTestAnnotation() {

		Set<FragInter> fragInterSet = LdoD.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

		List<FragInter> frags = new ArrayList<>(fragInterSet);

		VirtualEditionInter fragInter = (VirtualEditionInter) frags.get(0);

		LdoDUser user = LdoD.getInstance().getUser("ars");

		RangeJson rj = new RangeJson();
		rj.setStart("/div[1]/div[1]/p[3]");
		rj.setStartOffset(3);
		rj.setEnd("/div[1]/div[1]/p[3]");
		rj.setEndOffset(7);

		List<RangeJson> list = new ArrayList<>();
		list.add(rj);

		fragInter.createHumanAnnotation("A arte é um esquivar-se a agir","Interesting",user,
				list,Arrays.asList("tag1","tag2"));

	}
}
