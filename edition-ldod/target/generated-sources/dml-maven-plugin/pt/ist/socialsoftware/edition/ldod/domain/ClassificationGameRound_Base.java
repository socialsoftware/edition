package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class ClassificationGameRound_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> role$$classificationGameParticipant = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant getValue(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound o1) {
            return ((ClassificationGameRound_Base.DO_State)o1.get$obj$state(false)).classificationGameParticipant;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound o1, pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant o2) {
            ((ClassificationGameRound_Base.DO_State)o1.get$obj$state(true)).classificationGameParticipant = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant.role$$classificationGameRound;
        }
        
    };
    
    private final static class ClassificationGameParticipantInGameRounds {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant>(role$$classificationGameParticipant, "ClassificationGameParticipantInGameRounds");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> getRelationClassificationGameParticipantInGameRounds() {
        return ClassificationGameParticipantInGameRounds.relation;
    }
    
    static {
        ClassificationGameParticipantInGameRounds.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound.ClassificationGameParticipantInGameRounds");
    }
    
    // Slots
    
    // Role Slots
    
    // Init Instance
    
    private void initInstance() {
        init$Instance(true);
    }
    
    @Override
    protected void init$Instance(boolean allocateOnly) {
        super.init$Instance(allocateOnly);
        
    }
    
    // Constructors
    protected  ClassificationGameRound_Base() {
        super();
    }
    
    // Getters and Setters
    
    public int getNumber() {
        return ((DO_State)this.get$obj$state(false)).number;
    }
    
    public void setNumber(int number) {
        ((DO_State)this.get$obj$state(true)).number = number;
    }
    
    private int get$number() {
        int value = ((DO_State)this.get$obj$state(false)).number;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$number(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).number = (int)(value);
    }
    
    public int getRound() {
        return ((DO_State)this.get$obj$state(false)).round;
    }
    
    public void setRound(int round) {
        ((DO_State)this.get$obj$state(true)).round = round;
    }
    
    private int get$round() {
        int value = ((DO_State)this.get$obj$state(false)).round;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$round(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).round = (int)(value);
    }
    
    public java.lang.String getTag() {
        return ((DO_State)this.get$obj$state(false)).tag;
    }
    
    public void setTag(java.lang.String tag) {
        ((DO_State)this.get$obj$state(true)).tag = tag;
    }
    
    private java.lang.String get$tag() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).tag;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$tag(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).tag = (java.lang.String)((value == null) ? null : value);
    }
    
    public double getVote() {
        return ((DO_State)this.get$obj$state(false)).vote;
    }
    
    public void setVote(double vote) {
        ((DO_State)this.get$obj$state(true)).vote = vote;
    }
    
    private double get$vote() {
        double value = ((DO_State)this.get$obj$state(false)).vote;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueFordouble(value);
    }
    
    private final void set$vote(double value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).vote = (double)(value);
    }
    
    public org.joda.time.DateTime getTime() {
        return ((DO_State)this.get$obj$state(false)).time;
    }
    
    public void setTime(org.joda.time.DateTime time) {
        ((DO_State)this.get$obj$state(true)).time = time;
    }
    
    private java.sql.Timestamp get$time() {
        org.joda.time.DateTime value = ((DO_State)this.get$obj$state(false)).time;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForDateTime(value);
    }
    
    private final void set$time(org.joda.time.DateTime value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).time = (org.joda.time.DateTime)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant getClassificationGameParticipant() {
        return ((DO_State)this.get$obj$state(false)).classificationGameParticipant;
    }
    
    public void setClassificationGameParticipant(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant classificationGameParticipant) {
        getRelationClassificationGameParticipantInGameRounds().add((pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound)this, classificationGameParticipant);
    }
    
    private java.lang.Long get$oidClassificationGameParticipant() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).classificationGameParticipant;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfClassificationGameParticipant() {
        if (getClassificationGameParticipant() == null) return false;
        return true;
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.classificationGameParticipant != null) handleAttemptToDeleteConnectedObject("ClassificationGameParticipant");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$number(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "NUMBER"), state);
        set$round(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "ROUND"), state);
        set$tag(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "TAG"), state);
        set$vote(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readdouble(rs, "VOTE"), state);
        set$time(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDateTime(rs, "TIME"), state);
        castedState.classificationGameParticipant = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_CLASSIFICATION_GAME_PARTICIPANT");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private int number;
        private int round;
        private java.lang.String tag;
        private double vote;
        private org.joda.time.DateTime time;
        private pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant classificationGameParticipant;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.number = this.number;
            newCasted.round = this.round;
            newCasted.tag = this.tag;
            newCasted.vote = this.vote;
            newCasted.time = this.time;
            newCasted.classificationGameParticipant = this.classificationGameParticipant;
            
        }
        
    }
    
}
