package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class TypeNote_Base extends pt.ist.socialsoftware.edition.ldod.domain.PhysNote {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TypeNote,pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource> role$$manuscript = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.TypeNote,pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource getValue(pt.ist.socialsoftware.edition.ldod.domain.TypeNote o1) {
            return ((TypeNote_Base.DO_State)o1.get$obj$state(false)).manuscript;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.TypeNote o1, pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource o2) {
            ((TypeNote_Base.DO_State)o1.get$obj$state(true)).manuscript = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.TypeNote> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource.role$$typeNote;
        }
        
    };
    
    private final static class ManuscriptSourceHasTypeNote {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.TypeNote,pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.TypeNote,pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource>(role$$manuscript, "ManuscriptSourceHasTypeNote");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.TypeNote,pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource> getRelationManuscriptSourceHasTypeNote() {
        return ManuscriptSourceHasTypeNote.relation;
    }
    
    static {
        ManuscriptSourceHasTypeNote.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.TypeNote.ManuscriptSourceHasTypeNote");
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
    protected  TypeNote_Base() {
        super();
    }
    
    // Getters and Setters
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource getManuscript() {
        return ((DO_State)this.get$obj$state(false)).manuscript;
    }
    
    public void setManuscript(pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource manuscript) {
        getRelationManuscriptSourceHasTypeNote().add((pt.ist.socialsoftware.edition.ldod.domain.TypeNote)this, manuscript);
    }
    
    private java.lang.Long get$oidManuscript() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).manuscript;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfManuscript() {
        if (getManuscript() == null) return false;
        return true;
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.manuscript != null) handleAttemptToDeleteConnectedObject("Manuscript");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        castedState.manuscript = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_MANUSCRIPT");
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
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.PhysNote.DO_State {
        private pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource manuscript;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.manuscript = this.manuscript;
            
        }
        
    }
    
}
