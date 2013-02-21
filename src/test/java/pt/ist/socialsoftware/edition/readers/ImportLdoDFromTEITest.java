package pt.ist.socialsoftware.edition.readers;

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
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.Taxonomy;

public class ImportLdoDFromTEITest {

	@Before
	public void setUp() {
		DatabaseBootstrap.initDatabase();

		ImportLdoDFromTEI importLdoD = new ImportLdoDFromTEI();
		importLdoD.loadLdoDTEI();
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

		Transaction.commit();

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
			if (edition.getEditor().equals("Jacinto Prado Coelho")) {
				assertEquals("O Livro do Desassossego", edition.getTitle());
				assertEquals(1982, edition.getDate().getYear());
			}
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
