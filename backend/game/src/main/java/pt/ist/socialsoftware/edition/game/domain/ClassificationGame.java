package pt.ist.socialsoftware.edition.game.domain;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.game.api.GameRequiresInterface;

import pt.ist.socialsoftware.edition.notification.dtos.virtual.TagDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.notification.utils.LdoDException;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ClassificationGame extends ClassificationGame_Base {
    private static final Logger logger = LoggerFactory.getLogger(ClassificationGame.class);

    public enum ClassificationGameState {
        CREATED, OPEN, STARTED, TAGGING, VOTING, REVIEWING, ABORTED, FINISHED
    }

    public static final double SUBMIT_TAG = 1;
    public static final double VOTE_CHANGE = -1;
    public static final double SUBMITTER_IS_ROUND_WINNER = 5;
    public static final double SUBMITTED_LIKE_GAME_WINNER = 5;
    public static final double SUBMITTER_IS_GAME_WINNER = 10;
    public static final double VOTED_IN_ROUND_WINNER = 2;
    public static final double VOTED_IN_GAME_WINNER = 5;

    public static final int SUBMISSION_ROUND = 1;
    public static final int VOTING_PARAGRAPH_ROUND = 2;
    public static final int VOTING_FINAL_ROUND = 3;

    public ClassificationGame(VirtualEditionDto virtualEdition, String description, DateTime date,
                              VirtualEditionInterDto inter, String user) {

        //GameRequiresInterface gameRequiresInterface = new GameRequiresInterface();

        if (!GameRequiresInterface.getInstance().getOpenVocabularyStatus(virtualEdition.getAcronym())) {
            throw new LdoDException("Cannot create game due to close vocabulary");
        }

        setState(ClassificationGameState.CREATED);
        setDescription(description);
        setDateTime(date);


        setInterId(inter.getXmlId());

        setResponsible(user);

        setEditionId(virtualEdition.getAcronym());

        setClassificationModule(ClassificationModule.getInstance());
    }

    @Atomic(mode = TxMode.WRITE)
    public void remove() {
        //GameRequiresInterface gameRequiresInterface = new GameRequiresInterface();

        GameRequiresInterface.getInstance().removeTag(getTagId());

        getClassificationGameParticipantSet().stream().forEach(p -> p.remove());

        setClassificationModule(null);

        deleteDomainObject();
    }

    public boolean getOpenAnnotation() {
        //GameRequiresInterface gameRequiresInterface = new GameRequiresInterface();
        return GameRequiresInterface.getInstance().getOpenAnnotationStatus(getEditionId());
    }

    public boolean isActive() {
        return DateTime.now().isBefore(getDateTime().plusSeconds(55));
    }

    @Atomic(mode = TxMode.WRITE)
    public void addParticipant(String user) {
       // GameRequiresInterface gameRequiresInterface = new GameRequiresInterface();

        if (!getOpenAnnotation()
                && !GameRequiresInterface.getInstance().isUserParticipant(getEditionId(), user)) {
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
        String winner = participant.getPlayer().getUser();
        String tagName = getCurrentTagWinner();

        getClassificationGameParticipantSet().stream().filter(p -> p.getPlayer().getUser().equals(winner)).findFirst().get()
                .setWinner(true);

        //GameRequiresInterface gameRequiresInterface = new GameRequiresInterface();

        TagDto tag = GameRequiresInterface.getInstance().createTag(getEditionId(), getInterId(), tagName, winner);

        setTagId(tag.getUrlId());

        /*
         * Set<User> users = players.keySet().stream().map(p ->
         * VirtualModule.getInstance().getUser(p)).collect(Collectors.toSet());
         *
         * for (User user : users) { if (user.getPlayer() == null) { new
         * Player(user); }
         *
         * user.getPlayer().addClassificationGame(this); // missing setting up the score
         * user.getPlayer().setScore(players.get(user.getUsername())); }
         */

        getClassificationGameParticipantSet().stream()
                .forEach(p -> p.getPlayer().setScore(p.getPlayer().getScore() + p.getScore()));

        setState(ClassificationGameState.FINISHED);
    }

    public boolean canBeRemoved() {
        return getTagId() == null;
    }

    public Map<String, Double> getLeaderboard() {
        List<ClassificationGameParticipant> participants = getClassificationGameParticipantSet().stream()
                .sorted(Comparator.comparing(ClassificationGameParticipant::getScore)).collect(Collectors.toList());

        Map<String, Double> result = new HashMap<>();
        for (ClassificationGameParticipant participant : participants) {
            if (participant.getPlayer() != null) {
                result.put(participant.getPlayer().getUser(), participant.getScore());
            }
        }

        return result;
    }

    public ClassificationGameParticipant getParticipant(String username) {
        return getClassificationGameParticipantSet().stream()
                .filter(p -> p.getPlayer().getUser().equals(username)).findFirst().orElse(null);
    }

    public Stream<ClassificationGameRound> getAllRounds() {
        return getClassificationGameParticipantSet().stream().flatMap(p -> p.getClassificationGameRoundSet().stream());
    }

    public String getCurrentTagWinner() {
        return getAllRounds().max(Comparator.comparing(ClassificationGameRound::getVote)).map(r -> r.getTag())
                .orElse(null);
    }

    public Set<String> getCurrentTopTags(int numberOfParagraphs) {
        return IntStream.range(0, numberOfParagraphs).mapToObj(p -> getRoundWinnerTag4Paragraph(p).getTag()).distinct()
                .collect(Collectors.toSet());
    }

    public ClassificationGameParticipant getCurrentParticipantWinner() {
        String currentTagWinner = getCurrentTagWinner();

        return getAllRounds().filter(round -> round.getTag().equals(currentTagWinner))
                .sorted(Comparator.comparing(o -> o.getTime().getMillis())).findFirst()
                .map(r -> r.getClassificationGameParticipant()).orElse(null);
    }

    private ClassificationGameParticipant calculateParticipantsScores() {
        String tagWinner = getCurrentTagWinner();

        Set<ClassificationGameRound> roundWinners = getRoundWinners(tagWinner, SUBMISSION_ROUND)
                .collect(Collectors.toSet());

        // Submission round

        // Participant that has submitted GAME winner tag FIRST than anyone receives +
        // 10
        ClassificationGameParticipant gameWinner = roundWinners.stream()
                .min(Comparator.comparing(r -> r.getTime().getMillis())).map(r -> r.getClassificationGameParticipant())
                .orElse(null);
        gameWinner.setScore(gameWinner.getScore() + SUBMITTER_IS_GAME_WINNER);

        // Participant that has submitted GAME winner tag but is NOT the first equals
        // voter receives + 5
        roundWinners.stream().map(r -> r.getClassificationGameParticipant())
                .forEach(p -> p.setScore(p.getScore() + SUBMITTED_LIKE_GAME_WINNER));

        // Paragraph submission and voting
        int numberOfParagraphs = getAllRounds()
                .sorted(Comparator.comparing(ClassificationGameRound::getNumber).reversed()).map(r -> r.getNumber())
                .findFirst().orElse(0);

        for (int paragraph = 0; paragraph < numberOfParagraphs; paragraph++) {
            ClassificationGameRound winnerRoundForParagraph = getRoundWinnerTag4Paragraph(paragraph);

            if (winnerRoundForParagraph != null) {
                // Participant that has submitted ROUND winner tag receives 5
                getRoundWinnersSubmit4Paragraph(winnerRoundForParagraph.getTag(), paragraph)
                        .map(r -> r.getClassificationGameParticipant())
                        .forEach(p -> p.setScore(p.getScore() + SUBMITTER_IS_ROUND_WINNER));

                // Participants that voted in paragraph winner tag receives + 2
                getRoundWinnersVoting4Paragraph(winnerRoundForParagraph.getTag(), paragraph)
                        .map(r -> r.getClassificationGameParticipant())
                        .forEach(p -> p.setScore(p.getScore() + VOTED_IN_ROUND_WINNER));

            }
        }

        // ------------- Round 3 ------------- //
        // Voted on game winner tag + 5
        getRoundWinners(tagWinner, VOTING_FINAL_ROUND).map(r -> r.getClassificationGameParticipant())
                .forEach(p -> p.setScore(p.getScore() + VOTED_IN_GAME_WINNER));

        // Impossible to have bellow zero so every participant gets 1 point
        getClassificationGameParticipantSet().stream().forEach(p -> p.setScore(p.getScore() < 0 ? 1 : p.getScore()));

        return gameWinner;
    }

    private Stream<ClassificationGameRound> getRoundWinners(String tagWinner, int round) {
        return getAllRounds().filter(r -> r.getTag().equals(tagWinner) && r.getRound() == round)
                .sorted(Comparator.comparing(r -> r.getTime().getMillis()));
    }

    private Stream<ClassificationGameRound> getRoundWinnersSubmit4Paragraph(String tagWinner, int paragraph) {
        return getAllRounds().filter(
                r -> r.getTag().equals(tagWinner) && r.getNumber() == paragraph && r.getRound() == SUBMISSION_ROUND);
    }

    private Stream<ClassificationGameRound> getRoundWinnersVoting4Paragraph(String tagWinner, int paragraph) {
        return getAllRounds().filter(r -> r.getTag().equals(tagWinner) && r.getNumber() == paragraph
                && r.getRound() == VOTING_PARAGRAPH_ROUND);
    }

    private ClassificationGameRound getRoundWinnerTag4Paragraph(int paragraph) {
        return getAllRounds().filter(r -> r.getNumber() == paragraph && r.getRound() == VOTING_PARAGRAPH_ROUND)
                .sorted(Comparator.comparing(ClassificationGameRound::getVote).reversed()
                        .thenComparing(Comparator.comparing(o -> o.getTime().getMillis())))
                .findFirst().orElse(null);
    }

    public double getVotesForTagInFinalRound(String tag) {
        return getAllRounds().filter(r -> r.getRound() == VOTING_FINAL_ROUND && r.getTag().equals(tag))
                .sorted((r1, r2) -> r2.getTime().compareTo(r1.getTime())).map(r -> r.getVote()).findFirst().orElse(0.0);
    }

    public double getVotesForTagInParagraph(String tag, int paragraph) {
        return getAllRounds()
                .filter(r -> r.getNumber() == paragraph && r.getRound() == VOTING_PARAGRAPH_ROUND
                        && r.getTag().equals(tag))
                .sorted((r1, r2) -> r2.getTime().compareTo(r1.getTime())).map(r -> r.getVote()).findFirst().orElse(0.0);
    }

}
