package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class HumanAnnotation_Base extends pt.ist.socialsoftware.edition.ldod.domain.Annotation {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation,pt.ist.socialsoftware.edition.ldod.domain.SimpleText> role$$endText = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation,pt.ist.socialsoftware.edition.ldod.domain.SimpleText>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.SimpleText getValue(pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation o1) {
            return ((HumanAnnotation_Base.DO_State)o1.get$obj$state(false)).endText;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation o1, pt.ist.socialsoftware.edition.ldod.domain.SimpleText o2) {
            ((HumanAnnotation_Base.DO_State)o1.get$obj$state(true)).endText = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.SimpleText,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.SimpleText.role$$endAnnotations;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation,pt.ist.socialsoftware.edition.ldod.domain.SimpleText> role$$startText = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation,pt.ist.socialsoftware.edition.ldod.domain.SimpleText>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.SimpleText getValue(pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation o1) {
            return ((HumanAnnotation_Base.DO_State)o1.get$obj$state(false)).startText;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation o1, pt.ist.socialsoftware.edition.ldod.domain.SimpleText o2) {
            ((HumanAnnotation_Base.DO_State)o1.get$obj$state(true)).startText = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.SimpleText,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.SimpleText.role$$startAnnotations;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation,pt.ist.socialsoftware.edition.ldod.domain.Tag> role$$tag = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation,pt.ist.socialsoftware.edition.ldod.domain.Tag>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Tag> getSet(pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation o1) {
            return ((HumanAnnotation_Base)o1).get$rl$tag();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Tag.role$$annotation;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.SimpleText,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> getRelationHumanAnnotationHasEndSimpleText() {
        return pt.ist.socialsoftware.edition.ldod.domain.SimpleText.getRelationHumanAnnotationHasEndSimpleText();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.SimpleText,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> getRelationHumanAnnotationHasBeginSimpleText() {
        return pt.ist.socialsoftware.edition.ldod.domain.SimpleText.getRelationHumanAnnotationHasBeginSimpleText();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> getRelationHumanAnnotationHasTags() {
        return pt.ist.socialsoftware.edition.ldod.domain.Tag.getRelationHumanAnnotationHasTags();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation,pt.ist.socialsoftware.edition.ldod.domain.Tag> get$rl$tag() {
        return get$$relationList("tag", getRelationHumanAnnotationHasTags().getInverseRelation());
        
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
    protected  HumanAnnotation_Base() {
        super();
    }
    
    // Getters and Setters
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.SimpleText getEndText() {
        return ((DO_State)this.get$obj$state(false)).endText;
    }
    
    public void setEndText(pt.ist.socialsoftware.edition.ldod.domain.SimpleText endText) {
        getRelationHumanAnnotationHasEndSimpleText().add(endText, (pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation)this);
    }
    
    private java.lang.Long get$oidEndText() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).endText;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.SimpleText getStartText() {
        return ((DO_State)this.get$obj$state(false)).startText;
    }
    
    public void setStartText(pt.ist.socialsoftware.edition.ldod.domain.SimpleText startText) {
        getRelationHumanAnnotationHasBeginSimpleText().add(startText, (pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation)this);
    }
    
    private java.lang.Long get$oidStartText() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).startText;
        return (value == null) ? null : value.getOid();
    }
    
    public void addTag(pt.ist.socialsoftware.edition.ldod.domain.Tag tag) {
        getRelationHumanAnnotationHasTags().add(tag, (pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation)this);
    }
    
    public void removeTag(pt.ist.socialsoftware.edition.ldod.domain.Tag tag) {
        getRelationHumanAnnotationHasTags().remove(tag, (pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Tag> getTagSet() {
        return get$rl$tag();
    }
    
    public void set$tag(OJBFunctionalSetWrapper tag) {
        get$rl$tag().setFromOJB(this, "tag", tag);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Tag> getTag() {
        return getTagSet();
    }
    
    @Deprecated
    public int getTagCount() {
        return getTagSet().size();
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.endText != null) handleAttemptToDeleteConnectedObject("EndText");
        if (castedState.startText != null) handleAttemptToDeleteConnectedObject("StartText");
        if (get$rl$tag().size() > 0) handleAttemptToDeleteConnectedObject("Tag");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        castedState.endText = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_END_TEXT");
        castedState.startText = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_START_TEXT");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("tag")) return getRelationHumanAnnotationHasTags().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("tag", getRelationHumanAnnotationHasTags().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.Annotation.DO_State {
        private pt.ist.socialsoftware.edition.ldod.domain.SimpleText endText;
        private pt.ist.socialsoftware.edition.ldod.domain.SimpleText startText;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.endText = this.endText;
            newCasted.startText = this.startText;
            
        }
        
    }
    
}
