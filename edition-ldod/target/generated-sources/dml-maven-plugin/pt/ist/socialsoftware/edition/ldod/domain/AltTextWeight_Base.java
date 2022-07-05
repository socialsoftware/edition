package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class AltTextWeight_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight,pt.ist.socialsoftware.edition.ldod.domain.AltText> role$$altText = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight,pt.ist.socialsoftware.edition.ldod.domain.AltText>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.AltText getValue(pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight o1) {
            return ((AltTextWeight_Base.DO_State)o1.get$obj$state(false)).altText;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight o1, pt.ist.socialsoftware.edition.ldod.domain.AltText o2) {
            ((AltTextWeight_Base.DO_State)o1.get$obj$state(true)).altText = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.AltText,pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.AltText.role$$altTextWeight;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight,pt.ist.socialsoftware.edition.ldod.domain.SegText> role$$segText = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight,pt.ist.socialsoftware.edition.ldod.domain.SegText>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.SegText getValue(pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight o1) {
            return ((AltTextWeight_Base.DO_State)o1.get$obj$state(false)).segText;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight o1, pt.ist.socialsoftware.edition.ldod.domain.SegText o2) {
            ((AltTextWeight_Base.DO_State)o1.get$obj$state(true)).segText = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.SegText,pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.SegText.role$$altTextWeight;
        }
        
    };
    
    private final static class AltTextHasAltTextWeight {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight,pt.ist.socialsoftware.edition.ldod.domain.AltText> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight,pt.ist.socialsoftware.edition.ldod.domain.AltText>(role$$altText, "AltTextHasAltTextWeight");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight,pt.ist.socialsoftware.edition.ldod.domain.AltText> getRelationAltTextHasAltTextWeight() {
        return AltTextHasAltTextWeight.relation;
    }
    
    static {
        AltTextHasAltTextWeight.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight.AltTextHasAltTextWeight");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.SegText,pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight> getRelationAltTexWeightHasSegText() {
        return pt.ist.socialsoftware.edition.ldod.domain.SegText.getRelationAltTexWeightHasSegText();
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
    protected  AltTextWeight_Base() {
        super();
    }
    
    // Getters and Setters
    
    public double getWeight() {
        return ((DO_State)this.get$obj$state(false)).weight;
    }
    
    public void setWeight(double weight) {
        ((DO_State)this.get$obj$state(true)).weight = weight;
    }
    
    private double get$weight() {
        double value = ((DO_State)this.get$obj$state(false)).weight;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueFordouble(value);
    }
    
    private final void set$weight(double value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).weight = (double)(value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.AltText getAltText() {
        return ((DO_State)this.get$obj$state(false)).altText;
    }
    
    public void setAltText(pt.ist.socialsoftware.edition.ldod.domain.AltText altText) {
        getRelationAltTextHasAltTextWeight().add((pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight)this, altText);
    }
    
    private java.lang.Long get$oidAltText() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).altText;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.SegText getSegText() {
        return ((DO_State)this.get$obj$state(false)).segText;
    }
    
    public void setSegText(pt.ist.socialsoftware.edition.ldod.domain.SegText segText) {
        getRelationAltTexWeightHasSegText().add(segText, (pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight)this);
    }
    
    private java.lang.Long get$oidSegText() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).segText;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.altText != null) handleAttemptToDeleteConnectedObject("AltText");
        if (castedState.segText != null) handleAttemptToDeleteConnectedObject("SegText");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$weight(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readdouble(rs, "WEIGHT"), state);
        castedState.altText = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_ALT_TEXT");
        castedState.segText = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_SEG_TEXT");
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
        private double weight;
        private pt.ist.socialsoftware.edition.ldod.domain.AltText altText;
        private pt.ist.socialsoftware.edition.ldod.domain.SegText segText;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.weight = this.weight;
            newCasted.altText = this.altText;
            newCasted.segText = this.segText;
            
        }
        
    }
    
}
