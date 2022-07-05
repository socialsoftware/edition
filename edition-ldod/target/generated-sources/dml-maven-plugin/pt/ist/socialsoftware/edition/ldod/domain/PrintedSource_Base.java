package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class PrintedSource_Base extends pt.ist.socialsoftware.edition.ldod.domain.Source {
    // Static Slots
    
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
    protected  PrintedSource_Base() {
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
    
    public java.lang.String getJournal() {
        return ((DO_State)this.get$obj$state(false)).journal;
    }
    
    public void setJournal(java.lang.String journal) {
        ((DO_State)this.get$obj$state(true)).journal = journal;
    }
    
    private java.lang.String get$journal() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).journal;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$journal(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).journal = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getIssue() {
        return ((DO_State)this.get$obj$state(false)).issue;
    }
    
    public void setIssue(java.lang.String issue) {
        ((DO_State)this.get$obj$state(true)).issue = issue;
    }
    
    private java.lang.String get$issue() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).issue;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$issue(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).issue = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getPubPlace() {
        return ((DO_State)this.get$obj$state(false)).pubPlace;
    }
    
    public void setPubPlace(java.lang.String pubPlace) {
        ((DO_State)this.get$obj$state(true)).pubPlace = pubPlace;
    }
    
    private java.lang.String get$pubPlace() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).pubPlace;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$pubPlace(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).pubPlace = (java.lang.String)((value == null) ? null : value);
    }
    
    public int getStartPage() {
        return ((DO_State)this.get$obj$state(false)).startPage;
    }
    
    public void setStartPage(int startPage) {
        ((DO_State)this.get$obj$state(true)).startPage = startPage;
    }
    
    private int get$startPage() {
        int value = ((DO_State)this.get$obj$state(false)).startPage;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$startPage(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).startPage = (int)(value);
    }
    
    public int getEndPage() {
        return ((DO_State)this.get$obj$state(false)).endPage;
    }
    
    public void setEndPage(int endPage) {
        ((DO_State)this.get$obj$state(true)).endPage = endPage;
    }
    
    private int get$endPage() {
        int value = ((DO_State)this.get$obj$state(false)).endPage;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForint(value);
    }
    
    private final void set$endPage(int value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).endPage = (int)(value);
    }
    
    // Role Methods
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$title(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "TITLE"), state);
        set$journal(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "JOURNAL"), state);
        set$issue(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "ISSUE"), state);
        set$pubPlace(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "PUB_PLACE"), state);
        set$startPage(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "START_PAGE"), state);
        set$endPage(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readint(rs, "END_PAGE"), state);
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
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.Source.DO_State {
        private java.lang.String title;
        private java.lang.String journal;
        private java.lang.String issue;
        private java.lang.String pubPlace;
        private int startPage;
        private int endPage;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.title = this.title;
            newCasted.journal = this.journal;
            newCasted.issue = this.issue;
            newCasted.pubPlace = this.pubPlace;
            newCasted.startPage = this.startPage;
            newCasted.endPage = this.endPage;
            
        }
        
    }
    
}
