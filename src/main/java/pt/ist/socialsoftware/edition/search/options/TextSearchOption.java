package pt.ist.socialsoftware.edition.search.options;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.codehaus.jackson.annotate.JsonProperty;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.search.Indexer;

public final class TextSearchOption extends SearchOption {

	private final String text;

	public TextSearchOption(@JsonProperty("text") String text) {
		this.text = text.equals("null") || text.equals("") ? null : text;
	}

	@Override
	public String toString() {
		return "Text:" + text;
	}

	@Override
	public boolean visit(SourceInter inter) {
		return containsText(inter);
	}

	@Override
	public boolean visit(VirtualEditionInter inter) {
		return false;
	}

	@Override
	public boolean visit(ExpertEditionInter inter) {
		return containsText(inter);
	}

	private boolean containsText(FragInter inter){
		if(text != null) {
			try {
				List<String> search = (new Indexer()).search(text, inter);
				return search.size() > 0;
			} catch(IOException | ParseException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
