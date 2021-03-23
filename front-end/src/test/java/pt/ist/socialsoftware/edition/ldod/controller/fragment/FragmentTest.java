package pt.ist.socialsoftware.edition.ldod.controller.fragment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.frontend.config.Application;
import pt.ist.socialsoftware.edition.ldod.frontend.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.frontend.text.FragmentController;

import pt.ist.socialsoftware.edition.ldod.frontend.utils.AnnotationDTO;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.PermissionDTO;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.text.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.text.domain.Fragment;
import pt.ist.socialsoftware.edition.text.domain.ScholarInter;
import pt.ist.socialsoftware.edition.text.domain.TextModule;
import pt.ist.socialsoftware.edition.user.domain.User;
import pt.ist.socialsoftware.edition.user.utils.Emailer;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.virtual.domain.HumanAnnotation;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule;
import pt.ist.socialsoftware.edition.virtual.utils.RangeJson;

import java.io.FileNotFoundException;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class FragmentTest {
    public static final String ARS = "ars";

    @Mock
    private Emailer emailer;

    @InjectMocks
    FragmentController fragmentController;

    protected MockMvc mockMvc;

    @BeforeAll
    @Atomic(mode = TxMode.WRITE)
    public static void setUpAll() throws FileNotFoundException {
        TestLoadUtils.setUpDatabaseWithCorpus();

        String[] fragments = {"001.xml", "002.xml", "003.xml"};
        TestLoadUtils.loadFragments(fragments);

        TestLoadUtils.loadTestVirtualEdition();
    }

    @AfterAll
    @Atomic(mode = TxMode.WRITE)
    public static void tearDownAll() throws FileNotFoundException {
        TestLoadUtils.cleanDatabase();
    }

    @BeforeEach
    @Atomic(mode = TxMode.WRITE)
    public void setUp() throws FileNotFoundException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.fragmentController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
    }

    @AfterEach
    @Atomic(mode = TxMode.WRITE)
    public void tearDown() {
        Set<VirtualEditionInter> fragInterSet = VirtualModule.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

        List<VirtualEditionInter> frags = new ArrayList<>(fragInterSet);

        VirtualEditionInter fragInter = frags.get(0);

        List<HumanAnnotation> list = new ArrayList<>(fragInter.getAllDepthHumanAnnotationsAccessibleByUser(ARS));

        if (!list.isEmpty()) {
            HumanAnnotation a = new ArrayList<>(fragInter.getAllDepthHumanAnnotationsAccessibleByUser(ARS)).get(0);
            a.remove();
        }
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
    public void getFragInterFromIdScholar() throws Exception {
        this.mockMvc.perform(get("/fragments/fragment/{xmlId}/inter/{urlId}", "Fr001", "Fr001_WIT_MS_Fr001a_1"))
                .andDo(print()).andExpect(status().isOk()).andExpect(view().name("fragment/main"))
                .andExpect(model().attribute("fragment", notNullValue()))
                .andExpect(model().attribute("inters", notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getFragInterFromIdVirtual() throws Exception {
        VirtualEditionInter inter = VirtualModule.getInstance().getArchiveEdition().getAllDepthVirtualEditionInters().get(0);

        this.mockMvc.perform(get("/fragments/fragment/{xmlId}/inter/{urlId}", "Fr001", inter.getUrlId()))
                .andDo(print()).andExpect(status().isOk()).andExpect(view().name("fragment/main"))
                .andExpect(model().attribute("fragment", notNullValue()))
                .andExpect(model().attribute("inters", notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getNextFragmentWithScholarInter() throws Exception {
        ExpertEditionInter inter = TextModule.getInstance().getFragmentByXmlId("Fr001").getExpertEditionInterSet().stream().findAny().get();

        this.mockMvc.perform(get("/fragments/fragment/{xmlId}/inter/{urlId}/next", "Fr001", inter.getUrlId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/fragments/fragment/*/inter/*"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getNextFragmentWithVirtualInter() throws Exception {
        VirtualEditionInter inter = VirtualModule.getInstance().getArchiveEdition().getAllDepthVirtualEditionInters().get(0);

        this.mockMvc.perform(get("/fragments/fragment/{xmlId}/inter/{urlId}/next", "Fr001", inter.getUrlId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/fragments/fragment/*/inter/*"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getPrevFragmentWithScholarInter() throws Exception {
        ExpertEditionInter inter = TextModule.getInstance().getFragmentByXmlId("Fr001").getExpertEditionInterSet().stream().findAny().get();

        this.mockMvc.perform(get("/fragments/fragment/{xmlId}/inter/{urlId}/prev", "Fr001", inter.getUrlId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/fragments/fragment/*/inter/*"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getPrevFragmentWithVirtualInter() throws Exception {
        VirtualEditionInter inter = VirtualModule.getInstance().getArchiveEdition().getAllDepthVirtualEditionInters().get(0);

        this.mockMvc.perform(get("/fragments/fragment/{xmlId}/inter/{urlId}/prev", "Fr001", inter.getUrlId()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/fragments/fragment/*/inter/*"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getFragInterFromErrorId() throws Exception {
        this.mockMvc.perform(get("/fragments/fragment/{xmlId}/inter/{urlId}", "Fr001", "Error")).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getFragInterByScholarExternalId() throws Exception {
        ScholarInter fragInter = TextModule.getInstance().getFragmentByXmlId("Fr001").getScholarInterByUrlId("Fr001_WIT_MS_Fr001a_1");

        this.mockMvc.perform(get("/fragments/fragment/inter/{externalId}", fragInter.getExternalId())).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/fragments/fragment/"
                + fragInter.getFragment().getXmlId() + "/inter/" + fragInter.getUrlId()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getFragInterByVirtualExternalId() throws Exception {
        VirtualEditionInter inter = VirtualModule.getInstance().getArchiveEdition().getAllDepthVirtualEditionInters().get(0);

        this.mockMvc.perform(get("/fragments/fragment/inter/{externalId}", inter.getExternalId())).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/fragments/fragment/"
                + inter.getFragmentDto().getXmlId() + "/inter/" + inter.getUrlId()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getFragInterByExternalIdError() throws Exception {
        this.mockMvc.perform(get("/fragments/fragment/inter/{externalId}", "ERROR")).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(ARS)
    public void getFragInterTaxonomyTest() throws Exception {

        Set<VirtualEditionInter> fragInterSet = VirtualModule.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

        List<VirtualEditionInter> frags = new ArrayList<>(fragInterSet);

        VirtualEditionInter fragInter = frags.get(0);

        this.mockMvc.perform(get("/fragments/fragment/inter/{externalId}/taxonomy", fragInter.getExternalId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("fragment/taxonomy"))
             //   .andExpect(model().attribute("ldoD", notNullValue()))
                .andExpect(model().attribute("user", notNullValue()))
                .andExpect(model().attribute("inters", notNullValue()));

    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    @WithUserDetails(ARS)
    public void getScholarInterOneTest() throws Exception {
        Fragment frag = TextModule.getInstance().getFragmentByXmlId("Fr001");

        String[] inters = {
                frag.getRepresentativeSourceInter().getExternalId()
        };

        this.mockMvc.perform(get("/fragments/fragment/inter")
                .param("fragment", frag.getExternalId())
                .param("inters[]", inters))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("fragment/main"))
          //      .andExpect(model().attribute("ldoD", notNullValue()))
                .andExpect(model().attribute("fragment", notNullValue()))
                .andExpect(model().attribute("user", notNullValue()))
                .andExpect(model().attribute("inters", notNullValue()));
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    @WithUserDetails(ARS)
    public void getScholarInterThreeTest() throws Exception {
        Fragment frag = TextModule.getInstance().getFragmentByXmlId("Fr001");

        List<ScholarInter> scholarInters = new ArrayList<ScholarInter>(frag.getScholarInterSet());
        String[] inters = {
                scholarInters.get(0).getExternalId(),
                scholarInters.get(1).getExternalId(),
                scholarInters.get(2).getExternalId()
        };

        this.mockMvc.perform(get("/fragments/fragment/inter")
                .param("fragment", frag.getExternalId())
                .param("inters[]", inters))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("fragment/main"))
          //      .andExpect(model().attribute("ldoD", notNullValue()))
                .andExpect(model().attribute("fragment", notNullValue()))
                .andExpect(model().attribute("user", notNullValue()))
                .andExpect(model().attribute("inters", notNullValue()));
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    @WithUserDetails(ARS)
    public void getVirtualInterOneTest() throws Exception {
        Fragment frag = TextModule.getInstance().getFragmentByXmlId("Fr001");
        VirtualEditionInter inter = VirtualModule.getInstance().getArchiveEdition().getAllDepthVirtualEditionInters().get(0);

        String[] inters = {
                inter.getExternalId()
        };

        this.mockMvc.perform(get("/fragments/fragment/inter")
                .param("fragment", frag.getExternalId())
                .param("inters[]", inters))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("fragment/main"))
           //     .andExpect(model().attribute("ldoD", notNullValue()))
                .andExpect(model().attribute("fragment", notNullValue()))
                .andExpect(model().attribute("user", notNullValue()))
                .andExpect(model().attribute("inters", notNullValue()));
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void getInterEditorialTest() throws Exception {
        ScholarInter fragInter = TextModule.getInstance().getFragmentByXmlId("Fr001")
                .getScholarInterByUrlId("Fr001_WIT_MS_Fr001a_1");

        this.mockMvc.perform(get("/fragments/fragment/inter/editorial")
                .param("interp[]", fragInter.getExternalId())
                .param("diff", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("fragment/transcription"))
                .andExpect(model().attribute("inters", hasSize(1)))
                .andExpect(model().attribute("writer", notNullValue()));

    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void getInterAuthorialTest() throws Exception {
        ScholarInter fragInter = TextModule.getInstance().getFragmentByXmlId("Fr001")
                .getScholarInterByUrlId("Fr001_WIT_MS_Fr001a_1");

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
                .andExpect(model().attribute("inters", hasSize(1)))
                .andExpect(model().attribute("surface", notNullValue()))
                .andExpect(model().attribute("prevsurface", nullValue()))
                .andExpect(model().attribute("nextsurface", nullValue()))
                .andExpect(model().attribute("prevpb", nullValue()))
                .andExpect(model().attribute("nextpb", nullValue()))
                .andExpect(model().attribute("writer", notNullValue()));

    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void getInterAuthorialNoFacsTest() throws Exception {
        ScholarInter fragInter = TextModule.getInstance().getFragmentByXmlId("Fr001")
                .getScholarInterByUrlId("Fr001_WIT_MS_Fr001a_1");

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
                .andExpect(model().attribute("inters", hasSize(1)))
                .andExpect(model().attribute("surface", nullValue()))
                .andExpect(model().attribute("prevsurface", nullValue()))
                .andExpect(model().attribute("nextsurface", nullValue()))
                .andExpect(model().attribute("prevpb", nullValue()))
                .andExpect(model().attribute("nextpb", nullValue()))
                .andExpect(model().attribute("writer", notNullValue()));

    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void getScholarInterCompareTest() throws Exception {
        ScholarInter fragInterOne = TextModule.getInstance().getFragmentByXmlId("Fr001")
                .getScholarInterByUrlId("Fr001_WIT_ED_CRIT_SC");

        ScholarInter fragInterTwo = TextModule.getInstance().getFragmentByXmlId("Fr001")
                .getScholarInterByUrlId("Fr001_WIT_ED_CRIT_Z");

        String[] inters = {fragInterOne.getExternalId(), fragInterTwo.getExternalId()};

        this.mockMvc.perform(get("/fragments/fragment/inter/compare")
                .param("inters[]", inters)
                .param("spaces", "true")
                .param("line", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("fragment/inter2CompareSideBySide"))
                .andExpect(model().attribute("fragment", notNullValue()))
                .andExpect(model().attribute("lineByLine", is(false)))
                .andExpect(model().attribute("inters", hasSize(2)))
                .andExpect(model().attribute("writer", notNullValue()));

    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void searchAnnotationsTest() throws Exception {
        Set<VirtualEditionInter> fragInterSet = VirtualModule.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

        List<VirtualEditionInter> frags = new ArrayList<>(fragInterSet);

        VirtualEditionInter fragInter = frags.get(0);

        String response = this.mockMvc.perform(get("/fragments/fragment/search")
                .param("limit", "2")
                .param("uri", fragInter.getExternalId()))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> map = new HashMap<>();

        map = mapper.readValue(response, new TypeReference<Map<String, Object>>() {
        });

        assertEquals(4, map.get("total"));
    }


    @Test
    @Atomic(mode = TxMode.WRITE)
    @WithUserDetails(ARS)
    public void createAnnotationTest() throws Exception {
        Set<VirtualEditionInter> fragInterSet = VirtualModule.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

        List<VirtualEditionInter> frags = new ArrayList<>(fragInterSet);

        VirtualEditionInter fragInter = frags.get(0);

        VirtualModule ldod = VirtualModule.getInstance();

        // create permissionDTO
        VirtualEdition ve = ldod.getArchiveEdition();

        VirtualEdition otherVe = new VirtualEdition( ldod, "ars", "XPTO", "TITLE", LocalDate.now(), true, ve.getAcronym());

//        PermissionDTO permissionDTO = new PermissionDTO(ve, User.USER_ARS);
        PermissionDTO permissionDTO = new PermissionDTO(new VirtualEditionDto(ve), User.USER_ARS);


        // create annotationDTO
        AnnotationDTO annotationDTO = new AnnotationDTO();
        annotationDTO.setPermissions(permissionDTO);
        annotationDTO.setUri(fragInter.getExternalId());
        annotationDTO.setQuote("A arte é um esquivar-se a agir");
        annotationDTO.setText("Interesting");
        annotationDTO.setUser(ARS);

        RangeJson rj = new RangeJson();
        rj.setStart("/div[1]/div[1]/p[3]");
        rj.setStartOffset(3);
        rj.setEnd("/div[1]/div[1]/p[3]");
        rj.setEndOffset(7);

        List<RangeJson> list = new ArrayList<>();
        list.add(rj);

        annotationDTO.setRanges(list);
        annotationDTO.setTags(Arrays.asList("tag1", "tag2"));

        // send request to mock
        this.mockMvc.perform(post("/fragments/fragment/annotations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestLoadUtils.jsonBytes(annotationDTO)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    @WithUserDetails(ARS)
    public void getAnnotationTest() throws Exception {

        Set<VirtualEditionInter> fragInterSet = VirtualModule.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

        List<VirtualEditionInter> frags = new ArrayList<>(fragInterSet);

        VirtualEditionInter fragInter = frags.get(0);

        createTestAnnotation();

        HumanAnnotation a = new ArrayList<>(fragInter.getAllDepthHumanAnnotationsAccessibleByUser(ARS)).get(0);

        AnnotationDTO dto = new AnnotationDTO();

        dto.setText("Even more interesting");
        dto.setTags(Arrays.asList("tag3", "tag4"));

        this.mockMvc.perform(get("/fragments/fragment/annotations/{id}", a.getExternalId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    @WithUserDetails(ARS)
    public void getAnnotationNotFoundTest() throws Exception {

        this.mockMvc.perform(get("/fragments/fragment/annotations/{id}", "ERROR"))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    @WithUserDetails(ARS)
    public void updateAnnotationTest() throws Exception {
        Set<VirtualEditionInter> fragInterSet = VirtualModule.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

        List<VirtualEditionInter> frags = new ArrayList<>(fragInterSet);

        VirtualEditionInter fragInter = frags.get(0);

        createTestAnnotation();

        HumanAnnotation a = new ArrayList<>(fragInter.getAllDepthHumanAnnotationsAccessibleByUser(ARS)).get(0);

        AnnotationDTO dto = new AnnotationDTO();

        dto.setText("Even more interesting");
        dto.setTags(Arrays.asList("tag3", "tag4"));

        this.mockMvc.perform(put("/fragments/fragment/annotations/{id}", a.getExternalId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestLoadUtils.jsonBytes(dto)))
                .andDo(print())
                .andExpect(status().isOk());


        assertEquals("Even more interesting", a.getText());
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    @WithUserDetails(ARS)
    public void updateAnnotationNotFoundTest() throws Exception {

        AnnotationDTO dto = new AnnotationDTO();

        this.mockMvc.perform(put("/fragments/fragment/annotations/{id}", "ERROR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestLoadUtils.jsonBytes(dto)))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    @WithUserDetails(ARS)
    public void deleteAnnotationTest() throws Exception {

        Set<VirtualEditionInter> fragInterSet = VirtualModule.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

        List<VirtualEditionInter> frags = new ArrayList<>(fragInterSet);

        VirtualEditionInter fragInter = frags.get(0);

        fragInter.getAllDepthAnnotationsAccessibleByUser(ARS).stream().forEach(annotation -> annotation.remove());

        createTestAnnotation();

        HumanAnnotation a = new ArrayList<>(fragInter.getAllDepthHumanAnnotationsAccessibleByUser(ARS)).get(0);

        this.mockMvc.perform(delete("/fragments/fragment/annotations/{id}", a.getExternalId())
                .content(TestLoadUtils.jsonBytes(new AnnotationDTO()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertTrue(fragInter.getAllDepthHumanAnnotationsAccessibleByUser(ARS).isEmpty());
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    @WithUserDetails(ARS)

    public void deleteAnnotationNotFoundTest() throws Exception {

        this.mockMvc.perform(delete("/fragments/fragment/annotations/{id}", "ERROR")
                .content(TestLoadUtils.jsonBytes(new AnnotationDTO()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    @WithUserDetails(ARS)
    public void getAnnotationInterTest() throws Exception {

        Set<VirtualEditionInter> fragInterSet = VirtualModule.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

        List<VirtualEditionInter> frags = new ArrayList<>(fragInterSet);

        VirtualEditionInter fragInter = frags.get(0);

        HumanAnnotation humanAnnotation =createTestAnnotation();

        this.mockMvc.perform(get("/fragments/fragment/annotation/{annotationId}/categories", humanAnnotation.getExternalId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(containsString("tag1")))
                .andExpect(content().string(containsString("tag2")));
    }

    private HumanAnnotation createTestAnnotation() {
        Set<VirtualEditionInter> fragInterSet = VirtualModule.getInstance().getVirtualEdition("LdoD-Teste").getIntersSet();

        List<VirtualEditionInter> frags = new ArrayList<>(fragInterSet);

        VirtualEditionInter fragInter = frags.get(0);

        RangeJson rj = new RangeJson();
        rj.setStart("/div[1]/div[1]/p[3]");
        rj.setStartOffset(3);
        rj.setEnd("/div[1]/div[1]/p[3]");
        rj.setEndOffset(7);

        List<RangeJson> list = new ArrayList<>();
        list.add(rj);

        return fragInter.createHumanAnnotation("A arte é um esquivar-se a agir", "Interesting", User.USER_ARS,
                list, Arrays.asList("tag1", "tag2"));

    }
}
