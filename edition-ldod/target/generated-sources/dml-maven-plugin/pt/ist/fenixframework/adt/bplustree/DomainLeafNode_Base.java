package pt.ist.fenixframework.adt.bplustree;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class DomainLeafNode_Base extends pt.ist.fenixframework.adt.bplustree.LeafNode {
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
    protected  DomainLeafNode_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.util.TreeMap<Comparable,pt.ist.fenixframework.core.AbstractDomainObject> getDomainEntries() {
        return ((DO_State)this.get$obj$state(false)).domainEntries;
    }
    
    public void setDomainEntries(java.util.TreeMap<Comparable,pt.ist.fenixframework.core.AbstractDomainObject> domainEntries) {
        ((DO_State)this.get$obj$state(true)).domainEntries = domainEntries;
    }
    
    private java.lang.String get$domainEntries() {
        java.util.TreeMap<Comparable,pt.ist.fenixframework.core.AbstractDomainObject> value = ((DO_State)this.get$obj$state(false)).domainEntries;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(ValueTypeSerializer.serialize$DomainObjectMap(value));
    }
    
    private final void set$domainEntries(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).domainEntries = (java.util.TreeMap<Comparable,pt.ist.fenixframework.core.AbstractDomainObject>)((value == null) ? null : ValueTypeSerializer.deSerialize$DomainObjectMap(value));
    }
    
    // Role Methods
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$domainEntries(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "DOMAIN_ENTRIES"), state);
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
    protected static class DO_State extends pt.ist.fenixframework.adt.bplustree.LeafNode.DO_State {
        private java.util.TreeMap<Comparable,pt.ist.fenixframework.core.AbstractDomainObject> domainEntries;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.domainEntries = this.domainEntries;
            
        }
        
    }
    
}
