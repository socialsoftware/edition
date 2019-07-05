package pt.ist.socialsoftware.edition.ldod.search.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.ScholarInter;
import pt.ist.socialsoftware.edition.ldod.search.Indexer;
import pt.ist.socialsoftware.edition.ldod.search.SearchableElement;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TextSearchOption extends SearchOption {
    private static final Logger logger = LoggerFactory.getLogger(TextSearchOption.class);

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
        return "TextModule:" + this.text;
    }

    @Override
    public Stream<SearchableElement> search(Stream<SearchableElement> inters) {
        Set<ScholarInter> searchResult = new HashSet<>(search());
        Set<String> resultIds = searchResult.stream().map(scholarInter -> scholarInter.getXmlId()).collect(Collectors.toSet());
        return inters.filter(i -> resultIds.contains(i.getXmlId()));
    }

    public List<ScholarInter> search() {
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

    public List<ScholarInter> getFragIntersAndCleanMissingHits(List<String> hits) {
        List<ScholarInter> result = new ArrayList<>();
        List<String> misses = new ArrayList<>();
        for (String hit : hits) {
            try {
                DomainObject object = FenixFramework.getDomainObject(hit);
                if (!FenixFramework.isDomainObjectValid(object)) {
                    misses.add(hit);
                } else if (!(object instanceof ScholarInter)) {
                    misses.add(hit);
                } else {
                    ScholarInter inter = (ScholarInter) object;
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
