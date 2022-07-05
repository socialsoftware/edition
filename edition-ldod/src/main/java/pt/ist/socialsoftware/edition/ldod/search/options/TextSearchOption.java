package pt.ist.socialsoftware.edition.ldod.search.options;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.search.Indexer;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

public final class TextSearchOption extends SearchOption {
	private static Logger logger = LoggerFactory.getLogger(TextSearchOption.class);

	private final String text;

	public TextSearchOption(@JsonProperty("text") String text) {
		text = purgeSearchText(text);
		text = QueryParser.escape(text);
		this.text = text.equals("null") || text.equals("") ? null : text.trim();
	}

	public static String purgeSearchText(String text) {
		text = text.replaceAll("[^\\p{L}0-9\\-\\s]+", "");
		return text;
	}

	@Override
	public String toString() {
		return "Text:" + this.text;
	}

	@Override
	public Set<FragInter> search(Set<FragInter> inters) {
		Set<FragInter> searchResult = new HashSet<>(search());
		return inters.stream().filter(i -> searchResult.contains(i)).collect(Collectors.toSet());
	}

	public List<FragInter> search() {
		List<String> hits = new ArrayList<>();
		if (this.text != null) {
			Indexer indexer = Indexer.getIndexer();
			try {
				hits = indexer.search(this.text);
				logger.debug("search hits for:{} size:{}", this.text, hits.size());
			} catch (ParseException | IOException e) {
				throw new LdoDException("Error associated with textual search on Lucene");
			}
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
				} else if (!(object instanceof FragInter)) {
					misses.add(hit);
				} else {
					FragInter inter = (FragInter) object;
					result.add(inter);
				}
			} catch (InstantiationError e) {
				misses.add(hit);
			}
		}

		if (!misses.isEmpty()) {
			Indexer indexer = Indexer.getIndexer();
			indexer.cleanMissingHits(misses);
		}

		return result;
	}

}
