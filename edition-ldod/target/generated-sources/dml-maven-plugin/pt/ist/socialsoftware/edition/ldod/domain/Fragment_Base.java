package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Fragment_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> role$$textPortion = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.TextPortion>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getValue(pt.ist.socialsoftware.edition.ldod.domain.Fragment o1) {
            return ((Fragment_Base.DO_State)o1.get$obj$state(false)).textPortion;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Fragment o1, pt.ist.socialsoftware.edition.ldod.domain.TextPortion o2) {
            ((Fragment_Base.DO_State)o1.get$obj$state(true)).textPortion = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.Fragment> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.TextPortion.role$$fragment;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.LdoD> role$$ldoD = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.LdoD>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoD getValue(pt.ist.socialsoftware.edition.ldod.domain.Fragment o1) {
            return ((Fragment_Base.DO_State)o1.get$obj$state(false)).ldoD;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Fragment o1, pt.ist.socialsoftware.edition.ldod.domain.LdoD o2) {
            ((Fragment_Base.DO_State)o1.get$obj$state(true)).ldoD = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Fragment> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoD.role$$fragments;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.FragInter> role$$fragmentInter = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.FragInter>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.FragInter> getSet(pt.ist.socialsoftware.edition.ldod.domain.Fragment o1) {
            return ((Fragment_Base)o1).get$rl$fragmentInter();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.Fragment> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.FragInter.role$$fragment;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.RefText> role$$refText = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.RefText>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.RefText> getSet(pt.ist.socialsoftware.edition.ldod.domain.Fragment o1) {
            return ((Fragment_Base)o1).get$rl$refText();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.Fragment> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.RefText.role$$refFrag;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.Source> role$$sources = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.Source>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Source> getSet(pt.ist.socialsoftware.edition.ldod.domain.Fragment o1) {
            return ((Fragment_Base)o1).get$rl$sources();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Fragment> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Source.role$$fragment;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.Citation> role$$citation = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.Citation>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Citation> getSet(pt.ist.socialsoftware.edition.ldod.domain.Fragment o1) {
            return ((Fragment_Base)o1).get$rl$citation();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Citation,pt.ist.socialsoftware.edition.ldod.domain.Fragment> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Citation.role$$fragment;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.Fragment> getRelationFragmentHasTextPortion() {
        return pt.ist.socialsoftware.edition.ldod.domain.TextPortion.getRelationFragmentHasTextPortion();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Fragment> getRelationLdoDHasFragments() {
        return pt.ist.socialsoftware.edition.ldod.domain.LdoD.getRelationLdoDHasFragments();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.Fragment> getRelationFragmentHasFragInters() {
        return pt.ist.socialsoftware.edition.ldod.domain.FragInter.getRelationFragmentHasFragInters();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.Fragment> getRelationRefText2Fragment() {
        return pt.ist.socialsoftware.edition.ldod.domain.RefText.getRelationRefText2Fragment();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Fragment> getRelationFragmentHasSources() {
        return pt.ist.socialsoftware.edition.ldod.domain.Source.getRelationFragmentHasSources();
    }
    
    private final static class FragmentHasCitations {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.Citation> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.Citation>(role$$citation, "FragmentHasCitations");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.Citation> getRelationFragmentHasCitations() {
        return FragmentHasCitations.relation;
    }
    
    static {
        FragmentHasCitations.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Fragment.FragmentHasCitations");
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.FragInter> get$rl$fragmentInter() {
        return get$$relationList("fragmentInter", getRelationFragmentHasFragInters().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.RefText> get$rl$refText() {
        return get$$relationList("refText", getRelationRefText2Fragment().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.Source> get$rl$sources() {
        return get$$relationList("sources", getRelationFragmentHasSources().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.Citation> get$rl$citation() {
        return get$$relationList("citation", getRelationFragmentHasCitations());
        
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
    protected  Fragment_Base() {
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
    
    public java.lang.String getTitle() {
        return ((DO_State)this.get$obj$state(false)).title;
    }
    
    public void setTitle(java.lang.String title) {
        ((DO_State)this.get$obj$state(true)).title = title;
    }
    
    private java.lang.String get$title() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).title;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$title(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).title = (java.lang.String)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getTextPortion() {
        return ((DO_State)this.get$obj$state(false)).textPortion;
    }
    
    public void setTextPortion(pt.ist.socialsoftware.edition.ldod.domain.TextPortion textPortion) {
        getRelationFragmentHasTextPortion().add(textPortion, (pt.ist.socialsoftware.edition.ldod.domain.Fragment)this);
    }
    
    private java.lang.Long get$oidTextPortion() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).textPortion;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoD getLdoD() {
        return ((DO_State)this.get$obj$state(false)).ldoD;
    }
    
    public void setLdoD(pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD) {
        getRelationLdoDHasFragments().add(ldoD, (pt.ist.socialsoftware.edition.ldod.domain.Fragment)this);
    }
    
    private java.lang.Long get$oidLdoD() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).ldoD;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfLdoD() {
        if (getLdoD() == null) return false;
        return true;
    }
    
    public void addFragmentInter(pt.ist.socialsoftware.edition.ldod.domain.FragInter fragmentInter) {
        getRelationFragmentHasFragInters().add(fragmentInter, (pt.ist.socialsoftware.edition.ldod.domain.Fragment)this);
    }
    
    public void removeFragmentInter(pt.ist.socialsoftware.edition.ldod.domain.FragInter fragmentInter) {
        getRelationFragmentHasFragInters().remove(fragmentInter, (pt.ist.socialsoftware.edition.ldod.domain.Fragment)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.FragInter> getFragmentInterSet() {
        return get$rl$fragmentInter();
    }
    
    public void set$fragmentInter(OJBFunctionalSetWrapper fragmentInter) {
        get$rl$fragmentInter().setFromOJB(this, "fragmentInter", fragmentInter);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.FragInter> getFragmentInter() {
        return getFragmentInterSet();
    }
    
    @Deprecated
    public int getFragmentInterCount() {
        return getFragmentInterSet().size();
    }
    
    public void addRefText(pt.ist.socialsoftware.edition.ldod.domain.RefText refText) {
        getRelationRefText2Fragment().add(refText, (pt.ist.socialsoftware.edition.ldod.domain.Fragment)this);
    }
    
    public void removeRefText(pt.ist.socialsoftware.edition.ldod.domain.RefText refText) {
        getRelationRefText2Fragment().remove(refText, (pt.ist.socialsoftware.edition.ldod.domain.Fragment)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.RefText> getRefTextSet() {
        return get$rl$refText();
    }
    
    public void set$refText(OJBFunctionalSetWrapper refText) {
        get$rl$refText().setFromOJB(this, "refText", refText);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.RefText> getRefText() {
        return getRefTextSet();
    }
    
    @Deprecated
    public int getRefTextCount() {
        return getRefTextSet().size();
    }
    
    public void addSources(pt.ist.socialsoftware.edition.ldod.domain.Source sources) {
        getRelationFragmentHasSources().add(sources, (pt.ist.socialsoftware.edition.ldod.domain.Fragment)this);
    }
    
    public void removeSources(pt.ist.socialsoftware.edition.ldod.domain.Source sources) {
        getRelationFragmentHasSources().remove(sources, (pt.ist.socialsoftware.edition.ldod.domain.Fragment)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Source> getSourcesSet() {
        return get$rl$sources();
    }
    
    public void set$sources(OJBFunctionalSetWrapper sources) {
        get$rl$sources().setFromOJB(this, "sources", sources);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Source> getSources() {
        return getSourcesSet();
    }
    
    @Deprecated
    public int getSourcesCount() {
        return getSourcesSet().size();
    }
    
    public void addCitation(pt.ist.socialsoftware.edition.ldod.domain.Citation citation) {
        getRelationFragmentHasCitations().add((pt.ist.socialsoftware.edition.ldod.domain.Fragment)this, citation);
    }
    
    public void removeCitation(pt.ist.socialsoftware.edition.ldod.domain.Citation citation) {
        getRelationFragmentHasCitations().remove((pt.ist.socialsoftware.edition.ldod.domain.Fragment)this, citation);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Citation> getCitationSet() {
        return get$rl$citation();
    }
    
    public void set$citation(OJBFunctionalSetWrapper citation) {
        get$rl$citation().setFromOJB(this, "citation", citation);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Citation> getCitation() {
        return getCitationSet();
    }
    
    @Deprecated
    public int getCitationCount() {
        return getCitationSet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.textPortion != null) handleAttemptToDeleteConnectedObject("TextPortion");
        if (castedState.ldoD != null) handleAttemptToDeleteConnectedObject("LdoD");
        if (get$rl$fragmentInter().size() > 0) handleAttemptToDeleteConnectedObject("FragmentInter");
        if (get$rl$refText().size() > 0) handleAttemptToDeleteConnectedObject("RefText");
        if (get$rl$sources().size() > 0) handleAttemptToDeleteConnectedObject("Sources");
        if (get$rl$citation().size() > 0) handleAttemptToDeleteConnectedObject("Citation");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$xmlId(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "XML_ID"), state);
        set$title(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "TITLE"), state);
        castedState.textPortion = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_TEXT_PORTION");
        castedState.ldoD = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_LDO_D");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("fragmentInter")) return getRelationFragmentHasFragInters().getInverseRelation();
        if (attrName.equals("refText")) return getRelationRefText2Fragment().getInverseRelation();
        if (attrName.equals("sources")) return getRelationFragmentHasSources().getInverseRelation();
        if (attrName.equals("citation")) return getRelationFragmentHasCitations();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("fragmentInter", getRelationFragmentHasFragInters().getInverseRelation());
        get$$relationList("refText", getRelationRefText2Fragment().getInverseRelation());
        get$$relationList("sources", getRelationFragmentHasSources().getInverseRelation());
        get$$relationList("citation", getRelationFragmentHasCitations());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private java.lang.String xmlId;
        private java.lang.String title;
        private pt.ist.socialsoftware.edition.ldod.domain.TextPortion textPortion;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.xmlId = this.xmlId;
            newCasted.title = this.title;
            newCasted.textPortion = this.textPortion;
            newCasted.ldoD = this.ldoD;
            
        }
        
    }
    
}
