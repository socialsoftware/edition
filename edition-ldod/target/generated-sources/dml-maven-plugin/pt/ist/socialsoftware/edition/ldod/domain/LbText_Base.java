package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class LbText_Base extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> role$$fragInter = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.FragInter> getSet(pt.ist.socialsoftware.edition.ldod.domain.LbText o1) {
            return ((LbText_Base)o1).get$rl$fragInter();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.LbText> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.FragInter.role$$lbText;
        }
        
    };
    
    private final static class LbTextHasFragInters {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter>(role$$fragInter, "LbTextHasFragInters");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getRelationLbTextHasFragInters() {
        return LbTextHasFragInters.relation;
    }
    
    static {
        LbTextHasFragInters.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.LbText.LbTextHasFragInters");
        LbTextHasFragInters.relation.addListener(new pt.ist.fenixframework.dml.runtime.RelationAdapter<pt.ist.socialsoftware.edition.ldod.domain.LbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter>() {
            @Override
            public void beforeAdd(pt.ist.socialsoftware.edition.ldod.domain.LbText arg0, pt.ist.socialsoftware.edition.ldod.domain.FragInter arg1) {
                pt.ist.fenixframework.backend.jvstmojb.pstm.TransactionSupport.addRelationTuple("LbTextHasFragInters", arg1, "lbText", arg0, "fragInter");
            }
            @Override
            public void beforeRemove(pt.ist.socialsoftware.edition.ldod.domain.LbText arg0, pt.ist.socialsoftware.edition.ldod.domain.FragInter arg1) {
                pt.ist.fenixframework.backend.jvstmojb.pstm.TransactionSupport.removeRelationTuple("LbTextHasFragInters", arg1, "lbText", arg0, "fragInter");
            }
            
        }
        );
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> get$rl$fragInter() {
        return get$$relationList("fragInter", getRelationLbTextHasFragInters());
        
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
    protected  LbText_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.Boolean getBreakWord() {
        return ((DO_State)this.get$obj$state(false)).breakWord;
    }
    
    public void setBreakWord(java.lang.Boolean breakWord) {
        ((DO_State)this.get$obj$state(true)).breakWord = breakWord;
    }
    
    private java.lang.Boolean get$breakWord() {
        java.lang.Boolean value = ((DO_State)this.get$obj$state(false)).breakWord;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForBoolean(value);
    }
    
    private final void set$breakWord(java.lang.Boolean value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).breakWord = (java.lang.Boolean)((value == null) ? null : value);
    }
    
    public java.lang.Boolean getHyphenated() {
        return ((DO_State)this.get$obj$state(false)).hyphenated;
    }
    
    public void setHyphenated(java.lang.Boolean hyphenated) {
        ((DO_State)this.get$obj$state(true)).hyphenated = hyphenated;
    }
    
    private java.lang.Boolean get$hyphenated() {
        java.lang.Boolean value = ((DO_State)this.get$obj$state(false)).hyphenated;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForBoolean(value);
    }
    
    private final void set$hyphenated(java.lang.Boolean value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).hyphenated = (java.lang.Boolean)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public void addFragInter(pt.ist.socialsoftware.edition.ldod.domain.FragInter fragInter) {
        getRelationLbTextHasFragInters().add((pt.ist.socialsoftware.edition.ldod.domain.LbText)this, fragInter);
    }
    
    public void removeFragInter(pt.ist.socialsoftware.edition.ldod.domain.FragInter fragInter) {
        getRelationLbTextHasFragInters().remove((pt.ist.socialsoftware.edition.ldod.domain.LbText)this, fragInter);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.FragInter> getFragInterSet() {
        return get$rl$fragInter();
    }
    
    public void set$fragInter(OJBFunctionalSetWrapper fragInter) {
        get$rl$fragInter().setFromOJB(this, "fragInter", fragInter);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.FragInter> getFragInter() {
        return getFragInterSet();
    }
    
    @Deprecated
    public int getFragInterCount() {
        return getFragInterSet().size();
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$fragInter().size() > 0) handleAttemptToDeleteConnectedObject("FragInter");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$breakWord(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readBoolean(rs, "BREAK_WORD"), state);
        set$hyphenated(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readBoolean(rs, "HYPHENATED"), state);
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("fragInter")) return getRelationLbTextHasFragInters();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("fragInter", getRelationLbTextHasFragInters());
        
    }
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion.DO_State {
        private java.lang.Boolean breakWord;
        private java.lang.Boolean hyphenated;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.breakWord = this.breakWord;
            newCasted.hyphenated = this.hyphenated;
            
        }
        
    }
    
}
