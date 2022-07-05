package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class NoteText_Base extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.NoteText,pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> role$$annexNote = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.NoteText,pt.ist.socialsoftware.edition.ldod.domain.AnnexNote>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> getSet(pt.ist.socialsoftware.edition.ldod.domain.NoteText o1) {
            return ((NoteText_Base)o1).get$rl$annexNote();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.AnnexNote,pt.ist.socialsoftware.edition.ldod.domain.NoteText> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.AnnexNote.role$$noteText;
        }
        
    };
    
    private final static class AnnexNoteHasTextNote {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.NoteText,pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.NoteText,pt.ist.socialsoftware.edition.ldod.domain.AnnexNote>(role$$annexNote, "AnnexNoteHasTextNote");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.NoteText,pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> getRelationAnnexNoteHasTextNote() {
        return AnnexNoteHasTextNote.relation;
    }
    
    static {
        AnnexNoteHasTextNote.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.NoteText.AnnexNoteHasTextNote");
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.NoteText,pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> get$rl$annexNote() {
        return get$$relationList("annexNote", getRelationAnnexNoteHasTextNote());
        
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
    protected  NoteText_Base() {
        super();
    }
    
    // Getters and Setters
    
    public NoteText.NoteType getType() {
        return ((DO_State)this.get$obj$state(false)).type;
    }
    
    public void setType(NoteText.NoteType type) {
        ((DO_State)this.get$obj$state(true)).type = type;
    }
    
    private java.lang.String get$type() {
        NoteText.NoteType value = ((DO_State)this.get$obj$state(false)).type;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$type(NoteText.NoteType value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).type = (NoteText.NoteType)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public void addAnnexNote(pt.ist.socialsoftware.edition.ldod.domain.AnnexNote annexNote) {
        getRelationAnnexNoteHasTextNote().add((pt.ist.socialsoftware.edition.ldod.domain.NoteText)this, annexNote);
    }
    
    public void removeAnnexNote(pt.ist.socialsoftware.edition.ldod.domain.AnnexNote annexNote) {
        getRelationAnnexNoteHasTextNote().remove((pt.ist.socialsoftware.edition.ldod.domain.NoteText)this, annexNote);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> getAnnexNoteSet() {
        return get$rl$annexNote();
    }
    
    public void set$annexNote(OJBFunctionalSetWrapper annexNote) {
        get$rl$annexNote().setFromOJB(this, "annexNote", annexNote);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.AnnexNote> getAnnexNote() {
        return getAnnexNoteSet();
    }
    
    @Deprecated
    public int getAnnexNoteCount() {
        return getAnnexNoteSet().size();
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$annexNote().size() > 0) handleAttemptToDeleteConnectedObject("AnnexNote");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$type(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(NoteText.NoteType.class, rs, "TYPE"), state);
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("annexNote")) return getRelationAnnexNoteHasTextNote();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("annexNote", getRelationAnnexNoteHasTextNote());
        
    }
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion.DO_State {
        private NoteText.NoteType type;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.type = this.type;
            
        }
        
    }
    
}
