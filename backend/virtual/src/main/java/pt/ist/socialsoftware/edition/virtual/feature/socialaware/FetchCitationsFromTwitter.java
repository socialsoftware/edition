package pt.ist.socialsoftware.edition.virtual.feature.socialaware;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;

import pt.ist.socialsoftware.edition.notification.utils.PropertiesManager;
import twitter4j.*;
import twitter4j.Query.ResultType;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FetchCitationsFromTwitter {

    private final Logger logger = LoggerFactory.getLogger(FetchCitationsFromTwitter.class);
    private final int TWEETS_PER_QUERY = 100;
    private final int MAX_QUERIES = 180; // Queries = pages obtained (máximo empírico até agora foi 100 páginas, máximo
    // = 180)
    private final Map<String, String> TERMS_MAP = createTermsMap();

    @Atomic
    public void fetch() throws IOException {
        // Twitter twitter = getTwitterinstance(); //uses configuration builder
        Twitter twitter = new TwitterFactory().getInstance(); // uses twitter4j.properties

        this.logger.debug("Beginning of Fetch Citations");

        for (String term : this.TERMS_MAP.keySet()) {
            String fileName = this.TERMS_MAP.get(term);

            int numTweets = 0;
            long maxID = -1;

            BufferedWriter bw = null;
            FileWriter fw = null;
            File file;
            try {
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String exportDir = PropertiesManager.getProperties().getProperty("social.aware.dir");
                file = new File(exportDir + "twitter-" + fileName + "-" + timeStamp + ".json");

                fw = new FileWriter(file);
                bw = new BufferedWriter(fw);

                // This returns all the various rate limits in effect for us with the Twitter
                // API
                Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
                // This finds the rate limit specifically for doing the search API call we use
                // in this program
                RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
                // System.out.printf("You have %d calls remaining out of %d, Limit resets in %d
                // seconds (= %f minutes)\n",
                // searchTweetsRateLimit.getRemaining(), searchTweetsRateLimit.getLimit(),
                // searchTweetsRateLimit.getSecondsUntilReset(),
                // searchTweetsRateLimit.getSecondsUntilReset() / 60.0);

                // This is the loop that retrieve multiple blocks of tweets from Twitter
                for (int queryNumber = 0; queryNumber < this.MAX_QUERIES; queryNumber++) {
                    // System.out.printf("\n\n!!! Starting loop %d\n\n", queryNumber);

                    // Do we need to delay because we've already hit our rate limits?
                    if (searchTweetsRateLimit.getRemaining() == 0) {
                        // Yes we do, unfortunately ...
                        // System.out.printf("!!! Sleeping for %d seconds due to rate limits\n",
                        // searchTweetsRateLimit.getSecondsUntilReset());

                        // If you sleep exactly the number of seconds, you can make your query a bit too
                        // early
                        // and still get an error for exceeding rate limitations
                        //
                        // Adding two seconds seems to do the trick. Sadly, even just adding one second
                        // still triggers a
                        // rate limit exception more often than not. I have no idea why, and I know from
                        // a Comp Sci
                        // standpoint this is really bad, but just add in 2 seconds and go about your
                        // business. Or else.
                        try {
                            Thread.sleep((searchTweetsRateLimit.getSecondsUntilReset() + 2) * 1000l);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Query q = new Query(term); // SearchProcessor for tweets that contains this term
                    q.setCount(this.TWEETS_PER_QUERY); // How many tweets, max, to retrieve
                    q.setResultType(ResultType.recent); // Get all tweets
                    q.setLang("pt");

                    // If maxID is -1, then this is our first call and we do not want to tell
                    // Twitter what the maximum
                    // tweet id is we want to retrieve. But if it is not -1, then it represents the
                    // lowest tweet ID
                    // we've seen, so we want to start at it-1 (if we start at maxID, we would see
                    // the lowest tweet
                    // a second time...
                    if (maxID != -1) {
                        q.setMaxId(maxID - 1);
                    }

                    // This actually does the search on Twitter and makes the call across the
                    // network
                    QueryResult r = twitter.search(q);

                    // If there are NO tweets in the result set, it is Twitter's way of telling us
                    // that there are no
                    // more tweets to be retrieved. Remember that Twitter's search index only
                    // contains about a week's
                    // worth of tweets, and uncommon search terms can run out of week before they
                    // run out of tweets
                    if (r.getTweets().size() == 0) {
                        break; // Nothing? We must be done
                    }

                    // loop through all the tweets and process them. In this sample program, we just
                    // print them
                    // out, but in a real application you might save them to a database, a CSV file,
                    // do some
                    // analysis on them, whatever...
                    // Loop through all the tweets...
                    for (Status s : r.getTweets()) {
                        // Increment our count of tweets retrieved
                        numTweets++;

                        // Keep track of the lowest tweet ID. If you do not do this, you cannot retrieve
                        // multiple
                        // blocks of tweets...
                        if (maxID == -1 || s.getId() < maxID) {
                            maxID = s.getId();
                        }

                        // Do something with the tweet....
                        String text = null;
                        if (s.getRetweetedStatus() != null) {
                            text = s.getRetweetedStatus().getText();
                        } else {
                            text = s.getText();
                        }

                        Place place = s.getPlace();

                        String country = "unknown";
                        if (place != null && !place.getCountry().equals("")) {
                            country = place.getCountry();
                        }

                        String location = s.getUser().getLocation();
                        if (location.equals("")) {
                            location = "unknown";
                        }

                        String username = s.getUser().getScreenName();
                        String tweetURL = "https://twitter.com/" + username + "/status/" + s.getId();
                        String profURL = "https://twitter.com/" + username;

                        String profImg = s.getUser().getBiggerProfileImageURL();

                        String formatedDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(s.getCreatedAt());

                        long tweetID = s.getId();

                        boolean isRetweet = s.isRetweet();
                        boolean isRetweeted = s.isRetweeted();
                        int retweetCount = s.getRetweetCount();

                        long originalTweetID = -1L;
                        long currentUserRetID = -1L;
                        if (isRetweet) {
                            originalTweetID = s.getRetweetedStatus().getId();
                            currentUserRetID = s.getCurrentUserRetweetId();
                        }

                        // Writing in json file - JSON version
                        JSONObject obj = new JSONObject();
                        obj.put("date", formatedDate);
                        obj.put("username", username);
                        obj.put("tweetID", tweetID);
                        obj.put("text", text);
                        obj.put("country", country);
                        obj.put("location", location);
                        obj.put("tweetURL", tweetURL);
                        obj.put("profURL", profURL);
                        obj.put("profImg", profImg);

                        obj.put("isRetweet", isRetweet);
                        obj.put("isRetweeted", isRetweeted);
                        obj.put("retweetCount", retweetCount);
                        obj.put("originalTweetID", originalTweetID);
                        obj.put("currentUserRetID", currentUserRetID);

                        bw.write(obj.toString());
                        bw.write("\n");
                    }

                    // As part of what gets returned from Twitter when we make the search API call,
                    // we get an updated
                    // status on rate limits. We save this now so at the top of the loop we can
                    // decide whether we need
                    // to sleep or not before making the next call.
                    searchTweetsRateLimit = r.getRateLimitStatus();
                }
                this.logger.debug("Number of tweets retrieved: " + numTweets);
                System.out.printf("You have %d calls remaining out of %d, Limit resets in %d seconds (= %f minutes)\n",
                        searchTweetsRateLimit.getRemaining(), searchTweetsRateLimit.getLimit(),
                        searchTweetsRateLimit.getSecondsUntilReset(),
                        searchTweetsRateLimit.getSecondsUntilReset() / 60.0);

                this.logger.debug("Acabei de preencher o ficheiro: " + "twitter-" + fileName + "-" + timeStamp + ".json");

                bw.close();
                fw.close();

            } catch (IOException ioE) {
                ioE.printStackTrace();
            } catch (TwitterException te) {
                te.printStackTrace();
            }
        }
        this.logger.debug("End of Fetch Citations");
    }

    // may be useful
    public String getTweetInfoInStringFormat(Status s) {
        String toWrite = "";

        String text = null;
        if (s.getRetweetedStatus() != null) {
            text = s.getRetweetedStatus().getText();
        } else {
            text = s.getText();
        }

        Place place = s.getPlace();

        String country = "unknown";
        if (place != null && !place.getCountry().equals("")) {
            country = place.getCountry();
        }

        String location = s.getUser().getLocation();
        if (location.equals("")) {
            location = "unknown";
        }

        String username = s.getUser().getScreenName();
        String tweetURL = "https://twitter.com/" + username + "/status/" + s.getId();
        String profURL = "https://twitter.com/" + username;
        String profImg = s.getUser().getBiggerProfileImageURL();

        String formatedDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(s.getCreatedAt());

        long tweetID = s.getId();

        boolean isRetweet = s.isRetweet();
        boolean isRetweeted = s.isRetweeted();
        int retweetCount = s.getRetweetCount();

        long originalTweetID = -1L;
        long currentUserRetID = -1L;
        if (isRetweet) {
            originalTweetID = s.getRetweetedStatus().getId();
            currentUserRetID = s.getCurrentUserRetweetId();
        }

        toWrite = "\t At " + formatedDate + ", @" + username + " (id: " + tweetID + ")" + "\n" + "said: " + text + "\n"
                + "country: " + country + "\n" + "location: " + location + "\n" + "tweet URL: " + tweetURL + "\n"
                + "profile URL: " + profURL + "\n" + "profile Picture: " + profImg + "\n" + "isRetweet: " + isRetweet
                + "\n" + "isRetweeted: " + isRetweeted + "\n" + "retweetCount: " + retweetCount + "\n"
                + "originalTweetID: " + originalTweetID + "\n" + "currentUserRetID: " + currentUserRetID + "\n"
                + "############################" + "\n";

        return toWrite;
    }

    public Map<String, String> createTermsMap() {
        Map<String, String> termsMap = new HashMap<>();
        termsMap.put("Livro do Desassossego", "livro");
        termsMap.put("Fernando Pessoa", "fp");
        termsMap.put("Bernardo Soares", "bernardo");
        termsMap.put("Vicente Guedes", "vicente");
        return termsMap;
    }

    public Status getTweetById(long id, Twitter t) {
        Status s = null;
        try {
            s = t.showStatus(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return s;
    }

    public Twitter getTwitterinstance() {
        /**
         * if not using properties file, we can set access token by following way
         */
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(false).setOAuthConsumerKey("ije0NGcyFrXfDScLeCbwz2GQt")
                .setOAuthConsumerSecret("AibLMO2mbyFeUjyyjDxH1aftDoJjOF1UZXU8OuM76dDuJ3stdC")
                .setOAuthAccessToken("922101092938342400-IF3ButS0cnh76TsOiBhr8aZopVHQkTv")
                .setOAuthAccessTokenSecret("7boYO5EISBvC2WfWBeuEIgqEOEvLWDGTUgGsG2btGL2cx").setTweetModeExtended(true)
                .setPrettyDebugEnabled(false);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        return twitter;
    }
}
