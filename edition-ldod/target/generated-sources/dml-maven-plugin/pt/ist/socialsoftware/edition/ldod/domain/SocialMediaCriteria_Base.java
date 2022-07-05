package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class SocialMediaCriteria_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> role$$virtualEdition = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition getValue(pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria o1) {
            return ((SocialMediaCriteria_Base.DO_State)o1.get$obj$state(false)).virtualEdition;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria o1, pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o2) {
            ((SocialMediaCriteria_Base.DO_State)o1.get$obj$state(true)).virtualEdition = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.role$$criteria;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria> getRelationVirtualEditionHasSocialMediaCriteria() {
        return pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.getRelationVirtualEditionHasSocialMediaCriteria();
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
    protected  SocialMediaCriteria_Base() {
        super();
    }
    
    // Getters and Setters
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition getVirtualEdition() {
        return ((DO_State)this.get$obj$state(false)).virtualEdition;
    }
    
    public void setVirtualEdition(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition virtualEdition) {
        getRelationVirtualEditionHasSocialMediaCriteria().add(virtualEdition, (pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria)this);
    }
    
    private java.lang.Long get$oidVirtualEdition() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).virtualEdition;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfVirtualEdition() {
        if (getVirtualEdition() == null) return false;
        return true;
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.virtualEdition != null) handleAttemptToDeleteConnectedObject("VirtualEdition");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        castedState.virtualEdition = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_VIRTUAL_EDITION");
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
        private pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition virtualEdition;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.virtualEdition = this.virtualEdition;
            
        }
        
    }
    
}
