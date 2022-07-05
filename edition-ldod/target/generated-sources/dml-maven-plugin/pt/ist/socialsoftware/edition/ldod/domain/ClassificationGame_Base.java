package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class ClassificationGame_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter> role$$virtualEditionInter = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter getValue(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame o1) {
            return ((ClassificationGame_Base.DO_State)o1.get$obj$state(false)).virtualEditionInter;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame o1, pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter o2) {
            ((ClassificationGame_Base.DO_State)o1.get$obj$state(true)).virtualEditionInter = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter.role$$classificationGame;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.Tag> role$$tag = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.Tag>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.Tag getValue(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame o1) {
            return ((ClassificationGame_Base.DO_State)o1.get$obj$state(false)).tag;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame o1, pt.ist.socialsoftware.edition.ldod.domain.Tag o2) {
            ((ClassificationGame_Base.DO_State)o1.get$obj$state(true)).tag = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Tag.role$$classificationGame;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> role$$classificationGameParticipant = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> getSet(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame o1) {
            return ((ClassificationGame_Base)o1).get$rl$classificationGameParticipant();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant.role$$classificationGame;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition> role$$virtualEdition = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition getValue(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame o1) {
            return ((ClassificationGame_Base.DO_State)o1.get$obj$state(false)).virtualEdition;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame o1, pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition o2) {
            ((ClassificationGame_Base.DO_State)o1.get$obj$state(true)).virtualEdition = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.role$$classificationGame;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> role$$responsible = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoDUser getValue(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame o1) {
            return ((ClassificationGame_Base.DO_State)o1.get$obj$state(false)).responsible;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame o1, pt.ist.socialsoftware.edition.ldod.domain.LdoDUser o2) {
            ((ClassificationGame_Base.DO_State)o1.get$obj$state(true)).responsible = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoDUser,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.role$$responsibleForGames;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getRelationClassificationGameForVirtualEditionInter() {
        return pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter.getRelationClassificationGameForVirtualEditionInter();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Tag,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getRelationClassificationGameProducesTag() {
        return pt.ist.socialsoftware.edition.ldod.domain.Tag.getRelationClassificationGameProducesTag();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getRelationClassificationGameParticipantInGame() {
        return pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant.getRelationClassificationGameParticipantInGame();
    }
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame> getRelationVirtualEditionHasClassificationGames() {
        return pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition.getRelationVirtualEditionHasClassificationGames();
    }
    
    private final static class LdoDUserOwnsClassificationGame {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser>(role$$responsible, "LdoDUserOwnsClassificationGame");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.LdoDUser> getRelationLdoDUserOwnsClassificationGame() {
        return LdoDUserOwnsClassificationGame.relation;
    }
    
    static {
        LdoDUserOwnsClassificationGame.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame.LdoDUserOwnsClassificationGame");
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame,pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> get$rl$classificationGameParticipant() {
        return get$$relationList("classificationGameParticipant", getRelationClassificationGameParticipantInGame().getInverseRelation());
        
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
    protected  ClassificationGame_Base() {
        super();
    }
    
    // Getters and Setters
    
    public ClassificationGame.ClassificationGameState getState() {
        return ((DO_State)this.get$obj$state(false)).state;
    }
    
    public void setState(ClassificationGame.ClassificationGameState state) {
        ((DO_State)this.get$obj$state(true)).state = state;
    }
    
    private java.lang.String get$state() {
        ClassificationGame.ClassificationGameState value = ((DO_State)this.get$obj$state(false)).state;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForEnum(value);
    }
    
    private final void set$state(ClassificationGame.ClassificationGameState value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).state = (ClassificationGame.ClassificationGameState)((value == null) ? null : value);
    }
    
    public java.lang.String getDescription() {
        return ((DO_State)this.get$obj$state(false)).description;
    }
    
    public void setDescription(java.lang.String description) {
        ((DO_State)this.get$obj$state(true)).description = description;
    }
    
    private java.lang.String get$description() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).description;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$description(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).description = (java.lang.String)((value == null) ? null : value);
    }
    
    public org.joda.time.DateTime getDateTime() {
        return ((DO_State)this.get$obj$state(false)).dateTime;
    }
    
    public void setDateTime(org.joda.time.DateTime dateTime) {
        ((DO_State)this.get$obj$state(true)).dateTime = dateTime;
    }
    
    private java.sql.Timestamp get$dateTime() {
        org.joda.time.DateTime value = ((DO_State)this.get$obj$state(false)).dateTime;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForDateTime(value);
    }
    
    private final void set$dateTime(org.joda.time.DateTime value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).dateTime = (org.joda.time.DateTime)((value == null) ? null : value);
    }
    
    public boolean getSync() {
        return ((DO_State)this.get$obj$state(false)).sync;
    }
    
    public void setSync(boolean sync) {
        ((DO_State)this.get$obj$state(true)).sync = sync;
    }
    
    private boolean get$sync() {
        boolean value = ((DO_State)this.get$obj$state(false)).sync;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForboolean(value);
    }
    
    private final void set$sync(boolean value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).sync = (boolean)(value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter getVirtualEditionInter() {
        return ((DO_State)this.get$obj$state(false)).virtualEditionInter;
    }
    
    public void setVirtualEditionInter(pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter virtualEditionInter) {
        getRelationClassificationGameForVirtualEditionInter().add(virtualEditionInter, (pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame)this);
    }
    
    private java.lang.Long get$oidVirtualEditionInter() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).virtualEditionInter;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfVirtualEditionInter() {
        if (getVirtualEditionInter() == null) return false;
        return true;
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.Tag getTag() {
        return ((DO_State)this.get$obj$state(false)).tag;
    }
    
    public void setTag(pt.ist.socialsoftware.edition.ldod.domain.Tag tag) {
        getRelationClassificationGameProducesTag().add(tag, (pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame)this);
    }
    
    private java.lang.Long get$oidTag() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).tag;
        return (value == null) ? null : value.getOid();
    }
    
    public void addClassificationGameParticipant(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant classificationGameParticipant) {
        getRelationClassificationGameParticipantInGame().add(classificationGameParticipant, (pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame)this);
    }
    
    public void removeClassificationGameParticipant(pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant classificationGameParticipant) {
        getRelationClassificationGameParticipantInGame().remove(classificationGameParticipant, (pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame)this);
    }
    
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> getClassificationGameParticipantSet() {
        return get$rl$classificationGameParticipant();
    }
    
    public void set$classificationGameParticipant(OJBFunctionalSetWrapper classificationGameParticipant) {
        get$rl$classificationGameParticipant().setFromOJB(this, "classificationGameParticipant", classificationGameParticipant);
    }
    
    @Deprecated
    public java.util.Set<pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant> getClassificationGameParticipant() {
        return getClassificationGameParticipantSet();
    }
    
    @Deprecated
    public int getClassificationGameParticipantCount() {
        return getClassificationGameParticipantSet().size();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition getVirtualEdition() {
        return ((DO_State)this.get$obj$state(false)).virtualEdition;
    }
    
    public void setVirtualEdition(pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition virtualEdition) {
        getRelationVirtualEditionHasClassificationGames().add(virtualEdition, (pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame)this);
    }
    
    private java.lang.Long get$oidVirtualEdition() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).virtualEdition;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfVirtualEdition() {
        if (getVirtualEdition() == null) return false;
        return true;
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoDUser getResponsible() {
        return ((DO_State)this.get$obj$state(false)).responsible;
    }
    
    public void setResponsible(pt.ist.socialsoftware.edition.ldod.domain.LdoDUser responsible) {
        getRelationLdoDUserOwnsClassificationGame().add((pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame)this, responsible);
    }
    
    private java.lang.Long get$oidResponsible() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).responsible;
        return (value == null) ? null : value.getOid();
    }
    
    @pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate
    public final boolean checkMultiplicityOfResponsible() {
        if (getResponsible() == null) return false;
        return true;
    }
    
    
    protected void checkDisconnected() {
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (castedState.virtualEditionInter != null) handleAttemptToDeleteConnectedObject("VirtualEditionInter");
        if (castedState.tag != null) handleAttemptToDeleteConnectedObject("Tag");
        if (get$rl$classificationGameParticipant().size() > 0) handleAttemptToDeleteConnectedObject("ClassificationGameParticipant");
        if (castedState.virtualEdition != null) handleAttemptToDeleteConnectedObject("VirtualEdition");
        if (castedState.responsible != null) handleAttemptToDeleteConnectedObject("Responsible");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$state(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readEnum(ClassificationGame.ClassificationGameState.class, rs, "STATE"), state);
        set$description(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "DESCRIPTION"), state);
        set$dateTime(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDateTime(rs, "DATE_TIME"), state);
        set$sync(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readboolean(rs, "SYNC"), state);
        castedState.virtualEditionInter = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_VIRTUAL_EDITION_INTER");
        castedState.tag = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_TAG");
        castedState.virtualEdition = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_VIRTUAL_EDITION");
        castedState.responsible = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_RESPONSIBLE");
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("classificationGameParticipant")) return getRelationClassificationGameParticipantInGame().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("classificationGameParticipant", getRelationClassificationGameParticipantInGame().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State {
        private ClassificationGame.ClassificationGameState state;
        private java.lang.String description;
        private org.joda.time.DateTime dateTime;
        private boolean sync;
        private pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter virtualEditionInter;
        private pt.ist.socialsoftware.edition.ldod.domain.Tag tag;
        private pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition virtualEdition;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoDUser responsible;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.state = this.state;
            newCasted.description = this.description;
            newCasted.dateTime = this.dateTime;
            newCasted.sync = this.sync;
            newCasted.virtualEditionInter = this.virtualEditionInter;
            newCasted.tag = this.tag;
            newCasted.virtualEdition = this.virtualEdition;
            newCasted.responsible = this.responsible;
            
        }
        
    }
    
}
