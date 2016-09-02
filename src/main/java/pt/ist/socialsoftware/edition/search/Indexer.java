package pt.ist.socialsoftware.edition.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.socialsoftware.edition.domain.Edition.EditionType;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.visitors.PlainTextFragmentWriter;

public class Indexer {
	private static Logger logger = LoggerFactory.getLogger(Indexer.class);

	private static final String ID = "id";
	private static final String TEXT = "text";
	private final Analyzer analyzer;
	private static final HashMap<String, Map<String, Double>> terms = new HashMap<String, Map<String, Double>>();
	private final QueryParserBase queryParser;
	private final int significativeTerms = 1000;
	private final File file;
	private final IndexWriterConfig config;

	public Indexer() {
		analyzer = new IgnoreDiacriticsAnalyzer();
		String path = PropertiesManager.getProperties().getProperty("indexer.dir");
		file = new File(path);
		queryParser = new QueryParser(TEXT, analyzer);
		config = new IndexWriterConfig(Version.LATEST, analyzer);
	}

	public void addDocument(FragInter inter) throws IOException {
		// IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,
		// analyzer);
		Directory directory = new NIOFSDirectory(file);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		PlainTextFragmentWriter writer = new PlainTextFragmentWriter(inter);
		writer.write();
		String id = inter.getExternalId();
		String text = writer.getTranscription();
		Document doc = new Document();
		FieldType type = new FieldType();
		type.setIndexed(true);
		type.setStored(true);
		type.setStoreTermVectors(true);
		doc.add(new Field(TEXT, text, type));
		doc.add(new StringField(ID, id, Field.Store.YES));
		indexWriter.addDocument(doc);
		indexWriter.commit();
		indexWriter.close();
		directory.close();
	}

