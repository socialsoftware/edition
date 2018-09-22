package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.joda.time.DateTime;

import org.springframework.beans.factory.annotation.Autowired;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.dto.ClassificationGameDto;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

public class ClassificationGame extends ClassificationGame_Base {

	public enum ClassificationGameState {CREATED, OPEN, STARTED, FINISHED};

	public ClassificationGame(VirtualEdition virtualEdition, String description, boolean players, DateTime date,
			VirtualEditionInter inter, LdoDUser user) {
		if (!virtualEdition.getTaxonomy().getOpenVocabulary()) {
			throw new LdoDException("Cannot create game due to close vocabulary");
		}

		setState(ClassificationGameState.CREATED);
		setDescription(description);
		setOpenAnnotation(players);
		setDateTime(date);
		setVirtualEditionInter(inter);
		setResponsible(user);

		setVirtualEdition(virtualEdition);

		// Not open to all user, only members
		/*if(!players){
			List<LdoDUser> members = this.getVirtualEdition().getActiveMemberSet().stream().map(Member::getUser).collect(Collectors.toList());
			for (LdoDUser member : members) {
				if (member.getPlayer() == null) {
					member.setPlayer(new Player(member));
				}

				member.getPlayer().addClassificationGame(this);


			}
		}*/
		//gameBlockingMap.put(this.getExternalId(), new ArrayBlockingQueue<String>(100));
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		setVirtualEdition(null);

		Tag tag = getTag();
		if (tag != null) {
			setTag(null);
			tag.remove();
		}

		setVirtualEditionInter(null);
		setResponsible(null);

		getClassificationGameParticipantSet().stream().forEach(p -> p.remove());

		deleteDomainObject();
	}

	public boolean isActive() {
		return DateTime.now().isBefore(getDateTime());
	}

	@Atomic(mode = TxMode.WRITE)
	public void addParticipant(String username) {
		LdoDUser user = LdoD.getInstance().getUser(username);

		if (!getOpenAnnotation() && !getVirtualEdition().getActiveMemberSet().contains(user)) {
			new LdoDException("User not allowed to play this game.");
		}

		new ClassificationGameParticipant(this, user);
	}

	@Atomic(mode = TxMode.WRITE)
	public void finish(String winnerUsername, String tagName, Map<String,Double> players) {
		LdoDUser winner = LdoD.getInstance().getUser(winnerUsername);

		getClassificationGameParticipantSet().stream().filter(p -> p.getPlayer().getUser() == winner).findFirst().get().setWinner(true);

		Tag tag = getVirtualEdition().getTaxonomy().createTag(getVirtualEditionInter(), tagName, null, winner);

		setTag(tag);

		/*Set<LdoDUser> users = players.keySet().stream().map(p -> LdoD.getInstance().getUser(p)).collect(Collectors.toSet());

		for (LdoDUser user : users) {
			if (user.getPlayer() == null) {
				new Player(user);
			}

			user.getPlayer().addClassificationGame(this);
			// missing setting up the score
			user.getPlayer().setScore(players.get(user.getUsername()));
		}*/

		setState(ClassificationGameState.FINISHED);
	}

	public boolean canBeRemoved() {
		return getTag() == null;
	}

	public Map<String, Double> getLeaderboard() {
		List<Player> players = getClassificationGameParticipantSet().stream().map
				(ClassificationGameParticipant::getPlayer).sorted(Comparator.comparing(Player::score)).collect
				(Collectors.toList());

		return players.stream().collect(Collectors.toMap(p -> p.getUser().getUsername(),
				Player::getScore));
	}

}
