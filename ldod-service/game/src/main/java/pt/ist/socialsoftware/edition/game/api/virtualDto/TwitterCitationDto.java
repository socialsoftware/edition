package pt.ist.socialsoftware.edition.game.api.virtualDto;


public class TwitterCitationDto  {

    private String tweetText;
    private String country;
    private String location;
    private String username;
    private String userProfileUrl;

    public TwitterCitationDto() { }

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
