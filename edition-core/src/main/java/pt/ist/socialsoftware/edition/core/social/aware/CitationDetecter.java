package pt.ist.socialsoftware.edition.core.social.aware;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONObject;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.core.domain.FragInter;
import pt.ist.socialsoftware.edition.core.domain.Fragment;
import pt.ist.socialsoftware.edition.core.domain.HumanAnnotation;
import pt.ist.socialsoftware.edition.core.domain.LastTwitterID;
import pt.ist.socialsoftware.edition.core.domain.LdoD;
import pt.ist.socialsoftware.edition.core.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.core.search.IgnoreDiacriticsAnalyzer;
import pt.ist.socialsoftware.edition.core.utils.PropertiesManager;

public class CitationDetecter {
	
	private static Logger logger = LoggerFactory.getLogger(CitationDetecter.class);
	
	private static final String ID = "id";
	private static final String TEXT = "text";
	
	private static Path docDir;
	private static Analyzer analyzer;
	private static QueryParserBase queryParser;
   	
	//just for checking score in cited fragments
	private static BufferedWriter bw ;
	private static FileWriter fw;
	private static File toWriteFile;
	
	public CitationDetecter() throws IOException {
		String path = PropertiesManager.getProperties().getProperty("indexer.dir");
		docDir = Paths.get(path);
		analyzer = new IgnoreDiacriticsAnalyzer(); //experimentar outros analyzers e testar
		//analyzer = new PortugueseAnalyzer();
		queryParser = new QueryParser(TEXT, analyzer);
		
		//just for checking score in cited fragments
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		//String exportDir = PropertiesManager.getProperties().getProperty("social.aware.dir");
		toWriteFile = new File("C:/Users/dnf_o/projetoTese/ldod/social/awareGeneric/" + "citations-" + "generic" + "-" + timeStamp + ".txt");
		fw = new FileWriter(toWriteFile);
		bw = new BufferedWriter(fw);
	}

