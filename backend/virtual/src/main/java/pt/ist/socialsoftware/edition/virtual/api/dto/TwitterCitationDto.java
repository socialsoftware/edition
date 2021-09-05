package pt.ist.socialsoftware.edition.virtual.api.dto;

import pt.ist.socialsoftware.edition.virtual.domain.TwitterCitation;

public class TwitterCitationDto  {

    private String tweetText;
    private String country;
    private String location;
    private String username;
    private String userProfileUrl;
    private long id;
    private long tweetId;

    public TwitterCitationDto(TwitterCitation citation) {
        //super(citation);
        this.tweetText = citation.getTweetText();
        this.country = citation.getCountry();
        this.location = citation.getLocation();
        this.username = citation.getUsername();
        this.userProfileUrl = citation.getUserProfileURL();
        this.id = citation.getId();
        this.tweetId = citation.getTweetID();
    }

    public String getTweetText() {
        return tweetText;
    }

    public String getCountry() {
        return country;
    }

    public String getLocation() {
        return location;
    }

    public String getUsername() {
        return username;
    }


    public boolean isTwitterCitation() {
        return true;
    }

    public String getUserProfileURL() {
        return this.userProfileUrl;
    }

    public long getId() {
        return id;
    }

    public long getTweetId() {
        return tweetId;
    }
}
