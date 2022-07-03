package pt.ist.fenixframework.consistencyPredicates;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class PublicConsistencyPredicate_Base extends pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate> role$$publicConsistencyPredicateOverriding = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate> getSet(pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate o1) {
            return ((PublicConsistencyPredicate_Base)o1).get$rl$publicConsistencyPredicateOverriding();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate> getInverseRole() {
            return pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate.role$$publicConsistencyPredicateOverridden;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate> role$$publicConsistencyPredicateOverridden = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate>() {
        @Override
        public pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate getValue(pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate o1) {
            return ((PublicConsistencyPredicate_Base.DO_State)o1.get$obj$state(false)).publicConsistencyPredicateOverridden;
        }
        @Override
        public void setValue(pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate o1, pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate o2) {
            ((PublicConsistencyPredicate_Base.DO_State)o1.get$obj$state(true)).publicConsistencyPredicateOverridden = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate> getInverseRole() {
            return pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate.role$$publicConsistencyPredicateOverriding;
        }
        
    };
    
    
    private final static class PublicConsistencyPredicateOverriddenPublicConsistencyPredicatesOverriding {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate>(role$$publicConsistencyPredicateOverridden, "PublicConsistencyPredicateOverriddenPublicConsistencyPredicatesOverriding");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate> getRelationPublicConsistencyPredicateOverriddenPublicConsistencyPredicatesOverriding() {
        return PublicConsistencyPredicateOverriddenPublicConsistencyPredicatesOverriding.relation;
    }
    
    static {
        PublicConsistencyPredicateOverriddenPublicConsistencyPredicatesOverriding.relation.setRelationName("pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate.PublicConsistencyPredicateOverriddenPublicConsistencyPredicatesOverriding");
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate,pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate> get$rl$publicConsistencyPredicateOverriding() {
        return get$$relationList("publicConsistencyPredicateOverriding", getRelationPublicConsistencyPredicateOverriddenPublicConsistencyPredicatesOverriding().getInverseRelation());
        
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
    protected  PublicConsistencyPredicate_Base() {
        super();
    }
    
    // Getters and Setters
    
    // Role Methods
    
    public void addPublicConsistencyPredicateOverriding(pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate publicConsistencyPredicateOverriding) {
        getRelationPublicConsistencyPredicateOverriddenPublicConsistencyPredicatesOverriding().add(publicConsistencyPredicateOverriding, (pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate)this);
    }
    
    public void removePublicConsistencyPredicateOverriding(pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate publicConsistencyPredicateOverriding) {
        getRelationPublicConsistencyPredicateOverriddenPublicConsistencyPredicatesOverriding().remove(publicConsistencyPredicateOverriding, (pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate)this);
    }
    
    public java.util.Set<pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate> getPublicConsistencyPredicateOverridingSet() {
        return get$rl$publicConsistencyPredicateOverriding();
    }
    
    public void set$publicConsistencyPredicateOverriding(OJBFunctionalSetWrapper publicConsistencyPredicateOverriding) {
        get$rl$publicConsistencyPredicateOverriding().setFromOJB(this, "publicConsistencyPredicateOverriding", publicConsistencyPredicateOverriding);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate> getPublicConsistencyPredicateOverriding() {
        return getPublicConsistencyPredicateOverridingSet();
    }
    
    @Deprecated
    public int getPublicConsistencyPredicateOverridingCount() {
        return getPublicConsistencyPredicateOverridingSet().size();
    }
    
    public pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate getPublicConsistencyPredicateOverridden() {
        return ((DO_State)this.get$obj$state(false)).publicConsistencyPredicateOverridden;
    }
    
    public void setPublicConsistencyPredicateOverridden(pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate publicConsistencyPredicateOverridden) {
        getRelationPublicConsistencyPredicateOverriddenPublicConsistencyPredicatesOverriding().add((pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate)this, publicConsistencyPredicateOverridden);
    }
    
    private java.lang.Long get$oidPublicConsistencyPredicateOverridden() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).publicConsistencyPredicateOverridden;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$publicConsistencyPredicateOverriding().size() > 0) handleAttemptToDeleteConnectedObject("PublicConsistencyPredicateOverriding");
        if (castedState.publicConsistencyPredicateOverridden != null) handleAttemptToDeleteConnectedObject("PublicConsistencyPredicateOverridden");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        castedState.publicConsistencyPredicateOverridden = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_PUBLIC_CONSISTENCY_PREDICATE_OVERRIDDEN");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("publicConsistencyPredicateOverriding")) return getRelationPublicConsistencyPredicateOverriddenPublicConsistencyPredicatesOverriding().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("publicConsistencyPredicateOverriding", getRelationPublicConsistencyPredicateOverriddenPublicConsistencyPredicatesOverriding().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate.DO_State {
        private pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate publicConsistencyPredicateOverridden;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.publicConsistencyPredicateOverridden = this.publicConsistencyPredicateOverridden;
            
        }
        
    }
    
}
