package pt.ist.socialsoftware.edition.ldod.export;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.user.domain.RegistrationToken;
import pt.ist.socialsoftware.edition.user.domain.User;
import pt.ist.socialsoftware.edition.user.domain.UserModule;
import pt.ist.socialsoftware.edition.user.feature.inout.UsersXMLExport;
import pt.ist.socialsoftware.edition.user.feature.inout.UsersXMLImport;


import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class UsersXMLExportTest {
    @BeforeEach
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void setUp() throws FileNotFoundException {
        TestLoadUtils.setUpDatabaseWithCorpus();
    }

    @AfterEach
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void tearDown() {
        TestLoadUtils.cleanDatabase();
    }

    @Test
    @Atomic
    public void test() throws WriteOnReadError, NotSupportedException, SystemException {
        UsersXMLExport export = new UsersXMLExport();
        String usersXML = export.export();

        int numOfUsers = UserModule.getInstance().getUsersSet().size();
        int numOfUserConnections = UserModule.getInstance().getUserConnectionSet().size();
        int numOfRegistrationTokens = UserModule.getInstance().getTokenSet().size();

        UserModule.getInstance().getUsersSet().stream().forEach(u -> u.remove());
        UserModule.getInstance().getUserConnectionSet().stream().forEach(u -> u.remove());
        UserModule.getInstance().getTokenSet().stream().forEach(u -> u.remove());

        assertTrue(UserModule.getInstance().getUsersSet().size() == 0);
        assertTrue(UserModule.getInstance().getUserConnectionSet().size() == 0);
        assertTrue(UserModule.getInstance().getTokenSet().size() == 0);

        UsersXMLImport load = new UsersXMLImport();
        load.importUsers(usersXML);

        assertEquals(numOfUsers, UserModule.getInstance().getUsersSet().size());
        assertEquals(numOfUserConnections, UserModule.getInstance().getUserConnectionSet().size());
        assertEquals(numOfRegistrationTokens, UserModule.getInstance().getTokenSet().size());

        for (User user : UserModule.getInstance().getUsersSet()) {
            if (!user.getUsername().equals("Twitter")) {
                assertTrue(user.getRolesSet().size() != 0);
            }
        }

        for (RegistrationToken token : UserModule.getInstance().getTokenSet()) {
            assertNotNull(token.getUser());
        }
    }

}