	private List<String> getResults(String query) throws IOException, ParseException {
		logger.debug("Query: {}", query);

		Query q = queryParser.parse(query);
		Directory directory = new NIOFSDirectory(file);
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		int hitsPerPage = reader.numDocs();
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		List<String> hitList = new ArrayList<String>();
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			String id = d.get(ID);
			if (!hitList.contains(id)) {
				hitList.add(id);
			}
		}
		reader.close();
		directory.close();
		return hitList;
	}

	public List<String> search(String words) throws ParseException, IOException {
		String query = absoluteSearch(words);
		return getResults(query);
	}

	public List<String> search(String words, FragInter inter) throws IOException, ParseException {
		String query = absoluteSearch(words);
		query = ID + ":" + inter.getExternalId() + " AND " + query;
		return getResults(query);
	}

	// Search for fragments with a set of words similar to input
	// Fuzzy set for a minimum edition edition of 1
	private String fuzzySearch(String words) {
		String[] split = words.split("\\s+");
		int fuzzy = 1;
		String query = "" + split[0] + "~" + fuzzy;
		int len = split.length;

		for (int i = 1; i < len; i++) {
			query += " AND " + split[i] + "~" + fuzzy;
		}
		return query;
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

	public Map<String, Double> getTFIDF(Fragment fragment) throws IOException, ParseException {
		String id = fragment.getExternalId();
		if (terms.containsKey(id)) {
			return terms.get(id);
		}
		Map<String, Double> TFIDFMap = new HashMap<>();
		Map<String, Integer> tf = getTermCount(fragment);
		Directory directory = new NIOFSDirectory(file);
		IndexReader reader = DirectoryReader.open(directory);
		int df, numDocs;
		double tfidf;
		for (Entry<String, Integer> entry : tf.entrySet()) {
			df = reader.docFreq(new Term(TEXT, entry.getKey()));
			numDocs = reader.numDocs();
			tfidf = calculateLogTF(entry.getValue()) * calculateIDF(numDocs, df);
			TFIDFMap.put(entry.getKey(), tfidf);
		}
		reader.close();
		directory.close();
		List<Entry<String, Double>> list = TFIDFMap.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toList());
		TFIDFMap = new HashMap<String, Double>();
		int size = list.size();
		for (int i = 0; i < size && i < significativeTerms; i++) {
			TFIDFMap.put(list.get(i).getKey(), list.get(i).getValue());
		}
		terms.put(id, TFIDFMap);
		return TFIDFMap;
	}

	public Map<String, Double> getTFIDF(Fragment fragment, Collection<String> terms)
			throws IOException, ParseException {
		Map<String, Double> TFIDFMap = new HashMap<>(getTFIDF(fragment));
		TFIDFMap.keySet().retainAll(terms);
		return TFIDFMap;
	}

	private double calculateIDF(int numDocs, int df) {
		return Math.log10(numDocs / (double) df);
	}

	private double calculateSmoothIDF(int numDocs, int df) {
		return Math.log10(1 + (numDocs / (double) df));
	}

	private double calculateLogTF(Integer value) {
		return 1 + Math.log10(value);
	}

	private double calculateRawTF(Integer value) {
		return value;
	}

	private double calculateBooleanTF(Integer value) {
		return 1;
	}

	private Map<String, Integer> getTermCount(Fragment fragment) throws IOException, ParseException {
		Map<String, Integer> TFMap = new TreeMap<String, Integer>();
		Directory directory = new NIOFSDirectory(file);
		IndexReader reader = DirectoryReader.open(directory);
		String queryString;
		Query query;
		IndexSearcher searcher;
		int hitsPerPage, tf, docId, i, len;
		TopScoreDocCollector collector;
		ScoreDoc[] hits;
		Terms terms;
		TermsEnum termsEnum;
		BytesRef text;
		String term;
		for (FragInter fragInter : fragment.getFragmentInterSet()) {
			queryString = ID + ":" + fragInter.getExternalId();
			query = queryParser.parse(queryString);
			searcher = new IndexSearcher(reader);
			hitsPerPage = reader.numDocs();
			collector = TopScoreDocCollector.create(hitsPerPage, true);
			searcher.search(query, collector);
			hits = collector.topDocs().scoreDocs;
			len = hits.length;
			for (i = 0; i < len; ++i) {
				docId = hits[i].doc;
				terms = reader.getTermVector(docId, TEXT);
				if (terms == null)
					return TFMap;
				termsEnum = null;
				termsEnum = terms.iterator(termsEnum);
				text = null;
				while ((text = termsEnum.next()) != null) {
					term = text.utf8ToString();
					tf = (int) termsEnum.totalTermFreq();
					if (TFMap.containsKey(term)) {
						TFMap.put(term, TFMap.get(term) + tf);
					} else {
						TFMap.put(term, tf);
					}
				}
			}
		}
		reader.close();
		directory.close();
		return TFMap;
	}

	@Deprecated
	private Map<String, Integer> getTermCount(Fragment fragment, Collection<String> terms)
			throws IOException, ParseException {
		Map<String, Integer> termCount = getTermCount(fragment);
		termCount.keySet().retainAll(terms);
		return termCount;
	}

	@Deprecated
	public Collection<String> getTerms(Fragment fragment) throws IOException, ParseException {
		return getTFIDF(fragment).keySet();
	}

	public Collection<String> getTerms(Fragment fragment, int numberOfTerms) throws IOException, ParseException {
		Set<Entry<String, Double>> set = getTFIDF(fragment).entrySet();
		List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(set);
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		List<String> terms = new ArrayList<>();
		int size = list.size();
		for (int i = 0; i < size && i < numberOfTerms; i++) {
			terms.add(list.get(i).getKey());
		}
		return terms;
	}

	public Map<String, Double> getTFIDF(Source source, List<String> commonTerms) throws IOException, ParseException {
		Map<String, Double> TFIDFMap = new HashMap<>(getTFIDF(source));
		TFIDFMap.keySet().retainAll(commonTerms);
		return TFIDFMap;
	}

	private Map<String, Double> getTFIDF(Source source) throws IOException, ParseException {
		String id = source.getExternalId();
		if (terms.containsKey(id)) {
			return terms.get(id);
		}
		Map<String, Double> TFIDFMap = new HashMap<>();
		Map<String, Integer> tf = getTermCount(source);
		Directory directory = new NIOFSDirectory(file);
		IndexReader reader = DirectoryReader.open(directory);
		int df, numDocs;
		double tfidf;
		for (Entry<String, Integer> entry : tf.entrySet()) {
			df = reader.docFreq(new Term(TEXT, entry.getKey()));
			numDocs = reader.numDocs();
			tfidf = calculateLogTF(entry.getValue()) * calculateIDF(numDocs, df);
			TFIDFMap.put(entry.getKey(), tfidf);
		}
		reader.close();
		directory.close();
		List<Entry<String, Double>> list = TFIDFMap.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toList());
		TFIDFMap = new HashMap<String, Double>();
		int size = list.size();
		for (int i = 0; i < size && i < significativeTerms; i++) {
			TFIDFMap.put(list.get(i).getKey(), list.get(i).getValue());
		}
		terms.put(id, TFIDFMap);
		return TFIDFMap;
	}

	private Map<String, Integer> getTermCount(Source source) throws IOException, ParseException {
		Map<String, Integer> TFMap = new TreeMap<String, Integer>();
		Directory directory = new NIOFSDirectory(file);
		IndexReader reader = DirectoryReader.open(directory);
		String queryString;
		Query query;
		IndexSearcher searcher;
		int hitsPerPage, tf, docId, i, len;
		TopScoreDocCollector collector;
		ScoreDoc[] hits;
		Terms terms;
		TermsEnum termsEnum;
		BytesRef text;
		String term;
		for (FragInter fragInter : source.getSourceIntersSet()) {
			queryString = ID + ":" + fragInter.getExternalId();
			query = queryParser.parse(queryString);
			searcher = new IndexSearcher(reader);
			hitsPerPage = reader.numDocs();
			collector = TopScoreDocCollector.create(hitsPerPage, true);
			searcher.search(query, collector);
			hits = collector.topDocs().scoreDocs;
			len = hits.length;
			for (i = 0; i < len; ++i) {
				docId = hits[i].doc;
				terms = reader.getTermVector(docId, TEXT);
				if (terms == null)
					return TFMap;
				termsEnum = null;
				termsEnum = terms.iterator(termsEnum);
				text = null;
				while ((text = termsEnum.next()) != null) {
					term = text.utf8ToString();
					tf = (int) termsEnum.totalTermFreq();
					if (TFMap.containsKey(term)) {
						TFMap.put(term, TFMap.get(term) + tf);
					} else {
						TFMap.put(term, tf);
					}
				}
			}
		}
		reader.close();
		directory.close();
		return TFMap;
	}

	private Map<String, Double> getTFIDF(VirtualEditionInter virtualEditionInter, List<String> commonTerms)
			throws IOException, ParseException {
		Map<String, Double> TFIDFMap = new HashMap<>(getTFIDF(virtualEditionInter.getUses().getExternalId()));
		TFIDFMap.keySet().retainAll(commonTerms);
		return TFIDFMap;
	}

	private Map<String, Double> getTFIDF(SourceInter sourceInter, List<String> commonTerms)
			throws IOException, ParseException {
		Map<String, Double> TFIDFMap = new HashMap<>(getTFIDF(sourceInter.getExternalId()));
		TFIDFMap.keySet().retainAll(commonTerms);
		return TFIDFMap;
	}

	public Map<String, Double> getTFIDF(FragInter fragInter, List<String> commonTerms)
			throws IOException, ParseException {
		Map<String, Double> TFIDFMap = new HashMap<String, Double>(getTFIDF(fragInter.getExternalId()));
		TFIDFMap.keySet().retainAll(commonTerms);
		return TFIDFMap;
	}

	private Map<String, Double> getTFIDF(String id) throws IOException, ParseException {
		if (terms.containsKey(id)) {
			return terms.get(id);
		}
		Map<String, Double> TFIDFMap = new HashMap<>();
		Map<String, Integer> tf = getTermCount(id);
		Directory directory = new NIOFSDirectory(file);
		IndexReader reader = DirectoryReader.open(directory);
		int df, numDocs;
		double tfidf;
		for (Entry<String, Integer> entry : tf.entrySet()) {
			df = reader.docFreq(new Term(TEXT, entry.getKey()));
			numDocs = reader.numDocs();
			tfidf = calculateLogTF(entry.getValue()) * calculateIDF(numDocs, df);
			TFIDFMap.put(entry.getKey(), tfidf);
		}
		reader.close();
		directory.close();
		List<Entry<String, Double>> list = TFIDFMap.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toList());
		TFIDFMap = new HashMap<String, Double>();
		int size = list.size();
		for (int i = 0; i < size && i < significativeTerms; i++) {
			TFIDFMap.put(list.get(i).getKey(), list.get(i).getValue());
		}
		terms.put(id, TFIDFMap);
		return TFIDFMap;
	}

	private Map<String, Integer> getTermCount(String id) throws ParseException, IOException {
		Map<String, Integer> TFMap = new TreeMap<String, Integer>();
		Directory directory = new NIOFSDirectory(file);
		IndexReader reader = DirectoryReader.open(directory);
		String queryString;
		Query query;
		IndexSearcher searcher;
		int hitsPerPage, tf, docId, i, len;
		TopScoreDocCollector collector;
		ScoreDoc[] hits;
		Terms terms;
		TermsEnum termsEnum;
		BytesRef text;
		String term;
		queryString = ID + ":" + id;
		query = queryParser.parse(queryString);
		searcher = new IndexSearcher(reader);
		hitsPerPage = reader.numDocs();
		collector = TopScoreDocCollector.create(hitsPerPage, true);
		searcher.search(query, collector);
		hits = collector.topDocs().scoreDocs;
		len = hits.length;
		for (i = 0; i < len; ++i) {
			docId = hits[i].doc;
			terms = reader.getTermVector(docId, TEXT);
			if (terms == null)
				return TFMap;
			termsEnum = null;
			termsEnum = terms.iterator(termsEnum);
			text = null;
			while ((text = termsEnum.next()) != null) {
				term = text.utf8ToString();
				tf = (int) termsEnum.totalTermFreq();
				if (TFMap.containsKey(term)) {
					TFMap.put(term, TFMap.get(term) + tf);
				} else {
					TFMap.put(term, tf);
				}
			}
		}
		reader.close();
		directory.close();
		return TFMap;
	}

	public Collection<String> getTerms(Source source, int numberOfTerms) throws IOException, ParseException {
		Set<Entry<String, Double>> set = getTFIDF(source).entrySet();
		List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(set);
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		List<String> terms = new ArrayList<>();
		int size = list.size();
		for (int i = 0; i < size && i < numberOfTerms; i++) {
			terms.add(list.get(i).getKey());
		}
		return terms;
	}

	public Collection<? extends String> getTerms(FragInter inter, int numberOfTerms)
			throws IOException, ParseException {
		String id;
		if (inter.getEdition().getSourceType().equals(EditionType.VIRTUAL)) {
			id = inter.getLastUsed().getExternalId();
		} else {
			id = inter.getExternalId();
		}
		Set<Entry<String, Double>> set = getTFIDF(id).entrySet();
		List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(set);
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		List<String> terms = new ArrayList<>();
		int size = list.size();
		for (int i = 0; i < size && i < numberOfTerms; i++) {
			terms.add(list.get(i).getKey());
		}
		return terms;
	}

	public void cleanMissingHits(List<String> misses) {
		if (misses.isEmpty()) {
			return;
		}

		String query = misses.get(0);
		for (int i = 1; i < misses.size(); i++) {
			query += " OR " + misses.get(i);
		}

		QueryParser idQueryParser = new QueryParser(ID, analyzer);
		try {
			Query q = idQueryParser.parse(query);
			Directory directory = new NIOFSDirectory(file);
			IndexWriter indexWriter = new IndexWriter(directory, config);
			indexWriter.deleteDocuments(q);

			indexWriter.close();
			directory.close();
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}

	public void cleanLucene() {
		String path = PropertiesManager.getProperties().getProperty("indexer.dir");
		try {
			logger.debug("cleanLucene {}", path);
			FileUtils.cleanDirectory(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}