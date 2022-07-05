package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class LastTwitterID_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID,pt.ist.socialsoftware.edition.ldod.domain.LdoD> role$$ldoD = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID,pt.ist.socialsoftware.edition.ldod.domain.LdoD>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoD getValue(pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID o1) {
            return ((LastTwitterID_Base.DO_State)o1.get$obj$state(false)).ldoD;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID o1, pt.ist.socialsoftware.edition.ldod.domain.LdoD o2) {
            ((LastTwitterID_Base.DO_State)o1.get$obj$state(true)).ldoD = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoD.role$$lastTwitterID;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID> getRelationLdoDHasLastTwitterID() {
        return pt.ist.socialsoftware.edition.ldod.domain.LdoD.getRelationLdoDHasLastTwitterID();
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
    protected  LastTwitterID_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getLastBookParsedFile() {
        return ((DO_State)this.get$obj$state(false)).lastBookParsedFile;
    }
    
    public void setLastBookParsedFile(java.lang.String lastBookParsedFile) {
        ((DO_State)this.get$obj$state(true)).lastBookParsedFile = lastBookParsedFile;
    }
    
    private java.lang.String get$lastBookParsedFile() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).lastBookParsedFile;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$lastBookParsedFile(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).lastBookParsedFile = (java.lang.String)((value == null) ? null : value);
    }
    
    public long getBookLastTwitterID() {
        return ((DO_State)this.get$obj$state(false)).bookLastTwitterID;
    }
    
    public void setBookLastTwitterID(long bookLastTwitterID) {
        ((DO_State)this.get$obj$state(true)).bookLastTwitterID = bookLastTwitterID;
    }
    
    private long get$bookLastTwitterID() {
        long value = ((DO_State)this.get$obj$state(false)).bookLastTwitterID;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForlong(value);
    }
    
    private final void set$bookLastTwitterID(long value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).bookLastTwitterID = (long)(value);
    }
    
    public java.lang.String getLastPessoaParsedFile() {
        return ((DO_State)this.get$obj$state(false)).lastPessoaParsedFile;
    }
    
    public void setLastPessoaParsedFile(java.lang.String lastPessoaParsedFile) {
        ((DO_State)this.get$obj$state(true)).lastPessoaParsedFile = lastPessoaParsedFile;
    }
    
    private java.lang.String get$lastPessoaParsedFile() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).lastPessoaParsedFile;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$lastPessoaParsedFile(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).lastPessoaParsedFile = (java.lang.String)((value == null) ? null : value);
    }
    
    public long getPessoaLastTwitterID() {
        return ((DO_State)this.get$obj$state(false)).pessoaLastTwitterID;
    }
    
    public void setPessoaLastTwitterID(long pessoaLastTwitterID) {
        ((DO_State)this.get$obj$state(true)).pessoaLastTwitterID = pessoaLastTwitterID;
    }
    
    private long get$pessoaLastTwitterID() {
        long value = ((DO_State)this.get$obj$state(false)).pessoaLastTwitterID;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForlong(value);
    }
    
    private final void set$pessoaLastTwitterID(long value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).pessoaLastTwitterID = (long)(value);
    }
    
    public java.lang.String getLastBernardoParsedFile() {
        return ((DO_State)this.get$obj$state(false)).lastBernardoParsedFile;
    }
    
    public void setLastBernardoParsedFile(java.lang.String lastBernardoParsedFile) {
        ((DO_State)this.get$obj$state(true)).lastBernardoParsedFile = lastBernardoParsedFile;
    }
    
    private java.lang.String get$lastBernardoParsedFile() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).lastBernardoParsedFile;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$lastBernardoParsedFile(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).lastBernardoParsedFile = (java.lang.String)((value == null) ? null : value);
    }
    
    public long getBernardoLastTwitterID() {
        return ((DO_State)this.get$obj$state(false)).bernardoLastTwitterID;
    }
    
    public void setBernardoLastTwitterID(long bernardoLastTwitterID) {
        ((DO_State)this.get$obj$state(true)).bernardoLastTwitterID = bernardoLastTwitterID;
    }
    
    private long get$bernardoLastTwitterID() {
        long value = ((DO_State)this.get$obj$state(false)).bernardoLastTwitterID;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForlong(value);
    }
    
    private final void set$bernardoLastTwitterID(long value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).bernardoLastTwitterID = (long)(value);
    }
    
    public java.lang.String getLastVicenteParsedFile() {
        return ((DO_State)this.get$obj$state(false)).lastVicenteParsedFile;
    }
    
    public void setLastVicenteParsedFile(java.lang.String lastVicenteParsedFile) {
        ((DO_State)this.get$obj$state(true)).lastVicenteParsedFile = lastVicenteParsedFile;
    }
    
    private java.lang.String get$lastVicenteParsedFile() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).lastVicenteParsedFile;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$lastVicenteParsedFile(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).lastVicenteParsedFile = (java.lang.String)((value == null) ? null : value);
    }
    
    public long getVicenteLastTwitterID() {
        return ((DO_State)this.get$obj$state(false)).vicenteLastTwitterID;
    }
    
    public void setVicenteLastTwitterID(long vicenteLastTwitterID) {
        ((DO_State)this.get$obj$state(true)).vicenteLastTwitterID = vicenteLastTwitterID;
    }
    
    private long get$vicenteLastTwitterID() {
        long value = ((DO_State)this.get$obj$state(false)).vicenteLastTwitterID;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForlong(value);
    }
    
    private final void set$vicenteLastTwitterID(long value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).vicenteLastTwitterID = (long)(value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoD getLdoD() {
        return ((DO_State)this.get$obj$state(false)).ldoD;
    }
    
    public void setLdoD(pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD) {
        getRelationLdoDHasLastTwitterID().add(ldoD, (pt.ist.socialsoftware.edition.ldod.domain.LastTwitterID)this);
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
        if (castedState.ldoD != null) handleAttemptToDeleteConnectedObject("LdoD");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$lastBookParsedFile(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "LAST_BOOK_PARSED_FILE"), state);
        set$bookLastTwitterID(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readlong(rs, "BOOK_LAST_TWITTER_I_D"), state);
        set$lastPessoaParsedFile(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "LAST_PESSOA_PARSED_FILE"), state);
        set$pessoaLastTwitterID(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readlong(rs, "PESSOA_LAST_TWITTER_I_D"), state);
        set$lastBernardoParsedFile(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "LAST_BERNARDO_PARSED_FILE"), state);
        set$bernardoLastTwitterID(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readlong(rs, "BERNARDO_LAST_TWITTER_I_D"), state);
        set$lastVicenteParsedFile(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "LAST_VICENTE_PARSED_FILE"), state);
        set$vicenteLastTwitterID(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readlong(rs, "VICENTE_LAST_TWITTER_I_D"), state);
        castedState.ldoD = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_LDO_D");
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
        private java.lang.String lastBookParsedFile;
        private long bookLastTwitterID;
        private java.lang.String lastPessoaParsedFile;
        private long pessoaLastTwitterID;
        private java.lang.String lastBernardoParsedFile;
        private long bernardoLastTwitterID;
        private java.lang.String lastVicenteParsedFile;
        private long vicenteLastTwitterID;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.lastBookParsedFile = this.lastBookParsedFile;
            newCasted.bookLastTwitterID = this.bookLastTwitterID;
            newCasted.lastPessoaParsedFile = this.lastPessoaParsedFile;
            newCasted.pessoaLastTwitterID = this.pessoaLastTwitterID;
            newCasted.lastBernardoParsedFile = this.lastBernardoParsedFile;
            newCasted.bernardoLastTwitterID = this.bernardoLastTwitterID;
            newCasted.lastVicenteParsedFile = this.lastVicenteParsedFile;
            newCasted.vicenteLastTwitterID = this.vicenteLastTwitterID;
            newCasted.ldoD = this.ldoD;
            
        }
        
    }
    
}
