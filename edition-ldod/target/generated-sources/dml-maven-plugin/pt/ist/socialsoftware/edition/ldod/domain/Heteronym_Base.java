package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Heteronym_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Heteronym,pt.ist.socialsoftware.edition.ldod.domain.Source> role$$source = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Heteronym,pt.ist.socialsoftware.edition.ldod.domain.Source>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Source> getSet(pt.ist.socialsoftware.edition.ldod.domain.Heteronym o1) {
            return ((Heteronym_Base)o1).get$rl$source();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Heteronym> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Source.role$$heteronym;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Heteronym,pt.ist.socialsoftware.edition.ldod.domain.FragInter> role$$fragInter = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Heteronym,pt.ist.socialsoftware.edition.ldod.domain.FragInter>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.FragInter> getSet(pt.ist.socialsoftware.edition.ldod.domain.Heteronym o1) {
            return ((Heteronym_Base)o1).get$rl$fragInter();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.FragInter,pt.ist.socialsoftware.edition.ldod.domain.Heteronym> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.FragInter.role$$heteronym;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Heteronym,pt.ist.socialsoftware.edition.ldod.domain.LdoD> role$$ldoD = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Heteronym,pt.ist.socialsoftware.edition.ldod.domain.LdoD>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoD getValue(pt.ist.socialsoftware.edition.ldod.domain.Heteronym o1) {
            return ((Heteronym_Base.DO_State)o1.get$obj$state(false)).ldoD;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Heteronym o1, pt.ist.socialsoftware.edition.ldod.domain.LdoD o2) {
            ((Heteronym_Base.DO_State)o1.get$obj$state(true)).ldoD = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Heteronym> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoD.role$$heteronyms;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Source,pt.ist.socialsoftware.edition.ldod.domain.Heteronym> getRelationSourceHasHeteronym() {
        return pt.ist.socialsoftware.edition.ldod.domain.Source.getRelationSourceHasHeteronym();
    }
    
    private final static class FragInterAttributesHeteronym {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Heteronym,pt.ist.socialsoftware.edition.ldod.domain.FragInter> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Heteronym,pt.ist.socialsoftware.edition.ldod.domain.FragInter>(role$$fragInter, "FragInterAttributesHeteronym");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Heteronym,pt.ist.socialsoftware.edition.ldod.domain.FragInter> getRelationFragInterAttributesHeteronym() {
        return FragInterAttributesHeteronym.relation;
    }
    
    static {
        FragInterAttributesHeteronym.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Heteronym.FragInterAttributesHeteronym");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Heteronym> getRelationLdoDHasHeteronyms() {
        return pt.ist.socialsoftware.edition.ldod.domain.LdoD.getRelationLdoDHasHeteronyms();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Heteronym,pt.ist.socialsoftware.edition.ldod.domain.Source> get$rl$source() {
        return get$$relationList("source", getRelationSourceHasHeteronym().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Heteronym,pt.ist.socialsoftware.edition.ldod.domain.FragInter> get$rl$fragInter() {
        return get$$relationList("fragInter", getRelationFragInterAttributesHeteronym());
        
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
    protected  Heteronym_Base() {
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
    
    public java.lang.String getName() {
        return ((DO_State)this.get$obj$state(false)).name;
    }
    
    public void setName(java.lang.String name) {
        ((DO_State)this.get$obj$state(true)).name = name;
    }
    
    private java.lang.String get$name() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).name;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$name(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).name = (java.lang.String)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public void addSource(pt.ist.socialsoftware.edition.ldod.domain.Source source) {
        getRelationSourceHasHeteronym().add(source, (pt.ist.socialsoftware.edition.ldod.domain.Heteronym)this);
    }
    
    public void removeSource(pt.ist.socialsoftware.edition.ldod.domain.Source source) {
        getRelationSourceHasHeteronym().remove(source, (pt.ist.socialsoftware.edition.ldod.domain.Heteronym)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Source> getSourceSet() {
        return get$rl$source();
    }
    
    public void set$source(OJBFunctionalSetWrapper source) {
        get$rl$source().setFromOJB(this, "source", source);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Source> getSource() {
        return getSourceSet();
    }
    
    @Deprecated
    public int getSourceCount() {
        return getSourceSet().size();
    }
    
    public void addFragInter(pt.ist.socialsoftware.edition.ldod.domain.FragInter fragInter) {
        getRelationFragInterAttributesHeteronym().add((pt.ist.socialsoftware.edition.ldod.domain.Heteronym)this, fragInter);
    }
    
    public void removeFragInter(pt.ist.socialsoftware.edition.ldod.domain.FragInter fragInter) {
        getRelationFragInterAttributesHeteronym().remove((pt.ist.socialsoftware.edition.ldod.domain.Heteronym)this, fragInter);
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
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoD getLdoD() {
        return ((DO_State)this.get$obj$state(false)).ldoD;
    }
    
    public void setLdoD(pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD) {
        getRelationLdoDHasHeteronyms().add(ldoD, (pt.ist.socialsoftware.edition.ldod.domain.Heteronym)this);
    }
    
    private java.lang.Long get$oidLdoD() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).ldoD;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfLdoD() {
        if (getLdoD() == null) return false;
        return true;
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$source().size() > 0) handleAttemptToDeleteConnectedObject("Source");
        if (get$rl$fragInter().size() > 0) handleAttemptToDeleteConnectedObject("FragInter");
        if (castedState.ldoD != null) handleAttemptToDeleteConnectedObject("LdoD");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$xmlId(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "XML_ID"), state);
        set$name(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "NAME"), state);
        castedState.ldoD = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_LDO_D");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("source")) return getRelationSourceHasHeteronym().getInverseRelation();
        if (attrName.equals("fragInter")) return getRelationFragInterAttributesHeteronym();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("source", getRelationSourceHasHeteronym().getInverseRelation());
        get$$relationList("fragInter", getRelationFragInterAttributesHeteronym());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private java.lang.String xmlId;
        private java.lang.String name;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.xmlId = this.xmlId;
            newCasted.name = this.name;
            newCasted.ldoD = this.ldoD;
            
        }
        
    }
    
}
