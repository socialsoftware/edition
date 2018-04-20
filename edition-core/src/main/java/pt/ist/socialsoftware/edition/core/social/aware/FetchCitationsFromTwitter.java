package pt.ist.socialsoftware.edition.core.social.aware;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.core.utils.PropertiesManager;
import twitter4j.Place;
import twitter4j.Query;
import twitter4j.Query.ResultType;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class FetchCitationsFromTwitter {
	
	private static Logger logger = LoggerFactory.getLogger(FetchCitationsFromTwitter.class);
	private static final int TWEETS_PER_QUERY = 100;
	private static final int MAX_QUERIES = 180; //Queries = pages obtained (máximo empírico até agora foi 100 páginas, máximo = 180)
	private static final Map<String, String> TERMS_MAP = createTermsMap();
	
	@Atomic
	public void fetch() throws IOException {
		Twitter twitter = getTwitterinstance();
		
		for(String term: TERMS_MAP.keySet()) {
			String fileName = TERMS_MAP.get(term);
			System.out.println(fileName);
					
			/****************************** Writing tweets to file *****************************/
			int numTweets = 0;
			long maxID = -1;
	
			//String toWrite = "";
			String formatedDate = "";
			
			BufferedWriter bw = null;
			FileWriter fw = null;
		    File file;
			try{
				
				String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				String exportDir = PropertiesManager.getProperties().getProperty("social.aware.dir");
				file = new File(exportDir + "twitter-" + fileName + "-" + timeStamp + ".json");
				
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
				
				//This returns all the various rate limits in effect for us with the Twitter API
				Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
				//	This finds the rate limit specifically for doing the search API call we use in this program
				RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
				System.out.printf("You have %d calls remaining out of %d, Limit resets in %d seconds (= %f minutes)\n",
						searchTweetsRateLimit.getRemaining(),
						searchTweetsRateLimit.getLimit(),
						searchTweetsRateLimit.getSecondsUntilReset(),
						(searchTweetsRateLimit.getSecondsUntilReset())/60.0);
				
				int numElli = 0;
				//	This is the loop that retrieve multiple blocks of tweets from Twitter
				for (int queryNumber = 0; queryNumber < MAX_QUERIES; queryNumber++) {
					System.out.printf("\n\n!!! Starting loop %d\n\n", queryNumber);
	
					//	Do we need to delay because we've already hit our rate limits?
					if (searchTweetsRateLimit.getRemaining() == 0) {
						//	Yes we do, unfortunately ...
						System.out.printf("!!! Sleeping for %d seconds due to rate limits\n", searchTweetsRateLimit.getSecondsUntilReset());
	
						//	If you sleep exactly the number of seconds, you can make your query a bit too early
						//	and still get an error for exceeding rate limitations
						//
						// 	Adding two seconds seems to do the trick. Sadly, even just adding one second still triggers a
						//	rate limit exception more often than not.  I have no idea why, and I know from a Comp Sci
						//	standpoint this is really bad, but just add in 2 seconds and go about your business.  Or else.
						try {
							Thread.sleep((searchTweetsRateLimit.getSecondsUntilReset()+2) * 1000l);
						} catch (InterruptedException e) {
							System.out.println("Entrou na exception da Thread sleep");
							e.printStackTrace();
						}
					}
	
					Query q = new Query(term);			// Search for tweets that contains this term
					q.setCount(TWEETS_PER_QUERY);				// How many tweets, max, to retrieve
					q.setResultType(ResultType.recent);						// Get all tweets
					q.setLang("pt");						
	
					//	If maxID is -1, then this is our first call and we do not want to tell Twitter what the maximum
					//	tweet id is we want to retrieve.  But if it is not -1, then it represents the lowest tweet ID
					//	we've seen, so we want to start at it-1 (if we start at maxID, we would see the lowest tweet
					//	a second time...
					if (maxID != -1) {      
						q.setMaxId(maxID - 1);
					}
	
					//	This actually does the search on Twitter and makes the call across the network
					QueryResult r = twitter.search(q);
	
					//	If there are NO tweets in the result set, it is Twitter's way of telling us that there are no
					//	more tweets to be retrieved.  Remember that Twitter's search index only contains about a week's
					//	worth of tweets, and uncommon search terms can run out of week before they run out of tweets
					if (r.getTweets().size() == 0) { 
						System.out.println("fiz break!");
						break;			// Nothing? We must be done
					}
					
					//	loop through all the tweets and process them.  In this sample program, we just print them
					//	out, but in a real application you might save them to a database, a CSV file, do some
					//	analysis on them, whatever...
					//  Loop through all the tweets...
					for (Status s: r.getTweets()) {
						//	Increment our count of tweets retrieved
						numTweets++;
	
						//	Keep track of the lowest tweet ID.  If you do not do this, you cannot retrieve multiple
						//	blocks of tweets...
						if (maxID == -1 || s.getId() < maxID) {
							maxID = s.getId();
						}
						
						//	Do something with the tweet....
						String text = null;
						if(s.getRetweetedStatus() != null) 
							text = s.getRetweetedStatus().getText();
						else
							text = s.getText();
						
						//debug truncated - Elli
						if (text.contains("\u2026")) {
							numElli++;
							System.err.println(queryNumber);
							System.err.printf("--------------------At %s, @%-20s (id: %d) said:  %s\n",
									  s.getCreatedAt().toString(),
									  s.getUser().getScreenName(),
									  s.getId(),
									  text);
						}
						
						
						Place place = s.getPlace();
						
						String country = "unknown";
						if(place != null && !place.getCountry().equals("")) //this equals solves the case where country comes empty
							country = place.getCountry();
					
						String location = s.getUser().getLocation();
						if(location.equals(""))
							location = "unknown";
						
						String username = s.getUser().getScreenName();
						String tweetURL = "https://twitter.com/" + username + "/status/" + s.getId();
						String profURL = "https://twitter.com/" + username;
						String profImg = s.getUser().getBiggerProfileImageURL();

						formatedDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(s.getCreatedAt());
						
						long tweetID =  s.getId();
						
						/*
						toWrite = "\t At " + formatedDate + ", @" + username 
								+ " (id: " + + tweetID + ")" + "\n" + "said: " + text + "\n"
								+ "country: " + country + "\n"
								+ "location: " + location + "\n"
								+ "tweet URL: " + tweetURL + "\n"
								+ "profile URL: " + profURL + "\n"
								+ "profile Picture: " + profImg + "\n"
								+ "############################" + "\n";
						*/
						
						//Writing in json file - JSON version
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
						
						bw.write(obj.toString());
						bw.write("\n");
						
						System.out.println("####################################");
					}
	
					//	As part of what gets returned from Twitter when we make the search API call, we get an updated
					//	status on rate limits.  We save this now so at the top of the loop we can decide whether we need
					//	to sleep or not before making the next call.
					searchTweetsRateLimit = r.getRateLimitStatus();
				}
				System.out.println("Number of tweets retrieved: " + numTweets);
				System.out.println("Number of tweets elli: " + numElli);
				System.out.printf("You have %d calls remaining out of %d, Limit resets in %d seconds (= %f minutes)\n",
						searchTweetsRateLimit.getRemaining(),
						searchTweetsRateLimit.getLimit(),
						searchTweetsRateLimit.getSecondsUntilReset(),
						(searchTweetsRateLimit.getSecondsUntilReset())/60.0);	
				System.out.println("++++++++++++++++++++++++++++++ OUTRO FICHEIRO ++++++++++++++++++++++++++++++");
				
				bw.close();
				fw.close();
		
			}catch(IOException ioE) {
				ioE.printStackTrace();
				System.out.println("IOException at FetchCitationsFromTwitter!!: ");
			}catch(TwitterException te) {
				te.printStackTrace();
				System.out.println("Failed to search tweets!!: " + te.getMessage());
			}
		}
		logger.debug("End of Fetch Citations");
		
	}
	
	private static Map<String, String> createTermsMap() {
        Map<String,String> termsMap = new HashMap<String,String>();
        termsMap.put("Livro do Desassossego", "livro");
        termsMap.put("Fernando Pessoa", "fp");
        termsMap.put("Bernardo Soares", "bernardo");
        termsMap.put("Vicente Guedes", "vicente");
        return termsMap;
    }
	
	
	//Might be useful
	public static Status getTweetById(String id, Twitter t) {
		Status s = null;
		try {
			s = t.showStatus(Long.parseLong(id));
		} catch (NumberFormatException e) {
			System.out.println("Number Format Exception while getting tweet by id!!!");
			e.printStackTrace();
		} catch (TwitterException e) {
			System.out.println("Twitter Exception while getting tweet by id!!!");
			e.printStackTrace();
		}
		return s;
	}
	
	
	public static Twitter getTwitterinstance() {
		/**
		 * if not using properties file, we can set access token by following way
		 */
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("ije0NGcyFrXfDScLeCbwz2GQt")
		.setOAuthConsumerSecret("AibLMO2mbyFeUjyyjDxH1aftDoJjOF1UZXU8OuM76dDuJ3stdC")
		.setOAuthAccessToken("922101092938342400-IF3ButS0cnh76TsOiBhr8aZopVHQkTv")
		.setOAuthAccessTokenSecret("7boYO5EISBvC2WfWBeuEIgqEOEvLWDGTUgGsG2btGL2cx")
		.setTweetModeExtended(true)
		;
		
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		return twitter;
	}
}
