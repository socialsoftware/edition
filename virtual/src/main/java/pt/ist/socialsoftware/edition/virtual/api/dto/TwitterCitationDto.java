package pt.ist.socialsoftware.edition.virtual.api.dto;

import pt.ist.socialsoftware.edition.text.api.dto.CitationDto;
import pt.ist.socialsoftware.edition.virtual.domain.TwitterCitation;

public class TwitterCitationDto extends CitationDto {

    private String tweetText;
    private String country;
    private String location;
    private String username;
    private String userProfileUrl;

    public TwitterCitationDto(TwitterCitation citation) {
        super(citation);
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

    @Override
    public boolean isTwitterCitation() {
        return true;
    }

    public String getUserProfileURL() {
        return this.userProfileUrl;
    }
}
