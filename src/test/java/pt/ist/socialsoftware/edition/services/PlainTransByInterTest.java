package pt.ist.socialsoftware.edition.services;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import jvstm.Transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.loaders.LoadTEICorpus;
import pt.ist.socialsoftware.edition.loaders.LoadTEIFragments;
import pt.ist.socialsoftware.edition.utils.Bootstrap;

public class PlainTransByInterTest {

	@Before
	public void setUp() {
		Bootstrap.initDatabase();

		Transaction.begin();

		LoadTEICorpus corpusLoader = new LoadTEICorpus();
		try {
			corpusLoader.loadTEICorpus(new FileInputStream(
					"/Users/ars/Desktop/Frg.1_TEI-encoded_testing.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// /Users/ars/Desktop/Frg.1_TEI-encoded_testing.xml

		LoadTEIFragments fragmentsLoader = new LoadTEIFragments();
		try {
			fragmentsLoader.loadFragments(new FileInputStream(
					"/Users/ars/Desktop/Frg.1_TEI-encoded_testing.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		Transaction.abort();
	}

	@Test
	public void test() {
		String fragInterExternalID = null;

		LdoD ldoD = LdoD.getInstance();

		for (Fragment frag : ldoD.getFragmentsSet()) {
			for (FragInter fragInter : frag.getFragmentInterSet()) {
				if (fragInter.getShortName().equals("Cunha")) {
					fragInterExternalID = fragInter.getExternalId();
				}
			}
		}

		PlainTransByInter service = new PlainTransByInter();
		service.setFragInterExternalID(fragInterExternalID);
		service.execution();

		System.out.println(service.getTranscription());

		assertEquals(
				"Teresa Sobral Cunha: 18-10-1931 Prefiro a prosa ao verso, como modo de arte, por duas razões,",
				service.getTranscription().substring(0, 93));

		assertEquals(
				"Teresa Sobral Cunha: 18-10-1931 Prefiro a prosa ao verso, como modo de arte, por duas razões, das quais a primeira, que é minha, é que não tenho também pode ser minha escolha, pois incapaz de escrever em verso. Mas a segunda, porém, é de todos e não é - creio bem – uma sombra ou dis farce da primeira. Vale pois a pena que eu a esfie, porque toca no sentido íntimo de toda a valia da arte.[SPACE] Considero o verso como uma coisa intermédia, uma pas sa gem da música para a prosa. Como a música, o verso é limi tado por leis rítmicas, que ainda que não sejam as leis rígidas do verso regular, existem todavia como resguardos, coacções, dispositivos automáticos de opressão e castigo. Na prosa falamos livres. Podemos incluir ritmos musicais, e contudo pensar. Podemos incluir ritmos poéticos, e contudo estar fora deles. Um ritmo ocasional de verso não estorva a prosa; um ritmo ocasional de prosa faz tropeçar o verso.[SPACE]",
				service.getTranscription().trim());
	}
}
