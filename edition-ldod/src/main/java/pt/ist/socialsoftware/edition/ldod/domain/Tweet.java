package pt.ist.socialsoftware.edition.ldod.domain;

public class Tweet extends Tweet_Base {

	public Tweet(LdoD ldoD, String sourceLink, String date, String tweetText, long tweetID, String location,
			String country, String username, String profURL, String profImgURL, long originalTweetID, boolean isRetweet,
			TwitterCitation twitterCitation) {
		setLdoD(ldoD);
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
		setLdoD(null);
		setCitation(null);

		deleteDomainObject();
	}
}
