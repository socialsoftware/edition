package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Player_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Player,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> role$$user = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Player,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoDUser getValue(pt.ist.socialsoftware.edition.ldod.domain.Player o1) {
            return ((Player_Base.DO_State)o1.get$obj$state(false)).user;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Player o1, pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o2) {
            ((Player_Base.DO_State)o1.get$obj$state(true)).user = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Player> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.role$$player;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Player,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> role$$classificationGameParticipant = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Player,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> getSet(pt.ist.socialsoftware.edition.ldod.domain.Player o1) {
            return ((Player_Base)o1).get$rl$classificationGameParticipant();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant,pt.ist.socialsoftware.edition.ldod.domain.Player> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant.role$$player;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Player> getRelationLdodUserIsPlayer() {
        return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.getRelationLdodUserIsPlayer();
    }
    
    private final static class ClassificationGameParticipantIsPlayer {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Player,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Player,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant>(role$$classificationGameParticipant, "ClassificationGameParticipantIsPlayer");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Player,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> getRelationClassificationGameParticipantIsPlayer() {
        return ClassificationGameParticipantIsPlayer.relation;
    }
    
    static {
        ClassificationGameParticipantIsPlayer.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Player.ClassificationGameParticipantIsPlayer");
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Player,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> get$rl$classificationGameParticipant() {
        return get$$relationList("classificationGameParticipant", getRelationClassificationGameParticipantIsPlayer());
        
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
    protected  Player_Base() {
        super();
    }
    
    // Getters and Setters
    
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
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoDUser getUser() {
        return ((DO_State)this.get$obj$state(false)).user;
    }
    
    public void setUser(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser user) {
        getRelationLdodUserIsPlayer().add(user, (pt.ist.socialsoftware.edition.ldod.domain.Player)this);
    }
    
    private java.lang.Long get$oidUser() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).user;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfUser() {
        if (getUser() == null) return false;
        return true;
    }
    
    public void addClassificationGameParticipant(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant classificationGameParticipant) {
        getRelationClassificationGameParticipantIsPlayer().add((pt.ist.socialsoftware.edition.ldod.domain.Player)this, classificationGameParticipant);
    }
    
    public void removeClassificationGameParticipant(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant classificationGameParticipant) {
        getRelationClassificationGameParticipantIsPlayer().remove((pt.ist.socialsoftware.edition.ldod.domain.Player)this, classificationGameParticipant);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> getClassificationGameParticipantSet() {
        return get$rl$classificationGameParticipant();
    }
    
    public void set$classificationGameParticipant(OJBFunctionalSetWrapper classificationGameParticipant) {
        get$rl$classificationGameParticipant().setFromOJB(this, "classificationGameParticipant", classificationGameParticipant);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> getClassificationGameParticipant() {
        return getClassificationGameParticipantSet();
    }
    
    @Deprecated
    public int getClassificationGameParticipantCount() {
        return getClassificationGameParticipantSet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.user != null) handleAttemptToDeleteConnectedObject("User");
        if (get$rl$classificationGameParticipant().size() > 0) handleAttemptToDeleteConnectedObject("ClassificationGameParticipant");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$score(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readdouble(rs, "SCORE"), state);
        castedState.user = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_USER");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("classificationGameParticipant")) return getRelationClassificationGameParticipantIsPlayer();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("classificationGameParticipant", getRelationClassificationGameParticipantIsPlayer());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private double score;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoDUser user;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.score = this.score;
            newCasted.user = this.user;
            
        }
        
    }
    
}
