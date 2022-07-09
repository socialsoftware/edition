package pt.ist.fenixframework;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class DomainBPlusTreeJVSTM_Base extends pt.ist.fenixframework.adt.bplustree.DomainBPlusTree {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.DomainBPlusTreeJVSTM,pt.ist.fenixframework.DomainBPlusTreeData> role$$treeData = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.DomainBPlusTreeJVSTM,pt.ist.fenixframework.DomainBPlusTreeData>() {
        @Override
        public pt.ist.fenixframework.DomainBPlusTreeData getValue(pt.ist.fenixframework.DomainBPlusTreeJVSTM o1) {
            return ((DomainBPlusTreeJVSTM_Base.DO_State)o1.get$obj$state(false)).treeData;
        }
        @Override
        public void setValue(pt.ist.fenixframework.DomainBPlusTreeJVSTM o1, pt.ist.fenixframework.DomainBPlusTreeData o2) {
            ((DomainBPlusTreeJVSTM_Base.DO_State)o1.get$obj$state(true)).treeData = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.DomainBPlusTreeData,pt.ist.fenixframework.DomainBPlusTreeJVSTM> getInverseRole() {
            return pt.ist.fenixframework.DomainBPlusTreeData.role$$tree;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.DomainBPlusTreeData,pt.ist.fenixframework.DomainBPlusTreeJVSTM> getRelationDomainBPlusTreeJVSTMData() {
        return pt.ist.fenixframework.DomainBPlusTreeData.getRelationDomainBPlusTreeJVSTMData();
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
    protected  DomainBPlusTreeJVSTM_Base() {
        super();
    }
    
    // Getters and Setters
    
    // Role Methods
    
    public pt.ist.fenixframework.DomainBPlusTreeData getTreeData() {
        return ((DO_State)this.get$obj$state(false)).treeData;
    }
    
    public void setTreeData(pt.ist.fenixframework.DomainBPlusTreeData treeData) {
        getRelationDomainBPlusTreeJVSTMData().add(treeData, (pt.ist.fenixframework.DomainBPlusTreeJVSTM)this);
    }
    
    private java.lang.Long get$oidTreeData() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).treeData;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.treeData != null) handleAttemptToDeleteConnectedObject("TreeData");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        castedState.treeData = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_TREE_DATA");
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
    protected static class DO_State extends pt.ist.fenixframework.adt.bplustree.DomainBPlusTree.DO_State {
        private pt.ist.fenixframework.DomainBPlusTreeData treeData;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.treeData = this.treeData;
            
        }
        
    }
    
}
