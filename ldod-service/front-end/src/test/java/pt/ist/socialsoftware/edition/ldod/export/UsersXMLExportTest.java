package pt.ist.socialsoftware.edition.ldod.export;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.RegistrationTokenDto;
import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.UserDto;


import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class UsersXMLExportTest {

    private final FeUserRequiresInterface feUserRequiresInterface = new FeUserRequiresInterface();

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

        String usersXML = feUserRequiresInterface.exportXMLUsers();

        int numOfUsers = feUserRequiresInterface.getUsersSet().size();
        int numOfUserConnections = feUserRequiresInterface.getUserConnectionSet().size();
        int numOfRegistrationTokens = feUserRequiresInterface.getTokensSet().size();

        feUserRequiresInterface.getUsersSet().forEach(u -> u.removeUser());
        feUserRequiresInterface.getUserConnectionSet().stream().forEach(u -> u.removeUserConnection());
        feUserRequiresInterface.getTokensSet().stream().forEach(u -> u.removeToken());

        assertTrue(feUserRequiresInterface.getUsersSet().size() == 0);
        assertTrue(feUserRequiresInterface.getUserConnectionSet().size() == 0);
        assertTrue(feUserRequiresInterface.getTokensSet().size() == 0);

        feUserRequiresInterface.importUsersFromXML(new ByteArrayInputStream(usersXML.getBytes()));

        assertEquals(numOfUsers, feUserRequiresInterface.getUsersSet().size());
        assertEquals(numOfUserConnections, feUserRequiresInterface.getUserConnectionSet().size());
        assertEquals(numOfRegistrationTokens, feUserRequiresInterface.getTokensSet().size());

        for (UserDto user : feUserRequiresInterface.getUsersSet()) {
            if (!user.getUsername().equals("Twitter")) {
                assertTrue(user.getRolesSet().size() != 0);
            }
        }

        for (RegistrationTokenDto token :feUserRequiresInterface.getTokensSet()) {
            assertNotNull(token.getUser());
        }
    }

}
