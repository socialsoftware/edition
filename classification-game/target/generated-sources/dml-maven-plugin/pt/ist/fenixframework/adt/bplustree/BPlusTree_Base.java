package pt.ist.fenixframework.adt.bplustree;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class BPlusTree_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.adt.bplustree.BPlusTree,pt.ist.fenixframework.adt.bplustree.AbstractNode> role$$root = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.adt.bplustree.BPlusTree,pt.ist.fenixframework.adt.bplustree.AbstractNode>() {
        @Override
        public pt.ist.fenixframework.adt.bplustree.AbstractNode getValue(pt.ist.fenixframework.adt.bplustree.BPlusTree o1) {
            return ((BPlusTree_Base.DO_State)o1.get$obj$state(false)).root;
        }
        @Override
        public void setValue(pt.ist.fenixframework.adt.bplustree.BPlusTree o1, pt.ist.fenixframework.adt.bplustree.AbstractNode o2) {
            ((BPlusTree_Base.DO_State)o1.get$obj$state(true)).root = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.adt.bplustree.AbstractNode,pt.ist.fenixframework.adt.bplustree.BPlusTree> getInverseRole() {
            return new pt.ist.fenixframework.dml.runtime.RoleEmpty<pt.ist.fenixframework.adt.bplustree.AbstractNode,pt.ist.fenixframework.adt.bplustree.BPlusTree>(this);
        }
        
    };
    
    private final static class AdtBPlusTreeHasRootNode {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.adt.bplustree.BPlusTree,pt.ist.fenixframework.adt.bplustree.AbstractNode> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.adt.bplustree.BPlusTree,pt.ist.fenixframework.adt.bplustree.AbstractNode>(role$$root, "AdtBPlusTreeHasRootNode");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.adt.bplustree.BPlusTree,pt.ist.fenixframework.adt.bplustree.AbstractNode> getRelationAdtBPlusTreeHasRootNode() {
        return AdtBPlusTreeHasRootNode.relation;
    }
    
    static {
        AdtBPlusTreeHasRootNode.relation.setRelationName("pt.ist.fenixframework.adt.bplustree.BPlusTree.AdtBPlusTreeHasRootNode");
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
    protected  BPlusTree_Base() {
        super();
    }
    
    // Getters and Setters
    
    // Role Methods
    
    public pt.ist.fenixframework.adt.bplustree.AbstractNode getRoot() {
        return ((DO_State)this.get$obj$state(false)).root;
    }
    
    public void setRoot(pt.ist.fenixframework.adt.bplustree.AbstractNode root) {
        getRelationAdtBPlusTreeHasRootNode().add((pt.ist.fenixframework.adt.bplustree.BPlusTree)this, root);
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
        private pt.ist.fenixframework.adt.bplustree.AbstractNode root;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.root = this.root;
            
        }
        
    }
    
}
