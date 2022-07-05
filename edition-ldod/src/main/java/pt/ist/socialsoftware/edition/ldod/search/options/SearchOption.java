package pt.ist.socialsoftware.edition.ldod.search.options;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import pt.ist.socialsoftware.edition.ldod.domain.FragInter;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = EditionSearchOption.class, name = SearchOption.EDITION),
		@Type(value = ManuscriptSearchOption.class, name = SearchOption.MANUSCRIPT),
		@Type(value = TypescriptSearchOption.class, name = SearchOption.DACTILOSCRIPT),
		@Type(value = PublicationSearchOption.class, name = SearchOption.PUBLICATION),
		@Type(value = HeteronymSearchOption.class, name = SearchOption.HETERONYM),
		@Type(value = DateSearchOption.class, name = SearchOption.DATE),
		@Type(value = TaxonomySearchOption.class, name = SearchOption.TAXONOMY),
		@Type(value = TextSearchOption.class, name = SearchOption.TEXT),
		@Type(value = VirtualEditionSearchOption.class, name = SearchOption.VIRTUALEDITION) })
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

	/* Search options to include everything */
	public static final String ALL = "all";

	/* Search modes */
	public enum Mode {
		AND("and"), OR("or");

		private final String mode;

		private Mode(String mode) {
			this.mode = mode;
		}

		public String getMode() {
			return mode;
		}
	}

	public abstract Set<FragInter> search(Set<FragInter> inters);

	@Override
	public abstract String toString();
}