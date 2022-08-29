package pt.ist.socialsoftware.edition.ldod.bff.social.service;

import org.springframework.stereotype.Service;
import pt.ist.socialsoftware.edition.ldod.bff.social.dtos.CitationDto;
import pt.ist.socialsoftware.edition.ldod.bff.social.dtos.TweetListDto;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.ldod.social.aware.AwareAnnotationFactory;
import pt.ist.socialsoftware.edition.ldod.social.aware.CitationDetecter;
import pt.ist.socialsoftware.edition.ldod.social.aware.TweetFactory;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocialService {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");

    public List<CitationDto> getCitationsList() {
        return LdoD.getInstance()
                .getCitationsWithInfoRanges()
                .stream()
                .map(CitationDto::new)
                .collect(Collectors.toList());
    }

    public TweetListDto getTweetsToManage() {
        return new TweetListDto(
                this.getTwitterCitationsListSortedByData(),
                LdoD.getInstance().getTweetSet(),
                LdoD.getInstance().getNumberOfCitationsWithInfoRanges());
    }

    private List<TwitterCitation> getTwitterCitationsListSortedByData() {
        return LdoD.getInstance()
                .getAllTwitterCitation()
                .stream().sorted((c1, c2) -> java.time.LocalDateTime.parse(c2.getDate(), formatter).compareTo(java.time.LocalDateTime.parse(c1.getDate(), formatter)))
                .collect(Collectors.toList());

    }

    public TweetListDto removeTweets() {
        LdoD.getInstance().removeTweets();
        return getTweetsToManage();

    }


    public TweetListDto generateCitations() throws IOException {

        CitationDetecter detecter = new CitationDetecter();
        detecter.detect();

        TweetFactory tweetFactory = new TweetFactory();
        tweetFactory.create();

        AwareAnnotationFactory awareFactory = new AwareAnnotationFactory();
        awareFactory.generate();

        LdoD.dailyRegenerateTwitterCitationEdition();

        // Repeat to update edition
        awareFactory.generate();

        return this.getTweetsToManage();
    }
}
