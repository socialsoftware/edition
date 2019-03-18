package pt.ist.socialsoftware.edition.ldod.loaders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.TestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.domain.*;

public class ImportLdoDFromTEITest extends TestWithFragmentsLoading {
	private Fragment fragmentTest;

	@Override
	protected String[] fragmentsToLoad4Test() {
		String[] fragments = { "001.xml" };

		return fragments;
	}

	@Override
	protected void populate4Test() {
		this.fragmentTest = Text.getInstance().getFragmentsSet().stream().findFirst().get();
	}

	@Override
	protected void unpopulate4Test() {
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void testCorpusIdLoadead() {

		LdoD ldoD = LdoD.getInstance();
		Text text = Text.getInstance();

		checkTitleStmtLoad(ldoD);
		checkListBiblLoad(text);
		checkHeteronymsLoad(text);
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void testFragmentIsLoaded() {
		assertEquals("A arte é um esquivar-se a agir", this.fragmentTest.getTitle());
	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void testLoadWitnesses() {
		assertEquals(6, this.fragmentTest.getFragmentInterSet().size());
		for (FragInter fragmentInter : this.fragmentTest.getFragmentInterSet()) {
			if (fragmentInter instanceof ExpertEditionInter) {
				assertTrue(((ExpertEditionInter) fragmentInter).getExpertEdition() != null);
			} else if (fragmentInter instanceof SourceInter) {
				assertTrue(((SourceInter) fragmentInter).getSource() != null);
			}
		}

	}

	@Test
	@Atomic(mode = TxMode.READ)
	public void testLoadSources() {
		assertEquals(1, this.fragmentTest.getSourcesSet().size());
		for (Source source : this.fragmentTest.getSourcesSet()) {
			if (source instanceof ManuscriptSource) {
				ManuscriptSource manuscript = (ManuscriptSource) source;
				assertEquals("Lisbon", manuscript.getSettlement());
				assertEquals("BN", manuscript.getRepository());
				assertTrue(manuscript.getIdno().equals("bn-acpc-e-e3-1-1-89_0001_1_t24-C-R0150"));
			} else if (source instanceof PrintedSource) {
				PrintedSource printedSource = (PrintedSource) source;
				assertEquals("Revista Descobrimento", printedSource.getJournal());
				assertEquals("Lisbon", printedSource.getPubPlace());
				assertEquals("3", printedSource.getIssue());
				assertEquals("1931", Integer.toString(printedSource.getLdoDDate().getDate().getYear()));
			}

		}
	}

	private void checkHeteronymsLoad(Text text) {
		// includes the NullHeteronym instance
		assertEquals(3, text.getHeteronymsSet().size());
		for (Heteronym heteronym : text.getHeteronymsSet()) {
			assertTrue(heteronym.getName().equals("Bernardo Soares") || heteronym.getName().equals("Vicente Guedes")
					|| heteronym.getName().equals("não atribuído"));
		}
	}

	private void checkListBiblLoad(Text text) {
		assertEquals(4, text.getExpertEditionsSet().size());
		for (ExpertEdition edition : text.getExpertEditionsSet()) {
			assertEquals("Fernando Pessoa", edition.getAuthor());
			assertEquals("O Livro do Desassossego", edition.getTitle());
			assertTrue(edition.getEditor().equals("Jacinto do Prado Coelho")
					|| edition.getEditor().equals("Teresa Sobral Cunha") || edition.getEditor().equals("Richard Zenith")
					|| edition.getEditor().equals("Jerónimo Pizarro"));
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
