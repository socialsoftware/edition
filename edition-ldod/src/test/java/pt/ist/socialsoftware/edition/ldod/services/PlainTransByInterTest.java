package pt.ist.socialsoftware.edition.ldod.services;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.TestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;

public class PlainTransByInterTest extends TestWithFragmentsLoading {

	@Override
	protected String[] fragmentsToLoad4Test() {
		String[] fragments = { "001.xml" };

		return fragments;
	}

	@Override
	protected void populate4Test() {
	}

	@Override
	protected void unpopulate4Test() {
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
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
