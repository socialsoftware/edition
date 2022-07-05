package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Role_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Role,pt.ist.socialsoftware.edition.ldod.domain.LdoD> role$$ldoD = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Role,pt.ist.socialsoftware.edition.ldod.domain.LdoD>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoD getValue(pt.ist.socialsoftware.edition.ldod.domain.Role o1) {
            return ((Role_Base.DO_State)o1.get$obj$state(false)).ldoD;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Role o1, pt.ist.socialsoftware.edition.ldod.domain.LdoD o2) {
            ((Role_Base.DO_State)o1.get$obj$state(true)).ldoD = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Role> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoD.role$$roles;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Role,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> role$$users = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Role,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getSet(pt.ist.socialsoftware.edition.ldod.domain.Role o1) {
            return ((Role_Base)o1).get$rl$users();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Role> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.role$$roles;
        }
        
    };
    
    private final static class LdoDHasRoles {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Role,pt.ist.socialsoftware.edition.ldod.domain.LdoD> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Role,pt.ist.socialsoftware.edition.ldod.domain.LdoD>(role$$ldoD, "LdoDHasRoles");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Role,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getRelationLdoDHasRoles() {
        return LdoDHasRoles.relation;
    }
    
    static {
        LdoDHasRoles.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Role.LdoDHasRoles");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Role> getRelationLdoDUsersAndRoles() {
        return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.getRelationLdoDUsersAndRoles();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Role,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> get$rl$users() {
        return get$$relationList("users", getRelationLdoDUsersAndRoles().getInverseRelation());
        
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
    protected  Role_Base() {
        super();
    }
    
    // Getters and Setters
    
    public Role.RoleType getType() {
        return ((DO_State)this.get$obj$state(false)).type;
    }
    
    public void setType(Role.RoleType type) {
        ((DO_State)this.get$obj$state(true)).type = type;
    }
    
    private java.lang.String get$type() {
        Role.RoleType value = ((DO_State)this.get$obj$state(false)).type;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$type(Role.RoleType value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).type = (Role.RoleType)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoD getLdoD() {
        return ((DO_State)this.get$obj$state(false)).ldoD;
    }
    
    public void setLdoD(pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD) {
        getRelationLdoDHasRoles().add((pt.ist.socialsoftware.edition.ldod.domain.Role)this, ldoD);
    }
    
    private java.lang.Long get$oidLdoD() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).ldoD;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfLdoD() {
        if (getLdoD() == null) return false;
        return true;
    }
    
    public void addUsers(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser users) {
        getRelationLdoDUsersAndRoles().add(users, (pt.ist.socialsoftware.edition.ldod.domain.Role)this);
    }
    
    public void removeUsers(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser users) {
        getRelationLdoDUsersAndRoles().remove(users, (pt.ist.socialsoftware.edition.ldod.domain.Role)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getUsersSet() {
        return get$rl$users();
    }
    
    public void set$users(OJBFunctionalSetWrapper users) {
        get$rl$users().setFromOJB(this, "users", users);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getUsers() {
        return getUsersSet();
    }
    
    @Deprecated
    public int getUsersCount() {
        return getUsersSet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.ldoD != null) handleAttemptToDeleteConnectedObject("LdoD");
        if (get$rl$users().size() > 0) handleAttemptToDeleteConnectedObject("Users");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$type(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(Role.RoleType.class, rs, "TYPE"), state);
        castedState.ldoD = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_LDO_D");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("users")) return getRelationLdoDUsersAndRoles().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("users", getRelationLdoDUsersAndRoles().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private Role.RoleType type;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.type = this.type;
            newCasted.ldoD = this.ldoD;
            
        }
        
    }
    
}
