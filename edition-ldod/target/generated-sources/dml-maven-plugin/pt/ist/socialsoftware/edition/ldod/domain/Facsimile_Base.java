package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Facsimile_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Facsimile,pt.ist.socialsoftware.edition.ldod.domain.Source> role$$source = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Facsimile,pt.ist.socialsoftware.edition.ldod.domain.Source>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Source getValue(pt.ist.socialsoftware.edition.ldod.domain.Facsimile o1) {
            return ((Facsimile_Base.DO_State)o1.get$obj$state(false)).source;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Facsimile o1, pt.ist.socialsoftware.edition.ldod.domain.Source o2) {
            ((Facsimile_Base.DO_State)o1.get$obj$state(true)).source = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Facsimile> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Source.role$$facsimile;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Facsimile,pt.ist.socialsoftware.edition.ldod.domain.Surface> role$$firstSurface = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Facsimile,pt.ist.socialsoftware.edition.ldod.domain.Surface>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Surface getValue(pt.ist.socialsoftware.edition.ldod.domain.Facsimile o1) {
            return ((Facsimile_Base.DO_State)o1.get$obj$state(false)).firstSurface;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Facsimile o1, pt.ist.socialsoftware.edition.ldod.domain.Surface o2) {
            ((Facsimile_Base.DO_State)o1.get$obj$state(true)).firstSurface = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.Facsimile> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Surface.role$$facsimile;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Facsimile> getRelationSourceHasFacsimile() {
        return pt.ist.socialsoftware.edition.ldod.domain.Source.getRelationSourceHasFacsimile();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.Facsimile> getRelationFacsimileHasSurface() {
        return pt.ist.socialsoftware.edition.ldod.domain.Surface.getRelationFacsimileHasSurface();
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
    protected  Facsimile_Base() {
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
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.Source getSource() {
        return ((DO_State)this.get$obj$state(false)).source;
    }
    
    public void setSource(pt.ist.socialsoftware.edition.ldod.domain.Source source) {
        getRelationSourceHasFacsimile().add(source, (pt.ist.socialsoftware.edition.ldod.domain.Facsimile)this);
    }
    
    private java.lang.Long get$oidSource() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).source;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfSource() {
        if (getSource() == null) return false;
        return true;
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.Surface getFirstSurface() {
        return ((DO_State)this.get$obj$state(false)).firstSurface;
    }
    
    public void setFirstSurface(pt.ist.socialsoftware.edition.ldod.domain.Surface firstSurface) {
        getRelationFacsimileHasSurface().add(firstSurface, (pt.ist.socialsoftware.edition.ldod.domain.Facsimile)this);
    }
    
    private java.lang.Long get$oidFirstSurface() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).firstSurface;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.source != null) handleAttemptToDeleteConnectedObject("Source");
        if (castedState.firstSurface != null) handleAttemptToDeleteConnectedObject("FirstSurface");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$xmlId(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "XML_ID"), state);
        castedState.source = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_SOURCE");
        castedState.firstSurface = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_FIRST_SURFACE");
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
        private java.lang.String xmlId;
        private pt.ist.socialsoftware.edition.ldod.domain.Source source;
        private pt.ist.socialsoftware.edition.ldod.domain.Surface firstSurface;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.xmlId = this.xmlId;
            newCasted.source = this.source;
            newCasted.firstSurface = this.firstSurface;
            
        }
        
    }
    
}
