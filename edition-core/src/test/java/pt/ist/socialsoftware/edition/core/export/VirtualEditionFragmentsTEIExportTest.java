package pt.ist.socialsoftware.edition.core.export;

import static org.junit.Assert.assertEquals;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.core.domain.Fragment;
import pt.ist.socialsoftware.edition.core.domain.LdoD;
import pt.ist.socialsoftware.edition.core.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.core.loaders.VirtualEditionFragmentsTEIImport;

public class VirtualEditionFragmentsTEIExportTest {
	@BeforeEach
	public void setUp() throws WriteOnReadError, NotSupportedException, SystemException {
		FenixFramework.getTransactionManager().begin(false);
	}

	@Test
	@Atomic
	public void test() throws WriteOnReadError, NotSupportedException, SystemException {
		VirtualEditionFragmentsTEIExport export = new VirtualEditionFragmentsTEIExport();

		for (Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
			String fragmentTEI = export.exportFragment(fragment);

			int numberOfInters = fragment.getVirtualEditionInters().size();

			for (VirtualEditionInter inter : fragment.getVirtualEditionInters()) {
				inter.remove();
			}

			VirtualEditionFragmentsTEIImport im = new VirtualEditionFragmentsTEIImport();
			im.importFragmentFromTEI(fragmentTEI);

			System.out.println(export.exportFragment(fragment));

			assertEquals(numberOfInters, fragment.getVirtualEditionInters().size());

			// assertEquals(Arrays.stream(fragmentTEI.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
			// Arrays.stream(export.exportFragment(fragment).split("\\r?\\n")).sorted()
			// .collect(Collectors.joining("\\n")));
		}
	}

	@AfterEach
	public void tearDown() throws IllegalStateException, SecurityException, SystemException {
		FenixFramework.getTransactionManager().rollback();
	}
}
