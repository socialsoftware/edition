package pt.ist.fenixframework.adt.bplustree;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class LeafNodeArray_Base extends pt.ist.fenixframework.adt.bplustree.AbstractNodeArray {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.adt.bplustree.LeafNodeArray,pt.ist.fenixframework.adt.bplustree.LeafNodeArray> role$$previous = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.adt.bplustree.LeafNodeArray,pt.ist.fenixframework.adt.bplustree.LeafNodeArray>() {
        @Override
        public pt.ist.fenixframework.adt.bplustree.LeafNodeArray getValue(pt.ist.fenixframework.adt.bplustree.LeafNodeArray o1) {
            return ((LeafNodeArray_Base.DO_State)o1.get$obj$state(false)).previous;
        }
        @Override
        public void setValue(pt.ist.fenixframework.adt.bplustree.LeafNodeArray o1, pt.ist.fenixframework.adt.bplustree.LeafNodeArray o2) {
            ((LeafNodeArray_Base.DO_State)o1.get$obj$state(true)).previous = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.adt.bplustree.LeafNodeArray,pt.ist.fenixframework.adt.bplustree.LeafNodeArray> getInverseRole() {
            return pt.ist.fenixframework.adt.bplustree.LeafNodeArray.role$$next;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.adt.bplustree.LeafNodeArray,pt.ist.fenixframework.adt.bplustree.LeafNodeArray> role$$next = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.adt.bplustree.LeafNodeArray,pt.ist.fenixframework.adt.bplustree.LeafNodeArray>() {
        @Override
        public pt.ist.fenixframework.adt.bplustree.LeafNodeArray getValue(pt.ist.fenixframework.adt.bplustree.LeafNodeArray o1) {
            return ((LeafNodeArray_Base.DO_State)o1.get$obj$state(false)).next;
        }
        @Override
        public void setValue(pt.ist.fenixframework.adt.bplustree.LeafNodeArray o1, pt.ist.fenixframework.adt.bplustree.LeafNodeArray o2) {
            ((LeafNodeArray_Base.DO_State)o1.get$obj$state(true)).next = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.adt.bplustree.LeafNodeArray,pt.ist.fenixframework.adt.bplustree.LeafNodeArray> getInverseRole() {
            return pt.ist.fenixframework.adt.bplustree.LeafNodeArray.role$$previous;
        }
        
    };
    
    
    private final static class LeafNodeArrayHasSibling {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.adt.bplustree.LeafNodeArray,pt.ist.fenixframework.adt.bplustree.LeafNodeArray> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.adt.bplustree.LeafNodeArray,pt.ist.fenixframework.adt.bplustree.LeafNodeArray>(role$$next, "LeafNodeArrayHasSibling");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.adt.bplustree.LeafNodeArray,pt.ist.fenixframework.adt.bplustree.LeafNodeArray> getRelationLeafNodeArrayHasSibling() {
        return LeafNodeArrayHasSibling.relation;
    }
    
    static {
        LeafNodeArrayHasSibling.relation.setRelationName("pt.ist.fenixframework.adt.bplustree.LeafNodeArray.LeafNodeArrayHasSibling");
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
    protected  LeafNodeArray_Base() {
        super();
    }
    
    // Getters and Setters
    
    public pt.ist.fenixframework.adt.bplustree.DoubleArray<java.io.Serializable> getEntries() {
        return ((DO_State)this.get$obj$state(false)).entries;
    }
    
    public void setEntries(pt.ist.fenixframework.adt.bplustree.DoubleArray<java.io.Serializable> entries) {
        ((DO_State)this.get$obj$state(true)).entries = entries;
    }
    
    private java.lang.Object get$entries() {
        pt.ist.fenixframework.adt.bplustree.DoubleArray<java.io.Serializable> value = ((DO_State)this.get$obj$state(false)).entries;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForSerializable(ValueTypeSerializer.serialize$BackingArrays(value));
    }
    
    private final void set$entries(java.io.Serializable value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).entries = (pt.ist.fenixframework.adt.bplustree.DoubleArray<java.io.Serializable>)((value == null) ? null : ValueTypeSerializer.deSerialize$BackingArrays(value));
    }
    
    // Role Methods
    
    public pt.ist.fenixframework.adt.bplustree.LeafNodeArray getPrevious() {
        return ((DO_State)this.get$obj$state(false)).previous;
    }
    
    public void setPrevious(pt.ist.fenixframework.adt.bplustree.LeafNodeArray previous) {
        getRelationLeafNodeArrayHasSibling().add(previous, (pt.ist.fenixframework.adt.bplustree.LeafNodeArray)this);
    }
    
    private java.lang.Long get$oidPrevious() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).previous;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.fenixframework.adt.bplustree.LeafNodeArray getNext() {
        return ((DO_State)this.get$obj$state(false)).next;
    }
    
    public void setNext(pt.ist.fenixframework.adt.bplustree.LeafNodeArray next) {
        getRelationLeafNodeArrayHasSibling().add((pt.ist.fenixframework.adt.bplustree.LeafNodeArray)this, next);
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
    protected static class DO_State extends pt.ist.fenixframework.adt.bplustree.AbstractNodeArray.DO_State {
        private pt.ist.fenixframework.adt.bplustree.DoubleArray<java.io.Serializable> entries;
        private pt.ist.fenixframework.adt.bplustree.LeafNodeArray previous;
        private pt.ist.fenixframework.adt.bplustree.LeafNodeArray next;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.entries = this.entries;
            newCasted.previous = this.previous;
            newCasted.next = this.next;
            
        }
        
    }
    
}
