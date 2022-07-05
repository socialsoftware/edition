package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class PbText_Base extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.PbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> role$$fragInter = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.PbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.FragInter> getSet(pt.ist.socialsoftware.edition.ldod.domain.PbText o1) {
            return ((PbText_Base)o1).get$rl$fragInter();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.PbText> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.FragInter.role$$pbText;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.PbText,pt.ist.socialsoftware.edition.ldod.domain.Surface> role$$surface = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.PbText,pt.ist.socialsoftware.edition.ldod.domain.Surface>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Surface getValue(pt.ist.socialsoftware.edition.ldod.domain.PbText o1) {
            return ((PbText_Base.DO_State)o1.get$obj$state(false)).surface;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.PbText o1, pt.ist.socialsoftware.edition.ldod.domain.Surface o2) {
            ((PbText_Base.DO_State)o1.get$obj$state(true)).surface = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.PbText> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Surface.role$$pbText;
        }
        
    };
    
    private final static class PbTextHasFragInters {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.PbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.PbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter>(role$$fragInter, "PbTextHasFragInters");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.PbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getRelationPbTextHasFragInters() {
        return PbTextHasFragInters.relation;
    }
    
    static {
        PbTextHasFragInters.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.PbText.PbTextHasFragInters");
        PbTextHasFragInters.relation.addListener(new pt.ist.fenixframework.dml.runtime.RelationAdapter<pt.ist.socialsoftware.edition.ldod.domain.PbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter>() {
            @Override
            public void beforeAdd(pt.ist.socialsoftware.edition.ldod.domain.PbText arg0, pt.ist.socialsoftware.edition.ldod.domain.FragInter arg1) {
                pt.ist.fenixframework.backend.jvstmojb.pstm.TransactionSupport.addRelationTuple("PbTextHasFragInters", arg1, "pbText", arg0, "fragInter");
            }
            @Override
            public void beforeRemove(pt.ist.socialsoftware.edition.ldod.domain.PbText arg0, pt.ist.socialsoftware.edition.ldod.domain.FragInter arg1) {
                pt.ist.fenixframework.backend.jvstmojb.pstm.TransactionSupport.removeRelationTuple("PbTextHasFragInters", arg1, "pbText", arg0, "fragInter");
            }
            
        }
        );
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.PbText> getRelationPbTextSurface() {
        return pt.ist.socialsoftware.edition.ldod.domain.Surface.getRelationPbTextSurface();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.PbText,pt.ist.socialsoftware.edition.ldod.domain.FragInter> get$rl$fragInter() {
        return get$$relationList("fragInter", getRelationPbTextHasFragInters());
        
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
    protected  PbText_Base() {
        super();
    }
    
    // Getters and Setters
    
    public int getOrder() {
        return ((DO_State)this.get$obj$state(false)).order;
    }
    
    public void setOrder(int order) {
        ((DO_State)this.get$obj$state(true)).order = order;
    }
    
    private int get$order() {
        int value = ((DO_State)this.get$obj$state(false)).order;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$order(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).order = (int)(value);
    }
    
    // Role Methods
    
    public void addFragInter(pt.ist.socialsoftware.edition.ldod.domain.FragInter fragInter) {
        getRelationPbTextHasFragInters().add((pt.ist.socialsoftware.edition.ldod.domain.PbText)this, fragInter);
    }
    
    public void removeFragInter(pt.ist.socialsoftware.edition.ldod.domain.FragInter fragInter) {
        getRelationPbTextHasFragInters().remove((pt.ist.socialsoftware.edition.ldod.domain.PbText)this, fragInter);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.FragInter> getFragInterSet() {
        return get$rl$fragInter();
    }
    
    public void set$fragInter(OJBFunctionalSetWrapper fragInter) {
        get$rl$fragInter().setFromOJB(this, "fragInter", fragInter);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.FragInter> getFragInter() {
        return getFragInterSet();
    }
    
    @Deprecated
    public int getFragInterCount() {
        return getFragInterSet().size();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.Surface getSurface() {
        return ((DO_State)this.get$obj$state(false)).surface;
    }
    
    public void setSurface(pt.ist.socialsoftware.edition.ldod.domain.Surface surface) {
        getRelationPbTextSurface().add(surface, (pt.ist.socialsoftware.edition.ldod.domain.PbText)this);
    }
    
    private java.lang.Long get$oidSurface() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).surface;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$fragInter().size() > 0) handleAttemptToDeleteConnectedObject("FragInter");
        if (castedState.surface != null) handleAttemptToDeleteConnectedObject("Surface");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$order(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "ORDER"), state);
        castedState.surface = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_SURFACE");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("fragInter")) return getRelationPbTextHasFragInters();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("fragInter", getRelationPbTextHasFragInters());
        
    }
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.TextPortion.DO_State {
        private int order;
        private pt.ist.socialsoftware.edition.ldod.domain.Surface surface;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.order = this.order;
            newCasted.surface = this.surface;
            
        }
        
    }
    
}
