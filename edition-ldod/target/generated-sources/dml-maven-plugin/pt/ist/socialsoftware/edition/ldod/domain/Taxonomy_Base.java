package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Taxonomy_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Taxonomy,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> role$$edition = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Taxonomy,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition getValue(pt.ist.socialsoftware.edition.ldod.domain.Taxonomy o1) {
            return ((Taxonomy_Base.DO_State)o1.get$obj$state(false)).edition;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Taxonomy o1, pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o2) {
            ((Taxonomy_Base.DO_State)o1.get$obj$state(true)).edition = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Taxonomy> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.role$$taxonomy;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Taxonomy,pt.ist.socialsoftware.edition.ldod.domain.Category> role$$categories = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Taxonomy,pt.ist.socialsoftware.edition.ldod.domain.Category>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Category> getSet(pt.ist.socialsoftware.edition.ldod.domain.Taxonomy o1) {
            return ((Taxonomy_Base)o1).get$rl$categories();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Category,pt.ist.socialsoftware.edition.ldod.domain.Taxonomy> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Category.role$$taxonomy;
        }
        
    };
    
    private final static class VirtualEditionHasTaxonomies {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Taxonomy,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Taxonomy,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition>(role$$edition, "VirtualEditionHasTaxonomies");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Taxonomy,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getRelationVirtualEditionHasTaxonomies() {
        return VirtualEditionHasTaxonomies.relation;
    }
    
    static {
        VirtualEditionHasTaxonomies.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Taxonomy.VirtualEditionHasTaxonomies");
    }
    
    private final static class TaxonomyHasCategories {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Taxonomy,pt.ist.socialsoftware.edition.ldod.domain.Category> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Taxonomy,pt.ist.socialsoftware.edition.ldod.domain.Category>(role$$categories, "TaxonomyHasCategories");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Taxonomy,pt.ist.socialsoftware.edition.ldod.domain.Category> getRelationTaxonomyHasCategories() {
        return TaxonomyHasCategories.relation;
    }
    
    static {
        TaxonomyHasCategories.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Taxonomy.TaxonomyHasCategories");
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Taxonomy,pt.ist.socialsoftware.edition.ldod.domain.Category> get$rl$categories() {
        return get$$relationList("categories", getRelationTaxonomyHasCategories());
        
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
    protected  Taxonomy_Base() {
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
    
    public boolean getOpenManagement() {
        return ((DO_State)this.get$obj$state(false)).openManagement;
    }
    
    public void setOpenManagement(boolean openManagement) {
        ((DO_State)this.get$obj$state(true)).openManagement = openManagement;
    }
    
    private boolean get$openManagement() {
        boolean value = ((DO_State)this.get$obj$state(false)).openManagement;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForboolean(value);
    }
    
    private final void set$openManagement(boolean value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).openManagement = (boolean)(value);
    }
    
    public boolean getOpenVocabulary() {
        return ((DO_State)this.get$obj$state(false)).openVocabulary;
    }
    
    public void setOpenVocabulary(boolean openVocabulary) {
        ((DO_State)this.get$obj$state(true)).openVocabulary = openVocabulary;
    }
    
    private boolean get$openVocabulary() {
        boolean value = ((DO_State)this.get$obj$state(false)).openVocabulary;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForboolean(value);
    }
    
    private final void set$openVocabulary(boolean value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).openVocabulary = (boolean)(value);
    }
    
    public boolean getOpenAnnotation() {
        return ((DO_State)this.get$obj$state(false)).openAnnotation;
    }
    
    public void setOpenAnnotation(boolean openAnnotation) {
        ((DO_State)this.get$obj$state(true)).openAnnotation = openAnnotation;
    }
    
    private boolean get$openAnnotation() {
        boolean value = ((DO_State)this.get$obj$state(false)).openAnnotation;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForboolean(value);
    }
    
    private final void set$openAnnotation(boolean value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).openAnnotation = (boolean)(value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition getEdition() {
        return ((DO_State)this.get$obj$state(false)).edition;
    }
    
    public void setEdition(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition edition) {
        getRelationVirtualEditionHasTaxonomies().add((pt.ist.socialsoftware.edition.ldod.domain.Taxonomy)this, edition);
    }
    
    private java.lang.Long get$oidEdition() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).edition;
        return (value == null) ? null : value.getOid();
    }
    
    public void addCategories(pt.ist.socialsoftware.edition.ldod.domain.Category categories) {
        getRelationTaxonomyHasCategories().add((pt.ist.socialsoftware.edition.ldod.domain.Taxonomy)this, categories);
    }
    
    public void removeCategories(pt.ist.socialsoftware.edition.ldod.domain.Category categories) {
        getRelationTaxonomyHasCategories().remove((pt.ist.socialsoftware.edition.ldod.domain.Taxonomy)this, categories);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Category> getCategoriesSet() {
        return get$rl$categories();
    }
    
    public void set$categories(OJBFunctionalSetWrapper categories) {
        get$rl$categories().setFromOJB(this, "categories", categories);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Category> getCategories() {
        return getCategoriesSet();
    }
    
    @Deprecated
    public int getCategoriesCount() {
        return getCategoriesSet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.edition != null) handleAttemptToDeleteConnectedObject("Edition");
        if (get$rl$categories().size() > 0) handleAttemptToDeleteConnectedObject("Categories");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$xmlId(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "XML_ID"), state);
        set$openManagement(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readboolean(rs, "OPEN_MANAGEMENT"), state);
        set$openVocabulary(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readboolean(rs, "OPEN_VOCABULARY"), state);
        set$openAnnotation(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readboolean(rs, "OPEN_ANNOTATION"), state);
        castedState.edition = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_EDITION");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("categories")) return getRelationTaxonomyHasCategories();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("categories", getRelationTaxonomyHasCategories());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private java.lang.String xmlId;
        private boolean openManagement;
        private boolean openVocabulary;
        private boolean openAnnotation;
        private pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition edition;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.xmlId = this.xmlId;
            newCasted.openManagement = this.openManagement;
            newCasted.openVocabulary = this.openVocabulary;
            newCasted.openAnnotation = this.openAnnotation;
            newCasted.edition = this.edition;
            
        }
        
    }
    
}
