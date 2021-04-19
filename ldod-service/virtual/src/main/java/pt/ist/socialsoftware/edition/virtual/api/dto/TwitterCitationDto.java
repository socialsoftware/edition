package pt.ist.socialsoftware.edition.virtual.api.dto;

import pt.ist.socialsoftware.edition.virtual.api.textdto.CitationDto;
import pt.ist.socialsoftware.edition.virtual.domain.TwitterCitation;

public class TwitterCitationDto  {

    private String tweetText;
    private String country;
    private String location;
    private String username;
    private String userProfileUrl;

    public TwitterCitationDto(TwitterCitation citation) {
        //super(citation);
        this.tweetText = citation.getTweetText();
        this.country = citation.getCountry();
        this.location = citation.getLocation();
        this.username = citation.getUsername();
        this.userProfileUrl = citation.getUserProfileURL();
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
}
