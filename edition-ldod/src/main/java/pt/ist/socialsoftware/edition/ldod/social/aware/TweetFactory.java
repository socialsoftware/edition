package pt.ist.socialsoftware.edition.ldod.social.aware;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.Tweet;
import pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

public class TweetFactory {
	private static Logger logger = LoggerFactory.getLogger(TweetFactory.class);

	public void create() throws IOException {
		logger.debug("STARTED TWEET FACTORY!!!");

		File folder = new File(PropertiesManager.getProperties().getProperty("social.aware.dir"));
		// get just files, not directories
		File[] files = folder.listFiles((FileFilter) FileFileFilter.FILE);
		Arrays.sort(files, NameFileComparator.NAME_COMPARATOR);

		for (File fileEntry : files) {
			logger.debug("JSON file name: " + fileEntry.getName());
			fileTweetCreation(fileEntry);

		}
		logger.debug("FINISHED TWEET FACTORY!!!");
	}

	private void fileTweetCreation(File fileEntry) throws FileNotFoundException, IOException {
		String line = null;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fileEntry));

		while ((line = bufferedReader.readLine()) != null) {
			try {
				FenixFramework.getTransactionManager().begin();
			} catch (NotSupportedException | SystemException e1) {
				throw new LdoDException("Fail a transaction begin");
			}

			try {

				// verify here if the line tweet ID is bigger than the last twitter id in the
				// archive

				createTweet(line);
			} catch (ParseException e1) {
				// logger.debug("Miss the creation of a tweet due to the parse of some of its
				// data");
			}

			try {
				FenixFramework.getTransactionManager().commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				// logger.debug("Miss the creation of a tweet due to the info it contains");

			}
		}
		bufferedReader.close();
	}

	// @Atomic(mode = TxMode.WRITE)
	private void createTweet(String line) throws ParseException {
		LdoD ldoD = LdoD.getInstance();
		JSONObject obj = (JSONObject) new JSONParser().parse(line);

		// if tweets set does not contain current tweet in json file
		if (!ldoD.checkIfTweetExists((long) obj.get("tweetID"))) {
			String tweetText = (String) obj.get("text");
			String tweetTextSubstring = tweetText; // caso não tenha o "http"

			// removing "http" from tweet text
			if (tweetText.contains("http")) {
				int httpIndex = tweetText.indexOf("http");
				tweetTextSubstring = tweetText.substring(0, httpIndex);
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
						twitterCitation = ldoD.getTwitterCitationByTweetID((long) obj.get("originalTweetID"));

					}
					// tweet is not a retweet
					else {
						twitterCitation = ldoD.getTwitterCitationByTweetID((long) obj.get("tweetID"));
					}
				}
				// old JSON files
				else {
					twitterCitation = ldoD.getTwitterCitationByTweetID((long) obj.get("tweetID"));
				}

				// we only create Tweets that have a Twitter Citation associated
				if (twitterCitation != null) {
					// Create tweet
					// logger.debug("GOING TO CREATE A TWEET!!");
					new Tweet(ldoD, (String) obj.get("tweetURL"), (String) obj.get("date"), tweetTextSubstring,
							(long) obj.get("tweetID"), (String) obj.get("location"), (String) obj.get("country"),
							(String) obj.get("username"), (String) obj.get("profURL"), (String) obj.get("profImg"),
							originalTweetID, isRetweet, twitterCitation);
				}

			}

		}
	}
}
