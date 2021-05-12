package pt.ist.socialsoftware.edition.virtual.feature.socialaware;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
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
        tokenStream = new LowerCaseFilter(tokenStream);
        tokenStream = new StopFilter(tokenStream, getStopwordSet());
        tokenStream = new ASCIIFoldingFilter(tokenStream);
        return new TokenStreamComponents(source, tokenStream);
    }

    private CharArraySet getStopwordSet() {
        return this.stopWords;
    }

}
