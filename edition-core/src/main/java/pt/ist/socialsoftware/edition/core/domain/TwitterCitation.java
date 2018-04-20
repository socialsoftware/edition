package pt.ist.socialsoftware.edition.core.domain;

public class TwitterCitation extends TwitterCitation_Base {
    
    public TwitterCitation(LdoD ldoD, Fragment fragment, String sourceLink, String date, String fragText, 
    		String tweetText, long tweetID, String location, String country, String username, String profURL, String profImgURL) {
        super.init(ldoD, fragment, sourceLink, date, fragText);
        setTweetText(tweetText);
        setTweetID(tweetID);
        setLocation(location);
        setCountry(country);
        setUsername(username);
        setUserProfileURL(profURL);
        setUserImageURL(profImgURL);
    }
    
 
    
}
