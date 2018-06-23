package pt.ist.socialsoftware.edition.ldod.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class IgnoreDiacriticsAnalyzer extends Analyzer {

	private final CharArraySet stopWords;

	public IgnoreDiacriticsAnalyzer() {
		this.stopWords = PortugueseAnalyzer.getDefaultStopSet();
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer source = new StandardTokenizer();
		TokenStream tokenStream = source;
		tokenStream = new StandardFilter(tokenStream);
		tokenStream = new LowerCaseFilter(tokenStream);
		tokenStream = new StopFilter(tokenStream, getStopwordSet());
		tokenStream = new ASCIIFoldingFilter(tokenStream);
		return new TokenStreamComponents(source, tokenStream);
	}

	private CharArraySet getStopwordSet() {
		return stopWords;
	}

}
