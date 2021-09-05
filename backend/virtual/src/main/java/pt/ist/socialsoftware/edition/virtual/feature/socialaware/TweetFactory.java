package pt.ist.socialsoftware.edition.virtual.feature.socialaware;

import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.notification.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.virtual.domain.LastTwitterID;
import pt.ist.socialsoftware.edition.virtual.domain.Tweet;
import pt.ist.socialsoftware.edition.virtual.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule;


import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TweetFactory {
    private static final Logger logger = LoggerFactory.getLogger(TweetFactory.class);

    public void create() throws IOException {
        logger.debug("STARTED TWEET FACTORY!!!");

        File folder = new File(PropertiesManager.getProperties().getProperty("social.aware.dir"));
        // get just files, not directories
        File[] files = folder.listFiles((FileFilter) FileFileFilter.FILE);
        Arrays.sort(files, NameFileComparator.NAME_COMPARATOR);

        String[] sources = {LastTwitterID.FP_CITATIONS, LastTwitterID.LIVRO_CITATIONS, LastTwitterID.BERNARDO_CITATIONS, LastTwitterID.VICENTE_CITATIONS};

        for (String source : sources) {
            String lastFileName = getLastProcessedFileName(source);

            List<File> sourceFiles = Arrays.stream(files)
                    .filter(f -> f.getName().contains(source) && f.getName().compareTo(lastFileName) >= 0)
                    .collect(Collectors.toList());

            for (File fileEntry : sourceFiles) {
                logger.debug("JSON file name: " + fileEntry.getName());
                fileTweetCreation(fileEntry);
                updateLastProcessedFileName(fileEntry);
            }
        }

        logger.debug("DELETE CITATIONS WITHOUT INFO RANGE");
        VirtualModule.deleteTweetCitationsWithoutInfoRangeOrTweet();

        logger.debug("DELETE TWEETS WITHOUT CITATION");
        VirtualModule.deleteTweetsWithoutCitation();

        logger.debug("FINISHED TWEET FACTORY!!!");
    }

    @Atomic(mode = TxMode.WRITE)
    private void updateLastProcessedFileName(File fileEntry) {
        VirtualModule.getInstance().getLastTwitterID().updateLastParsedFile(fileEntry.getName());
    }

    @Atomic(mode = TxMode.READ)
    private String getLastProcessedFileName(String source) {
        return VirtualModule.getInstance().getLastTwitterID().getLastParsedFile(source) != null
                ? VirtualModule.getInstance().getLastTwitterID().getLastParsedFile(source)
                : "";
    }

    private void fileTweetCreation(File fileEntry) throws FileNotFoundException, IOException {
        String line = null;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileEntry));

        while ((line = bufferedReader.readLine()) != null) {
//			try {
//				FenixFramework.getTransactionManager().begin();
//			} catch (NotSupportedException | SystemException e1) {
//				throw new LdoDException("Fail a transaction begin");
//			}

            try {

                // verify here if the line tweet ID is bigger than the last twitter id in the
                // archive

                createTweet(line);
            } catch (ParseException e1) {
                logger.debug("Miss the creation of a tweet due to the parse of some of its data");
            }

//			try {
//				FenixFramework.getTransactionManager().commit();
//			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
//					| HeuristicRollbackException | SystemException e) {
//				logger.debug("Miss the creation of a tweet due to the info it contains");
//
//			}
        }
        bufferedReader.close();
    }

    @Atomic(mode = TxMode.WRITE)
    private void createTweet(String line) throws ParseException {
        VirtualModule virtualModule = VirtualModule.getInstance();
        JSONObject obj = (JSONObject) new JSONParser().parse(line);

        // if tweets set does not contain current tweet in json file
        if (!virtualModule.checkIfTweetExists((long) obj.get("tweetID"))) {
            // remove emojis, etc
            String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
            Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher((String) obj.get("text"));
            String tweetTextSubstring = matcher.replaceAll("");

            // removing "http" from tweet text
            if (tweetTextSubstring.contains("http")) {
                int httpIndex = tweetTextSubstring.indexOf("http");
                tweetTextSubstring = tweetTextSubstring.substring(0, httpIndex);
            }

            if (!tweetTextSubstring.equals("")) {
                TwitterCitation twitterCitation = null;
                boolean isRetweet = false;
                long originalTweetID = -1L;
                // new JSON files contain this field
                if (obj.containsKey("originalTweetID")) {

                    originalTweetID = (long) obj.get("originalTweetID");
                    isRetweet = (boolean) obj.get("isRetweet");
                    // tweet is a retweet
                    if (isRetweet) {
                        twitterCitation = virtualModule.getTwitterCitationByTweetID((long) obj.get("originalTweetID"));

                    }
                    // tweet is not a retweet
                    else {
                        twitterCitation = virtualModule.getTwitterCitationByTweetID((long) obj.get("tweetID"));
                    }
                }
                // old JSON files
                else {
                    twitterCitation = virtualModule.getTwitterCitationByTweetID((long) obj.get("tweetID"));
                }

                // we only create Tweets that have a Twitter Citation with InfoRange associated
                if (twitterCitation != null && !twitterCitation.getInfoRangeDtoSet().isEmpty()) {
                    // Create tweet
                    matcher = pattern.matcher((String) obj.get("location"));
                    String cleanTweetLocation = matcher.replaceAll("");

                    matcher = pattern.matcher((String) obj.get("country"));
                    String cleanTweetCountry = matcher.replaceAll("");

                    new Tweet(virtualModule, (String) obj.get("tweetURL"), (String) obj.get("date"), tweetTextSubstring,
                            (long) obj.get("tweetID"), cleanTweetLocation, cleanTweetCountry,
                            (String) obj.get("username"), (String) obj.get("profURL"), (String) obj.get("profImg"),
                            originalTweetID, isRetweet, twitterCitation);
                }

            }

        }
    }
}
