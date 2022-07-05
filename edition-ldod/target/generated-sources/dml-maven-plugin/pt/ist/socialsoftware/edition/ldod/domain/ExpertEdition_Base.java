package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class ExpertEdition_Base extends pt.ist.socialsoftware.edition.ldod.domain.Edition {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD> role$$ldoD4Expert = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoD getValue(pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition o1) {
            return ((ExpertEdition_Base.DO_State)o1.get$obj$state(false)).ldoD4Expert;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition o1, pt.ist.socialsoftware.edition.ldod.domain.LdoD o2) {
            ((ExpertEdition_Base.DO_State)o1.get$obj$state(true)).ldoD4Expert = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoD.role$$expertEditions;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition,pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter> role$$expertEditionInters = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition,pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter> getSet(pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition o1) {
            return ((ExpertEdition_Base)o1).get$rl$expertEditionInters();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter,pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter.role$$expertEdition;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition> getRelationLdoDHasExpertEditions() {
        return pt.ist.socialsoftware.edition.ldod.domain.LdoD.getRelationLdoDHasExpertEditions();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter,pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition> getRelationExpertEditionHasExpertEditionInters() {
        return pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter.getRelationExpertEditionHasExpertEditionInters();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition,pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter> get$rl$expertEditionInters() {
        return get$$relationList("expertEditionInters", getRelationExpertEditionHasExpertEditionInters().getInverseRelation());
        
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
    protected  ExpertEdition_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getAuthor() {
        return ((DO_State)this.get$obj$state(false)).author;
    }
    
    public void setAuthor(java.lang.String author) {
        ((DO_State)this.get$obj$state(true)).author = author;
    }
    
    private java.lang.String get$author() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).author;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$author(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).author = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getEditor() {
        return ((DO_State)this.get$obj$state(false)).editor;
    }
    
    public void setEditor(java.lang.String editor) {
        ((DO_State)this.get$obj$state(true)).editor = editor;
    }
    
    private java.lang.String get$editor() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).editor;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$editor(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).editor = (java.lang.String)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoD getLdoD4Expert() {
        return ((DO_State)this.get$obj$state(false)).ldoD4Expert;
    }
    
    public void setLdoD4Expert(pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD4Expert) {
        getRelationLdoDHasExpertEditions().add(ldoD4Expert, (pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition)this);
    }
    
    private java.lang.Long get$oidLdoD4Expert() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).ldoD4Expert;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfLdoD4Expert() {
        if (getLdoD4Expert() == null) return false;
        return true;
    }
    
    public void addExpertEditionInters(pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter expertEditionInters) {
        getRelationExpertEditionHasExpertEditionInters().add(expertEditionInters, (pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition)this);
    }
    
    public void removeExpertEditionInters(pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter expertEditionInters) {
        getRelationExpertEditionHasExpertEditionInters().remove(expertEditionInters, (pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter> getExpertEditionIntersSet() {
        return get$rl$expertEditionInters();
    }
    
    public void set$expertEditionInters(OJBFunctionalSetWrapper expertEditionInters) {
        get$rl$expertEditionInters().setFromOJB(this, "expertEditionInters", expertEditionInters);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter> getExpertEditionInters() {
        return getExpertEditionIntersSet();
    }
    
    @Deprecated
    public int getExpertEditionIntersCount() {
        return getExpertEditionIntersSet().size();
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.ldoD4Expert != null) handleAttemptToDeleteConnectedObject("LdoD4Expert");
        if (get$rl$expertEditionInters().size() > 0) handleAttemptToDeleteConnectedObject("ExpertEditionInters");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$author(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "AUTHOR"), state);
        set$editor(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "EDITOR"), state);
        castedState.ldoD4Expert = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_LDO_D4_EXPERT");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("expertEditionInters")) return getRelationExpertEditionHasExpertEditionInters().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("expertEditionInters", getRelationExpertEditionHasExpertEditionInters().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.Edition.DO_State {
        private java.lang.String author;
        private java.lang.String editor;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD4Expert;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.author = this.author;
            newCasted.editor = this.editor;
            newCasted.ldoD4Expert = this.ldoD4Expert;
            
        }
        
    }
    
}
