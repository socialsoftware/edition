package pt.ist.socialsoftware.edition.core.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

public class RegistrationTokenTest extends RollbackCaseTest {

	LdoD ldoD;
	LdoDUser user;

	@Override
	public void populate4Test() {
		this.ldoD = LdoD.getInstance();

		this.user = new LdoDUser(this.ldoD, "ars1", "ars", "Antonio", "Silva", "a@a.a");
		new RegistrationToken("token", this.user);
	}

	@Test
	public void removeOutdatedTokensOne() {
		int size = this.ldoD.getUsersSet().size();
		int tokens = this.ldoD.getTokenSet().size();

		this.ldoD.removeOutdatedUnconfirmedUsers();

		assertEquals(tokens, this.ldoD.getTokenSet().size());
		assertEquals(size, this.ldoD.getUsersSet().size());
	}

	@Test
	public void removeOutdatedTokensTwo() {
		this.user.getToken().setExpireTimeDateTime(new DateTime(2014, 1, 1, 0, 0, 0, 0));

		int size = this.ldoD.getUsersSet().size();
		int tokens = this.ldoD.getTokenSet().size();

		this.ldoD.removeOutdatedUnconfirmedUsers();

		assertEquals(tokens - 1, this.ldoD.getTokenSet().size());
		assertEquals(size - 1, this.ldoD.getUsersSet().size());
	}

}
