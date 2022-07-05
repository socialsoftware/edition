package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Surface_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.Facsimile> role$$facsimile = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.Facsimile>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Facsimile getValue(pt.ist.socialsoftware.edition.ldod.domain.Surface o1) {
            return ((Surface_Base.DO_State)o1.get$obj$state(false)).facsimile;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Surface o1, pt.ist.socialsoftware.edition.ldod.domain.Facsimile o2) {
            ((Surface_Base.DO_State)o1.get$obj$state(true)).facsimile = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Facsimile,pt.ist.socialsoftware.edition.ldod.domain.Surface> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Facsimile.role$$firstSurface;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.PbText> role$$pbText = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.PbText>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.PbText> getSet(pt.ist.socialsoftware.edition.ldod.domain.Surface o1) {
            return ((Surface_Base)o1).get$rl$pbText();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.PbText,pt.ist.socialsoftware.edition.ldod.domain.Surface> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.PbText.role$$surface;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.Surface> role$$prev = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.Surface>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Surface getValue(pt.ist.socialsoftware.edition.ldod.domain.Surface o1) {
            return ((Surface_Base.DO_State)o1.get$obj$state(false)).prev;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Surface o1, pt.ist.socialsoftware.edition.ldod.domain.Surface o2) {
            ((Surface_Base.DO_State)o1.get$obj$state(true)).prev = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.Surface> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Surface.role$$next;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.Surface> role$$next = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.Surface>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Surface getValue(pt.ist.socialsoftware.edition.ldod.domain.Surface o1) {
            return ((Surface_Base.DO_State)o1.get$obj$state(false)).next;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Surface o1, pt.ist.socialsoftware.edition.ldod.domain.Surface o2) {
            ((Surface_Base.DO_State)o1.get$obj$state(true)).next = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.Surface> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Surface.role$$prev;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.RefText> role$$refText = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.RefText>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.RefText> getSet(pt.ist.socialsoftware.edition.ldod.domain.Surface o1) {
            return ((Surface_Base)o1).get$rl$refText();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.RefText,pt.ist.socialsoftware.edition.ldod.domain.Surface> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.RefText.role$$surface;
        }
        
    };
    
    private final static class FacsimileHasSurface {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.Facsimile> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.Facsimile>(role$$facsimile, "FacsimileHasSurface");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.Facsimile> getRelationFacsimileHasSurface() {
        return FacsimileHasSurface.relation;
    }
    
    static {
        FacsimileHasSurface.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Surface.FacsimileHasSurface");
    }
    
    private final static class PbTextSurface {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.PbText> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.PbText>(role$$pbText, "PbTextSurface");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.PbText> getRelationPbTextSurface() {
        return PbTextSurface.relation;
    }
    
    static {
        PbTextSurface.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Surface.PbTextSurface");
    }
    
    
    private final static class SurfaceNextSurface {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.Surface> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.Surface>(role$$next, "SurfaceNextSurface");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.Surface> getRelationSurfaceNextSurface() {
        return SurfaceNextSurface.relation;
    }
    
    static {
        SurfaceNextSurface.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Surface.SurfaceNextSurface");
    }
    
    private final static class RefText2Surface {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.RefText> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.RefText>(role$$refText, "RefText2Surface");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.RefText> getRelationRefText2Surface() {
        return RefText2Surface.relation;
    }
    
    static {
        RefText2Surface.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Surface.RefText2Surface");
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.PbText> get$rl$pbText() {
        return get$$relationList("pbText", getRelationPbTextSurface());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Surface,pt.ist.socialsoftware.edition.ldod.domain.RefText> get$rl$refText() {
        return get$$relationList("refText", getRelationRefText2Surface());
        
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
    protected  Surface_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getXmlId() {
        return ((DO_State)this.get$obj$state(false)).xmlId;
    }
    
    public void setXmlId(java.lang.String xmlId) {
        ((DO_State)this.get$obj$state(true)).xmlId = xmlId;
    }
    
    private java.lang.String get$xmlId() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).xmlId;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$xmlId(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).xmlId = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getGraphic() {
        return ((DO_State)this.get$obj$state(false)).graphic;
    }
    
    public void setGraphic(java.lang.String graphic) {
        ((DO_State)this.get$obj$state(true)).graphic = graphic;
    }
    
    private java.lang.String get$graphic() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).graphic;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$graphic(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).graphic = (java.lang.String)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.Facsimile getFacsimile() {
        return ((DO_State)this.get$obj$state(false)).facsimile;
    }
    
    public void setFacsimile(pt.ist.socialsoftware.edition.ldod.domain.Facsimile facsimile) {
        getRelationFacsimileHasSurface().add((pt.ist.socialsoftware.edition.ldod.domain.Surface)this, facsimile);
    }
    
    private java.lang.Long get$oidFacsimile() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).facsimile;
        return (value == null) ? null : value.getOid();
    }
    
    public void addPbText(pt.ist.socialsoftware.edition.ldod.domain.PbText pbText) {
        getRelationPbTextSurface().add((pt.ist.socialsoftware.edition.ldod.domain.Surface)this, pbText);
    }
    
    public void removePbText(pt.ist.socialsoftware.edition.ldod.domain.PbText pbText) {
        getRelationPbTextSurface().remove((pt.ist.socialsoftware.edition.ldod.domain.Surface)this, pbText);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.PbText> getPbTextSet() {
        return get$rl$pbText();
    }
    
    public void set$pbText(OJBFunctionalSetWrapper pbText) {
        get$rl$pbText().setFromOJB(this, "pbText", pbText);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.PbText> getPbText() {
        return getPbTextSet();
    }
    
    @Deprecated
    public int getPbTextCount() {
        return getPbTextSet().size();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.Surface getPrev() {
        return ((DO_State)this.get$obj$state(false)).prev;
    }
    
    public void setPrev(pt.ist.socialsoftware.edition.ldod.domain.Surface prev) {
        getRelationSurfaceNextSurface().add(prev, (pt.ist.socialsoftware.edition.ldod.domain.Surface)this);
    }
    
    private java.lang.Long get$oidPrev() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).prev;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.Surface getNext() {
        return ((DO_State)this.get$obj$state(false)).next;
    }
    
    public void setNext(pt.ist.socialsoftware.edition.ldod.domain.Surface next) {
        getRelationSurfaceNextSurface().add((pt.ist.socialsoftware.edition.ldod.domain.Surface)this, next);
    }
    
    private java.lang.Long get$oidNext() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).next;
        return (value == null) ? null : value.getOid();
    }
    
    public void addRefText(pt.ist.socialsoftware.edition.ldod.domain.RefText refText) {
        getRelationRefText2Surface().add((pt.ist.socialsoftware.edition.ldod.domain.Surface)this, refText);
    }
    
    public void removeRefText(pt.ist.socialsoftware.edition.ldod.domain.RefText refText) {
        getRelationRefText2Surface().remove((pt.ist.socialsoftware.edition.ldod.domain.Surface)this, refText);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.RefText> getRefTextSet() {
        return get$rl$refText();
    }
    
    public void set$refText(OJBFunctionalSetWrapper refText) {
        get$rl$refText().setFromOJB(this, "refText", refText);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.RefText> getRefText() {
        return getRefTextSet();
    }
    
    @Deprecated
    public int getRefTextCount() {
        return getRefTextSet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.facsimile != null) handleAttemptToDeleteConnectedObject("Facsimile");
        if (get$rl$pbText().size() > 0) handleAttemptToDeleteConnectedObject("PbText");
        if (castedState.prev != null) handleAttemptToDeleteConnectedObject("Prev");
        if (castedState.next != null) handleAttemptToDeleteConnectedObject("Next");
        if (get$rl$refText().size() > 0) handleAttemptToDeleteConnectedObject("RefText");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$xmlId(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "XML_ID"), state);
        set$graphic(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "GRAPHIC"), state);
        castedState.facsimile = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_FACSIMILE");
        castedState.prev = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_PREV");
        castedState.next = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_NEXT");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("pbText")) return getRelationPbTextSurface();
        if (attrName.equals("refText")) return getRelationRefText2Surface();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("pbText", getRelationPbTextSurface());
        get$$relationList("refText", getRelationRefText2Surface());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private java.lang.String xmlId;
        private java.lang.String graphic;
        private pt.ist.socialsoftware.edition.ldod.domain.Facsimile facsimile;
        private pt.ist.socialsoftware.edition.ldod.domain.Surface prev;
        private pt.ist.socialsoftware.edition.ldod.domain.Surface next;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.xmlId = this.xmlId;
            newCasted.graphic = this.graphic;
            newCasted.facsimile = this.facsimile;
            newCasted.prev = this.prev;
            newCasted.next = this.next;
            
        }
        
    }
    
}
