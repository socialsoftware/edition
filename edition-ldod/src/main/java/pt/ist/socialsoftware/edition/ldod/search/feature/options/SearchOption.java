package pt.ist.socialsoftware.edition.ldod.search.feature.options;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pt.ist.socialsoftware.edition.ldod.search.api.dto.SearchableElementDto;

import java.util.stream.Stream;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@Type(value = EditionSearchOption.class, name = SearchOption.EDITION),
        @Type(value = ManuscriptSearchOption.class, name = SearchOption.MANUSCRIPT),
        @Type(value = TypescriptSearchOption.class, name = SearchOption.DACTILOSCRIPT),
        @Type(value = PublicationSearchOption.class, name = SearchOption.PUBLICATION),
        @Type(value = HeteronymSearchOption.class, name = SearchOption.HETERONYM),
        @Type(value = DateSearchOption.class, name = SearchOption.DATE),
        @Type(value = TaxonomySearchOption.class, name = SearchOption.TAXONOMY),
        @Type(value = TextSearchOption.class, name = SearchOption.TEXT),
        @Type(value = VirtualEditionSearchOption.class, name = SearchOption.VIRTUALEDITION)})
public abstract class SearchOption {
    /* Json Properties */
    public static final String EDITION = "edition";
    public static final String MANUSCRIPT = "manuscript";
    public static final String DACTILOSCRIPT = "dactiloscript";
    public static final String PUBLICATION = "publication";
    public static final String HETERONYM = "heteronym";
    public static final String DATE = "date";
    public static final String TEXT = "text";
    public static final String TAXONOMY = "taxonomy";
    public static final String VIRTUALEDITION = "virtualedition";

    /* SearchProcessor options to include everything */
    public static final String ALL = "all";

    /* SearchProcessor modes */
    public enum Mode {
        AND("and"), OR("or");

        private final String mode;

        private Mode(String mode) {
            this.mode = mode;
        }

        public String getMode() {
            return this.mode;
        }
    }

    public abstract Stream<SearchableElementDto> search(Stream<SearchableElementDto> inters);

    @Override
    public abstract String toString();
}