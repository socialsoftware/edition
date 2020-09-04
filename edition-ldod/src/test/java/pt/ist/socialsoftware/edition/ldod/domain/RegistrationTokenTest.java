package pt.ist.socialsoftware.edition.ldod.domain;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.TestWithFragmentsLoading;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTokenTest extends TestWithFragmentsLoading {

    UserModule userModule;
    User user;
    private RegistrationToken registration;

    @Override
    protected String[] fragmentsToLoad4Test() {
        String[] fragments = new String[0];

        return fragments;
    }

    @Override
    public void populate4Test() {
        this.userModule = UserModule.getInstance();

        this.user = new User(this.userModule, "ars1", "ars", "Antonio", "Silva", "a@a.a");
        this.registration = new RegistrationToken("token", this.user);
    }

    @Override
    public void unpopulate4Test() {
        TestLoadUtils.cleanDatabase();
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void removeOutdatedTokensOne() {
        int size = this.userModule.getUsersSet().size();
        int tokens = this.userModule.getTokenSet().size();

        this.userModule.removeOutdatedUnconfirmedUsers();

        assertEquals(tokens, this.userModule.getTokenSet().size());
        assertEquals(size, this.userModule.getUsersSet().size());
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void removeOutdatedTokensTwo() {
        this.user.getToken().setExpireTimeDateTime(new DateTime(2014, 1, 1, 0, 0, 0, 0));

        int size = this.userModule.getUsersSet().size();
        int tokens = this.userModule.getTokenSet().size();

        this.userModule.removeOutdatedUnconfirmedUsers();

        assertEquals(tokens - 1, this.userModule.getTokenSet().size());
        assertEquals(size - 1, this.userModule.getUsersSet().size());
    }

}
