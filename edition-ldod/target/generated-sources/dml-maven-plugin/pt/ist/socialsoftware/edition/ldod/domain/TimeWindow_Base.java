package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class TimeWindow_Base extends pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria {
    // Static Slots
    
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
    protected  TimeWindow_Base() {
        super();
    }
    
    // Getters and Setters
    
    public org.joda.time.LocalDate getBeginDate() {
        return ((DO_State)this.get$obj$state(false)).beginDate;
    }
    
    public void setBeginDate(org.joda.time.LocalDate beginDate) {
        ((DO_State)this.get$obj$state(true)).beginDate = beginDate;
    }
    
    private java.lang.String get$beginDate() {
        org.joda.time.LocalDate value = ((DO_State)this.get$obj$state(false)).beginDate;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForLocalDate(value);
    }
    
    private final void set$beginDate(org.joda.time.LocalDate value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).beginDate = (org.joda.time.LocalDate)((value == null) ? null : value);
    }
    
    public org.joda.time.LocalDate getEndDate() {
        return ((DO_State)this.get$obj$state(false)).endDate;
    }
    
    public void setEndDate(org.joda.time.LocalDate endDate) {
        ((DO_State)this.get$obj$state(true)).endDate = endDate;
    }
    
    private java.lang.String get$endDate() {
        org.joda.time.LocalDate value = ((DO_State)this.get$obj$state(false)).endDate;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForLocalDate(value);
    }
    
    private final void set$endDate(org.joda.time.LocalDate value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).endDate = (org.joda.time.LocalDate)((value == null) ? null : value);
    }
    
    // Role Methods
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$beginDate(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readLocalDate(rs, "BEGIN_DATE"), state);
        set$endDate(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readLocalDate(rs, "END_DATE"), state);
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
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria.DO_State {
        private org.joda.time.LocalDate beginDate;
        private org.joda.time.LocalDate endDate;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.beginDate = this.beginDate;
            newCasted.endDate = this.endDate;
            
        }
        
    }
    
}
