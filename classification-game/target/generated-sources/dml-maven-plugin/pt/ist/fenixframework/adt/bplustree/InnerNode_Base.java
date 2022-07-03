package pt.ist.fenixframework.adt.bplustree;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class InnerNode_Base extends pt.ist.fenixframework.adt.bplustree.AbstractNode {
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
    protected  InnerNode_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.util.TreeMap<Comparable,AbstractNode> getSubNodes() {
        return ((DO_State)this.get$obj$state(false)).subNodes;
    }
    
    public void setSubNodes(java.util.TreeMap<Comparable,AbstractNode> subNodes) {
        ((DO_State)this.get$obj$state(true)).subNodes = subNodes;
    }
    
    private java.lang.Object get$subNodes() {
        java.util.TreeMap<Comparable,AbstractNode> value = ((DO_State)this.get$obj$state(false)).subNodes;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForSerializable(ValueTypeSerializer.serialize$GenericTreeMap(value));
    }
    
    private final void set$subNodes(java.io.Serializable value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).subNodes = (java.util.TreeMap<Comparable,AbstractNode>)((value == null) ? null : ValueTypeSerializer.deSerialize$GenericTreeMap(value));
    }
    
    // Role Methods
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$subNodes(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readSerializable(rs, "SUB_NODES"), state);
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
    protected static class DO_State extends pt.ist.fenixframework.adt.bplustree.AbstractNode.DO_State {
        private java.util.TreeMap<Comparable,AbstractNode> subNodes;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.subNodes = this.subNodes;
            
        }
        
    }
    
}
