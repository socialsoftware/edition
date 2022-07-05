package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Edition_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Edition,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> role$$isUsedBy = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Edition,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getSet(pt.ist.socialsoftware.edition.ldod.domain.Edition o1) {
            return ((Edition_Base)o1).get$rl$isUsedBy();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Edition> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.role$$uses;
        }
        
    };
    
    private final static class VirtualEditionUsesEdition {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Edition,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Edition,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition>(role$$isUsedBy, "VirtualEditionUsesEdition");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Edition,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getRelationVirtualEditionUsesEdition() {
        return VirtualEditionUsesEdition.relation;
    }
    
    static {
        VirtualEditionUsesEdition.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Edition.VirtualEditionUsesEdition");
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Edition,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> get$rl$isUsedBy() {
        return get$$relationList("isUsedBy", getRelationVirtualEditionUsesEdition());
        
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
    protected  Edition_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getXmlId() {
        return ((DO_State)this.get$obj$state(false)).xmlId;
    }
    
    public void setXmlId(java.lang.String xmlId) {
        ((DO_State)this.get$obj$state(true)).xmlId = xmlId;
    }
    
    private java.lang.String get$xmlId() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).xmlId;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$xmlId(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).xmlId = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getTitle() {
        return ((DO_State)this.get$obj$state(false)).title;
    }
    
    public void setTitle(java.lang.String title) {
        ((DO_State)this.get$obj$state(true)).title = title;
    }
    
    private java.lang.String get$title() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).title;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$title(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).title = (java.lang.String)((value == null) ? null : value);
    }
    
    public org.joda.time.LocalDate getDate() {
        return ((DO_State)this.get$obj$state(false)).date;
    }
    
    public void setDate(org.joda.time.LocalDate date) {
        ((DO_State)this.get$obj$state(true)).date = date;
    }
    
    private java.lang.String get$date() {
        org.joda.time.LocalDate value = ((DO_State)this.get$obj$state(false)).date;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForLocalDate(value);
    }
    
    private final void set$date(org.joda.time.LocalDate value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).date = (org.joda.time.LocalDate)((value == null) ? null : value);
    }
    
    public java.lang.String getAcronym() {
        return ((DO_State)this.get$obj$state(false)).acronym;
    }
    
    public void setAcronym(java.lang.String acronym) {
        ((DO_State)this.get$obj$state(true)).acronym = acronym;
    }
    
    private java.lang.String get$acronym() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).acronym;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$acronym(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).acronym = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.Boolean getPub() {
        return ((DO_State)this.get$obj$state(false)).pub;
    }
    
    public void setPub(java.lang.Boolean pub) {
        ((DO_State)this.get$obj$state(true)).pub = pub;
    }
    
    private java.lang.Boolean get$pub() {
        java.lang.Boolean value = ((DO_State)this.get$obj$state(false)).pub;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForBoolean(value);
    }
    
    private final void set$pub(java.lang.Boolean value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).pub = (java.lang.Boolean)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public void addIsUsedBy(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition isUsedBy) {
        getRelationVirtualEditionUsesEdition().add((pt.ist.socialsoftware.edition.ldod.domain.Edition)this, isUsedBy);
    }
    
    public void removeIsUsedBy(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition isUsedBy) {
        getRelationVirtualEditionUsesEdition().remove((pt.ist.socialsoftware.edition.ldod.domain.Edition)this, isUsedBy);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getIsUsedBySet() {
        return get$rl$isUsedBy();
    }
    
    public void set$isUsedBy(OJBFunctionalSetWrapper isUsedBy) {
        get$rl$isUsedBy().setFromOJB(this, "isUsedBy", isUsedBy);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getIsUsedBy() {
        return getIsUsedBySet();
    }
    
    @Deprecated
    public int getIsUsedByCount() {
        return getIsUsedBySet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$isUsedBy().size() > 0) handleAttemptToDeleteConnectedObject("IsUsedBy");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$xmlId(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "XML_ID"), state);
        set$title(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "TITLE"), state);
        set$date(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readLocalDate(rs, "DATE"), state);
        set$acronym(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "ACRONYM"), state);
        set$pub(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readBoolean(rs, "PUB"), state);
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("isUsedBy")) return getRelationVirtualEditionUsesEdition();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("isUsedBy", getRelationVirtualEditionUsesEdition());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private java.lang.String xmlId;
        private java.lang.String title;
        private org.joda.time.LocalDate date;
        private java.lang.String acronym;
        private java.lang.Boolean pub;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.xmlId = this.xmlId;
            newCasted.title = this.title;
            newCasted.date = this.date;
            newCasted.acronym = this.acronym;
            newCasted.pub = this.pub;
            
        }
        
    }
    
}
