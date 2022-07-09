package pt.ist.fenixframework.data;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class InstallationData_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.data.InstallationData,pt.ist.fenixframework.DomainRoot> role$$domainRoot = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.data.InstallationData,pt.ist.fenixframework.DomainRoot>() {
        @Override
        public pt.ist.fenixframework.DomainRoot getValue(pt.ist.fenixframework.data.InstallationData o1) {
            return ((InstallationData_Base.DO_State)o1.get$obj$state(false)).domainRoot;
        }
        @Override
        public void setValue(pt.ist.fenixframework.data.InstallationData o1, pt.ist.fenixframework.DomainRoot o2) {
            ((InstallationData_Base.DO_State)o1.get$obj$state(true)).domainRoot = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.DomainRoot,pt.ist.fenixframework.data.InstallationData> getInverseRole() {
            return pt.ist.fenixframework.DomainRoot.role$$installationData;
        }
        
    };
    
    private final static class DomainRootHasInstallationData {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.data.InstallationData,pt.ist.fenixframework.DomainRoot> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.data.InstallationData,pt.ist.fenixframework.DomainRoot>(role$$domainRoot, "DomainRootHasInstallationData");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.data.InstallationData,pt.ist.fenixframework.DomainRoot> getRelationDomainRootHasInstallationData() {
        return DomainRootHasInstallationData.relation;
    }
    
    static {
        DomainRootHasInstallationData.relation.setRelationName("pt.ist.fenixframework.data.InstallationData.DomainRootHasInstallationData");
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
    protected  InstallationData_Base() {
        super();
    }
    
    // Getters and Setters
    
    public pt.ist.fenixframework.data.ModuleData getModuleData() {
        return ((DO_State)this.get$obj$state(false)).moduleData;
    }
    
    public void setModuleData(pt.ist.fenixframework.data.ModuleData moduleData) {
        ((DO_State)this.get$obj$state(true)).moduleData = moduleData;
    }
    
    private java.lang.String get$moduleData() {
        pt.ist.fenixframework.data.ModuleData value = ((DO_State)this.get$obj$state(false)).moduleData;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForJsonElement(ValueTypeSerializer.serialize$ModuleData(value));
    }
    
    private final void set$moduleData(com.google.gson.JsonElement value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).moduleData = (pt.ist.fenixframework.data.ModuleData)((value == null) ? null : ValueTypeSerializer.deSerialize$ModuleData(value));
    }
    
    // Role Methods
    
    public pt.ist.fenixframework.DomainRoot getDomainRoot() {
        return ((DO_State)this.get$obj$state(false)).domainRoot;
    }
    
    public void setDomainRoot(pt.ist.fenixframework.DomainRoot domainRoot) {
        getRelationDomainRootHasInstallationData().add((pt.ist.fenixframework.data.InstallationData)this, domainRoot);
    }
    
    private java.lang.Long get$oidDomainRoot() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).domainRoot;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.domainRoot != null) handleAttemptToDeleteConnectedObject("DomainRoot");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$moduleData(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readJsonElement(rs, "MODULE_DATA"), state);
        castedState.domainRoot = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_DOMAIN_ROOT");
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
        private pt.ist.fenixframework.data.ModuleData moduleData;
        private pt.ist.fenixframework.DomainRoot domainRoot;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.moduleData = this.moduleData;
            newCasted.domainRoot = this.domainRoot;
            
        }
        
    }
    
}
