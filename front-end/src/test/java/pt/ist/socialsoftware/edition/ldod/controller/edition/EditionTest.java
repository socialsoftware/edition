package pt.ist.socialsoftware.edition.ldod.controller.edition;

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
import pt.ist.socialsoftware.edition.ldod.frontend.config.Application;
import pt.ist.socialsoftware.edition.ldod.frontend.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.frontend.text.EditionController;
import pt.ist.socialsoftware.edition.ldod.frontend.text.FeTextRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.FeVirtualRequiresInterface;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.CategoryDto;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class EditionTest {

	public static final String ARCHIVE_EDITION_ACRONYM = "LdoD-Arquivo";
	public static final String PIZARRO_EDITION_ACRONYM = "JP";

	private final FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();
	private final FeVirtualRequiresInterface feVirtualRequiresInterface = new FeVirtualRequiresInterface();

    @InjectMocks
    private EditionController editionController;

	protected MockMvc mockMvc;

	@BeforeAll
	@Atomic(mode = TxMode.WRITE)
	public static void setUpAll() throws IOException {
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
		TestLoadUtils.cleanDatabase();
	}

	@BeforeEach
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.editionController)
				.setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
	}


	@Test
	public void getEditionPizzarroTest() throws Exception {
		this.mockMvc.perform(get("/edition/acronym/{acronym}", PIZARRO_EDITION_ACRONYM)).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("edition/tableOfContents"))
				.andExpect(model().attribute("heteronym", nullValue())).andExpect(model().attribute("editionDto",
				hasProperty("acronym", equalTo(PIZARRO_EDITION_ACRONYM))));

	}

	@Test
	public void getEditionArchiveTest() throws Exception {
		this.mockMvc.perform(get("/edition/acronym/{acronym}", ARCHIVE_EDITION_ACRONYM)).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("edition/tableOfContents"))
				.andExpect(model().attribute("heteronym", nullValue())).andExpect(model().attribute("editionDto",
				hasProperty("acronym", equalTo(ARCHIVE_EDITION_ACRONYM))));

	}

	@Test
    public void errorEdition() throws Exception {
        this.mockMvc.perform(get("/edition/acronym/{acronym}", "ERROR")).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = TxMode.READ)
    public void getEditionByExternalId() throws Exception {
//        String id = TextModule.getInstance().getJPEdition().getExternalId();
		String id = feTextRequiresInterface.getExpertEditionByAcronym(PIZARRO_EDITION_ACRONYM).getExternalId();

        this.mockMvc.perform(get("/edition/internalid/{externalId}", id)).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/edition/acronym/" + PIZARRO_EDITION_ACRONYM));
    }

    @Test
    @Atomic(mode = TxMode.READ)
    public void getEditionWithHeteronym() throws Exception {
//        String editionId = TextModule.getInstance().getJPEdition().getExternalId();
//        String hetronymId = TextModule.getInstance().getSortedHeteronyms().get(0).getExternalId();

		String editionId = feTextRequiresInterface.getExpertEditionByAcronym(PIZARRO_EDITION_ACRONYM).getExternalId();
		String hetronymId = feTextRequiresInterface.getSortedHeteronyms().get(0).getExternalId();

        this.mockMvc.perform(get("/edition/internalid/heteronym/{id1}/{id2}", editionId, hetronymId)).andDo(print())
                .andExpect(status().isOk()).andExpect(view().name("edition/tableOfContents"))
                .andExpect(model().attribute("heteronym", notNullValue()))
                .andExpect(model().attribute("edition", notNullValue())).andExpect(model().attribute("editionDto",
                hasProperty("acronym", equalTo(PIZARRO_EDITION_ACRONYM))));
    }

    @Test
    @Atomic(mode = TxMode.READ)
    public void getEditionWithErrorAcronymId() throws Exception {
//        String hetronymId = TextModule.getInstance().getSortedHeteronyms().get(0).getExternalId();
		String hetronymId = feTextRequiresInterface.getSortedHeteronyms().get(0).getExternalId();


		this.mockMvc.perform(get("/edition/internalid/heteronym/{id1}/{id2}", "ERROR", hetronymId)).andDo(print())
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/error"));
    }

    @Test
    @Atomic(mode = TxMode.READ)
    public void getEditionWithErrorHeteronym() throws Exception {
//        String editionId = TextModule.getInstance().getJPEdition().getExternalId();
		String editionId = feTextRequiresInterface.getExpertEditionByAcronym(PIZARRO_EDITION_ACRONYM).getExternalId();

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
		this.mockMvc.perform(get("/edition/acronym/{acronym}/taxonomy", ARCHIVE_EDITION_ACRONYM))
				.andDo(print())
				.andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("edition/taxonomyTableOfContents"));
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void getCategoryTableOfContentsTest() throws Exception {
		CategoryDto category = feVirtualRequiresInterface.getVirtualEditionByAcronym(ARCHIVE_EDITION_ACRONYM)
				.getTaxonomy().getCategoriesSet().stream()
				.findAny()
				.orElse(null);

		this.mockMvc.perform(get("/edition/acronym/{acronym}/category/{urlId}", ARCHIVE_EDITION_ACRONYM, category.getUrlId()))
				.andDo(print())
				.andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("edition/categoryTableOfContents"));
	}

}
