package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class LdoD_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Role> role$$roles = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Role>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Role> getSet(pt.ist.socialsoftware.edition.ldod.domain.LdoD o1) {
            return ((LdoD_Base)o1).get$rl$roles();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Role,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Role.role$$ldoD;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition> role$$expertEditions = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition> getSet(pt.ist.socialsoftware.edition.ldod.domain.LdoD o1) {
            return ((LdoD_Base)o1).get$rl$expertEditions();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition.role$$ldoD4Expert;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken> role$$token = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken> getSet(pt.ist.socialsoftware.edition.ldod.domain.LdoD o1) {
            return ((LdoD_Base)o1).get$rl$token();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken.role$$ldoD;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.NullEdition> role$$nullEdition = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.NullEdition>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.NullEdition getValue(pt.ist.socialsoftware.edition.ldod.domain.LdoD o1) {
            return ((LdoD_Base.DO_State)o1.get$obj$state(false)).nullEdition;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.LdoD o1, pt.ist.socialsoftware.edition.ldod.domain.NullEdition o2) {
            ((LdoD_Base.DO_State)o1.get$obj$state(true)).nullEdition = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.NullEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.NullEdition.role$$ldoD4NullEdition;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> role$$virtualEditions = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getSet(pt.ist.socialsoftware.edition.ldod.domain.LdoD o1) {
            return ((LdoD_Base)o1).get$rl$virtualEditions();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.role$$ldoD4Virtual;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Fragment> role$$fragments = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Fragment>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Fragment> getSet(pt.ist.socialsoftware.edition.ldod.domain.LdoD o1) {
            return ((LdoD_Base)o1).get$rl$fragments();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Fragment,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Fragment.role$$ldoD;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Tweet> role$$tweet = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Tweet>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Tweet> getSet(pt.ist.socialsoftware.edition.ldod.domain.LdoD o1) {
            return ((LdoD_Base)o1).get$rl$tweet();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Tweet,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Tweet.role$$ldoD;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Heteronym> role$$heteronyms = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Heteronym>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Heteronym> getSet(pt.ist.socialsoftware.edition.ldod.domain.LdoD o1) {
            return ((LdoD_Base)o1).get$rl$heteronyms();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Heteronym,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Heteronym.role$$ldoD;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> role$$users = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getSet(pt.ist.socialsoftware.edition.ldod.domain.LdoD o1) {
            return ((LdoD_Base)o1).get$rl$users();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.role$$ldoD;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.fenixframework.DomainRoot> role$$root = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.fenixframework.DomainRoot>() {
        @Override
        public pt.ist.fenixframework.DomainRoot getValue(pt.ist.socialsoftware.edition.ldod.domain.LdoD o1) {
            return ((LdoD_Base.DO_State)o1.get$obj$state(false)).root;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.LdoD o1, pt.ist.fenixframework.DomainRoot o2) {
            ((LdoD_Base.DO_State)o1.get$obj$state(true)).root = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.fenixframework.DomainRoot,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getInverseRole() {
            return pt.ist.fenixframework.DomainRoot.role$$ldoD;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID> role$$lastTwitterID = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID getValue(pt.ist.socialsoftware.edition.ldod.domain.LdoD o1) {
            return ((LdoD_Base.DO_State)o1.get$obj$state(false)).lastTwitterID;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.LdoD o1, pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID o2) {
            ((LdoD_Base.DO_State)o1.get$obj$state(true)).lastTwitterID = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID.role$$ldoD;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.UserConnection> role$$userConnection = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.UserConnection>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.UserConnection> getSet(pt.ist.socialsoftware.edition.ldod.domain.LdoD o1) {
            return ((LdoD_Base)o1).get$rl$userConnection();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.UserConnection,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.UserConnection.role$$ldoD;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Role,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getRelationLdoDHasRoles() {
        return pt.ist.socialsoftware.edition.ldod.domain.Role.getRelationLdoDHasRoles();
    }
    
    private final static class LdoDHasExpertEditions {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition>(role$$expertEditions, "LdoDHasExpertEditions");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition> getRelationLdoDHasExpertEditions() {
        return LdoDHasExpertEditions.relation;
    }
    
    static {
        LdoDHasExpertEditions.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.LdoD.LdoDHasExpertEditions");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getRelationLdoDHasRegistrationToken() {
        return pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken.getRelationLdoDHasRegistrationToken();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.NullEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getRelationLdoDHasNullEdition() {
        return pt.ist.socialsoftware.edition.ldod.domain.NullEdition.getRelationLdoDHasNullEdition();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getRelationLdoDHasVirtualEditions() {
        return pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.getRelationLdoDHasVirtualEditions();
    }
    
    private final static class LdoDHasFragments {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Fragment> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Fragment>(role$$fragments, "LdoDHasFragments");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Fragment> getRelationLdoDHasFragments() {
        return LdoDHasFragments.relation;
    }
    
    static {
        LdoDHasFragments.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.LdoD.LdoDHasFragments");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Tweet,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getRelationLdoDHasTweets() {
        return pt.ist.socialsoftware.edition.ldod.domain.Tweet.getRelationLdoDHasTweets();
    }
    
    private final static class LdoDHasHeteronyms {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Heteronym> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Heteronym>(role$$heteronyms, "LdoDHasHeteronyms");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Heteronym> getRelationLdoDHasHeteronyms() {
        return LdoDHasHeteronyms.relation;
    }
    
    static {
        LdoDHasHeteronyms.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.LdoD.LdoDHasHeteronyms");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getRelationLdoDHasLdoDUsers() {
        return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.getRelationLdoDHasLdoDUsers();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.fenixframework.DomainRoot,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getRelationDomainRootHasLdoDApplication() {
        return pt.ist.fenixframework.DomainRoot.getRelationDomainRootHasLdoDApplication();
    }
    
    private final static class LdoDHasLastTwitterID {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID>(role$$lastTwitterID, "LdoDHasLastTwitterID");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID> getRelationLdoDHasLastTwitterID() {
        return LdoDHasLastTwitterID.relation;
    }
    
    static {
        LdoDHasLastTwitterID.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.LdoD.LdoDHasLastTwitterID");
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.UserConnection,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getRelationLdoDHasUserConnections() {
        return pt.ist.socialsoftware.edition.ldod.domain.UserConnection.getRelationLdoDHasUserConnections();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Role> get$rl$roles() {
        return get$$relationList("roles", getRelationLdoDHasRoles().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition> get$rl$expertEditions() {
        return get$$relationList("expertEditions", getRelationLdoDHasExpertEditions());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken> get$rl$token() {
        return get$$relationList("token", getRelationLdoDHasRegistrationToken().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> get$rl$virtualEditions() {
        return get$$relationList("virtualEditions", getRelationLdoDHasVirtualEditions().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Fragment> get$rl$fragments() {
        return get$$relationList("fragments", getRelationLdoDHasFragments());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Tweet> get$rl$tweet() {
        return get$$relationList("tweet", getRelationLdoDHasTweets().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Heteronym> get$rl$heteronyms() {
        return get$$relationList("heteronyms", getRelationLdoDHasHeteronyms());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> get$rl$users() {
        return get$$relationList("users", getRelationLdoDHasLdoDUsers().getInverseRelation());
        
    }
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.UserConnection> get$rl$userConnection() {
        return get$$relationList("userConnection", getRelationLdoDHasUserConnections().getInverseRelation());
        
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
    protected  LdoD_Base() {
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
    
    public java.lang.String getAuthor() {
        return ((DO_State)this.get$obj$state(false)).author;
    }
    
    public void setAuthor(java.lang.String author) {
        ((DO_State)this.get$obj$state(true)).author = author;
    }
    
    private java.lang.String get$author() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).author;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$author(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).author = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getEditor() {
        return ((DO_State)this.get$obj$state(false)).editor;
    }
    
    public void setEditor(java.lang.String editor) {
        ((DO_State)this.get$obj$state(true)).editor = editor;
    }
    
    private java.lang.String get$editor() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).editor;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$editor(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).editor = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getSponsor() {
        return ((DO_State)this.get$obj$state(false)).sponsor;
    }
    
    public void setSponsor(java.lang.String sponsor) {
        ((DO_State)this.get$obj$state(true)).sponsor = sponsor;
    }
    
    private java.lang.String get$sponsor() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).sponsor;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$sponsor(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).sponsor = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getPrincipal() {
        return ((DO_State)this.get$obj$state(false)).principal;
    }
    
    public void setPrincipal(java.lang.String principal) {
        ((DO_State)this.get$obj$state(true)).principal = principal;
    }
    
    private java.lang.String get$principal() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).principal;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$principal(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).principal = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getFunder() {
        return ((DO_State)this.get$obj$state(false)).funder;
    }
    
    public void setFunder(java.lang.String funder) {
        ((DO_State)this.get$obj$state(true)).funder = funder;
    }
    
    private java.lang.String get$funder() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).funder;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$funder(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).funder = (java.lang.String)((value == null) ? null : value);
    }
    
    public boolean getAdmin() {
        return ((DO_State)this.get$obj$state(false)).admin;
    }
    
    public void setAdmin(boolean admin) {
        ((DO_State)this.get$obj$state(true)).admin = admin;
    }
    
    private boolean get$admin() {
        boolean value = ((DO_State)this.get$obj$state(false)).admin;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForboolean(value);
    }
    
    private final void set$admin(boolean value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).admin = (boolean)(value);
    }
    
    // Role Methods
    
    public void addRoles(pt.ist.socialsoftware.edition.ldod.domain.Role roles) {
        getRelationLdoDHasRoles().add(roles, (pt.ist.socialsoftware.edition.ldod.domain.LdoD)this);
    }
    
    public void removeRoles(pt.ist.socialsoftware.edition.ldod.domain.Role roles) {
        getRelationLdoDHasRoles().remove(roles, (pt.ist.socialsoftware.edition.ldod.domain.LdoD)this);
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
    
    public void addExpertEditions(pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition expertEditions) {
        getRelationLdoDHasExpertEditions().add((pt.ist.socialsoftware.edition.ldod.domain.LdoD)this, expertEditions);
    }
    
    public void removeExpertEditions(pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition expertEditions) {
        getRelationLdoDHasExpertEditions().remove((pt.ist.socialsoftware.edition.ldod.domain.LdoD)this, expertEditions);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition> getExpertEditionsSet() {
        return get$rl$expertEditions();
    }
    
    public void set$expertEditions(OJBFunctionalSetWrapper expertEditions) {
        get$rl$expertEditions().setFromOJB(this, "expertEditions", expertEditions);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition> getExpertEditions() {
        return getExpertEditionsSet();
    }
    
    @Deprecated
    public int getExpertEditionsCount() {
        return getExpertEditionsSet().size();
    }
    
    public void addToken(pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken token) {
        getRelationLdoDHasRegistrationToken().add(token, (pt.ist.socialsoftware.edition.ldod.domain.LdoD)this);
    }
    
    public void removeToken(pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken token) {
        getRelationLdoDHasRegistrationToken().remove(token, (pt.ist.socialsoftware.edition.ldod.domain.LdoD)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken> getTokenSet() {
        return get$rl$token();
    }
    
    public void set$token(OJBFunctionalSetWrapper token) {
        get$rl$token().setFromOJB(this, "token", token);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken> getToken() {
        return getTokenSet();
    }
    
    @Deprecated
    public int getTokenCount() {
        return getTokenSet().size();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.NullEdition getNullEdition() {
        return ((DO_State)this.get$obj$state(false)).nullEdition;
    }
    
    public void setNullEdition(pt.ist.socialsoftware.edition.ldod.domain.NullEdition nullEdition) {
        getRelationLdoDHasNullEdition().add(nullEdition, (pt.ist.socialsoftware.edition.ldod.domain.LdoD)this);
    }
    
    private java.lang.Long get$oidNullEdition() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).nullEdition;
        return (value == null) ? null : value.getOid();
    }
    
    public void addVirtualEditions(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition virtualEditions) {
        getRelationLdoDHasVirtualEditions().add(virtualEditions, (pt.ist.socialsoftware.edition.ldod.domain.LdoD)this);
    }
    
    public void removeVirtualEditions(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition virtualEditions) {
        getRelationLdoDHasVirtualEditions().remove(virtualEditions, (pt.ist.socialsoftware.edition.ldod.domain.LdoD)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getVirtualEditionsSet() {
        return get$rl$virtualEditions();
    }
    
    public void set$virtualEditions(OJBFunctionalSetWrapper virtualEditions) {
        get$rl$virtualEditions().setFromOJB(this, "virtualEditions", virtualEditions);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> getVirtualEditions() {
        return getVirtualEditionsSet();
    }
    
    @Deprecated
    public int getVirtualEditionsCount() {
        return getVirtualEditionsSet().size();
    }
    
    public void addFragments(pt.ist.socialsoftware.edition.ldod.domain.Fragment fragments) {
        getRelationLdoDHasFragments().add((pt.ist.socialsoftware.edition.ldod.domain.LdoD)this, fragments);
    }
    
    public void removeFragments(pt.ist.socialsoftware.edition.ldod.domain.Fragment fragments) {
        getRelationLdoDHasFragments().remove((pt.ist.socialsoftware.edition.ldod.domain.LdoD)this, fragments);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Fragment> getFragmentsSet() {
        return get$rl$fragments();
    }
    
    public void set$fragments(OJBFunctionalSetWrapper fragments) {
        get$rl$fragments().setFromOJB(this, "fragments", fragments);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Fragment> getFragments() {
        return getFragmentsSet();
    }
    
    @Deprecated
    public int getFragmentsCount() {
        return getFragmentsSet().size();
    }
    
    public void addTweet(pt.ist.socialsoftware.edition.ldod.domain.Tweet tweet) {
        getRelationLdoDHasTweets().add(tweet, (pt.ist.socialsoftware.edition.ldod.domain.LdoD)this);
    }
    
    public void removeTweet(pt.ist.socialsoftware.edition.ldod.domain.Tweet tweet) {
        getRelationLdoDHasTweets().remove(tweet, (pt.ist.socialsoftware.edition.ldod.domain.LdoD)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Tweet> getTweetSet() {
        return get$rl$tweet();
    }
    
    public void set$tweet(OJBFunctionalSetWrapper tweet) {
        get$rl$tweet().setFromOJB(this, "tweet", tweet);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Tweet> getTweet() {
        return getTweetSet();
    }
    
    @Deprecated
    public int getTweetCount() {
        return getTweetSet().size();
    }
    
    public void addHeteronyms(pt.ist.socialsoftware.edition.ldod.domain.Heteronym heteronyms) {
        getRelationLdoDHasHeteronyms().add((pt.ist.socialsoftware.edition.ldod.domain.LdoD)this, heteronyms);
    }
    
    public void removeHeteronyms(pt.ist.socialsoftware.edition.ldod.domain.Heteronym heteronyms) {
        getRelationLdoDHasHeteronyms().remove((pt.ist.socialsoftware.edition.ldod.domain.LdoD)this, heteronyms);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Heteronym> getHeteronymsSet() {
        return get$rl$heteronyms();
    }
    
    public void set$heteronyms(OJBFunctionalSetWrapper heteronyms) {
        get$rl$heteronyms().setFromOJB(this, "heteronyms", heteronyms);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.Heteronym> getHeteronyms() {
        return getHeteronymsSet();
    }
    
    @Deprecated
    public int getHeteronymsCount() {
        return getHeteronymsSet().size();
    }
    
    public void addUsers(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser users) {
        getRelationLdoDHasLdoDUsers().add(users, (pt.ist.socialsoftware.edition.ldod.domain.LdoD)this);
    }
    
    public void removeUsers(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser users) {
        getRelationLdoDHasLdoDUsers().remove(users, (pt.ist.socialsoftware.edition.ldod.domain.LdoD)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getUsersSet() {
        return get$rl$users();
    }
    
    public void set$users(OJBFunctionalSetWrapper users) {
        get$rl$users().setFromOJB(this, "users", users);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getUsers() {
        return getUsersSet();
    }
    
    @Deprecated
    public int getUsersCount() {
        return getUsersSet().size();
    }
    
    public pt.ist.fenixframework.DomainRoot getRoot() {
        return ((DO_State)this.get$obj$state(false)).root;
    }
    
    public void setRoot(pt.ist.fenixframework.DomainRoot root) {
        getRelationDomainRootHasLdoDApplication().add(root, (pt.ist.socialsoftware.edition.ldod.domain.LdoD)this);
    }
    
    private java.lang.Long get$oidRoot() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).root;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfRoot() {
        if (getRoot() == null) return false;
        return true;
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID getLastTwitterID() {
        return ((DO_State)this.get$obj$state(false)).lastTwitterID;
    }
    
    public void setLastTwitterID(pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID lastTwitterID) {
        getRelationLdoDHasLastTwitterID().add((pt.ist.socialsoftware.edition.ldod.domain.LdoD)this, lastTwitterID);
    }
    
    private java.lang.Long get$oidLastTwitterID() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).lastTwitterID;
        return (value == null) ? null : value.getOid();
    }
    
    public void addUserConnection(pt.ist.socialsoftware.edition.ldod.domain.UserConnection userConnection) {
        getRelationLdoDHasUserConnections().add(userConnection, (pt.ist.socialsoftware.edition.ldod.domain.LdoD)this);
    }
    
    public void removeUserConnection(pt.ist.socialsoftware.edition.ldod.domain.UserConnection userConnection) {
        getRelationLdoDHasUserConnections().remove(userConnection, (pt.ist.socialsoftware.edition.ldod.domain.LdoD)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.UserConnection> getUserConnectionSet() {
        return get$rl$userConnection();
    }
    
    public void set$userConnection(OJBFunctionalSetWrapper userConnection) {
        get$rl$userConnection().setFromOJB(this, "userConnection", userConnection);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.UserConnection> getUserConnection() {
        return getUserConnectionSet();
    }
    
    @Deprecated
    public int getUserConnectionCount() {
        return getUserConnectionSet().size();
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$roles().size() > 0) handleAttemptToDeleteConnectedObject("Roles");
        if (get$rl$expertEditions().size() > 0) handleAttemptToDeleteConnectedObject("ExpertEditions");
        if (get$rl$token().size() > 0) handleAttemptToDeleteConnectedObject("Token");
        if (castedState.nullEdition != null) handleAttemptToDeleteConnectedObject("NullEdition");
        if (get$rl$virtualEditions().size() > 0) handleAttemptToDeleteConnectedObject("VirtualEditions");
        if (get$rl$fragments().size() > 0) handleAttemptToDeleteConnectedObject("Fragments");
        if (get$rl$tweet().size() > 0) handleAttemptToDeleteConnectedObject("Tweet");
        if (get$rl$heteronyms().size() > 0) handleAttemptToDeleteConnectedObject("Heteronyms");
        if (get$rl$users().size() > 0) handleAttemptToDeleteConnectedObject("Users");
        if (castedState.root != null) handleAttemptToDeleteConnectedObject("Root");
        if (castedState.lastTwitterID != null) handleAttemptToDeleteConnectedObject("LastTwitterID");
        if (get$rl$userConnection().size() > 0) handleAttemptToDeleteConnectedObject("UserConnection");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$title(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "TITLE"), state);
        set$author(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "AUTHOR"), state);
        set$editor(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "EDITOR"), state);
        set$sponsor(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "SPONSOR"), state);
        set$principal(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "PRINCIPAL"), state);
        set$funder(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "FUNDER"), state);
        set$admin(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readboolean(rs, "ADMIN"), state);
        castedState.nullEdition = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_NULL_EDITION");
        castedState.root = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_ROOT");
        castedState.lastTwitterID = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_LAST_TWITTER_I_D");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("roles")) return getRelationLdoDHasRoles().getInverseRelation();
        if (attrName.equals("expertEditions")) return getRelationLdoDHasExpertEditions();
        if (attrName.equals("token")) return getRelationLdoDHasRegistrationToken().getInverseRelation();
        if (attrName.equals("virtualEditions")) return getRelationLdoDHasVirtualEditions().getInverseRelation();
        if (attrName.equals("fragments")) return getRelationLdoDHasFragments();
        if (attrName.equals("tweet")) return getRelationLdoDHasTweets().getInverseRelation();
        if (attrName.equals("heteronyms")) return getRelationLdoDHasHeteronyms();
        if (attrName.equals("users")) return getRelationLdoDHasLdoDUsers().getInverseRelation();
        if (attrName.equals("userConnection")) return getRelationLdoDHasUserConnections().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("roles", getRelationLdoDHasRoles().getInverseRelation());
        get$$relationList("expertEditions", getRelationLdoDHasExpertEditions());
        get$$relationList("token", getRelationLdoDHasRegistrationToken().getInverseRelation());
        get$$relationList("virtualEditions", getRelationLdoDHasVirtualEditions().getInverseRelation());
        get$$relationList("fragments", getRelationLdoDHasFragments());
        get$$relationList("tweet", getRelationLdoDHasTweets().getInverseRelation());
        get$$relationList("heteronyms", getRelationLdoDHasHeteronyms());
        get$$relationList("users", getRelationLdoDHasLdoDUsers().getInverseRelation());
        get$$relationList("userConnection", getRelationLdoDHasUserConnections().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private java.lang.String title;
        private java.lang.String author;
        private java.lang.String editor;
        private java.lang.String sponsor;
        private java.lang.String principal;
        private java.lang.String funder;
        private boolean admin;
        private pt.ist.socialsoftware.edition.ldod.domain.NullEdition nullEdition;
        private pt.ist.fenixframework.DomainRoot root;
        private pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID lastTwitterID;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.title = this.title;
            newCasted.author = this.author;
            newCasted.editor = this.editor;
            newCasted.sponsor = this.sponsor;
            newCasted.principal = this.principal;
            newCasted.funder = this.funder;
            newCasted.admin = this.admin;
            newCasted.nullEdition = this.nullEdition;
            newCasted.root = this.root;
            newCasted.lastTwitterID = this.lastTwitterID;
            
        }
        
    }
    
}
