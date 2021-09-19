package pt.ist.socialsoftware.edition.game.domain;



public class ClassificationGameParticipant extends ClassificationGameParticipant_Base {

    public ClassificationGameParticipant(ClassificationGame classificationGame, String user) {
        setClassificationGame(classificationGame);

        Player player = ClassificationModule.getInstance().getPlayerByUsername(user);

        if (player == null) {
            player = new Player(user);
        }

        setPlayer(player);
    }

    public void remove() {
        setClassificationGame(null);
        setPlayer(null);

        getClassificationGameRoundSet().stream().forEach(g -> g.remove());

        deleteDomainObject();
    }
}
