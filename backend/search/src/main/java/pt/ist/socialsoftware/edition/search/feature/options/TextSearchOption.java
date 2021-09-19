package pt.ist.socialsoftware.edition.search.feature.options;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.search.api.dto.SearchableElementDto;
import pt.ist.socialsoftware.edition.search.api.dto.TextSearchOptionDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TextSearchOption extends SearchOption {
    private static final Logger logger = LoggerFactory.getLogger(TextSearchOption.class);

    private final String text;

    public TextSearchOption(TextSearchOptionDto textSearchOptionDto) {
        this.text = textSearchOptionDto.getText();
    }

    public TextSearchOption(String search) {
        this.text = search;
    }

    public static String purgeSearchText(String text) {
        text = text.replaceAll("[^\\p{L}0-9\\-\\s]+", "");
        return text;
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
