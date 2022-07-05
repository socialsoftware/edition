package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class SpaceText_Base extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion {
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
    protected  SpaceText_Base() {
        super();
    }
    
    // Getters and Setters
    
    public SpaceText.SpaceDim getDim() {
        return ((DO_State)this.get$obj$state(false)).dim;
    }
    
    public void setDim(SpaceText.SpaceDim dim) {
        ((DO_State)this.get$obj$state(true)).dim = dim;
    }
    
    private java.lang.String get$dim() {
        SpaceText.SpaceDim value = ((DO_State)this.get$obj$state(false)).dim;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$dim(SpaceText.SpaceDim value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).dim = (SpaceText.SpaceDim)((value == null) ? null : value);
    }
    
    public int getQuantity() {
        return ((DO_State)this.get$obj$state(false)).quantity;
    }
    
    public void setQuantity(int quantity) {
        ((DO_State)this.get$obj$state(true)).quantity = quantity;
    }
    
    private int get$quantity() {
        int value = ((DO_State)this.get$obj$state(false)).quantity;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$quantity(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).quantity = (int)(value);
    }
    
    public SpaceText.SpaceUnit getUnit() {
        return ((DO_State)this.get$obj$state(false)).unit;
    }
    
    public void setUnit(SpaceText.SpaceUnit unit) {
        ((DO_State)this.get$obj$state(true)).unit = unit;
    }
    
    private java.lang.String get$unit() {
        SpaceText.SpaceUnit value = ((DO_State)this.get$obj$state(false)).unit;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$unit(SpaceText.SpaceUnit value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).unit = (SpaceText.SpaceUnit)((value == null) ? null : value);
    }
    
    // Role Methods
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$dim(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(SpaceText.SpaceDim.class, rs, "DIM"), state);
        set$quantity(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "QUANTITY"), state);
        set$unit(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(SpaceText.SpaceUnit.class, rs, "UNIT"), state);
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
        private SpaceText.SpaceDim dim;
        private int quantity;
        private SpaceText.SpaceUnit unit;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.dim = this.dim;
            newCasted.quantity = this.quantity;
            newCasted.unit = this.unit;
            
        }
        
    }
    
}
