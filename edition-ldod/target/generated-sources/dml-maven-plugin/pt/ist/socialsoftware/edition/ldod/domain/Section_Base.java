package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Section_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.Section> role$$subSections = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.Section>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Section> getSet(pt.ist.socialsoftware.edition.ldod.domain.Section o1) {
            return ((Section_Base)o1).get$rl$subSections();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.Section> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Section.role$$parentSection;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.Section> role$$parentSection = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.Section>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Section getValue(pt.ist.socialsoftware.edition.ldod.domain.Section o1) {
            return ((Section_Base.DO_State)o1.get$obj$state(false)).parentSection;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Section o1, pt.ist.socialsoftware.edition.ldod.domain.Section o2) {
            ((Section_Base.DO_State)o1.get$obj$state(true)).parentSection = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.Section> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Section.role$$subSections;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> role$$virtualEdition = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition getValue(pt.ist.socialsoftware.edition.ldod.domain.Section o1) {
            return ((Section_Base.DO_State)o1.get$obj$state(false)).virtualEdition;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Section o1, pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o2) {
            ((Section_Base.DO_State)o1.get$obj$state(true)).virtualEdition = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Section> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.role$$sections;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> role$$VirtualEditionInter = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getSet(pt.ist.socialsoftware.edition.ldod.domain.Section o1) {
            return ((Section_Base)o1).get$rl$VirtualEditionInter();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.Section> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter.role$$section;
        }
        
    };
    
    
    private final static class SectionHasSections {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.Section> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.Section>(role$$parentSection, "SectionHasSections");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.Section> getRelationSectionHasSections() {
        return SectionHasSections.relation;
    }
    
    static {
        SectionHasSections.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Section.SectionHasSections");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Section> getRelationVirtualEditionHasSections() {
        return pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.getRelationVirtualEditionHasSections();
    }
    
    private final static class SectionHasVitualEditionInters {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter>(role$$VirtualEditionInter, "SectionHasVitualEditionInters");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getRelationSectionHasVitualEditionInters() {
        return SectionHasVitualEditionInters.relation;
    }
    
    static {
        SectionHasVitualEditionInters.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Section.SectionHasVitualEditionInters");
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.Section> get$rl$subSections() {
        return get$$relationList("subSections", getRelationSectionHasSections().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> get$rl$VirtualEditionInter() {
        return get$$relationList("VirtualEditionInter", getRelationSectionHasVitualEditionInters());
        
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
    protected  Section_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getTitle() {
        return ((DO_State)this.get$obj$state(false)).title;
    }
    
    public void setTitle(java.lang.String title) {
        ((DO_State)this.get$obj$state(true)).title = title;
    }
    
    private java.lang.String get$title() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).title;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$title(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).title = (java.lang.String)((value == null) ? null : value);
    }
    
    public int getNumber() {
        return ((DO_State)this.get$obj$state(false)).number;
    }
    
    public void setNumber(int number) {
        ((DO_State)this.get$obj$state(true)).number = number;
    }
    
    private int get$number() {
        int value = ((DO_State)this.get$obj$state(false)).number;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$number(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).number = (int)(value);
    }
    
    // Role Methods
    
    public void addSubSections(pt.ist.socialsoftware.edition.ldod.domain.Section subSections) {
        getRelationSectionHasSections().add(subSections, (pt.ist.socialsoftware.edition.ldod.domain.Section)this);
    }
    
    public void removeSubSections(pt.ist.socialsoftware.edition.ldod.domain.Section subSections) {
        getRelationSectionHasSections().remove(subSections, (pt.ist.socialsoftware.edition.ldod.domain.Section)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Section> getSubSectionsSet() {
        return get$rl$subSections();
    }
    
    public void set$subSections(OJBFunctionalSetWrapper subSections) {
        get$rl$subSections().setFromOJB(this, "subSections", subSections);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Section> getSubSections() {
        return getSubSectionsSet();
    }
    
    @Deprecated
    public int getSubSectionsCount() {
        return getSubSectionsSet().size();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.Section getParentSection() {
        return ((DO_State)this.get$obj$state(false)).parentSection;
    }
    
    public void setParentSection(pt.ist.socialsoftware.edition.ldod.domain.Section parentSection) {
        getRelationSectionHasSections().add((pt.ist.socialsoftware.edition.ldod.domain.Section)this, parentSection);
    }
    
    private java.lang.Long get$oidParentSection() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).parentSection;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition getVirtualEdition() {
        return ((DO_State)this.get$obj$state(false)).virtualEdition;
    }
    
    public void setVirtualEdition(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition virtualEdition) {
        getRelationVirtualEditionHasSections().add(virtualEdition, (pt.ist.socialsoftware.edition.ldod.domain.Section)this);
    }
    
    private java.lang.Long get$oidVirtualEdition() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).virtualEdition;
        return (value == null) ? null : value.getOid();
    }
    
    public void addVirtualEditionInter(pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter VirtualEditionInter) {
        getRelationSectionHasVitualEditionInters().add((pt.ist.socialsoftware.edition.ldod.domain.Section)this, VirtualEditionInter);
    }
    
    public void removeVirtualEditionInter(pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter VirtualEditionInter) {
        getRelationSectionHasVitualEditionInters().remove((pt.ist.socialsoftware.edition.ldod.domain.Section)this, VirtualEditionInter);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getVirtualEditionInterSet() {
        return get$rl$VirtualEditionInter();
    }
    
    public void set$VirtualEditionInter(OJBFunctionalSetWrapper VirtualEditionInter) {
        get$rl$VirtualEditionInter().setFromOJB(this, "VirtualEditionInter", VirtualEditionInter);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> getVirtualEditionInter() {
        return getVirtualEditionInterSet();
    }
    
    @Deprecated
    public int getVirtualEditionInterCount() {
        return getVirtualEditionInterSet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$subSections().size() > 0) handleAttemptToDeleteConnectedObject("SubSections");
        if (castedState.parentSection != null) handleAttemptToDeleteConnectedObject("ParentSection");
        if (castedState.virtualEdition != null) handleAttemptToDeleteConnectedObject("VirtualEdition");
        if (get$rl$VirtualEditionInter().size() > 0) handleAttemptToDeleteConnectedObject("VirtualEditionInter");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$title(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "TITLE"), state);
        set$number(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "NUMBER"), state);
        castedState.parentSection = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_PARENT_SECTION");
        castedState.virtualEdition = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_VIRTUAL_EDITION");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("subSections")) return getRelationSectionHasSections().getInverseRelation();
        if (attrName.equals("VirtualEditionInter")) return getRelationSectionHasVitualEditionInters();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("subSections", getRelationSectionHasSections().getInverseRelation());
        get$$relationList("VirtualEditionInter", getRelationSectionHasVitualEditionInters());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private java.lang.String title;
        private int number;
        private pt.ist.socialsoftware.edition.ldod.domain.Section parentSection;
        private pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition virtualEdition;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.title = this.title;
            newCasted.number = this.number;
            newCasted.parentSection = this.parentSection;
            newCasted.virtualEdition = this.virtualEdition;
            
        }
        
    }
    
}
