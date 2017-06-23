package pt.ist.socialsoftware.edition.export;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.RegistrationToken;
import pt.ist.socialsoftware.edition.loaders.UsersXMLImport;

public class UsersXMLExportTest {
	@Before
	public void setUp() throws WriteOnReadError, NotSupportedException, SystemException {
		FenixFramework.getTransactionManager().begin(false);
	}

	@Test
	@Atomic
	public void test() throws WriteOnReadError, NotSupportedException, SystemException {
		UsersXMLExport export = new UsersXMLExport();
		String usersXML = export.export();

		int numOfUsers = LdoD.getInstance().getUsersSet().size();
		int numOfUserConnections = LdoD.getInstance().getUserConnectionSet().size();
		int numOfRegistrationTokens = LdoD.getInstance().getTokenSet().size();

		LdoD.getInstance().getUsersSet().stream().forEach(u -> u.remove());
		LdoD.getInstance().getUserConnectionSet().stream().forEach(u -> u.remove());
		LdoD.getInstance().getTokenSet().stream().forEach(u -> u.remove());

		assertTrue(LdoD.getInstance().getUsersSet().size() == 0);
		assertTrue(LdoD.getInstance().getUserConnectionSet().size() == 0);
		assertTrue(LdoD.getInstance().getTokenSet().size() == 0);

		UsersXMLImport load = new UsersXMLImport();
		load.importUsers(usersXML);

		assertEquals(numOfUsers, LdoD.getInstance().getUsersSet().size());
		assertEquals(numOfUserConnections, LdoD.getInstance().getUserConnectionSet().size());
		assertEquals(numOfRegistrationTokens, LdoD.getInstance().getTokenSet().size());

		for (LdoDUser user : LdoD.getInstance().getUsersSet()) {
			assertTrue(user.getRolesSet().size() != 0);
		}

		for (RegistrationToken token : LdoD.getInstance().getTokenSet()) {
			assertNotNull(token.getUser());
		}
	}

	@After
	public void tearDown() throws IllegalStateException, SecurityException, SystemException {
		FenixFramework.getTransactionManager().rollback();
	}

}
