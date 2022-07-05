package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Member_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Member,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> role$$virtualEdition = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Member,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition getValue(pt.ist.socialsoftware.edition.ldod.domain.Member o1) {
            return ((Member_Base.DO_State)o1.get$obj$state(false)).virtualEdition;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Member o1, pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o2) {
            ((Member_Base.DO_State)o1.get$obj$state(true)).virtualEdition = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Member> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.role$$member;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Member,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> role$$user = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Member,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoDUser getValue(pt.ist.socialsoftware.edition.ldod.domain.Member o1) {
            return ((Member_Base.DO_State)o1.get$obj$state(false)).user;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Member o1, pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o2) {
            ((Member_Base.DO_State)o1.get$obj$state(true)).user = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Member> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.role$$member;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Member> getRelationMemberHasVirtualEdition() {
        return pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.getRelationMemberHasVirtualEdition();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Member> getRelationMemberHasLdoDUser() {
        return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.getRelationMemberHasLdoDUser();
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
    protected  Member_Base() {
        super();
    }
    
    // Getters and Setters
    
    public Member.MemberRole getRole() {
        return ((DO_State)this.get$obj$state(false)).role;
    }
    
    public void setRole(Member.MemberRole role) {
        ((DO_State)this.get$obj$state(true)).role = role;
    }
    
    private java.lang.String get$role() {
        Member.MemberRole value = ((DO_State)this.get$obj$state(false)).role;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$role(Member.MemberRole value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).role = (Member.MemberRole)((value == null) ? null : value);
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
    
    public boolean getActive() {
        return ((DO_State)this.get$obj$state(false)).active;
    }
    
    public void setActive(boolean active) {
        ((DO_State)this.get$obj$state(true)).active = active;
    }
    
    private boolean get$active() {
        boolean value = ((DO_State)this.get$obj$state(false)).active;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForboolean(value);
    }
    
    private final void set$active(boolean value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).active = (boolean)(value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition getVirtualEdition() {
        return ((DO_State)this.get$obj$state(false)).virtualEdition;
    }
    
    public void setVirtualEdition(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition virtualEdition) {
        getRelationMemberHasVirtualEdition().add(virtualEdition, (pt.ist.socialsoftware.edition.ldod.domain.Member)this);
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
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoDUser getUser() {
        return ((DO_State)this.get$obj$state(false)).user;
    }
    
    public void setUser(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser user) {
        getRelationMemberHasLdoDUser().add(user, (pt.ist.socialsoftware.edition.ldod.domain.Member)this);
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
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.virtualEdition != null) handleAttemptToDeleteConnectedObject("VirtualEdition");
        if (castedState.user != null) handleAttemptToDeleteConnectedObject("User");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$role(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(Member.MemberRole.class, rs, "ROLE"), state);
        set$date(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readLocalDate(rs, "DATE"), state);
        set$active(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readboolean(rs, "ACTIVE"), state);
        castedState.virtualEdition = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_VIRTUAL_EDITION");
        castedState.user = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_USER");
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
        private Member.MemberRole role;
        private org.joda.time.LocalDate date;
        private boolean active;
        private pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition virtualEdition;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoDUser user;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.role = this.role;
            newCasted.date = this.date;
            newCasted.active = this.active;
            newCasted.virtualEdition = this.virtualEdition;
            newCasted.user = this.user;
            
        }
        
    }
    
}
