package pt.ist.socialsoftware.edition.notification.dtos.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TextSearchOptionDto extends SearchOptionDto {
    private static final Logger logger = LoggerFactory.getLogger(TextSearchOptionDto.class);

    private final String text;

    public TextSearchOptionDto(@JsonProperty("text") String text) {
        text = purgeSearchText(text);
        text = QueryParser.escape(text);
        this.text = text.equals("null") || text.equals("") ? null : text.trim();
    }

    public static String purgeSearchText(String text) {
        text = text.replaceAll("[^\\p{L}0-9\\-\\s]+", "");
        return text;
    }


    public String getText() {
        return this.text;
    }

}
