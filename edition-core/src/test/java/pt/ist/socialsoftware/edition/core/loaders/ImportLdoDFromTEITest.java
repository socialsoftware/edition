package pt.ist.socialsoftware.edition.core.loaders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.core.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.core.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.core.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.core.domain.FragInter;
import pt.ist.socialsoftware.edition.core.domain.Fragment;
import pt.ist.socialsoftware.edition.core.domain.Heteronym;
import pt.ist.socialsoftware.edition.core.domain.LdoD;
import pt.ist.socialsoftware.edition.core.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.core.domain.PrintedSource;
import pt.ist.socialsoftware.edition.core.domain.Source;
import pt.ist.socialsoftware.edition.core.domain.SourceInter;
import pt.ist.socialsoftware.edition.core.utils.PropertiesManager;

public class ImportLdoDFromTEITest {
	private static final String TESTS_DIR = PropertiesManager.getProperties().getProperty("tests.dir");

	private static Fragment fragmentTest;

	@BeforeClass
	public static void setUp() throws WriteOnReadError, NotSupportedException, SystemException {
		FenixFramework.getTransactionManager().begin(false);

		LoadTEIFragments fragmentsLoader = new LoadTEIFragments();
		try {
			fragmentsLoader.loadFragmentsAtOnce(new FileInputStream(TESTS_DIR + "Frg.1_TEI-encoded_testing.xml"));
		} catch (FileNotFoundException e) {
			throw new LdoDException();
		}

		LdoD ldoD = LdoD.getInstance();
		fragmentTest = ldoD.getFragmentsSet().stream().filter(f -> f.getXmlId().equals("Fr1TEST")).findFirst().get();
	}

	@AfterClass
	public static void tearDown() throws IllegalStateException, SecurityException, SystemException {
		FenixFramework.getTransactionManager().rollback();
	}

	@Test
	public void testCorpusIdLoadead() {

		LdoD ldoD = LdoD.getInstance();

		checkTitleStmtLoad(ldoD);
		checkListBiblLoad(ldoD);
		checkHeteronymsLoad(ldoD);

	}

	@Test
	public void testFragmentIsLoaded() {
		assertEquals("Prefiro a prosa ao verso...", fragmentTest.getTitle());
	}

	@Test
	public void testLoadWitnesses() {
		assertEquals(7, fragmentTest.getFragmentInterSet().size());
		for (FragInter fragmentInter : fragmentTest.getFragmentInterSet()) {
			if (fragmentInter instanceof ExpertEditionInter) {
				assertTrue(((ExpertEditionInter) fragmentInter).getExpertEdition() != null);
			} else if (fragmentInter instanceof SourceInter) {
				assertTrue(((SourceInter) fragmentInter).getSource() != null);
			}
		}

	}

	@Test
	public void testLoadSources() {
		assertEquals(3, fragmentTest.getSourcesSet().size());
		for (Source source : fragmentTest.getSourcesSet()) {
			if (source instanceof ManuscriptSource) {
				ManuscriptSource manuscript = (ManuscriptSource) source;
				assertEquals("Lisbon", manuscript.getSettlement());
				assertEquals("BN", manuscript.getRepository());
				assertTrue(manuscript.getIdno().equals("bn-acpc-e-e3-4-1-87_0007_4_t24-C-R0150")
						|| manuscript.getIdno().equals("bn-acpc-e-e3-4-1-87_0005_3_t24-C-R0150"));
			} else if (source instanceof PrintedSource) {
				PrintedSource printedSource = (PrintedSource) source;
				assertEquals("Revista Descobrimento", printedSource.getJournal());
				assertEquals("Lisbon", printedSource.getPubPlace());
				assertEquals("3", printedSource.getIssue());
				assertEquals("1931", Integer.toString(printedSource.getLdoDDate().getDate().getYear()));
			}

		}
	}

	private void checkHeteronymsLoad(LdoD ldoD) {
		// includes the NullHeteronym instance
		assertEquals(3, ldoD.getHeteronymsSet().size());
		for (Heteronym heteronym : ldoD.getHeteronymsSet()) {
			assertTrue(heteronym.getName().equals("Bernardo Soares") || heteronym.getName().equals("Vicente Guedes")
					|| heteronym.getName().equals("não atribuído"));
		}
	}

	private void checkListBiblLoad(LdoD ldoD) {
		assertEquals(4, ldoD.getExpertEditionsSet().size());
		for (ExpertEdition edition : ldoD.getExpertEditionsSet()) {
			assertEquals("Fernando Pessoa", edition.getAuthor());
			assertEquals("O Livro do Desassossego", edition.getTitle());
			assertTrue((edition.getEditor().equals("Jacinto do Prado Coelho"))
					|| (edition.getEditor().equals("Teresa Sobral Cunha"))
					|| (edition.getEditor().equals("Richard Zenith"))
					|| (edition.getEditor().equals("Jerónimo Pizarro")));
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
