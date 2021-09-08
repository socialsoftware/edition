package pt.ist.socialsoftware.edition.game.api;



import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.game.api.dtoc.ClassificationGameDto;
import pt.ist.socialsoftware.edition.game.api.dtoc.ClassificationGameParticipantDto;
import pt.ist.socialsoftware.edition.game.api.dtoc.ClassificationVirtualGameDto;
import pt.ist.socialsoftware.edition.game.api.dtoc.PlayerDto;
import pt.ist.socialsoftware.edition.game.api.dtoc.wrapper.CreateGameWrapper;
import pt.ist.socialsoftware.edition.game.domain.*;
import pt.ist.socialsoftware.edition.game.feature.classification.GameRunner;
import pt.ist.socialsoftware.edition.game.feature.classification.inout.GameXMLImport;
import pt.ist.socialsoftware.edition.game.utils.GameBootstrap;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Component
public class GameProvidesInterface {
    private static final Logger logger = LoggerFactory.getLogger(GameProvidesInterface.class);

    
    @Autowired
    GameRunner gameRunner;

    @GetMapping("/classificationGames")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<ClassificationGameDto> getClassificationGames() {
        logger.debug("getClassificationGames");
        return ClassificationModule.getInstance().getClassificationGameSet().stream()
                .map(ClassificationGameDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/overallUserPosition")
    @Atomic(mode = Atomic.TxMode.READ)
    public int getOverallUserPosition(@RequestParam(name = "username") String username) {
        logger.debug("getOverallUserPosition: " + username);
        return ClassificationModule.getInstance().getOverallUserPosition(username);
    }

    @GetMapping("/player/{username}")
    @Atomic(mode = Atomic.TxMode.READ)
    public PlayerDto getPlayerByUsername(@PathVariable("username") String username) {
        logger.debug("getPlayerByUsername: " + username);
        Player player = ClassificationModule.getInstance().getPlayerByUsername(username);
        if (player != null){
            return new PlayerDto(player);
        }

        return null;
    }

    @GetMapping("/player/{username}/classificationGames")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<ClassificationGameDto> getClassificationGamesForPlayer(@PathVariable("username") String username) {
        logger.debug("getClassificationGamesForPlayer: " + username);
        Player player = ClassificationModule.getInstance().getPlayerByUsername(username);
        if (player != null){
            return player.getClassificationGameParticipantSet().stream().map(ClassificationGameParticipant::getClassificationGame)
                    .map(ClassificationGameDto::new).collect(Collectors.toSet());
        }

        return new HashSet<>();
    }

    @GetMapping("/classificationGamesForEdition")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<ClassificationGameDto> getClassificationGamesForEdition(@RequestParam(name = "acronym") String acronym) {
        logger.debug("getClassificationGamesForEdition: " + acronym);
        return  ClassificationModule.getInstance().getClassificationGamesForEdition(acronym)
                .stream().sorted(Comparator.comparing(ClassificationGame::getDateTime))
                .map(ClassificationGameDto::new).collect(Collectors.toSet());
    }

    @PostMapping("/createClassificationGame")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createClassificationGame(@RequestBody CreateGameWrapper createGameWrapper) {
        logger.debug("createClassificationGame");
        ClassificationModule.createClassificationGame(createGameWrapper.getVirtualEditionDto(), createGameWrapper.getDescription(), createGameWrapper.getParse(), createGameWrapper.getInter(), createGameWrapper.getAuthenticatedUser());
    }

    @PostMapping("/importGamesFromTEI")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void importGamesFromTEI(@RequestBody byte[] inputStream) {
        logger.debug("importGamesFromTEI");
        GameXMLImport game = new GameXMLImport();
        game.importGamesFromTEI(new ByteArrayInputStream(inputStream));
    }

    @GetMapping("/classificationGame/ext/{externalId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public ClassificationGameDto getClassificationGameByExternalId(@PathVariable("externalId") String externalId) {
        logger.debug("getClassificationGameByExternalId: " + externalId);
        DomainObject object = FenixFramework.getDomainObject(externalId);

        if (object instanceof ClassificationGame) {
            return new ClassificationGameDto((ClassificationGame) object);
        }
        return null;
    }

    @PostMapping("/removeClassificationGame")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeClassificationGame(@RequestParam(name = "externalId") String externalId) {
        logger.debug("removeClassificationGame: " + externalId);
        DomainObject object = FenixFramework.getDomainObject(externalId);
        if (object instanceof ClassificationGame) {
            ((ClassificationGame) object).remove();
        }
    }

    @GetMapping("/classificationGameParticipant/ext/{externalId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<ClassificationGameParticipantDto> getClassificationGameParticipantByExternalId(@PathVariable("externalId") String externalId) {
        logger.debug("getClassificationGameParticipantByExternalId: " + externalId);
        DomainObject object = FenixFramework.getDomainObject(externalId);
        if (object instanceof ClassificationGame) {
           return  ((ClassificationGame) object).getClassificationGameParticipantSet().stream()
                   .sorted(Comparator.comparing(ClassificationGameParticipant::getScore).reversed())
                   .map(ClassificationGameParticipantDto::new).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @PostMapping("/initializeGameModule")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void initializeGameModule() {
        logger.debug("initializeGameModule");
        GameBootstrap.initializeGameModule();
    }


    @PostMapping("/startGameRunner")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void startGameRunner(@RequestParam(name = "id") String id) {
        logger.debug("startGameRunner: " + id);
        this.gameRunner.setGameId(id);
        new Thread(this.gameRunner).start();
    }

    @GetMapping("/getGamesForScheduledTasks")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<String> getGamesForScheduledTasks() {
        DateTime now = DateTime.now();
        logger.debug("getGamesForScheduledTasks: " + now);
        return ClassificationModule.getInstance().getClassificationGameSet().stream()
                .filter(g -> g.getState().equals(ClassificationGame.ClassificationGameState.CREATED)
                        && g.getDateTime().isAfter(now) && g.getDateTime().isBefore(now.plusMinutes(2)))
                .sorted(Comparator.comparing(ClassificationGame::getDateTime)).map(g -> g.getExternalId())
                .collect(Collectors.toList());
    }

    @PostMapping("/manageDailyClassificationGames")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void manageDailyClassificationGames() {
        logger.debug("manageDailyClassificationGames");
        ClassificationModule.manageDailyClassificationGames(DateTime.now());
    }

    // Test Calls

    @PostMapping("/classificationGame/{externalId}/addParticipant")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void addParticipantToGame(@PathVariable("externalId") String externalId, @RequestParam(name = "username") String username) {
        logger.debug("addParticipantToGame: " + username);
        DomainObject object = FenixFramework.getDomainObject(externalId);
        if (object instanceof ClassificationGame) {
          ((ClassificationGame) object).addParticipant(username);
        }
    }

    @PostMapping("/setTestGameRound")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void setTestGameRound(@RequestParam(name = "acronym") String acronym, @RequestParam(name = "username") String username) {
        logger.debug("setTestGameRound: " + username);

        ClassificationGameParticipant participant = ClassificationModule.getInstance().getClassificationGamesForEdition(acronym).stream().findFirst().get().getParticipant(username);

        ClassificationGameRound round = new ClassificationGameRound();
        round.setNumber(1);
        round.setRound(1);
        round.setTag("XXXX");
        round.setVote(1);
        round.setClassificationGameParticipant(participant);
        round.setTime(DateTime.now());
        participant.setScore(participant.getScore() + ClassificationGame.SUBMIT_TAG);
    }

    @PostMapping("/removeGameModule")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeGameModule() {
        logger.debug("removeGameModule");
        ClassificationModule classificationModule = ClassificationModule.getInstance();
        if (classificationModule != null) {
            classificationModule.remove();
        }
    }

}
