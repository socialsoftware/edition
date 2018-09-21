package pt.ist.socialsoftware.edition.ldod.game.classification;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

public class GameRunner implements Runnable{
    private String gameId;

    public GameRunner(String gameId) {
        this.gameId = gameId;
    }

    @Override
    public void run() {
        ArrayBlockingQueue<String> clients = ClassificationGame.getGameBlockingMap().get(this.gameId);
        if (canGameStart(this.gameId, clients)) {
            startGame(this.gameId, clients);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private boolean canGameStart(String id, ArrayBlockingQueue<String> clients){
        ClassificationGame game  = FenixFramework.getDomainObject(id);
        if (game.getOpenAnnotation()) {
            return clients != null && clients.size() >= 2;
        }
        else{
            List<String> players = game.getPlayerSet().stream().map(player -> player.getUser().getUsername()).collect
                    (Collectors.toList());
            return clients.containsAll(players);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private void startGame(String id, ArrayBlockingQueue<String> clients) {
        ClassificationGame game  = FenixFramework.getDomainObject(id);
        if (game.getOpenAnnotation()) {
            for (String userId: clients) {
                LdoDUser user = LdoD.getInstance().getUser(userId);
                Player player = user.getPlayer();
                if(player == null){
                    player = new Player(user);

                }
                game.addPlayer(player);
            }
        }
        game.setState(ClassificationGame.ClassificationGameState.STARTED);
        game.setStarted(true);
    }

}
