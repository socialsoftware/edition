package pt.ist.socialsoftware.edition.ldod.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import pt.ist.socialsoftware.edition.ldod.RollbackCaseTest;

public class RegistrationTokenTest extends RollbackCaseTest {

	UserManager userManager;
	LdoDUser user;

	@Override
	public void populate4Test() {
		this.userManager = UserManager.getInstance();

		this.user = new LdoDUser(this.userManager, "ars1", "ars", "Antonio", "Silva", "a@a.a");
		new RegistrationToken("token", this.user);
	}

	@Test
	public void removeOutdatedTokensOne() {
		int size = this.userManager.getUsersSet().size();
		int tokens = this.userManager.getTokenSet().size();

		this.userManager.removeOutdatedUnconfirmedUsers();

		assertEquals(tokens, this.userManager.getTokenSet().size());
		assertEquals(size, this.userManager.getUsersSet().size());
	}

	@Test
	public void removeOutdatedTokensTwo() {
		this.user.getToken().setExpireTimeDateTime(new DateTime(2014, 1, 1, 0, 0, 0, 0));

		int size = this.userManager.getUsersSet().size();
		int tokens = this.userManager.getTokenSet().size();

		this.userManager.removeOutdatedUnconfirmedUsers();

		assertEquals(tokens - 1, this.userManager.getTokenSet().size());
		assertEquals(size - 1, this.userManager.getUsersSet().size());
	}

}
