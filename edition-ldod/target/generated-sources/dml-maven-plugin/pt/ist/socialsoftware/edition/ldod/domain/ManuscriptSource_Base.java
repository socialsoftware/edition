package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class ManuscriptSource_Base extends pt.ist.socialsoftware.edition.ldod.domain.Source {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.TypeNote> role$$typeNote = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.TypeNote>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.TypeNote> getSet(pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource o1) {
            return ((ManuscriptSource_Base)o1).get$rl$typeNote();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.TypeNote,pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.TypeNote.role$$manuscript;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.Dimensions> role$$dimensions = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.Dimensions>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Dimensions> getSet(pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource o1) {
            return ((ManuscriptSource_Base)o1).get$rl$dimensions();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Dimensions,pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Dimensions.role$$manuscriptSource;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.HandNote> role$$handNote = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.HandNote>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.HandNote> getSet(pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource o1) {
            return ((ManuscriptSource_Base)o1).get$rl$handNote();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.HandNote,pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.HandNote.role$$manuscript;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.TypeNote,pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource> getRelationManuscriptSourceHasTypeNote() {
        return pt.ist.socialsoftware.edition.ldod.domain.TypeNote.getRelationManuscriptSourceHasTypeNote();
    }
    
    private final static class ManuscriptSourceHasDimensions {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.Dimensions> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.Dimensions>(role$$dimensions, "ManuscriptSourceHasDimensions");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.Dimensions> getRelationManuscriptSourceHasDimensions() {
        return ManuscriptSourceHasDimensions.relation;
    }
    
    static {
        ManuscriptSourceHasDimensions.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource.ManuscriptSourceHasDimensions");
    }
    
    private final static class ManuscriptSourceHasHandNote {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.HandNote> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.HandNote>(role$$handNote, "ManuscriptSourceHasHandNote");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.HandNote> getRelationManuscriptSourceHasHandNote() {
        return ManuscriptSourceHasHandNote.relation;
    }
    
    static {
        ManuscriptSourceHasHandNote.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource.ManuscriptSourceHasHandNote");
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.TypeNote> get$rl$typeNote() {
        return get$$relationList("typeNote", getRelationManuscriptSourceHasTypeNote().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.Dimensions> get$rl$dimensions() {
        return get$$relationList("dimensions", getRelationManuscriptSourceHasDimensions());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.HandNote> get$rl$handNote() {
        return get$$relationList("handNote", getRelationManuscriptSourceHasHandNote());
        
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
    protected  ManuscriptSource_Base() {
        super();
    }
    
    // Getters and Setters
    
    public ManuscriptSource.Form getForm() {
        return ((DO_State)this.get$obj$state(false)).form;
    }
    
    public void setForm(ManuscriptSource.Form form) {
        ((DO_State)this.get$obj$state(true)).form = form;
    }
    
    private java.lang.String get$form() {
        ManuscriptSource.Form value = ((DO_State)this.get$obj$state(false)).form;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$form(ManuscriptSource.Form value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).form = (ManuscriptSource.Form)((value == null) ? null : value);
    }
    
    public ManuscriptSource.Material getMaterial() {
        return ((DO_State)this.get$obj$state(false)).material;
    }
    
    public void setMaterial(ManuscriptSource.Material material) {
        ((DO_State)this.get$obj$state(true)).material = material;
    }
    
    private java.lang.String get$material() {
        ManuscriptSource.Material value = ((DO_State)this.get$obj$state(false)).material;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$material(ManuscriptSource.Material value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).material = (ManuscriptSource.Material)((value == null) ? null : value);
    }
    
    public int getColumns() {
        return ((DO_State)this.get$obj$state(false)).columns;
    }
    
    public void setColumns(int columns) {
        ((DO_State)this.get$obj$state(true)).columns = columns;
    }
    
    private int get$columns() {
        int value = ((DO_State)this.get$obj$state(false)).columns;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$columns(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).columns = (int)(value);
    }
    
    public java.lang.Boolean getHasLdoDLabel() {
        return ((DO_State)this.get$obj$state(false)).hasLdoDLabel;
    }
    
    public void setHasLdoDLabel(java.lang.Boolean hasLdoDLabel) {
        ((DO_State)this.get$obj$state(true)).hasLdoDLabel = hasLdoDLabel;
    }
    
    private java.lang.Boolean get$hasLdoDLabel() {
        java.lang.Boolean value = ((DO_State)this.get$obj$state(false)).hasLdoDLabel;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForBoolean(value);
    }
    
    private final void set$hasLdoDLabel(java.lang.Boolean value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).hasLdoDLabel = (java.lang.Boolean)((value == null) ? null : value);
    }
    
    public java.lang.String getNotes() {
        return ((DO_State)this.get$obj$state(false)).notes;
    }
    
    public void setNotes(java.lang.String notes) {
        ((DO_State)this.get$obj$state(true)).notes = notes;
    }
    
    private java.lang.String get$notes() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).notes;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$notes(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).notes = (java.lang.String)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public void addTypeNote(pt.ist.socialsoftware.edition.ldod.domain.TypeNote typeNote) {
        getRelationManuscriptSourceHasTypeNote().add(typeNote, (pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource)this);
    }
    
    public void removeTypeNote(pt.ist.socialsoftware.edition.ldod.domain.TypeNote typeNote) {
        getRelationManuscriptSourceHasTypeNote().remove(typeNote, (pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.TypeNote> getTypeNoteSet() {
        return get$rl$typeNote();
    }
    
    public void set$typeNote(OJBFunctionalSetWrapper typeNote) {
        get$rl$typeNote().setFromOJB(this, "typeNote", typeNote);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.TypeNote> getTypeNote() {
        return getTypeNoteSet();
    }
    
    @Deprecated
    public int getTypeNoteCount() {
        return getTypeNoteSet().size();
    }
    
    public void addDimensions(pt.ist.socialsoftware.edition.ldod.domain.Dimensions dimensions) {
        getRelationManuscriptSourceHasDimensions().add((pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource)this, dimensions);
    }
    
    public void removeDimensions(pt.ist.socialsoftware.edition.ldod.domain.Dimensions dimensions) {
        getRelationManuscriptSourceHasDimensions().remove((pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource)this, dimensions);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Dimensions> getDimensionsSet() {
        return get$rl$dimensions();
    }
    
    public void set$dimensions(OJBFunctionalSetWrapper dimensions) {
        get$rl$dimensions().setFromOJB(this, "dimensions", dimensions);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Dimensions> getDimensions() {
        return getDimensionsSet();
    }
    
    @Deprecated
    public int getDimensionsCount() {
        return getDimensionsSet().size();
    }
    
    public void addHandNote(pt.ist.socialsoftware.edition.ldod.domain.HandNote handNote) {
        getRelationManuscriptSourceHasHandNote().add((pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource)this, handNote);
    }
    
    public void removeHandNote(pt.ist.socialsoftware.edition.ldod.domain.HandNote handNote) {
        getRelationManuscriptSourceHasHandNote().remove((pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource)this, handNote);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.HandNote> getHandNoteSet() {
        return get$rl$handNote();
    }
    
    public void set$handNote(OJBFunctionalSetWrapper handNote) {
        get$rl$handNote().setFromOJB(this, "handNote", handNote);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.HandNote> getHandNote() {
        return getHandNoteSet();
    }
    
    @Deprecated
    public int getHandNoteCount() {
        return getHandNoteSet().size();
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$typeNote().size() > 0) handleAttemptToDeleteConnectedObject("TypeNote");
        if (get$rl$dimensions().size() > 0) handleAttemptToDeleteConnectedObject("Dimensions");
        if (get$rl$handNote().size() > 0) handleAttemptToDeleteConnectedObject("HandNote");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$form(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(ManuscriptSource.Form.class, rs, "FORM"), state);
        set$material(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(ManuscriptSource.Material.class, rs, "MATERIAL"), state);
        set$columns(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "COLUMNS"), state);
        set$hasLdoDLabel(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readBoolean(rs, "HAS_LDO_D_LABEL"), state);
        set$notes(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "NOTES"), state);
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("typeNote")) return getRelationManuscriptSourceHasTypeNote().getInverseRelation();
        if (attrName.equals("dimensions")) return getRelationManuscriptSourceHasDimensions();
        if (attrName.equals("handNote")) return getRelationManuscriptSourceHasHandNote();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("typeNote", getRelationManuscriptSourceHasTypeNote().getInverseRelation());
        get$$relationList("dimensions", getRelationManuscriptSourceHasDimensions());
        get$$relationList("handNote", getRelationManuscriptSourceHasHandNote());
        
    }
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.Source.DO_State {
        private ManuscriptSource.Form form;
        private ManuscriptSource.Material material;
        private int columns;
        private java.lang.Boolean hasLdoDLabel;
        private java.lang.String notes;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.form = this.form;
            newCasted.material = this.material;
            newCasted.columns = this.columns;
            newCasted.hasLdoDLabel = this.hasLdoDLabel;
            newCasted.notes = this.notes;
            
        }
        
    }
    
}
