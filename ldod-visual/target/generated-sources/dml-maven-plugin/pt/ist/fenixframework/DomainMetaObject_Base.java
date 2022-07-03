package pt.ist.fenixframework;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class DomainMetaObject_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> role$$ownDependenceRecord = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getSet(pt.ist.fenixframework.DomainMetaObject o1) {
            return ((DomainMetaObject_Base)o1).get$rl$ownDependenceRecord();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.DomainMetaObject> getInverseRole() {
            return pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord.role$$dependentDomainMetaObject;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.DomainMetaClass> role$$domainMetaClass = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.DomainMetaClass>() {
        @Override
        public pt.ist.fenixframework.DomainMetaClass getValue(pt.ist.fenixframework.DomainMetaObject o1) {
            return ((DomainMetaObject_Base.DO_State)o1.get$obj$state(false)).domainMetaClass;
        }
        @Override
        public void setValue(pt.ist.fenixframework.DomainMetaObject o1, pt.ist.fenixframework.DomainMetaClass o2) {
            ((DomainMetaObject_Base.DO_State)o1.get$obj$state(true)).domainMetaClass = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainMetaObject> getInverseRole() {
            return new pt.ist.fenixframework.dml.runtime.RoleEmpty<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainMetaObject>(this);
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> role$$dependingDependenceRecord = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getSet(pt.ist.fenixframework.DomainMetaObject o1) {
            return ((DomainMetaObject_Base)o1).get$rl$dependingDependenceRecord();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.DomainMetaObject> getInverseRole() {
            return pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord.role$$dependedDomainMetaObject;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.DomainObject> role$$domainObject = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.DomainObject>() {
        @Override
        public pt.ist.fenixframework.DomainObject getValue(pt.ist.fenixframework.DomainMetaObject o1) {
            return ((DomainMetaObject_Base.DO_State)o1.get$obj$state(false)).domainObject;
        }
        @Override
        public void setValue(pt.ist.fenixframework.DomainMetaObject o1, pt.ist.fenixframework.DomainObject o2) {
            ((DomainMetaObject_Base.DO_State)o1.get$obj$state(true)).domainObject = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.DomainObject,pt.ist.fenixframework.DomainMetaObject> getInverseRole() {
            return new pt.ist.fenixframework.dml.runtime.RoleEmpty<pt.ist.fenixframework.DomainObject,pt.ist.fenixframework.DomainMetaObject>(this);
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.DomainMetaObject> getRelationDependentDomainMetaObjectOwnDependenceRecords() {
        return pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord.getRelationDependentDomainMetaObjectOwnDependenceRecords();
    }
    
    private final static class DomainMetaObjectsDomainMetaClass {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.DomainMetaClass> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.DomainMetaClass>(role$$domainMetaClass, "DomainMetaObjectsDomainMetaClass");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.DomainMetaClass> getRelationDomainMetaObjectsDomainMetaClass() {
        return DomainMetaObjectsDomainMetaClass.relation;
    }
    
    static {
        DomainMetaObjectsDomainMetaClass.relation.setRelationName("pt.ist.fenixframework.DomainMetaObject.DomainMetaObjectsDomainMetaClass");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.DomainMetaObject> getRelationDependedDomainMetaObjectsDependingDependenceRecords() {
        return pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord.getRelationDependedDomainMetaObjectsDependingDependenceRecords();
    }
    
    private final static class DomainMetaObjectAbstractDomainObject {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.DomainObject> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.DomainObject>(role$$domainObject, "DomainMetaObjectAbstractDomainObject");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.DomainObject> getRelationDomainMetaObjectAbstractDomainObject() {
        return DomainMetaObjectAbstractDomainObject.relation;
    }
    
    static {
        DomainMetaObjectAbstractDomainObject.relation.setRelationName("pt.ist.fenixframework.DomainMetaObject.DomainMetaObjectAbstractDomainObject");
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> get$rl$ownDependenceRecord() {
        return get$$relationList("ownDependenceRecord", getRelationDependentDomainMetaObjectOwnDependenceRecords().getInverseRelation());
        
    }
    private RelationList<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> get$rl$dependingDependenceRecord() {
        return get$$relationList("dependingDependenceRecord", getRelationDependedDomainMetaObjectsDependingDependenceRecords().getInverseRelation());
        
    }
    
    // Init Instance
    
    private void initInstance() {
        init$Instance(true);
    }
    
    @Override
    protected void init$Instance(boolean allocateOnly) {
        super.init$Instance(allocateOnly);
        
    }
    
    // Constructors
    protected  DomainMetaObject_Base() {
        super();
    }
    
    // Getters and Setters
    
    // Role Methods
    
    public void addOwnDependenceRecord(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord ownDependenceRecord) {
        getRelationDependentDomainMetaObjectOwnDependenceRecords().add(ownDependenceRecord, (pt.ist.fenixframework.DomainMetaObject)this);
    }
    
    public void removeOwnDependenceRecord(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord ownDependenceRecord) {
        getRelationDependentDomainMetaObjectOwnDependenceRecords().remove(ownDependenceRecord, (pt.ist.fenixframework.DomainMetaObject)this);
    }
    
    public java.util.Set<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getOwnDependenceRecordSet() {
        return get$rl$ownDependenceRecord();
    }
    
    public void set$ownDependenceRecord(OJBFunctionalSetWrapper ownDependenceRecord) {
        get$rl$ownDependenceRecord().setFromOJB(this, "ownDependenceRecord", ownDependenceRecord);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getOwnDependenceRecord() {
        return getOwnDependenceRecordSet();
    }
    
    @Deprecated
    public int getOwnDependenceRecordCount() {
        return getOwnDependenceRecordSet().size();
    }
    
    public pt.ist.fenixframework.DomainMetaClass getDomainMetaClass() {
        return ((DO_State)this.get$obj$state(false)).domainMetaClass;
    }
    
    public void setDomainMetaClass(pt.ist.fenixframework.DomainMetaClass domainMetaClass) {
        getRelationDomainMetaObjectsDomainMetaClass().add((pt.ist.fenixframework.DomainMetaObject)this, domainMetaClass);
    }
    
    private java.lang.Long get$oidDomainMetaClass() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).domainMetaClass;
        return (value == null) ? null : value.getOid();
    }
    
    public void addDependingDependenceRecord(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord dependingDependenceRecord) {
        getRelationDependedDomainMetaObjectsDependingDependenceRecords().add(dependingDependenceRecord, (pt.ist.fenixframework.DomainMetaObject)this);
    }
    
    public void removeDependingDependenceRecord(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord dependingDependenceRecord) {
        getRelationDependedDomainMetaObjectsDependingDependenceRecords().remove(dependingDependenceRecord, (pt.ist.fenixframework.DomainMetaObject)this);
    }
    
    public java.util.Set<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getDependingDependenceRecordSet() {
        return get$rl$dependingDependenceRecord();
    }
    
    public void set$dependingDependenceRecord(OJBFunctionalSetWrapper dependingDependenceRecord) {
        get$rl$dependingDependenceRecord().setFromOJB(this, "dependingDependenceRecord", dependingDependenceRecord);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getDependingDependenceRecord() {
        return getDependingDependenceRecordSet();
    }
    
    @Deprecated
    public int getDependingDependenceRecordCount() {
        return getDependingDependenceRecordSet().size();
    }
    
    public pt.ist.fenixframework.DomainObject getDomainObject() {
        return ((DO_State)this.get$obj$state(false)).domainObject;
    }
    
    public void setDomainObject(pt.ist.fenixframework.DomainObject domainObject) {
        getRelationDomainMetaObjectAbstractDomainObject().add((pt.ist.fenixframework.DomainMetaObject)this, domainObject);
    }
    
    private java.lang.Long get$oidDomainObject() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).domainObject;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$ownDependenceRecord().size() > 0) handleAttemptToDeleteConnectedObject("OwnDependenceRecord");
        if (castedState.domainMetaClass != null) handleAttemptToDeleteConnectedObject("DomainMetaClass");
        if (get$rl$dependingDependenceRecord().size() > 0) handleAttemptToDeleteConnectedObject("DependingDependenceRecord");
        if (castedState.domainObject != null) handleAttemptToDeleteConnectedObject("DomainObject");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        castedState.domainMetaClass = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_DOMAIN_META_CLASS");
        castedState.domainObject = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_DOMAIN_OBJECT");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("ownDependenceRecord")) return getRelationDependentDomainMetaObjectOwnDependenceRecords().getInverseRelation();
        if (attrName.equals("dependingDependenceRecord")) return getRelationDependedDomainMetaObjectsDependingDependenceRecords().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("ownDependenceRecord", getRelationDependentDomainMetaObjectOwnDependenceRecords().getInverseRelation());
        get$$relationList("dependingDependenceRecord", getRelationDependedDomainMetaObjectsDependingDependenceRecords().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private pt.ist.fenixframework.DomainMetaClass domainMetaClass;
        private pt.ist.fenixframework.DomainObject domainObject;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.domainMetaClass = this.domainMetaClass;
            newCasted.domainObject = this.domainObject;
            
        }
        
    }
    
}
