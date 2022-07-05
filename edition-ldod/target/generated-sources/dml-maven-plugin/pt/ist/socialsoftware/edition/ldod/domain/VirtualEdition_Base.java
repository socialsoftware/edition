package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class VirtualEdition_Base extends pt.ist.socialsoftware.edition.ldod.domain.Edition {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD> role$$ldoD4Virtual = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoD getValue(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o1) {
            return ((VirtualEdition_Base.DO_State)o1.get$obj$state(false)).ldoD4Virtual;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o1, pt.ist.socialsoftware.edition.ldod.domain.LdoD o2) {
            ((VirtualEdition_Base.DO_State)o1.get$obj$state(true)).ldoD4Virtual = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoD.role$$virtualEditions;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Taxonomy> role$$taxonomy = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Taxonomy>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Taxonomy getValue(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o1) {
            return ((VirtualEdition_Base.DO_State)o1.get$obj$state(false)).taxonomy;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o1, pt.ist.socialsoftware.edition.ldod.domain.Taxonomy o2) {
            ((VirtualEdition_Base.DO_State)o1.get$obj$state(true)).taxonomy = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Taxonomy,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Taxonomy.role$$edition;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Section> role$$sections = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Section>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Section> getSet(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o1) {
            return ((VirtualEdition_Base)o1).get$rl$sections();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Section,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Section.role$$virtualEdition;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Member> role$$member = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Member>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Member> getSet(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o1) {
            return ((VirtualEdition_Base)o1).get$rl$member();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Member,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Member.role$$virtualEdition;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Edition> role$$uses = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Edition>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Edition getValue(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o1) {
            return ((VirtualEdition_Base.DO_State)o1.get$obj$state(false)).uses;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o1, pt.ist.socialsoftware.edition.ldod.domain.Edition o2) {
            ((VirtualEdition_Base.DO_State)o1.get$obj$state(true)).uses = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Edition,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Edition.role$$isUsedBy;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> role$$classificationGame = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getSet(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o1) {
            return ((VirtualEdition_Base)o1).get$rl$classificationGame();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame.role$$virtualEdition;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights> role$$recommendationWeights = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights> getSet(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o1) {
            return ((VirtualEdition_Base)o1).get$rl$recommendationWeights();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights.role$$virtualEdition;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria> role$$criteria = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria> getSet(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o1) {
            return ((VirtualEdition_Base)o1).get$rl$criteria();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria.role$$virtualEdition;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> role$$selectedBy = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getSet(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o1) {
            return ((VirtualEdition_Base)o1).get$rl$selectedBy();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.role$$selectedVirtualEditions;
        }
        
    };
    
    private final static class LdoDHasVirtualEditions {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD>(role$$ldoD4Virtual, "LdoDHasVirtualEditions");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getRelationLdoDHasVirtualEditions() {
        return LdoDHasVirtualEditions.relation;
    }
    
    static {
        LdoDHasVirtualEditions.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.LdoDHasVirtualEditions");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Taxonomy,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getRelationVirtualEditionHasTaxonomies() {
        return pt.ist.socialsoftware.edition.ldod.domain.Taxonomy.getRelationVirtualEditionHasTaxonomies();
    }
    
    private final static class VirtualEditionHasSections {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Section> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Section>(role$$sections, "VirtualEditionHasSections");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Section> getRelationVirtualEditionHasSections() {
        return VirtualEditionHasSections.relation;
    }
    
    static {
        VirtualEditionHasSections.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.VirtualEditionHasSections");
    }
    
    private final static class MemberHasVirtualEdition {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Member> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Member>(role$$member, "MemberHasVirtualEdition");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Member> getRelationMemberHasVirtualEdition() {
        return MemberHasVirtualEdition.relation;
    }
    
    static {
        MemberHasVirtualEdition.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.MemberHasVirtualEdition");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Edition,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getRelationVirtualEditionUsesEdition() {
        return pt.ist.socialsoftware.edition.ldod.domain.Edition.getRelationVirtualEditionUsesEdition();
    }
    
    private final static class VirtualEditionHasClassificationGames {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame>(role$$classificationGame, "VirtualEditionHasClassificationGames");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getRelationVirtualEditionHasClassificationGames() {
        return VirtualEditionHasClassificationGames.relation;
    }
    
    static {
        VirtualEditionHasClassificationGames.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.VirtualEditionHasClassificationGames");
    }
    
    private final static class VirtualEditionHasRecommendationWeights {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights>(role$$recommendationWeights, "VirtualEditionHasRecommendationWeights");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights> getRelationVirtualEditionHasRecommendationWeights() {
        return VirtualEditionHasRecommendationWeights.relation;
    }
    
    static {
        VirtualEditionHasRecommendationWeights.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.VirtualEditionHasRecommendationWeights");
    }
    
    private final static class VirtualEditionHasSocialMediaCriteria {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria>(role$$criteria, "VirtualEditionHasSocialMediaCriteria");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria> getRelationVirtualEditionHasSocialMediaCriteria() {
        return VirtualEditionHasSocialMediaCriteria.relation;
    }
    
    static {
        VirtualEditionHasSocialMediaCriteria.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.VirtualEditionHasSocialMediaCriteria");
    }
    
    private final static class LdoDUserSelectsVirtualEditions {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser>(role$$selectedBy, "LdoDUserSelectsVirtualEditions");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getRelationLdoDUserSelectsVirtualEditions() {
        return LdoDUserSelectsVirtualEditions.relation;
    }
    
    static {
        LdoDUserSelectsVirtualEditions.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.LdoDUserSelectsVirtualEditions");
        LdoDUserSelectsVirtualEditions.relation.addListener(new pt.ist.fenixframework.dml.runtime.RelationAdapter<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser>() {
            @Override
            public void beforeAdd(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition arg0, pt.ist.socialsoftware.edition.ldod.domain.LdoDUser arg1) {
                pt.ist.fenixframework.backend.jvstmojb.pstm.TransactionSupport.addRelationTuple("LdoDUserSelectsVirtualEditions", arg1, "selectedVirtualEditions", arg0, "selectedBy");
            }
            @Override
            public void beforeRemove(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition arg0, pt.ist.socialsoftware.edition.ldod.domain.LdoDUser arg1) {
                pt.ist.fenixframework.backend.jvstmojb.pstm.TransactionSupport.removeRelationTuple("LdoDUserSelectsVirtualEditions", arg1, "selectedVirtualEditions", arg0, "selectedBy");
            }
            
        }
        );
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Section> get$rl$sections() {
        return get$$relationList("sections", getRelationVirtualEditionHasSections());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.Member> get$rl$member() {
        return get$$relationList("member", getRelationMemberHasVirtualEdition());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> get$rl$classificationGame() {
        return get$$relationList("classificationGame", getRelationVirtualEditionHasClassificationGames());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights> get$rl$recommendationWeights() {
        return get$$relationList("recommendationWeights", getRelationVirtualEditionHasRecommendationWeights());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria> get$rl$criteria() {
        return get$$relationList("criteria", getRelationVirtualEditionHasSocialMediaCriteria());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> get$rl$selectedBy() {
        return get$$relationList("selectedBy", getRelationLdoDUserSelectsVirtualEditions());
        
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
    protected  VirtualEdition_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getSynopsis() {
        return ((DO_State)this.get$obj$state(false)).synopsis;
    }
    
    public void setSynopsis(java.lang.String synopsis) {
        ((DO_State)this.get$obj$state(true)).synopsis = synopsis;
    }
    
    private java.lang.String get$synopsis() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).synopsis;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$synopsis(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).synopsis = (java.lang.String)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoD getLdoD4Virtual() {
        return ((DO_State)this.get$obj$state(false)).ldoD4Virtual;
    }
    
    public void setLdoD4Virtual(pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD4Virtual) {
        getRelationLdoDHasVirtualEditions().add((pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition)this, ldoD4Virtual);
    }
    
    private java.lang.Long get$oidLdoD4Virtual() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).ldoD4Virtual;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfLdoD4Virtual() {
        if (getLdoD4Virtual() == null) return false;
        return true;
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.Taxonomy getTaxonomy() {
        return ((DO_State)this.get$obj$state(false)).taxonomy;
    }
    
    public void setTaxonomy(pt.ist.socialsoftware.edition.ldod.domain.Taxonomy taxonomy) {
        getRelationVirtualEditionHasTaxonomies().add(taxonomy, (pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition)this);
    }
    
    private java.lang.Long get$oidTaxonomy() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).taxonomy;
        return (value == null) ? null : value.getOid();
    }
    
    public void addSections(pt.ist.socialsoftware.edition.ldod.domain.Section sections) {
        getRelationVirtualEditionHasSections().add((pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition)this, sections);
    }
    
    public void removeSections(pt.ist.socialsoftware.edition.ldod.domain.Section sections) {
        getRelationVirtualEditionHasSections().remove((pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition)this, sections);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Section> getSectionsSet() {
        return get$rl$sections();
    }
    
    public void set$sections(OJBFunctionalSetWrapper sections) {
        get$rl$sections().setFromOJB(this, "sections", sections);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Section> getSections() {
        return getSectionsSet();
    }
    
    @Deprecated
    public int getSectionsCount() {
        return getSectionsSet().size();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfSections() {
        if (get$rl$sections().size() < 1) return false;
        return true;
    }
    
    public void addMember(pt.ist.socialsoftware.edition.ldod.domain.Member member) {
        getRelationMemberHasVirtualEdition().add((pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition)this, member);
    }
    
    public void removeMember(pt.ist.socialsoftware.edition.ldod.domain.Member member) {
        getRelationMemberHasVirtualEdition().remove((pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition)this, member);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Member> getMemberSet() {
        return get$rl$member();
    }
    
    public void set$member(OJBFunctionalSetWrapper member) {
        get$rl$member().setFromOJB(this, "member", member);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Member> getMember() {
        return getMemberSet();
    }
    
    @Deprecated
    public int getMemberCount() {
        return getMemberSet().size();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.Edition getUses() {
        return ((DO_State)this.get$obj$state(false)).uses;
    }
    
    public void setUses(pt.ist.socialsoftware.edition.ldod.domain.Edition uses) {
        getRelationVirtualEditionUsesEdition().add(uses, (pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition)this);
    }
    
    private java.lang.Long get$oidUses() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).uses;
        return (value == null) ? null : value.getOid();
    }
    
    public void addClassificationGame(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame classificationGame) {
        getRelationVirtualEditionHasClassificationGames().add((pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition)this, classificationGame);
    }
    
    public void removeClassificationGame(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame classificationGame) {
        getRelationVirtualEditionHasClassificationGames().remove((pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition)this, classificationGame);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getClassificationGameSet() {
        return get$rl$classificationGame();
    }
    
    public void set$classificationGame(OJBFunctionalSetWrapper classificationGame) {
        get$rl$classificationGame().setFromOJB(this, "classificationGame", classificationGame);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getClassificationGame() {
        return getClassificationGameSet();
    }
    
    @Deprecated
    public int getClassificationGameCount() {
        return getClassificationGameSet().size();
    }
    
    public void addRecommendationWeights(pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights recommendationWeights) {
        getRelationVirtualEditionHasRecommendationWeights().add((pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition)this, recommendationWeights);
    }
    
    public void removeRecommendationWeights(pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights recommendationWeights) {
        getRelationVirtualEditionHasRecommendationWeights().remove((pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition)this, recommendationWeights);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights> getRecommendationWeightsSet() {
        return get$rl$recommendationWeights();
    }
    
    public void set$recommendationWeights(OJBFunctionalSetWrapper recommendationWeights) {
        get$rl$recommendationWeights().setFromOJB(this, "recommendationWeights", recommendationWeights);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights> getRecommendationWeights() {
        return getRecommendationWeightsSet();
    }
    
    @Deprecated
    public int getRecommendationWeightsCount() {
        return getRecommendationWeightsSet().size();
    }
    
    public void addCriteria(pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria criteria) {
        getRelationVirtualEditionHasSocialMediaCriteria().add((pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition)this, criteria);
    }
    
    public void removeCriteria(pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria criteria) {
        getRelationVirtualEditionHasSocialMediaCriteria().remove((pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition)this, criteria);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria> getCriteriaSet() {
        return get$rl$criteria();
    }
    
    public void set$criteria(OJBFunctionalSetWrapper criteria) {
        get$rl$criteria().setFromOJB(this, "criteria", criteria);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria> getCriteria() {
        return getCriteriaSet();
    }
    
    @Deprecated
    public int getCriteriaCount() {
        return getCriteriaSet().size();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfCriteria() {
        if (get$rl$criteria().size() > 4) return false;
        return true;
    }
    
    public void addSelectedBy(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser selectedBy) {
        getRelationLdoDUserSelectsVirtualEditions().add((pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition)this, selectedBy);
    }
    
    public void removeSelectedBy(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser selectedBy) {
        getRelationLdoDUserSelectsVirtualEditions().remove((pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition)this, selectedBy);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getSelectedBySet() {
        return get$rl$selectedBy();
    }
    
    public void set$selectedBy(OJBFunctionalSetWrapper selectedBy) {
        get$rl$selectedBy().setFromOJB(this, "selectedBy", selectedBy);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getSelectedBy() {
        return getSelectedBySet();
    }
    
    @Deprecated
    public int getSelectedByCount() {
        return getSelectedBySet().size();
    }
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.ldoD4Virtual != null) handleAttemptToDeleteConnectedObject("LdoD4Virtual");
        if (castedState.taxonomy != null) handleAttemptToDeleteConnectedObject("Taxonomy");
        if (get$rl$sections().size() > 0) handleAttemptToDeleteConnectedObject("Sections");
        if (get$rl$member().size() > 0) handleAttemptToDeleteConnectedObject("Member");
        if (castedState.uses != null) handleAttemptToDeleteConnectedObject("Uses");
        if (get$rl$classificationGame().size() > 0) handleAttemptToDeleteConnectedObject("ClassificationGame");
        if (get$rl$recommendationWeights().size() > 0) handleAttemptToDeleteConnectedObject("RecommendationWeights");
        if (get$rl$criteria().size() > 0) handleAttemptToDeleteConnectedObject("Criteria");
        if (get$rl$selectedBy().size() > 0) handleAttemptToDeleteConnectedObject("SelectedBy");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$synopsis(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "SYNOPSIS"), state);
        castedState.ldoD4Virtual = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_LDO_D4_VIRTUAL");
        castedState.taxonomy = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_TAXONOMY");
        castedState.uses = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_USES");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("sections")) return getRelationVirtualEditionHasSections();
        if (attrName.equals("member")) return getRelationMemberHasVirtualEdition();
        if (attrName.equals("classificationGame")) return getRelationVirtualEditionHasClassificationGames();
        if (attrName.equals("recommendationWeights")) return getRelationVirtualEditionHasRecommendationWeights();
        if (attrName.equals("criteria")) return getRelationVirtualEditionHasSocialMediaCriteria();
        if (attrName.equals("selectedBy")) return getRelationLdoDUserSelectsVirtualEditions();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("sections", getRelationVirtualEditionHasSections());
        get$$relationList("member", getRelationMemberHasVirtualEdition());
        get$$relationList("classificationGame", getRelationVirtualEditionHasClassificationGames());
        get$$relationList("recommendationWeights", getRelationVirtualEditionHasRecommendationWeights());
        get$$relationList("criteria", getRelationVirtualEditionHasSocialMediaCriteria());
        get$$relationList("selectedBy", getRelationLdoDUserSelectsVirtualEditions());
        
    }
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.Edition.DO_State {
        private java.lang.String synopsis;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD4Virtual;
        private pt.ist.socialsoftware.edition.ldod.domain.Taxonomy taxonomy;
        private pt.ist.socialsoftware.edition.ldod.domain.Edition uses;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.synopsis = this.synopsis;
            newCasted.ldoD4Virtual = this.ldoD4Virtual;
            newCasted.taxonomy = this.taxonomy;
            newCasted.uses = this.uses;
            
        }
        
    }
    
}
