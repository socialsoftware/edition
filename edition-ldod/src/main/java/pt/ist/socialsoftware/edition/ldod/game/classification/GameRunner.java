package pt.ist.socialsoftware.edition.ldod.game.classification;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;

import java.util.*;
import java.util.stream.Collectors;

@Component
//@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Scope("thread")
public class GameRunner implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(GameRunner.class);
    private String gameId;
    private DateTime startTime;
    private Set<String> playersInGame;

    @Autowired
    private SimpMessagingTemplate broker;

    @Autowired
    public GameRunner(final SimpMessagingTemplate broker) {
        this.broker = broker;
    }

    public void setGameId(String gameId) {
        logger.debug("setGameId: {}", gameId);
        this.gameId = gameId;
    }

    @Override
    public void run() {
        logger.debug("here {}", gameId);
        if (canOpenGame()) {
            while (Instant.now().isBefore(startTime.plusMinutes(1))) {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (canGameStart(this.gameId)) {
                logger.debug("running game {}", this.gameId);
                startGame(this.gameId);
            }
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private synchronized boolean canOpenGame() {
        logger.debug("running game {}", this.gameId);

        ClassificationGame game  = FenixFramework.getDomainObject(this.gameId);

        logger.debug("running game {}", game);

        this.startTime = game.getDateTime();
        if (game.getState().equals(ClassificationGame.ClassificationGameState.CREATED)) {
            game.setState(ClassificationGame.ClassificationGameState.OPEN);
            return true;
        }
        return false;
    }


    @Atomic(mode = TxMode.READ)
    private boolean canGameStart(String id){
        ClassificationGame game  = FenixFramework.getDomainObject(id);

        // TODO: inform if the game cannot start
        logger.debug("canGameStart size: {}", game.getClassificationGameParticipantSet().size());
        return game.getClassificationGameParticipantSet().size() >= 2;
    }

    @Atomic(mode = TxMode.WRITE)
    private void startGame(String id) {
        ClassificationGame game  = FenixFramework.getDomainObject(id);
        game.setState(ClassificationGame.ClassificationGameState.STARTED);

        playersInGame = game.getClassificationGameParticipantSet().stream().map(p -> p.getPlayer().getUser().getUsername()).collect(Collectors.toSet());

        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("currentUsers", String.valueOf(playersInGame.size()));
        payload.put("command", "ready");
        broker.convertAndSend("/topic/config", payload.values());

    }

   /* @MessageMapping("/register")
    @SendTo("/topic/config")
    @Atomic(mode = TxMode.READ)
    public @ResponseBody
    void handleRegister(@Payload Map<String, String> payload) {
        //logger.debug("handleRegister keys: {}, values: {}", payload.keySet(), payload.values());

        String gameId = payload.get("gameId");
        payload.remove("userId");
        payload.remove("gameId");

        Thread t = new Thread(new Runnable() {
            @Atomic(mode = TxMode.READ)
            public void run() {
                while (true) {
                    ClassificationGame game = FenixFramework.getDomainObject(gameId);
                    if (game != null && game.getState().equals(ClassificationGame.ClassificationGameState.STARTED)) {

                        payload.put("currentUsers", String.valueOf(ClassificationGame.getUsers(gameId).size()));
                        payload.put("command", "ready");
                        broker.convertAndSend("/topic/config", payload.values());
                        break;
                    }
                }
            }
        });
        t.start();
    }*/

}
