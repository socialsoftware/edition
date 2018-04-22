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

@Component
public class ScheduledTasks {
	private static Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

	@Scheduled(cron = "0 0 10,18 * * *")
	public void reportCurrentTime() throws IOException {
		WriteVirtualEditonsToFile write = new WriteVirtualEditonsToFile();
		write.export();
	}
	
	@Scheduled(cron = "0 35 11 * * *")
	public void fetchFromTwitter() throws IOException {
		FetchCitationsFromTwitter fetch = new FetchCitationsFromTwitter();
		fetch.fetch();
	}
	
	@Scheduled(cron = "0 10 12 * * *")
	public void detectCitations() throws IOException {
		CitationDetecter detecter = new CitationDetecter();
		detecter.detect();
	}
	
	@Scheduled(cron = "0 31 16 * * *")
	public void createAwareAnnotations() throws IOException {
		AwareAnnotationFactory awareFactory = new AwareAnnotationFactory();
		awareFactory.create();
	}
}