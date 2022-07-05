package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class AnnexNote_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.AnnexNote,pt.ist.socialsoftware.edition.ldod.domain.NoteText> role$$noteText = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.AnnexNote,pt.ist.socialsoftware.edition.ldod.domain.NoteText>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.NoteText getValue(pt.ist.socialsoftware.edition.ldod.domain.AnnexNote o1) {
            return ((AnnexNote_Base.DO_State)o1.get$obj$state(false)).noteText;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.AnnexNote o1, pt.ist.socialsoftware.edition.ldod.domain.NoteText o2) {
            ((AnnexNote_Base.DO_State)o1.get$obj$state(true)).noteText = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.NoteText,pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.NoteText.role$$annexNote;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.AnnexNote,pt.ist.socialsoftware.edition.ldod.domain.FragInter> role$$fragInter = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.AnnexNote,pt.ist.socialsoftware.edition.ldod.domain.FragInter>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.FragInter getValue(pt.ist.socialsoftware.edition.ldod.domain.AnnexNote o1) {
            return ((AnnexNote_Base.DO_State)o1.get$obj$state(false)).fragInter;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.AnnexNote o1, pt.ist.socialsoftware.edition.ldod.domain.FragInter o2) {
            ((AnnexNote_Base.DO_State)o1.get$obj$state(true)).fragInter = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.FragInter.role$$annexNote;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.NoteText,pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> getRelationAnnexNoteHasTextNote() {
        return pt.ist.socialsoftware.edition.ldod.domain.NoteText.getRelationAnnexNoteHasTextNote();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> getRelationFragInterHasAnnexNote() {
        return pt.ist.socialsoftware.edition.ldod.domain.FragInter.getRelationFragInterHasAnnexNote();
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
    protected  AnnexNote_Base() {
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
    
    public pt.ist.socialsoftware.edition.ldod.domain.NoteText getNoteText() {
        return ((DO_State)this.get$obj$state(false)).noteText;
    }
    
    public void setNoteText(pt.ist.socialsoftware.edition.ldod.domain.NoteText noteText) {
        getRelationAnnexNoteHasTextNote().add(noteText, (pt.ist.socialsoftware.edition.ldod.domain.AnnexNote)this);
    }
    
    private java.lang.Long get$oidNoteText() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).noteText;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfNoteText() {
        if (getNoteText() == null) return false;
        return true;
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.FragInter getFragInter() {
        return ((DO_State)this.get$obj$state(false)).fragInter;
    }
    
    public void setFragInter(pt.ist.socialsoftware.edition.ldod.domain.FragInter fragInter) {
        getRelationFragInterHasAnnexNote().add(fragInter, (pt.ist.socialsoftware.edition.ldod.domain.AnnexNote)this);
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
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.noteText != null) handleAttemptToDeleteConnectedObject("NoteText");
        if (castedState.fragInter != null) handleAttemptToDeleteConnectedObject("FragInter");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$number(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "NUMBER"), state);
        castedState.noteText = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_NOTE_TEXT");
        castedState.fragInter = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_FRAG_INTER");
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
        private int number;
        private pt.ist.socialsoftware.edition.ldod.domain.NoteText noteText;
        private pt.ist.socialsoftware.edition.ldod.domain.FragInter fragInter;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.number = this.number;
            newCasted.noteText = this.noteText;
            newCasted.fragInter = this.fragInter;
            
        }
        
    }
    
}
