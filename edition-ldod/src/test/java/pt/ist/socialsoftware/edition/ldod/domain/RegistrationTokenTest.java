package pt.ist.socialsoftware.edition.ldod.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.TestWithFragmentsLoading;

public class RegistrationTokenTest extends TestWithFragmentsLoading {

	LdoD ldoD;
	LdoDUser user;
	private RegistrationToken registration;

	@Override
	protected String[] fragmentsToLoad4Test() {
		String[] fragments = new String[0];

		return fragments;
	}

	@Override
	public void populate4Test() {
		this.ldoD = LdoD.getInstance();

		this.user = new LdoDUser(this.ldoD, "ars1", "ars", "Antonio", "Silva", "a@a.a");
		this.registration = new RegistrationToken("token", this.user);
	}

	@Override
	public void unpopulate4Test() {
		TestLoadUtils.cleanDatabaseButCorpus();
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	public void removeOutdatedTokensOne() {
		int size = this.ldoD.getUsersSet().size();
		int tokens = this.ldoD.getTokenSet().size();

		this.ldoD.removeOutdatedUnconfirmedUsers();

		assertEquals(tokens, this.ldoD.getTokenSet().size());
		assertEquals(size, this.ldoD.getUsersSet().size());
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	public void removeOutdatedTokensTwo() {
		this.user.getToken().setExpireTimeDateTime(new DateTime(2014, 1, 1, 0, 0, 0, 0));

		int size = this.ldoD.getUsersSet().size();
		int tokens = this.ldoD.getTokenSet().size();

		this.ldoD.removeOutdatedUnconfirmedUsers();

		assertEquals(tokens - 1, this.ldoD.getTokenSet().size());
		assertEquals(size - 1, this.ldoD.getUsersSet().size());
	}

}
