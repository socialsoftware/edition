package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class ClassificationGameParticipant_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound> role$$classificationGameRound = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound> getSet(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant o1) {
            return ((ClassificationGameParticipant_Base)o1).get$rl$classificationGameRound();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound.role$$classificationGameParticipant;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> role$$classificationGame = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame getValue(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant o1) {
            return ((ClassificationGameParticipant_Base.DO_State)o1.get$obj$state(false)).classificationGame;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant o1, pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame o2) {
            ((ClassificationGameParticipant_Base.DO_State)o1.get$obj$state(true)).classificationGame = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame.role$$classificationGameParticipant;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant,pt.ist.socialsoftware.edition.ldod.domain.Player> role$$player = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant,pt.ist.socialsoftware.edition.ldod.domain.Player>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Player getValue(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant o1) {
            return ((ClassificationGameParticipant_Base.DO_State)o1.get$obj$state(false)).player;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant o1, pt.ist.socialsoftware.edition.ldod.domain.Player o2) {
            ((ClassificationGameParticipant_Base.DO_State)o1.get$obj$state(true)).player = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Player,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Player.role$$classificationGameParticipant;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> getRelationClassificationGameParticipantInGameRounds() {
        return pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound.getRelationClassificationGameParticipantInGameRounds();
    }
    
    private final static class ClassificationGameParticipantInGame {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame>(role$$classificationGame, "ClassificationGameParticipantInGame");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getRelationClassificationGameParticipantInGame() {
        return ClassificationGameParticipantInGame.relation;
    }
    
    static {
        ClassificationGameParticipantInGame.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant.ClassificationGameParticipantInGame");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Player,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> getRelationClassificationGameParticipantIsPlayer() {
        return pt.ist.socialsoftware.edition.ldod.domain.Player.getRelationClassificationGameParticipantIsPlayer();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound> get$rl$classificationGameRound() {
        return get$$relationList("classificationGameRound", getRelationClassificationGameParticipantInGameRounds().getInverseRelation());
        
    }
    
    // Init Instance
    
    private void initInstance() {
        init$Instance(true);
    }
    
    @Override
    protected void init$Instance(boolean allocateOnly) {
        super.init$Instance(allocateOnly);
        
    }
    
    // Constructors
    protected  ClassificationGameParticipant_Base() {
        super();
    }
    
    // Getters and Setters
    
    public boolean getWinner() {
        return ((DO_State)this.get$obj$state(false)).winner;
    }
    
    public void setWinner(boolean winner) {
        ((DO_State)this.get$obj$state(true)).winner = winner;
    }
    
    private boolean get$winner() {
        boolean value = ((DO_State)this.get$obj$state(false)).winner;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForboolean(value);
    }
    
    private final void set$winner(boolean value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).winner = (boolean)(value);
    }
    
    public double getScore() {
        return ((DO_State)this.get$obj$state(false)).score;
    }
    
    public void setScore(double score) {
        ((DO_State)this.get$obj$state(true)).score = score;
    }
    
    private double get$score() {
        double value = ((DO_State)this.get$obj$state(false)).score;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueFordouble(value);
    }
    
    private final void set$score(double value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).score = (double)(value);
    }
    
    // Role Methods
    
    public void addClassificationGameRound(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound classificationGameRound) {
        getRelationClassificationGameParticipantInGameRounds().add(classificationGameRound, (pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant)this);
    }
    
    public void removeClassificationGameRound(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound classificationGameRound) {
        getRelationClassificationGameParticipantInGameRounds().remove(classificationGameRound, (pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound> getClassificationGameRoundSet() {
        return get$rl$classificationGameRound();
    }
    
    public void set$classificationGameRound(OJBFunctionalSetWrapper classificationGameRound) {
        get$rl$classificationGameRound().setFromOJB(this, "classificationGameRound", classificationGameRound);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound> getClassificationGameRound() {
        return getClassificationGameRoundSet();
    }
    
    @Deprecated
    public int getClassificationGameRoundCount() {
        return getClassificationGameRoundSet().size();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame getClassificationGame() {
        return ((DO_State)this.get$obj$state(false)).classificationGame;
    }
    
    public void setClassificationGame(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame classificationGame) {
        getRelationClassificationGameParticipantInGame().add((pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant)this, classificationGame);
    }
    
    private java.lang.Long get$oidClassificationGame() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).classificationGame;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfClassificationGame() {
        if (getClassificationGame() == null) return false;
        return true;
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.Player getPlayer() {
        return ((DO_State)this.get$obj$state(false)).player;
    }
    
    public void setPlayer(pt.ist.socialsoftware.edition.ldod.domain.Player player) {
        getRelationClassificationGameParticipantIsPlayer().add(player, (pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant)this);
    }
    
    private java.lang.Long get$oidPlayer() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).player;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfPlayer() {
        if (getPlayer() == null) return false;
        return true;
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$classificationGameRound().size() > 0) handleAttemptToDeleteConnectedObject("ClassificationGameRound");
        if (castedState.classificationGame != null) handleAttemptToDeleteConnectedObject("ClassificationGame");
        if (castedState.player != null) handleAttemptToDeleteConnectedObject("Player");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$winner(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readboolean(rs, "WINNER"), state);
        set$score(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readdouble(rs, "SCORE"), state);
        castedState.classificationGame = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_CLASSIFICATION_GAME");
        castedState.player = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_PLAYER");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("classificationGameRound")) return getRelationClassificationGameParticipantInGameRounds().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("classificationGameRound", getRelationClassificationGameParticipantInGameRounds().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private boolean winner;
        private double score;
        private pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame classificationGame;
        private pt.ist.socialsoftware.edition.ldod.domain.Player player;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.winner = this.winner;
            newCasted.score = this.score;
            newCasted.classificationGame = this.classificationGame;
            newCasted.player = this.player;
            
        }
        
    }
    
}
