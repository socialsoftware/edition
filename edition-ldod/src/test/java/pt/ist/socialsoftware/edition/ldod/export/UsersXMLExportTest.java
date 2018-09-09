package pt.ist.socialsoftware.edition.ldod.export;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualManager;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken;
import pt.ist.socialsoftware.edition.ldod.loaders.UsersXMLImport;

public class UsersXMLExportTest {
	@BeforeEach
	public void setUp() throws WriteOnReadError, NotSupportedException, SystemException {
		FenixFramework.getTransactionManager().begin(false);
	}

	@Test
	@Atomic
	public void test() throws WriteOnReadError, NotSupportedException, SystemException {
		UsersXMLExport export = new UsersXMLExport();
		String usersXML = export.export();

		int numOfUsers = VirtualManager.getInstance().getUsersSet().size();
		int numOfUserConnections = VirtualManager.getInstance().getUserConnectionSet().size();
		int numOfRegistrationTokens = VirtualManager.getInstance().getTokenSet().size();

		VirtualManager.getInstance().getUsersSet().stream().forEach(u -> u.remove());
		VirtualManager.getInstance().getUserConnectionSet().stream().forEach(u -> u.remove());
		VirtualManager.getInstance().getTokenSet().stream().forEach(u -> u.remove());

		assertTrue(VirtualManager.getInstance().getUsersSet().size() == 0);
		assertTrue(VirtualManager.getInstance().getUserConnectionSet().size() == 0);
		assertTrue(VirtualManager.getInstance().getTokenSet().size() == 0);

		UsersXMLImport load = new UsersXMLImport();
		load.importUsers(usersXML);

		assertEquals(numOfUsers, VirtualManager.getInstance().getUsersSet().size());
		assertEquals(numOfUserConnections, VirtualManager.getInstance().getUserConnectionSet().size());
		assertEquals(numOfRegistrationTokens, VirtualManager.getInstance().getTokenSet().size());

		for (LdoDUser user : VirtualManager.getInstance().getUsersSet()) {
			assertTrue(user.getRolesSet().size() != 0);
		}

		for (RegistrationToken token : VirtualManager.getInstance().getTokenSet()) {
			assertNotNull(token.getUser());
		}
	}

	@AfterEach
	public void tearDown() throws IllegalStateException, SecurityException, SystemException {
		FenixFramework.getTransactionManager().rollback();
	}

}
