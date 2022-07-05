package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Rend_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Rend,pt.ist.socialsoftware.edition.ldod.domain.TextPortion> role$$text = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Rend,pt.ist.socialsoftware.edition.ldod.domain.TextPortion>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getValue(pt.ist.socialsoftware.edition.ldod.domain.Rend o1) {
            return ((Rend_Base.DO_State)o1.get$obj$state(false)).text;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Rend o1, pt.ist.socialsoftware.edition.ldod.domain.TextPortion o2) {
            ((Rend_Base.DO_State)o1.get$obj$state(true)).text = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.Rend> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.TextPortion.role$$rend;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.TextPortion,pt.ist.socialsoftware.edition.ldod.domain.Rend> getRelationTextPortionHasRend() {
        return pt.ist.socialsoftware.edition.ldod.domain.TextPortion.getRelationTextPortionHasRend();
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
    protected  Rend_Base() {
        super();
    }
    
    // Getters and Setters
    
    public Rend.Rendition getRend() {
        return ((DO_State)this.get$obj$state(false)).rend;
    }
    
    public void setRend(Rend.Rendition rend) {
        ((DO_State)this.get$obj$state(true)).rend = rend;
    }
    
    private java.lang.String get$rend() {
        Rend.Rendition value = ((DO_State)this.get$obj$state(false)).rend;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$rend(Rend.Rendition value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).rend = (Rend.Rendition)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.TextPortion getText() {
        return ((DO_State)this.get$obj$state(false)).text;
    }
    
    public void setText(pt.ist.socialsoftware.edition.ldod.domain.TextPortion text) {
        getRelationTextPortionHasRend().add(text, (pt.ist.socialsoftware.edition.ldod.domain.Rend)this);
    }
    
    private java.lang.Long get$oidText() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).text;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.text != null) handleAttemptToDeleteConnectedObject("Text");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$rend(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(Rend.Rendition.class, rs, "REND"), state);
        castedState.text = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_TEXT");
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
        private Rend.Rendition rend;
        private pt.ist.socialsoftware.edition.ldod.domain.TextPortion text;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.rend = this.rend;
            newCasted.text = this.text;
            
        }
        
    }
    
}
