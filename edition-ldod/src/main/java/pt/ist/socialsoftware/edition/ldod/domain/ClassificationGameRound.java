package pt.ist.socialsoftware.edition.ldod.domain;

public class ClassificationGameRound extends ClassificationGameRound_Base {
    
    public ClassificationGameRound() {
        super();
    }

    public void remove() {
        setClassificationGameParticipant(null);
        deleteDomainObject();
    }
    
}
