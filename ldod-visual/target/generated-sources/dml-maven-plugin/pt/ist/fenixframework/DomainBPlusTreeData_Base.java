package pt.ist.fenixframework;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class DomainBPlusTreeData_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.DomainBPlusTreeData,pt.ist.fenixframework.DomainBPlusTreeJVSTM> role$$tree = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.DomainBPlusTreeData,pt.ist.fenixframework.DomainBPlusTreeJVSTM>() {
        @Override
        public pt.ist.fenixframework.DomainBPlusTreeJVSTM getValue(pt.ist.fenixframework.DomainBPlusTreeData o1) {
            return ((DomainBPlusTreeData_Base.DO_State)o1.get$obj$state(false)).tree;
        }
        @Override
        public void setValue(pt.ist.fenixframework.DomainBPlusTreeData o1, pt.ist.fenixframework.DomainBPlusTreeJVSTM o2) {
            ((DomainBPlusTreeData_Base.DO_State)o1.get$obj$state(true)).tree = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.DomainBPlusTreeJVSTM,pt.ist.fenixframework.DomainBPlusTreeData> getInverseRole() {
            return pt.ist.fenixframework.DomainBPlusTreeJVSTM.role$$treeData;
        }
        
    };
    
    private final static class DomainBPlusTreeJVSTMData {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.DomainBPlusTreeData,pt.ist.fenixframework.DomainBPlusTreeJVSTM> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.DomainBPlusTreeData,pt.ist.fenixframework.DomainBPlusTreeJVSTM>(role$$tree, "DomainBPlusTreeJVSTMData");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.DomainBPlusTreeData,pt.ist.fenixframework.DomainBPlusTreeJVSTM> getRelationDomainBPlusTreeJVSTMData() {
        return DomainBPlusTreeJVSTMData.relation;
    }
    
    static {
        DomainBPlusTreeJVSTMData.relation.setRelationName("pt.ist.fenixframework.DomainBPlusTreeData.DomainBPlusTreeJVSTMData");
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
    protected  DomainBPlusTreeData_Base() {
        super();
    }
    
    // Getters and Setters
    
    public int getSize() {
        return ((DO_State)this.get$obj$state(false)).size;
    }
    
    public void setSize(int size) {
        ((DO_State)this.get$obj$state(true)).size = size;
    }
    
    private int get$size() {
        int value = ((DO_State)this.get$obj$state(false)).size;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$size(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).size = (int)(value);
    }
    
    // Role Methods
    
    public pt.ist.fenixframework.DomainBPlusTreeJVSTM getTree() {
        return ((DO_State)this.get$obj$state(false)).tree;
    }
    
    public void setTree(pt.ist.fenixframework.DomainBPlusTreeJVSTM tree) {
        getRelationDomainBPlusTreeJVSTMData().add((pt.ist.fenixframework.DomainBPlusTreeData)this, tree);
    }
    
    private java.lang.Long get$oidTree() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).tree;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.tree != null) handleAttemptToDeleteConnectedObject("Tree");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$size(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "SIZE"), state);
        castedState.tree = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_TREE");
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
        private int size;
        private pt.ist.fenixframework.DomainBPlusTreeJVSTM tree;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.size = this.size;
            newCasted.tree = this.tree;
            
        }
        
    }
    
}
