package pt.ist.socialsoftware.edition.game.api.remote;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.game.domain.ClassificationGame;
import pt.ist.socialsoftware.edition.game.domain.ClassificationGameParticipant;
import pt.ist.socialsoftware.edition.game.domain.ClassificationGameRound;
import pt.ist.socialsoftware.edition.game.domain.ClassificationModule;
import pt.ist.socialsoftware.edition.game.api.dtoc.ClassificationVirtualGameDto;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services/ldod-game")
public class ClassificationGameController {
    private static final Logger logger = LoggerFactory.getLogger(ClassificationGameController.class);

    @Autowired
    private SimpMessagingTemplate broker;

    private final Map<String, ClassificationVirtualGameDto> gamesMapDto = new LinkedHashMap<>(100);

    // ------------- REST Methods ------------- //

    @GetMapping(value = "/{username}/active", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasPermission(#username, 'user.logged')")
    public @ResponseBody
    ResponseEntity<List<ClassificationVirtualGameDto>> getActiveGames(
            @PathVariable(value = "username") String username) {
        // logger.debug("getActiveGames: {}", username);

        List<ClassificationVirtualGameDto> result = ClassificationModule.getInstance().getActiveGames4User(username).stream()
                .map(ClassificationVirtualGameDto::new).sorted(Comparator.comparingLong(ClassificationVirtualGameDto::getDateTime))
                .collect(Collectors.toList());

        for (ClassificationVirtualGameDto gameDto : result) {
            if (!this.gamesMapDto.containsKey(gameDto.getGameExternalId())) {
                this.gamesMapDto.put(gameDto.getGameExternalId(), gameDto);
            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/end/{gameId}")
    public @ResponseBody
    ResponseEntity<?> end(@PathVariable(value = "gameId") String gameId) {
        // logger.debug("end: {}", gameId);

        ClassificationGame game = FenixFramework.getDomainObject(gameId);
        game.finish();
        String usernameWinner = game.getClassificationGameParticipantSet().stream()
                .filter(ClassificationGameParticipant::getWinner).findFirst().get().getPlayer().getUser();

        List<Object> response = new ArrayList<>();
        response.add(usernameWinner);
        return new ResponseEntity<>(response.toArray(), HttpStatus.OK);
    }

    @GetMapping("/leaderboard")
    public @ResponseBody
    ResponseEntity<?> getLeaderboard() {
        // logger.debug("getLeaderboard: {}", gameId);

        List<Object> response = new ArrayList<>();

        Map<String, Double> overallLeaderboard = ClassificationModule.getInstance().getOverallLeaderboard();
        List<String> users = overallLeaderboard.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).map(Map.Entry::getKey)
                .collect(Collectors.toList());
        List<Double> scores = overallLeaderboard.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).map(Map.Entry::getValue)
                .collect(Collectors.toList());

        response.add(users);
        response.add(scores);

        return new ResponseEntity<>(response.toArray(), HttpStatus.OK);
    }

    // ------------- WebSocket Methods ------------- //

    @MessageMapping("/{gameId}/connect")
    public @ResponseBody
    void handleConnect(@Payload Map<String, String> payload) {
        // logger.debug(" handleConnect keys: {}, value: {}", payload.keySet(),
        // payload.values());

        String userId = payload.get("userId");
        String gameId = payload.get("gameId");

        createGameParticipant(gameId, userId);
    }

    @MessageMapping("/{gameId}/tags")
    @SendTo("/topic/ldod-game/{gameId}/tags")
    public @ResponseBody
    void handleTags(@Payload Map<String, String> payload) {
        // logger.debug("handleTags keys: {}, values: {}", payload.keySet(),
        // payload.values());

        String gameId = payload.get("gameId");
        String authorId = payload.get("authorId");
        String tag = payload.get("msg");
        Object number = payload.get("paragraph");
        int finalNumber = Integer.parseInt((String) number);

        changeGameState(gameId, ClassificationGame.ClassificationGameState.TAGGING);
        saveTag(gameId, authorId, tag, finalNumber);

        this.broker.convertAndSend("/topic/ldod-game/" + gameId + "/tags", payload.values());
    }

    @Atomic(mode = TxMode.READ)
    @MessageMapping("/{gameId}/votes")
    @SendTo("/topic/ldod-game/{gameId}/votes")
    public @ResponseBody
    void handleVotes(@Payload Map<String, String> payload) {
        // logger.debug("handleVotes keys: {}, values: {}", payload.keySet(),
        // payload.values());

        String gameId = payload.get("gameId");
        String voterId = payload.get("voterId");
        String tagMsg = payload.get("msg");
        Object vote = payload.get("vote");
        double finalVote = Double.parseDouble((String) vote);
        Object number = payload.get("paragraph");
        int finalNumber = Integer.parseInt((String) number);
        double v = 0;

        changeGameState(gameId, ClassificationGame.ClassificationGameState.VOTING);
        v = saveVote(gameId, voterId, tagMsg, finalVote, finalNumber);

        payload.remove("paragraph");
        String currentTopTag = getCurrentTagWinner(gameId);
        String currentWinner = getCurrentParticipantWinner(gameId);

        payload.put("vote", String.valueOf(v));
        payload.put("top", currentTopTag);
        payload.put("winner", currentWinner);

        this.broker.convertAndSend("/topic/ldod-game/" + gameId + "/votes", payload.values());
    }

    @Atomic(mode = TxMode.READ)
    @MessageMapping("/{gameId}/review")
    @SendTo("/topic/ldod-game/{gameId}/review")
    public @ResponseBody
    void handleReview(@Payload Map<String, String> payload) {
        // logger.debug("handleReview keys: {}, values: {}", payload.keySet(),
        // payload.values());

        String gameId = payload.get("gameId");
        Object limit = payload.get("limit");
        int numberOfParagraphs = Integer.parseInt((String) limit);
        numberOfParagraphs = numberOfParagraphs == 1 ? 2 : numberOfParagraphs;

        changeGameState(gameId, ClassificationGame.ClassificationGameState.REVIEWING);
        Set<String> topTags = getTopTags(gameId, numberOfParagraphs);

        List<Map<String, String>> response = new ArrayList<>();

        for (String topTag : topTags) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("tag", topTag);
            map.put("vote", String.valueOf(0));
            response.add(map);
        }

        String currentTopTag = getCurrentTagWinner(gameId);
        String currentWinner = getCurrentParticipantWinner(gameId);

        payload.put("top", currentTopTag);
        payload.put("winner", currentWinner);

        Map<String, String> map = new LinkedHashMap<>();
        map.put(currentWinner, currentTopTag);
        response.add(map);

        this.broker.convertAndSend("/topic/ldod-game/" + gameId + "/review", response);
    }

    @MessageMapping("/{gameId}/sync")
    public void syncGame(@Payload Map<String, String> payload) {
        // logger.debug("syncGame: {}", payload.values());
        String gameId = payload.get("gameId");
        changeToSync(gameId);
    }

    // ------------- DB Transactions ------------- //

    @Atomic(mode = TxMode.WRITE)
    private void createGameParticipant(String gameId, String userId) {
        ClassificationGame game = FenixFramework.getDomainObject(gameId);
        game.addParticipant(userId);
    }

    @Atomic(mode = TxMode.WRITE)
    private void saveTag(String gameId, String userId, String tag, int number) {
        ClassificationGame game = FenixFramework.getDomainObject(gameId);
        ClassificationGameParticipant participant = game.getParticipant(userId);
        ClassificationGameRound round = new ClassificationGameRound();

        round.setNumber(number);
        round.setRound(1);
        round.setTag(tag);
        round.setVote(1);
        round.setClassificationGameParticipant(participant);
        round.setTime(DateTime.now());
        participant.setScore(participant.getScore() + ClassificationGame.SUBMIT_TAG);
    }

    @Atomic(mode = TxMode.WRITE)
    private double saveVote(String gameId, String userId, String tag, double vote, int paragraph) {
        ClassificationGame game = FenixFramework.getDomainObject(gameId);
        ClassificationGameParticipant participant = game.getParticipant(userId);
        ClassificationGameRound round = new ClassificationGameRound();

        round.setNumber(paragraph);

        if (getGameState(gameId).equals(ClassificationGame.ClassificationGameState.REVIEWING)) {
            round.setRound(ClassificationGame.VOTING_FINAL_ROUND);
            round.setVote(game.getVotesForTagInFinalRound(tag) + vote);
        } else if (getGameState(gameId).equals(ClassificationGame.ClassificationGameState.VOTING)) {
            round.setRound(ClassificationGame.VOTING_PARAGRAPH_ROUND);
            round.setVote(game.getVotesForTagInParagraph(tag, paragraph) + vote);
        }

        round.setTag(tag);

        // Vote changed in review, makes participant receive a -1 penalty
        if (vote < 0) {
            participant.setScore(participant.getScore() + ClassificationGame.VOTE_CHANGE);
        }

        round.setClassificationGameParticipant(participant);
        round.setTime(DateTime.now());

        return round.getVote();
    }

    @Atomic(mode = TxMode.WRITE)
    private void changeToSync(String gameId) {
        ClassificationGame game = FenixFramework.getDomainObject(gameId);
        game.setSync(true);
    }

    @Atomic(mode = TxMode.WRITE)
    private void changeGameState(String gameId, ClassificationGame.ClassificationGameState state) {
        ClassificationGame game = FenixFramework.getDomainObject(gameId);

        // If we reached reviewing state it should not switch to voting again
        if (game.getState().equals(ClassificationGame.ClassificationGameState.REVIEWING)) {
            return;
        }
        game.setState(state);
    }

    @Atomic(mode = TxMode.READ)
    private Set<String> getTopTags(String gameId, int numberOfParagraphs) {
        ClassificationGame game = FenixFramework.getDomainObject(gameId);
        return game.getCurrentTopTags(numberOfParagraphs);
    }

    @Atomic(mode = TxMode.READ)
    private String getCurrentTagWinner(String gameId) {
        ClassificationGame game = FenixFramework.getDomainObject(gameId);
        return game.getCurrentTagWinner();
    }

    @Atomic(mode = TxMode.READ)
    private String getCurrentParticipantWinner(String gameId) {
        ClassificationGame game = FenixFramework.getDomainObject(gameId);
        return game.getCurrentParticipantWinner().getPlayer().getUser();
    }

    @Atomic(mode = TxMode.READ)
    private ClassificationGame.ClassificationGameState getGameState(String gameId) {
        ClassificationGame game = FenixFramework.getDomainObject(gameId);
        return game.getState();
    }
}
