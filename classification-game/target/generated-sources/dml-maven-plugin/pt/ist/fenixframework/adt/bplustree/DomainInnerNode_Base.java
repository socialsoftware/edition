package pt.ist.fenixframework.adt.bplustree;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class DomainInnerNode_Base extends pt.ist.fenixframework.adt.bplustree.InnerNode {
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
    protected  DomainInnerNode_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.util.TreeMap<Comparable,AbstractNode> getSubNodesByOid() {
        return ((DO_State)this.get$obj$state(false)).subNodesByOid;
    }
    
    public void setSubNodesByOid(java.util.TreeMap<Comparable,AbstractNode> subNodesByOid) {
        ((DO_State)this.get$obj$state(true)).subNodesByOid = subNodesByOid;
    }
    
    private java.lang.String get$subNodesByOid() {
        java.util.TreeMap<Comparable,AbstractNode> value = ((DO_State)this.get$obj$state(false)).subNodesByOid;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(ValueTypeSerializer.serialize$OidIndexedMap(value));
    }
    
    private final void set$subNodesByOid(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).subNodesByOid = (java.util.TreeMap<Comparable,AbstractNode>)((value == null) ? null : ValueTypeSerializer.deSerialize$OidIndexedMap(value));
    }
    
    // Role Methods
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$subNodesByOid(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "SUB_NODES_BY_OID"), state);
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
    protected static class DO_State extends pt.ist.fenixframework.adt.bplustree.InnerNode.DO_State {
        private java.util.TreeMap<Comparable,AbstractNode> subNodesByOid;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.subNodesByOid = this.subNodesByOid;
            
        }
        
    }
    
}
