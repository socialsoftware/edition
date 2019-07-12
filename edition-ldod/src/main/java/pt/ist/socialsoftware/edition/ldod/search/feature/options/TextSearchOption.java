package pt.ist.socialsoftware.edition.ldod.search.feature.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.search.api.dto.SearchableElementDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;

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
    public Stream<SearchableElementDto> search(Stream<SearchableElementDto> inters) {
        Set<ScholarInterDto> searchResult = new HashSet<>(search());
        Set<String> resultIds = searchResult.stream().map(scholarInterDto -> scholarInterDto.getXmlId()).collect(Collectors.toSet());
        return inters.filter(i -> resultIds.contains(i.getXmlId()));
    }

    public List<ScholarInterDto> search() {
        if (this.text != null) {
            SearchRequiresInterface searchRequiresInterface = new SearchRequiresInterface();
            return searchRequiresInterface.searchScholarInterForWords(this.text);
        }

        return new ArrayList<>();
    }

}
