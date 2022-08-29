package pt.ist.socialsoftware.edition.ldod.domain;

public class TwitterCitation extends TwitterCitation_Base {

	public TwitterCitation(Fragment fragment, String sourceLink, String date, String fragText, String tweetText,
			long tweetID, String location, String country, String username, String profURL, String profImgURL) {

		super.init(fragment, sourceLink, date.trim(), fragText.trim());
		setTweetText(tweetText.trim());
		setTweetID(tweetID);
		setLocation(location.trim());
		setCountry(country.trim());
		setUsername(username);
		setUserProfileURL(profURL);
		setUserImageURL(profImgURL);
	}

	@Override
	public void remove() {
		getTweetSet().forEach(this::removeTweet);

		super.remove();
	}

	@Override
	public long getId() {
		return getTweetID();
	}

	@Override
	public int getNumberOfRetweets() {
		return this.getTweetSet().size() - 1; // -1 to not include the original tweet
	}

}
