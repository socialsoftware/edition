package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class ExpertEditionInter_Base extends pt.ist.socialsoftware.edition.ldod.domain.FragInter {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter,pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition> role$$expertEdition = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter,pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition getValue(pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter o1) {
            return ((ExpertEditionInter_Base.DO_State)o1.get$obj$state(false)).expertEdition;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter o1, pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition o2) {
            ((ExpertEditionInter_Base.DO_State)o1.get$obj$state(true)).expertEdition = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition,pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition.role$$expertEditionInters;
        }
        
    };
    
    private final static class ExpertEditionHasExpertEditionInters {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter,pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter,pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition>(role$$expertEdition, "ExpertEditionHasExpertEditionInters");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter,pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition> getRelationExpertEditionHasExpertEditionInters() {
        return ExpertEditionHasExpertEditionInters.relation;
    }
    
    static {
        ExpertEditionHasExpertEditionInters.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter.ExpertEditionHasExpertEditionInters");
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
    protected  ExpertEditionInter_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getTitle() {
        return ((DO_State)this.get$obj$state(false)).title;
    }
    
    public void setTitle(java.lang.String title) {
        ((DO_State)this.get$obj$state(true)).title = title;
    }
    
    private java.lang.String get$title() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).title;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$title(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).title = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getVolume() {
        return ((DO_State)this.get$obj$state(false)).volume;
    }
    
    public void setVolume(java.lang.String volume) {
        ((DO_State)this.get$obj$state(true)).volume = volume;
    }
    
    private java.lang.String get$volume() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).volume;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$volume(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).volume = (java.lang.String)((value == null) ? null : value);
    }
    
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
    
    public java.lang.String getSubNumber() {
        return ((DO_State)this.get$obj$state(false)).subNumber;
    }
    
    public void setSubNumber(java.lang.String subNumber) {
        ((DO_State)this.get$obj$state(true)).subNumber = subNumber;
    }
    
    private java.lang.String get$subNumber() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).subNumber;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$subNumber(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).subNumber = (java.lang.String)((value == null) ? null : value);
    }
    
    public int getStartPage() {
        return ((DO_State)this.get$obj$state(false)).startPage;
    }
    
    public void setStartPage(int startPage) {
        ((DO_State)this.get$obj$state(true)).startPage = startPage;
    }
    
    private int get$startPage() {
        int value = ((DO_State)this.get$obj$state(false)).startPage;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$startPage(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).startPage = (int)(value);
    }
    
    public int getEndPage() {
        return ((DO_State)this.get$obj$state(false)).endPage;
    }
    
    public void setEndPage(int endPage) {
        ((DO_State)this.get$obj$state(true)).endPage = endPage;
    }
    
    private int get$endPage() {
        int value = ((DO_State)this.get$obj$state(false)).endPage;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$endPage(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).endPage = (int)(value);
    }
    
    public java.lang.String getNotes() {
        return ((DO_State)this.get$obj$state(false)).notes;
    }
    
    public void setNotes(java.lang.String notes) {
        ((DO_State)this.get$obj$state(true)).notes = notes;
    }
    
    private java.lang.String get$notes() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).notes;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$notes(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).notes = (java.lang.String)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition getExpertEdition() {
        return ((DO_State)this.get$obj$state(false)).expertEdition;
    }
    
    public void setExpertEdition(pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition expertEdition) {
        getRelationExpertEditionHasExpertEditionInters().add((pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter)this, expertEdition);
    }
    
    private java.lang.Long get$oidExpertEdition() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).expertEdition;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfExpertEdition() {
        if (getExpertEdition() == null) return false;
        return true;
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.expertEdition != null) handleAttemptToDeleteConnectedObject("ExpertEdition");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$title(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "TITLE"), state);
        set$volume(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "VOLUME"), state);
        set$number(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "NUMBER"), state);
        set$subNumber(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "SUB_NUMBER"), state);
        set$startPage(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "START_PAGE"), state);
        set$endPage(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "END_PAGE"), state);
        set$notes(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "NOTES"), state);
        castedState.expertEdition = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_EXPERT_EDITION");
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
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.FragInter.DO_State {
        private java.lang.String title;
        private java.lang.String volume;
        private int number;
        private java.lang.String subNumber;
        private int startPage;
        private int endPage;
        private java.lang.String notes;
        private pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition expertEdition;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.title = this.title;
            newCasted.volume = this.volume;
            newCasted.number = this.number;
            newCasted.subNumber = this.subNumber;
            newCasted.startPage = this.startPage;
            newCasted.endPage = this.endPage;
            newCasted.notes = this.notes;
            newCasted.expertEdition = this.expertEdition;
            
        }
        
    }
    
}
