package pt.ist.socialsoftware.edition.ldod.domain;

import org.joda.time.DateTime;
import org.junit.jupiter.api.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.RegistrationTokenDto;
import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.UserDto;


import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTokenTest {

    private final FeUserRequiresInterface feUserRequiresInterface = new FeUserRequiresInterface();

    UserDto user;
    private RegistrationTokenDto registration;


    @BeforeEach
    @Atomic(mode = TxMode.WRITE)
    public void setUpAll() throws FileNotFoundException {
        TestLoadUtils.setUpDatabaseWithCorpus();

        this.user = feUserRequiresInterface.createTestUser("ars1", "ars", "Antonio", "Silva", "a@a.a");
        this.registration = feUserRequiresInterface.createRegistrationToken(this.user.getUsername(), "token");
    }

    @AfterEach
    @Atomic(mode = TxMode.WRITE)
    public void tearDownAll() {
        TestLoadUtils.cleanDatabase();
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void removeOutdatedTokensOne() {
//        int size = this.userModule.getUsersSet().size();
//        int tokens = this.userModule.getTokenSet().size();
        int size = feUserRequiresInterface.getUsersSet().size();
        int tokens = feUserRequiresInterface.getTokensSet().size();

        this.feUserRequiresInterface.removeOutdatedUnconfirmedUsers();

        assertEquals(tokens, this.feUserRequiresInterface.getTokensSet().size());
        assertEquals(size, this.feUserRequiresInterface.getUsersSet().size());
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void removeOutdatedTokensTwo() {
//        this.user.getToken().setExpireTimeDateTime(new DateTime(2014, 1, 1, 0, 0, 0, 0));
        this.user.getToken().updateExpireTimeDateTime(2014, 1, 1, 0, 0, 0, 0);


        int size = this.feUserRequiresInterface.getUsersSet().size();
        int tokens = this.feUserRequiresInterface.getTokensSet().size();

        this.feUserRequiresInterface.removeOutdatedUnconfirmedUsers();

        assertEquals(tokens - 1, this.feUserRequiresInterface.getTokensSet().size());
        assertEquals(size - 1, this.feUserRequiresInterface.getUsersSet().size());
    }

}
