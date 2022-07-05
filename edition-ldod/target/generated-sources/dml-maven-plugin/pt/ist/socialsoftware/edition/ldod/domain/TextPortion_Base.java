package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class TextPortion_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.Fragment> role$$fragment = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.Fragment>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Fragment getValue(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1) {
            return ((TextPortion_Base.DO_State)o1.get$obj$state(false)).fragment;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1, pt.ist.socialsoftware.edition.ldod.domain.Fragment o2) {
            ((TextPortion_Base.DO_State)o1.get$obj$state(true)).fragment = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Fragment.role$$textPortion;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> role$$parentOfLastText = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getValue(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1) {
            return ((TextPortion_Base.DO_State)o1.get$obj$state(false)).parentOfLastText;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1, pt.ist.socialsoftware.edition.ldod.domain.TextPortion o2) {
            ((TextPortion_Base.DO_State)o1.get$obj$state(true)).parentOfLastText = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.TextPortion.role$$lastChildText;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> role$$lastChildText = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getValue(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1) {
            return ((TextPortion_Base.DO_State)o1.get$obj$state(false)).lastChildText;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1, pt.ist.socialsoftware.edition.ldod.domain.TextPortion o2) {
            ((TextPortion_Base.DO_State)o1.get$obj$state(true)).lastChildText = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.TextPortion.role$$parentOfLastText;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> role$$prevText = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getValue(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1) {
            return ((TextPortion_Base.DO_State)o1.get$obj$state(false)).prevText;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1, pt.ist.socialsoftware.edition.ldod.domain.TextPortion o2) {
            ((TextPortion_Base.DO_State)o1.get$obj$state(true)).prevText = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.TextPortion.role$$nextText;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> role$$nextText = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getValue(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1) {
            return ((TextPortion_Base.DO_State)o1.get$obj$state(false)).nextText;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1, pt.ist.socialsoftware.edition.ldod.domain.TextPortion o2) {
            ((TextPortion_Base.DO_State)o1.get$obj$state(true)).nextText = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.TextPortion.role$$prevText;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.Rend> role$$rend = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.Rend>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Rend> getSet(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1) {
            return ((TextPortion_Base)o1).get$rl$rend();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Rend,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Rend.role$$text;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> role$$parentText = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getValue(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1) {
            return ((TextPortion_Base.DO_State)o1.get$obj$state(false)).parentText;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1, pt.ist.socialsoftware.edition.ldod.domain.TextPortion o2) {
            ((TextPortion_Base.DO_State)o1.get$obj$state(true)).parentText = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.TextPortion.role$$childText;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> role$$childText = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getSet(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1) {
            return ((TextPortion_Base)o1).get$rl$childText();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.TextPortion.role$$parentText;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> role$$parentOfFirstText = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getValue(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1) {
            return ((TextPortion_Base.DO_State)o1.get$obj$state(false)).parentOfFirstText;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1, pt.ist.socialsoftware.edition.ldod.domain.TextPortion o2) {
            ((TextPortion_Base.DO_State)o1.get$obj$state(true)).parentOfFirstText = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.TextPortion.role$$firstChildText;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> role$$firstChildText = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getValue(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1) {
            return ((TextPortion_Base.DO_State)o1.get$obj$state(false)).firstChildText;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1, pt.ist.socialsoftware.edition.ldod.domain.TextPortion o2) {
            ((TextPortion_Base.DO_State)o1.get$obj$state(true)).firstChildText = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.TextPortion.role$$parentOfFirstText;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.PhysNote> role$$physNote = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.PhysNote>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.PhysNote> getSet(pt.ist.socialsoftware.edition.ldod.domain.TextPortion o1) {
            return ((TextPortion_Base)o1).get$rl$physNote();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.PhysNote,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.PhysNote.role$$textPortion;
        }
        
    };
    
    private final static class FragmentHasTextPortion {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.Fragment> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.Fragment>(role$$fragment, "FragmentHasTextPortion");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.Fragment> getRelationFragmentHasTextPortion() {
        return FragmentHasTextPortion.relation;
    }
    
    static {
        FragmentHasTextPortion.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.TextPortion.FragmentHasTextPortion");
    }
    
    
    private final static class TextPortionContainsLastTextPortion {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion>(role$$lastChildText, "TextPortionContainsLastTextPortion");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getRelationTextPortionContainsLastTextPortion() {
        return TextPortionContainsLastTextPortion.relation;
    }
    
    static {
        TextPortionContainsLastTextPortion.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.TextPortion.TextPortionContainsLastTextPortion");
    }
    
    
    private final static class TextPortionHasNextTextPortion {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion>(role$$nextText, "TextPortionHasNextTextPortion");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getRelationTextPortionHasNextTextPortion() {
        return TextPortionHasNextTextPortion.relation;
    }
    
    static {
        TextPortionHasNextTextPortion.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.TextPortion.TextPortionHasNextTextPortion");
    }
    
    private final static class TextPortionHasRend {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.Rend> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.Rend>(role$$rend, "TextPortionHasRend");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.Rend> getRelationTextPortionHasRend() {
        return TextPortionHasRend.relation;
    }
    
    static {
        TextPortionHasRend.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.TextPortion.TextPortionHasRend");
    }
    
    
    private final static class TextPortionContainsTextPortion {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion>(role$$childText, "TextPortionContainsTextPortion");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getRelationTextPortionContainsTextPortion() {
        return TextPortionContainsTextPortion.relation;
    }
    
    static {
        TextPortionContainsTextPortion.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.TextPortion.TextPortionContainsTextPortion");
    }
    
    
    private final static class TextPortionContainsFirstTextPortion {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion>(role$$firstChildText, "TextPortionContainsFirstTextPortion");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getRelationTextPortionContainsFirstTextPortion() {
        return TextPortionContainsFirstTextPortion.relation;
    }
    
    static {
        TextPortionContainsFirstTextPortion.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.TextPortion.TextPortionContainsFirstTextPortion");
    }
    
    private final static class PhysNoteRefersTextPortion {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.PhysNote> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.PhysNote>(role$$physNote, "PhysNoteRefersTextPortion");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.PhysNote> getRelationPhysNoteRefersTextPortion() {
        return PhysNoteRefersTextPortion.relation;
    }
    
    static {
        PhysNoteRefersTextPortion.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.TextPortion.PhysNoteRefersTextPortion");
        PhysNoteRefersTextPortion.relation.addListener(new pt.ist.fenixframework.dml.runtime.RelationAdapter<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.PhysNote>() {
            @Override
            public void beforeAdd(pt.ist.socialsoftware.edition.ldod.domain.TextPortion arg0, pt.ist.socialsoftware.edition.ldod.domain.PhysNote arg1) {
                pt.ist.fenixframework.backend.jvstmojb.pstm.TransactionSupport.addRelationTuple("PhysNoteRefersTextPortion", arg1, "textPortion", arg0, "physNote");
            }
            @Override
            public void beforeRemove(pt.ist.socialsoftware.edition.ldod.domain.TextPortion arg0, pt.ist.socialsoftware.edition.ldod.domain.PhysNote arg1) {
                pt.ist.fenixframework.backend.jvstmojb.pstm.TransactionSupport.removeRelationTuple("PhysNoteRefersTextPortion", arg1, "textPortion", arg0, "physNote");
            }
            
        }
        );
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.Rend> get$rl$rend() {
        return get$$relationList("rend", getRelationTextPortionHasRend());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> get$rl$childText() {
        return get$$relationList("childText", getRelationTextPortionContainsTextPortion());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.PhysNote> get$rl$physNote() {
        return get$$relationList("physNote", getRelationPhysNoteRefersTextPortion());
        
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
    protected  TextPortion_Base() {
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
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.Fragment getFragment() {
        return ((DO_State)this.get$obj$state(false)).fragment;
    }
    
    public void setFragment(pt.ist.socialsoftware.edition.ldod.domain.Fragment fragment) {
        getRelationFragmentHasTextPortion().add((pt.ist.socialsoftware.edition.ldod.domain.TextPortion)this, fragment);
    }
    
    private java.lang.Long get$oidFragment() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).fragment;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getParentOfLastText() {
        return ((DO_State)this.get$obj$state(false)).parentOfLastText;
    }
    
    public void setParentOfLastText(pt.ist.socialsoftware.edition.ldod.domain.TextPortion parentOfLastText) {
        getRelationTextPortionContainsLastTextPortion().add(parentOfLastText, (pt.ist.socialsoftware.edition.ldod.domain.TextPortion)this);
    }
    
    private java.lang.Long get$oidParentOfLastText() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).parentOfLastText;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getLastChildText() {
        return ((DO_State)this.get$obj$state(false)).lastChildText;
    }
    
    public void setLastChildText(pt.ist.socialsoftware.edition.ldod.domain.TextPortion lastChildText) {
        getRelationTextPortionContainsLastTextPortion().add((pt.ist.socialsoftware.edition.ldod.domain.TextPortion)this, lastChildText);
    }
    
    private java.lang.Long get$oidLastChildText() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).lastChildText;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getPrevText() {
        return ((DO_State)this.get$obj$state(false)).prevText;
    }
    
    public void setPrevText(pt.ist.socialsoftware.edition.ldod.domain.TextPortion prevText) {
        getRelationTextPortionHasNextTextPortion().add(prevText, (pt.ist.socialsoftware.edition.ldod.domain.TextPortion)this);
    }
    
    private java.lang.Long get$oidPrevText() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).prevText;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getNextText() {
        return ((DO_State)this.get$obj$state(false)).nextText;
    }
    
    public void setNextText(pt.ist.socialsoftware.edition.ldod.domain.TextPortion nextText) {
        getRelationTextPortionHasNextTextPortion().add((pt.ist.socialsoftware.edition.ldod.domain.TextPortion)this, nextText);
    }
    
    private java.lang.Long get$oidNextText() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).nextText;
        return (value == null) ? null : value.getOid();
    }
    
    public void addRend(pt.ist.socialsoftware.edition.ldod.domain.Rend rend) {
        getRelationTextPortionHasRend().add((pt.ist.socialsoftware.edition.ldod.domain.TextPortion)this, rend);
    }
    
    public void removeRend(pt.ist.socialsoftware.edition.ldod.domain.Rend rend) {
        getRelationTextPortionHasRend().remove((pt.ist.socialsoftware.edition.ldod.domain.TextPortion)this, rend);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Rend> getRendSet() {
        return get$rl$rend();
    }
    
    public void set$rend(OJBFunctionalSetWrapper rend) {
        get$rl$rend().setFromOJB(this, "rend", rend);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Rend> getRend() {
        return getRendSet();
    }
    
    @Deprecated
    public int getRendCount() {
        return getRendSet().size();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getParentText() {
        return ((DO_State)this.get$obj$state(false)).parentText;
    }
    
    public void setParentText(pt.ist.socialsoftware.edition.ldod.domain.TextPortion parentText) {
        getRelationTextPortionContainsTextPortion().add(parentText, (pt.ist.socialsoftware.edition.ldod.domain.TextPortion)this);
    }
    
    private java.lang.Long get$oidParentText() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).parentText;
        return (value == null) ? null : value.getOid();
    }
    
    public void addChildText(pt.ist.socialsoftware.edition.ldod.domain.TextPortion childText) {
        getRelationTextPortionContainsTextPortion().add((pt.ist.socialsoftware.edition.ldod.domain.TextPortion)this, childText);
    }
    
    public void removeChildText(pt.ist.socialsoftware.edition.ldod.domain.TextPortion childText) {
        getRelationTextPortionContainsTextPortion().remove((pt.ist.socialsoftware.edition.ldod.domain.TextPortion)this, childText);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getChildTextSet() {
        return get$rl$childText();
    }
    
    public void set$childText(OJBFunctionalSetWrapper childText) {
        get$rl$childText().setFromOJB(this, "childText", childText);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getChildText() {
        return getChildTextSet();
    }
    
    @Deprecated
    public int getChildTextCount() {
        return getChildTextSet().size();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getParentOfFirstText() {
        return ((DO_State)this.get$obj$state(false)).parentOfFirstText;
    }
    
    public void setParentOfFirstText(pt.ist.socialsoftware.edition.ldod.domain.TextPortion parentOfFirstText) {
        getRelationTextPortionContainsFirstTextPortion().add(parentOfFirstText, (pt.ist.socialsoftware.edition.ldod.domain.TextPortion)this);
    }
    
    private java.lang.Long get$oidParentOfFirstText() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).parentOfFirstText;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getFirstChildText() {
        return ((DO_State)this.get$obj$state(false)).firstChildText;
    }
    
    public void setFirstChildText(pt.ist.socialsoftware.edition.ldod.domain.TextPortion firstChildText) {
        getRelationTextPortionContainsFirstTextPortion().add((pt.ist.socialsoftware.edition.ldod.domain.TextPortion)this, firstChildText);
    }
    
    private java.lang.Long get$oidFirstChildText() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).firstChildText;
        return (value == null) ? null : value.getOid();
    }
    
    public void addPhysNote(pt.ist.socialsoftware.edition.ldod.domain.PhysNote physNote) {
        getRelationPhysNoteRefersTextPortion().add((pt.ist.socialsoftware.edition.ldod.domain.TextPortion)this, physNote);
    }
    
    public void removePhysNote(pt.ist.socialsoftware.edition.ldod.domain.PhysNote physNote) {
        getRelationPhysNoteRefersTextPortion().remove((pt.ist.socialsoftware.edition.ldod.domain.TextPortion)this, physNote);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.PhysNote> getPhysNoteSet() {
        return get$rl$physNote();
    }
    
    public void set$physNote(OJBFunctionalSetWrapper physNote) {
        get$rl$physNote().setFromOJB(this, "physNote", physNote);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.PhysNote> getPhysNote() {
        return getPhysNoteSet();
    }
    
    @Deprecated
    public int getPhysNoteCount() {
        return getPhysNoteSet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.fragment != null) handleAttemptToDeleteConnectedObject("Fragment");
        if (castedState.parentOfLastText != null) handleAttemptToDeleteConnectedObject("ParentOfLastText");
        if (castedState.lastChildText != null) handleAttemptToDeleteConnectedObject("LastChildText");
        if (castedState.prevText != null) handleAttemptToDeleteConnectedObject("PrevText");
        if (castedState.nextText != null) handleAttemptToDeleteConnectedObject("NextText");
        if (get$rl$rend().size() > 0) handleAttemptToDeleteConnectedObject("Rend");
        if (castedState.parentText != null) handleAttemptToDeleteConnectedObject("ParentText");
        if (get$rl$childText().size() > 0) handleAttemptToDeleteConnectedObject("ChildText");
        if (castedState.parentOfFirstText != null) handleAttemptToDeleteConnectedObject("ParentOfFirstText");
        if (castedState.firstChildText != null) handleAttemptToDeleteConnectedObject("FirstChildText");
        if (get$rl$physNote().size() > 0) handleAttemptToDeleteConnectedObject("PhysNote");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$xmlId(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "XML_ID"), state);
        castedState.fragment = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_FRAGMENT");
        castedState.parentOfLastText = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_PARENT_OF_LAST_TEXT");
        castedState.lastChildText = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_LAST_CHILD_TEXT");
        castedState.prevText = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_PREV_TEXT");
        castedState.nextText = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_NEXT_TEXT");
        castedState.parentText = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_PARENT_TEXT");
        castedState.parentOfFirstText = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_PARENT_OF_FIRST_TEXT");
        castedState.firstChildText = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_FIRST_CHILD_TEXT");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("rend")) return getRelationTextPortionHasRend();
        if (attrName.equals("childText")) return getRelationTextPortionContainsTextPortion();
        if (attrName.equals("physNote")) return getRelationPhysNoteRefersTextPortion();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("rend", getRelationTextPortionHasRend());
        get$$relationList("childText", getRelationTextPortionContainsTextPortion());
        get$$relationList("physNote", getRelationPhysNoteRefersTextPortion());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private java.lang.String xmlId;
        private pt.ist.socialsoftware.edition.ldod.domain.Fragment fragment;
        private pt.ist.socialsoftware.edition.ldod.domain.TextPortion parentOfLastText;
        private pt.ist.socialsoftware.edition.ldod.domain.TextPortion lastChildText;
        private pt.ist.socialsoftware.edition.ldod.domain.TextPortion prevText;
        private pt.ist.socialsoftware.edition.ldod.domain.TextPortion nextText;
        private pt.ist.socialsoftware.edition.ldod.domain.TextPortion parentText;
        private pt.ist.socialsoftware.edition.ldod.domain.TextPortion parentOfFirstText;
        private pt.ist.socialsoftware.edition.ldod.domain.TextPortion firstChildText;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.xmlId = this.xmlId;
            newCasted.fragment = this.fragment;
            newCasted.parentOfLastText = this.parentOfLastText;
            newCasted.lastChildText = this.lastChildText;
            newCasted.prevText = this.prevText;
            newCasted.nextText = this.nextText;
            newCasted.parentText = this.parentText;
            newCasted.parentOfFirstText = this.parentOfFirstText;
            newCasted.firstChildText = this.firstChildText;
            
        }
        
    }
    
}
