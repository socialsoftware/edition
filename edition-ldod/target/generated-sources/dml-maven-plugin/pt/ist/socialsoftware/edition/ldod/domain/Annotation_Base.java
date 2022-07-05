package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Annotation_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Annotation,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> role$$virtualEditionInter = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Annotation,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter getValue(pt.ist.socialsoftware.edition.ldod.domain.Annotation o1) {
            return ((Annotation_Base.DO_State)o1.get$obj$state(false)).virtualEditionInter;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Annotation o1, pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter o2) {
            ((Annotation_Base.DO_State)o1.get$obj$state(true)).virtualEditionInter = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.Annotation> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter.role$$annotation;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Annotation,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> role$$user = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Annotation,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoDUser getValue(pt.ist.socialsoftware.edition.ldod.domain.Annotation o1) {
            return ((Annotation_Base.DO_State)o1.get$obj$state(false)).user;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Annotation o1, pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o2) {
            ((Annotation_Base.DO_State)o1.get$obj$state(true)).user = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Annotation> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.role$$annotation;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Annotation,pt.ist.socialsoftware.edition.ldod.domain.Range> role$$range = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Annotation,pt.ist.socialsoftware.edition.ldod.domain.Range>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Range> getSet(pt.ist.socialsoftware.edition.ldod.domain.Annotation o1) {
            return ((Annotation_Base)o1).get$rl$range();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Range,pt.ist.socialsoftware.edition.ldod.domain.Annotation> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Range.role$$annotation;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.Annotation> getRelationVirtualEditionInterHasAnnotations() {
        return pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter.getRelationVirtualEditionInterHasAnnotations();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Annotation> getRelationLdoDUserWritesAnnotations() {
        return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.getRelationLdoDUserWritesAnnotations();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Range,pt.ist.socialsoftware.edition.ldod.domain.Annotation> getRelationAnnotationHasRanges() {
        return pt.ist.socialsoftware.edition.ldod.domain.Range.getRelationAnnotationHasRanges();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Annotation,pt.ist.socialsoftware.edition.ldod.domain.Range> get$rl$range() {
        return get$$relationList("range", getRelationAnnotationHasRanges().getInverseRelation());
        
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
    protected  Annotation_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getQuote() {
        return ((DO_State)this.get$obj$state(false)).quote;
    }
    
    public void setQuote(java.lang.String quote) {
        ((DO_State)this.get$obj$state(true)).quote = quote;
    }
    
    private java.lang.String get$quote() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).quote;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$quote(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).quote = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getText() {
        return ((DO_State)this.get$obj$state(false)).text;
    }
    
    public void setText(java.lang.String text) {
        ((DO_State)this.get$obj$state(true)).text = text;
    }
    
    private java.lang.String get$text() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).text;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$text(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).text = (java.lang.String)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter getVirtualEditionInter() {
        return ((DO_State)this.get$obj$state(false)).virtualEditionInter;
    }
    
    public void setVirtualEditionInter(pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter virtualEditionInter) {
        getRelationVirtualEditionInterHasAnnotations().add(virtualEditionInter, (pt.ist.socialsoftware.edition.ldod.domain.Annotation)this);
    }
    
    private java.lang.Long get$oidVirtualEditionInter() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).virtualEditionInter;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfVirtualEditionInter() {
        if (getVirtualEditionInter() == null) return false;
        return true;
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoDUser getUser() {
        return ((DO_State)this.get$obj$state(false)).user;
    }
    
    public void setUser(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser user) {
        getRelationLdoDUserWritesAnnotations().add(user, (pt.ist.socialsoftware.edition.ldod.domain.Annotation)this);
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
    
    public void addRange(pt.ist.socialsoftware.edition.ldod.domain.Range range) {
        getRelationAnnotationHasRanges().add(range, (pt.ist.socialsoftware.edition.ldod.domain.Annotation)this);
    }
    
    public void removeRange(pt.ist.socialsoftware.edition.ldod.domain.Range range) {
        getRelationAnnotationHasRanges().remove(range, (pt.ist.socialsoftware.edition.ldod.domain.Annotation)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Range> getRangeSet() {
        return get$rl$range();
    }
    
    public void set$range(OJBFunctionalSetWrapper range) {
        get$rl$range().setFromOJB(this, "range", range);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Range> getRange() {
        return getRangeSet();
    }
    
    @Deprecated
    public int getRangeCount() {
        return getRangeSet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.virtualEditionInter != null) handleAttemptToDeleteConnectedObject("VirtualEditionInter");
        if (castedState.user != null) handleAttemptToDeleteConnectedObject("User");
        if (get$rl$range().size() > 0) handleAttemptToDeleteConnectedObject("Range");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$quote(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "QUOTE"), state);
        set$text(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "TEXT"), state);
        castedState.virtualEditionInter = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_VIRTUAL_EDITION_INTER");
        castedState.user = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_USER");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("range")) return getRelationAnnotationHasRanges().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("range", getRelationAnnotationHasRanges().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private java.lang.String quote;
        private java.lang.String text;
        private pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter virtualEditionInter;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoDUser user;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.quote = this.quote;
            newCasted.text = this.text;
            newCasted.virtualEditionInter = this.virtualEditionInter;
            newCasted.user = this.user;
            
        }
        
    }
    
}
