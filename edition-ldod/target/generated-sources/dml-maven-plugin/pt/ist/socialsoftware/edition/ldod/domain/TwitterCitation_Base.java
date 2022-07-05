package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class TwitterCitation_Base extends pt.ist.socialsoftware.edition.ldod.domain.Citation {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation,pt.ist.socialsoftware.edition.ldod.domain.Tweet> role$$tweet = new pt.ist.fenixframework.dml.runtime.RoleMany<pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation,pt.ist.socialsoftware.edition.ldod.domain.Tweet>() {
        public pt.ist.fenixframework.dml.runtime.RelationBaseSet<pt.ist.socialsoftware.edition.ldod.domain.Tweet> getSet(pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation o1) {
            return ((TwitterCitation_Base)o1).get$rl$tweet();
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.Tweet,pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.Tweet.role$$citation;
        }
        
    };
    
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Tweet,pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation> getRelationTwitterCitationHasTweets() {
        return pt.ist.socialsoftware.edition.ldod.domain.Tweet.getRelationTwitterCitationHasTweets();
    }
    
    // Slots
    
    // Role Slots
    private RelationList<pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation,pt.ist.socialsoftware.edition.ldod.domain.Tweet> get$rl$tweet() {
        return get$$relationList("tweet", getRelationTwitterCitationHasTweets().getInverseRelation());
        
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
    protected  TwitterCitation_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getTweetText() {
        return ((DO_State)this.get$obj$state(false)).tweetText;
    }
    
    public void setTweetText(java.lang.String tweetText) {
        ((DO_State)this.get$obj$state(true)).tweetText = tweetText;
    }
    
    private java.lang.String get$tweetText() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).tweetText;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$tweetText(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).tweetText = (java.lang.String)((value == null) ? null : value);
    }
    
    public long getTweetID() {
        return ((DO_State)this.get$obj$state(false)).tweetID;
    }
    
    public void setTweetID(long tweetID) {
        ((DO_State)this.get$obj$state(true)).tweetID = tweetID;
    }
    
    private long get$tweetID() {
        long value = ((DO_State)this.get$obj$state(false)).tweetID;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForlong(value);
    }
    
    private final void set$tweetID(long value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).tweetID = (long)(value);
    }
    
    public java.lang.String getLocation() {
        return ((DO_State)this.get$obj$state(false)).location;
    }
    
    public void setLocation(java.lang.String location) {
        ((DO_State)this.get$obj$state(true)).location = location;
    }
    
    private java.lang.String get$location() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).location;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$location(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).location = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getCountry() {
        return ((DO_State)this.get$obj$state(false)).country;
    }
    
    public void setCountry(java.lang.String country) {
        ((DO_State)this.get$obj$state(true)).country = country;
    }
    
    private java.lang.String get$country() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).country;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$country(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).country = (java.lang.String)((value == null) ? null : value);
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
    
    public java.lang.String getUserProfileURL() {
        return ((DO_State)this.get$obj$state(false)).userProfileURL;
    }
    
    public void setUserProfileURL(java.lang.String userProfileURL) {
        ((DO_State)this.get$obj$state(true)).userProfileURL = userProfileURL;
    }
    
    private java.lang.String get$userProfileURL() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).userProfileURL;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$userProfileURL(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).userProfileURL = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getUserImageURL() {
        return ((DO_State)this.get$obj$state(false)).userImageURL;
    }
    
    public void setUserImageURL(java.lang.String userImageURL) {
        ((DO_State)this.get$obj$state(true)).userImageURL = userImageURL;
    }
    
    private java.lang.String get$userImageURL() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).userImageURL;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$userImageURL(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).userImageURL = (java.lang.String)((value == null) ? null : value);
    }
    
    // Role Methods
    
    public void addTweet(pt.ist.socialsoftware.edition.ldod.domain.Tweet tweet) {
        getRelationTwitterCitationHasTweets().add(tweet, (pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation)this);
    }
    
    public void removeTweet(pt.ist.socialsoftware.edition.ldod.domain.Tweet tweet) {
        getRelationTwitterCitationHasTweets().remove(tweet, (pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation)this);
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
    
    
    protected void checkDisconnected() {
        super.checkDisconnected();
        DO_State castedState = (DO_State)this.get$obj$state(false);
        if (get$rl$tweet().size() > 0) handleAttemptToDeleteConnectedObject("Tweet");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        super.readStateFromResultSet(rs, state);
        DO_State castedState = (DO_State)state;
        set$tweetText(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "TWEET_TEXT"), state);
        set$tweetID(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readlong(rs, "TWEET_I_D"), state);
        set$location(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "LOCATION"), state);
        set$country(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "COUNTRY"), state);
        set$username(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "USERNAME"), state);
        set$userProfileURL(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "USER_PROFILE_U_R_L"), state);
        set$userImageURL(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "USER_IMAGE_U_R_L"), state);
    }
    protected pt.ist.fenixframework.dml.runtime.Relation get$$relationFor(String attrName) {
        if (attrName.equals("tweet")) return getRelationTwitterCitationHasTweets().getInverseRelation();
        return super.get$$relationFor(attrName);
        
    }
    protected pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  make$newState() {
        return new DO_State();
        
    }
    protected void create$allLists() {
        super.create$allLists();
        get$$relationList("tweet", getRelationTwitterCitationHasTweets().getInverseRelation());
        
    }
    protected static class DO_State extends pt.ist.socialsoftware.edition.ldod.domain.Citation.DO_State {
        private java.lang.String tweetText;
        private long tweetID;
        private java.lang.String location;
        private java.lang.String country;
        private java.lang.String username;
        private java.lang.String userProfileURL;
        private java.lang.String userImageURL;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.tweetText = this.tweetText;
            newCasted.tweetID = this.tweetID;
            newCasted.location = this.location;
            newCasted.country = this.country;
            newCasted.username = this.username;
            newCasted.userProfileURL = this.userProfileURL;
            newCasted.userImageURL = this.userImageURL;
            
        }
        
    }
    
}
