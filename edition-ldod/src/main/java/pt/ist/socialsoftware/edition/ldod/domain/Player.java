package pt.ist.socialsoftware.edition.ldod.domain;

public class Player extends Player_Base {

	public Player(LdoDUser user) {
		setUser(user);
	}

	public void remove() {
		setUser(null);
		getClassificationGameSet().stream().forEach(g -> g.removePlayer(this));

		deleteDomainObject();
	}

	public long score() {
		return getClassificationGameSet().stream().filter(g -> g.getTag().getContributor() == getUser()).count();
	}

}
