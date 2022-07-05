package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class InfoRange_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.InfoRange,pt.ist.socialsoftware.edition.ldod.domain.FragInter> role$$fragInter = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.InfoRange,pt.ist.socialsoftware.edition.ldod.domain.FragInter>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.FragInter getValue(pt.ist.socialsoftware.edition.ldod.domain.InfoRange o1) {
            return ((InfoRange_Base.DO_State)o1.get$obj$state(false)).fragInter;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.InfoRange o1, pt.ist.socialsoftware.edition.ldod.domain.FragInter o2) {
            ((InfoRange_Base.DO_State)o1.get$obj$state(true)).fragInter = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.InfoRange> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.FragInter.role$$infoRange;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.InfoRange,pt.ist.socialsoftware.edition.ldod.domain.Citation> role$$citation = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.InfoRange,pt.ist.socialsoftware.edition.ldod.domain.Citation>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Citation getValue(pt.ist.socialsoftware.edition.ldod.domain.InfoRange o1) {
            return ((InfoRange_Base.DO_State)o1.get$obj$state(false)).citation;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.InfoRange o1, pt.ist.socialsoftware.edition.ldod.domain.Citation o2) {
            ((InfoRange_Base.DO_State)o1.get$obj$state(true)).citation = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Citation,pt.ist.socialsoftware.edition.ldod.domain.InfoRange> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Citation.role$$infoRange;
        }
        
    };
    
    private final static class InfoRangeHasFragInter {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.InfoRange,pt.ist.socialsoftware.edition.ldod.domain.FragInter> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.InfoRange,pt.ist.socialsoftware.edition.ldod.domain.FragInter>(role$$fragInter, "InfoRangeHasFragInter");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.InfoRange,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getRelationInfoRangeHasFragInter() {
        return InfoRangeHasFragInter.relation;
    }
    
    static {
        InfoRangeHasFragInter.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.InfoRange.InfoRangeHasFragInter");
    }
    
    private final static class CitationHasInfoRanges {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.InfoRange,pt.ist.socialsoftware.edition.ldod.domain.Citation> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.InfoRange,pt.ist.socialsoftware.edition.ldod.domain.Citation>(role$$citation, "CitationHasInfoRanges");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.InfoRange,pt.ist.socialsoftware.edition.ldod.domain.Citation> getRelationCitationHasInfoRanges() {
        return CitationHasInfoRanges.relation;
    }
    
    static {
        CitationHasInfoRanges.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.InfoRange.CitationHasInfoRanges");
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
    protected  InfoRange_Base() {
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
    
    public pt.ist.socialsoftware.edition.ldod.domain.FragInter getFragInter() {
        return ((DO_State)this.get$obj$state(false)).fragInter;
    }
    
    public void setFragInter(pt.ist.socialsoftware.edition.ldod.domain.FragInter fragInter) {
        getRelationInfoRangeHasFragInter().add((pt.ist.socialsoftware.edition.ldod.domain.InfoRange)this, fragInter);
    }
    
    private java.lang.Long get$oidFragInter() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).fragInter;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfFragInter() {
        if (getFragInter() == null) return false;
        return true;
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.Citation getCitation() {
        return ((DO_State)this.get$obj$state(false)).citation;
    }
    
    public void setCitation(pt.ist.socialsoftware.edition.ldod.domain.Citation citation) {
        getRelationCitationHasInfoRanges().add((pt.ist.socialsoftware.edition.ldod.domain.InfoRange)this, citation);
    }
    
    private java.lang.Long get$oidCitation() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).citation;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfCitation() {
        if (getCitation() == null) return false;
        return true;
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.fragInter != null) handleAttemptToDeleteConnectedObject("FragInter");
        if (castedState.citation != null) handleAttemptToDeleteConnectedObject("Citation");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$start(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "START"), state);
        set$startOffset(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "START_OFFSET"), state);
        set$end(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "END"), state);
        set$endOffset(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "END_OFFSET"), state);
        set$quote(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "QUOTE"), state);
        set$text(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "TEXT"), state);
        castedState.fragInter = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_FRAG_INTER");
        castedState.citation = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_CITATION");
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
        private java.lang.String quote;
        private java.lang.String text;
        private pt.ist.socialsoftware.edition.ldod.domain.FragInter fragInter;
        private pt.ist.socialsoftware.edition.ldod.domain.Citation citation;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.start = this.start;
            newCasted.startOffset = this.startOffset;
            newCasted.end = this.end;
            newCasted.endOffset = this.endOffset;
            newCasted.quote = this.quote;
            newCasted.text = this.text;
            newCasted.fragInter = this.fragInter;
            newCasted.citation = this.citation;
            
        }
        
    }
    
}
