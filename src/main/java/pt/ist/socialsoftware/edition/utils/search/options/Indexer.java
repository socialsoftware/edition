package pt.ist.socialsoftware.edition.utils.search.options;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.visitors.TextFragmentWriter;

public class Indexer {
	private static final String ID = "id";
	private static final String TEXT = "text";

	private static Indexer instance = null;

	private final IndexWriter indexWriter;
	private final Analyzer analyzer;
	private final Directory index;
	private final IndexWriterConfig config;

	private Indexer() throws IOException {

		//analyzer = new StandardAnalyzer(Version.LUCENE_40);
		analyzer = new IgnoreDiacriticsAnalyzer();
		String path = PropertiesManager.getProperties().getProperty("indexer.dir");
		File file = new File(path);

		index = new NIOFSDirectory(file);
		config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
		indexWriter = new IndexWriter(index, config);

	}

	public static Indexer getInstance() throws IOException {
		if(instance == null) {
			instance = new Indexer();
		}
		return instance;
	}

	public void addDocument(FragInter inter) throws IOException {

		TextFragmentWriter writer = new TextFragmentWriter(inter);
		writer.write();
		String id = inter.getExternalId();
		String text = writer.getTranscription();

		Document doc = new Document();
		doc.add(new TextField(TEXT, text, Field.Store.YES));
		doc.add(new StringField(ID, id, Field.Store.YES));

		indexWriter.addDocument(doc);

		indexWriter.commit();
	}

	private List<String> getRetults(String query) throws IOException, ParseException {
		System.out.println("Query: " + query);

		Query q = new QueryParser(Version.LUCENE_40, TEXT, analyzer).parse(query);
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		int hitsPerPage = reader.numDocs();

		System.out.println("Number of Fragments: " + hitsPerPage);
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		List<String> hitList = new ArrayList<String>();

		System.out.println("Fragments Found: " + hits.length);
		for(int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			String id = d.get(ID);
			if(!hitList.contains(id)) {
				hitList.add(id);
			}
		}
		reader.close();
		return hitList;

	}

	public List<String> search(String words) throws ParseException, IOException {
		String query = absoluteSearch(words);
		return getRetults(query);
	}

	public List<String> search(String words, FragInter inter) throws IOException, ParseException {
		String query = absoluteSearch(words);
		query = ID + ":" + inter.getExternalId() + " AND " + query;
		return getRetults(query);
	}

	// Search for fragments with a set of words similar to input
	// Fuzzy set for a minimum edition edition of 1
	private String fuzzySearch(String words) {
		String[] split = words.split(" ");
		int fuzzy = 1;
		String query = "" + split[0] + "~" + fuzzy;
		int len = split.length;

		for(int i = 1; i < len; i++) {
			query += " AND " + split[i] + "~" + fuzzy;
		}
		return query;
	}

	// Search for fragments with a set of equal to inputs
	private String absoluteSearch(String words) {
		String[] split = words.split(" ");
		String query = "" + split[0];
		int len = split.length;

		for(int i = 1; i < len; i++) {
			query += " AND " + split[i];
		}
		return query;
	}
}
