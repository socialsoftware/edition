package pt.ist.fenixframework.consistencyPredicates;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class DomainDependenceRecord_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate> role$$inconsistentPredicate = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate>() {
        @Override
        public pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate getValue(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord o1) {
            return ((DomainDependenceRecord_Base.DO_State)o1.get$obj$state(false)).inconsistentPredicate;
        }
        @Override
        public void setValue(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord o1, pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate o2) {
            ((DomainDependenceRecord_Base.DO_State)o1.get$obj$state(true)).inconsistentPredicate = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getInverseRole() {
            return pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate.role$$inconsistentDependenceRecord;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.DomainMetaObject> role$$dependentDomainMetaObject = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.DomainMetaObject>() {
        @Override
        public pt.ist.fenixframework.DomainMetaObject getValue(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord o1) {
            return ((DomainDependenceRecord_Base.DO_State)o1.get$obj$state(false)).dependentDomainMetaObject;
        }
        @Override
        public void setValue(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord o1, pt.ist.fenixframework.DomainMetaObject o2) {
            ((DomainDependenceRecord_Base.DO_State)o1.get$obj$state(true)).dependentDomainMetaObject = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getInverseRole() {
            return pt.ist.fenixframework.DomainMetaObject.role$$ownDependenceRecord;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate> role$$domainConsistencyPredicate = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate>() {
        @Override
        public pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate getValue(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord o1) {
            return ((DomainDependenceRecord_Base.DO_State)o1.get$obj$state(false)).domainConsistencyPredicate;
        }
        @Override
        public void setValue(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord o1, pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate o2) {
            ((DomainDependenceRecord_Base.DO_State)o1.get$obj$state(true)).domainConsistencyPredicate = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getInverseRole() {
            return pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate.role$$domainDependenceRecord;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.DomainMetaObject> role$$dependedDomainMetaObject = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.DomainMetaObject>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.fenixframework.DomainMetaObject> getSet(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord o1) {
            return ((DomainDependenceRecord_Base)o1).get$rl$dependedDomainMetaObject();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.DomainMetaObject,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getInverseRole() {
            return pt.ist.fenixframework.DomainMetaObject.role$$dependingDependenceRecord;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getRelationDomainConsistencyPredicateInconsistentDependenceRecords() {
        return pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate.getRelationDomainConsistencyPredicateInconsistentDependenceRecords();
    }
    
    private final static class DependentDomainMetaObjectOwnDependenceRecords {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.DomainMetaObject> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.DomainMetaObject>(role$$dependentDomainMetaObject, "DependentDomainMetaObjectOwnDependenceRecords");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.DomainMetaObject> getRelationDependentDomainMetaObjectOwnDependenceRecords() {
        return DependentDomainMetaObjectOwnDependenceRecords.relation;
    }
    
    static {
        DependentDomainMetaObjectOwnDependenceRecords.relation.setRelationName("pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord.DependentDomainMetaObjectOwnDependenceRecords");
    }
    
    private final static class DomainConsistencyPredicateDomainDependenceRecords {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate>(role$$domainConsistencyPredicate, "DomainConsistencyPredicateDomainDependenceRecords");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate> getRelationDomainConsistencyPredicateDomainDependenceRecords() {
        return DomainConsistencyPredicateDomainDependenceRecords.relation;
    }
    
    static {
        DomainConsistencyPredicateDomainDependenceRecords.relation.setRelationName("pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord.DomainConsistencyPredicateDomainDependenceRecords");
    }
    
    private final static class DependedDomainMetaObjectsDependingDependenceRecords {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.DomainMetaObject> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.DomainMetaObject>(role$$dependedDomainMetaObject, "DependedDomainMetaObjectsDependingDependenceRecords");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.DomainMetaObject> getRelationDependedDomainMetaObjectsDependingDependenceRecords() {
        return DependedDomainMetaObjectsDependingDependenceRecords.relation;
    }
    
    static {
        DependedDomainMetaObjectsDependingDependenceRecords.relation.setRelationName("pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord.DependedDomainMetaObjectsDependingDependenceRecords");
        DependedDomainMetaObjectsDependingDependenceRecords.relation.addListener(new pt.ist.fenixframework.dml.runtime.RelationAdapter<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.DomainMetaObject>() {
            @Override
            public void beforeAdd(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord arg0, pt.ist.fenixframework.DomainMetaObject arg1) {
                pt.ist.fenixframework.backend.jvstmojb.pstm.TransactionSupport.addRelationTuple("DependedDomainMetaObjectsDependingDependenceRecords", arg1, "dependingDependenceRecord", arg0, "dependedDomainMetaObject");
            }
            @Override
            public void beforeRemove(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord arg0, pt.ist.fenixframework.DomainMetaObject arg1) {
                pt.ist.fenixframework.backend.jvstmojb.pstm.TransactionSupport.removeRelationTuple("DependedDomainMetaObjectsDependingDependenceRecords", arg1, "dependingDependenceRecord", arg0, "dependedDomainMetaObject");
            }
            
        }
        );
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.DomainMetaObject> get$rl$dependedDomainMetaObject() {
        return get$$relationList("dependedDomainMetaObject", getRelationDependedDomainMetaObjectsDependingDependenceRecords());
        
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
    protected  DomainDependenceRecord_Base() {
        super();
    }
    
    // Getters and Setters
    
    // Role Methods
    
    public pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate getInconsistentPredicate() {
        return ((DO_State)this.get$obj$state(false)).inconsistentPredicate;
    }
    
    public void setInconsistentPredicate(pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate inconsistentPredicate) {
        getRelationDomainConsistencyPredicateInconsistentDependenceRecords().add(inconsistentPredicate, (pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord)this);
    }
    
    private java.lang.Long get$oidInconsistentPredicate() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).inconsistentPredicate;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.fenixframework.DomainMetaObject getDependentDomainMetaObject() {
        return ((DO_State)this.get$obj$state(false)).dependentDomainMetaObject;
    }
    
    public void setDependentDomainMetaObject(pt.ist.fenixframework.DomainMetaObject dependentDomainMetaObject) {
        getRelationDependentDomainMetaObjectOwnDependenceRecords().add((pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord)this, dependentDomainMetaObject);
    }
    
    private java.lang.Long get$oidDependentDomainMetaObject() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).dependentDomainMetaObject;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate getDomainConsistencyPredicate() {
        return ((DO_State)this.get$obj$state(false)).domainConsistencyPredicate;
    }
    
    public void setDomainConsistencyPredicate(pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate domainConsistencyPredicate) {
        getRelationDomainConsistencyPredicateDomainDependenceRecords().add((pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord)this, domainConsistencyPredicate);
    }
    
    private java.lang.Long get$oidDomainConsistencyPredicate() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).domainConsistencyPredicate;
        return (value == null) ? null : value.getOid();
    }
    
    public void addDependedDomainMetaObject(pt.ist.fenixframework.DomainMetaObject dependedDomainMetaObject) {
        getRelationDependedDomainMetaObjectsDependingDependenceRecords().add((pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord)this, dependedDomainMetaObject);
    }
    
    public void removeDependedDomainMetaObject(pt.ist.fenixframework.DomainMetaObject dependedDomainMetaObject) {
        getRelationDependedDomainMetaObjectsDependingDependenceRecords().remove((pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord)this, dependedDomainMetaObject);
    }
    
    public java.util.Set<pt.ist.fenixframework.DomainMetaObject> getDependedDomainMetaObjectSet() {
        return get$rl$dependedDomainMetaObject();
    }
    
    public void set$dependedDomainMetaObject(OJBFunctionalSetWrapper dependedDomainMetaObject) {
        get$rl$dependedDomainMetaObject().setFromOJB(this, "dependedDomainMetaObject", dependedDomainMetaObject);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.fenixframework.DomainMetaObject> getDependedDomainMetaObject() {
        return getDependedDomainMetaObjectSet();
    }
    
    @Deprecated
    public int getDependedDomainMetaObjectCount() {
        return getDependedDomainMetaObjectSet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.inconsistentPredicate != null) handleAttemptToDeleteConnectedObject("InconsistentPredicate");
        if (castedState.dependentDomainMetaObject != null) handleAttemptToDeleteConnectedObject("DependentDomainMetaObject");
        if (castedState.domainConsistencyPredicate != null) handleAttemptToDeleteConnectedObject("DomainConsistencyPredicate");
        if (get$rl$dependedDomainMetaObject().size() > 0) handleAttemptToDeleteConnectedObject("DependedDomainMetaObject");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        castedState.inconsistentPredicate = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_INCONSISTENT_PREDICATE");
        castedState.dependentDomainMetaObject = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_DEPENDENT_DOMAIN_META_OBJECT");
        castedState.domainConsistencyPredicate = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_DOMAIN_CONSISTENCY_PREDICATE");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("dependedDomainMetaObject")) return getRelationDependedDomainMetaObjectsDependingDependenceRecords();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("dependedDomainMetaObject", getRelationDependedDomainMetaObjectsDependingDependenceRecords());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate inconsistentPredicate;
        private pt.ist.fenixframework.DomainMetaObject dependentDomainMetaObject;
        private pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate domainConsistencyPredicate;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.inconsistentPredicate = this.inconsistentPredicate;
            newCasted.dependentDomainMetaObject = this.dependentDomainMetaObject;
            newCasted.domainConsistencyPredicate = this.domainConsistencyPredicate;
            
        }
        
    }
    
}
