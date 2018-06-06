package pt.ist.socialsoftware.edition.core.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pt.ist.socialsoftware.edition.core.export.WriteVirtualEditonsToFile;
import pt.ist.socialsoftware.edition.core.social.aware.AwareAnnotationFactory;
import pt.ist.socialsoftware.edition.core.social.aware.CitationDetecter;
import pt.ist.socialsoftware.edition.core.social.aware.FetchCitationsFromTwitter;
import pt.ist.socialsoftware.edition.core.social.aware.TweetFactory;

@Component
public class ScheduledTasks {
	private static Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

	@Scheduled(cron = "0 0 2,3 * * *")
	public void reportCurrentTime() throws IOException {
		WriteVirtualEditonsToFile write = new WriteVirtualEditonsToFile();
		write.export();
	}
	
	@Scheduled(cron = "0 56 2 * * *")
	public void fetchFromTwitter() throws IOException {
		FetchCitationsFromTwitter fetch = new FetchCitationsFromTwitter();
		fetch.fetch();
	}
	
	@Scheduled(cron = "0 4 3 * * *")
	public void detectCitations() throws IOException {
		CitationDetecter detecter = new CitationDetecter();
		detecter.detect();
	}
	
	
	@Scheduled(cron = "0 5 4 * * *")
	public void createTweets() throws IOException {
		TweetFactory tweetFactory = new TweetFactory();
		tweetFactory.create();
	}
	
	
	@Scheduled(cron = "0 54 16 * * *")
	public void createAwareAnnotations() throws IOException {
		AwareAnnotationFactory awareFactory = new AwareAnnotationFactory();
		awareFactory.create();
	}
}