package pt.ist.socialsoftware.edition.search.options;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.search.Indexer;

public final class TextSearchOption extends SearchOption {

	private final String text;

	public TextSearchOption(@JsonProperty("text") String text) {
		this.text = text.equals("null") || text.equals("") ? null : text.trim();
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

	private boolean containsText(FragInter inter) {
		if (text != null) {
			try {
				List<String> search = (new Indexer()).search(text, inter);
				return search.size() > 0;
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public List<FragInter> search() {
		List<String> hits = new ArrayList<>();
		Indexer indexer = new Indexer();
		try {
			hits = indexer.search(text);
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return getFragIntersAndCleanMissingHits(hits);
	}

	public List<FragInter> getFragIntersAndCleanMissingHits(List<String> hits) {
		List<FragInter> result = new ArrayList<>();
		List<String> misses = new ArrayList<>();
		for (String hit : hits) {
			try {
				DomainObject object = FenixFramework.getDomainObject(hit);
				if (!FenixFramework.isDomainObjectValid(object)) {
					misses.add(hit);
					break;
				}
				if (!(object instanceof FragInter)) {
					misses.add(hit);
					break;
				}

				FragInter inter = (FragInter) object;
				result.add(inter);

			} catch (InstantiationError e) {
				misses.add(hit);
			}
		}

		if (!misses.isEmpty()) {
			Indexer indexer = new Indexer();
			indexer.cleanMissingHits(misses);
		}

		return result;
	}

}
