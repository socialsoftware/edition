package pt.ist.socialsoftware.edition.loaders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.pstm.Transaction;
import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.DatabaseBootstrap;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.EditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.Heteronym;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.domain.PrintedSource;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.services.LoadLdoDFromTEIService;

public class ImportLdoDFromTEITest {

	@Before
	public void setUp() {
		DatabaseBootstrap.initDatabase();

		LoadLdoDFromTEIService service = new LoadLdoDFromTEIService();
		service.atomicExecution();
	}

	@After
	public void tearDown() {
		boolean committed = false;
		try {
			Transaction.begin();
			LdoD ldoD = LdoD.getInstance();
			Set<Edition> editions = ldoD.getEditionsSet();
			editions.clear();
			Set<Taxonomy> taxonomies = ldoD.getTaxonomiesSet();
			taxonomies.clear();
			Set<Heteronym> heteronyms = ldoD.getHeteronymsSet();
			heteronyms.clear();
			Set<Fragment> fragments = ldoD.getFragmentsSet();
			fragments.clear();
			Transaction.commit();
			committed = true;
		} finally {
			if (!committed) {
				Transaction.abort();
				fail("failed @TearDown.");
			}
		}

	}

	@Test
	public void TestSucessfulLoading() {
		Transaction.begin();

		LdoD ldoD = LdoD.getInstance();

		checkTitleStmtLoad(ldoD);
		checkListBiblLoad(ldoD);
		checkTaxonomiesLoad(ldoD);
		checkHeteronymsLoad(ldoD);

		Fragment fragment = checkFragmentLoad(ldoD);
		checkLoadSources(fragment);
		checkLoadWitnesses(fragment);

		Transaction.commit();

	}

	private void checkLoadWitnesses(Fragment fragment) {
		assertEquals(7, fragment.getFragmentInter().size());
		for (FragInter fragmentInter : fragment.getFragmentInter()) {
			if (fragmentInter instanceof EditionInter) {
				assertTrue(((EditionInter) fragmentInter).hasEdition());
			} else if (fragmentInter instanceof SourceInter) {
				assertTrue(((SourceInter) fragmentInter).hasSource());
			}
		}

	}

	private void checkLoadSources(Fragment fragment) {
		assertEquals(3, fragment.getSources().size());
		for (Source source : fragment.getSources()) {
			if (source instanceof ManuscriptSource) {
				ManuscriptSource manuscript = (ManuscriptSource) source;
				assertEquals("Lisbon", manuscript.getSettlement());
				assertEquals("BN", manuscript.getRepository());
				assertTrue(manuscript.getIdno().equals(
						"bn-acpc-e-e3-4-1-87_0007_4_t24-C-R0150")
						|| manuscript.getIdno().equals(
								"bn-acpc-e-e3-4-1-87_0005_3_t24-C-R0150"));
			} else if (source instanceof PrintedSource) {
				PrintedSource printedSource = (PrintedSource) source;
				assertEquals("Revista Descobrimento", printedSource.getTitle());
				assertEquals("Lisbon", printedSource.getPubPlace());
				assertEquals("3", printedSource.getIssue());
				assertEquals(1931, printedSource.getDate().getYear());
			}

		}
	}

	private Fragment checkFragmentLoad(LdoD ldoD) {
		assertEquals(1, ldoD.getFragments().size());

		Fragment returnFragment = null;
		for (Fragment fragment : ldoD.getFragments()) {
			assertEquals("Prefiro a prosa ao verso...", fragment.getTitle());

			returnFragment = fragment;
		}

		return returnFragment;

	}

	private void checkHeteronymsLoad(LdoD ldoD) {
		assertEquals(2, ldoD.getHeteronyms().size());
		for (Heteronym heteronym : ldoD.getHeteronyms()) {
			assertTrue(heteronym.getName().equals("Bernardo Soares")
					|| heteronym.getName().equals("Vicente Guedes"));
		}
	}

	private void checkTaxonomiesLoad(LdoD ldoD) {
		assertEquals(1, ldoD.getTaxonomies().size());
		Taxonomy taxonomy = ldoD.getTaxonomies().get(0);
		assertEquals("António Quadros", taxonomy.getName());

		assertEquals(2, taxonomy.getCategories().size());
		for (Category category : taxonomy.getCategories()) {
			assertTrue(category.getName().equals("Fase Confessional")
					|| category.getName().equals("Fase Decadentista"));
		}
	}

	private void checkListBiblLoad(LdoD ldoD) {
		assertEquals(4, ldoD.getEditions().size());
		for (Edition edition : ldoD.getEditions()) {
			assertEquals("Fernando Pessoa", edition.getAuthor());
			assertEquals("O Livro do Desassossego", edition.getTitle());
			assertTrue((edition.getEditor().equals("Jacinto Prado Coelho") && edition
					.getDate().getYear() == 1982)
					|| (edition.getEditor().equals("Teresa Sobral Cunha") && edition
							.getDate().getYear() == 1997)
					|| (edition.getEditor().equals("Richard Zenith") && edition
							.getDate().getYear() == 2007)
					|| (edition.getEditor().equals("Jerónimo Pizarro") && edition
							.getDate().getYear() == 2010));
		}
	}

	private void checkTitleStmtLoad(LdoD ldoD) {
		assertEquals("O Livro do Desassossego", ldoD.getTitle());
		assertEquals("Fernando Pessoa", ldoD.getAuthor());
		assertEquals("Project: Nenhum Problema tem Solução", ldoD.getEditor());
		assertEquals("", ldoD.getSponsor());
		assertEquals("FCT", ldoD.getFunder());
		assertEquals("Manuel Portela", ldoD.getPrincipal());
	}

}
