package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class AltText_Base extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.AltText,pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight> role$$altTextWeight = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.AltText,pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight> getSet(pt.ist.socialsoftware.edition.ldod.domain.AltText o1) {
            return ((AltText_Base)o1).get$rl$altTextWeight();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight,pt.ist.socialsoftware.edition.ldod.domain.AltText> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight.role$$altText;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight,pt.ist.socialsoftware.edition.ldod.domain.AltText> getRelationAltTextHasAltTextWeight() {
        return pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight.getRelationAltTextHasAltTextWeight();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.AltText,pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight> get$rl$altTextWeight() {
        return get$$relationList("altTextWeight", getRelationAltTextHasAltTextWeight().getInverseRelation());
        
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
    protected  AltText_Base() {
        super();
    }
    
    // Getters and Setters
    
    public AltText.AltMode getMode() {
        return ((DO_State)this.get$obj$state(false)).mode;
    }
    
    public void setMode(AltText.AltMode mode) {
        ((DO_State)this.get$obj$state(true)).mode = mode;
    }
    
    private java.lang.String get$mode() {
        AltText.AltMode value = ((DO_State)this.get$obj$state(false)).mode;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$mode(AltText.AltMode value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).mode = (AltText.AltMode)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public void addAltTextWeight(pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight altTextWeight) {
        getRelationAltTextHasAltTextWeight().add(altTextWeight, (pt.ist.socialsoftware.edition.ldod.domain.AltText)this);
    }
    
    public void removeAltTextWeight(pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight altTextWeight) {
        getRelationAltTextHasAltTextWeight().remove(altTextWeight, (pt.ist.socialsoftware.edition.ldod.domain.AltText)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight> getAltTextWeightSet() {
        return get$rl$altTextWeight();
    }
    
    public void set$altTextWeight(OJBFunctionalSetWrapper altTextWeight) {
        get$rl$altTextWeight().setFromOJB(this, "altTextWeight", altTextWeight);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight> getAltTextWeight() {
        return getAltTextWeightSet();
    }
    
    @Deprecated
    public int getAltTextWeightCount() {
        return getAltTextWeightSet().size();
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$altTextWeight().size() > 0) handleAttemptToDeleteConnectedObject("AltTextWeight");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$mode(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(AltText.AltMode.class, rs, "MODE"), state);
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("altTextWeight")) return getRelationAltTextHasAltTextWeight().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("altTextWeight", getRelationAltTextHasAltTextWeight().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion.DO_State {
        private AltText.AltMode mode;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.mode = this.mode;
            
        }
        
    }
    
}
