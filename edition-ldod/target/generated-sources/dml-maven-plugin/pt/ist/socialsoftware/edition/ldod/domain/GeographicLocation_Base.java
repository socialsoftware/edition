package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class GeographicLocation_Base extends pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria {
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
    protected  GeographicLocation_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getLocation() {
        return ((DO_State)this.get$obj$state(false)).location;
    }
    
    public void setLocation(java.lang.String location) {
        ((DO_State)this.get$obj$state(true)).location = location;
    }
    
    private java.lang.String get$location() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).location;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$location(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).location = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getCountry() {
        return ((DO_State)this.get$obj$state(false)).country;
    }
    
    public void setCountry(java.lang.String country) {
        ((DO_State)this.get$obj$state(true)).country = country;
    }
    
    private java.lang.String get$country() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).country;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$country(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).country = (java.lang.String)((value == null) ? null : value);
    }
    
    // Role Methods
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$location(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "LOCATION"), state);
        set$country(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "COUNTRY"), state);
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
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria.DO_State {
        private java.lang.String location;
        private java.lang.String country;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.location = this.location;
            newCasted.country = this.country;
            
        }
        
    }
    
}
