package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class SegText_Base extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.SegText,pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight> role$$altTextWeight = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.SegText,pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight getValue(pt.ist.socialsoftware.edition.ldod.domain.SegText o1) {
            return ((SegText_Base.DO_State)o1.get$obj$state(false)).altTextWeight;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.SegText o1, pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight o2) {
            ((SegText_Base.DO_State)o1.get$obj$state(true)).altTextWeight = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight,pt.ist.socialsoftware.edition.ldod.domain.SegText> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight.role$$segText;
        }
        
    };
    
    private final static class AltTexWeightHasSegText {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.SegText,pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.SegText,pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight>(role$$altTextWeight, "AltTexWeightHasSegText");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.SegText,pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight> getRelationAltTexWeightHasSegText() {
        return AltTexWeightHasSegText.relation;
    }
    
    static {
        AltTexWeightHasSegText.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.SegText.AltTexWeightHasSegText");
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
    protected  SegText_Base() {
        super();
    }
    
    // Getters and Setters
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight getAltTextWeight() {
        return ((DO_State)this.get$obj$state(false)).altTextWeight;
    }
    
    public void setAltTextWeight(pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight altTextWeight) {
        getRelationAltTexWeightHasSegText().add((pt.ist.socialsoftware.edition.ldod.domain.SegText)this, altTextWeight);
    }
    
    private java.lang.Long get$oidAltTextWeight() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).altTextWeight;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.altTextWeight != null) handleAttemptToDeleteConnectedObject("AltTextWeight");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        castedState.altTextWeight = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_ALT_TEXT_WEIGHT");
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
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion.DO_State {
        private pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight altTextWeight;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.altTextWeight = this.altTextWeight;
            
        }
        
    }
    
}
