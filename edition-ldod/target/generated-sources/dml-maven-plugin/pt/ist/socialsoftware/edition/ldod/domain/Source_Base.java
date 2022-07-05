package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Source_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Heteronym> role$$heteronym = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Heteronym>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Heteronym getValue(pt.ist.socialsoftware.edition.ldod.domain.Source o1) {
            return ((Source_Base.DO_State)o1.get$obj$state(false)).heteronym;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Source o1, pt.ist.socialsoftware.edition.ldod.domain.Heteronym o2) {
            ((Source_Base.DO_State)o1.get$obj$state(true)).heteronym = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Heteronym,pt.ist.socialsoftware.edition.ldod.domain.Source> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Heteronym.role$$source;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.LdoDDate> role$$ldoDDate = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.LdoDDate>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoDDate getValue(pt.ist.socialsoftware.edition.ldod.domain.Source o1) {
            return ((Source_Base.DO_State)o1.get$obj$state(false)).ldoDDate;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Source o1, pt.ist.socialsoftware.edition.ldod.domain.LdoDDate o2) {
            ((Source_Base.DO_State)o1.get$obj$state(true)).ldoDDate = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoDDate,pt.ist.socialsoftware.edition.ldod.domain.Source> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoDDate.role$$source;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Facsimile> role$$facsimile = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Facsimile>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Facsimile getValue(pt.ist.socialsoftware.edition.ldod.domain.Source o1) {
            return ((Source_Base.DO_State)o1.get$obj$state(false)).facsimile;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Source o1, pt.ist.socialsoftware.edition.ldod.domain.Facsimile o2) {
            ((Source_Base.DO_State)o1.get$obj$state(true)).facsimile = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Facsimile,pt.ist.socialsoftware.edition.ldod.domain.Source> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Facsimile.role$$source;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Fragment> role$$fragment = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Fragment>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Fragment getValue(pt.ist.socialsoftware.edition.ldod.domain.Source o1) {
            return ((Source_Base.DO_State)o1.get$obj$state(false)).fragment;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Source o1, pt.ist.socialsoftware.edition.ldod.domain.Fragment o2) {
            ((Source_Base.DO_State)o1.get$obj$state(true)).fragment = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.Source> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Fragment.role$$sources;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.SourceInter> role$$sourceInters = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.SourceInter>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.SourceInter> getSet(pt.ist.socialsoftware.edition.ldod.domain.Source o1) {
            return ((Source_Base)o1).get$rl$sourceInters();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.SourceInter,pt.ist.socialsoftware.edition.ldod.domain.Source> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.SourceInter.role$$source;
        }
        
    };
    
    private final static class SourceHasHeteronym {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Heteronym> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Heteronym>(role$$heteronym, "SourceHasHeteronym");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Heteronym> getRelationSourceHasHeteronym() {
        return SourceHasHeteronym.relation;
    }
    
    static {
        SourceHasHeteronym.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Source.SourceHasHeteronym");
    }
    
    private final static class SourceHasDate {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.LdoDDate> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.LdoDDate>(role$$ldoDDate, "SourceHasDate");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.LdoDDate> getRelationSourceHasDate() {
        return SourceHasDate.relation;
    }
    
    static {
        SourceHasDate.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Source.SourceHasDate");
    }
    
    private final static class SourceHasFacsimile {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Facsimile> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Facsimile>(role$$facsimile, "SourceHasFacsimile");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Facsimile> getRelationSourceHasFacsimile() {
        return SourceHasFacsimile.relation;
    }
    
    static {
        SourceHasFacsimile.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Source.SourceHasFacsimile");
    }
    
    private final static class FragmentHasSources {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Fragment> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Fragment>(role$$fragment, "FragmentHasSources");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Fragment> getRelationFragmentHasSources() {
        return FragmentHasSources.relation;
    }
    
    static {
        FragmentHasSources.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Source.FragmentHasSources");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.SourceInter,pt.ist.socialsoftware.edition.ldod.domain.Source> getRelationSourceHasSourceInters() {
        return pt.ist.socialsoftware.edition.ldod.domain.SourceInter.getRelationSourceHasSourceInters();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.SourceInter> get$rl$sourceInters() {
        return get$$relationList("sourceInters", getRelationSourceHasSourceInters().getInverseRelation());
        
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
    protected  Source_Base() {
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
    
    public Source.SourceType getType() {
        return ((DO_State)this.get$obj$state(false)).type;
    }
    
    public void setType(Source.SourceType type) {
        ((DO_State)this.get$obj$state(true)).type = type;
    }
    
    private java.lang.String get$type() {
        Source.SourceType value = ((DO_State)this.get$obj$state(false)).type;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$type(Source.SourceType value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).type = (Source.SourceType)((value == null) ? null : value);
    }
    
    public java.lang.String getSettlement() {
        return ((DO_State)this.get$obj$state(false)).settlement;
    }
    
    public void setSettlement(java.lang.String settlement) {
        ((DO_State)this.get$obj$state(true)).settlement = settlement;
    }
    
    private java.lang.String get$settlement() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).settlement;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$settlement(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).settlement = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getRepository() {
        return ((DO_State)this.get$obj$state(false)).repository;
    }
    
    public void setRepository(java.lang.String repository) {
        ((DO_State)this.get$obj$state(true)).repository = repository;
    }
    
    private java.lang.String get$repository() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).repository;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$repository(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).repository = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getIdno() {
        return ((DO_State)this.get$obj$state(false)).idno;
    }
    
    public void setIdno(java.lang.String idno) {
        ((DO_State)this.get$obj$state(true)).idno = idno;
    }
    
    private java.lang.String get$idno() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).idno;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$idno(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).idno = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getAltIdentifier() {
        return ((DO_State)this.get$obj$state(false)).altIdentifier;
    }
    
    public void setAltIdentifier(java.lang.String altIdentifier) {
        ((DO_State)this.get$obj$state(true)).altIdentifier = altIdentifier;
    }
    
    private java.lang.String get$altIdentifier() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).altIdentifier;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$altIdentifier(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).altIdentifier = (java.lang.String)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.Heteronym getHeteronym() {
        return ((DO_State)this.get$obj$state(false)).heteronym;
    }
    
    public void setHeteronym(pt.ist.socialsoftware.edition.ldod.domain.Heteronym heteronym) {
        getRelationSourceHasHeteronym().add((pt.ist.socialsoftware.edition.ldod.domain.Source)this, heteronym);
    }
    
    private java.lang.Long get$oidHeteronym() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).heteronym;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoDDate getLdoDDate() {
        return ((DO_State)this.get$obj$state(false)).ldoDDate;
    }
    
    public void setLdoDDate(pt.ist.socialsoftware.edition.ldod.domain.LdoDDate ldoDDate) {
        getRelationSourceHasDate().add((pt.ist.socialsoftware.edition.ldod.domain.Source)this, ldoDDate);
    }
    
    private java.lang.Long get$oidLdoDDate() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).ldoDDate;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.Facsimile getFacsimile() {
        return ((DO_State)this.get$obj$state(false)).facsimile;
    }
    
    public void setFacsimile(pt.ist.socialsoftware.edition.ldod.domain.Facsimile facsimile) {
        getRelationSourceHasFacsimile().add((pt.ist.socialsoftware.edition.ldod.domain.Source)this, facsimile);
    }
    
    private java.lang.Long get$oidFacsimile() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).facsimile;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.Fragment getFragment() {
        return ((DO_State)this.get$obj$state(false)).fragment;
    }
    
    public void setFragment(pt.ist.socialsoftware.edition.ldod.domain.Fragment fragment) {
        getRelationFragmentHasSources().add((pt.ist.socialsoftware.edition.ldod.domain.Source)this, fragment);
    }
    
    private java.lang.Long get$oidFragment() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).fragment;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfFragment() {
        if (getFragment() == null) return false;
        return true;
    }
    
    public void addSourceInters(pt.ist.socialsoftware.edition.ldod.domain.SourceInter sourceInters) {
        getRelationSourceHasSourceInters().add(sourceInters, (pt.ist.socialsoftware.edition.ldod.domain.Source)this);
    }
    
    public void removeSourceInters(pt.ist.socialsoftware.edition.ldod.domain.SourceInter sourceInters) {
        getRelationSourceHasSourceInters().remove(sourceInters, (pt.ist.socialsoftware.edition.ldod.domain.Source)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.SourceInter> getSourceIntersSet() {
        return get$rl$sourceInters();
    }
    
    public void set$sourceInters(OJBFunctionalSetWrapper sourceInters) {
        get$rl$sourceInters().setFromOJB(this, "sourceInters", sourceInters);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.SourceInter> getSourceInters() {
        return getSourceIntersSet();
    }
    
    @Deprecated
    public int getSourceIntersCount() {
        return getSourceIntersSet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.heteronym != null) handleAttemptToDeleteConnectedObject("Heteronym");
        if (castedState.ldoDDate != null) handleAttemptToDeleteConnectedObject("LdoDDate");
        if (castedState.facsimile != null) handleAttemptToDeleteConnectedObject("Facsimile");
        if (castedState.fragment != null) handleAttemptToDeleteConnectedObject("Fragment");
        if (get$rl$sourceInters().size() > 0) handleAttemptToDeleteConnectedObject("SourceInters");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$xmlId(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "XML_ID"), state);
        set$type(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(Source.SourceType.class, rs, "TYPE"), state);
        set$settlement(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "SETTLEMENT"), state);
        set$repository(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "REPOSITORY"), state);
        set$idno(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "IDNO"), state);
        set$altIdentifier(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "ALT_IDENTIFIER"), state);
        castedState.heteronym = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_HETERONYM");
        castedState.ldoDDate = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_LDO_D_DATE");
        castedState.facsimile = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_FACSIMILE");
        castedState.fragment = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_FRAGMENT");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("sourceInters")) return getRelationSourceHasSourceInters().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("sourceInters", getRelationSourceHasSourceInters().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private java.lang.String xmlId;
        private Source.SourceType type;
        private java.lang.String settlement;
        private java.lang.String repository;
        private java.lang.String idno;
        private java.lang.String altIdentifier;
        private pt.ist.socialsoftware.edition.ldod.domain.Heteronym heteronym;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoDDate ldoDDate;
        private pt.ist.socialsoftware.edition.ldod.domain.Facsimile facsimile;
        private pt.ist.socialsoftware.edition.ldod.domain.Fragment fragment;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.xmlId = this.xmlId;
            newCasted.type = this.type;
            newCasted.settlement = this.settlement;
            newCasted.repository = this.repository;
            newCasted.idno = this.idno;
            newCasted.altIdentifier = this.altIdentifier;
            newCasted.heteronym = this.heteronym;
            newCasted.ldoDDate = this.ldoDDate;
            newCasted.facsimile = this.facsimile;
            newCasted.fragment = this.fragment;
            
        }
        
    }
    
}
