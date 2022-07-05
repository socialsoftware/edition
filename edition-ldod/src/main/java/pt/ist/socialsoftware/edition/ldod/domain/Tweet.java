package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

public class Tweet extends Tweet_Base {

	public Tweet(LdoD ldoD, String sourceLink, String date, String tweetText, long tweetID, String location,
			String country, String username, String profURL, String profImgURL, long originalTweetID, boolean isRetweet,
			TwitterCitation twitterCitation) {
		if (!isRetweet && originalTweetID != -1l) {
			throw new LdoDException("This tweet is not a retweet, therefore its original tweet id must be -1!");
		}
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
		if (getCitation() != null) {
			getCitation().remove();
		}

		deleteDomainObject();
	}
}
