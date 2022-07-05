package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Tag_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> role$$contributor = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoDUser getValue(pt.ist.socialsoftware.edition.ldod.domain.Tag o1) {
            return ((Tag_Base.DO_State)o1.get$obj$state(false)).contributor;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Tag o1, pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o2) {
            ((Tag_Base.DO_State)o1.get$obj$state(true)).contributor = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Tag> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.role$$tag;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> role$$classificationGame = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame getValue(pt.ist.socialsoftware.edition.ldod.domain.Tag o1) {
            return ((Tag_Base.DO_State)o1.get$obj$state(false)).classificationGame;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Tag o1, pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame o2) {
            ((Tag_Base.DO_State)o1.get$obj$state(true)).classificationGame = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.Tag> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame.role$$tag;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> role$$annotation = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation getValue(pt.ist.socialsoftware.edition.ldod.domain.Tag o1) {
            return ((Tag_Base.DO_State)o1.get$obj$state(false)).annotation;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Tag o1, pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation o2) {
            ((Tag_Base.DO_State)o1.get$obj$state(true)).annotation = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation,pt.ist.socialsoftware.edition.ldod.domain.Tag> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation.role$$tag;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> role$$inter = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter getValue(pt.ist.socialsoftware.edition.ldod.domain.Tag o1) {
            return ((Tag_Base.DO_State)o1.get$obj$state(false)).inter;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Tag o1, pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter o2) {
            ((Tag_Base.DO_State)o1.get$obj$state(true)).inter = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.Tag> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter.role$$tag;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.Category> role$$category = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.Category>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Category getValue(pt.ist.socialsoftware.edition.ldod.domain.Tag o1) {
            return ((Tag_Base.DO_State)o1.get$obj$state(false)).category;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Tag o1, pt.ist.socialsoftware.edition.ldod.domain.Category o2) {
            ((Tag_Base.DO_State)o1.get$obj$state(true)).category = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Category,pt.ist.socialsoftware.edition.ldod.domain.Tag> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Category.role$$tag;
        }
        
    };
    
    private final static class TagHasContributor {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser>(role$$contributor, "TagHasContributor");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getRelationTagHasContributor() {
        return TagHasContributor.relation;
    }
    
    static {
        TagHasContributor.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Tag.TagHasContributor");
    }
    
    private final static class ClassificationGameProducesTag {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame>(role$$classificationGame, "ClassificationGameProducesTag");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getRelationClassificationGameProducesTag() {
        return ClassificationGameProducesTag.relation;
    }
    
    static {
        ClassificationGameProducesTag.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Tag.ClassificationGameProducesTag");
    }
    
    private final static class HumanAnnotationHasTags {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation>(role$$annotation, "HumanAnnotationHasTags");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation> getRelationHumanAnnotationHasTags() {
        return HumanAnnotationHasTags.relation;
    }
    
    static {
        HumanAnnotationHasTags.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Tag.HumanAnnotationHasTags");
    }
    
    private final static class Tag2VirtualEditionInter {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter>(role$$inter, "Tag2VirtualEditionInter");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getRelationTag2VirtualEditionInter() {
        return Tag2VirtualEditionInter.relation;
    }
    
    static {
        Tag2VirtualEditionInter.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Tag.Tag2VirtualEditionInter");
    }
    
    private final static class Tag2Category {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.Category> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.Category>(role$$category, "Tag2Category");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.Category> getRelationTag2Category() {
        return Tag2Category.relation;
    }
    
    static {
        Tag2Category.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Tag.Tag2Category");
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
    protected  Tag_Base() {
        super();
    }
    
    // Getters and Setters
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoDUser getContributor() {
        return ((DO_State)this.get$obj$state(false)).contributor;
    }
    
    public void setContributor(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser contributor) {
        getRelationTagHasContributor().add((pt.ist.socialsoftware.edition.ldod.domain.Tag)this, contributor);
    }
    
    private java.lang.Long get$oidContributor() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).contributor;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame getClassificationGame() {
        return ((DO_State)this.get$obj$state(false)).classificationGame;
    }
    
    public void setClassificationGame(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame classificationGame) {
        getRelationClassificationGameProducesTag().add((pt.ist.socialsoftware.edition.ldod.domain.Tag)this, classificationGame);
    }
    
    private java.lang.Long get$oidClassificationGame() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).classificationGame;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation getAnnotation() {
        return ((DO_State)this.get$obj$state(false)).annotation;
    }
    
    public void setAnnotation(pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation annotation) {
        getRelationHumanAnnotationHasTags().add((pt.ist.socialsoftware.edition.ldod.domain.Tag)this, annotation);
    }
    
    private java.lang.Long get$oidAnnotation() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).annotation;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter getInter() {
        return ((DO_State)this.get$obj$state(false)).inter;
    }
    
    public void setInter(pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter inter) {
        getRelationTag2VirtualEditionInter().add((pt.ist.socialsoftware.edition.ldod.domain.Tag)this, inter);
    }
    
    private java.lang.Long get$oidInter() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).inter;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfInter() {
        if (getInter() == null) return false;
        return true;
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.Category getCategory() {
        return ((DO_State)this.get$obj$state(false)).category;
    }
    
    public void setCategory(pt.ist.socialsoftware.edition.ldod.domain.Category category) {
        getRelationTag2Category().add((pt.ist.socialsoftware.edition.ldod.domain.Tag)this, category);
    }
    
    private java.lang.Long get$oidCategory() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).category;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfCategory() {
        if (getCategory() == null) return false;
        return true;
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.contributor != null) handleAttemptToDeleteConnectedObject("Contributor");
        if (castedState.classificationGame != null) handleAttemptToDeleteConnectedObject("ClassificationGame");
        if (castedState.annotation != null) handleAttemptToDeleteConnectedObject("Annotation");
        if (castedState.inter != null) handleAttemptToDeleteConnectedObject("Inter");
        if (castedState.category != null) handleAttemptToDeleteConnectedObject("Category");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        castedState.contributor = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_CONTRIBUTOR");
        castedState.classificationGame = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_CLASSIFICATION_GAME");
        castedState.annotation = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_ANNOTATION");
        castedState.inter = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_INTER");
        castedState.category = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_CATEGORY");
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
        private pt.ist.socialsoftware.edition.ldod.domain.LdoDUser contributor;
        private pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame classificationGame;
        private pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation annotation;
        private pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter inter;
        private pt.ist.socialsoftware.edition.ldod.domain.Category category;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.contributor = this.contributor;
            newCasted.classificationGame = this.classificationGame;
            newCasted.annotation = this.annotation;
            newCasted.inter = this.inter;
            newCasted.category = this.category;
            
        }
        
    }
    
}
