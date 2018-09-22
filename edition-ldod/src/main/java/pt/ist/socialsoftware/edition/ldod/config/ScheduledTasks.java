package pt.ist.socialsoftware.edition.ldod.config;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.*;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.dto.ClassificationGameDto;
import pt.ist.socialsoftware.edition.ldod.export.WriteVirtualEditonsToFile;
import pt.ist.socialsoftware.edition.ldod.game.classification.GameRunner;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.social.aware.AwareAnnotationFactory;
import pt.ist.socialsoftware.edition.ldod.social.aware.CitationDetecter;
import pt.ist.socialsoftware.edition.ldod.social.aware.FetchCitationsFromTwitter;
import pt.ist.socialsoftware.edition.ldod.social.aware.TweetFactory;

@Component
public class ScheduledTasks {
	private static Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

	@Autowired
	GameRunner gameRunner;

	@Scheduled(cron="0 * * * * *")
	//@Scheduled(cron="0 0/5 * * * ?")
	public void scheduleGames() throws IOException {
		logger.debug("scheduleGames starting");
		List<String> gameIds = getGames();
		for (String id: gameIds) {
			logger.debug("scheduleGames id {}", id);
			//GameRunner game = new GameRunner(id);
			//new Thread(game).start();
			gameRunner.setGameId(id);
			new Thread(gameRunner).start();
		}
	}

	@Atomic(mode = TxMode.READ)
	private List<String> getGames() {
		DateTime now = DateTime.now();

		return LdoD.getInstance().getVirtualEditionsSet().stream().flatMap(virtualEdition ->
				virtualEdition.getClassificationGameSet().stream()).filter(g -> g.getState().equals(ClassificationGame
				.ClassificationGameState.CREATED) && g.getDateTime().isAfter(now) && g
				.getDateTime().isBefore(now.plusMinutes(2))).sorted(Comparator.comparing
				(ClassificationGame::getDateTime)).map(g -> g.getExternalId()).collect(Collectors.toList());
	}

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