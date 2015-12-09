package pt.ist.socialsoftware.edition.search.options;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.utils.Bootstrap;

@Ignore
public class TaxonomySearchOptionTest {
	private SearchOption option;

	@Before
	public void setUp() throws Exception {

		Bootstrap.initDatabase();
		try {
			FenixFramework.getTransactionManager().begin(false);
		} catch (WriteOnReadError | NotSupportedException | SystemException e1) {
			e1.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		try {
			FenixFramework.getTransactionManager().rollback();
		} catch (IllegalStateException | SecurityException | SystemException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAdDocTaxonomyVisitor() {
		option = new TaxonomySearchOption("andre");

		LdoD ldod = LdoD.getInstance();

		for (Fragment frag : ldod.getFragmentsSet()) {
			for (FragInter inter : frag.getFragmentInterSet()) {
				inter.accept(option);
			}
		}
	}

	@Test
	public void testGeneratedTaxonomyVisitor() {
		option = new TaxonomySearchOption("fatal");

		LdoD ldod = LdoD.getInstance();

		for (Fragment frag : ldod.getFragmentsSet()) {
			for (FragInter inter : frag.getFragmentInterSet()) {
				inter.accept(option);
			}
		}
	}

	@Test
	public void testMergedTaxonomyVisitor() {
		option = new TaxonomySearchOption("inexpressa");

		LdoD ldod = LdoD.getInstance();

		for (Fragment frag : ldod.getFragmentsSet()) {
			for (FragInter inter : frag.getFragmentInterSet()) {
				inter.accept(option);
			}
		}
	}

}
