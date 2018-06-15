package pt.ist.socialsoftware.edition.ldod.services;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.loaders.LoadTEIFragments;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

public class PlainTransByInterTest {
	private final String TESTS_DIR = PropertiesManager.getProperties().getProperty("tests.dir");

	@BeforeEach
	public void setUp() {

		try {
			FenixFramework.getTransactionManager().begin(false);
		} catch (WriteOnReadError | NotSupportedException | SystemException e1) {
			throw new LdoDException();
		}

		LoadTEIFragments fragmentsLoader = new LoadTEIFragments();
		try {
			fragmentsLoader.loadFragmentsAtOnce(new FileInputStream(this.TESTS_DIR + "Frg.1_TEI-encoded_testing.xml"));
		} catch (FileNotFoundException e) {
			throw new LdoDException();
		}
	}

	@AfterEach
	public void tearDown() {
		try {
			FenixFramework.getTransactionManager().rollback();
		} catch (IllegalStateException | SecurityException | SystemException e) {
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

		assertEquals(" <p class", service.getTranscription().substring(0, 9));
	}
}
