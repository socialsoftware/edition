package pt.ist.socialsoftware.edition.ldod.domain;

public class Player extends Player_Base {

    public Player(String user) {
        setClassificationModule(ClassificationModule.getInstance());
        setUser(user);
    }

    public void remove() {
        setClassificationModule(null);
        getClassificationGameParticipantSet().stream().forEach(p -> p.remove());

        deleteDomainObject();
    }

    public long score() {
        return 0;
        //return getClassificationGameSet().stream().filter(g -> g.getTag().getContributor() == getUser()).count();
    }

}
