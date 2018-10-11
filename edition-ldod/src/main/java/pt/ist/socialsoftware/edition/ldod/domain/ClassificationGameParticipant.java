package pt.ist.socialsoftware.edition.ldod.domain;

public class ClassificationGameParticipant extends ClassificationGameParticipant_Base {

    public ClassificationGameParticipant(ClassificationGame classificationGame, LdoDUser user) {
        setClassificationGame(classificationGame);

        if (user.getPlayer() == null){
            new Player(user);
        }

        setPlayer(user.getPlayer());
    }

    public void remove() {
        setClassificationGame(null);
        setPlayer(null);

        getClassificationGameRoundSet().stream().forEach(g -> g.remove());

        deleteDomainObject();
    }
}
