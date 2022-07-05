package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class VirtualEditionInter_Base extends pt.ist.socialsoftware.edition.ldod.domain.FragInter {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.Annotation> role$$annotation = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.Annotation>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Annotation> getSet(pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter o1) {
            return ((VirtualEditionInter_Base)o1).get$rl$annotation();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Annotation,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Annotation.role$$virtualEditionInter;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> role$$classificationGame = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getSet(pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter o1) {
            return ((VirtualEditionInter_Base)o1).get$rl$classificationGame();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame.role$$virtualEditionInter;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.Tag> role$$tag = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.Tag>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Tag> getSet(pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter o1) {
            return ((VirtualEditionInter_Base)o1).get$rl$tag();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Tag.role$$inter;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.Section> role$$section = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.Section>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Section getValue(pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter o1) {
            return ((VirtualEditionInter_Base.DO_State)o1.get$obj$state(false)).section;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter o1, pt.ist.socialsoftware.edition.ldod.domain.Section o2) {
            ((VirtualEditionInter_Base.DO_State)o1.get$obj$state(true)).section = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Section.role$$VirtualEditionInter;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.FragInter> role$$uses = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.FragInter>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.FragInter getValue(pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter o1) {
            return ((VirtualEditionInter_Base.DO_State)o1.get$obj$state(false)).uses;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter o1, pt.ist.socialsoftware.edition.ldod.domain.FragInter o2) {
            ((VirtualEditionInter_Base.DO_State)o1.get$obj$state(true)).uses = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.FragInter.role$$isUsedBy;
        }
        
    };
    
    private final static class VirtualEditionInterHasAnnotations {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.Annotation> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.Annotation>(role$$annotation, "VirtualEditionInterHasAnnotations");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.Annotation> getRelationVirtualEditionInterHasAnnotations() {
        return VirtualEditionInterHasAnnotations.relation;
    }
    
    static {
        VirtualEditionInterHasAnnotations.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter.VirtualEditionInterHasAnnotations");
    }
    
    private final static class ClassificationGameForVirtualEditionInter {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame>(role$$classificationGame, "ClassificationGameForVirtualEditionInter");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getRelationClassificationGameForVirtualEditionInter() {
        return ClassificationGameForVirtualEditionInter.relation;
    }
    
    static {
        ClassificationGameForVirtualEditionInter.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter.ClassificationGameForVirtualEditionInter");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getRelationTag2VirtualEditionInter() {
        return pt.ist.socialsoftware.edition.ldod.domain.Tag.getRelationTag2VirtualEditionInter();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getRelationSectionHasVitualEditionInters() {
        return pt.ist.socialsoftware.edition.ldod.domain.Section.getRelationSectionHasVitualEditionInters();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getRelationVirtualEditionInterUsesFragInter() {
        return pt.ist.socialsoftware.edition.ldod.domain.FragInter.getRelationVirtualEditionInterUsesFragInter();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.Annotation> get$rl$annotation() {
        return get$$relationList("annotation", getRelationVirtualEditionInterHasAnnotations());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> get$rl$classificationGame() {
        return get$$relationList("classificationGame", getRelationClassificationGameForVirtualEditionInter());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.Tag> get$rl$tag() {
        return get$$relationList("tag", getRelationTag2VirtualEditionInter().getInverseRelation());
        
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
    protected  VirtualEditionInter_Base() {
        super();
    }
    
    // Getters and Setters
    
    public int getNumber() {
        return ((DO_State)this.get$obj$state(false)).number;
    }
    
    public void setNumber(int number) {
        ((DO_State)this.get$obj$state(true)).number = number;
    }
    
    private int get$number() {
        int value = ((DO_State)this.get$obj$state(false)).number;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$number(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).number = (int)(value);
    }
    
    // Role Methods
    
    public void addAnnotation(pt.ist.socialsoftware.edition.ldod.domain.Annotation annotation) {
        getRelationVirtualEditionInterHasAnnotations().add((pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter)this, annotation);
    }
    
    public void removeAnnotation(pt.ist.socialsoftware.edition.ldod.domain.Annotation annotation) {
        getRelationVirtualEditionInterHasAnnotations().remove((pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter)this, annotation);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Annotation> getAnnotationSet() {
        return get$rl$annotation();
    }
    
    public void set$annotation(OJBFunctionalSetWrapper annotation) {
        get$rl$annotation().setFromOJB(this, "annotation", annotation);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Annotation> getAnnotation() {
        return getAnnotationSet();
    }
    
    @Deprecated
    public int getAnnotationCount() {
        return getAnnotationSet().size();
    }
    
    public void addClassificationGame(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame classificationGame) {
        getRelationClassificationGameForVirtualEditionInter().add((pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter)this, classificationGame);
    }
    
    public void removeClassificationGame(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame classificationGame) {
        getRelationClassificationGameForVirtualEditionInter().remove((pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter)this, classificationGame);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getClassificationGameSet() {
        return get$rl$classificationGame();
    }
    
    public void set$classificationGame(OJBFunctionalSetWrapper classificationGame) {
        get$rl$classificationGame().setFromOJB(this, "classificationGame", classificationGame);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getClassificationGame() {
        return getClassificationGameSet();
    }
    
    @Deprecated
    public int getClassificationGameCount() {
        return getClassificationGameSet().size();
    }
    
    public void addTag(pt.ist.socialsoftware.edition.ldod.domain.Tag tag) {
        getRelationTag2VirtualEditionInter().add(tag, (pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter)this);
    }
    
    public void removeTag(pt.ist.socialsoftware.edition.ldod.domain.Tag tag) {
        getRelationTag2VirtualEditionInter().remove(tag, (pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter)this);
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
    
    public pt.ist.socialsoftware.edition.ldod.domain.Section getSection() {
        return ((DO_State)this.get$obj$state(false)).section;
    }
    
    public void setSection(pt.ist.socialsoftware.edition.ldod.domain.Section section) {
        getRelationSectionHasVitualEditionInters().add(section, (pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter)this);
    }
    
    private java.lang.Long get$oidSection() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).section;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfSection() {
        if (getSection() == null) return false;
        return true;
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.FragInter getUses() {
        return ((DO_State)this.get$obj$state(false)).uses;
    }
    
    public void setUses(pt.ist.socialsoftware.edition.ldod.domain.FragInter uses) {
        getRelationVirtualEditionInterUsesFragInter().add(uses, (pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter)this);
    }
    
    private java.lang.Long get$oidUses() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).uses;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfUses() {
        if (getUses() == null) return false;
        return true;
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$annotation().size() > 0) handleAttemptToDeleteConnectedObject("Annotation");
        if (get$rl$classificationGame().size() > 0) handleAttemptToDeleteConnectedObject("ClassificationGame");
        if (get$rl$tag().size() > 0) handleAttemptToDeleteConnectedObject("Tag");
        if (castedState.section != null) handleAttemptToDeleteConnectedObject("Section");
        if (castedState.uses != null) handleAttemptToDeleteConnectedObject("Uses");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$number(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "NUMBER"), state);
        castedState.section = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_SECTION");
        castedState.uses = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_USES");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("annotation")) return getRelationVirtualEditionInterHasAnnotations();
        if (attrName.equals("classificationGame")) return getRelationClassificationGameForVirtualEditionInter();
        if (attrName.equals("tag")) return getRelationTag2VirtualEditionInter().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("annotation", getRelationVirtualEditionInterHasAnnotations());
        get$$relationList("classificationGame", getRelationClassificationGameForVirtualEditionInter());
        get$$relationList("tag", getRelationTag2VirtualEditionInter().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.FragInter.DO_State {
        private int number;
        private pt.ist.socialsoftware.edition.ldod.domain.Section section;
        private pt.ist.socialsoftware.edition.ldod.domain.FragInter uses;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.number = this.number;
            newCasted.section = this.section;
            newCasted.uses = this.uses;
            
        }
        
    }
    
}
