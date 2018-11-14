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
import pt.ist.socialsoftware.edition.ldod.loaders.UsersXMLImport;
import pt.ist.socialsoftware.edition.user.domain.RegistrationToken;
import pt.ist.socialsoftware.edition.user.domain.User;
import pt.ist.socialsoftware.edition.user.domain.UserManager;

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

		int numOfUsers = UserManager.getInstance().getUsersSet().size();
		int numOfUserConnections = UserManager.getInstance().getUserConnectionSet().size();
		int numOfRegistrationTokens = UserManager.getInstance().getTokenSet().size();

		UserManager.getInstance().getUsersSet().stream().forEach(u -> u.remove());
		UserManager.getInstance().getUserConnectionSet().stream().forEach(u -> u.remove());
		UserManager.getInstance().getTokenSet().stream().forEach(u -> u.remove());

		assertTrue(UserManager.getInstance().getUsersSet().size() == 0);
		assertTrue(UserManager.getInstance().getUserConnectionSet().size() == 0);
		assertTrue(UserManager.getInstance().getTokenSet().size() == 0);

		UsersXMLImport load = new UsersXMLImport();
		load.importUsers(usersXML);

		assertEquals(numOfUsers, UserManager.getInstance().getUsersSet().size());
		assertEquals(numOfUserConnections, UserManager.getInstance().getUserConnectionSet().size());
		assertEquals(numOfRegistrationTokens, UserManager.getInstance().getTokenSet().size());

		for (User user : UserManager.getInstance().getUsersSet()) {
			assertTrue(user.getRolesSet().size() != 0);
		}

		for (RegistrationToken token : UserManager.getInstance().getTokenSet()) {
			assertNotNull(token.getUser());
		}
	}

	@AfterEach
	public void tearDown() throws IllegalStateException, SecurityException, SystemException {
		FenixFramework.getTransactionManager().rollback();
	}

}
