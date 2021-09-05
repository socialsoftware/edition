package pt.ist.socialsoftware.edition.virtual.domain;

import pt.ist.socialsoftware.edition.notification.dtos.text.CitationDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.InfoRangeDto;
import pt.ist.socialsoftware.edition.virtual.api.VirtualRequiresInterface;


import java.util.Set;

public class TwitterCitation extends TwitterCitation_Base {



	public TwitterCitation(FragmentDto fragment, String sourceLink, String date, String fragText, String tweetText,
						   long tweetID, String location, String country, String username, String profURL, String profImgURL) {

	//	super.init(fragment.getXmlId(), sourceLink, date.trim(), fragText.trim());

//		infoRangeDtos = VirtualRequiresInterface.getInstance().getInfoRangeDtoSetFromCitation("");

		setFragmentXmlId(fragment.getXmlId());
		setDate(date);
		setSourceLink(sourceLink);
		setFragText(fragText.trim());

		setTweetText(tweetText.trim());
		setTweetID(tweetID);
		setLocation(location.trim());
		setCountry(country.trim());
		setUsername(username);
		setUserProfileURL(profURL);
		setUserImageURL(profImgURL);
		VirtualRequiresInterface.getInstance().createCitation(getFragmentXmlId(), sourceLink, date, fragText, tweetID);
	}

	public TwitterCitation(CitationDto citationDto) {
		setFragmentXmlId(citationDto.getFragmentXmlId());
		setSourceLink(citationDto.getSourceLink());
		setDate(citationDto.getDate());
		setTweetText("");
		setTweetID(citationDto.getId());
		setLocation("");
		setCountry("");
		setUsername("");
		setUserProfileURL("");
		setUserImageURL("");
	}


	public boolean isTwitterCitation() {
		return false;
	}


	public void remove() {
		getTweetSet().stream().forEach(t -> removeTweet(t));

	//	super.remove();
	}


	public long getId() {
		return getTweetID();
	}


	public int getNumberOfRetweets() {
		return this.getTweetSet().size() - 1; // -1 to not include the original tweet
	}


//	public Set<InfoRangeDto> getInfoRangeDtoSet() {
//		return VirtualRequiresInterface.getInstance().getInfoRangeDtoSetFromCitation(getExternalId());
//	}



	public Set<InfoRangeDto> getInfoRangeDtoSet() {
		return VirtualRequiresInterface.getInstance().getInfoRangeDtoSetFromCitation(getId());
	}
}
