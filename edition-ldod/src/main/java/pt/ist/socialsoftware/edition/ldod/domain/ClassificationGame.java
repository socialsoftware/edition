package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.*;
import java.util.stream.Collectors;

import org.joda.time.DateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

public class ClassificationGame extends ClassificationGame_Base {
	private static Logger logger = LoggerFactory.getLogger(ClassificationGame.class);

	public enum ClassificationGameState {CREATED, OPEN, STARTED, TAGGING, VOTING, REVIEWING, ABORTED, FINISHED};
	public static final double SUBMIT = 1;
	public static final double SUBMITTER_IS_ROUND_WINNER = 5;
	public static final double SUBMITTER_IS_GAME_WINNER = 10;
	public static final double VOTING_IN_ROUND_WINNER = 2;
	public static final double VOTING_IN_GAME_WINNER = 5;
	private Map<String, Double> tags = new LinkedHashMap<>();

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
	public void finish(String winnerUsername, String tagName) {
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

	public ClassificationGameParticipant getParticipant(String username) {
		return getClassificationGameParticipantSet().stream()
				.filter(p -> p.getPlayer().getUser().getUsername().equals(username)).findFirst().orElse(null);
	}

	private Set<ClassificationGameRound> getAllRounds(){
		return  getClassificationGameParticipantSet().stream().map
				(ClassificationGameParticipant::getClassificationGameRoundSet).
				flatMap(Set::stream).collect(Collectors.toSet());
	}

	public String getCurrentTagWinner() {
		return tags.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
	}

	public Map<String, Double> getCurrentTopTags(int limit) {
		return tags.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
				.limit(limit).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public ClassificationGameParticipant getCurrentParticipantWinner() {
		String currentTagWinner = getCurrentTagWinner();
		Set<ClassificationGameRound> roundsByDate = getAllRounds().stream().sorted(Comparator.comparing
				(ClassificationGameRound::getTime).reversed()).collect(Collectors.toSet());
		ClassificationGameRound gameRound = roundsByDate.stream().filter(round -> round.getTag().equals
				(currentTagWinner)).findFirst().orElse(null);

		/*for (ClassificationGameRound r : roundsByDate) {
			logger.debug("Ronda: {} date {}", r.getNumber(), r.getTime());
		}*/
		return gameRound.getClassificationGameParticipant();
	}

	/*public Map<String, Double> joinRoundsByTag() {
		Map<String, Double> collect = getAllRounds().stream().collect(Collectors.groupingBy
				(ClassificationGameRound::getTag,
						Collectors.summingDouble(ClassificationGameRound::getVote)));
		for (Map.Entry<String, Double> e : collect.entrySet()) {
			logger.debug("Tag: {}  Votes: {}", e.getKey(), e.getValue());
		}

		return collect;
	}*/

	/*public double getScore4Tag (String tag) {
		return joinRoundsByTag().entrySet().stream().filter(e -> e.getKey().equals(tag))
				.map(Map.Entry::getValue)
				.findFirst()
				.orElse(null);
	}*/

	public ClassificationGameRound getRound4User(String username, int roundNumber) {
		return getAllRounds().stream().filter(r -> r
				.getClassificationGameParticipant().getPlayer().getUser().getUsername().equals(username) && r.getNumber() == roundNumber).findFirst().orElse(null);
	}

	public void addTag(String tag, double vote) {
		tags.put(tag, vote);
	}

	public Map<String, Double> getTags() {
		return tags;
	}


	/*private Map<Integer, Double> joinRoundsByNumber() {
		return  getAllRounds().stream().collect(Collectors.groupingBy(ClassificationGameRound::getNumber,
				Collectors.summingDouble(ClassificationGameRound::getVote)));
	}*/


}
