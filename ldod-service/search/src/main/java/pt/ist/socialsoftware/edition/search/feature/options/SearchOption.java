package pt.ist.socialsoftware.edition.search.feature.options;

import pt.ist.socialsoftware.edition.search.api.dto.SearchableElementDto;

import java.util.stream.Stream;


public abstract class SearchOption {
    /* SearchProcessor options to include everything */
    public static final String ALL = "all";

    /* SearchProcessor modes */
    public enum Mode {
        AND("and"), OR("or");

        private final String mode;

        Mode(String mode) {
            this.mode = mode;
        }

        public String getMode() {
            return this.mode;
        }
    }

    public abstract Stream<SearchableElementDto> search(Stream<SearchableElementDto> inters);

}