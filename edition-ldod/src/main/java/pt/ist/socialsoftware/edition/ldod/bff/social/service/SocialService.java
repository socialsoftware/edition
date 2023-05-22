package pt.ist.socialsoftware.edition.ldod.bff.social.service;

import org.springframework.stereotype.Service;
import pt.ist.socialsoftware.edition.ldod.bff.social.dtos.CitationDto;
import pt.ist.socialsoftware.edition.ldod.bff.social.dtos.TweetListDto;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.ldod.social.aware.AwareAnnotationFactory;
import pt.ist.socialsoftware.edition.ldod.social.aware.CitationDetecter;
import pt.ist.socialsoftware.edition.ldod.social.aware.FetchCitationsFromTwitter;
import pt.ist.socialsoftware.edition.ldod.social.aware.TweetFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class SocialService {

    private final AwareAnnotationFactory awareFactory = new AwareAnnotationFactory();

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
                .stream()
                .sorted((c1, c2) -> parseDate(c2.getDate()).compareTo(parseDate(c1.getDate())))
                .collect(Collectors.toList());
    }

    public TweetListDto removeTweets() {
        LdoD.getInstance().removeTweets();
        return getTweetsToManage();

    }


    public TweetListDto generateCitations() throws IOException {
        FetchCitationsFromTwitter fetch = new FetchCitationsFromTwitter();
        fetch.fetch();
        new CitationDetecter().detect();
        new TweetFactory().create();
        awareFactory.generate();
        LdoD.dailyRegenerateTwitterCitationEdition();
        awareFactory.generate();

        return this.getTweetsToManage();
    }

    public LocalDateTime parseDate(String dateStr) {
        DateTimeFormatter formatterEnglish = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        DateTimeFormatter formatterPortuguese = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss", new Locale("pt", "BR"));
        try {
            return LocalDateTime.parse(dateStr, formatterEnglish);
        } catch (DateTimeParseException e) {
            return LocalDateTime.parse(dateStr, formatterPortuguese);
        }
    }
}
