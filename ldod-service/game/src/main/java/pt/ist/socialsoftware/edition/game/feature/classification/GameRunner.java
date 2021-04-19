package pt.ist.socialsoftware.edition.game.feature.classification;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.game.domain.ClassificationGame;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Scope("thread")
public class GameRunner implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(GameRunner.class);
    private String gameId;
    private DateTime startTime;
    private Set<String> playersInGame;

    @Autowired
    private final SimpMessagingTemplate broker;

    @Autowired
    public GameRunner(SimpMessagingTemplate broker) {
        this.broker = broker;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    @Override
    public void run() {
        if (canOpenGame()) {
            while (Instant.now().isBefore(this.startTime.plusMinutes(1))) {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (canGameStart()) {
                logger.debug("running game {}", this.gameId);
                startGame();
                while (!hasGameEnded()) {
                    if (canGameContinue()) {
                        try {
                            Thread.sleep(600);
                            continueGame();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                abortGame();
            }
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private synchronized boolean canOpenGame() {
        ClassificationGame game = FenixFramework.getDomainObject(this.gameId);

        logger.debug("running game {}", game);

        this.startTime = game.getDateTime();
        if (game.getState().equals(ClassificationGame.ClassificationGameState.CREATED)) {
            game.setState(ClassificationGame.ClassificationGameState.OPEN);
            return true;
        }
        return false;
    }

    @Atomic(mode = TxMode.READ)
    private boolean canGameStart() {
        ClassificationGame game = FenixFramework.getDomainObject(this.gameId);

        // TODO: inform if the game cannot start
        logger.debug("canGameStart size: {}", game.getClassificationGameParticipantSet().size());
        return game.getClassificationGameParticipantSet().size() >= 2;
    }

    @Atomic(mode = TxMode.WRITE)
    private void startGame() {
        ClassificationGame game = FenixFramework.getDomainObject(this.gameId);
        game.setState(ClassificationGame.ClassificationGameState.STARTED);

        this.playersInGame = game.getClassificationGameParticipantSet().stream().map(p -> p.getPlayer().getUser()).collect(Collectors.toSet());

        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("currentUsers", String.valueOf(this.playersInGame.size()));
        payload.put("command", "ready");
        this.broker.convertAndSend("/topic/ldod-game/" + this.gameId + "/register", payload.values());

    }

    @Atomic(mode = TxMode.WRITE)
    private void abortGame() {
        ClassificationGame game = FenixFramework.getDomainObject(this.gameId);
        game.setState(ClassificationGame.ClassificationGameState.ABORTED);

        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("currentUsers", String.valueOf(0));
        payload.put("command", "aborted");
        this.broker.convertAndSend("/topic/ldod-game/" + this.gameId + "/register", payload.values());
    }

    @Atomic(mode = TxMode.READ)
    private boolean canGameContinue() {
        ClassificationGame game = FenixFramework.getDomainObject(this.gameId);
        return game.getSync();
    }

    @Atomic(mode = TxMode.WRITE)
    private void continueGame() {
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("command", "continue");
        // Game is continuing so we change to not syncing
        ClassificationGame game = FenixFramework.getDomainObject(this.gameId);
        game.setSync(false);
        this.broker.convertAndSend("/topic/ldod-game/" + this.gameId + "/sync", payload.values());
    }

    @Atomic(mode = TxMode.READ)
    private boolean hasGameEnded() {
        ClassificationGame game = FenixFramework.getDomainObject(this.gameId);
        return game.getState().equals(ClassificationGame.ClassificationGameState.FINISHED);
    }
}
