package pt.ist.socialsoftware.edition.game.api;



import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.game.api.dtoc.ClassificationGameDto;
import pt.ist.socialsoftware.edition.game.api.dtoc.ClassificationGameParticipantDto;
import pt.ist.socialsoftware.edition.game.api.dtoc.ClassificationVirtualGameDto;
import pt.ist.socialsoftware.edition.game.api.dtoc.PlayerDto;
import pt.ist.socialsoftware.edition.game.api.virtualDto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.game.api.virtualDto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.game.domain.ClassificationGame;
import pt.ist.socialsoftware.edition.game.domain.ClassificationGameParticipant;
import pt.ist.socialsoftware.edition.game.domain.ClassificationModule;
import pt.ist.socialsoftware.edition.game.domain.Player;
import pt.ist.socialsoftware.edition.game.feature.classification.GameRunner;
import pt.ist.socialsoftware.edition.game.feature.classification.inout.GameXMLImport;
import pt.ist.socialsoftware.edition.game.utils.GameBootstrap;


import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GameProvidesInterface {

    @Autowired
    GameRunner gameRunner;

    public Set<ClassificationGameDto> getClassificationGames() {
        return ClassificationModule.getInstance().getClassificationGameSet().stream()
                .map(ClassificationGameDto::new).collect(Collectors.toSet());
    }

    public int getOverallUserPosition(String username) {
        return ClassificationModule.getInstance().getOverallUserPosition(username);
    }

    public PlayerDto getPlayerByUsername(String username) {
        Player player = ClassificationModule.getInstance().getPlayerByUsername(username);
        if (player != null){
            return new PlayerDto(player);
        }

        return null;
    }

    public Set<ClassificationGameDto> getClassificationGamesForPlayer(String username) {
        Player player = ClassificationModule.getInstance().getPlayerByUsername(username);
        if (player != null){
            return player.getClassificationGameParticipantSet().stream().map(ClassificationGameParticipant::getClassificationGame)
                    .map(ClassificationGameDto::new).collect(Collectors.toSet());
        }

        return new HashSet<>();
    }

    public Set<ClassificationGameDto> getClassificationGamesForEdition(String acronym) {
        return  ClassificationModule.getInstance().getClassificationGamesForEdition(acronym)
                .stream().sorted(Comparator.comparing(ClassificationGame::getDateTime))
                .map(ClassificationGameDto::new).collect(Collectors.toSet());
    }


    public void createClassificationGame(VirtualEditionDto virtualEdition, String description, DateTime parse, VirtualEditionInterDto inter, String authenticatedUser) {
        ClassificationModule.createClassificationGame(virtualEdition, description, parse, inter, authenticatedUser);
    }

    public void importGamesFromTEI(InputStream inputStream) {
        GameXMLImport game = new GameXMLImport();
        game.importGamesFromTEI(inputStream);
    }

    public ClassificationGameDto getClassificationGameByExternalId(String externalId) {
        DomainObject object = FenixFramework.getDomainObject(externalId);

        if (object instanceof ClassificationGame) {
            return new ClassificationGameDto((ClassificationGame) object);
        }
        return null;
    }

    public void removeClassificationGame(String externalId) {
        DomainObject object = FenixFramework.getDomainObject(externalId);
        if (object instanceof ClassificationGame) {
            ((ClassificationGame) object).remove();
        }
    }

    public List<ClassificationGameParticipantDto> getClassificationGameParticipantByExternalId(String externalId) {
        DomainObject object = FenixFramework.getDomainObject(externalId);
        if (object instanceof ClassificationGame) {
           return  ((ClassificationGame) object).getClassificationGameParticipantSet().stream()
                   .sorted(Comparator.comparing(ClassificationGameParticipant::getScore).reversed())
                   .map(ClassificationGameParticipantDto::new).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public void initializeGameModule() {
        GameBootstrap.initializeGameModule();
    }

    public void startGameRunner(String id) {
        this.gameRunner.setGameId(id);
        new Thread(this.gameRunner).start();
    }

    public List<String> getGamesForScheduledTasks(DateTime now) {
        return ClassificationModule.getInstance().getClassificationGameSet().stream()
                .filter(g -> g.getState().equals(ClassificationGame.ClassificationGameState.CREATED)
                        && g.getDateTime().isAfter(now) && g.getDateTime().isBefore(now.plusMinutes(2)))
                .sorted(Comparator.comparing(ClassificationGame::getDateTime)).map(g -> g.getExternalId())
                .collect(Collectors.toList());
    }

    public void manageDailyClassificationGames() {
        ClassificationModule.manageDailyClassificationGames(DateTime.now());
    }
}
