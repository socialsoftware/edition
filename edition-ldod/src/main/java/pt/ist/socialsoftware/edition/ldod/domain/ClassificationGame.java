package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

public class ClassificationGame extends ClassificationGame_Base {

	public enum ClassificationGameState {OPEN, STARTED, FINISHED};

	private volatile static ArrayBlockingQueue<String> userArray = new ArrayBlockingQueue<>(100);
	private volatile static Map<String, ArrayBlockingQueue<String>> gameBlockingMap = new ConcurrentHashMap<>(100);
	public volatile boolean started;

	public ClassificationGame(VirtualEdition virtualEdition, String description, boolean players, DateTime date,
			VirtualEditionInter inter, LdoDUser user) {
		if (!virtualEdition.getTaxonomy().getOpenVocabulary()) {
			throw new LdoDException("Cannot create game due to close vocabulary");
		}

		setState(ClassificationGameState.OPEN);
		setDescription(description);
		setOpenAnnotation(players);
		setDateTime(date);
		setVirtualEditionInter(inter);
		setResponsible(user);

		setVirtualEdition(virtualEdition);

		// Not open to all user, only members
		if(!players){
			List<LdoDUser> members = this.getVirtualEdition().getActiveMemberSet().stream().map(Member::getUser).collect(Collectors.toList());
			for (LdoDUser member : members) {
				if (member.getPlayer() == null) {
					member.setPlayer(new Player(member));
				}

				member.getPlayer().addClassificationGame(this);

			}
		}
		gameBlockingMap.put(this.getExternalId(), new ArrayBlockingQueue<String>(100));
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

		setState(ClassificationGameState.FINISHED);
		setStarted(false);
	}

	public boolean canBeRemoved() {
		return getTag() == null;
	}

	public Map<String, Double> getLeaderboard(){
		Map<String, Double> collect = getPlayerSet().stream().collect(Collectors.toMap(player -> player.getUser()
				.getUsername(), Player::getScore));
		return collect.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

	}

	public static Map<String, ArrayBlockingQueue<String>> getGameBlockingMap() {
		return gameBlockingMap;
	}

	public static ArrayBlockingQueue<String> getUserArray() {
		return userArray;
	}

	public boolean hasStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}
}
