package pt.ist.fenixframework.adt.bplustree;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class AbstractNodeArray_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.adt.bplustree.AbstractNodeArray,pt.ist.fenixframework.adt.bplustree.InnerNodeArray> role$$parent = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.adt.bplustree.AbstractNodeArray,pt.ist.fenixframework.adt.bplustree.InnerNodeArray>() {
        @Override
        public pt.ist.fenixframework.adt.bplustree.InnerNodeArray getValue(pt.ist.fenixframework.adt.bplustree.AbstractNodeArray o1) {
            return ((AbstractNodeArray_Base.DO_State)o1.get$obj$state(false)).parent;
        }
        @Override
        public void setValue(pt.ist.fenixframework.adt.bplustree.AbstractNodeArray o1, pt.ist.fenixframework.adt.bplustree.InnerNodeArray o2) {
            ((AbstractNodeArray_Base.DO_State)o1.get$obj$state(true)).parent = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.adt.bplustree.InnerNodeArray,pt.ist.fenixframework.adt.bplustree.AbstractNodeArray> getInverseRole() {
            return new pt.ist.fenixframework.dml.runtime.RoleEmpty<pt.ist.fenixframework.adt.bplustree.InnerNodeArray,pt.ist.fenixframework.adt.bplustree.AbstractNodeArray>(this);
        }
        
    };
    
    private final static class NodeArrayHasParent {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.adt.bplustree.AbstractNodeArray,pt.ist.fenixframework.adt.bplustree.InnerNodeArray> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.adt.bplustree.AbstractNodeArray,pt.ist.fenixframework.adt.bplustree.InnerNodeArray>(role$$parent, "NodeArrayHasParent");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.adt.bplustree.AbstractNodeArray,pt.ist.fenixframework.adt.bplustree.InnerNodeArray> getRelationNodeArrayHasParent() {
        return NodeArrayHasParent.relation;
    }
    
    static {
        NodeArrayHasParent.relation.setRelationName("pt.ist.fenixframework.adt.bplustree.AbstractNodeArray.NodeArrayHasParent");
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
    protected  AbstractNodeArray_Base() {
        super();
    }
    
    // Getters and Setters
    
    // Role Methods
    
    public pt.ist.fenixframework.adt.bplustree.InnerNodeArray getParent() {
        return ((DO_State)this.get$obj$state(false)).parent;
    }
    
    public void setParent(pt.ist.fenixframework.adt.bplustree.InnerNodeArray parent) {
        getRelationNodeArrayHasParent().add((pt.ist.fenixframework.adt.bplustree.AbstractNodeArray)this, parent);
    }
    
    private java.lang.Long get$oidParent() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).parent;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.parent != null) handleAttemptToDeleteConnectedObject("Parent");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        castedState.parent = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_PARENT");
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
        private pt.ist.fenixframework.adt.bplustree.InnerNodeArray parent;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.parent = this.parent;
            
        }
        
    }
    
}
