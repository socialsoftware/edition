package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class LdoDUser_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken> role$$token = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken getValue(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o1) {
            return ((LdoDUser_Base.DO_State)o1.get$obj$state(false)).token;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o1, pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken o2) {
            ((LdoDUser_Base.DO_State)o1.get$obj$state(true)).token = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken.role$$user;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Annotation> role$$annotation = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Annotation>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Annotation> getSet(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o1) {
            return ((LdoDUser_Base)o1).get$rl$annotation();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Annotation,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Annotation.role$$user;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights> role$$recommendationWeights = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights> getSet(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o1) {
            return ((LdoDUser_Base)o1).get$rl$recommendationWeights();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights.role$$user;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Player> role$$player = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Player>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Player getValue(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o1) {
            return ((LdoDUser_Base.DO_State)o1.get$obj$state(false)).player;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o1, pt.ist.socialsoftware.edition.ldod.domain.Player o2) {
            ((LdoDUser_Base.DO_State)o1.get$obj$state(true)).player = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Player,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Player.role$$user;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Tag> role$$tag = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Tag>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Tag> getSet(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o1) {
            return ((LdoDUser_Base)o1).get$rl$tag();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Tag.role$$contributor;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.LdoD> role$$ldoD = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.LdoD>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoD getValue(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o1) {
            return ((LdoDUser_Base.DO_State)o1.get$obj$state(false)).ldoD;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o1, pt.ist.socialsoftware.edition.ldod.domain.LdoD o2) {
            ((LdoDUser_Base.DO_State)o1.get$obj$state(true)).ldoD = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoD.role$$users;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Role> role$$roles = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Role>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Role> getSet(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o1) {
            return ((LdoDUser_Base)o1).get$rl$roles();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Role,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Role.role$$users;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> role$$selectedVirtualEditions = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getSet(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o1) {
            return ((LdoDUser_Base)o1).get$rl$selectedVirtualEditions();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.role$$selectedBy;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> role$$responsibleForGames = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getSet(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o1) {
            return ((LdoDUser_Base)o1).get$rl$responsibleForGames();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame.role$$responsible;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Member> role$$member = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Member>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Member> getSet(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o1) {
            return ((LdoDUser_Base)o1).get$rl$member();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Member,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Member.role$$user;
        }
        
    };
    
    private final static class LdoDUserHasRegistrationToken {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken>(role$$token, "LdoDUserHasRegistrationToken");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken> getRelationLdoDUserHasRegistrationToken() {
        return LdoDUserHasRegistrationToken.relation;
    }
    
    static {
        LdoDUserHasRegistrationToken.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.LdoDUserHasRegistrationToken");
    }
    
    private final static class LdoDUserWritesAnnotations {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Annotation> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Annotation>(role$$annotation, "LdoDUserWritesAnnotations");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Annotation> getRelationLdoDUserWritesAnnotations() {
        return LdoDUserWritesAnnotations.relation;
    }
    
    static {
        LdoDUserWritesAnnotations.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.LdoDUserWritesAnnotations");
    }
    
    private final static class LdoDUserHasRecommendationWeights {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights>(role$$recommendationWeights, "LdoDUserHasRecommendationWeights");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights> getRelationLdoDUserHasRecommendationWeights() {
        return LdoDUserHasRecommendationWeights.relation;
    }
    
    static {
        LdoDUserHasRecommendationWeights.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.LdoDUserHasRecommendationWeights");
    }
    
    private final static class LdodUserIsPlayer {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Player> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Player>(role$$player, "LdodUserIsPlayer");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Player> getRelationLdodUserIsPlayer() {
        return LdodUserIsPlayer.relation;
    }
    
    static {
        LdodUserIsPlayer.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.LdodUserIsPlayer");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getRelationTagHasContributor() {
        return pt.ist.socialsoftware.edition.ldod.domain.Tag.getRelationTagHasContributor();
    }
    
    private final static class LdoDHasLdoDUsers {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.LdoD> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.LdoD>(role$$ldoD, "LdoDHasLdoDUsers");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getRelationLdoDHasLdoDUsers() {
        return LdoDHasLdoDUsers.relation;
    }
    
    static {
        LdoDHasLdoDUsers.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.LdoDHasLdoDUsers");
    }
    
    private final static class LdoDUsersAndRoles {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Role> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Role>(role$$roles, "LdoDUsersAndRoles");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Role> getRelationLdoDUsersAndRoles() {
        return LdoDUsersAndRoles.relation;
    }
    
    static {
        LdoDUsersAndRoles.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.LdoDUsersAndRoles");
        LdoDUsersAndRoles.relation.addListener(new pt.ist.fenixframework.dml.runtime.RelationAdapter<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Role>() {
            @Override
            public void beforeAdd(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser arg0, pt.ist.socialsoftware.edition.ldod.domain.Role arg1) {
                pt.ist.fenixframework.backend.jvstmojb.pstm.TransactionSupport.addRelationTuple("LdoDUsersAndRoles", arg1, "users", arg0, "roles");
            }
            @Override
            public void beforeRemove(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser arg0, pt.ist.socialsoftware.edition.ldod.domain.Role arg1) {
                pt.ist.fenixframework.backend.jvstmojb.pstm.TransactionSupport.removeRelationTuple("LdoDUsersAndRoles", arg1, "users", arg0, "roles");
            }
            
        }
        );
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getRelationLdoDUserSelectsVirtualEditions() {
        return pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.getRelationLdoDUserSelectsVirtualEditions();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getRelationLdoDUserOwnsClassificationGame() {
        return pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame.getRelationLdoDUserOwnsClassificationGame();
    }
    
    private final static class MemberHasLdoDUser {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Member> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Member>(role$$member, "MemberHasLdoDUser");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Member> getRelationMemberHasLdoDUser() {
        return MemberHasLdoDUser.relation;
    }
    
    static {
        MemberHasLdoDUser.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.MemberHasLdoDUser");
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Annotation> get$rl$annotation() {
        return get$$relationList("annotation", getRelationLdoDUserWritesAnnotations());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights> get$rl$recommendationWeights() {
        return get$$relationList("recommendationWeights", getRelationLdoDUserHasRecommendationWeights());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Tag> get$rl$tag() {
        return get$$relationList("tag", getRelationTagHasContributor().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Role> get$rl$roles() {
        return get$$relationList("roles", getRelationLdoDUsersAndRoles());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> get$rl$selectedVirtualEditions() {
        return get$$relationList("selectedVirtualEditions", getRelationLdoDUserSelectsVirtualEditions().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> get$rl$responsibleForGames() {
        return get$$relationList("responsibleForGames", getRelationLdoDUserOwnsClassificationGame().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.Member> get$rl$member() {
        return get$$relationList("member", getRelationMemberHasLdoDUser());
        
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
    protected  LdoDUser_Base() {
        super();
    }
    
    // Getters and Setters
    
    public boolean getEnabled() {
        return ((DO_State)this.get$obj$state(false)).enabled;
    }
    
    public void setEnabled(boolean enabled) {
        ((DO_State)this.get$obj$state(true)).enabled = enabled;
    }
    
    private boolean get$enabled() {
        boolean value = ((DO_State)this.get$obj$state(false)).enabled;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForboolean(value);
    }
    
    private final void set$enabled(boolean value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).enabled = (boolean)(value);
    }
    
    public boolean getActive() {
        return ((DO_State)this.get$obj$state(false)).active;
    }
    
    public void setActive(boolean active) {
        ((DO_State)this.get$obj$state(true)).active = active;
    }
    
    private boolean get$active() {
        boolean value = ((DO_State)this.get$obj$state(false)).active;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForboolean(value);
    }
    
    private final void set$active(boolean value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).active = (boolean)(value);
    }
    
    public java.lang.String getUsername() {
        return ((DO_State)this.get$obj$state(false)).username;
    }
    
    public void setUsername(java.lang.String username) {
        ((DO_State)this.get$obj$state(true)).username = username;
    }
    
    private java.lang.String get$username() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).username;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$username(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).username = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getPassword() {
        return ((DO_State)this.get$obj$state(false)).password;
    }
    
    public void setPassword(java.lang.String password) {
        ((DO_State)this.get$obj$state(true)).password = password;
    }
    
    private java.lang.String get$password() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).password;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$password(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).password = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getFirstName() {
        return ((DO_State)this.get$obj$state(false)).firstName;
    }
    
    public void setFirstName(java.lang.String firstName) {
        ((DO_State)this.get$obj$state(true)).firstName = firstName;
    }
    
    private java.lang.String get$firstName() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).firstName;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$firstName(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).firstName = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getLastName() {
        return ((DO_State)this.get$obj$state(false)).lastName;
    }
    
    public void setLastName(java.lang.String lastName) {
        ((DO_State)this.get$obj$state(true)).lastName = lastName;
    }
    
    private java.lang.String get$lastName() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).lastName;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$lastName(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).lastName = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getEmail() {
        return ((DO_State)this.get$obj$state(false)).email;
    }
    
    public void setEmail(java.lang.String email) {
        ((DO_State)this.get$obj$state(true)).email = email;
    }
    
    private java.lang.String get$email() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).email;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$email(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).email = (java.lang.String)((value == null) ? null : value);
    }
    
    public org.joda.time.LocalDate getLastLogin() {
        return ((DO_State)this.get$obj$state(false)).lastLogin;
    }
    
    public void setLastLogin(org.joda.time.LocalDate lastLogin) {
        ((DO_State)this.get$obj$state(true)).lastLogin = lastLogin;
    }
    
    private java.lang.String get$lastLogin() {
        org.joda.time.LocalDate value = ((DO_State)this.get$obj$state(false)).lastLogin;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForLocalDate(value);
    }
    
    private final void set$lastLogin(org.joda.time.LocalDate value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).lastLogin = (org.joda.time.LocalDate)((value == null) ? null : value);
    }
    
    public LdoDUser.SocialMediaService getSocialMediaService() {
        return ((DO_State)this.get$obj$state(false)).socialMediaService;
    }
    
    public void setSocialMediaService(LdoDUser.SocialMediaService socialMediaService) {
        ((DO_State)this.get$obj$state(true)).socialMediaService = socialMediaService;
    }
    
    private java.lang.String get$socialMediaService() {
        LdoDUser.SocialMediaService value = ((DO_State)this.get$obj$state(false)).socialMediaService;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$socialMediaService(LdoDUser.SocialMediaService value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).socialMediaService = (LdoDUser.SocialMediaService)((value == null) ? null : value);
    }
    
    public java.lang.String getSocialMediaId() {
        return ((DO_State)this.get$obj$state(false)).socialMediaId;
    }
    
    public void setSocialMediaId(java.lang.String socialMediaId) {
        ((DO_State)this.get$obj$state(true)).socialMediaId = socialMediaId;
    }
    
    private java.lang.String get$socialMediaId() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).socialMediaId;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$socialMediaId(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).socialMediaId = (java.lang.String)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken getToken() {
        return ((DO_State)this.get$obj$state(false)).token;
    }
    
    public void setToken(pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken token) {
        getRelationLdoDUserHasRegistrationToken().add((pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this, token);
    }
    
    private java.lang.Long get$oidToken() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).token;
        return (value == null) ? null : value.getOid();
    }
    
    public void addAnnotation(pt.ist.socialsoftware.edition.ldod.domain.Annotation annotation) {
        getRelationLdoDUserWritesAnnotations().add((pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this, annotation);
    }
    
    public void removeAnnotation(pt.ist.socialsoftware.edition.ldod.domain.Annotation annotation) {
        getRelationLdoDUserWritesAnnotations().remove((pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this, annotation);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Annotation> getAnnotationSet() {
        return get$rl$annotation();
    }
    
    public void set$annotation(OJBFunctionalSetWrapper annotation) {
        get$rl$annotation().setFromOJB(this, "annotation", annotation);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Annotation> getAnnotation() {
        return getAnnotationSet();
    }
    
    @Deprecated
    public int getAnnotationCount() {
        return getAnnotationSet().size();
    }
    
    public void addRecommendationWeights(pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights recommendationWeights) {
        getRelationLdoDUserHasRecommendationWeights().add((pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this, recommendationWeights);
    }
    
    public void removeRecommendationWeights(pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights recommendationWeights) {
        getRelationLdoDUserHasRecommendationWeights().remove((pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this, recommendationWeights);
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
    
    public pt.ist.socialsoftware.edition.ldod.domain.Player getPlayer() {
        return ((DO_State)this.get$obj$state(false)).player;
    }
    
    public void setPlayer(pt.ist.socialsoftware.edition.ldod.domain.Player player) {
        getRelationLdodUserIsPlayer().add((pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this, player);
    }
    
    private java.lang.Long get$oidPlayer() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).player;
        return (value == null) ? null : value.getOid();
    }
    
    public void addTag(pt.ist.socialsoftware.edition.ldod.domain.Tag tag) {
        getRelationTagHasContributor().add(tag, (pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this);
    }
    
    public void removeTag(pt.ist.socialsoftware.edition.ldod.domain.Tag tag) {
        getRelationTagHasContributor().remove(tag, (pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this);
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
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoD getLdoD() {
        return ((DO_State)this.get$obj$state(false)).ldoD;
    }
    
    public void setLdoD(pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD) {
        getRelationLdoDHasLdoDUsers().add((pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this, ldoD);
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
    
    public void addRoles(pt.ist.socialsoftware.edition.ldod.domain.Role roles) {
        getRelationLdoDUsersAndRoles().add((pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this, roles);
    }
    
    public void removeRoles(pt.ist.socialsoftware.edition.ldod.domain.Role roles) {
        getRelationLdoDUsersAndRoles().remove((pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this, roles);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Role> getRolesSet() {
        return get$rl$roles();
    }
    
    public void set$roles(OJBFunctionalSetWrapper roles) {
        get$rl$roles().setFromOJB(this, "roles", roles);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Role> getRoles() {
        return getRolesSet();
    }
    
    @Deprecated
    public int getRolesCount() {
        return getRolesSet().size();
    }
    
    public void addSelectedVirtualEditions(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition selectedVirtualEditions) {
        getRelationLdoDUserSelectsVirtualEditions().add(selectedVirtualEditions, (pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this);
    }
    
    public void removeSelectedVirtualEditions(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition selectedVirtualEditions) {
        getRelationLdoDUserSelectsVirtualEditions().remove(selectedVirtualEditions, (pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getSelectedVirtualEditionsSet() {
        return get$rl$selectedVirtualEditions();
    }
    
    public void set$selectedVirtualEditions(OJBFunctionalSetWrapper selectedVirtualEditions) {
        get$rl$selectedVirtualEditions().setFromOJB(this, "selectedVirtualEditions", selectedVirtualEditions);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getSelectedVirtualEditions() {
        return getSelectedVirtualEditionsSet();
    }
    
    @Deprecated
    public int getSelectedVirtualEditionsCount() {
        return getSelectedVirtualEditionsSet().size();
    }
    
    public void addResponsibleForGames(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame responsibleForGames) {
        getRelationLdoDUserOwnsClassificationGame().add(responsibleForGames, (pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this);
    }
    
    public void removeResponsibleForGames(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame responsibleForGames) {
        getRelationLdoDUserOwnsClassificationGame().remove(responsibleForGames, (pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getResponsibleForGamesSet() {
        return get$rl$responsibleForGames();
    }
    
    public void set$responsibleForGames(OJBFunctionalSetWrapper responsibleForGames) {
        get$rl$responsibleForGames().setFromOJB(this, "responsibleForGames", responsibleForGames);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getResponsibleForGames() {
        return getResponsibleForGamesSet();
    }
    
    @Deprecated
    public int getResponsibleForGamesCount() {
        return getResponsibleForGamesSet().size();
    }
    
    public void addMember(pt.ist.socialsoftware.edition.ldod.domain.Member member) {
        getRelationMemberHasLdoDUser().add((pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this, member);
    }
    
    public void removeMember(pt.ist.socialsoftware.edition.ldod.domain.Member member) {
        getRelationMemberHasLdoDUser().remove((pt.ist.socialsoftware.edition.ldod.domain.LdoDUser)this, member);
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
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.token != null) handleAttemptToDeleteConnectedObject("Token");
        if (get$rl$annotation().size() > 0) handleAttemptToDeleteConnectedObject("Annotation");
        if (get$rl$recommendationWeights().size() > 0) handleAttemptToDeleteConnectedObject("RecommendationWeights");
        if (castedState.player != null) handleAttemptToDeleteConnectedObject("Player");
        if (get$rl$tag().size() > 0) handleAttemptToDeleteConnectedObject("Tag");
        if (castedState.ldoD != null) handleAttemptToDeleteConnectedObject("LdoD");
        if (get$rl$roles().size() > 0) handleAttemptToDeleteConnectedObject("Roles");
        if (get$rl$selectedVirtualEditions().size() > 0) handleAttemptToDeleteConnectedObject("SelectedVirtualEditions");
        if (get$rl$responsibleForGames().size() > 0) handleAttemptToDeleteConnectedObject("ResponsibleForGames");
        if (get$rl$member().size() > 0) handleAttemptToDeleteConnectedObject("Member");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$enabled(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readboolean(rs, "ENABLED"), state);
        set$active(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readboolean(rs, "ACTIVE"), state);
        set$username(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "USERNAME"), state);
        set$password(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "PASSWORD"), state);
        set$firstName(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "FIRST_NAME"), state);
        set$lastName(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "LAST_NAME"), state);
        set$email(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "EMAIL"), state);
        set$lastLogin(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readLocalDate(rs, "LAST_LOGIN"), state);
        set$socialMediaService(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(LdoDUser.SocialMediaService.class, rs, "SOCIAL_MEDIA_SERVICE"), state);
        set$socialMediaId(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "SOCIAL_MEDIA_ID"), state);
        castedState.token = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_TOKEN");
        castedState.player = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_PLAYER");
        castedState.ldoD = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_LDO_D");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("annotation")) return getRelationLdoDUserWritesAnnotations();
        if (attrName.equals("recommendationWeights")) return getRelationLdoDUserHasRecommendationWeights();
        if (attrName.equals("tag")) return getRelationTagHasContributor().getInverseRelation();
        if (attrName.equals("roles")) return getRelationLdoDUsersAndRoles();
        if (attrName.equals("selectedVirtualEditions")) return getRelationLdoDUserSelectsVirtualEditions().getInverseRelation();
        if (attrName.equals("responsibleForGames")) return getRelationLdoDUserOwnsClassificationGame().getInverseRelation();
        if (attrName.equals("member")) return getRelationMemberHasLdoDUser();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("annotation", getRelationLdoDUserWritesAnnotations());
        get$$relationList("recommendationWeights", getRelationLdoDUserHasRecommendationWeights());
        get$$relationList("tag", getRelationTagHasContributor().getInverseRelation());
        get$$relationList("roles", getRelationLdoDUsersAndRoles());
        get$$relationList("selectedVirtualEditions", getRelationLdoDUserSelectsVirtualEditions().getInverseRelation());
        get$$relationList("responsibleForGames", getRelationLdoDUserOwnsClassificationGame().getInverseRelation());
        get$$relationList("member", getRelationMemberHasLdoDUser());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private boolean enabled;
        private boolean active;
        private java.lang.String username;
        private java.lang.String password;
        private java.lang.String firstName;
        private java.lang.String lastName;
        private java.lang.String email;
        private org.joda.time.LocalDate lastLogin;
        private LdoDUser.SocialMediaService socialMediaService;
        private java.lang.String socialMediaId;
        private pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken token;
        private pt.ist.socialsoftware.edition.ldod.domain.Player player;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.enabled = this.enabled;
            newCasted.active = this.active;
            newCasted.username = this.username;
            newCasted.password = this.password;
            newCasted.firstName = this.firstName;
            newCasted.lastName = this.lastName;
            newCasted.email = this.email;
            newCasted.lastLogin = this.lastLogin;
            newCasted.socialMediaService = this.socialMediaService;
            newCasted.socialMediaId = this.socialMediaId;
            newCasted.token = this.token;
            newCasted.player = this.player;
            newCasted.ldoD = this.ldoD;
            
        }
        
    }
    
}
