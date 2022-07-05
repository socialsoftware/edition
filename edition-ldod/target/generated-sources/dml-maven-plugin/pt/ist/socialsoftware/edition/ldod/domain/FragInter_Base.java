package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class FragInter_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.PbText> role$$pbText = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.PbText>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.PbText> getSet(pt.ist.socialsoftware.edition.ldod.domain.FragInter o1) {
            return ((FragInter_Base)o1).get$rl$pbText();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.PbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.PbText.role$$fragInter;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.InfoRange> role$$infoRange = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.InfoRange>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.InfoRange> getSet(pt.ist.socialsoftware.edition.ldod.domain.FragInter o1) {
            return ((FragInter_Base)o1).get$rl$infoRange();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.InfoRange,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.InfoRange.role$$fragInter;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.Fragment> role$$fragment = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.Fragment>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Fragment getValue(pt.ist.socialsoftware.edition.ldod.domain.FragInter o1) {
            return ((FragInter_Base.DO_State)o1.get$obj$state(false)).fragment;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.FragInter o1, pt.ist.socialsoftware.edition.ldod.domain.Fragment o2) {
            ((FragInter_Base.DO_State)o1.get$obj$state(true)).fragment = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Fragment.role$$fragmentInter;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.RefText> role$$refText = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.RefText>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.RefText> getSet(pt.ist.socialsoftware.edition.ldod.domain.FragInter o1) {
            return ((FragInter_Base)o1).get$rl$refText();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.RefText.role$$fragInter;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.Heteronym> role$$heteronym = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.Heteronym>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Heteronym getValue(pt.ist.socialsoftware.edition.ldod.domain.FragInter o1) {
            return ((FragInter_Base.DO_State)o1.get$obj$state(false)).heteronym;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.FragInter o1, pt.ist.socialsoftware.edition.ldod.domain.Heteronym o2) {
            ((FragInter_Base.DO_State)o1.get$obj$state(true)).heteronym = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Heteronym,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Heteronym.role$$fragInter;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.LbText> role$$lbText = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.LbText>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.LbText> getSet(pt.ist.socialsoftware.edition.ldod.domain.FragInter o1) {
            return ((FragInter_Base)o1).get$rl$lbText();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LbText.role$$fragInter;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.LdoDDate> role$$ldoDDate = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.LdoDDate>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoDDate getValue(pt.ist.socialsoftware.edition.ldod.domain.FragInter o1) {
            return ((FragInter_Base.DO_State)o1.get$obj$state(false)).ldoDDate;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.FragInter o1, pt.ist.socialsoftware.edition.ldod.domain.LdoDDate o2) {
            ((FragInter_Base.DO_State)o1.get$obj$state(true)).ldoDDate = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoDDate,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoDDate.role$$fragInter;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.RdgText> role$$rdg = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.RdgText>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.RdgText> getSet(pt.ist.socialsoftware.edition.ldod.domain.FragInter o1) {
            return ((FragInter_Base)o1).get$rl$rdg();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.RdgText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.RdgText.role$$fragInters;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> role$$annexNote = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.AnnexNote>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> getSet(pt.ist.socialsoftware.edition.ldod.domain.FragInter o1) {
            return ((FragInter_Base)o1).get$rl$annexNote();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.AnnexNote,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.AnnexNote.role$$fragInter;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> role$$isUsedBy = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getSet(pt.ist.socialsoftware.edition.ldod.domain.FragInter o1) {
            return ((FragInter_Base)o1).get$rl$isUsedBy();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter.role$$uses;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.PbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getRelationPbTextHasFragInters() {
        return pt.ist.socialsoftware.edition.ldod.domain.PbText.getRelationPbTextHasFragInters();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.InfoRange,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getRelationInfoRangeHasFragInter() {
        return pt.ist.socialsoftware.edition.ldod.domain.InfoRange.getRelationInfoRangeHasFragInter();
    }
    
    private final static class FragmentHasFragInters {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.Fragment> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.Fragment>(role$$fragment, "FragmentHasFragInters");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.Fragment> getRelationFragmentHasFragInters() {
        return FragmentHasFragInters.relation;
    }
    
    static {
        FragmentHasFragInters.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.FragInter.FragmentHasFragInters");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getRelationRefText2FragInter() {
        return pt.ist.socialsoftware.edition.ldod.domain.RefText.getRelationRefText2FragInter();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Heteronym,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getRelationFragInterAttributesHeteronym() {
        return pt.ist.socialsoftware.edition.ldod.domain.Heteronym.getRelationFragInterAttributesHeteronym();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getRelationLbTextHasFragInters() {
        return pt.ist.socialsoftware.edition.ldod.domain.LbText.getRelationLbTextHasFragInters();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDDate,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getRelationFragInterHasDate() {
        return pt.ist.socialsoftware.edition.ldod.domain.LdoDDate.getRelationFragInterHasDate();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.RdgText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getRelationFragInterHasRdgTexts() {
        return pt.ist.socialsoftware.edition.ldod.domain.RdgText.getRelationFragInterHasRdgTexts();
    }
    
    private final static class FragInterHasAnnexNote {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.AnnexNote>(role$$annexNote, "FragInterHasAnnexNote");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> getRelationFragInterHasAnnexNote() {
        return FragInterHasAnnexNote.relation;
    }
    
    static {
        FragInterHasAnnexNote.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.FragInter.FragInterHasAnnexNote");
    }
    
    private final static class VirtualEditionInterUsesFragInter {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter>(role$$isUsedBy, "VirtualEditionInterUsesFragInter");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getRelationVirtualEditionInterUsesFragInter() {
        return VirtualEditionInterUsesFragInter.relation;
    }
    
    static {
        VirtualEditionInterUsesFragInter.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.FragInter.VirtualEditionInterUsesFragInter");
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.PbText> get$rl$pbText() {
        return get$$relationList("pbText", getRelationPbTextHasFragInters().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.InfoRange> get$rl$infoRange() {
        return get$$relationList("infoRange", getRelationInfoRangeHasFragInter().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.RefText> get$rl$refText() {
        return get$$relationList("refText", getRelationRefText2FragInter().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.LbText> get$rl$lbText() {
        return get$$relationList("lbText", getRelationLbTextHasFragInters().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.RdgText> get$rl$rdg() {
        return get$$relationList("rdg", getRelationFragInterHasRdgTexts().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> get$rl$annexNote() {
        return get$$relationList("annexNote", getRelationFragInterHasAnnexNote());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> get$rl$isUsedBy() {
        return get$$relationList("isUsedBy", getRelationVirtualEditionInterUsesFragInter());
        
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
    protected  FragInter_Base() {
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
    
    public int getNumAnnexNotes() {
        return ((DO_State)this.get$obj$state(false)).numAnnexNotes;
    }
    
    public void setNumAnnexNotes(int numAnnexNotes) {
        ((DO_State)this.get$obj$state(true)).numAnnexNotes = numAnnexNotes;
    }
    
    private int get$numAnnexNotes() {
        int value = ((DO_State)this.get$obj$state(false)).numAnnexNotes;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$numAnnexNotes(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).numAnnexNotes = (int)(value);
    }
    
    // Role Methods
    
    public void addPbText(pt.ist.socialsoftware.edition.ldod.domain.PbText pbText) {
        getRelationPbTextHasFragInters().add(pbText, (pt.ist.socialsoftware.edition.ldod.domain.FragInter)this);
    }
    
    public void removePbText(pt.ist.socialsoftware.edition.ldod.domain.PbText pbText) {
        getRelationPbTextHasFragInters().remove(pbText, (pt.ist.socialsoftware.edition.ldod.domain.FragInter)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.PbText> getPbTextSet() {
        return get$rl$pbText();
    }
    
    public void set$pbText(OJBFunctionalSetWrapper pbText) {
        get$rl$pbText().setFromOJB(this, "pbText", pbText);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.PbText> getPbText() {
        return getPbTextSet();
    }
    
    @Deprecated
    public int getPbTextCount() {
        return getPbTextSet().size();
    }
    
    public void addInfoRange(pt.ist.socialsoftware.edition.ldod.domain.InfoRange infoRange) {
        getRelationInfoRangeHasFragInter().add(infoRange, (pt.ist.socialsoftware.edition.ldod.domain.FragInter)this);
    }
    
    public void removeInfoRange(pt.ist.socialsoftware.edition.ldod.domain.InfoRange infoRange) {
        getRelationInfoRangeHasFragInter().remove(infoRange, (pt.ist.socialsoftware.edition.ldod.domain.FragInter)this);
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
    
    public pt.ist.socialsoftware.edition.ldod.domain.Fragment getFragment() {
        return ((DO_State)this.get$obj$state(false)).fragment;
    }
    
    public void setFragment(pt.ist.socialsoftware.edition.ldod.domain.Fragment fragment) {
        getRelationFragmentHasFragInters().add((pt.ist.socialsoftware.edition.ldod.domain.FragInter)this, fragment);
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
    
    public void addRefText(pt.ist.socialsoftware.edition.ldod.domain.RefText refText) {
        getRelationRefText2FragInter().add(refText, (pt.ist.socialsoftware.edition.ldod.domain.FragInter)this);
    }
    
    public void removeRefText(pt.ist.socialsoftware.edition.ldod.domain.RefText refText) {
        getRelationRefText2FragInter().remove(refText, (pt.ist.socialsoftware.edition.ldod.domain.FragInter)this);
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
    
    public pt.ist.socialsoftware.edition.ldod.domain.Heteronym getHeteronym() {
        return ((DO_State)this.get$obj$state(false)).heteronym;
    }
    
    public void setHeteronym(pt.ist.socialsoftware.edition.ldod.domain.Heteronym heteronym) {
        getRelationFragInterAttributesHeteronym().add(heteronym, (pt.ist.socialsoftware.edition.ldod.domain.FragInter)this);
    }
    
    private java.lang.Long get$oidHeteronym() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).heteronym;
        return (value == null) ? null : value.getOid();
    }
    
    public void addLbText(pt.ist.socialsoftware.edition.ldod.domain.LbText lbText) {
        getRelationLbTextHasFragInters().add(lbText, (pt.ist.socialsoftware.edition.ldod.domain.FragInter)this);
    }
    
    public void removeLbText(pt.ist.socialsoftware.edition.ldod.domain.LbText lbText) {
        getRelationLbTextHasFragInters().remove(lbText, (pt.ist.socialsoftware.edition.ldod.domain.FragInter)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.LbText> getLbTextSet() {
        return get$rl$lbText();
    }
    
    public void set$lbText(OJBFunctionalSetWrapper lbText) {
        get$rl$lbText().setFromOJB(this, "lbText", lbText);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.LbText> getLbText() {
        return getLbTextSet();
    }
    
    @Deprecated
    public int getLbTextCount() {
        return getLbTextSet().size();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoDDate getLdoDDate() {
        return ((DO_State)this.get$obj$state(false)).ldoDDate;
    }
    
    public void setLdoDDate(pt.ist.socialsoftware.edition.ldod.domain.LdoDDate ldoDDate) {
        getRelationFragInterHasDate().add(ldoDDate, (pt.ist.socialsoftware.edition.ldod.domain.FragInter)this);
    }
    
    private java.lang.Long get$oidLdoDDate() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).ldoDDate;
        return (value == null) ? null : value.getOid();
    }
    
    public void addRdg(pt.ist.socialsoftware.edition.ldod.domain.RdgText rdg) {
        getRelationFragInterHasRdgTexts().add(rdg, (pt.ist.socialsoftware.edition.ldod.domain.FragInter)this);
    }
    
    public void removeRdg(pt.ist.socialsoftware.edition.ldod.domain.RdgText rdg) {
        getRelationFragInterHasRdgTexts().remove(rdg, (pt.ist.socialsoftware.edition.ldod.domain.FragInter)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.RdgText> getRdgSet() {
        return get$rl$rdg();
    }
    
    public void set$rdg(OJBFunctionalSetWrapper rdg) {
        get$rl$rdg().setFromOJB(this, "rdg", rdg);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.RdgText> getRdg() {
        return getRdgSet();
    }
    
    @Deprecated
    public int getRdgCount() {
        return getRdgSet().size();
    }
    
    public void addAnnexNote(pt.ist.socialsoftware.edition.ldod.domain.AnnexNote annexNote) {
        getRelationFragInterHasAnnexNote().add((pt.ist.socialsoftware.edition.ldod.domain.FragInter)this, annexNote);
    }
    
    public void removeAnnexNote(pt.ist.socialsoftware.edition.ldod.domain.AnnexNote annexNote) {
        getRelationFragInterHasAnnexNote().remove((pt.ist.socialsoftware.edition.ldod.domain.FragInter)this, annexNote);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> getAnnexNoteSet() {
        return get$rl$annexNote();
    }
    
    public void set$annexNote(OJBFunctionalSetWrapper annexNote) {
        get$rl$annexNote().setFromOJB(this, "annexNote", annexNote);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> getAnnexNote() {
        return getAnnexNoteSet();
    }
    
    @Deprecated
    public int getAnnexNoteCount() {
        return getAnnexNoteSet().size();
    }
    
    public void addIsUsedBy(pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter isUsedBy) {
        getRelationVirtualEditionInterUsesFragInter().add((pt.ist.socialsoftware.edition.ldod.domain.FragInter)this, isUsedBy);
    }
    
    public void removeIsUsedBy(pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter isUsedBy) {
        getRelationVirtualEditionInterUsesFragInter().remove((pt.ist.socialsoftware.edition.ldod.domain.FragInter)this, isUsedBy);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getIsUsedBySet() {
        return get$rl$isUsedBy();
    }
    
    public void set$isUsedBy(OJBFunctionalSetWrapper isUsedBy) {
        get$rl$isUsedBy().setFromOJB(this, "isUsedBy", isUsedBy);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getIsUsedBy() {
        return getIsUsedBySet();
    }
    
    @Deprecated
    public int getIsUsedByCount() {
        return getIsUsedBySet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$pbText().size() > 0) handleAttemptToDeleteConnectedObject("PbText");
        if (get$rl$infoRange().size() > 0) handleAttemptToDeleteConnectedObject("InfoRange");
        if (castedState.fragment != null) handleAttemptToDeleteConnectedObject("Fragment");
        if (get$rl$refText().size() > 0) handleAttemptToDeleteConnectedObject("RefText");
        if (castedState.heteronym != null) handleAttemptToDeleteConnectedObject("Heteronym");
        if (get$rl$lbText().size() > 0) handleAttemptToDeleteConnectedObject("LbText");
        if (castedState.ldoDDate != null) handleAttemptToDeleteConnectedObject("LdoDDate");
        if (get$rl$rdg().size() > 0) handleAttemptToDeleteConnectedObject("Rdg");
        if (get$rl$annexNote().size() > 0) handleAttemptToDeleteConnectedObject("AnnexNote");
        if (get$rl$isUsedBy().size() > 0) handleAttemptToDeleteConnectedObject("IsUsedBy");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$xmlId(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "XML_ID"), state);
        set$numAnnexNotes(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "NUM_ANNEX_NOTES"), state);
        castedState.fragment = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_FRAGMENT");
        castedState.heteronym = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_HETERONYM");
        castedState.ldoDDate = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_LDO_D_DATE");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("pbText")) return getRelationPbTextHasFragInters().getInverseRelation();
        if (attrName.equals("infoRange")) return getRelationInfoRangeHasFragInter().getInverseRelation();
        if (attrName.equals("refText")) return getRelationRefText2FragInter().getInverseRelation();
        if (attrName.equals("lbText")) return getRelationLbTextHasFragInters().getInverseRelation();
        if (attrName.equals("rdg")) return getRelationFragInterHasRdgTexts().getInverseRelation();
        if (attrName.equals("annexNote")) return getRelationFragInterHasAnnexNote();
        if (attrName.equals("isUsedBy")) return getRelationVirtualEditionInterUsesFragInter();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("pbText", getRelationPbTextHasFragInters().getInverseRelation());
        get$$relationList("infoRange", getRelationInfoRangeHasFragInter().getInverseRelation());
        get$$relationList("refText", getRelationRefText2FragInter().getInverseRelation());
        get$$relationList("lbText", getRelationLbTextHasFragInters().getInverseRelation());
        get$$relationList("rdg", getRelationFragInterHasRdgTexts().getInverseRelation());
        get$$relationList("annexNote", getRelationFragInterHasAnnexNote());
        get$$relationList("isUsedBy", getRelationVirtualEditionInterUsesFragInter());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private java.lang.String xmlId;
        private int numAnnexNotes;
        private pt.ist.socialsoftware.edition.ldod.domain.Fragment fragment;
        private pt.ist.socialsoftware.edition.ldod.domain.Heteronym heteronym;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoDDate ldoDDate;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.xmlId = this.xmlId;
            newCasted.numAnnexNotes = this.numAnnexNotes;
            newCasted.fragment = this.fragment;
            newCasted.heteronym = this.heteronym;
            newCasted.ldoDDate = this.ldoDDate;
            
        }
        
    }
    
}
