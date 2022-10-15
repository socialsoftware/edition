package pt.ist.socialsoftware.edition.ldod.config;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.export.WriteVirtualEditionsToFile;
import pt.ist.socialsoftware.edition.ldod.game.classification.GameRunner;
import pt.ist.socialsoftware.edition.ldod.social.aware.AwareAnnotationFactory;
import pt.ist.socialsoftware.edition.ldod.social.aware.CitationDetecter;
import pt.ist.socialsoftware.edition.ldod.social.aware.FetchCitationsFromTwitter;
import pt.ist.socialsoftware.edition.ldod.social.aware.TweetFactory;

@Component
public class ScheduledTasks {
	private static Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

	@Autowired
	GameRunner gameRunner;

	@Scheduled(cron = "0 * * * * *")
	public void scheduleGames() throws IOException {
		logger.debug("scheduleGames starting");
		List<String> gameIds = getGames();
		for (String id : gameIds) {
			logger.debug("scheduleGames id {}", id);
			this.gameRunner.setGameId(id);
			new Thread(this.gameRunner).start();
		}
	}

	@Atomic(mode = TxMode.READ)
	private List<String> getGames() {
		DateTime now = DateTime.now();

		return LdoD.getInstance().getVirtualEditionsSet().stream()
				.flatMap(virtualEdition -> virtualEdition.getClassificationGameSet().stream())
				.filter(g -> g.getState().equals(ClassificationGame.ClassificationGameState.CREATED)
						&& g.getDateTime().isAfter(now) && g.getDateTime().isBefore(now.plusMinutes(2)))
				.sorted(Comparator.comparing(ClassificationGame::getDateTime)).map(g -> g.getExternalId())
				.collect(Collectors.toList());
	}

	@Scheduled(cron = "0 0 1 * * *")
	public void generateTwitterCitations() throws IOException {
		FetchCitationsFromTwitter fetch = new FetchCitationsFromTwitter();
		fetch.fetch();

		CitationDetecter detecter = new CitationDetecter();
		detecter.detect();

		TweetFactory tweetFactory = new TweetFactory();
		tweetFactory.create();

		AwareAnnotationFactory awareFactory = new AwareAnnotationFactory();
		awareFactory.generate();

		LdoD.dailyRegenerateTwitterCitationEdition();

		// Repeat to update edition
		awareFactory.generate();
	}

	@Scheduled(cron = "0 0 5 * * *")
	public void lucenePerformance() throws IOException {
		// LucenePerformance lucenePerformance = new LucenePerformance();
		// lucenePerformance.runLivro();
		// lucenePerformance.runBernardo();
		// lucenePerformance.runFP();
		// lucenePerformance.runVicente();
	}

	@Scheduled(cron = "0 0 10,18 * * *")
	public void reportCurrentTime() throws IOException {
		WriteVirtualEditionsToFile write = new WriteVirtualEditionsToFile();
		write.export();
	}

	@Scheduled(cron = "0 0 12 * * *")
	public void generateGames() throws IOException {
		logger.debug("generateGames");
		LdoD.manageDailyClassificationGames(DateTime.now());
	}

}