package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class RdgText_Base extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.RdgText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> role$$fragInters = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.RdgText,pt.ist.socialsoftware.edition.ldod.domain.FragInter>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.FragInter> getSet(pt.ist.socialsoftware.edition.ldod.domain.RdgText o1) {
            return ((RdgText_Base)o1).get$rl$fragInters();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.RdgText> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.FragInter.role$$rdg;
        }
        
    };
    
    private final static class FragInterHasRdgTexts {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.RdgText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.RdgText,pt.ist.socialsoftware.edition.ldod.domain.FragInter>(role$$fragInters, "FragInterHasRdgTexts");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.RdgText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getRelationFragInterHasRdgTexts() {
        return FragInterHasRdgTexts.relation;
    }
    
    static {
        FragInterHasRdgTexts.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.RdgText.FragInterHasRdgTexts");
        FragInterHasRdgTexts.relation.addListener(new pt.ist.fenixframework.dml.runtime.RelationAdapter<pt.ist.socialsoftware.edition.ldod.domain.RdgText,pt.ist.socialsoftware.edition.ldod.domain.FragInter>() {
            @Override
            public void beforeAdd(pt.ist.socialsoftware.edition.ldod.domain.RdgText arg0, pt.ist.socialsoftware.edition.ldod.domain.FragInter arg1) {
                pt.ist.fenixframework.backend.jvstmojb.pstm.TransactionSupport.addRelationTuple("FragInterHasRdgTexts", arg1, "rdg", arg0, "fragInters");
            }
            @Override
            public void beforeRemove(pt.ist.socialsoftware.edition.ldod.domain.RdgText arg0, pt.ist.socialsoftware.edition.ldod.domain.FragInter arg1) {
                pt.ist.fenixframework.backend.jvstmojb.pstm.TransactionSupport.removeRelationTuple("FragInterHasRdgTexts", arg1, "rdg", arg0, "fragInters");
            }
            
        }
        );
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.RdgText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> get$rl$fragInters() {
        return get$$relationList("fragInters", getRelationFragInterHasRdgTexts());
        
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
    protected  RdgText_Base() {
        super();
    }
    
    // Getters and Setters
    
    public TextPortion.VariationType getType() {
        return ((DO_State)this.get$obj$state(false)).type;
    }
    
    public void setType(TextPortion.VariationType type) {
        ((DO_State)this.get$obj$state(true)).type = type;
    }
    
    private java.lang.String get$type() {
        TextPortion.VariationType value = ((DO_State)this.get$obj$state(false)).type;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$type(TextPortion.VariationType value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).type = (TextPortion.VariationType)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public void addFragInters(pt.ist.socialsoftware.edition.ldod.domain.FragInter fragInters) {
        getRelationFragInterHasRdgTexts().add((pt.ist.socialsoftware.edition.ldod.domain.RdgText)this, fragInters);
    }
    
    public void removeFragInters(pt.ist.socialsoftware.edition.ldod.domain.FragInter fragInters) {
        getRelationFragInterHasRdgTexts().remove((pt.ist.socialsoftware.edition.ldod.domain.RdgText)this, fragInters);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.FragInter> getFragIntersSet() {
        return get$rl$fragInters();
    }
    
    public void set$fragInters(OJBFunctionalSetWrapper fragInters) {
        get$rl$fragInters().setFromOJB(this, "fragInters", fragInters);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.FragInter> getFragInters() {
        return getFragIntersSet();
    }
    
    @Deprecated
    public int getFragIntersCount() {
        return getFragIntersSet().size();
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$fragInters().size() > 0) handleAttemptToDeleteConnectedObject("FragInters");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$type(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(TextPortion.VariationType.class, rs, "TYPE"), state);
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("fragInters")) return getRelationFragInterHasRdgTexts();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("fragInters", getRelationFragInterHasRdgTexts());
        
    }
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion.DO_State {
        private TextPortion.VariationType type;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.type = this.type;
            
        }
        
    }
    
}
