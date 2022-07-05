package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class PhysNote_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.PhysNote,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> role$$textPortion = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.PhysNote,pt.ist.socialsoftware.edition.ldod.domain.TextPortion>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getSet(pt.ist.socialsoftware.edition.ldod.domain.PhysNote o1) {
            return ((PhysNote_Base)o1).get$rl$textPortion();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.PhysNote> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.TextPortion.role$$physNote;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.PhysNote> getRelationPhysNoteRefersTextPortion() {
        return pt.ist.socialsoftware.edition.ldod.domain.TextPortion.getRelationPhysNoteRefersTextPortion();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.PhysNote,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> get$rl$textPortion() {
        return get$$relationList("textPortion", getRelationPhysNoteRefersTextPortion().getInverseRelation());
        
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
    protected  PhysNote_Base() {
        super();
    }
    
    // Getters and Setters
    
    public ManuscriptSource.Medium getMedium() {
        return ((DO_State)this.get$obj$state(false)).medium;
    }
    
    public void setMedium(ManuscriptSource.Medium medium) {
        ((DO_State)this.get$obj$state(true)).medium = medium;
    }
    
    private java.lang.String get$medium() {
        ManuscriptSource.Medium value = ((DO_State)this.get$obj$state(false)).medium;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$medium(ManuscriptSource.Medium value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).medium = (ManuscriptSource.Medium)((value == null) ? null : value);
    }
    
    public java.lang.String getNote() {
        return ((DO_State)this.get$obj$state(false)).note;
    }
    
    public void setNote(java.lang.String note) {
        ((DO_State)this.get$obj$state(true)).note = note;
    }
    
    private java.lang.String get$note() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).note;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$note(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).note = (java.lang.String)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public void addTextPortion(pt.ist.socialsoftware.edition.ldod.domain.TextPortion textPortion) {
        getRelationPhysNoteRefersTextPortion().add(textPortion, (pt.ist.socialsoftware.edition.ldod.domain.PhysNote)this);
    }
    
    public void removeTextPortion(pt.ist.socialsoftware.edition.ldod.domain.TextPortion textPortion) {
        getRelationPhysNoteRefersTextPortion().remove(textPortion, (pt.ist.socialsoftware.edition.ldod.domain.PhysNote)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getTextPortionSet() {
        return get$rl$textPortion();
    }
    
    public void set$textPortion(OJBFunctionalSetWrapper textPortion) {
        get$rl$textPortion().setFromOJB(this, "textPortion", textPortion);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.TextPortion> getTextPortion() {
        return getTextPortionSet();
    }
    
    @Deprecated
    public int getTextPortionCount() {
        return getTextPortionSet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$textPortion().size() > 0) handleAttemptToDeleteConnectedObject("TextPortion");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$medium(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(ManuscriptSource.Medium.class, rs, "MEDIUM"), state);
        set$note(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "NOTE"), state);
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("textPortion")) return getRelationPhysNoteRefersTextPortion().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("textPortion", getRelationPhysNoteRefersTextPortion().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private ManuscriptSource.Medium medium;
        private java.lang.String note;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.medium = this.medium;
            newCasted.note = this.note;
            
        }
        
    }
    
}
