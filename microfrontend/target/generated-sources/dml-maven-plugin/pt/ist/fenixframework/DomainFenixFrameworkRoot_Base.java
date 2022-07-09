package pt.ist.fenixframework;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class DomainFenixFrameworkRoot_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.DomainFenixFrameworkRoot,pt.ist.fenixframework.DomainMetaClass> role$$domainMetaClass = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.DomainFenixFrameworkRoot,pt.ist.fenixframework.DomainMetaClass>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.fenixframework.DomainMetaClass> getSet(pt.ist.fenixframework.DomainFenixFrameworkRoot o1) {
            return ((DomainFenixFrameworkRoot_Base)o1).get$rl$domainMetaClass();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainFenixFrameworkRoot> getInverseRole() {
            return pt.ist.fenixframework.DomainMetaClass.role$$domainFenixFrameworkRoot;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.DomainFenixFrameworkRoot,pt.ist.fenixframework.DomainRoot> role$$domainRoot = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.DomainFenixFrameworkRoot,pt.ist.fenixframework.DomainRoot>() {
        @Override
        public pt.ist.fenixframework.DomainRoot getValue(pt.ist.fenixframework.DomainFenixFrameworkRoot o1) {
            return ((DomainFenixFrameworkRoot_Base.DO_State)o1.get$obj$state(false)).domainRoot;
        }
        @Override
        public void setValue(pt.ist.fenixframework.DomainFenixFrameworkRoot o1, pt.ist.fenixframework.DomainRoot o2) {
            ((DomainFenixFrameworkRoot_Base.DO_State)o1.get$obj$state(true)).domainRoot = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.DomainRoot,pt.ist.fenixframework.DomainFenixFrameworkRoot> getInverseRole() {
            return pt.ist.fenixframework.DomainRoot.role$$domainFenixFrameworkRoot;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.DomainMetaClass,pt.ist.fenixframework.DomainFenixFrameworkRoot> getRelationDomainFenixFrameworkRootDomainMetaClasses() {
        return pt.ist.fenixframework.DomainMetaClass.getRelationDomainFenixFrameworkRootDomainMetaClasses();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.DomainRoot,pt.ist.fenixframework.DomainFenixFrameworkRoot> getRelationDomainRootDomainFenixFrameworkRoot() {
        return pt.ist.fenixframework.DomainRoot.getRelationDomainRootDomainFenixFrameworkRoot();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.fenixframework.DomainFenixFrameworkRoot,pt.ist.fenixframework.DomainMetaClass> get$rl$domainMetaClass() {
        return get$$relationList("domainMetaClass", getRelationDomainFenixFrameworkRootDomainMetaClasses().getInverseRelation());
        
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
    protected  DomainFenixFrameworkRoot_Base() {
        super();
    }
    
    // Getters and Setters
    
    // Role Methods
    
    public void addDomainMetaClass(pt.ist.fenixframework.DomainMetaClass domainMetaClass) {
        getRelationDomainFenixFrameworkRootDomainMetaClasses().add(domainMetaClass, (pt.ist.fenixframework.DomainFenixFrameworkRoot)this);
    }
    
    public void removeDomainMetaClass(pt.ist.fenixframework.DomainMetaClass domainMetaClass) {
        getRelationDomainFenixFrameworkRootDomainMetaClasses().remove(domainMetaClass, (pt.ist.fenixframework.DomainFenixFrameworkRoot)this);
    }
    
    public java.util.Set<pt.ist.fenixframework.DomainMetaClass> getDomainMetaClassSet() {
        return get$rl$domainMetaClass();
    }
    
    public void set$domainMetaClass(OJBFunctionalSetWrapper domainMetaClass) {
        get$rl$domainMetaClass().setFromOJB(this, "domainMetaClass", domainMetaClass);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.fenixframework.DomainMetaClass> getDomainMetaClass() {
        return getDomainMetaClassSet();
    }
    
    @Deprecated
    public int getDomainMetaClassCount() {
        return getDomainMetaClassSet().size();
    }
    
    public pt.ist.fenixframework.DomainRoot getDomainRoot() {
        return ((DO_State)this.get$obj$state(false)).domainRoot;
    }
    
    public void setDomainRoot(pt.ist.fenixframework.DomainRoot domainRoot) {
        getRelationDomainRootDomainFenixFrameworkRoot().add(domainRoot, (pt.ist.fenixframework.DomainFenixFrameworkRoot)this);
    }
    
    private java.lang.Long get$oidDomainRoot() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).domainRoot;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$domainMetaClass().size() > 0) handleAttemptToDeleteConnectedObject("DomainMetaClass");
        if (castedState.domainRoot != null) handleAttemptToDeleteConnectedObject("DomainRoot");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        castedState.domainRoot = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_DOMAIN_ROOT");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("domainMetaClass")) return getRelationDomainFenixFrameworkRootDomainMetaClasses().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("domainMetaClass", getRelationDomainFenixFrameworkRootDomainMetaClasses().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private pt.ist.fenixframework.DomainRoot domainRoot;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.domainRoot = this.domainRoot;
            
        }
        
    }
    
}
