package pt.ist.fenixframework.adt.bplustree;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class BPlusTreeArray_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.adt.bplustree.BPlusTreeArray,pt.ist.fenixframework.adt.bplustree.AbstractNodeArray> role$$root = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.adt.bplustree.BPlusTreeArray,pt.ist.fenixframework.adt.bplustree.AbstractNodeArray>() {
        @Override
        public pt.ist.fenixframework.adt.bplustree.AbstractNodeArray getValue(pt.ist.fenixframework.adt.bplustree.BPlusTreeArray o1) {
            return ((BPlusTreeArray_Base.DO_State)o1.get$obj$state(false)).root;
        }
        @Override
        public void setValue(pt.ist.fenixframework.adt.bplustree.BPlusTreeArray o1, pt.ist.fenixframework.adt.bplustree.AbstractNodeArray o2) {
            ((BPlusTreeArray_Base.DO_State)o1.get$obj$state(true)).root = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.adt.bplustree.AbstractNodeArray,pt.ist.fenixframework.adt.bplustree.BPlusTreeArray> getInverseRole() {
            return new pt.ist.fenixframework.dml.runtime.RoleEmpty<pt.ist.fenixframework.adt.bplustree.AbstractNodeArray,pt.ist.fenixframework.adt.bplustree.BPlusTreeArray>(this);
        }
        
    };
    
    private final static class BPlusTreeArrayHasRootNode {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.adt.bplustree.BPlusTreeArray,pt.ist.fenixframework.adt.bplustree.AbstractNodeArray> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.adt.bplustree.BPlusTreeArray,pt.ist.fenixframework.adt.bplustree.AbstractNodeArray>(role$$root, "BPlusTreeArrayHasRootNode");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.adt.bplustree.BPlusTreeArray,pt.ist.fenixframework.adt.bplustree.AbstractNodeArray> getRelationBPlusTreeArrayHasRootNode() {
        return BPlusTreeArrayHasRootNode.relation;
    }
    
    static {
        BPlusTreeArrayHasRootNode.relation.setRelationName("pt.ist.fenixframework.adt.bplustree.BPlusTreeArray.BPlusTreeArrayHasRootNode");
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
    protected  BPlusTreeArray_Base() {
        super();
    }
    
    // Getters and Setters
    
    // Role Methods
    
    public pt.ist.fenixframework.adt.bplustree.AbstractNodeArray getRoot() {
        return ((DO_State)this.get$obj$state(false)).root;
    }
    
    public void setRoot(pt.ist.fenixframework.adt.bplustree.AbstractNodeArray root) {
        getRelationBPlusTreeArrayHasRootNode().add((pt.ist.fenixframework.adt.bplustree.BPlusTreeArray)this, root);
    }
    
    private java.lang.Long get$oidRoot() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).root;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.root != null) handleAttemptToDeleteConnectedObject("Root");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        castedState.root = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_ROOT");
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
        private pt.ist.fenixframework.adt.bplustree.AbstractNodeArray root;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.root = this.root;
            
        }
        
    }
    
}
