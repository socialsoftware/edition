package pt.ist.socialsoftware.edition.virtual.domain;


import pt.ist.socialsoftware.edition.notification.utils.LdoDException;

public class Tweet extends Tweet_Base {

    public Tweet(VirtualModule virtualModule, String sourceLink, String date, String tweetText, long tweetID, String location,
                 String country, String username, String profURL, String profImgURL, long originalTweetID, boolean isRetweet,
                 TwitterCitation twitterCitation) {
        if (!isRetweet && originalTweetID != -1l) {
            throw new LdoDException("This tweet is not a retweet, therefore its original tweet id must be -1!");
        }
        setVirtualModule(virtualModule);
        setSourceLink(sourceLink);
        setDate(date);
        setTweetText(tweetText);

        setTweetID(tweetID);
        setLocation(location);
        setCountry(country);
        setUsername(username);
        setUserProfileURL(profURL);
        setUserImageURL(profImgURL);

        setOriginalTweetID(originalTweetID);
        setIsRetweet(isRetweet);
        setCitation(twitterCitation);
    }

    public void remove() {
        setVirtualModule(null);
        if (getCitation() != null) {
            getCitation().remove();
        }

        deleteDomainObject();
    }
}
