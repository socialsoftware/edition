package pt.ist.socialsoftware.edition.virtual.api.dto;

import pt.ist.socialsoftware.edition.virtual.domain.Tweet;

public class TweetDto {

    private long id;

    public TweetDto(Tweet tweet) {
        this.id = tweet.getTweetID();
    }

    public long getId() {
        return id;
    }
}
