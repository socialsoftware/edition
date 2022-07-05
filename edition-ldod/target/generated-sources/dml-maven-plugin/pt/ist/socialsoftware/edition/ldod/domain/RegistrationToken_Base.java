package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class RegistrationToken_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> role$$user = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoDUser getValue(pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken o1) {
            return ((RegistrationToken_Base.DO_State)o1.get$obj$state(false)).user;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken o1, pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o2) {
            ((RegistrationToken_Base.DO_State)o1.get$obj$state(true)).user = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.role$$token;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken,pt.ist.socialsoftware.edition.ldod.domain.LdoD> role$$ldoD = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken,pt.ist.socialsoftware.edition.ldod.domain.LdoD>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoD getValue(pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken o1) {
            return ((RegistrationToken_Base.DO_State)o1.get$obj$state(false)).ldoD;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken o1, pt.ist.socialsoftware.edition.ldod.domain.LdoD o2) {
            ((RegistrationToken_Base.DO_State)o1.get$obj$state(true)).ldoD = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoD.role$$token;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken> getRelationLdoDUserHasRegistrationToken() {
        return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.getRelationLdoDUserHasRegistrationToken();
    }
    
    private final static class LdoDHasRegistrationToken {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken,pt.ist.socialsoftware.edition.ldod.domain.LdoD> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken,pt.ist.socialsoftware.edition.ldod.domain.LdoD>(role$$ldoD, "LdoDHasRegistrationToken");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getRelationLdoDHasRegistrationToken() {
        return LdoDHasRegistrationToken.relation;
    }
    
    static {
        LdoDHasRegistrationToken.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken.LdoDHasRegistrationToken");
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
    protected  RegistrationToken_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getToken() {
        return ((DO_State)this.get$obj$state(false)).token;
    }
    
    public void setToken(java.lang.String token) {
        ((DO_State)this.get$obj$state(true)).token = token;
    }
    
    private java.lang.String get$token() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).token;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$token(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).token = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.Long getExpireTime() {
        return ((DO_State)this.get$obj$state(false)).expireTime;
    }
    
    public void setExpireTime(java.lang.Long expireTime) {
        ((DO_State)this.get$obj$state(true)).expireTime = expireTime;
    }
    
    private java.lang.Long get$expireTime() {
        java.lang.Long value = ((DO_State)this.get$obj$state(false)).expireTime;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForLong(value);
    }
    
    private final void set$expireTime(java.lang.Long value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).expireTime = (java.lang.Long)((value == null) ? null : value);
    }
    
    public boolean getAuthorized() {
        return ((DO_State)this.get$obj$state(false)).authorized;
    }
    
    public void setAuthorized(boolean authorized) {
        ((DO_State)this.get$obj$state(true)).authorized = authorized;
    }
    
    private boolean get$authorized() {
        boolean value = ((DO_State)this.get$obj$state(false)).authorized;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForboolean(value);
    }
    
    private final void set$authorized(boolean value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).authorized = (boolean)(value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoDUser getUser() {
        return ((DO_State)this.get$obj$state(false)).user;
    }
    
    public void setUser(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser user) {
        getRelationLdoDUserHasRegistrationToken().add(user, (pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken)this);
    }
    
    private java.lang.Long get$oidUser() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).user;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfUser() {
        if (getUser() == null) return false;
        return true;
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoD getLdoD() {
        return ((DO_State)this.get$obj$state(false)).ldoD;
    }
    
    public void setLdoD(pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD) {
        getRelationLdoDHasRegistrationToken().add((pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken)this, ldoD);
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
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.user != null) handleAttemptToDeleteConnectedObject("User");
        if (castedState.ldoD != null) handleAttemptToDeleteConnectedObject("LdoD");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$token(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "TOKEN"), state);
        set$expireTime(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readLong(rs, "EXPIRE_TIME"), state);
        set$authorized(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readboolean(rs, "AUTHORIZED"), state);
        castedState.user = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_USER");
        castedState.ldoD = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_LDO_D");
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
        private java.lang.String token;
        private java.lang.Long expireTime;
        private boolean authorized;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoDUser user;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.token = this.token;
            newCasted.expireTime = this.expireTime;
            newCasted.authorized = this.authorized;
            newCasted.user = this.user;
            newCasted.ldoD = this.ldoD;
            
        }
        
    }
    
}
