package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class UserConnection_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.UserConnection,pt.ist.socialsoftware.edition.ldod.domain.LdoD> role$$ldoD = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.UserConnection,pt.ist.socialsoftware.edition.ldod.domain.LdoD>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoD getValue(pt.ist.socialsoftware.edition.ldod.domain.UserConnection o1) {
            return ((UserConnection_Base.DO_State)o1.get$obj$state(false)).ldoD;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.UserConnection o1, pt.ist.socialsoftware.edition.ldod.domain.LdoD o2) {
            ((UserConnection_Base.DO_State)o1.get$obj$state(true)).ldoD = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.UserConnection> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoD.role$$userConnection;
        }
        
    };
    
    private final static class LdoDHasUserConnections {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.UserConnection,pt.ist.socialsoftware.edition.ldod.domain.LdoD> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.UserConnection,pt.ist.socialsoftware.edition.ldod.domain.LdoD>(role$$ldoD, "LdoDHasUserConnections");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.UserConnection,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getRelationLdoDHasUserConnections() {
        return LdoDHasUserConnections.relation;
    }
    
    static {
        LdoDHasUserConnections.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.UserConnection.LdoDHasUserConnections");
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
    protected  UserConnection_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getUserId() {
        return ((DO_State)this.get$obj$state(false)).userId;
    }
    
    public void setUserId(java.lang.String userId) {
        ((DO_State)this.get$obj$state(true)).userId = userId;
    }
    
    private java.lang.String get$userId() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).userId;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$userId(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).userId = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getProviderId() {
        return ((DO_State)this.get$obj$state(false)).providerId;
    }
    
    public void setProviderId(java.lang.String providerId) {
        ((DO_State)this.get$obj$state(true)).providerId = providerId;
    }
    
    private java.lang.String get$providerId() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).providerId;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$providerId(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).providerId = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getProviderUserId() {
        return ((DO_State)this.get$obj$state(false)).providerUserId;
    }
    
    public void setProviderUserId(java.lang.String providerUserId) {
        ((DO_State)this.get$obj$state(true)).providerUserId = providerUserId;
    }
    
    private java.lang.String get$providerUserId() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).providerUserId;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$providerUserId(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).providerUserId = (java.lang.String)((value == null) ? null : value);
    }
    
    public int getRank() {
        return ((DO_State)this.get$obj$state(false)).rank;
    }
    
    public void setRank(int rank) {
        ((DO_State)this.get$obj$state(true)).rank = rank;
    }
    
    private int get$rank() {
        int value = ((DO_State)this.get$obj$state(false)).rank;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$rank(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).rank = (int)(value);
    }
    
    public java.lang.String getDisplayName() {
        return ((DO_State)this.get$obj$state(false)).displayName;
    }
    
    public void setDisplayName(java.lang.String displayName) {
        ((DO_State)this.get$obj$state(true)).displayName = displayName;
    }
    
    private java.lang.String get$displayName() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).displayName;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$displayName(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).displayName = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getProfileUrl() {
        return ((DO_State)this.get$obj$state(false)).profileUrl;
    }
    
    public void setProfileUrl(java.lang.String profileUrl) {
        ((DO_State)this.get$obj$state(true)).profileUrl = profileUrl;
    }
    
    private java.lang.String get$profileUrl() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).profileUrl;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$profileUrl(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).profileUrl = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getImageUrl() {
        return ((DO_State)this.get$obj$state(false)).imageUrl;
    }
    
    public void setImageUrl(java.lang.String imageUrl) {
        ((DO_State)this.get$obj$state(true)).imageUrl = imageUrl;
    }
    
    private java.lang.String get$imageUrl() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).imageUrl;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$imageUrl(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).imageUrl = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getAccessToken() {
        return ((DO_State)this.get$obj$state(false)).accessToken;
    }
    
    public void setAccessToken(java.lang.String accessToken) {
        ((DO_State)this.get$obj$state(true)).accessToken = accessToken;
    }
    
    private java.lang.String get$accessToken() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).accessToken;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$accessToken(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).accessToken = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getSecret() {
        return ((DO_State)this.get$obj$state(false)).secret;
    }
    
    public void setSecret(java.lang.String secret) {
        ((DO_State)this.get$obj$state(true)).secret = secret;
    }
    
    private java.lang.String get$secret() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).secret;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$secret(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).secret = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getRefreshToken() {
        return ((DO_State)this.get$obj$state(false)).refreshToken;
    }
    
    public void setRefreshToken(java.lang.String refreshToken) {
        ((DO_State)this.get$obj$state(true)).refreshToken = refreshToken;
    }
    
    private java.lang.String get$refreshToken() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).refreshToken;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$refreshToken(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).refreshToken = (java.lang.String)((value == null) ? null : value);
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
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoD getLdoD() {
        return ((DO_State)this.get$obj$state(false)).ldoD;
    }
    
    public void setLdoD(pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD) {
        getRelationLdoDHasUserConnections().add((pt.ist.socialsoftware.edition.ldod.domain.UserConnection)this, ldoD);
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
        if (castedState.ldoD != null) handleAttemptToDeleteConnectedObject("LdoD");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$userId(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "USER_ID"), state);
        set$providerId(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "PROVIDER_ID"), state);
        set$providerUserId(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "PROVIDER_USER_ID"), state);
        set$rank(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "RANK"), state);
        set$displayName(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "DISPLAY_NAME"), state);
        set$profileUrl(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "PROFILE_URL"), state);
        set$imageUrl(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "IMAGE_URL"), state);
        set$accessToken(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "ACCESS_TOKEN"), state);
        set$secret(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "SECRET"), state);
        set$refreshToken(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "REFRESH_TOKEN"), state);
        set$expireTime(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readLong(rs, "EXPIRE_TIME"), state);
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
        private java.lang.String userId;
        private java.lang.String providerId;
        private java.lang.String providerUserId;
        private int rank;
        private java.lang.String displayName;
        private java.lang.String profileUrl;
        private java.lang.String imageUrl;
        private java.lang.String accessToken;
        private java.lang.String secret;
        private java.lang.String refreshToken;
        private java.lang.Long expireTime;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.userId = this.userId;
            newCasted.providerId = this.providerId;
            newCasted.providerUserId = this.providerUserId;
            newCasted.rank = this.rank;
            newCasted.displayName = this.displayName;
            newCasted.profileUrl = this.profileUrl;
            newCasted.imageUrl = this.imageUrl;
            newCasted.accessToken = this.accessToken;
            newCasted.secret = this.secret;
            newCasted.refreshToken = this.refreshToken;
            newCasted.expireTime = this.expireTime;
            newCasted.ldoD = this.ldoD;
            
        }
        
    }
    
}
