package pt.ist.socialsoftware.edition.export;

import static org.junit.Assert.assertTrue;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.loaders.UsersXMLImport;

public class ExportUsersTest {
	@Before
	public void setUp() throws WriteOnReadError, NotSupportedException, SystemException {
		FenixFramework.getTransactionManager().begin(false);
	}

	@Test
	@Atomic
	public void test() throws WriteOnReadError, NotSupportedException, SystemException {
		UsersXMLExport export = new UsersXMLExport();
		String usersXML = export.export();

		LdoD.getInstance().getUsersSet().stream().forEach(u -> u.remove());

		assertTrue(LdoD.getInstance().getUsersSet().size() == 0);

		UsersXMLImport load = new UsersXMLImport();
		load.importUsers(usersXML);

		assertTrue(LdoD.getInstance().getUsersSet().size() != 0);
	}

	@After
	public void tearDown() throws IllegalStateException, SecurityException, SystemException {
		FenixFramework.getTransactionManager().rollback();
	}

}
