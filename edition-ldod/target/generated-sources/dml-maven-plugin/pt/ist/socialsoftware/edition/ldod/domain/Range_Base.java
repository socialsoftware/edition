package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Range_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Range,pt.ist.socialsoftware.edition.ldod.domain.Annotation> role$$annotation = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Range,pt.ist.socialsoftware.edition.ldod.domain.Annotation>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Annotation getValue(pt.ist.socialsoftware.edition.ldod.domain.Range o1) {
            return ((Range_Base.DO_State)o1.get$obj$state(false)).annotation;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Range o1, pt.ist.socialsoftware.edition.ldod.domain.Annotation o2) {
            ((Range_Base.DO_State)o1.get$obj$state(true)).annotation = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Annotation,pt.ist.socialsoftware.edition.ldod.domain.Range> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Annotation.role$$range;
        }
        
    };
    
    private final static class AnnotationHasRanges {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Range,pt.ist.socialsoftware.edition.ldod.domain.Annotation> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Range,pt.ist.socialsoftware.edition.ldod.domain.Annotation>(role$$annotation, "AnnotationHasRanges");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Range,pt.ist.socialsoftware.edition.ldod.domain.Annotation> getRelationAnnotationHasRanges() {
        return AnnotationHasRanges.relation;
    }
    
    static {
        AnnotationHasRanges.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Range.AnnotationHasRanges");
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
    protected  Range_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getStart() {
        return ((DO_State)this.get$obj$state(false)).start;
    }
    
    public void setStart(java.lang.String start) {
        ((DO_State)this.get$obj$state(true)).start = start;
    }
    
    private java.lang.String get$start() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).start;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$start(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).start = (java.lang.String)((value == null) ? null : value);
    }
    
    public int getStartOffset() {
        return ((DO_State)this.get$obj$state(false)).startOffset;
    }
    
    public void setStartOffset(int startOffset) {
        ((DO_State)this.get$obj$state(true)).startOffset = startOffset;
    }
    
    private int get$startOffset() {
        int value = ((DO_State)this.get$obj$state(false)).startOffset;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$startOffset(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).startOffset = (int)(value);
    }
    
    public java.lang.String getEnd() {
        return ((DO_State)this.get$obj$state(false)).end;
    }
    
    public void setEnd(java.lang.String end) {
        ((DO_State)this.get$obj$state(true)).end = end;
    }
    
    private java.lang.String get$end() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).end;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$end(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).end = (java.lang.String)((value == null) ? null : value);
    }
    
    public int getEndOffset() {
        return ((DO_State)this.get$obj$state(false)).endOffset;
    }
    
    public void setEndOffset(int endOffset) {
        ((DO_State)this.get$obj$state(true)).endOffset = endOffset;
    }
    
    private int get$endOffset() {
        int value = ((DO_State)this.get$obj$state(false)).endOffset;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$endOffset(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).endOffset = (int)(value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.Annotation getAnnotation() {
        return ((DO_State)this.get$obj$state(false)).annotation;
    }
    
    public void setAnnotation(pt.ist.socialsoftware.edition.ldod.domain.Annotation annotation) {
        getRelationAnnotationHasRanges().add((pt.ist.socialsoftware.edition.ldod.domain.Range)this, annotation);
    }
    
    private java.lang.Long get$oidAnnotation() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).annotation;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfAnnotation() {
        if (getAnnotation() == null) return false;
        return true;
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.annotation != null) handleAttemptToDeleteConnectedObject("Annotation");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$start(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "START"), state);
        set$startOffset(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "START_OFFSET"), state);
        set$end(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "END"), state);
        set$endOffset(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "END_OFFSET"), state);
        castedState.annotation = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_ANNOTATION");
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
        private java.lang.String start;
        private int startOffset;
        private java.lang.String end;
        private int endOffset;
        private pt.ist.socialsoftware.edition.ldod.domain.Annotation annotation;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.start = this.start;
            newCasted.startOffset = this.startOffset;
            newCasted.end = this.end;
            newCasted.endOffset = this.endOffset;
            newCasted.annotation = this.annotation;
            
        }
        
    }
    
}
