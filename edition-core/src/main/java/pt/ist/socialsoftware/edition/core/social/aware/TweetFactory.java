package pt.ist.socialsoftware.edition.core.social.aware;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.core.domain.FragInter;
import pt.ist.socialsoftware.edition.core.domain.Fragment;
import pt.ist.socialsoftware.edition.core.domain.LdoD;
import pt.ist.socialsoftware.edition.core.domain.Tweet;
import pt.ist.socialsoftware.edition.core.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.core.search.IgnoreDiacriticsAnalyzer;
import pt.ist.socialsoftware.edition.core.utils.PropertiesManager;

public class TweetFactory {
	private static Logger logger = LoggerFactory.getLogger(TweetFactory.class);
	
	//just for checking score in cited fragments
	private static BufferedWriter bw ;
	private static FileWriter fw;
	private static File toWriteFile;
	
	 public static void logger(Object toPrint) {
	    System.out.println(toPrint);
	 }
	
	 public TweetFactory() throws IOException {	
		//just for writing tweet objects in a file 
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		toWriteFile = new File("C:/Users/dnf_o/projetoTese/ldod/social/tweetObjects/" + "tweets" + "-" + timeStamp + ".txt");
		fw = new FileWriter(toWriteFile);
		bw = new BufferedWriter(fw);
	 }
	
	
	@Atomic
	public void create() throws IOException {
		logger("STARTED TWEET FACTORY!!!");
		LdoD ldoD = LdoD.getInstance();

		File folder = new File(PropertiesManager.getProperties().getProperty("social.aware.dir"));
		for (File fileEntry : folder.listFiles()) {
			logger("início de uma iteração!");
			bw.write("+++++++++++++++++++++++++++++++++++ JSON ++++++++++++++++++++++++++++++++++++");
			bw.write("\n");
	    	bw.write(fileEntry.getName());
			bw.write("\n");
			
			try {

			    JSONObject obj = new JSONObject();
			    String line = null;
		        
			    BufferedReader bufferedReader = new BufferedReader(new FileReader(fileEntry));
		        
			    //arranjar outra maneira para não criar Tweets repetidos sem ter de usar o tempMaxID
			    //criar uma relação entre LdoD e Tweet de maneira a q o LdoD tenha um set composto por todos os tweets
			    //como os ficheiros antigos são lidos antes
			    //ao percorrer um ficheiro .json, mal encontre um tweet id q já exista posso dar break, sair do while,
			    //e passar ao próximo ficheiro .json
			    
			    //Nota: acho q afinal não posso dar logo break como dava no CitationDecter
			    //tenho mesmo de percorrer os ficheiros .json até ao fim e vou a cada linha ver se o tweetID já existe
			    //é mais labrego mas garanto q vejo tudo.
			    //o break stressa em casos em q o mesmo tweet id existe para diferentes keywords, seria um break mt precoce caso
			    //um destes tweets fosse encontrado. o resto do ficheiro não seria processado e não faria qq sentido dar skip do resto do ficheiro

			    //Nota2: acho q esta lógica teria sido mt mais fácil do q a q adotei no CitationDecter ...
			    //talvez nem fosse preciso criar a classe LastTwitterID ... 
			    
			    //Nota3: afinal não! dá jeito a classe LastTwitterID ...  pq aumenta mt a eficiência, não temos
			    //q andar a ler o ficheiro json todo

			    
			    while((line = bufferedReader.readLine()) != null) {

			    	obj = (JSONObject) new JSONParser().parse(line);
			    			    	
			    	//if tweets set does not contain current tweet in json file
			    	if(!ldoD.checkIfTweetExists((long)obj.get("tweetID"))) {	

						String tweetText = (String)obj.get("text");
						String tweetTextSubstring = tweetText; //caso não tenha o "http"
						
						//removing "http" from tweet text
						if(tweetText.contains("http")) {
							int httpIndex = tweetText.indexOf("http");
							tweetTextSubstring = tweetText.substring(0, httpIndex);
						}
						
						if(!tweetTextSubstring.equals("")) {
						
							bw.write("\n");
							bw.write("Date: " + (String)obj.get("date"));
							bw.write("\n");
							bw.write("Tweet ID: " + (long)obj.get("tweetID"));
							bw.write("\n");
							bw.write("JSON Text: " + tweetTextSubstring);
							bw.write("\n");
							
							
							//****************        Obtain Twitter Citation       **********************************/
							
							TwitterCitation twitterCitation = null;
							boolean isRetweet = false;
							long originalTweetID = -1L;
							//novos ficheiros JSON contêm este field
							if(obj.containsKey("originalTweetID")) {
								bw.write("Original ID: " + (long)obj.get("originalTweetID"));
								bw.write("\n");
								bw.write("Retweet: " + (boolean)obj.get("isRetweet"));
								bw.write("\n");
								
								originalTweetID = (long)obj.get("originalTweetID");
								isRetweet = (boolean)obj.get("isRetweet");
								//o tweet é um retweet
								if(isRetweet) {
									twitterCitation = ldoD.getTwitterCitationByTweetID((long)obj.get("originalTweetID"));
									
								}
							}
							//antigos ficheiros JSON ou simplesmente novos ficheiros em q o tweet é um tweet original
							else {
								twitterCitation = ldoD.getTwitterCitationByTweetID((long)obj.get("tweetID"));
							}
							bw.write("CREATED A NEW TWEET!!");
							bw.write("\n");
							//Create tweet
							new Tweet(ldoD, (String)obj.get("tweetURL"), (String)obj.get("date"), tweetTextSubstring, 
									(long)obj.get("tweetID"), (String)obj.get("location"), 
									(String)obj.get("country"), (String)obj.get("username"), (String)obj.get("profURL"), 
									(String)obj.get("profImg"), originalTweetID, isRetweet, twitterCitation);
							
							if(twitterCitation != null) {
								bw.write("Twitter Citation - Frag Text: " + twitterCitation.getFragText());
								bw.write("\n");
								bw.write("Twitter Citation - Tweet ID: " + twitterCitation.getTweetID());
								bw.write("\n");
							}
							else {
								bw.write("Twitter Citation: NULL");
								bw.write("\n");
							}
						}
						
			            bw.write("-------------------------------- NEXT!!!!!!!!!!!!!!!!!! -----------------------------------------");
			            bw.write("\n");
		        	}
		        }//chaveta do while
		    bufferedReader.close();		        
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
		}//chaveta do for		
		bw.close();
		fw.close();
		logger("FINISHED TWEET FACTORY!!!");
	}
}
