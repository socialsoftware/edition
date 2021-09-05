package pt.ist.socialsoftware.edition.ldod.frontend.config;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.frontend.game.FeGameRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.FeVirtualRequiresInterface;

import java.io.IOException;
import java.util.List;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private final FeGameRequiresInterface feGameRequiresInterface = new FeGameRequiresInterface();
    private final FeVirtualRequiresInterface feVirtualRequiresInterface = new FeVirtualRequiresInterface();


    @Scheduled(cron = "0 * * * * *")
    public void scheduleGames() throws IOException {
        logger.debug("scheduleGames starting");
        List<String> gameIds = getGames();
        for (String id : gameIds) {
            logger.debug("scheduleGames id {}", id);
            this.feGameRequiresInterface.startGameRunner(id);
        }
    }

    @Atomic(mode = TxMode.READ)
    private List<String> getGames() {
        DateTime now = DateTime.now();
        return this.feGameRequiresInterface.getGamesForScheduledTasks(now);
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void generateTwitterCitations() throws IOException {

        this.feVirtualRequiresInterface.fetchCitationsFromTwitter();

        this.feVirtualRequiresInterface.detectCitation();

        this.feVirtualRequiresInterface.createTweetFactory();

        this.feVirtualRequiresInterface.generateAwareAnnotations();

        this.feVirtualRequiresInterface.dailyRegenerateTwitterCitationEdition();

        this.feVirtualRequiresInterface.generateAwareAnnotations();
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
        this.feVirtualRequiresInterface.getWriteVirtualEditionToFileExport();
    }

    @Scheduled(cron = "0 0 12 * * *")
    public void generateGames() throws IOException {
        logger.debug("generateGames");
        this.feGameRequiresInterface.manageDailyClassificationGames();
    }

}