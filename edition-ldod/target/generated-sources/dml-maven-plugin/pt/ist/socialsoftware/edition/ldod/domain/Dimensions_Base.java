package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Dimensions_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Dimensions,pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource> role$$manuscriptSource = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Dimensions,pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource getValue(pt.ist.socialsoftware.edition.ldod.domain.Dimensions o1) {
            return ((Dimensions_Base.DO_State)o1.get$obj$state(false)).manuscriptSource;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Dimensions o1, pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource o2) {
            ((Dimensions_Base.DO_State)o1.get$obj$state(true)).manuscriptSource = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.Dimensions> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource.role$$dimensions;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource,pt.ist.socialsoftware.edition.ldod.domain.Dimensions> getRelationManuscriptSourceHasDimensions() {
        return pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource.getRelationManuscriptSourceHasDimensions();
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
    protected  Dimensions_Base() {
        super();
    }
    
    // Getters and Setters
    
    public float getHeight() {
        return ((DO_State)this.get$obj$state(false)).height;
    }
    
    public void setHeight(float height) {
        ((DO_State)this.get$obj$state(true)).height = height;
    }
    
    private float get$height() {
        float value = ((DO_State)this.get$obj$state(false)).height;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForfloat(value);
    }
    
    private final void set$height(float value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).height = (float)(value);
    }
    
    public float getWidth() {
        return ((DO_State)this.get$obj$state(false)).width;
    }
    
    public void setWidth(float width) {
        ((DO_State)this.get$obj$state(true)).width = width;
    }
    
    private float get$width() {
        float value = ((DO_State)this.get$obj$state(false)).width;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForfloat(value);
    }
    
    private final void set$width(float value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).width = (float)(value);
    }
    
    public int getPosition() {
        return ((DO_State)this.get$obj$state(false)).position;
    }
    
    public void setPosition(int position) {
        ((DO_State)this.get$obj$state(true)).position = position;
    }
    
    private int get$position() {
        int value = ((DO_State)this.get$obj$state(false)).position;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$position(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).position = (int)(value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource getManuscriptSource() {
        return ((DO_State)this.get$obj$state(false)).manuscriptSource;
    }
    
    public void setManuscriptSource(pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource manuscriptSource) {
        getRelationManuscriptSourceHasDimensions().add(manuscriptSource, (pt.ist.socialsoftware.edition.ldod.domain.Dimensions)this);
    }
    
    private java.lang.Long get$oidManuscriptSource() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).manuscriptSource;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfManuscriptSource() {
        if (getManuscriptSource() == null) return false;
        return true;
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.manuscriptSource != null) handleAttemptToDeleteConnectedObject("ManuscriptSource");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$height(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readfloat(rs, "HEIGHT"), state);
        set$width(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readfloat(rs, "WIDTH"), state);
        set$position(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "POSITION"), state);
        castedState.manuscriptSource = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_MANUSCRIPT_SOURCE");
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
        private float height;
        private float width;
        private int position;
        private pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource manuscriptSource;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.height = this.height;
            newCasted.width = this.width;
            newCasted.position = this.position;
            newCasted.manuscriptSource = this.manuscriptSource;
            
        }
        
    }
    
}
