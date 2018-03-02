package pt.ist.socialsoftware.edition.core.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pt.ist.socialsoftware.edition.core.export.WriteVirtualEditonsToFile;
import pt.ist.socialsoftware.edition.core.social.aware.FetchCitationsFromTwitter;
//import pt.ist.socialsoftware.edition.core.social.aware.FetchCitationsFromTwitter;

@Component
public class ScheduledTasks {
	private static Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

	@Scheduled(cron = "0 0 10,18 * * *")
	public void reportCurrentTime() throws IOException {
		WriteVirtualEditonsToFile write = new WriteVirtualEditonsToFile();
		write.export();
	}
	
	@Scheduled(cron = "0 0 20 * * *")
	public void fetchFromTwitter() throws IOException {
		FetchCitationsFromTwitter fetch = new FetchCitationsFromTwitter();
		fetch.fetch();
	}
}