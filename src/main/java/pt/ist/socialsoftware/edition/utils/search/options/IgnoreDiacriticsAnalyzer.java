package pt.ist.socialsoftware.edition.utils.search.options;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;

public class IgnoreDiacriticsAnalyzer extends Analyzer {

	private final CharArraySet stopWords;

	public IgnoreDiacriticsAnalyzer() {
		this.stopWords = PortugueseAnalyzer.getDefaultStopSet();
	}

	public IgnoreDiacriticsAnalyzer(CharArraySet stopWords) {
		this.stopWords = stopWords;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		Tokenizer source = new StandardTokenizer(reader);
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
