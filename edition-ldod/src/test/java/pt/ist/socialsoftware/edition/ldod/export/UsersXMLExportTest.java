package pt.ist.socialsoftware.edition.ldod.export;

import org.junit.jupiter.api.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.TestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken;
import pt.ist.socialsoftware.edition.ldod.domain.User;
import pt.ist.socialsoftware.edition.ldod.domain.UserModule;
import pt.ist.socialsoftware.edition.ldod.loaders.UsersXMLImport;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import static org.junit.Assert.*;

public class UsersXMLExportTest extends TestWithFragmentsLoading {

    @Override
    protected String[] fragmentsToLoad4Test() {
        String[] fragments = new String[0];

        return fragments;
    }

    @Override
    protected void populate4Test() {
    }

    @Override
    protected void unpopulate4Test() {
        TestLoadUtils.cleanDatabaseButCorpus();
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
