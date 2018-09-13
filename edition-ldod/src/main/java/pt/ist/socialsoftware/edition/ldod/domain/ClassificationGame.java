package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

public class ClassificationGame extends ClassificationGame_Base {

	public ClassificationGame(VirtualEdition virtualEdition, String description, boolean players, DateTime date,
			VirtualEditionInter inter, LdoDUser user) {
		if (!virtualEdition.getTaxonomy().getOpenVocabulary()) {
			throw new LdoDException("Cannot create game due to close vocabulary");
		}

		setDescription(description);
		setOpenAnnotation(players);
		setDateTime(date);
		setVirtualEditionInter(inter);
		setResponsible(user);

		setVirtualEdition(virtualEdition);
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		setVirtualEdition(null);

		if (getTag() != null) {
			getTag().remove();
		}

		setVirtualEditionInter(null);
		setResponsible(null);

		getPlayerSet().stream().forEach(p -> p.removeClassificationGame(this));

		deleteDomainObject();
	}

	public boolean isActive() {
		return DateTime.now().isBefore(getDateTime());
	}

	@Atomic(mode = TxMode.WRITE)
	public void finish(String winnerUsername, String tagName, Map<String,Double> players) {
		LdoDUser winner = LdoD.getInstance().getUser(winnerUsername);

		Tag tag = getVirtualEdition().getTaxonomy().createTag(getVirtualEditionInter(), tagName, null, winner);

		setTag(tag);

		Set<LdoDUser> users = players.keySet().stream().map(p -> LdoD.getInstance().getUser(p)).collect(Collectors.toSet());

		for (LdoDUser user : users) {
			if (user.getPlayer() == null) {
				new Player(user);
			}

			user.getPlayer().addClassificationGame(this);
			// missing setting up the score
			user.getPlayer().setScore(players.get(user.getUsername()));
		}
	}

	public boolean canBeRemoved() {
		return getTag() == null;
	}

}
