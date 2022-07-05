package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.Tweet;
import pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation;

public class TweetListDto {

	private List<TwitterCitationDto> citations;
	private int tweetsSize;
	private int numberCitations;

	public TweetListDto(List<TwitterCitation> citations, Set<Tweet> tweetSet, int numberOfCitationsWithInfoRanges) {
		this.setCitations(citations.stream().map(TwitterCitationDto::new).collect(Collectors.toList()));
		this.setTweetsSize(tweetSet.size());
		this.setNumberCitations(numberOfCitationsWithInfoRanges);
		
	}

	public List<TwitterCitationDto> getCitations() {
		return citations;
	}

	public void setCitations(List<TwitterCitationDto> citations) {
		this.citations = citations;
	}

	public int getTweetsSize() {
		return tweetsSize;
	}

	public void setTweetsSize(int tweetsSize) {
		this.tweetsSize = tweetsSize;
	}

	public int getNumberCitations() {
		return numberCitations;
	}

	public void setNumberCitations(int numberCitations) {
		this.numberCitations = numberCitations;
	}


}
