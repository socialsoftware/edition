package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class RefText_Base extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.Fragment> role$$refFrag = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.Fragment>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Fragment getValue(pt.ist.socialsoftware.edition.ldod.domain.RefText o1) {
            return ((RefText_Base.DO_State)o1.get$obj$state(false)).refFrag;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.RefText o1, pt.ist.socialsoftware.edition.ldod.domain.Fragment o2) {
            ((RefText_Base.DO_State)o1.get$obj$state(true)).refFrag = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.RefText> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Fragment.role$$refText;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> role$$fragInter = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.FragInter>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.FragInter getValue(pt.ist.socialsoftware.edition.ldod.domain.RefText o1) {
            return ((RefText_Base.DO_State)o1.get$obj$state(false)).fragInter;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.RefText o1, pt.ist.socialsoftware.edition.ldod.domain.FragInter o2) {
            ((RefText_Base.DO_State)o1.get$obj$state(true)).fragInter = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.RefText> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.FragInter.role$$refText;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.Surface> role$$surface = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.Surface>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Surface getValue(pt.ist.socialsoftware.edition.ldod.domain.RefText o1) {
            return ((RefText_Base.DO_State)o1.get$obj$state(false)).surface;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.RefText o1, pt.ist.socialsoftware.edition.ldod.domain.Surface o2) {
            ((RefText_Base.DO_State)o1.get$obj$state(true)).surface = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.RefText> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Surface.role$$refText;
        }
        
    };
    
    private final static class RefText2Fragment {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.Fragment> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.Fragment>(role$$refFrag, "RefText2Fragment");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.Fragment> getRelationRefText2Fragment() {
        return RefText2Fragment.relation;
    }
    
    static {
        RefText2Fragment.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.RefText.RefText2Fragment");
    }
    
    private final static class RefText2FragInter {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.FragInter>(role$$fragInter, "RefText2FragInter");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getRelationRefText2FragInter() {
        return RefText2FragInter.relation;
    }
    
    static {
        RefText2FragInter.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.RefText.RefText2FragInter");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.RefText> getRelationRefText2Surface() {
        return pt.ist.socialsoftware.edition.ldod.domain.Surface.getRelationRefText2Surface();
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
    protected  RefText_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getTarget() {
        return ((DO_State)this.get$obj$state(false)).target;
    }
    
    public void setTarget(java.lang.String target) {
        ((DO_State)this.get$obj$state(true)).target = target;
    }
    
    private java.lang.String get$target() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).target;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$target(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).target = (java.lang.String)((value == null) ? null : value);
    }
    
    public RefText.RefType getType() {
        return ((DO_State)this.get$obj$state(false)).type;
    }
    
    public void setType(RefText.RefType type) {
        ((DO_State)this.get$obj$state(true)).type = type;
    }
    
    private java.lang.String get$type() {
        RefText.RefType value = ((DO_State)this.get$obj$state(false)).type;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$type(RefText.RefType value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).type = (RefText.RefType)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.Fragment getRefFrag() {
        return ((DO_State)this.get$obj$state(false)).refFrag;
    }
    
    public void setRefFrag(pt.ist.socialsoftware.edition.ldod.domain.Fragment refFrag) {
        getRelationRefText2Fragment().add((pt.ist.socialsoftware.edition.ldod.domain.RefText)this, refFrag);
    }
    
    private java.lang.Long get$oidRefFrag() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).refFrag;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.FragInter getFragInter() {
        return ((DO_State)this.get$obj$state(false)).fragInter;
    }
    
    public void setFragInter(pt.ist.socialsoftware.edition.ldod.domain.FragInter fragInter) {
        getRelationRefText2FragInter().add((pt.ist.socialsoftware.edition.ldod.domain.RefText)this, fragInter);
    }
    
    private java.lang.Long get$oidFragInter() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).fragInter;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.Surface getSurface() {
        return ((DO_State)this.get$obj$state(false)).surface;
    }
    
    public void setSurface(pt.ist.socialsoftware.edition.ldod.domain.Surface surface) {
        getRelationRefText2Surface().add(surface, (pt.ist.socialsoftware.edition.ldod.domain.RefText)this);
    }
    
    private java.lang.Long get$oidSurface() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).surface;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.refFrag != null) handleAttemptToDeleteConnectedObject("RefFrag");
        if (castedState.fragInter != null) handleAttemptToDeleteConnectedObject("FragInter");
        if (castedState.surface != null) handleAttemptToDeleteConnectedObject("Surface");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$target(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "TARGET"), state);
        set$type(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(RefText.RefType.class, rs, "TYPE"), state);
        castedState.refFrag = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_REF_FRAG");
        castedState.fragInter = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_FRAG_INTER");
        castedState.surface = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_SURFACE");
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
        private java.lang.String target;
        private RefText.RefType type;
        private pt.ist.socialsoftware.edition.ldod.domain.Fragment refFrag;
        private pt.ist.socialsoftware.edition.ldod.domain.FragInter fragInter;
        private pt.ist.socialsoftware.edition.ldod.domain.Surface surface;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.target = this.target;
            newCasted.type = this.type;
            newCasted.refFrag = this.refFrag;
            newCasted.fragInter = this.fragInter;
            newCasted.surface = this.surface;
            
        }
        
    }
    
}
