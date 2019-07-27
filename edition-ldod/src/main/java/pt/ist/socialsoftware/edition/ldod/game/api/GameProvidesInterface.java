package pt.ist.socialsoftware.edition.ldod.game.api;

import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.game.api.dto.ClassificationGameDto;
import pt.ist.socialsoftware.edition.ldod.game.api.dto.PlayerDto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GameProvidesInterface {

    public Set<ClassificationGameDto> getClassificationGames(){
        return ClassificationModule.getInstance().getClassificationGameSet().stream()
                .map(ClassificationGameDto::new).collect(Collectors.toSet());
    }

    public int getOverallUserPosition(String username){
        return ClassificationModule.getInstance().getOverallUserPosition(username);
    }

    public PlayerDto getPlayerByUsername(String username){
        Player player = ClassificationModule.getInstance().getPlayerByUsername(username);

        if (player != null){
            return new PlayerDto(player);
        }

        return null;
    }

    public Set<ClassificationGameDto> getClassificationGamesForPlayer(String username){
        Player player = ClassificationModule.getInstance().getPlayerByUsername(username);

        if (player != null){
            return player.getClassificationGameParticipantSet().stream().map(ClassificationGameParticipant::getClassificationGame)
                    .map(ClassificationGameDto::new).collect(Collectors.toSet());
        }

        return new HashSet<>();
    }
}
