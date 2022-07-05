package pt.ist.fenixframework.adt.bplustree;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class LeafNode_Base extends pt.ist.fenixframework.adt.bplustree.AbstractNode {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.adt.bplustree.LeafNode,pt.ist.fenixframework.adt.bplustree.LeafNode> role$$previous = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.adt.bplustree.LeafNode,pt.ist.fenixframework.adt.bplustree.LeafNode>() {
        @Override
        public pt.ist.fenixframework.adt.bplustree.LeafNode getValue(pt.ist.fenixframework.adt.bplustree.LeafNode o1) {
            return ((LeafNode_Base.DO_State)o1.get$obj$state(false)).previous;
        }
        @Override
        public void setValue(pt.ist.fenixframework.adt.bplustree.LeafNode o1, pt.ist.fenixframework.adt.bplustree.LeafNode o2) {
            ((LeafNode_Base.DO_State)o1.get$obj$state(true)).previous = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.adt.bplustree.LeafNode,pt.ist.fenixframework.adt.bplustree.LeafNode> getInverseRole() {
            return pt.ist.fenixframework.adt.bplustree.LeafNode.role$$next;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.adt.bplustree.LeafNode,pt.ist.fenixframework.adt.bplustree.LeafNode> role$$next = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.adt.bplustree.LeafNode,pt.ist.fenixframework.adt.bplustree.LeafNode>() {
        @Override
        public pt.ist.fenixframework.adt.bplustree.LeafNode getValue(pt.ist.fenixframework.adt.bplustree.LeafNode o1) {
            return ((LeafNode_Base.DO_State)o1.get$obj$state(false)).next;
        }
        @Override
        public void setValue(pt.ist.fenixframework.adt.bplustree.LeafNode o1, pt.ist.fenixframework.adt.bplustree.LeafNode o2) {
            ((LeafNode_Base.DO_State)o1.get$obj$state(true)).next = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.adt.bplustree.LeafNode,pt.ist.fenixframework.adt.bplustree.LeafNode> getInverseRole() {
            return pt.ist.fenixframework.adt.bplustree.LeafNode.role$$previous;
        }
        
    };
    
    
    private final static class AdtLeafNodeHasSibling {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.adt.bplustree.LeafNode,pt.ist.fenixframework.adt.bplustree.LeafNode> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.adt.bplustree.LeafNode,pt.ist.fenixframework.adt.bplustree.LeafNode>(role$$next, "AdtLeafNodeHasSibling");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.adt.bplustree.LeafNode,pt.ist.fenixframework.adt.bplustree.LeafNode> getRelationAdtLeafNodeHasSibling() {
        return AdtLeafNodeHasSibling.relation;
    }
    
    static {
        AdtLeafNodeHasSibling.relation.setRelationName("pt.ist.fenixframework.adt.bplustree.LeafNode.AdtLeafNodeHasSibling");
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
    protected  LeafNode_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.util.TreeMap<Comparable,? extends java.io.Serializable> getEntries() {
        return ((DO_State)this.get$obj$state(false)).entries;
    }
    
    public void setEntries(java.util.TreeMap<Comparable,? extends java.io.Serializable> entries) {
        ((DO_State)this.get$obj$state(true)).entries = entries;
    }
    
    private java.lang.Object get$entries() {
        java.util.TreeMap<Comparable,? extends java.io.Serializable> value = ((DO_State)this.get$obj$state(false)).entries;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForSerializable(ValueTypeSerializer.serialize$GenericTreeMap(value));
    }
    
    private final void set$entries(java.io.Serializable value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).entries = (java.util.TreeMap<Comparable,? extends java.io.Serializable>)((value == null) ? null : ValueTypeSerializer.deSerialize$GenericTreeMap(value));
    }
    
    // Role Methods
    
    public pt.ist.fenixframework.adt.bplustree.LeafNode getPrevious() {
        return ((DO_State)this.get$obj$state(false)).previous;
    }
    
    public void setPrevious(pt.ist.fenixframework.adt.bplustree.LeafNode previous) {
        getRelationAdtLeafNodeHasSibling().add(previous, (pt.ist.fenixframework.adt.bplustree.LeafNode)this);
    }
    
    private java.lang.Long get$oidPrevious() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).previous;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.fenixframework.adt.bplustree.LeafNode getNext() {
        return ((DO_State)this.get$obj$state(false)).next;
    }
    
    public void setNext(pt.ist.fenixframework.adt.bplustree.LeafNode next) {
        getRelationAdtLeafNodeHasSibling().add((pt.ist.fenixframework.adt.bplustree.LeafNode)this, next);
    }
    
    private java.lang.Long get$oidNext() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).next;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.previous != null) handleAttemptToDeleteConnectedObject("Previous");
        if (castedState.next != null) handleAttemptToDeleteConnectedObject("Next");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$entries(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readSerializable(rs, "ENTRIES"), state);
        castedState.previous = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_PREVIOUS");
        castedState.next = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_NEXT");
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
        private java.util.TreeMap<Comparable,? extends java.io.Serializable> entries;
        private pt.ist.fenixframework.adt.bplustree.LeafNode previous;
        private pt.ist.fenixframework.adt.bplustree.LeafNode next;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.entries = this.entries;
            newCasted.previous = this.previous;
            newCasted.next = this.next;
            
        }
        
    }
    
}
