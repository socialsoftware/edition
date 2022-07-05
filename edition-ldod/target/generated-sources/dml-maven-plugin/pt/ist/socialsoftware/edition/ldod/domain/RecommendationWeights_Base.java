package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class RecommendationWeights_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> role$$user = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoDUser getValue(pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights o1) {
            return ((RecommendationWeights_Base.DO_State)o1.get$obj$state(false)).user;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights o1, pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o2) {
            ((RecommendationWeights_Base.DO_State)o1.get$obj$state(true)).user = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.role$$recommendationWeights;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> role$$virtualEdition = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition getValue(pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights o1) {
            return ((RecommendationWeights_Base.DO_State)o1.get$obj$state(false)).virtualEdition;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights o1, pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o2) {
            ((RecommendationWeights_Base.DO_State)o1.get$obj$state(true)).virtualEdition = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.role$$recommendationWeights;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights> getRelationLdoDUserHasRecommendationWeights() {
        return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.getRelationLdoDUserHasRecommendationWeights();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights> getRelationVirtualEditionHasRecommendationWeights() {
        return pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.getRelationVirtualEditionHasRecommendationWeights();
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
    protected  RecommendationWeights_Base() {
        super();
    }
    
    // Getters and Setters
    
    public double getHeteronymWeight() {
        return ((DO_State)this.get$obj$state(false)).heteronymWeight;
    }
    
    public void setHeteronymWeight(double heteronymWeight) {
        ((DO_State)this.get$obj$state(true)).heteronymWeight = heteronymWeight;
    }
    
    private double get$heteronymWeight() {
        double value = ((DO_State)this.get$obj$state(false)).heteronymWeight;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueFordouble(value);
    }
    
    private final void set$heteronymWeight(double value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).heteronymWeight = (double)(value);
    }
    
    public double getDateWeight() {
        return ((DO_State)this.get$obj$state(false)).dateWeight;
    }
    
    public void setDateWeight(double dateWeight) {
        ((DO_State)this.get$obj$state(true)).dateWeight = dateWeight;
    }
    
    private double get$dateWeight() {
        double value = ((DO_State)this.get$obj$state(false)).dateWeight;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueFordouble(value);
    }
    
    private final void set$dateWeight(double value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).dateWeight = (double)(value);
    }
    
    public double getTextWeight() {
        return ((DO_State)this.get$obj$state(false)).textWeight;
    }
    
    public void setTextWeight(double textWeight) {
        ((DO_State)this.get$obj$state(true)).textWeight = textWeight;
    }
    
    private double get$textWeight() {
        double value = ((DO_State)this.get$obj$state(false)).textWeight;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueFordouble(value);
    }
    
    private final void set$textWeight(double value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).textWeight = (double)(value);
    }
    
    public double getTaxonomyWeight() {
        return ((DO_State)this.get$obj$state(false)).taxonomyWeight;
    }
    
    public void setTaxonomyWeight(double taxonomyWeight) {
        ((DO_State)this.get$obj$state(true)).taxonomyWeight = taxonomyWeight;
    }
    
    private double get$taxonomyWeight() {
        double value = ((DO_State)this.get$obj$state(false)).taxonomyWeight;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueFordouble(value);
    }
    
    private final void set$taxonomyWeight(double value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).taxonomyWeight = (double)(value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoDUser getUser() {
        return ((DO_State)this.get$obj$state(false)).user;
    }
    
    public void setUser(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser user) {
        getRelationLdoDUserHasRecommendationWeights().add(user, (pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights)this);
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
    
    public pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition getVirtualEdition() {
        return ((DO_State)this.get$obj$state(false)).virtualEdition;
    }
    
    public void setVirtualEdition(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition virtualEdition) {
        getRelationVirtualEditionHasRecommendationWeights().add(virtualEdition, (pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights)this);
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
        if (castedState.user != null) handleAttemptToDeleteConnectedObject("User");
        if (castedState.virtualEdition != null) handleAttemptToDeleteConnectedObject("VirtualEdition");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$heteronymWeight(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readdouble(rs, "HETERONYM_WEIGHT"), state);
        set$dateWeight(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readdouble(rs, "DATE_WEIGHT"), state);
        set$textWeight(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readdouble(rs, "TEXT_WEIGHT"), state);
        set$taxonomyWeight(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readdouble(rs, "TAXONOMY_WEIGHT"), state);
        castedState.user = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_USER");
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
        private double heteronymWeight;
        private double dateWeight;
        private double textWeight;
        private double taxonomyWeight;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoDUser user;
        private pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition virtualEdition;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.heteronymWeight = this.heteronymWeight;
            newCasted.dateWeight = this.dateWeight;
            newCasted.textWeight = this.textWeight;
            newCasted.taxonomyWeight = this.taxonomyWeight;
            newCasted.user = this.user;
            newCasted.virtualEdition = this.virtualEdition;
            
        }
        
    }
    
}
