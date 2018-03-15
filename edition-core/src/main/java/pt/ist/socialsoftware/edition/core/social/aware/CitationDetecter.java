package pt.ist.socialsoftware.edition.core.social.aware;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;

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
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.core.search.IgnoreDiacriticsAnalyzer;
import pt.ist.socialsoftware.edition.core.utils.PropertiesManager;

public class CitationDetecter {
	
	private static final String ID = "id";
	private static final String TEXT = "text";
	private static final String REP = "rep";
	
	private final Path docDir;
	private final Analyzer analyzer;
	private final QueryParserBase queryParser;

	public CitationDetecter() {
		String path = PropertiesManager.getProperties().getProperty("indexer.dir");
		this.docDir = Paths.get(path);
		this.analyzer = new IgnoreDiacriticsAnalyzer();
		this.queryParser = new QueryParser(TEXT, this.analyzer);
	}
	
	@Atomic
	public void detect() throws IOException {
				
		// 2. query
		String querystr = "Detesto a leitura";
		 //String querystr = "Lucene AND in AND Action";
		Query q = null;
		
		try {
			//q = queryParser.parse(querystr);	
			q = queryParser.parse(absoluteSearch(querystr));		
			
				//q = new QueryParser("title", analyzer).parse(querystr + "~" + "0.5");
				//q = new QueryParser("title", analyzer).parse(querystr);
			
		} catch (org.apache.lucene.queryparser.classic.ParseException e) {
			System.out.println("Query Parser exception!!");
			e.printStackTrace();
		}
		
		//Fuzzy Search
			//q = new FuzzyQuery(new Term("title", querystr));
		
		
		//*******************************************************************************************************************
		// 3. search
		int hitsPerPage = 10;
		Directory directory = new NIOFSDirectory(this.docDir);
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
			//IndexReader reader = DirectoryReader.open(index);
			//IndexSearcher searcher = new IndexSearcher(reader);
		
		
		//Versão da net
		//TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
		//searcher.search(q, collector); //método void (deve servir apenas para dar setup )
		//ScoreDoc[] hits = collector.topDocs().scoreDocs;
		
		
		//Minha versão
		ScoreDoc[] hits = searcher.search(q, hitsPerPage).scoreDocs;

		// 4. display results
		//int docId é o número/id da classe ScoreDoc (começa sempre a 0, 1, 2, ...) - ordem pela qual foram inseridos no Index
		//no código do projeto, ID é mesmo um field tal como o nosso ISBN ou o Text
		System.out.println("Found " + hits.length + " hits.");
		for (int i = 0; i < hits.length; i++) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
				String id = d.get(ID);
			float score = hits[i].score;
			System.out.println((i + 1) + "." + " Text: " + d.get(TEXT) + "\n" +
								"DocID: " + docId + "\t" + "ID: " + id + "\t" + " Score: " + score);
			System.out.println("--------------------------------------------------------\n");
		}
		reader.close();
			directory.close();
		
		/*
		// 0. Specify the analyzer for tokenizing text.
		//    The same analyzer should be used for indexing and searching
		Analyzer analyzer = new StandardAnalyzer();
			//Analyzer analyzerPT = new PortugueseAnalyzer(Version.LUCENE_40);
		
		// 1. create the index
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter w = new IndexWriter(index, config);
		
		addDoc(w, "Lucene in Action", "193398817");
		addDoc(w, "Lucene for Dummies", "55320055Z");
		addDoc(w, "Lucene Managing Gigabytes", "55063554A");
		addDoc(w, "The Art of Computer Science", "9900333X");
		
		w.close();
		
		// 2. query
		String querystr = "Action";
		 //String querystr = "Lucene AND in AND Action";
		Query q = null;
		
		try {
			q = new QueryParser("title", analyzer).parse(querystr + "~" + "0.5");
				//q = new QueryParser("title", analyzer).parse(querystr);
			
		} catch (org.apache.lucene.queryparser.classic.ParseException e) {
			System.out.println("Query Parser exception!!");
			e.printStackTrace();
		}
		
		//Fuzzy Search
			//q = new FuzzyQuery(new Term("title", querystr));
		
		
		//*******************************************************************************************************************
		// 3. search
		int hitsPerPage = 10;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		
		
		//Versão da net
		//TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
		//searcher.search(q, collector); //método void (deve servir apenas para dar setup )
		//ScoreDoc[] hits = collector.topDocs().scoreDocs;
		
		
		//Minha versão
		ScoreDoc[] hits = searcher.search(q, hitsPerPage).scoreDocs;

		// 4. display results
		//int docId é o número/id da classe ScoreDoc (começa sempre a 0, 1, 2, ...)
		//no código do projeto, ID é mesmo um field tal como o nosso ISBN ou o Text
		System.out.println("Found " + hits.length + " hits.");
		for (int i = 0; i < hits.length; i++) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);

			float score = hits[i].score;
			System.out.println((i + 1) + ". ISBN: " + d.get("isbn") + "\t" + " Text: " + d.get("title") + "\n" +
								"ID: " + docId + "\t" + " Score: " + score);
			System.out.println("--------------------------------------------------------\n");
		}

		// reader can only be closed when there
		// is no need to access the documents any more.
		reader.close();
		*/
	}
	
	
	// Search for fragments with a set of equal to inputs
	private String absoluteSearch(String words) {
		String[] split = words.split("\\s+");
		String query = "" + split[0];
		int len = split.length;

		for (int i = 1; i < len; i++) {
			query += " AND " + split[i];
		}
		return query;
	}
	

	//Just for testing, fragment indexation is already done!
	private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
		Document doc = new Document();
		doc.add(new TextField("title", title, Field.Store.YES));
		// use a string field for isbn because we don't want it tokenized
		doc.add(new StringField("isbn", isbn, Field.Store.YES));
		
		w.addDocument(doc);
	}
	
	
}
