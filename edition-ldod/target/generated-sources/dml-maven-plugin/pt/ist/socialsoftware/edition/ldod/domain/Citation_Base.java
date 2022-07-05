package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Citation_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Citation,pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation> role$$awareAnnotation = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Citation,pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation> getSet(pt.ist.socialsoftware.edition.ldod.domain.Citation o1) {
            return ((Citation_Base)o1).get$rl$awareAnnotation();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation,pt.ist.socialsoftware.edition.ldod.domain.Citation> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation.role$$citation;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Citation,pt.ist.socialsoftware.edition.ldod.domain.Fragment> role$$fragment = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Citation,pt.ist.socialsoftware.edition.ldod.domain.Fragment>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Fragment getValue(pt.ist.socialsoftware.edition.ldod.domain.Citation o1) {
            return ((Citation_Base.DO_State)o1.get$obj$state(false)).fragment;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Citation o1, pt.ist.socialsoftware.edition.ldod.domain.Fragment o2) {
            ((Citation_Base.DO_State)o1.get$obj$state(true)).fragment = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.Citation> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Fragment.role$$citation;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Citation,pt.ist.socialsoftware.edition.ldod.domain.InfoRange> role$$infoRange = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Citation,pt.ist.socialsoftware.edition.ldod.domain.InfoRange>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.InfoRange> getSet(pt.ist.socialsoftware.edition.ldod.domain.Citation o1) {
            return ((Citation_Base)o1).get$rl$infoRange();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.InfoRange,pt.ist.socialsoftware.edition.ldod.domain.Citation> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.InfoRange.role$$citation;
        }
        
    };
    
    private final static class CitationHasAwareAnnotations {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Citation,pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Citation,pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation>(role$$awareAnnotation, "CitationHasAwareAnnotations");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Citation,pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation> getRelationCitationHasAwareAnnotations() {
        return CitationHasAwareAnnotations.relation;
    }
    
    static {
        CitationHasAwareAnnotations.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Citation.CitationHasAwareAnnotations");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.Citation> getRelationFragmentHasCitations() {
        return pt.ist.socialsoftware.edition.ldod.domain.Fragment.getRelationFragmentHasCitations();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.InfoRange,pt.ist.socialsoftware.edition.ldod.domain.Citation> getRelationCitationHasInfoRanges() {
        return pt.ist.socialsoftware.edition.ldod.domain.InfoRange.getRelationCitationHasInfoRanges();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Citation,pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation> get$rl$awareAnnotation() {
        return get$$relationList("awareAnnotation", getRelationCitationHasAwareAnnotations());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Citation,pt.ist.socialsoftware.edition.ldod.domain.InfoRange> get$rl$infoRange() {
        return get$$relationList("infoRange", getRelationCitationHasInfoRanges().getInverseRelation());
        
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
    protected  Citation_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getSourceLink() {
        return ((DO_State)this.get$obj$state(false)).sourceLink;
    }
    
    public void setSourceLink(java.lang.String sourceLink) {
        ((DO_State)this.get$obj$state(true)).sourceLink = sourceLink;
    }
    
    private java.lang.String get$sourceLink() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).sourceLink;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$sourceLink(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).sourceLink = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getDate() {
        return ((DO_State)this.get$obj$state(false)).date;
    }
    
    public void setDate(java.lang.String date) {
        ((DO_State)this.get$obj$state(true)).date = date;
    }
    
    private java.lang.String get$date() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).date;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$date(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).date = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getFragText() {
        return ((DO_State)this.get$obj$state(false)).fragText;
    }
    
    public void setFragText(java.lang.String fragText) {
        ((DO_State)this.get$obj$state(true)).fragText = fragText;
    }
    
    private java.lang.String get$fragText() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).fragText;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$fragText(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).fragText = (java.lang.String)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public void addAwareAnnotation(pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation awareAnnotation) {
        getRelationCitationHasAwareAnnotations().add((pt.ist.socialsoftware.edition.ldod.domain.Citation)this, awareAnnotation);
    }
    
    public void removeAwareAnnotation(pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation awareAnnotation) {
        getRelationCitationHasAwareAnnotations().remove((pt.ist.socialsoftware.edition.ldod.domain.Citation)this, awareAnnotation);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation> getAwareAnnotationSet() {
        return get$rl$awareAnnotation();
    }
    
    public void set$awareAnnotation(OJBFunctionalSetWrapper awareAnnotation) {
        get$rl$awareAnnotation().setFromOJB(this, "awareAnnotation", awareAnnotation);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation> getAwareAnnotation() {
        return getAwareAnnotationSet();
    }
    
    @Deprecated
    public int getAwareAnnotationCount() {
        return getAwareAnnotationSet().size();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.Fragment getFragment() {
        return ((DO_State)this.get$obj$state(false)).fragment;
    }
    
    public void setFragment(pt.ist.socialsoftware.edition.ldod.domain.Fragment fragment) {
        getRelationFragmentHasCitations().add(fragment, (pt.ist.socialsoftware.edition.ldod.domain.Citation)this);
    }
    
    private java.lang.Long get$oidFragment() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).fragment;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfFragment() {
        if (getFragment() == null) return false;
        return true;
    }
    
    public void addInfoRange(pt.ist.socialsoftware.edition.ldod.domain.InfoRange infoRange) {
        getRelationCitationHasInfoRanges().add(infoRange, (pt.ist.socialsoftware.edition.ldod.domain.Citation)this);
    }
    
    public void removeInfoRange(pt.ist.socialsoftware.edition.ldod.domain.InfoRange infoRange) {
        getRelationCitationHasInfoRanges().remove(infoRange, (pt.ist.socialsoftware.edition.ldod.domain.Citation)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.InfoRange> getInfoRangeSet() {
        return get$rl$infoRange();
    }
    
    public void set$infoRange(OJBFunctionalSetWrapper infoRange) {
        get$rl$infoRange().setFromOJB(this, "infoRange", infoRange);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.InfoRange> getInfoRange() {
        return getInfoRangeSet();
    }
    
    @Deprecated
    public int getInfoRangeCount() {
        return getInfoRangeSet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$awareAnnotation().size() > 0) handleAttemptToDeleteConnectedObject("AwareAnnotation");
        if (castedState.fragment != null) handleAttemptToDeleteConnectedObject("Fragment");
        if (get$rl$infoRange().size() > 0) handleAttemptToDeleteConnectedObject("InfoRange");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$sourceLink(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "SOURCE_LINK"), state);
        set$date(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "DATE"), state);
        set$fragText(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "FRAG_TEXT"), state);
        castedState.fragment = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_FRAGMENT");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("awareAnnotation")) return getRelationCitationHasAwareAnnotations();
        if (attrName.equals("infoRange")) return getRelationCitationHasInfoRanges().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("awareAnnotation", getRelationCitationHasAwareAnnotations());
        get$$relationList("infoRange", getRelationCitationHasInfoRanges().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private java.lang.String sourceLink;
        private java.lang.String date;
        private java.lang.String fragText;
        private pt.ist.socialsoftware.edition.ldod.domain.Fragment fragment;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.sourceLink = this.sourceLink;
            newCasted.date = this.date;
            newCasted.fragText = this.fragText;
            newCasted.fragment = this.fragment;
            
        }
        
    }
    
}
