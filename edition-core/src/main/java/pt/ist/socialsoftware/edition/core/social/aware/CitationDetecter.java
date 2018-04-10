package pt.ist.socialsoftware.edition.core.social.aware;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.core.domain.Citation;
import pt.ist.socialsoftware.edition.core.domain.LdoD;
import pt.ist.socialsoftware.edition.core.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.core.search.IgnoreDiacriticsAnalyzer;
import pt.ist.socialsoftware.edition.core.utils.PropertiesManager;

public class CitationDetecter {
		
	private static final Map<String, String> TERMS_MAP = createTermsMap();
	
	private static final String ID = "id";
	private static final String TEXT = "text";
	private static final String REP = "rep";
	
	//descomentar!!
	private static Path docDir;
	private static Analyzer analyzer;
	private static QueryParserBase queryParser;
   	
	//public static Path docDir = Paths.get(PropertiesManager.getProperties().getProperty("indexer.dir")); //teste
 
	//apagar estes atributos, só para testar
	/*
	public static Analyzer analyzer = new IgnoreDiacriticsAnalyzer();
	public static IndexWriterConfig config = new IndexWriterConfig(analyzer);
    public static RAMDirectory ramDirectory = new RAMDirectory();
    public static IndexWriter indexWriter;
	*/
    
	private static BufferedWriter bw ;
	private static FileWriter fw;
	private static File toWriteFile;
	
	public CitationDetecter() throws IOException {
		//descomentar!!
		String path = PropertiesManager.getProperties().getProperty("indexer.dir");
		this.docDir = Paths.get(path);
		
		this.analyzer = new IgnoreDiacriticsAnalyzer(); //experimentar outros analyzers e testar
		//this.analyzer = new PortugueseAnalyzer();
		
		this.queryParser = new QueryParser(TEXT, this.analyzer);
		
		//Teste:
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String exportDir = PropertiesManager.getProperties().getProperty("social.aware.dir");
		toWriteFile = new File(exportDir + "citations-" + "generic" + "-" + timeStamp + ".txt");
		fw = new FileWriter(toWriteFile);
		bw = new BufferedWriter(fw);
		
	}
	
	//apagar
	/*
	public static void createIndex() {
	    try {
	            indexWriter = new IndexWriter(ramDirectory, config);    
	            createDoc("Sam", "Lucene index option analyzed vs not analyzed");    
	            createDoc("Sam", "Lucene field boost and query time boost example");    
	            createDoc("Jack", "How to do Lucene search highlight example");
	            createDoc("Smith","Lucene BooleanQuery is depreacted as of 5.3.0" );
	            createDoc("Smith","What is term vector in Lucene" );
	            createDoc("Fernando Pessoa", "Agir,  \r\n" + 
						"Eis a inteligência verdadeira.\r\n" + 
						"Serei o que quiser.\r\n" + 
						"Mas tenho que querer o que for.\r\n" + 
						"O êxito está em ter êxito,\r\n" + 
						"E não em ter condições de êxito.\r\n" + 
						"Condições de palácio\r\n" + 
						"Tem qualquer terra larga,\r\n" + 
						"Mas onde estará o palácio\r\n" + 
						"Se não o fizerem ali?");
	            	
	            indexWriter.close();
	    } catch (IOException | NullPointerException ex) {
	        System.out.println("Exception : " + ex.getLocalizedMessage());
	    } 
	}
	*/
	
	//apagar
	/*
	public static void createDoc(String author, String text) throws IOException {
	    Document doc = new Document();
	    doc.add(new TextField("author", author, Field.Store.YES));
	    doc.add(new TextField("text", text, Field.Store.YES));
	
	    indexWriter.addDocument(doc);
	}
	*/
	
