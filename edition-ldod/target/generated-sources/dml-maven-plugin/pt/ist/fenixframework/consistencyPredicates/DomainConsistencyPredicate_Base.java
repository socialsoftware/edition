package pt.ist.fenixframework.consistencyPredicates;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class DomainConsistencyPredicate_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> role$$inconsistentDependenceRecord = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getSet(pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate o1) {
            return ((DomainConsistencyPredicate_Base)o1).get$rl$inconsistentDependenceRecord();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate> getInverseRole() {
            return pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord.role$$inconsistentPredicate;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate,pt.ist.fenixframework.DomainMetaClass> role$$domainMetaClass = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate,pt.ist.fenixframework.DomainMetaClass>() {
        @Override
        public pt.ist.fenixframework.DomainMetaClass getValue(pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate o1) {
            return ((DomainConsistencyPredicate_Base.DO_State)o1.get$obj$state(false)).domainMetaClass;
        }
        @Override
        public void setValue(pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate o1, pt.ist.fenixframework.DomainMetaClass o2) {
            ((DomainConsistencyPredicate_Base.DO_State)o1.get$obj$state(true)).domainMetaClass = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate> getInverseRole() {
            return pt.ist.fenixframework.DomainMetaClass.role$$declaredConsistencyPredicate;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> role$$domainDependenceRecord = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getSet(pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate o1) {
            return ((DomainConsistencyPredicate_Base)o1).get$rl$domainDependenceRecord();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate> getInverseRole() {
            return pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord.role$$domainConsistencyPredicate;
        }
        
    };
    
    private final static class DomainConsistencyPredicateInconsistentDependenceRecords {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord>(role$$inconsistentDependenceRecord, "DomainConsistencyPredicateInconsistentDependenceRecords");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getRelationDomainConsistencyPredicateInconsistentDependenceRecords() {
        return DomainConsistencyPredicateInconsistentDependenceRecords.relation;
    }
    
    static {
        DomainConsistencyPredicateInconsistentDependenceRecords.relation.setRelationName("pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate.DomainConsistencyPredicateInconsistentDependenceRecords");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate> getRelationDomainMetaClassDeclaredConsistencyPredicates() {
        return pt.ist.fenixframework.DomainMetaClass.getRelationDomainMetaClassDeclaredConsistencyPredicates();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate> getRelationDomainConsistencyPredicateDomainDependenceRecords() {
        return pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord.getRelationDomainConsistencyPredicateDomainDependenceRecords();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> get$rl$inconsistentDependenceRecord() {
        return get$$relationList("inconsistentDependenceRecord", getRelationDomainConsistencyPredicateInconsistentDependenceRecords());
        
    }
    private RelationList<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> get$rl$domainDependenceRecord() {
        return get$$relationList("domainDependenceRecord", getRelationDomainConsistencyPredicateDomainDependenceRecords().getInverseRelation());
        
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
    protected  DomainConsistencyPredicate_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.reflect.Method getPredicate() {
        return ((DO_State)this.get$obj$state(false)).predicate;
    }
    
    public void setPredicate(java.lang.reflect.Method predicate) {
        ((DO_State)this.get$obj$state(true)).predicate = predicate;
    }
    
    private java.lang.String get$predicate() {
        java.lang.reflect.Method value = ((DO_State)this.get$obj$state(false)).predicate;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(ValueTypeSerializer.serialize$PredicateMethod(value));
    }
    
    private final void set$predicate(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).predicate = (java.lang.reflect.Method)((value == null) ? null : ValueTypeSerializer.deSerialize$PredicateMethod(value));
    }
    
    public java.lang.Boolean getInitialized() {
        return ((DO_State)this.get$obj$state(false)).initialized;
    }
    
    public void setInitialized(java.lang.Boolean initialized) {
        ((DO_State)this.get$obj$state(true)).initialized = initialized;
    }
    
    private java.lang.Boolean get$initialized() {
        java.lang.Boolean value = ((DO_State)this.get$obj$state(false)).initialized;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForBoolean(value);
    }
    
    private final void set$initialized(java.lang.Boolean value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).initialized = (java.lang.Boolean)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public void addInconsistentDependenceRecord(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord inconsistentDependenceRecord) {
        getRelationDomainConsistencyPredicateInconsistentDependenceRecords().add((pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate)this, inconsistentDependenceRecord);
    }
    
    public void removeInconsistentDependenceRecord(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord inconsistentDependenceRecord) {
        getRelationDomainConsistencyPredicateInconsistentDependenceRecords().remove((pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate)this, inconsistentDependenceRecord);
    }
    
    public java.util.Set<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getInconsistentDependenceRecordSet() {
        return get$rl$inconsistentDependenceRecord();
    }
    
    public void set$inconsistentDependenceRecord(OJBFunctionalSetWrapper inconsistentDependenceRecord) {
        get$rl$inconsistentDependenceRecord().setFromOJB(this, "inconsistentDependenceRecord", inconsistentDependenceRecord);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getInconsistentDependenceRecord() {
        return getInconsistentDependenceRecordSet();
    }
    
    @Deprecated
    public int getInconsistentDependenceRecordCount() {
        return getInconsistentDependenceRecordSet().size();
    }
    
    public pt.ist.fenixframework.DomainMetaClass getDomainMetaClass() {
        return ((DO_State)this.get$obj$state(false)).domainMetaClass;
    }
    
    public void setDomainMetaClass(pt.ist.fenixframework.DomainMetaClass domainMetaClass) {
        getRelationDomainMetaClassDeclaredConsistencyPredicates().add(domainMetaClass, (pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate)this);
    }
    
    private java.lang.Long get$oidDomainMetaClass() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).domainMetaClass;
        return (value == null) ? null : value.getOid();
    }
    
    public void addDomainDependenceRecord(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord domainDependenceRecord) {
        getRelationDomainConsistencyPredicateDomainDependenceRecords().add(domainDependenceRecord, (pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate)this);
    }
    
    public void removeDomainDependenceRecord(pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord domainDependenceRecord) {
        getRelationDomainConsistencyPredicateDomainDependenceRecords().remove(domainDependenceRecord, (pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate)this);
    }
    
    public java.util.Set<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getDomainDependenceRecordSet() {
        return get$rl$domainDependenceRecord();
    }
    
    public void set$domainDependenceRecord(OJBFunctionalSetWrapper domainDependenceRecord) {
        get$rl$domainDependenceRecord().setFromOJB(this, "domainDependenceRecord", domainDependenceRecord);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord> getDomainDependenceRecord() {
        return getDomainDependenceRecordSet();
    }
    
    @Deprecated
    public int getDomainDependenceRecordCount() {
        return getDomainDependenceRecordSet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$inconsistentDependenceRecord().size() > 0) handleAttemptToDeleteConnectedObject("InconsistentDependenceRecord");
        if (castedState.domainMetaClass != null) handleAttemptToDeleteConnectedObject("DomainMetaClass");
        if (get$rl$domainDependenceRecord().size() > 0) handleAttemptToDeleteConnectedObject("DomainDependenceRecord");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$predicate(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "PREDICATE"), state);
        set$initialized(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readBoolean(rs, "INITIALIZED"), state);
        castedState.domainMetaClass = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_DOMAIN_META_CLASS");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("inconsistentDependenceRecord")) return getRelationDomainConsistencyPredicateInconsistentDependenceRecords();
        if (attrName.equals("domainDependenceRecord")) return getRelationDomainConsistencyPredicateDomainDependenceRecords().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("inconsistentDependenceRecord", getRelationDomainConsistencyPredicateInconsistentDependenceRecords());
        get$$relationList("domainDependenceRecord", getRelationDomainConsistencyPredicateDomainDependenceRecords().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private java.lang.reflect.Method predicate;
        private java.lang.Boolean initialized;
        private pt.ist.fenixframework.DomainMetaClass domainMetaClass;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.predicate = this.predicate;
            newCasted.initialized = this.initialized;
            newCasted.domainMetaClass = this.domainMetaClass;
            
        }
        
    }
    
}
