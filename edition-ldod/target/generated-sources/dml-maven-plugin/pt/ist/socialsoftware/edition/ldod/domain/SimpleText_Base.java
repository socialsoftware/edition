package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class SimpleText_Base extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.SimpleText,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> role$$endAnnotations = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.SimpleText,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> getSet(pt.ist.socialsoftware.edition.ldod.domain.SimpleText o1) {
            return ((SimpleText_Base)o1).get$rl$endAnnotations();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation,pt.ist.socialsoftware.edition.ldod.domain.SimpleText> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation.role$$endText;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.SimpleText,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> role$$startAnnotations = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.SimpleText,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> getSet(pt.ist.socialsoftware.edition.ldod.domain.SimpleText o1) {
            return ((SimpleText_Base)o1).get$rl$startAnnotations();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation,pt.ist.socialsoftware.edition.ldod.domain.SimpleText> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation.role$$startText;
        }
        
    };
    
    private final static class HumanAnnotationHasEndSimpleText {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.SimpleText,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.SimpleText,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation>(role$$endAnnotations, "HumanAnnotationHasEndSimpleText");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.SimpleText,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> getRelationHumanAnnotationHasEndSimpleText() {
        return HumanAnnotationHasEndSimpleText.relation;
    }
    
    static {
        HumanAnnotationHasEndSimpleText.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.SimpleText.HumanAnnotationHasEndSimpleText");
    }
    
    private final static class HumanAnnotationHasBeginSimpleText {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.SimpleText,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.SimpleText,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation>(role$$startAnnotations, "HumanAnnotationHasBeginSimpleText");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.SimpleText,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> getRelationHumanAnnotationHasBeginSimpleText() {
        return HumanAnnotationHasBeginSimpleText.relation;
    }
    
    static {
        HumanAnnotationHasBeginSimpleText.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.SimpleText.HumanAnnotationHasBeginSimpleText");
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.SimpleText,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> get$rl$endAnnotations() {
        return get$$relationList("endAnnotations", getRelationHumanAnnotationHasEndSimpleText());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.SimpleText,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> get$rl$startAnnotations() {
        return get$$relationList("startAnnotations", getRelationHumanAnnotationHasBeginSimpleText());
        
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
    protected  SimpleText_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getValue() {
        return ((DO_State)this.get$obj$state(false)).value;
    }
    
    public void setValue(java.lang.String value) {
        ((DO_State)this.get$obj$state(true)).value = value;
    }
    
    private java.lang.String get$value() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).value;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$value(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).value = (java.lang.String)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public void addEndAnnotations(pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation endAnnotations) {
        getRelationHumanAnnotationHasEndSimpleText().add((pt.ist.socialsoftware.edition.ldod.domain.SimpleText)this, endAnnotations);
    }
    
    public void removeEndAnnotations(pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation endAnnotations) {
        getRelationHumanAnnotationHasEndSimpleText().remove((pt.ist.socialsoftware.edition.ldod.domain.SimpleText)this, endAnnotations);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> getEndAnnotationsSet() {
        return get$rl$endAnnotations();
    }
    
    public void set$endAnnotations(OJBFunctionalSetWrapper endAnnotations) {
        get$rl$endAnnotations().setFromOJB(this, "endAnnotations", endAnnotations);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> getEndAnnotations() {
        return getEndAnnotationsSet();
    }
    
    @Deprecated
    public int getEndAnnotationsCount() {
        return getEndAnnotationsSet().size();
    }
    
    public void addStartAnnotations(pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation startAnnotations) {
        getRelationHumanAnnotationHasBeginSimpleText().add((pt.ist.socialsoftware.edition.ldod.domain.SimpleText)this, startAnnotations);
    }
    
    public void removeStartAnnotations(pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation startAnnotations) {
        getRelationHumanAnnotationHasBeginSimpleText().remove((pt.ist.socialsoftware.edition.ldod.domain.SimpleText)this, startAnnotations);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> getStartAnnotationsSet() {
        return get$rl$startAnnotations();
    }
    
    public void set$startAnnotations(OJBFunctionalSetWrapper startAnnotations) {
        get$rl$startAnnotations().setFromOJB(this, "startAnnotations", startAnnotations);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> getStartAnnotations() {
        return getStartAnnotationsSet();
    }
    
    @Deprecated
    public int getStartAnnotationsCount() {
        return getStartAnnotationsSet().size();
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$endAnnotations().size() > 0) handleAttemptToDeleteConnectedObject("EndAnnotations");
        if (get$rl$startAnnotations().size() > 0) handleAttemptToDeleteConnectedObject("StartAnnotations");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$value(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "VALUE"), state);
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("endAnnotations")) return getRelationHumanAnnotationHasEndSimpleText();
        if (attrName.equals("startAnnotations")) return getRelationHumanAnnotationHasBeginSimpleText();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("endAnnotations", getRelationHumanAnnotationHasEndSimpleText());
        get$$relationList("startAnnotations", getRelationHumanAnnotationHasBeginSimpleText());
        
    }
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion.DO_State {
        private java.lang.String value;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.value = this.value;
            
        }
        
    }
    
}
