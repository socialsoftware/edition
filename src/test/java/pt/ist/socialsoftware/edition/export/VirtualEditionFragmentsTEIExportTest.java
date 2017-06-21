package pt.ist.socialsoftware.edition.export;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;

public class VirtualEditionFragmentsTEIExportTest {
	@Before
	public void setUp() throws WriteOnReadError, NotSupportedException, SystemException {
		FenixFramework.getTransactionManager().begin(false);
	}

	@Test
	@Atomic
	public void test() throws WriteOnReadError, NotSupportedException, SystemException {
		VirtualEditionFragmentsTEIExport export = new VirtualEditionFragmentsTEIExport();

		for (Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
			String fragmentTEI = export.exportFragment(fragment);
			System.out.println(fragmentTEI);

			// int numOfFragments = LdoD.getInstance().getFragmentsSet().size();
			//
			// LdoD.getInstance().getFragmentsSet().forEach(f -> f.remove());
			//
			// VirtualEditionFragmentsTEIImport im = new
			// VirtualEditionFragmentsTEIImport();
			// im.importFragement(fragmentTEI);
			//
			// assertEquals(numOfFragments,
			// LdoD.getInstance().getFragmentsSet().size());
			//
			// System.out.println(export.export());
			// assertEquals(Arrays.stream(fragmentTEI.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
			// Arrays.stream(export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
		}
	}

	@After
	public void tearDown() throws IllegalStateException, SecurityException, SystemException {
		FenixFramework.getTransactionManager().rollback();
	}
}
