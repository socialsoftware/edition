package pt.ist.socialsoftware.edition.services;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.loaders.LoadTEIFragments;
import pt.ist.socialsoftware.edition.utils.Bootstrap;

public class PlainTransByInterTest {

	@Before
	public void setUp() {
		Bootstrap.initDatabase();

		try {
			FenixFramework.getTransactionManager().begin(false);
		} catch (WriteOnReadError | NotSupportedException | SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// LoadTEICorpus corpusLoader = new LoadTEICorpus();
		// try {
		// corpusLoader.loadTEICorpus(new FileInputStream(
		// "/Users/ars/Desktop/Frg.1_TEI-encoded_testing.xml"));
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// /Users/ars/Desktop/Frg.1_TEI-encoded_testing.xml

		LoadTEIFragments fragmentsLoader = new LoadTEIFragments();
		try {
			fragmentsLoader.loadFragmentsAtOnce(new FileInputStream(
					"/Users/ars/Desktop/Frg.1_TEI-encoded_testing.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		try {
			FenixFramework.getTransactionManager().rollback();
		} catch (IllegalStateException | SecurityException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		assertEquals(" <p align", service.getTranscription().substring(0, 9));
	}
}