	@Atomic
	public void detect() throws IOException {
		for(String term: TERMS_MAP.keySet()) {
			int count = 0;
			String fileName = TERMS_MAP.get(term);
						
			//createIndex(); //apagar
		    System.out.println("STARTING DEBUG!!");
		    
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String exportDir = PropertiesManager.getProperties().getProperty("social.aware.dir");
			File file = new File(exportDir + "twitter-" + fileName + "-" + timeStamp + ".json");
			
			try {
				List<JSONObject> json = new ArrayList<JSONObject>(); //acho q não é preciso
			    JSONObject obj = new JSONObject();
			    String line = null;
		        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		        while((line = bufferedReader.readLine()) != null) {
		        	obj = (JSONObject) new JSONParser()
							.parse(new InputStreamReader(
									(InputStream) new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)); //a ver se funciona
					json.add(obj); //acho q não é preciso
					
					String tweetText = (String)obj.get("text");
					String tweetTextSubstring = tweetText; //caso não tenha o "http"
					
					if(tweetText.contains("http")) {
						int httpIndex = tweetText.indexOf("http");
						tweetTextSubstring = tweetText.substring(0, httpIndex);
					}
					
					System.out.println(term + ": " + count);
					System.out.println("JSON Text: " + tweetTextSubstring);
					System.out.println("++++++++++++++++++++++++++++++++++++++++");
					bw.write(term + ": " + count);
					bw.write("\n");
					bw.write("JSON Text: " + tweetTextSubstring);
					bw.write("\n");
					bw.write("++++++++++++++++++++++++++++++++++++++++");
					bw.write("\n");
					
					count++;
					
					if(!tweetTextSubstring.equals("")) {
						searchQueryParserJSON(tweetTextSubstring, obj); //descomentar
						//searchQueryParser(absoluteSearch(tweetTextSubstring)); //demasiado rígida, nao funciona no nosso caso
					}
					
					System.out.println("-------------------------------- NEXT!!!!!!!!!!!!!!!!!! -----------------------------------------");
		            bw.write("-------------------------------- NEXT!!!!!!!!!!!!!!!!!! -----------------------------------------");
		            bw.write("\n");
		        }
		    bufferedReader.close();
		    //ramDirectory.close();
		        
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (org.apache.lucene.queryparser.classic.ParseException e) {
				e.printStackTrace();
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}	
		}
		bw.close();
		fw.close();
	}
	
	public static void searchQueryParserJSON(String query, JSONObject obj) throws ParseException, org.apache.lucene.queryparser.classic.ParseException, IOException {
        QueryParser parser = new QueryParser(TEXT, analyzer);
        Query parsedQuery = parser.parse(QueryParser.escape(query)); //escape foi a solução porque ele stressava com o EOF
        
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
			Directory directory = new NIOFSDirectory(docDir); //descomentar
			IndexReader idxReader = DirectoryReader.open(directory);
	        IndexSearcher idxSearcher = new IndexSearcher(idxReader);
	
	        ScoreDoc[] hits = idxSearcher.search(query, hitsPerPage).scoreDocs;
	        /*
	        System.out.println("Found " + hits.length + " hits");
	        for (int i = 0; i < hits.length; i++) {
				int docId = hits[i].doc;
				float score = hits[i].score;

				Document d = idxSearcher.doc(docId);
				System.out.println((i + 1) + "." + " Text: " + d.get(TEXT) + "\n" +
									"DocID: " + docId + "\t" + "FragInter ID: " + d.get(ID) + "\t" + " Score: " + score);
				System.out.println("--------------------------------------------------------\n");
			}
			*/
	        if(hits.length > 0) {
		        int docId = hits[0].doc;
				float score = hits[0].score;
				if(score > 30) {	
					Document d = idxSearcher.doc(docId);
					
						if(hits.length > 1) {
							//Same frag
					        boolean sameFrag = false;
							if(LdoD.getInstance().getFragmentByXmlId(d.get(ID))
									== LdoD.getInstance().getFragmentByXmlId(idxSearcher.doc(hits[1].doc).get(ID))) {
								sameFrag = true;
							};
							System.out.println(sameFrag);
						}
					
					System.out.println("Text: " + d.get(TEXT) + "\n" +
										"DocID: " + docId + "\t" + "FragInter ID: " + d.get(ID) + "\t" + " Score: " + score);
					System.out.println("--------------------------------------------------------\n");
					bw.write("Text: " + d.get(TEXT) + "\n" +
							"DocID: " + docId + "\t" + "FragInter ID: " + d.get(ID) + "\t" + " Score: " + score);
					bw.write("\n");
					bw.write("--------------------------------------------------------\n");
					bw.write("\n");
					
					/*
					//create citations
					TwitterCitation c = new TwitterCitation();
					c.setSourceLink((String)obj.get("tweetURL"));
					c.setDate((String)obj.get("date"));
					c.setFragText(d.get(TEXT));
					c.setTweetText((String)obj.get("text"));
					c.setTweetID((String)obj.get("tweetID"));
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
			directory.close(); //descomentar!!
			//idxReader.close(); 
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 
	}
	
	public static void searchQueryParser(String query) throws ParseException, org.apache.lucene.queryparser.classic.ParseException, IOException {
        QueryParser parser = new QueryParser(TEXT, analyzer);
        Query parsedQuery = parser.parse(QueryParser.escape(query)); //escape foi a solução porque ele stressava com o EOF
        
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
			Directory directory = new NIOFSDirectory(docDir); //descomentar
			IndexReader idxReader = DirectoryReader.open(directory);
	        IndexSearcher idxSearcher = new IndexSearcher(idxReader);
	
	        ScoreDoc[] hits = idxSearcher.search(query, hitsPerPage).scoreDocs;
	        /*
	        System.out.println("Found " + hits.length + " hits");
	        for (int i = 0; i < hits.length; i++) {
				int docId = hits[i].doc;
				float score = hits[i].score;

				Document d = idxSearcher.doc(docId);
				System.out.println((i + 1) + "." + " Text: " + d.get(TEXT) + "\n" +
									"DocID: " + docId + "\t" + "FragInter ID: " + d.get(ID) + "\t" + " Score: " + score);
				System.out.println("--------------------------------------------------------\n");
			}
			*/
	        if(hits.length > 0) {
		        int docId = hits[0].doc;
				float score = hits[0].score;
				if(score > 30) {	
					Document d = idxSearcher.doc(docId);
					
						if(hits.length > 1) {
							//Same frag
					        boolean sameFrag = false;
							if(LdoD.getInstance().getFragmentByXmlId(d.get(ID))
									== LdoD.getInstance().getFragmentByXmlId(idxSearcher.doc(hits[1].doc).get(ID))) {
								sameFrag = true;
							};
							System.out.println(sameFrag);
						}
					
					System.out.println("Text: " + d.get(TEXT) + "\n" +
										"DocID: " + docId + "\t" + "FragInter ID: " + d.get(ID) + "\t" + " Score: " + score);
					System.out.println("--------------------------------------------------------\n");
					bw.write("Text: " + d.get(TEXT) + "\n" +
							"DocID: " + docId + "\t" + "FragInter ID: " + d.get(ID) + "\t" + " Score: " + score);
					bw.write("\n");
					bw.write("--------------------------------------------------------\n");
					bw.write("\n");
					
					//create citations
					TwitterCitation c = new TwitterCitation();
					
				}
				else {
					bw.write("SCORE IS TOO LOW: " + score);
					bw.write("\n");
					bw.write("--------------------------------------------------------\n");
					bw.write("\n");
				}
	        }	
			directory.close(); //descomentar!!
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
