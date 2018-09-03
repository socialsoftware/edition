package pt.ist.socialsoftware.edition.ldod.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pt.ist.socialsoftware.edition.ldod.export.WriteVirtualEditonsToFile;
import pt.ist.socialsoftware.edition.ldod.social.aware.AwareAnnotationFactory;
import pt.ist.socialsoftware.edition.ldod.social.aware.CitationDetecter;
import pt.ist.socialsoftware.edition.ldod.social.aware.FetchCitationsFromTwitter;
import pt.ist.socialsoftware.edition.ldod.social.aware.TweetFactory;

@Component
public class ScheduledTasks {
	private static Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

	@Scheduled(cron = "0 0 10,18 * * *")
	public void reportCurrentTime() throws IOException {
		WriteVirtualEditonsToFile write = new WriteVirtualEditonsToFile();
		write.export();
	}

	@Scheduled(cron = "0 0 1 * * *")
	public void fetchFromTwitter() throws IOException {
		FetchCitationsFromTwitter fetch = new FetchCitationsFromTwitter();
		fetch.fetch();
	}

	@Scheduled(cron = "0 0 2 * * *")
	public void detectCitations() throws IOException {
		CitationDetecter detecter = new CitationDetecter();
		detecter.detect();
	}

	@Scheduled(cron = "0 0 3 * * *")
	public void createTweets() throws IOException {
		TweetFactory tweetFactory = new TweetFactory();
		tweetFactory.create();
	}

	@Scheduled(cron = "0 0 4 * * *")
	public void createAwareAnnotations() throws IOException {
		AwareAnnotationFactory awareFactory = new AwareAnnotationFactory();
		awareFactory.generate();
	}
}