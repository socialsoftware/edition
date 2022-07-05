package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class LdoDDate_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LdoDDate,pt.ist.socialsoftware.edition.ldod.domain.Source> role$$source = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LdoDDate,pt.ist.socialsoftware.edition.ldod.domain.Source>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Source getValue(pt.ist.socialsoftware.edition.ldod.domain.LdoDDate o1) {
            return ((LdoDDate_Base.DO_State)o1.get$obj$state(false)).source;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.LdoDDate o1, pt.ist.socialsoftware.edition.ldod.domain.Source o2) {
            ((LdoDDate_Base.DO_State)o1.get$obj$state(true)).source = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.LdoDDate> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Source.role$$ldoDDate;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LdoDDate,pt.ist.socialsoftware.edition.ldod.domain.FragInter> role$$fragInter = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LdoDDate,pt.ist.socialsoftware.edition.ldod.domain.FragInter>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.FragInter getValue(pt.ist.socialsoftware.edition.ldod.domain.LdoDDate o1) {
            return ((LdoDDate_Base.DO_State)o1.get$obj$state(false)).fragInter;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.LdoDDate o1, pt.ist.socialsoftware.edition.ldod.domain.FragInter o2) {
            ((LdoDDate_Base.DO_State)o1.get$obj$state(true)).fragInter = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.LdoDDate> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.FragInter.role$$ldoDDate;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.LdoDDate> getRelationSourceHasDate() {
        return pt.ist.socialsoftware.edition.ldod.domain.Source.getRelationSourceHasDate();
    }
    
    private final static class FragInterHasDate {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDDate,pt.ist.socialsoftware.edition.ldod.domain.FragInter> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDDate,pt.ist.socialsoftware.edition.ldod.domain.FragInter>(role$$fragInter, "FragInterHasDate");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDDate,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getRelationFragInterHasDate() {
        return FragInterHasDate.relation;
    }
    
    static {
        FragInterHasDate.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.LdoDDate.FragInterHasDate");
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
    protected  LdoDDate_Base() {
        super();
    }
    
    // Getters and Setters
    
    public org.joda.time.LocalDate getDate() {
        return ((DO_State)this.get$obj$state(false)).date;
    }
    
    public void setDate(org.joda.time.LocalDate date) {
        ((DO_State)this.get$obj$state(true)).date = date;
    }
    
    private java.lang.String get$date() {
        org.joda.time.LocalDate value = ((DO_State)this.get$obj$state(false)).date;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForLocalDate(value);
    }
    
    private final void set$date(org.joda.time.LocalDate value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).date = (org.joda.time.LocalDate)((value == null) ? null : value);
    }
    
    public LdoDDate.DateType getType() {
        return ((DO_State)this.get$obj$state(false)).type;
    }
    
    public void setType(LdoDDate.DateType type) {
        ((DO_State)this.get$obj$state(true)).type = type;
    }
    
    private java.lang.String get$type() {
        LdoDDate.DateType value = ((DO_State)this.get$obj$state(false)).type;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$type(LdoDDate.DateType value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).type = (LdoDDate.DateType)((value == null) ? null : value);
    }
    
    public Fragment.PrecisionType getPrecision() {
        return ((DO_State)this.get$obj$state(false)).precision;
    }
    
    public void setPrecision(Fragment.PrecisionType precision) {
        ((DO_State)this.get$obj$state(true)).precision = precision;
    }
    
    private java.lang.String get$precision() {
        Fragment.PrecisionType value = ((DO_State)this.get$obj$state(false)).precision;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$precision(Fragment.PrecisionType value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).precision = (Fragment.PrecisionType)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.Source getSource() {
        return ((DO_State)this.get$obj$state(false)).source;
    }
    
    public void setSource(pt.ist.socialsoftware.edition.ldod.domain.Source source) {
        getRelationSourceHasDate().add(source, (pt.ist.socialsoftware.edition.ldod.domain.LdoDDate)this);
    }
    
    private java.lang.Long get$oidSource() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).source;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.FragInter getFragInter() {
        return ((DO_State)this.get$obj$state(false)).fragInter;
    }
    
    public void setFragInter(pt.ist.socialsoftware.edition.ldod.domain.FragInter fragInter) {
        getRelationFragInterHasDate().add((pt.ist.socialsoftware.edition.ldod.domain.LdoDDate)this, fragInter);
    }
    
    private java.lang.Long get$oidFragInter() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).fragInter;
        return (value == null) ? null : value.getOid();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.source != null) handleAttemptToDeleteConnectedObject("Source");
        if (castedState.fragInter != null) handleAttemptToDeleteConnectedObject("FragInter");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$date(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readLocalDate(rs, "DATE"), state);
        set$type(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(LdoDDate.DateType.class, rs, "TYPE"), state);
        set$precision(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(Fragment.PrecisionType.class, rs, "PRECISION"), state);
        castedState.source = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_SOURCE");
        castedState.fragInter = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_FRAG_INTER");
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
        private org.joda.time.LocalDate date;
        private LdoDDate.DateType type;
        private Fragment.PrecisionType precision;
        private pt.ist.socialsoftware.edition.ldod.domain.Source source;
        private pt.ist.socialsoftware.edition.ldod.domain.FragInter fragInter;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.date = this.date;
            newCasted.type = this.type;
            newCasted.precision = this.precision;
            newCasted.source = this.source;
            newCasted.fragInter = this.fragInter;
            
        }
        
    }
    
}
