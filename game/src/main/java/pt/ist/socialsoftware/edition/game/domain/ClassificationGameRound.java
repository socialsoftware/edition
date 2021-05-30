package pt.ist.socialsoftware.edition.game.domain;

public class ClassificationGameRound extends ClassificationGameRound_Base {
    
    public ClassificationGameRound() {
        super();
    }

    public void remove() {
        setClassificationGameParticipant(null);
        deleteDomainObject();
    }
    
}
