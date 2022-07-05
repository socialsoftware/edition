package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.backend.jvstmojb.pstm.RelationList;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.ist.fenixframework.ValueTypeSerializer;


@SuppressWarnings("all")
public abstract class Tweet_Base extends pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject {
    // Static Slots
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Tweet,pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation> role$$citation = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Tweet,pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation getValue(pt.ist.socialsoftware.edition.ldod.domain.Tweet o1) {
            return ((Tweet_Base.DO_State)o1.get$obj$state(false)).citation;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Tweet o1, pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation o2) {
            ((Tweet_Base.DO_State)o1.get$obj$state(true)).citation = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation,pt.ist.socialsoftware.edition.ldod.domain.Tweet> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation.role$$tweet;
        }
        
    };
    public final static pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Tweet,pt.ist.socialsoftware.edition.ldod.domain.LdoD> role$$ldoD = new pt.ist.fenixframework.dml.runtime.RoleOne<pt.ist.socialsoftware.edition.ldod.domain.Tweet,pt.ist.socialsoftware.edition.ldod.domain.LdoD>() {
        @Override
        public pt.ist.socialsoftware.edition.ldod.domain.LdoD getValue(pt.ist.socialsoftware.edition.ldod.domain.Tweet o1) {
            return ((Tweet_Base.DO_State)o1.get$obj$state(false)).ldoD;
        }
        @Override
        public void setValue(pt.ist.socialsoftware.edition.ldod.domain.Tweet o1, pt.ist.socialsoftware.edition.ldod.domain.LdoD o2) {
            ((Tweet_Base.DO_State)o1.get$obj$state(true)).ldoD = o2;
        }
        public pt.ist.fenixframework.dml.runtime.Role<pt.ist.socialsoftware.edition.ldod.domain.LdoD,pt.ist.socialsoftware.edition.ldod.domain.Tweet> getInverseRole() {
            return pt.ist.socialsoftware.edition.ldod.domain.LdoD.role$$tweet;
        }
        
    };
    
    private final static class TwitterCitationHasTweets {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Tweet,pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Tweet,pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation>(role$$citation, "TwitterCitationHasTweets");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Tweet,pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation> getRelationTwitterCitationHasTweets() {
        return TwitterCitationHasTweets.relation;
    }
    
    static {
        TwitterCitationHasTweets.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Tweet.TwitterCitationHasTweets");
    }
    
    private final static class LdoDHasTweets {
        private static final pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Tweet,pt.ist.socialsoftware.edition.ldod.domain.LdoD> relation = new pt.ist.fenixframework.backend.jvstmojb.pstm.LoggingRelation<pt.ist.socialsoftware.edition.ldod.domain.Tweet,pt.ist.socialsoftware.edition.ldod.domain.LdoD>(role$$ldoD, "LdoDHasTweets");
    }
    public static pt.ist.fenixframework.dml.runtime.DirectRelation<pt.ist.socialsoftware.edition.ldod.domain.Tweet,pt.ist.socialsoftware.edition.ldod.domain.LdoD> getRelationLdoDHasTweets() {
        return LdoDHasTweets.relation;
    }
    
    static {
        LdoDHasTweets.relation.setRelationName("pt.ist.socialsoftware.edition.ldod.domain.Tweet.LdoDHasTweets");
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
    protected  Tweet_Base() {
        super();
    }
    
    // Getters and Setters
    
    public java.lang.String getSourceLink() {
        return ((DO_State)this.get$obj$state(false)).sourceLink;
    }
    
    public void setSourceLink(java.lang.String sourceLink) {
        ((DO_State)this.get$obj$state(true)).sourceLink = sourceLink;
    }
    
    private java.lang.String get$sourceLink() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).sourceLink;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$sourceLink(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).sourceLink = (java.lang.String)((value == null) ? null : value);
    }
    
    public java.lang.String getDate() {
        return ((DO_State)this.get$obj$state(false)).date;
    }
    
    public void setDate(java.lang.String date) {
        ((DO_State)this.get$obj$state(true)).date = date;
    }
    
    private java.lang.String get$date() {
        java.lang.String value = ((DO_State)this.get$obj$state(false)).date;
        return (value == null) ? null : pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForString(value);
    }
    
    private final void set$date(java.lang.String value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).date = (java.lang.String)((value == null) ? null : value);
    }
    
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
    
    public long getOriginalTweetID() {
        return ((DO_State)this.get$obj$state(false)).originalTweetID;
    }
    
    public void setOriginalTweetID(long originalTweetID) {
        ((DO_State)this.get$obj$state(true)).originalTweetID = originalTweetID;
    }
    
    private long get$originalTweetID() {
        long value = ((DO_State)this.get$obj$state(false)).originalTweetID;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForlong(value);
    }
    
    private final void set$originalTweetID(long value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).originalTweetID = (long)(value);
    }
    
    public boolean getIsRetweet() {
        return ((DO_State)this.get$obj$state(false)).isRetweet;
    }
    
    public void setIsRetweet(boolean isRetweet) {
        ((DO_State)this.get$obj$state(true)).isRetweet = isRetweet;
    }
    
    private boolean get$isRetweet() {
        boolean value = ((DO_State)this.get$obj$state(false)).isRetweet;
        return pt.ist.fenixframework.backend.jvstmojb.repository.ToSqlConverter.getValueForboolean(value);
    }
    
    private final void set$isRetweet(boolean value, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  obj$state) {
        ((DO_State)obj$state).isRetweet = (boolean)(value);
    }
    
    // Role Methods
    
    public pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation getCitation() {
        return ((DO_State)this.get$obj$state(false)).citation;
    }
    
    public void setCitation(pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation citation) {
        getRelationTwitterCitationHasTweets().add((pt.ist.socialsoftware.edition.ldod.domain.Tweet)this, citation);
    }
    
    private java.lang.Long get$oidCitation() {
        pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject value = (pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject) ((DO_State)this.get$obj$state(false)).citation;
        return (value == null) ? null : value.getOid();
    }
    
    public pt.ist.socialsoftware.edition.ldod.domain.LdoD getLdoD() {
        return ((DO_State)this.get$obj$state(false)).ldoD;
    }
    
    public void setLdoD(pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD) {
        getRelationLdoDHasTweets().add((pt.ist.socialsoftware.edition.ldod.domain.Tweet)this, ldoD);
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
        if (castedState.citation != null) handleAttemptToDeleteConnectedObject("Citation");
        if (castedState.ldoD != null) handleAttemptToDeleteConnectedObject("LdoD");
        
    }
    
    protected void readStateFromResultSet(java.sql.ResultSet rs, pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  state) throws java.sql.SQLException {
        DO_State castedState = (DO_State)state;
        set$sourceLink(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "SOURCE_LINK"), state);
        set$date(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "DATE"), state);
        set$tweetText(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "TWEET_TEXT"), state);
        set$tweetID(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readlong(rs, "TWEET_I_D"), state);
        set$location(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "LOCATION"), state);
        set$country(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "COUNTRY"), state);
        set$username(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "USERNAME"), state);
        set$userProfileURL(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "USER_PROFILE_U_R_L"), state);
        set$userImageURL(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readString(rs, "USER_IMAGE_U_R_L"), state);
        set$originalTweetID(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readlong(rs, "ORIGINAL_TWEET_I_D"), state);
        set$isRetweet(pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readboolean(rs, "IS_RETWEET"), state);
        castedState.citation = pt.ist.fenixframework.backend.jvstmojb.repository.ResultSetReader.readDomainObject(rs, "OID_CITATION");
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
        private java.lang.String sourceLink;
        private java.lang.String date;
        private java.lang.String tweetText;
        private long tweetID;
        private java.lang.String location;
        private java.lang.String country;
        private java.lang.String username;
        private java.lang.String userProfileURL;
        private java.lang.String userImageURL;
        private long originalTweetID;
        private boolean isRetweet;
        private pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation citation;
        private pt.ist.socialsoftware.edition.ldod.domain.LdoD ldoD;
        protected void copyTo(pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject.DO_State  newState) {
            super.copyTo(newState);
            DO_State newCasted = (DO_State)newState;
            newCasted.sourceLink = this.sourceLink;
            newCasted.date = this.date;
            newCasted.tweetText = this.tweetText;
            newCasted.tweetID = this.tweetID;
            newCasted.location = this.location;
            newCasted.country = this.country;
            newCasted.username = this.username;
            newCasted.userProfileURL = this.userProfileURL;
            newCasted.userImageURL = this.userImageURL;
            newCasted.originalTweetID = this.originalTweetID;
            newCasted.isRetweet = this.isRetweet;
            newCasted.citation = this.citation;
            newCasted.ldoD = this.ldoD;
            
        }
        
    }
    
}
