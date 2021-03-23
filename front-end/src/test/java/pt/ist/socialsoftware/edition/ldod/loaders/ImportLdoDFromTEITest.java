package pt.ist.socialsoftware.edition.ldod.loaders;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.text.domain.*;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ImportLdoDFromTEITest {
    private Fragment fragmentTest;

    @BeforeEach
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void setUp() throws FileNotFoundException {
        TestLoadUtils.setUpDatabaseWithCorpus();

        String[] fragments = {"001.xml"};
        TestLoadUtils.loadFragments(fragments);

        this.fragmentTest = TextModule.getInstance().getFragmentsSet().stream().findFirst().get();
    }

    @AfterEach
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void tearDown() {
        TestLoadUtils.cleanDatabase();
    }

    @Test
    @Atomic(mode = TxMode.READ)
    public void testCorpusIdLoadead() {

        VirtualModule virtualModule = VirtualModule.getInstance();
        TextModule text = TextModule.getInstance();

        checkTitleStmtLoad(virtualModule);
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
        assertEquals(5, this.fragmentTest.getScholarInterSet().size());
        for (ScholarInter fragmentInter : this.fragmentTest.getScholarInterSet()) {
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

    private void checkHeteronymsLoad(TextModule text) {
        // includes the NullHeteronym instance
        assertEquals(3, text.getHeteronymsSet().size());
        for (Heteronym heteronym : text.getHeteronymsSet()) {
            assertTrue(heteronym.getName().equals("Bernardo Soares") || heteronym.getName().equals("Vicente Guedes")
                    || heteronym.getName().equals("não atribuído"));
        }
    }

    private void checkListBiblLoad(TextModule text) {
        assertEquals(4, text.getExpertEditionsSet().size());
        for (ExpertEdition edition : text.getExpertEditionsSet()) {
            assertEquals("Fernando Pessoa", edition.getAuthor());
            assertEquals("O Livro do Desassossego", edition.getTitle());
            assertTrue(edition.getEditor().equals("Jacinto do Prado Coelho")
                    || edition.getEditor().equals("Teresa Sobral Cunha") || edition.getEditor().equals("Richard Zenith")
                    || edition.getEditor().equals("Jerónimo Pizarro"));
        }
    }

    private void checkTitleStmtLoad(VirtualModule virtualModule) {
        assertEquals("O Livro do Desassossego", virtualModule.getTitle());
        assertEquals("Fernando Pessoa", virtualModule.getAuthor());
        assertEquals("Project: Nenhum Problema tem Solução", virtualModule.getEditor());
        assertEquals("", virtualModule.getSponsor());
        assertEquals("FCT", virtualModule.getFunder());
        assertEquals("Manuel Portela", virtualModule.getPrincipal());
    }

}
