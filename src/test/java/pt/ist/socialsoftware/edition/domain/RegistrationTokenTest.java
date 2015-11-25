package pt.ist.socialsoftware.edition.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;

public class RegistrationTokenTest extends RollbackCaseTest {

	LdoD ldoD;
	LdoDUser user;

	@Override
	public void populate4Test() {
		ldoD = LdoD.getInstance();

		user = new LdoDUser(ldoD, "ars1", "ars", "Antonio", "Silva", "a@a.a");
		new RegistrationToken("token", user);
	}

	@Test
	public void removeOutdatedTokensOne() {
		int size = ldoD.getUsersSet().size();
		int tokens = ldoD.getTokenSet().size();

		ldoD.removeOutdatedUnconfirmedUsers();

		assertEquals(tokens, ldoD.getTokenSet().size());
		assertEquals(size, ldoD.getUsersSet().size());
	}

	@Test
	public void removeOutdatedTokensTwo() {
		user.getToken().setExpireTimeDateTime(new DateTime(2014, 1, 1, 0, 0, 0, 0));

		int size = ldoD.getUsersSet().size();
		int tokens = ldoD.getTokenSet().size();

		ldoD.removeOutdatedUnconfirmedUsers();

		assertEquals(tokens - 1, ldoD.getTokenSet().size());
		assertEquals(size - 1, ldoD.getUsersSet().size());
	}

}
