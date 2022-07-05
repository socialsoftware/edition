package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class AwareAnnotation_Base extends pt.ist.socialsoftware.edition.ldod.domain.Annotation {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation,pt.ist.socialsoftware.edition.ldod.domain.Citation> role$$citation = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation,pt.ist.socialsoftware.edition.ldod.domain.Citation>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Citation getValue(pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation o1) {
            return ((AwareAnnotation_Base.DO_State)o1.get$obj$state(false)).citation;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation o1, pt.ist.socialsoftware.edition.ldod.domain.Citation o2) {
            ((AwareAnnotation_Base.DO_State)o1.get$obj$state(true)).citation = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Citation,pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Citation.role$$awareAnnotation;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Citation,pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation> getRelationCitationHasAwareAnnotations() {
        return pt.ist.socialsoftware.edition.ldod.domain.Citation.getRelationCitationHasAwareAnnotations();
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
    protected  AwareAnnotation_Base() {
        super();
    }
    
    // Getters and Setters
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.Citation getCitation() {
        return ((DO_State)this.get$obj$state(false)).citation;
    }
    
    public void setCitation(pt.ist.socialsoftware.edition.ldod.domain.Citation citation) {
        getRelationCitationHasAwareAnnotations().add(citation, (pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation)this);
    }
    
    private java.lang.Long get$oidCitation() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).citation;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfCitation() {
        if (getCitation() == null) return false;
        return true;
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.citation != null) handleAttemptToDeleteConnectedObject("Citation");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        castedState.citation = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_CITATION");
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
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.Annotation.DO_State {
        private pt.ist.socialsoftware.edition.ldod.domain.Citation citation;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.citation = this.citation;
            
        }
        
    }
    
}
