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
	public static final double SUBMIT_TAG = 1;
	public static final double VOTE_CHANGE = -1;
	public static final double SUBMITTER_IS_ROUND_WINNER = 5;
	public static final double SUBMITTER_IS_GAME_WINNER = 10;
	public static final double VOTED_IN_ROUND_WINNER = 2;
	public static final double VOTED_IN_GAME_WINNER = 5;
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
	public void finish() {
		if (getState().equals(ClassificationGameState.FINISHED)) {
			// CANT RECALCULATE OR FINISH A GAME AFTER GAME FINISHED
			return;
		}
		ClassificationGameParticipant participant = calculateParticipantsScores();
		LdoDUser winner = participant.getPlayer().getUser();
		String tagName = getCurrentTagWinner();

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

		getClassificationGameParticipantSet().stream().forEach(p-> p.getPlayer().setScore(p.getScore()));

		setState(ClassificationGameState.FINISHED);
	}

	public boolean canBeRemoved() {
		return getTag() == null;
	}

	public Map<String, Double> getLeaderboard() {
		List<ClassificationGameParticipant> participants = getClassificationGameParticipantSet().stream().sorted(Comparator.comparing(ClassificationGameParticipant::getScore)).collect
				(Collectors.toList());

		return participants.stream().collect(Collectors.toMap(p -> p.getPlayer().getUser().getUsername(),
				ClassificationGameParticipant::getScore));
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

	private String getTagWinner() {
		int limit = getAllRounds().stream().max(Comparator.comparing(ClassificationGameRound::getNumber)).get().getNumber();
		return getRoundWinnerTag4Paragraph(limit , 3).getTag();
	}

	public Map<String, Double> getCurrentTopTags(int limit) {
		return tags.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
				.limit(limit).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}


	public ClassificationGameParticipant getCurrentParticipantWinner() {
		String currentTagWinner = getCurrentTagWinner();
		Set<ClassificationGameRound> roundsByDate = getAllRounds().stream().
				sorted(Comparator.comparing(o -> o.getTime().getMillis())).
				collect(Collectors.toSet());
		ClassificationGameRound gameRound = roundsByDate.stream().filter(round -> round.getTag().equals
				(currentTagWinner)).findFirst().orElse(null);

		return gameRound.getClassificationGameParticipant();
	}

	public ClassificationGameRound getRound4User(String username, int roundNumber) {
		return getAllRounds().stream().
				filter(r -> r.getClassificationGameParticipant().getPlayer().getUser().getUsername().
						equals(username) && r.getNumber() == roundNumber).
				findFirst().orElse(null);
	}

	public void addTag(String tag, double vote) {
		tags.put(tag, vote);
	}

	public Map<String, Double> getTags() {
		return tags;
	}

	private ClassificationGameParticipant calculateParticipantsScores() {
		//String currentTagWinner = getCurrentTagWinner();
		String tagWinner =  getCurrentTagWinner();

		//Map<ClassificationGameParticipant, ClassificationGameRound> roundOneMap = getRoundMap(currentTagWinner, 1);
		Map<ClassificationGameParticipant, ClassificationGameRound> roundOneMap = getRoundMap(tagWinner, 1);

		// ------------- Round 1 ------------- //

		// Participant that has submitted GAME winner tag FIRST than anyone receives + 10
		ClassificationGameParticipant gameWinner = roundOneMap.entrySet().stream().
				min(Comparator.comparing(e -> e.getValue().getTime().getMillis())).
				orElse(null).getKey();
		gameWinner.setScore(gameWinner.getScore() + SUBMITTER_IS_GAME_WINNER);

		// Participant that has submitted GAME winner tag but is NOT the first equals voter receives + 5
		roundOneMap.forEach((p,r) -> p.setScore(p.getScore() + VOTED_IN_GAME_WINNER));


		// ------------- Round 2 ------------- //
		// TODO: NOT WORKING - check get round winnertag4paragraph
		int reviewNumber = getAllRounds().stream().max(Comparator.comparing(ClassificationGameRound::getNumber)).get().getNumber();
		Map<String, ClassificationGameRound> topRounds = new HashMap<>();
		for (int i = 0; i < reviewNumber; i++) {
			ClassificationGameRound r = getRoundWinnerTag4Paragraph(i, 2);
			topRounds.put(r.getTag(), r);
		}

		for (ClassificationGameRound r : topRounds.values()) {
			// For each top tag im getting the participants that submitted them
			Set<ClassificationGameParticipant> topTagsSubmitters = getRoundMap(r.getTag(), 1).keySet();

			// Participant that has submitted ROUND winner tag FIRST than anyone receives + 5
			topTagsSubmitters.forEach(p -> p.setScore(p.getScore() + SUBMITTER_IS_ROUND_WINNER));

			// For each top tag im getting the participants that voted in them
			Set<ClassificationGameParticipant> topTagsVoters = getRoundMap(r.getTag(), 2).keySet();

			// Participant that has voted in ROUND winner tag receives + 2
			topTagsVoters.forEach(p -> p.setScore(p.getScore() + VOTED_IN_ROUND_WINNER));
		}

		// ------------- Round 3 ------------- //
		// Voted on game winner tag + 5
		Map<ClassificationGameParticipant, ClassificationGameRound> roundThreeMap = getRoundMap(tagWinner, 3);
		roundThreeMap.forEach((p,r) -> p.setScore(p.getScore() + VOTED_IN_GAME_WINNER));


		// Impossible to have bellow zero so every participant gets 1 point
		getClassificationGameParticipantSet().stream().
				forEach(p -> p.setScore(p.getScore() < 0 ? 1 : p.getScore()));

		return gameWinner;
	}

	private Map<ClassificationGameParticipant, ClassificationGameRound> getRoundMap(String currentTagWinner, int i) {
		return getAllRounds().
				stream().filter(round -> round.getTag().equals(currentTagWinner) && round.getRound() == i).
				collect(Collectors.toSet()).stream().sorted(Comparator.comparing(r -> r.getTime().getMillis())).
				collect(Collectors.toMap(r -> r.getClassificationGameParticipant(), r -> r, (r1, r2) -> r1));
	}

	public ClassificationGameRound getRoundWinnerTag4Paragraph(int number, int round) {
		return getAllRounds().stream().
				filter(r -> r.getNumber() == number && r.getRound() == round)
				.sorted(Comparator.comparing(o -> o.getTime().getMillis()))
				.max(Comparator.comparing(ClassificationGameRound::getVote)).orElse(null);
	}
}
