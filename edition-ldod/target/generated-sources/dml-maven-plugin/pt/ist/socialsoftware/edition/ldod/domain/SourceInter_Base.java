package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class SourceInter_Base extends pt.ist.socialsoftware.edition.ldod.domain.FragInter {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.SourceInter,pt.ist.socialsoftware.edition.ldod.domain.Source> role$$source = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.SourceInter,pt.ist.socialsoftware.edition.ldod.domain.Source>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Source getValue(pt.ist.socialsoftware.edition.ldod.domain.SourceInter o1) {
            return ((SourceInter_Base.DO_State)o1.get$obj$state(false)).source;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.SourceInter o1, pt.ist.socialsoftware.edition.ldod.domain.Source o2) {
            ((SourceInter_Base.DO_State)o1.get$obj$state(true)).source = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.SourceInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Source.role$$sourceInters;
        }
        
    };
    
    private final static class SourceHasSourceInters {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.SourceInter,pt.ist.socialsoftware.edition.ldod.domain.Source> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.SourceInter,pt.ist.socialsoftware.edition.ldod.domain.Source>(role$$source, "SourceHasSourceInters");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.SourceInter,pt.ist.socialsoftware.edition.ldod.domain.Source> getRelationSourceHasSourceInters() {
        return SourceHasSourceInters.relation;
    }
    
    static {
        SourceHasSourceInters.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.SourceInter.SourceHasSourceInters");
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
    protected  SourceInter_Base() {
        super();
    }
    
    // Getters and Setters
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.Source getSource() {
        return ((DO_State)this.get$obj$state(false)).source;
    }
    
    public void setSource(pt.ist.socialsoftware.edition.ldod.domain.Source source) {
        getRelationSourceHasSourceInters().add((pt.ist.socialsoftware.edition.ldod.domain.SourceInter)this, source);
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
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.source != null) handleAttemptToDeleteConnectedObject("Source");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        castedState.source = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_SOURCE");
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
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.FragInter.DO_State {
        private pt.ist.socialsoftware.edition.ldod.domain.Source source;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.source = this.source;
            
        }
        
    }
    
}
