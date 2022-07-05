package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class NullEdition_Base extends pt.ist.socialsoftware.edition.ldod.domain.Edition {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.NullEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD> role$$ldoD4NullEdition = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.NullEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoD getValue(pt.ist.socialsoftware.edition.ldod.domain.NullEdition o1) {
            return ((NullEdition_Base.DO_State)o1.get$obj$state(false)).ldoD4NullEdition;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.NullEdition o1, pt.ist.socialsoftware.edition.ldod.domain.LdoD o2) {
            ((NullEdition_Base.DO_State)o1.get$obj$state(true)).ldoD4NullEdition = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.NullEdition> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoD.role$$nullEdition;
        }
        
    };
    
    private final static class LdoDHasNullEdition {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.NullEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.NullEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD>(role$$ldoD4NullEdition, "LdoDHasNullEdition");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.NullEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getRelationLdoDHasNullEdition() {
        return LdoDHasNullEdition.relation;
    }
    
    static {
        LdoDHasNullEdition.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.NullEdition.LdoDHasNullEdition");
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
    protected  NullEdition_Base() {
        super();
    }
    
    // Getters and Setters
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoD getLdoD4NullEdition() {
        return ((DO_State)this.get$obj$state(false)).ldoD4NullEdition;
    }
    
    public void setLdoD4NullEdition(pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD4NullEdition) {
        getRelationLdoDHasNullEdition().add((pt.ist.socialsoftware.edition.ldod.domain.NullEdition)this, ldoD4NullEdition);
    }
    
    private java.lang.Long get$oidLdoD4NullEdition() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).ldoD4NullEdition;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfLdoD4NullEdition() {
        if (getLdoD4NullEdition() == null) return false;
        return true;
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.ldoD4NullEdition != null) handleAttemptToDeleteConnectedObject("LdoD4NullEdition");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        castedState.ldoD4NullEdition = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_LDO_D4_NULL_EDITION");
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
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.Edition.DO_State {
        private pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD4NullEdition;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.ldoD4NullEdition = this.ldoD4NullEdition;
            
        }
        
    }
    
}
