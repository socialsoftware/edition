package pt.ist.socialsoftware.edition.ldod.bff.social.dtos;


import pt.ist.socialsoftware.edition.ldod.domain.Tweet;
import pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TweetListDto {

    private List<TwitterCitationDto> twitterCitations;
    private int tweetsSize;
    private int numberOfCitationsWithInfoRanges;

    public TweetListDto(List<TwitterCitation> citations, Set<Tweet> tweetSet, int numberOfCitationsWithInfoRanges) {
        this.setTwitterCitations(citations.stream().map(TwitterCitationDto::new).collect(Collectors.toList()));
        this.setTweetsSize(tweetSet.size());
        this.setNumberOfCitationsWithInfoRanges(numberOfCitationsWithInfoRanges);

    }

    public List<TwitterCitationDto> getTwitterCitations() {
        return twitterCitations;
    }

    public void setTwitterCitations(List<TwitterCitationDto> twitterCitations) {
        this.twitterCitations = twitterCitations;
    }

    public int getTweetsSize() {
        return tweetsSize;
    }

    public void setTweetsSize(int tweetsSize) {
        this.tweetsSize = tweetsSize;
    }

    public int getNumberOfCitationsWithInfoRanges() {
        return numberOfCitationsWithInfoRanges;
    }

    public void setNumberOfCitationsWithInfoRanges(int numberOfCitationsWithInfoRanges) {
        this.numberOfCitationsWithInfoRanges = numberOfCitationsWithInfoRanges;
    }


}
