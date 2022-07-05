package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Category_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Category,pt.ist.socialsoftware.edition.ldod.domain.Taxonomy> role$$taxonomy = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Category,pt.ist.socialsoftware.edition.ldod.domain.Taxonomy>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Taxonomy getValue(pt.ist.socialsoftware.edition.ldod.domain.Category o1) {
            return ((Category_Base.DO_State)o1.get$obj$state(false)).taxonomy;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Category o1, pt.ist.socialsoftware.edition.ldod.domain.Taxonomy o2) {
            ((Category_Base.DO_State)o1.get$obj$state(true)).taxonomy = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Taxonomy,pt.ist.socialsoftware.edition.ldod.domain.Category> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Taxonomy.role$$categories;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Category,pt.ist.socialsoftware.edition.ldod.domain.Tag> role$$tag = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Category,pt.ist.socialsoftware.edition.ldod.domain.Tag>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Tag> getSet(pt.ist.socialsoftware.edition.ldod.domain.Category o1) {
            return ((Category_Base)o1).get$rl$tag();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.Category> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Tag.role$$category;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Taxonomy,pt.ist.socialsoftware.edition.ldod.domain.Category> getRelationTaxonomyHasCategories() {
        return pt.ist.socialsoftware.edition.ldod.domain.Taxonomy.getRelationTaxonomyHasCategories();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.Category> getRelationTag2Category() {
        return pt.ist.socialsoftware.edition.ldod.domain.Tag.getRelationTag2Category();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Category,pt.ist.socialsoftware.edition.ldod.domain.Tag> get$rl$tag() {
        return get$$relationList("tag", getRelationTag2Category().getInverseRelation());
        
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
    protected  Category_Base() {
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
    
    public pt.ist.socialsoftware.edition.ldod.domain.Taxonomy getTaxonomy() {
        return ((DO_State)this.get$obj$state(false)).taxonomy;
    }
    
    public void setTaxonomy(pt.ist.socialsoftware.edition.ldod.domain.Taxonomy taxonomy) {
        getRelationTaxonomyHasCategories().add(taxonomy, (pt.ist.socialsoftware.edition.ldod.domain.Category)this);
    }
    
    private java.lang.Long get$oidTaxonomy() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).taxonomy;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfTaxonomy() {
        if (getTaxonomy() == null) return false;
        return true;
    }
    
    public void addTag(pt.ist.socialsoftware.edition.ldod.domain.Tag tag) {
        getRelationTag2Category().add(tag, (pt.ist.socialsoftware.edition.ldod.domain.Category)this);
    }
    
    public void removeTag(pt.ist.socialsoftware.edition.ldod.domain.Tag tag) {
        getRelationTag2Category().remove(tag, (pt.ist.socialsoftware.edition.ldod.domain.Category)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Tag> getTagSet() {
        return get$rl$tag();
    }
    
    public void set$tag(OJBFunctionalSetWrapper tag) {
        get$rl$tag().setFromOJB(this, "tag", tag);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Tag> getTag() {
        return getTagSet();
    }
    
    @Deprecated
    public int getTagCount() {
        return getTagSet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.taxonomy != null) handleAttemptToDeleteConnectedObject("Taxonomy");
        if (get$rl$tag().size() > 0) handleAttemptToDeleteConnectedObject("Tag");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$xmlId(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "XML_ID"), state);
        set$name(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "NAME"), state);
        castedState.taxonomy = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_TAXONOMY");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("tag")) return getRelationTag2Category().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("tag", getRelationTag2Category().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private java.lang.String xmlId;
        private java.lang.String name;
        private pt.ist.socialsoftware.edition.ldod.domain.Taxonomy taxonomy;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.xmlId = this.xmlId;
            newCasted.name = this.name;
            newCasted.taxonomy = this.taxonomy;
            
        }
        
    }
    
}
