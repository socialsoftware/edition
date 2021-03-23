package pt.ist.socialsoftware.edition.virtual.domain;

import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.text.api.dto.InfoRangeDto;

import java.util.Set;

public class TwitterCitation extends TwitterCitation_Base {

	private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

	public TwitterCitation(FragmentDto fragment, String sourceLink, String date, String fragText, String tweetText,
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
	public boolean isTwitterCitation() {
		return false;
	}

	@Override
	public void remove() {
		getTweetSet().stream().forEach(t -> removeTweet(t));

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


	public Set<InfoRangeDto> getInfoRangeDtoSet() {
		return this.textProvidesInterface.getInfoRangeDtoSetFromCitation(getExternalId());
	}

}
