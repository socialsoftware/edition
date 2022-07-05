package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class GapText_Base extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion {
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
    protected  GapText_Base() {
        super();
    }
    
    // Getters and Setters
    
    public GapText.GapReason getReason() {
        return ((DO_State)this.get$obj$state(false)).reason;
    }
    
    public void setReason(GapText.GapReason reason) {
        ((DO_State)this.get$obj$state(true)).reason = reason;
    }
    
    private java.lang.String get$reason() {
        GapText.GapReason value = ((DO_State)this.get$obj$state(false)).reason;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$reason(GapText.GapReason value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).reason = (GapText.GapReason)((value == null) ? null : value);
    }
    
    public GapText.GapUnit getUnit() {
        return ((DO_State)this.get$obj$state(false)).unit;
    }
    
    public void setUnit(GapText.GapUnit unit) {
        ((DO_State)this.get$obj$state(true)).unit = unit;
    }
    
    private java.lang.String get$unit() {
        GapText.GapUnit value = ((DO_State)this.get$obj$state(false)).unit;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$unit(GapText.GapUnit value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).unit = (GapText.GapUnit)((value == null) ? null : value);
    }
    
    public int getExtent() {
        return ((DO_State)this.get$obj$state(false)).extent;
    }
    
    public void setExtent(int extent) {
        ((DO_State)this.get$obj$state(true)).extent = extent;
    }
    
    private int get$extent() {
        int value = ((DO_State)this.get$obj$state(false)).extent;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$extent(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).extent = (int)(value);
    }
    
    // Role Methods
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$reason(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(GapText.GapReason.class, rs, "REASON"), state);
        set$unit(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(GapText.GapUnit.class, rs, "UNIT"), state);
        set$extent(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "EXTENT"), state);
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
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion.DO_State {
        private GapText.GapReason reason;
        private GapText.GapUnit unit;
        private int extent;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.reason = this.reason;
            newCasted.unit = this.unit;
            newCasted.extent = this.extent;
            
        }
        
    }
    
}
