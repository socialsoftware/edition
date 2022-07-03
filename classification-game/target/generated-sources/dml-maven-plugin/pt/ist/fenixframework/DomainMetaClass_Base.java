package pt.ist.fenixframework;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class DomainMetaClass_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate> role$$declaredConsistencyPredicate = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate> getSet(pt.ist.fenixframework.DomainMetaClass o1) {
            return ((DomainMetaClass_Base)o1).get$rl$declaredConsistencyPredicate();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate,pt.ist.fenixframework.DomainMetaClass> getInverseRole() {
            return pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate.role$$domainMetaClass;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainFenixFrameworkRoot> role$$domainFenixFrameworkRoot = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainFenixFrameworkRoot>() {
        @Override
        public pt.ist.fenixframework.DomainFenixFrameworkRoot getValue(pt.ist.fenixframework.DomainMetaClass o1) {
            return ((DomainMetaClass_Base.DO_State)o1.get$obj$state(false)).domainFenixFrameworkRoot;
        }
        @Override
        public void setValue(pt.ist.fenixframework.DomainMetaClass o1, pt.ist.fenixframework.DomainFenixFrameworkRoot o2) {
            ((DomainMetaClass_Base.DO_State)o1.get$obj$state(true)).domainFenixFrameworkRoot = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.DomainFenixFrameworkRoot,pt.ist.fenixframework.DomainMetaClass> getInverseRole() {
            return pt.ist.fenixframework.DomainFenixFrameworkRoot.role$$domainMetaClass;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainMetaClass> role$$domainMetaSuperclass = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainMetaClass>() {
        @Override
        public pt.ist.fenixframework.DomainMetaClass getValue(pt.ist.fenixframework.DomainMetaClass o1) {
            return ((DomainMetaClass_Base.DO_State)o1.get$obj$state(false)).domainMetaSuperclass;
        }
        @Override
        public void setValue(pt.ist.fenixframework.DomainMetaClass o1, pt.ist.fenixframework.DomainMetaClass o2) {
            ((DomainMetaClass_Base.DO_State)o1.get$obj$state(true)).domainMetaSuperclass = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainMetaClass> getInverseRole() {
            return pt.ist.fenixframework.DomainMetaClass.role$$domainMetaSubclass;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainMetaClass> role$$domainMetaSubclass = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainMetaClass>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.fenixframework.DomainMetaClass> getSet(pt.ist.fenixframework.DomainMetaClass o1) {
            return ((DomainMetaClass_Base)o1).get$rl$domainMetaSubclass();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainMetaClass> getInverseRole() {
            return pt.ist.fenixframework.DomainMetaClass.role$$domainMetaSuperclass;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.adt.bplustree.DomainBPlusTree> role$$existingDomainMetaObjects = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.adt.bplustree.DomainBPlusTree>() {
        @Override
        public pt.ist.fenixframework.adt.bplustree.DomainBPlusTree getValue(pt.ist.fenixframework.DomainMetaClass o1) {
            return ((DomainMetaClass_Base.DO_State)o1.get$obj$state(false)).existingDomainMetaObjects;
        }
        @Override
        public void setValue(pt.ist.fenixframework.DomainMetaClass o1, pt.ist.fenixframework.adt.bplustree.DomainBPlusTree o2) {
            ((DomainMetaClass_Base.DO_State)o1.get$obj$state(true)).existingDomainMetaObjects = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.adt.bplustree.DomainBPlusTree,pt.ist.fenixframework.DomainMetaClass> getInverseRole() {
            return new pt.ist.fenixframework.dml.runtime.RoleEmpty<pt.ist.fenixframework.adt.bplustree.DomainBPlusTree,pt.ist.fenixframework.DomainMetaClass>(this);
        }
        
    };
    
    private final static class DomainMetaClassDeclaredConsistencyPredicates {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate>(role$$declaredConsistencyPredicate, "DomainMetaClassDeclaredConsistencyPredicates");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate> getRelationDomainMetaClassDeclaredConsistencyPredicates() {
        return DomainMetaClassDeclaredConsistencyPredicates.relation;
    }
    
    static {
        DomainMetaClassDeclaredConsistencyPredicates.relation.setRelationName("pt.ist.fenixframework.DomainMetaClass.DomainMetaClassDeclaredConsistencyPredicates");
    }
    
    private final static class DomainFenixFrameworkRootDomainMetaClasses {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainFenixFrameworkRoot> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainFenixFrameworkRoot>(role$$domainFenixFrameworkRoot, "DomainFenixFrameworkRootDomainMetaClasses");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainFenixFrameworkRoot> getRelationDomainFenixFrameworkRootDomainMetaClasses() {
        return DomainFenixFrameworkRootDomainMetaClasses.relation;
    }
    
    static {
        DomainFenixFrameworkRootDomainMetaClasses.relation.setRelationName("pt.ist.fenixframework.DomainMetaClass.DomainFenixFrameworkRootDomainMetaClasses");
    }
    
    
    private final static class DomainMetaSuperclassDomainMetaSubclasses {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainMetaClass> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainMetaClass>(role$$domainMetaSubclass, "DomainMetaSuperclassDomainMetaSubclasses");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainMetaClass> getRelationDomainMetaSuperclassDomainMetaSubclasses() {
        return DomainMetaSuperclassDomainMetaSubclasses.relation;
    }
    
    static {
        DomainMetaSuperclassDomainMetaSubclasses.relation.setRelationName("pt.ist.fenixframework.DomainMetaClass.DomainMetaSuperclassDomainMetaSubclasses");
    }
    
    private final static class DomainMetaClassExistingDomainMetaObjects {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.adt.bplustree.DomainBPlusTree> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.adt.bplustree.DomainBPlusTree>(role$$existingDomainMetaObjects, "DomainMetaClassExistingDomainMetaObjects");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.adt.bplustree.DomainBPlusTree> getRelationDomainMetaClassExistingDomainMetaObjects() {
        return DomainMetaClassExistingDomainMetaObjects.relation;
    }
    
    static {
        DomainMetaClassExistingDomainMetaObjects.relation.setRelationName("pt.ist.fenixframework.DomainMetaClass.DomainMetaClassExistingDomainMetaObjects");
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate> get$rl$declaredConsistencyPredicate() {
        return get$$relationList("declaredConsistencyPredicate", getRelationDomainMetaClassDeclaredConsistencyPredicates());
        
    }
    private RelationList<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainMetaClass> get$rl$domainMetaSubclass() {
        return get$$relationList("domainMetaSubclass", getRelationDomainMetaSuperclassDomainMetaSubclasses());
        
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
    protected  DomainMetaClass_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getDomainClassName() {
        return ((DO_State)this.get$obj$state(false)).domainClassName;
    }
    
    public void setDomainClassName(java.lang.String domainClassName) {
        ((DO_State)this.get$obj$state(true)).domainClassName = domainClassName;
    }
    
    private java.lang.String get$domainClassName() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).domainClassName;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$domainClassName(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).domainClassName = (java.lang.String)((value == null) ? null : value);
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
    
    public void addDeclaredConsistencyPredicate(pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate declaredConsistencyPredicate) {
        getRelationDomainMetaClassDeclaredConsistencyPredicates().add((pt.ist.fenixframework.DomainMetaClass)this, declaredConsistencyPredicate);
    }
    
    public void removeDeclaredConsistencyPredicate(pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate declaredConsistencyPredicate) {
        getRelationDomainMetaClassDeclaredConsistencyPredicates().remove((pt.ist.fenixframework.DomainMetaClass)this, declaredConsistencyPredicate);
    }
    
    public java.util.Set<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate> getDeclaredConsistencyPredicateSet() {
        return get$rl$declaredConsistencyPredicate();
    }
    
    public void set$declaredConsistencyPredicate(OJBFunctionalSetWrapper declaredConsistencyPredicate) {
        get$rl$declaredConsistencyPredicate().setFromOJB(this, "declaredConsistencyPredicate", declaredConsistencyPredicate);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate> getDeclaredConsistencyPredicate() {
        return getDeclaredConsistencyPredicateSet();
    }
    
    @Deprecated
    public int getDeclaredConsistencyPredicateCount() {
        return getDeclaredConsistencyPredicateSet().size();
    }
    
    public pt.ist.fenixframework.DomainFenixFrameworkRoot getDomainFenixFrameworkRoot() {
        return ((DO_State)this.get$obj$state(false)).domainFenixFrameworkRoot;
    }
    
    public void setDomainFenixFrameworkRoot(pt.ist.fenixframework.DomainFenixFrameworkRoot domainFenixFrameworkRoot) {
        getRelationDomainFenixFrameworkRootDomainMetaClasses().add((pt.ist.fenixframework.DomainMetaClass)this, domainFenixFrameworkRoot);
    }
    
    private java.lang.Long get$oidDomainFenixFrameworkRoot() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).domainFenixFrameworkRoot;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.fenixframework.DomainMetaClass getDomainMetaSuperclass() {
        return ((DO_State)this.get$obj$state(false)).domainMetaSuperclass;
    }
    
    public void setDomainMetaSuperclass(pt.ist.fenixframework.DomainMetaClass domainMetaSuperclass) {
        getRelationDomainMetaSuperclassDomainMetaSubclasses().add(domainMetaSuperclass, (pt.ist.fenixframework.DomainMetaClass)this);
    }
    
    private java.lang.Long get$oidDomainMetaSuperclass() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).domainMetaSuperclass;
        return (value == null) ? null : value.getOid();
    }
    
    public void addDomainMetaSubclass(pt.ist.fenixframework.DomainMetaClass domainMetaSubclass) {
        getRelationDomainMetaSuperclassDomainMetaSubclasses().add((pt.ist.fenixframework.DomainMetaClass)this, domainMetaSubclass);
    }
    
    public void removeDomainMetaSubclass(pt.ist.fenixframework.DomainMetaClass domainMetaSubclass) {
        getRelationDomainMetaSuperclassDomainMetaSubclasses().remove((pt.ist.fenixframework.DomainMetaClass)this, domainMetaSubclass);
    }
    
    public java.util.Set<pt.ist.fenixframework.DomainMetaClass> getDomainMetaSubclassSet() {
        return get$rl$domainMetaSubclass();
    }
    
    public void set$domainMetaSubclass(OJBFunctionalSetWrapper domainMetaSubclass) {
        get$rl$domainMetaSubclass().setFromOJB(this, "domainMetaSubclass", domainMetaSubclass);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.fenixframework.DomainMetaClass> getDomainMetaSubclass() {
        return getDomainMetaSubclassSet();
    }
    
    @Deprecated
    public int getDomainMetaSubclassCount() {
        return getDomainMetaSubclassSet().size();
    }
    
    public pt.ist.fenixframework.adt.bplustree.DomainBPlusTree getExistingDomainMetaObjects() {
        return ((DO_State)this.get$obj$state(false)).existingDomainMetaObjects;
    }
    
    public void setExistingDomainMetaObjects(pt.ist.fenixframework.adt.bplustree.DomainBPlusTree existingDomainMetaObjects) {
        getRelationDomainMetaClassExistingDomainMetaObjects().add((pt.ist.fenixframework.DomainMetaClass)this, existingDomainMetaObjects);
    }
    
    private java.lang.Long get$oidExistingDomainMetaObjects() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).existingDomainMetaObjects;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$declaredConsistencyPredicate().size() > 0) handleAttemptToDeleteConnectedObject("DeclaredConsistencyPredicate");
        if (castedState.domainFenixFrameworkRoot != null) handleAttemptToDeleteConnectedObject("DomainFenixFrameworkRoot");
        if (castedState.domainMetaSuperclass != null) handleAttemptToDeleteConnectedObject("DomainMetaSuperclass");
        if (get$rl$domainMetaSubclass().size() > 0) handleAttemptToDeleteConnectedObject("DomainMetaSubclass");
        if (castedState.existingDomainMetaObjects != null) handleAttemptToDeleteConnectedObject("ExistingDomainMetaObjects");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$domainClassName(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "DOMAIN_CLASS_NAME"), state);
        set$initialized(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readBoolean(rs, "INITIALIZED"), state);
        castedState.domainFenixFrameworkRoot = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_DOMAIN_FENIX_FRAMEWORK_ROOT");
        castedState.domainMetaSuperclass = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_DOMAIN_META_SUPERCLASS");
        castedState.existingDomainMetaObjects = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_EXISTING_DOMAIN_META_OBJECTS");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("declaredConsistencyPredicate")) return getRelationDomainMetaClassDeclaredConsistencyPredicates();
        if (attrName.equals("domainMetaSubclass")) return getRelationDomainMetaSuperclassDomainMetaSubclasses();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("declaredConsistencyPredicate", getRelationDomainMetaClassDeclaredConsistencyPredicates());
        get$$relationList("domainMetaSubclass", getRelationDomainMetaSuperclassDomainMetaSubclasses());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private java.lang.String domainClassName;
        private java.lang.Boolean initialized;
        private pt.ist.fenixframework.DomainFenixFrameworkRoot domainFenixFrameworkRoot;
        private pt.ist.fenixframework.DomainMetaClass domainMetaSuperclass;
        private pt.ist.fenixframework.adt.bplustree.DomainBPlusTree existingDomainMetaObjects;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.domainClassName = this.domainClassName;
            newCasted.initialized = this.initialized;
            newCasted.domainFenixFrameworkRoot = this.domainFenixFrameworkRoot;
            newCasted.domainMetaSuperclass = this.domainMetaSuperclass;
            newCasted.existingDomainMetaObjects = this.existingDomainMetaObjects;
            
        }
        
    }
    
}