	@Atomic
	public void detect() throws IOException {		
		LdoD.getInstance().getLastTwitterID().resetTwitterIDS(); //serve só para testar melhor pq dá reset ao id na base de dados
		File folder = new File(PropertiesManager.getProperties().getProperty("social.aware.dir"));
	    for (File fileEntry : folder.listFiles()) {
			System.out.println("STARTING CITATION DETECTER!!");
			System.out.println("+++++++++++++++++++++++++++++++++++ JSON ++++++++++++++++++++++++++++++++++++");
	    	System.out.println(fileEntry.getName());
	    	bw.write("+++++++++++++++++++++++++++++++++++ JSON ++++++++++++++++++++++++++++++++++++");
			bw.write("\n");
	    	bw.write(fileEntry.getName());
			bw.write("\n");

			try {
			    JSONObject obj = new JSONObject();
			    String line = null;
		        
			    BufferedReader bufferedReader = new BufferedReader(new FileReader(fileEntry));
		        
			    //criar um tempMaxID que guarda o valor de LdoD.getInstance().getLastTwitterID()
			    //pq é preciso darmos set na base de dados do valor antes do while, pq vem logo na primeira linha
			    long tempMaxID = LdoD.getInstance().getLastTwitterID().getLastTwitterID(fileEntry.getName());
			    
				int lineNum = 0;
			    while((line = bufferedReader.readLine()) != null) {
			    	obj = (JSONObject) new JSONParser().parse(line);
			    	if(lineNum == 0) { 
			    		//prints e writes para debug
			    		System.out.println("----------- PRIMEIRA LINHA ---------------");
						System.out.println("LdoD last twitter ID: " + LdoD.getInstance().
								getLastTwitterID().getLastTwitterID(fileEntry.getName()));
				        System.out.println("Date: " + (String)obj.get("date"));
					    System.out.println("----------- RESTANTES LINHAS ---------------");

				        bw.write("----------- PRIMEIRA LINHA ---------------");
						bw.write("\n");
					    bw.write("LdoD last twitter ID: " + LdoD.getInstance().
					    		getLastTwitterID().getLastTwitterID(fileEntry.getName()));
						bw.write("\n");
					    bw.write("Date: " + (String)obj.get("date"));
						bw.write("\n");
						bw.write("----------- RESTANTES LINHAS ---------------");
						bw.write("\n");
			    		
						if((long)obj.get("tweetID") > LdoD.getInstance().getLastTwitterID().getLastTwitterID(fileEntry.getName())) {
			    			LdoD.getInstance().getLastTwitterID().updateLastTwitterID(fileEntry.getName(),(long)obj.get("tweetID"));
			    		}
		        	}
			    	lineNum++;
			    	
		        	System.out.println("tempMaxID: " + tempMaxID);

		        	if((long)obj.get("tweetID") > tempMaxID) {
						System.out.println("Date: " + (String)obj.get("date"));
		        		
						String tweetText = (String)obj.get("text");
						String tweetTextSubstring = tweetText; //caso não tenha o "http"
						
						//removing "http" from tweet text
						if(tweetText.contains("http")) {
							int httpIndex = tweetText.indexOf("http");
							tweetTextSubstring = tweetText.substring(0, httpIndex);
						}
						
						//System.out.println(term + ": " + count);
						//System.out.println("JSON Text: " + tweetTextSubstring);
						//System.out.println("++++++++++++++++++++++++++++++++++++++++");
						//bw.write(term + ": " + count);
						bw.write("\n");
						bw.write("Date: " + (String)obj.get("date"));
						bw.write("\n");
						bw.write("TweetID: " + (long)obj.get("tweetID"));
						bw.write("\n");
						bw.write("JSON Text: " + tweetTextSubstring);
						bw.write("\n");
						bw.write("++++++++++++++++++++++++++++++++++++++++");
						bw.write("\n");
						
						//count++;
						
						if(!tweetTextSubstring.equals("")) {
							searchQueryParserJSON(tweetTextSubstring, obj);
							//searchQueryParser(absoluteSearch(tweetTextSubstring)); //demasiado rígida, nao funciona no nosso caso
						}
						
						System.out.println("-------------------------------- NEXT!!!!!!!!!!!!!!!!!! -----------------------------------------");
			            bw.write("-------------------------------- NEXT!!!!!!!!!!!!!!!!!! -----------------------------------------");
			            bw.write("\n");
		        	}
		        	else {
		        		break;
		        	}
		        }//chaveta do while
		    bufferedReader.close();		        
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (org.apache.lucene.queryparser.classic.ParseException e) {
				e.printStackTrace();
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}	
		} //chaveta do for
		logger.debug("LdoD BookLastTwitterID:{}", LdoD.getInstance().getLastTwitterID().getBookLastTwitterID()); 
		
		bw.close();
		fw.close();
		
		/*
		for (Fragment frag : LdoD.getInstance().getFragmentsSet()) {
			System.out.println("+++++++++++++++++++++++++++");
			System.out.println(frag.getTitle());
			System.out.println(frag.getExternalId());
		}
		*/
	}
	
	public static void searchQueryParserJSON(String query, JSONObject obj) throws ParseException, org.apache.lucene.queryparser.classic.ParseException, IOException {
        Query parsedQuery = queryParser.parse(QueryParser.escape(query)); //escape foi a solução porque ele stressava com o EOF
        
        System.out.println("ParsedQuery: " + parsedQuery.toString());
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        bw.write("ParsedQuery: " + parsedQuery.toString());
        bw.write("\n");
        bw.write("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        bw.write("\n");
        
        searchIndexAndDisplayResultsJSON(parsedQuery, obj);
    }
	
	
	public static void searchIndexAndDisplayResultsJSON(Query query, JSONObject obj) {
	    try {
	    	int hitsPerPage = 5;
			Directory directory = new NIOFSDirectory(docDir);
			IndexReader idxReader = DirectoryReader.open(directory);
	        IndexSearcher idxSearcher = new IndexSearcher(idxReader);
	
	        ScoreDoc[] hits = idxSearcher.search(query, hitsPerPage).scoreDocs;
	        if(hits.length > 0) {
		        int docId = hits[0].doc;
				float score = hits[0].score;
				if(score > 30) {	
					Document d = idxSearcher.doc(docId);
					bw.write("SCORE IS HIGH ENOUGH: " + score);
					bw.write("\n");
					System.out.println("Text: " + d.get(TEXT) + "\n" +
										"DocID: " + docId + "\t" + "FragInter ID: " + d.get(ID) + "\t" + " Score: " + score);
					System.out.println("--------------------------------------------------------\n");
					bw.write("Text: " + d.get(TEXT) + "\n" +
							"DocID: " + docId + "\t" + "FragInter ID: " + d.get(ID) + "\t" + "Document Class: "  + d.getClass() +
							"\t" + " Score: " + score);
					
					bw.write("\n");
					bw.write("--------------------------------------------------------\n");
					bw.write("\n");
					
					
					//******************************** CREATE CITATIONS ***************************************/
					
					//necessary because the same tweet was collected using different keywords in FetchCitationsFromTwitter class
					//check if twitter ID already exists in the list of Citations
					//if it does idExists=true, therefore we don't create a citation for it!
					Set<TwitterCitation> allTwitterCitations = LdoD.getInstance().getCitationsSet()
							.stream().filter(TwitterCitation.class::isInstance).map(TwitterCitation.class::cast)
							.collect(Collectors.toSet());
					boolean twitterIDExists = false;
					for(TwitterCitation tc: allTwitterCitations) {
						if(tc.getTweetID() == (long)obj.get("tweetID")) {
							twitterIDExists = true;
							//bw.write("TWITTER ID ALREADY EXISTS!!\n");
							break;
						}
					}
					
					//o if(!twitterIDExists) { não deveria vir logo aqui??
					//atualmente estamos a obter o Fragment e a limpar o http do texto antes desta verificação
					//não há razão para isso, estamos a ser pouco eficientes
					
					
					//obtain Fragment
					
					//using xml id
					//bw.write("Fragment itself (using XMLID): " + LdoD.getInstance().getFragmentByXmlId(d.get(ID)));
					//bw.write("\n");
					
					
					//using external id
					FragInter inter = FenixFramework.getDomainObject(d.get(ID));
					bw.write("---------- USING EXTERNAL ID----------------\n");
					Fragment fragment = null;
					for (Fragment frag : LdoD.getInstance().getFragmentsSet()) {
						if (frag == inter.getFragment()) {
							bw.write("Entrei no if do External ID!!");
							bw.write("\n");
							fragment = frag;
							break;
						}
					}
					bw.write("Fragment itself (using ExternalID): " + fragment);
					bw.write("\n");
					if(fragment != null) {
						bw.write("Fragment was not null!!");
						bw.write("\n");
						bw.write("Fragment External ID: " + fragment.getExternalId());
						bw.write("\n");
					}
					
					//Nota: o tweet text que é passado ao construtor tem os https ainda!
					//(String)obj.get("text") - está errado, temos de limpar os https!!
					String tweetText = (String)obj.get("text");
					String tweetTextSubstring = tweetText; //caso não tenha o "http"
					
					//removing "http" from tweet text
					if(tweetText.contains("http")) {
						int httpIndex = tweetText.indexOf("http");
						tweetTextSubstring = tweetText.substring(0, httpIndex);
					}
					//^pôr isto num método à parte
					
					if(!twitterIDExists) {
						new TwitterCitation(LdoD.getInstance(), fragment,
								(String)obj.get("tweetURL"), (String)obj.get("date"),
								d.get(TEXT), tweetTextSubstring, (long)obj.get("tweetID"), (String)obj.get("location"), 
								(String)obj.get("country"), (String)obj.get("username"), (String)obj.get("profURL"), 
								(String)obj.get("profImg"));
					}		
					
					//using setters version
					/*
					c.setSourceLink((String)obj.get("tweetURL"));
					c.setDate((String)obj.get("date"));
					c.setFragText(d.get(TEXT));
					c.setTweetText((String)obj.get("text"));
					c.setTweetID((long)obj.get("tweetID"));
					c.setLocation((String)obj.get("location"));
					c.setCountry((String)obj.get("country"));
					c.setUsername((String)obj.get("username"));
					c.setUserProfileURL((String)obj.get("profURL"));
					c.setUserImageURL((String)obj.get("profImg"));
					*/
					
				}
				else {
					bw.write("SCORE IS TOO LOW: " + score);
					bw.write("\n");
					bw.write("--------------------------------------------------------\n");
					bw.write("\n");
				}
	        }	
			directory.close();
			//idxReader.close(); 
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 
	}
	
	public static void searchQueryParser(String query) throws ParseException, org.apache.lucene.queryparser.classic.ParseException, IOException {
        //QueryParser parser = new QueryParser(TEXT, analyzer);
        Query parsedQuery = queryParser.parse(QueryParser.escape(query)); //escape foi a solução porque ele stressava com o EOF
        
        System.out.println("ParsedQuery: " + parsedQuery.toString());
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        bw.write("ParsedQuery: " + parsedQuery.toString());
        bw.write("\n");
        bw.write("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        bw.write("\n");
        
        searchIndexAndDisplayResults(parsedQuery);
    }
	
	public static void searchIndexAndDisplayResults(Query query) {
	    try {
	    	int hitsPerPage = 5;
			Directory directory = new NIOFSDirectory(docDir); 
			IndexReader idxReader = DirectoryReader.open(directory);
	        IndexSearcher idxSearcher = new IndexSearcher(idxReader);
	
	        ScoreDoc[] hits = idxSearcher.search(query, hitsPerPage).scoreDocs;
	        if(hits.length > 0) {
		        int docId = hits[0].doc;
				float score = hits[0].score;
				if(score > 30) {	
					Document d = idxSearcher.doc(docId);
					System.out.println("Text: " + d.get(TEXT) + "\n" +
										"DocID: " + docId + "\t" + "FragInter ID: " + d.get(ID) + "\t" + " Score: " + score);
					System.out.println("--------------------------------------------------------\n");
					bw.write("Text: " + d.get(TEXT) + "\n" +
							"DocID: " + docId + "\t" + "FragInter ID: " + d.get(ID) + "\t" + " Score: " + score);
					bw.write("\n");
					bw.write("--------------------------------------------------------\n");
					bw.write("\n");
				}
				else {
					bw.write("SCORE IS TOO LOW: " + score);
					bw.write("\n");
					bw.write("--------------------------------------------------------\n");
					bw.write("\n");
				}
	        }	
			directory.close();
			//idxReader.close(); 
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 
	}
	

	// Fuzzy Query - multiple terms! pesquisa tem de ser exata
    public static void searchSpanQuery(String words) {	
    	String[] split = words.split("\\s+");
    	int len = split.length;
    	SpanQuery[] clauses = new SpanQuery[len];
 		for (int i = 0; i < len; i++) {
 	    	clauses[i] = new SpanMultiTermQueryWrapper(new FuzzyQuery(new Term(TEXT, split[i])));
 		}
    	SpanNearQuery query = new SpanNearQuery(clauses, 0, true);
        
    	searchIndexAndDisplayResults(query);
    }
	
    
    // Fuzzy Search - Pesquisa tem de ser exata
    // Search for fragments with a set of words similar to input
 	// Fuzzy set for a minimum edition edition of 1
 	public static void fuzzySearch(String words) throws ParseException, org.apache.lucene.queryparser.classic.ParseException, IOException {
 		String[] split = words.split("\\s+");
 		double fuzzy = 1; //default = 0.5
 		String query = "" + split[0] + "~" + fuzzy;
 		int len = split.length;

 		for (int i = 1; i < len; i++) {
 			query += " AND " + split[i] + "~" + fuzzy;
 		}
 		searchQueryParser(query);
 	}
	
	// Search for fragments with a set of equal to inputs
	public String absoluteSearch(String words) {
		String[] split = words.split("\\s+");
		String query = "" + split[0];
		int len = split.length;

		for (int i = 1; i < len; i++) {
			query += " AND " + split[i];
		}
		return query;
	}
	
	public void searchSingleTerm(String field, String termText){
	    Term term = new Term(field, termText);
	    TermQuery termQuery = new TermQuery(term);
	
	    searchIndexAndDisplayResults(termQuery);
	}
	
	public static Map<String, String> createTermsMap() {
        Map<String,String> termsMap = new HashMap<String,String>();
        termsMap.put("Livro do Desassossego", "livro");
        termsMap.put("Fernando Pessoa", "fp");
        termsMap.put("Bernardo Soares", "bernardo");
        termsMap.put("Vicente Guedes", "vicente");
        return termsMap;
    }
}
