package pt.ist.socialsoftware.edition.export;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.stream.Collectors;

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
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.loaders.VirtualEditionFragmentsTEIImport;

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

			int numberOfInters = fragment.getVirtualEditionInters().size();

			for (VirtualEditionInter inter : fragment.getVirtualEditionInters()) {
				inter.remove();
			}

			VirtualEditionFragmentsTEIImport im = new VirtualEditionFragmentsTEIImport();
			im.importFragmentFromTEI(fragmentTEI);

			System.out.println(export.exportFragment(fragment));

			assertEquals(numberOfInters, fragment.getVirtualEditionInters().size());

			assertEquals(Arrays.stream(fragmentTEI.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
					Arrays.stream(export.exportFragment(fragment).split("\\r?\\n")).sorted()
							.collect(Collectors.joining("\\n")));
		}
	}

	@After
	public void tearDown() throws IllegalStateException, SecurityException, SystemException {
		FenixFramework.getTransactionManager().rollback();
	}
}
